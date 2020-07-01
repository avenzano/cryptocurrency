package org.testapp.cryptowallet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.testapp.cryptowallet.model.CryptoCurrency;
import org.testapp.cryptowallet.pagination.Page;
import org.testapp.cryptowallet.service.CryptoCurrencyService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value="The CryptoCurrencies API controller", description = "The CryptoCurrencies API controller")
@RestController
@RequestMapping("/cryptocurrencies")
public class CryptoCurrencyController {

	@Autowired
	private CryptoCurrencyService cryptoCurrencyService;
	
	@ApiOperation(value="Retrieve all CryptoCurrencies with their respective prices (paginated)")
	@GetMapping
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successfully retrieved currency prices page!"),
			@ApiResponse(code = 400, message = "Page parameters wrong or problem accesing prices API")
	})
	private Page<CryptoCurrency> getCurrencies(
			@ApiParam(value = "Page requested")
			@RequestParam(value = "page", defaultValue = "1") int page,
			@ApiParam(value = "Maximum number of currencies to retrieve")
			@RequestParam(value = "size", defaultValue = "20") int size) {
		if ( page < 1 ) {
			throw new IllegalArgumentException("Requested page number cannot be inferior to 1");
		}
		if ( size > 100 ) {
			throw new IllegalArgumentException("You cannot request more than 100 coins per page"); 
		}
		int offset = (page - 1) * size;
		return cryptoCurrencyService.getAvailableCurrencies2(offset, size);
	}
	
}
