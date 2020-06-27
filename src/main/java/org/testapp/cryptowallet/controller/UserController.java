package org.testapp.cryptowallet.controller;

import static org.testapp.cryptowallet.pagination.PageUtils.createPage;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.testapp.cryptowallet.model.CryptoWallet;
import org.testapp.cryptowallet.model.Deposit;
import org.testapp.cryptowallet.model.User;
import org.testapp.cryptowallet.pagination.Page;
import org.testapp.cryptowallet.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "User's administration controller", description = "User's administration controller")
@RestController
@RequestMapping(path = "/users")
public class UserController {

	@Autowired
	private UserService userService;
	
	@ApiOperation(value="Retrieve the user info for the given username", response = User.class)
	@GetMapping(path = "/{username}")
	@ApiResponses({
		@ApiResponse(code = 200, message = "User's data successfully retrieved!"),
		@ApiResponse(code = 400, message = "Username not known to the system"),
		@ApiResponse(code = 500, message = "Internal problem retrieving the user")
	})
	public User getById(
			@ApiParam(value="The username of the user", required = true)
			@PathVariable("username") String username) {
		return userService.get(username);
	}
	
	@ApiOperation(value="Creates a new User", response = User.class, consumes = "application/json", produces = "application/json")
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses({
		@ApiResponse(code = 200, message = "User successfully created!"),
		@ApiResponse(code = 400, message = "Invalid username or username in use"),
		@ApiResponse(code = 500, message = "Internal problem creating the user")
	})
	public User create(
			@ApiParam(value = "The new user's information") 
			@RequestBody User newUser) {
		User created = userService.signInUser(newUser);
		return created;
	}
	
	@ApiOperation(value="Add a new deposit to the User's account")
	@PostMapping("{username}/deposits")
	@ApiResponses({
		@ApiResponse(code = 200, message = "Deposit correctly executed!"),
		@ApiResponse(code = 400, message = "Incorrect username, unknown currency or problem calculating currency-to-USD ratio"),
		@ApiResponse(code = 500, message = "Internal problem executing the deposit")
	})
	public Deposit deposit(@ApiParam(name = "username", value = "The user's username")
			@PathVariable("username") String username,
			@ApiParam(value="The deposit's information (currency, amount)")
			@RequestBody Deposit deposit) {
		
		User user = userService.get(username);
		return userService.deposit(user, deposit);
	}
	
	@ApiOperation(value="List all the user's deposits (paginated)")
	@GetMapping(value = "{username}/deposits", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses({
		@ApiResponse(code = 200, message = "User deposits page successfully retrieved!"),
		@ApiResponse(code = 400, message = "Incorrect username"),
		@ApiResponse(code = 500, message = "Problem retrieving the user's deposits")
	})
	public Page<Deposit> getDeposits(
			@ApiParam(value = "User's username")
			@PathVariable("username") String id, 
			@ApiParam(value="The page number to list")
			@RequestParam(value="page", defaultValue="1") int page,
			@ApiParam(value="The number of returned deposits")
			@RequestParam(value="size", defaultValue="10") int size) {
//		User user = userService.get(id);
		List<Deposit> deposits = userService.getDeposits(id);
		return createPage(deposits, page, size);
	}
	
	@ApiOperation(value="List all the user's wallets (paginated)")
	@GetMapping(value = "{username}/wallets", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses({
		@ApiResponse(code = 200, message = "Wallet's page successfully retrieved!"),
		@ApiResponse(code = 400, message = "Incorrect username"),
		@ApiResponse(code = 500, message = "Problem retrieving the user's wallets")
	})
	public Page<CryptoWallet> getWallets(
			@ApiParam(value = "User's username")
			@PathVariable("username") String id, 
			@ApiParam(value="The page number to list")
			@RequestParam(value="page", defaultValue="1") int page,
			@ApiParam(value="The number of returned deposits")
			@RequestParam(value="size", defaultValue="10") int size) {
		User user = userService.get(id);
		return createPage(user.getWallets(), page, size);
	}

}
