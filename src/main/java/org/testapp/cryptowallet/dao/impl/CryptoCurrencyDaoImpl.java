package org.testapp.cryptowallet.dao.impl;

import static java.util.Spliterator.ORDERED;
import static java.util.Spliterators.spliteratorUnknownSize;
import static java.util.stream.StreamSupport.stream;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.testapp.cryptowallet.cryptocompare.model.CoinData;
import org.testapp.cryptowallet.dao.CryptoCurrencyDao;
import org.testapp.cryptowallet.exception.MissingCoinDataException;
import org.testapp.cryptowallet.exception.MissingCoinDataException.ErrorType;
import org.testapp.cryptowallet.model.CryptoCurrency;
import org.testapp.cryptowallet.pagination.Page;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class CryptoCurrencyDaoImpl implements CryptoCurrencyDao {

	private List<CryptoCurrency> cachedCurrencies;
	
	private LocalDateTime lastAccess;
	
	@Value("${org.testapp.cryptowallet.coins.renew.time}")
	private int renewTime;
	
	@Value("${org.testapp.cryptowallet.api-key}")
	private String key;
	
	@Override
	public BigDecimal getRatio(String symbol1, String symbol2) {
		String pricesObj = getPrices(symbol1, symbol2);
		ObjectMapper om = new ObjectMapper();
		try {
			JsonNode root = om.readTree(pricesObj);
			checkError(root, symbol1, symbol2);
			String ratioStr = root.get(symbol1).get(symbol2).asText();
			return new BigDecimal(ratioStr);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Check if the node corresponds to an error on the API
	 * and throw an exception if it's the case.
	 * 
	 * @param root
	 * @param symbol2 
	 * @param symbol1 
	 */
	private void checkError(JsonNode root, String symbol1, String symbol2) {
		// if a Response field is present, an error occurred
		if ( !root.has("Response") ) {
			return;
		}
		String message = null;
		String paramWithError = null;
		if ( root.has("Message")) {
			message = root.get("Message").asText();
		}
		if ( root.has("ParamWithError")) {
			paramWithError = root.get("ParamWithError").asText();
		}
		String missingSymbol = paramWithError.equalsIgnoreCase("fsyms") ? symbol1 : symbol2;
		throw new MissingCoinDataException(missingSymbol, ErrorType.NO_RATIO, message);
	}

	@Override
	public List<CryptoCurrency> getAll1(int offset, int size) {
		// update crypto coins list from API if required
		List<CryptoCurrency> list = updateCoinList();
		// get 'slice' of coins
		List<CryptoCurrency> page = list.subList(offset, Math.min(list.size(), offset + size));
		// update the selected coins price
		setCryptoCurrencyPrices(page);
		
		return page;
	}
	
	private boolean shouldRenewCache() {
		return cachedCurrencies == null || LocalDateTime.now().plusHours(-6).isAfter(lastAccess);
	}
	
	private List<CryptoCurrency> updateCoinList() {
		if ( shouldRenewCache() ) {
			RestTemplate template = new RestTemplate();
			String url = "https://min-api.cryptocompare.com/data/all/coinlist?api_key="+key;
			ResponseEntity<String> response = template.exchange(url, HttpMethod.GET, null, String.class);
			
			List<CoinData> apiCoins = loadCoinsFromAPI(response.getBody());
			cachedCurrencies = new ArrayList<>();
			for (CoinData coinData : apiCoins) {
				CryptoCurrency cc = new CryptoCurrency(coinData.getId(), coinData.getCoinName(), coinData.getSymbol());
				cachedCurrencies.add(cc);
			}
			lastAccess = LocalDateTime.now();
		}
		return cachedCurrencies;
	}
	
	/**
	 * Get updated prices for the given {@link CryptoCurrency}'s
	 * 
	 * @param currencies
	 * @return
	 */
	private void setCryptoCurrencyPrices(List<CryptoCurrency> currencies) {
		Map<String, Map<String, BigDecimal>> id2Prices = currencies.stream().collect(Collectors.toMap(CryptoCurrency::getSymbol, CryptoCurrency::getPrices));
		// Note: since the API allows a list of CryptoCurrencies up to 300 characters, i need to split it if required
		StringBuilder strb = new StringBuilder();
		id2Prices.keySet().forEach( symbol -> {
			if ( strb.length() + symbol.length() + 1 > 300 ) {
				loadPricesFromAPI(strb.toString(), id2Prices);
				strb.setLength(0);
			}
			if ( strb.length() > 0 ) {
				strb.append(",");
			}
			strb.append(symbol);
		});
		if ( strb.length() > 0 ) {
			loadPricesFromAPI(strb.toString(), id2Prices);
		}
	}
	
	private String getPrices(String srcSymbols, String tgtSymbols) {
		RestTemplate template = new RestTemplate();
		String priceUrl = "https://min-api.cryptocompare.com/data/pricemulti?fsyms="+srcSymbols+"&tsyms="+tgtSymbols+"&api_key="+key;
		ResponseEntity<String> response = template.exchange(priceUrl, HttpMethod.GET, null, String.class);
		return response.getBody();
	}
	
	private void loadPricesFromAPI(String symbolList, Map<String, Map<String, BigDecimal>> prices) {
		RestTemplate template = new RestTemplate();
		String priceUrl = "https://min-api.cryptocompare.com/data/pricemulti?fsyms="+symbolList+"&tsyms=USD,EUR&api_key="+key;
		ResponseEntity<String> response = template.exchange(priceUrl, HttpMethod.GET, null, String.class);
		
		ObjectMapper om = new ObjectMapper();
		om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
			JsonNode root = om.readTree(response.getBody());
			Iterator<Entry<String, JsonNode>> fields = root.fields();
			while (fields.hasNext()) {
				Map.Entry<String, JsonNode> entry = fields.next();
				Map<String, BigDecimal> currPrice = prices.get(entry.getKey());
				if ( !currPrice.isEmpty() ) {
					currPrice.clear();
				}
				Iterator<Entry<String, JsonNode>> pricesIt = entry.getValue().fields();
				while (pricesIt.hasNext()) {
					Entry<String, JsonNode> pricePair = pricesIt.next();
					currPrice.put(pricePair.getKey(), new BigDecimal(pricePair.getValue().toString()));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private List<CoinData> loadCoinsFromAPI(String body) {
		List<CoinData> coins = new ArrayList<>();
		ObjectMapper om = new ObjectMapper();
		om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
			JsonNode root = om.readTree(body);
			JsonNode data = root.at("/Data");
			JSon2CoinData json2Coin = new JSon2CoinData(om);
			stream(spliteratorUnknownSize(data.elements(), ORDERED), false)
				.map(json2Coin)
				.forEach( coin -> coins.add(coin));
		} catch (IOException e) {
			throw new MissingCoinDataException("???", ErrorType.NO_COIN, "Coins information could not be retrieved from the API");
		}
		return coins;
	}

	@Override
	public Page<CryptoCurrency> getAll(int offset, int size) {
		List<CryptoCurrency> list = updateCoinList();
		int totalCoins = list.size();
		if ( offset >= list.size() ) {
			int lastPage = totalCoins % size == 0 ? (totalCoins / size) : (totalCoins / size)+1;
			throw new IllegalArgumentException("Request page does not exist (last page is "+lastPage+")"); 
		}
		// update crypto coins list from API if required
		// get 'slice' of coins
		List<CryptoCurrency> page = new ArrayList<CryptoCurrency>(list.subList(offset, Math.min(list.size(), offset + size)));
		// update the selected coins price
		setCryptoCurrencyPrices(page);
		
		return new Page<>(page, list.size(), offset, size);
	}

}

class JSon2CoinData implements Function<JsonNode, CoinData> {
	
	private ObjectMapper mapper;
	
	public JSon2CoinData(ObjectMapper objMapper) {
		this.mapper = objMapper;
	}

	@Override
	public CoinData apply(JsonNode t) {
		CoinData result = null;
		try {
			result = mapper.treeToValue(t, CoinData.class);
		} catch (JsonProcessingException e) {
			throw new MissingCoinDataException("???", ErrorType.NO_COIN, "An error occurred while parsing the crypto coin data ("+t.toPrettyString()+")");
		}
		return result;
	}
	
}
