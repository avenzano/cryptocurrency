package org.testapp.cryptowallet.exception;

import static org.springframework.util.StringUtils.isEmpty;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalControllerExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(InsufficientFundsException.class)
	public ResponseEntity<Object> insufficientFunds(InsufficientFundsException ex, WebRequest wr) {
		Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("error", HttpStatus.BAD_REQUEST.toString());
        body.put("message", "There are insufficient funds to perform the operation");
        if ( ! isEmpty(ex.getMessage()) ) {
        	body.put("detail", ex.getMessage());	
        }
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(MissingCoinDataException.class)
	public ResponseEntity<Object> dataError(MissingCoinDataException ex, WebRequest wr) {
		Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("error", HttpStatus.BAD_REQUEST.toString());
        body.put("message", "An error occured accesing the CryptoCompare API");
        body.put("coin symbol", ex.getSymbol());
        body.put("api message", ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<Object> dataError(IllegalArgumentException ex, WebRequest wr) {
		Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("error", HttpStatus.BAD_REQUEST.toString());
        body.put("message", ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
	}
}
