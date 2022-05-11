# invoiceXpress challenge

## Client from the Bahamas

___

#### This is a challenge for invoiceExpress

    https://github.com/weareswat/challenges/blob/master/1-development/client-from-the-bahamas.md 

___

### Developed with:

* Java 17
* Maven 3.8.4
* Spring boot 2.6.7
* H2 (In memory RDBMS)
* Lombok
* OpenFeign
* Tests with JUnit and Mockito

### Install

___
This project uses Java and Maven
(See docker further below)

### Install

    mvn clean install


### Tests


    mvn tests

### Endpoints:

___ 

* Bahamas Tax Deductions API
  ###### (Bahamas External API - localhost:8082/register)
![](https://media0.giphy.com/media/aguNS13Fp2srFMMDbp/giphy.gif?cid=790b761143232fff3b73bdaa1cd12dd8903dff2b42ba337e&rid=giphy.gif&ct=g)
* Register Client
  ###### (Client Microservice - localhost:8081/client)
![](https://media1.giphy.com/media/Thq0hatwUPaSKqZ4Eg/giphy.gif?cid=790b76115c2ad4ff49d899f8ab30587a3d6176bc6f56cbeb&rid=giphy.gif&ct=g)
* Get Client By Fiscal Id
  ###### (Client Microservice - localhost:8081/client/fiscalId)
![](https://media0.giphy.com/media/ozYGhPvm5lESncz8oA/giphy.gif?cid=790b7611e7f6dc0bffc6e8ed55eabfc6b52d3ddca7d86f4f&rid=giphy.gif&ct=g)
* Retrieve Invoice
  ###### (Invoice Microservice - Main Microservice - localhost:8080/retrieve-bahamas-client)
![](https://media1.giphy.com/media/bxbH3amGd79Bqqvhmi/giphy.gif?cid=790b76118c0506e1c6a28475e8cf89ebc8de99cd4a1637b0&rid=giphy.gif&ct=g)
* Register Invoice
  ###### (Invoice Microservice - Main Microservice - http://localhost:8080/store-bahamas-client)
![](https://media4.giphy.com/media/RFya14lExDciw0QZkD/giphy.gif?cid=790b761172b18233c02b5271a506961dbb2445e5fa6a35dd&rid=giphy.gif&ct=g)

### RDBMS
___ 

Pré inserted registers:

    Invoice Table

    insert into invoice (invoice_id, client_fiscal_id) values (1, 59184); --client 1
    insert into invoice (invoice_id, client_fiscal_id) values (1, 405563); --client 2
    insert into invoice (invoice_id, client_fiscal_id) values (2, 564815); --client 3

___ 
    Client Table

    insert into client (id, name, email, fiscal_id) values (1, 'Demetria', 'dlooby0@jiathis.com', 59184);
    insert into client (id, name, email, fiscal_id) values (2, 'Eleni', 'erawcliffe1@flickr.com', 405563);
    insert into client (id, name, email, fiscal_id) values (3, 'Yoshi', 'yarrighini2@histats.com', 564815);
    insert into client (id, name, email, fiscal_id) values (4, 'Merla', 'mshurmore3@netscape.com', 561053);
    insert into client (id, name, email, fiscal_id) values (5, 'Torrey', 'thuriche4@accuweather.com', 322682);
    insert into client (id, name, email, fiscal_id) values (6, 'Petronella', 'pleckey5@last.fm', 322157);
    insert into client (id, name, email, fiscal_id) values (7, 'Norene', 'nleddy6@odnoklassniki.ru', 476771);
    insert into client (id, name, email, fiscal_id) values (8, 'Noach', 'nshervil7@abc.net.au', 149893);
    insert into client (id, name, email, fiscal_id) values (9, 'Grover', 'gmathieu8@cocolog-nifty.com', 208351);
    insert into client (id, name, email, fiscal_id) values (10, 'Brigit', 'bgascoyne9@ucoz.com', 299917);


## Running On Docker
___
    docker compose up

This will start 3 containers:
* Bahamas API (localhost::8082)
* Client Microservice (localhost::8081)
* Invoice Microservice (localhost::8080)

## Solutions
___ 
I made 3 microservices:

* Bahamas API
    * Simulating an External API from Bahamas Government Tax Dedutions
* Client Microservice

    * Internal or External APIs for creating and getting a client
* Invoice Microservice

    * The main microservice
    * Allows to register a client for a Invoice:
        * For invoices with one or no customers and avoiding duplicate records
        * For clients that exists in the Client Microservice
        * Submitting tax deductions to the Bahamas Government API after registering a customer on the invoice, just as requested
    * API to return clients by Invoice Id
