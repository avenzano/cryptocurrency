package org.testapp.cryptowallet.serializer;

import java.io.IOException;

import org.testapp.cryptowallet.model.CryptoWallet;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class WalletCurrenciesJsonSerializer extends JsonSerializer<CryptoWallet> {

	@Override
	public void serialize(CryptoWallet value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		// TODO Auto-generated method stub
		
	}

}
