package org.testapp.cryptowallet.controller;

import static org.testapp.cryptowallet.pagination.PageUtils.createPage;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.testapp.cryptowallet.model.BuyOrder;
import org.testapp.cryptowallet.model.CryptoWallet;
import org.testapp.cryptowallet.model.InOrder;
import org.testapp.cryptowallet.model.OutOrder;
import org.testapp.cryptowallet.model.TransferOrder;
import org.testapp.cryptowallet.pagination.Page;
import org.testapp.cryptowallet.service.CryptoWalletService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "CryptoWallet operations controller", description = "CryptoWallet CRUD Operations, buys and transfers")
@RestController
@RequestMapping("/wallets")
public class CryptoWalletController {

	@Autowired
	private CryptoWalletService walletService;
	
	@ApiOperation(value="Returns the CryptoWallet information for the given id", response=CryptoWallet.class)
	@GetMapping(path="/{id}")
	@ApiResponses({
		@ApiResponse(code=200, message = "CryptoWallet data successfully retrieved"),
		@ApiResponse(code=500, message = "An error occurred while retrieving the wallet's data")
	})
	public CryptoWallet getCryptoWallet(@PathVariable("id") String id) {
		// TODO use parameter to return full info ??
		CryptoWallet wallet = walletService.getById(id);
		return wallet;
	}
	
	@ApiOperation(value="Creates a new CryptoWallet for the specified user")
	@PostMapping
	@ApiResponses({
		@ApiResponse(code = 200, message = "CryptoWallet successfully created!"),
		@ApiResponse(code = 400, message = "Incorrect wallet's username provided or wrong values (coins or values)"),
		@ApiResponse(code = 500, message = "Problem generating the wallet (possibly id generation or persistence)")
	})
	public CryptoWallet saveCryptoWallet(@RequestBody CryptoWallet newWallet) {
		CryptoWallet wallet = walletService.create(newWallet);
		return wallet;
	}
	
	@ApiOperation(value="Replaces a given CryptoWallet info with the given values")
	@PutMapping
	@ApiResponses({
		@ApiResponse(code = 200, message = "Wallet succesfully updated!"),
		@ApiResponse(code = 400, message = "Incorrect username or invalid wallet's friendly name"),
		@ApiResponse(code = 500, message = "Problem storing the wallet")
	})
	public void updateWallet(
			@ApiParam("Wallet's data to be updated")
			@RequestBody CryptoWallet wallet) {
		walletService.update(wallet);
	}
	
	@ApiOperation(value="List the buy orders on the selected wallet (paginated)")
	@GetMapping(path="/{id}/in-orders")
	@ApiResponses({
		@ApiResponse(code = 200, message = "Buy orders successfully retrieved!"),
		@ApiResponse(code = 400, message = "Incorrect wallet's id (as part of the url)"),
		@ApiResponse(code = 500, message = "Problem retrieving the wallet's buy orders")
	})
	public Page<InOrder> getCryptoWalletDeposits(
			@ApiParam(value="The wallet's unique id")
			@PathVariable("id") String id,
			@RequestParam(value="page", defaultValue="1") int page,
			@ApiParam(value="The number of returned orders")
			@RequestParam(value="size", defaultValue = "10") int size) {
		List<InOrder> orders = walletService.getInOrdersById(id);
		return createPage(orders, page, size);
	}
	
	@ApiOperation(value="Execute a given buy order of a given cryptocurrency and add the values to the wallet")
	@PostMapping(path="/{id}/in-orders")
	@ApiResponses({
		@ApiResponse(code = 200, message = "Buy order successfully executed!"),
		@ApiResponse(code = 400, message = "Incorrect wallet's id, insufficient funds or problem getting trade ratio!"),
		@ApiResponse(code = 500, message = "Internal problem processing the order")
	})
	public BuyOrder executeBuyOrder(
			@ApiParam("The wallet's id")
			@PathVariable("id") String id,
			@ApiParam("Buy order data (Money currency, amount and target cryptocurrency)")
			@RequestBody BuyOrder order) {
		BuyOrder executedOrder = walletService.execute(id, order);
		return executedOrder;
	}
	
	@ApiOperation(value="List the transfer orders on the selected wallet (paginated)")
	@GetMapping(path="/{id}/out-orders")
	@ApiResponses({
		@ApiResponse(code = 200, message = "Transfer orders successfully retrieved!"),
		@ApiResponse(code = 400, message = "Incorrect wallet's id (as part of the url)"),
		@ApiResponse(code = 500, message = "Problem retrieving the wallet's transfer orders")
	})
	public Page<OutOrder> getWalletTransfers(
			@ApiParam(value = "The wallet's unique id")
			@PathVariable("id") String id,
			@ApiParam(value = "Page number to retrieve (defaults to 1)")
			@RequestParam(value="page", defaultValue="1") int page,
			@ApiParam(value="The number of returned orders")
			@RequestParam(value="size", defaultValue = "10") int size) {
		CryptoWallet wallet = walletService.getById(id);
		
		return createPage(wallet.getOutOrders(), page, size);
	}
	
	@ApiOperation(value="Execute a given transfer order from a cryptocurrency on this wallet to a cryptocurrency on a target wallet")
	@PostMapping(path="/{id}/out-orders")
	@ApiResponses({
		@ApiResponse(code = 200, message = "Transfer order successfully executed!"),
		@ApiResponse(code = 400, message = "Incorrect wallet's id, insufficient currencies or problem getting trade ratio!"),
		@ApiResponse(code = 500, message = "Internal problem processing the order")
	})
	public TransferOrder executeTransferOrder(
			@ApiParam(value = "The wallet's unique id")
			@PathVariable("id") String id, 
			@ApiParam("Transfer order data (Cryptocurrencies and amount to transfer)")
			@RequestBody TransferOrder order) {
		TransferOrder executedOrder = walletService.execute(id, order);
		return executedOrder;
	}
	
	@ApiOperation("Delete the wallet (if empty)")
	@DeleteMapping("/{id}")
	@ApiResponses({
		@ApiResponse(code = 200, message = "Wallet correctly deleted!"),
		@ApiResponse(code = 400, message = "Incorrect wallet's id or wallet not empty (values must be transferred before deletion)"),
		@ApiResponse(code = 500, message = "Internal problem deleting the wallet")
	})
	public CryptoWallet delete(
			@ApiParam("The id of the wallet to delete")
			@PathVariable(name = "id") String id) {
		return walletService.delete(id);
	}
	
}
