# CryptoWallet management proof-of-concept application

A proof-of-concept cryptowallet app based on Spring Boot 2 and related technologies

# Implementation details

## Domain Model

The specification document only included wallets, currencies/cryptocurrencies and prices. I decided to add users so the experience will be a little more realistic. Instead of creating cryptowallets out of thin air, i added the following requirements:

1. A **CryptoWallet** can only be created by a registered **User**.
2. Users have a unique USD based **Account** in which money can be added through a **Deposit**.
3. In order to buy cryptocurrencies and store them in cryptowallets, a **BuyOrder** must be executed.

The option suggested on the specification (adding values directly on a wallet's creation) is supported, but as a more real experience the flow deposit -> buy -> transfer is available.

To keep it simple, Users cannot be deleted or updated and only basic information is managed (first and last name, username and mail address).

## Resources

This is the organization of the REST resources:

users -> POST and GET operations to create and retrieve a User

users/{username}/deposits -> POST and GET operations to make a deposit in the user's account and list all deposits (paginated)

users/{username}/wallets -> GET operation only to list a user's cryptowallets

wallets -> POST, GET, PUT and DELETE operations to support all four CRUD operations on wallets.

wallets/{id}/in-orders -> POST and GET operations to execute a buy order or list all buy operations, respectively

wallets/{id}/out-orders -> POST and GET operations to execute a transfer order or list all transfer operations, respectively

cryptocurrencies -> GET operation to list all the available cryptocurrencies with their current prices (more on this later)

*(so basically i'm following a RESTful services approach)*

## Application layers

The implementation uses Spring Boot 2.3 (Spring 5.x) with a simplistic Controller -> Service -> Dao structure.

Dao's are implemented using in-memory collections instead of a DB-based persistence implementation, and no transactional support is provided (also entities are not annotated with JPA-related annotations).

The idea here is to use this three layers so things are kept separate and relatively ordered, and at the same time simplify or annotate all that is non essential.

The Rest Controller layer is fully implemented, including documentation with Swagger annotations.

Cryptocurrency's DAO encapsulates access to the CryptoCompare's API, but this is explained in the following section.

## CyptoCompare's API usage

As i previously pointed out, the usage of this API is hidden behind the CryptoCurrencies DAO. *While implementing the access to the available's cryptocurrencies list i encountered a problem*: The list of available coins returned more than 5000 records! On top of that, the records didn't include prices.
Considering that the delay in the response from the service and the number of records returned made impossible to retrieve all the information on every request to the DAO, i decided two things:

1. I will keep a copy of the coins list locally, refreshing it every 6 hours (i suppose that the number of coins does not change frequently)
2. The list will be accesed one page at a time, and only at the time of the page request will the prices be retrieved and added to the coin list.

The DAO component also exposed a **getRatio** method in order to execute buy orders (USD/EUR to BTC/ETH,etc) and transfer orders (BTC/ETH to BTC/ETH, etc, etc).

## Documentation (API)

Documentation is provided as part of the deployment on the path https://cryptowallet.cfapps.io/cryptowallet-app/swagger-ui.html

## Pagination

Pagination is implemented with:

1. **page** and **size** parameters on cryptocurrencies list, user deposits and wallets, and wallet's buy and transfer orders.

Example cryptocurrencies?page=1&size=20

**If no parameter is provided, a default value is used**

2. **A Page object** that is returned with the list of elements, the total numbers of elements in the resource and to properties indicating if there is a next and previous page.

With this minimal implementation a client can traverse through the entire data set without problems.

## Caching

A basic caching strategy is implemented on Spring, caching:

**Users**: User retrieval is cached, and a user's cache is invalidated if a deposit is made (it's account balance changes) or if a buy order is executed (same reason).

A user's deposit list is also cached, with the same rule: *if a new deposit is made it's cache is invalidated* (only for that user!)

**Wallets**, **Orders**, etc: *The logic is equivalent for wallets and their operations, taking into account that wallets can be updated or deleted* (obviously cache is invalidated in both scenarios).



