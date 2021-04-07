# cafemanagement

A Cafeteria Management System built using Java, Spring Boot, Thymeleaf, PostgreSQL, Bootstrap, jQuery.

There are three personas in the system.

* Admin 
  * **Manage Users** - Add / Update / Remove Billing Clerks & other Admins.
  * **Manage Menus** - Add / Update / Remove Menus, activate / deactivate them & Add / Remove Menu Items for each menu.
  * **Manage Orders** - View all orders, their ratings and pending orders queue, mark pending orders as delivered / cancelled and place order for walk-in customer.
  * **Reports** - View Sale Reports based on Date Range, Customer Name & Approver Name
  * **Invoices** - View individual invoice and print them.

* Billing Clerk
  * **View Orders** - View only the pending orders queue, mark them as delivered / cancelled.
  * **Manage Orders** - Place order for walk-in customer.

* Customer
  * **User Profile Management** - can signup, login, request for password reset.
  * **Add to Cart** - View list of active menus, add / remove quantities of each item to cart.
  * **Order Summary** - View summary of order and place order.
  * **Order History** - View all past orders, check the individual order details (items, quantities, status) and provide rating for the service.


# Configuration

## Requirements

* Java 8
* Maven 3.6.3
* PostgreSQL 13.2
* PGAdmin 4

## Installation

### Setting up PostgreSQL:

Download Postgres 13.2 from [here](https://www.postgresql.org/download/macosx/)

(Optional) Download PGAdmin 4 from [here](https://www.pgadmin.org/download/pgadmin-4-macos/)

Add the PostgreSQL path to `.bash-profile` and load a new terminal window.

Connect to psql: 

```
sudo -u postgres psql
```

When prompted for the password for user `postgres`, enter the password specified during the PostgreSQL installation process.

Creating the database: 

```
create database <dbname>;
create user <user> with encrypted password '<password>';
grant all privileges on database <dbname> to <user>;
```

Add the respective username, dbname and password to the application.properties file under `main > src > resources` as: 

```
spring.datasource.url=jdbc:postgresql://localhost:<portno>/<dbname>
spring.datasource.username=<user>
spring.datasource.password=<password>
```

The <portno> is the number specified during the installation (default is 5432)

### Running through IDE (IntelliJ IDEA):

Import the project.

Setup Maven in IntelliJ.

Build and Run the project.

### Running from Terminal:

Navigate to the project folder

```
cd cafemgmt
```

Build project: 

```
mvn clean install -DskipTests
```

(Make sure Maven is installed and available)

Run project:

```
java -jar target/cafemgmt-0.0.1-SNAPSHOT.war
```

When the app is run for the first time, a default administrator will be created. Check the `ApplicationStartHandlerConfig.java` file for the credentials.

# REST API Support:

The application has REST API Support for the following entities:

* Users
* Menus
* Menu Items

# Accessing the application: 

After server startup, the application will be accessible from `http://localhost:8080/`. 
