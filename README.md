# cafemanagement

A Cafeteria Management System built using Java, Spring Boot, Thymeleaf, PostgreSQL, Bootstrap, jQuery.

There are three personas in the system.

* Admin 
  * **Manage Users** - Add / Update / Remove Billing Clerks & other Admins.
  * **Manage Menus** - Add / Update / Remove Menus, activate / deactivate them & Add / Remove Menu Items for each menu.
  * **Manage Orders** - View all orders, their ratings and pending orders queue, mark pending orders as delivered / cancelled and place order for walk-in customer.
  * **Reports** - View Sale Reports filtered based on Date Range, Customer Name & Approver Name, take export of the filtered data in CSV format.
  * **Invoices** - View individual invoices and print them.

* Billing Clerk
  * **View Orders** - View only the pending orders queue, mark them as delivered / cancelled.
  * **Manage Orders** - Place order for walk-in customer.

* Customer
  * **User Profile Management** - Can signup, login, request for password reset.
  * **Add to Cart** - View list of active menus, add / remove quantities of each item to cart.
  * **Order Summary** - View summary of order and place order.
  * **Order History** - View all past orders, check the individual order details (items, quantities, status) and provide rating for the service.


# Configuration

## Requirements

* Java 8
* Maven 3.6.3
* PostgreSQL 13.2
* PGAdmin 4

## Installation (Local)

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
* Orders
* Reports (Overall)

All API calls require Basic Authentication in form of emailAddress and Password. APIs are available only to admins & billing clerks.

## Users

### Get all users:

Method: GET

```
/api/users
```

### View User: 

Method: GET

```
/api/users/{id}
```

### Create User

Method: POST

```
/api/users
```

Sample Payload:

```
{ 
"name":"Jon Snow",
"emailAddress":"jon@test.com",
"authority":"ROLE_CLERK"
}
```

1. `authority` field cannot take value `ROLE_CUSTOMER`. Accepted values are `ROLE_ADMIN` & `ROLE_CLERK`.

### Update User

Method: PUT

```
api/users/{id}
```

Sample Payload: 

```
{
"name":"Jon Snow",
"emailAddress":"jon2@test.com",
"authority":"ROLE_ADMIN"
}
```

1. `authority` field cannot take value `ROLE_CUSTOMER`. Accepted values are `ROLE_ADMIN` & `ROLE_CLERK`.

### Delete User

Method: DELETE

```
/api/users/{id}
```

1. Id cannot be the same user used for authentication.

## Menus

### List all menus: 

Method: GET

```
/api/menus
```

### View a menu: 

Method: GET

```
/api/menus/{id}
```

### Create a menu: 

Method: POST

```
/api/menus
```

Sample Payload: 

```
{
"name": "South Indian",
"description": "Traditional South Indian Menu",
"menu_items": [3, 5, 66],
"active":true
}
```

1. menu_items accepts an array of menu_item_ids and cannot be empty.
2. name, description and menu_items are mandatory.

### Update a menu: 

Method: PUT

```
/api/menus/{id}
```

Sample Payload:

```
{
"name": "South Indian 2",
"description": "Traditional South Indian Menu 2",
"menu_items": [3, 5],
"active":false
}
```
1. menu_items accepts an array of menu_item_ids and cannot be empty.

### Delete a menu:

Method: DELETE

```
/api/menus/{id}
```

## Menu Items:

### List all menu items: 

Method: GET

```
/api/menus/items
```

### View a menu item:

Method: GET

```
/api/menus/items/{id}
```

### Create a menu item: 

Method: POST
Content-Type: Multipart/Form-Data

```
/api/menus/items
```

Sample Payload:

```
{
name:"Idiyappam"
description:"A delicacy from Tamilnadu that's usually served as breakfast. It is made from rice flour, steam-cooked thus making it very light."
price:5.5
image:<Multipart-File>
}
```

1. Image should be a Multipart file. 
2. Accepted file types: [ jpg, jpeg, png, gif, bmp ]
3. All 4 parameters are mandatory

### Delete a menu item: 

Method: DELETE

```
/api/menus/items/{id}
```

## Orders

### View all orders: 

Method: GET

```
/api/orders
```

1. This will return all placed orders.
2. This endpoint will accept an optional flag/param `status`.
3. `status` can be `1, 2 or 3`.
4. The mapping is: `1 - Pending` , `2 - Approved` , `3 - Cancelled`.
5. If `status` is not passed, all placed orders will be returned. 

Sample:

```
/api/orders?status=1
```

This will list only the pending orders.

### Create an order: 

Method: POST

```
/api/orders
```

Sample Payload:

```
{
    "customerId": 23,
    "status": 1,
    "cartItemList":"7:1:11,8:5:11"
}
```

1. This will create an order with `status` set as 1, by default.
2. This endpoint will accept an optional flag/param `approved`.
3. `approved` can be `true` or `false`.
4. If `approved` is set as `true`, the order will be created with `status` set as `2` - approving it right away.
5. `cartItemList` will accept a comma-delimited string of pipe-delimited values in this format: `itemId:quantity:menuId,itemId2:quantity2:menuId2,...`
6. `customerId` and `status` are mandatory.

```
/api/orders?approved=true
```

### Approve an order: 

Method: POST

```
/api/orders/{id}/approve
```

1. Cancelled orders cannot be approved.

### Cancel an order:

Method: POST

```
/api/orders/{id}/cancel
```

2. Approved orders cannot be cancelled.

### Approve all orders: 

Method: POST

```
/api/orders/approve_all
```

1. All pending orders will be approved.

### Cancel all orders: 

Method: POST

```
/api/orders/cancel_all
```

2. All pending orders will be cancelled.

## Reports

### Generate Report:

Method: POST

```
/api/generate_report
```

1. This endpoint will accept a mandatory param `dateRange` in format `mm/dd/yyyy - mm/dd/yyyy` and two optional params `customerId` and `approverId`

Call like:

```
/api/generate_report?dateRange=03/31/2021 - 03/31/2021&customerId=1&approverId=2
```

Sample Response: 

```
{
    "totalProfit": "306.0",
    "totalOrders": "6"
}
```


# Accessing the application: 

After server startup, the application will be accessible from `http://localhost:8000/`. 
