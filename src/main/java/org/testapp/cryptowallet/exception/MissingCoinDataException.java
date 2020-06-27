package org.testapp.cryptowallet.exception;

/**
 * Data required from the CryptoCompare is unavailable (service unavailable or
 * simply no data).
 * 
 * @author avenzano
 */
public class MissingCoinDataException extends RuntimeException {

	public static enum ErrorType { NO_COIN, NO_PRICE, NO_RATIO, UNKNOWN }
	
	private static final long serialVersionUID = 1L;
	
	private String symbol;
	
	private ErrorType type;

	public MissingCoinDataException() {}
	
	public MissingCoinDataException(String symbol, ErrorType errType, String message) {
		super(message);
		this.symbol = symbol;
		this.type = errType;
	}
	
	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public ErrorType getType() {
		return type;
	}

	public void setType(ErrorType type) {
		this.type = type;
	}
	
}
