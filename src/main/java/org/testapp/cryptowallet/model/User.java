package org.testapp.cryptowallet.model;

import java.time.LocalDate;
import java.util.List;

import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "The CryptoWallet's user")
public class User {

	@ApiModelProperty(required = true, value = "The user's document id", position = 1, example="11222333")
	private String id;
	
	@ApiModelProperty(value = "The user's username", notes = "The username must be unique", required = true, position = 2, example="johndoe")
	@Size(min = 8, max=30)
	private String username;
	
	@ApiModelProperty(required = true, value = "The user's first name (could be more than one)",
			example="John")
	private String firstName;
	
	@ApiModelProperty(required = true, value = "The user's last name (could be more than one)", 
			example="Doe")
	private String lastName;
	
	@ApiModelProperty(required = true, value = "The user's contact mail address", example = "johndoe@company.com")
	private String mailAdress;
	
	@ApiModelProperty(readOnly = true, value="The user's join date", example = "2020-01-20")
	private LocalDate joinDate;
	
	@ApiModelProperty(hidden = true)
	@JsonIgnore
	private List<CryptoWallet> wallets;
	
	@ApiModelProperty(value = "Account's information", readOnly = true)
	private Account account;
	
	@ApiModelProperty(hidden = true)
	@JsonIgnore
	private List<Deposit> deposits;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@JsonProperty
	public LocalDate getJoinDate() {
		return joinDate;
	}

	@JsonIgnore
	public void setJoinDate(LocalDate startDate) {
		this.joinDate = startDate;
	}
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getMailAdress() {
		return mailAdress;
	}

	public void setMailAdress(String mailAdress) {
		this.mailAdress = mailAdress;
	}

	@JsonProperty
	public Account getAccount() {
		return account;
	}

	@JsonIgnore
	public void setAccount(Account account) {
		this.account = account;
	}

	public List<CryptoWallet> getWallets() {
		return wallets;
	}

	public void setWallets(List<CryptoWallet> wallets) {
		this.wallets = wallets;
	}

	public List<Deposit> getDeposits() {
		return deposits;
	}

	public void setDeposits(List<Deposit> deposits) {
		this.deposits = deposits;
	}

	public void remove(CryptoWallet wallet) {
		wallets.remove(wallet);
	}
	
}
