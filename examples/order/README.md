# Order Simulation Example

A simple order register application for a restaurant by using the JMiniApp framework.

## Overview

This example shows how to create a basic mini-app using JMiniApp core that shows a restaurant application to help take orders from the clients.

The user can register an order with a few selection of products, consult an existing order, show all the orders registered and delete orders through the terminal menu.

## Features

- **Add Order**: Register an order with a few preselected products.
- **Consult Order**: Consult the information of an Order.
- **Print All Tickets**: Print the information of all tickets.
- **Delete Order**: Delete the register of an order.
- **Persistent State**: The register of all the orders is kept in the application.

## Project Structure

```
order/
├── pom.xml
├── README.md
└── src/main/java/com/jminiapp/examples/order/
    ├── OrdersApp.java         # Main application class
    ├── OrderAppRunner.java    # Bootstrap configuration
    ├── Order.java             # Main model of the application
    ├── Product.java           # Auxiliary model for the order
    ├── Menu.java              # Auxiliary model for the order
    └── OrderJSONAdapter.java  # JSON format adapter
```

## Key Components

### Order
Model class that represents the principal object of the application:
- `addProduct(Product product)`: Add a product to the productList of the class.
- `calculateTotal()`: Calculates the total amount of the order.
- `printTicket()`: Prints all the information of the Order.

### OrderJSONAdapter
A format adapter that enables JSON import/export for `Order`; for this application, the main app has a `ordersList` to keep track of all the orders:
- Implements `JSONAdapter<Order>` from the framework.
- Registers with the framework during app bootstrap.
- Provides automatic serialization/deserialization.

### OrdersApp
The main application class that extends `JMiniApp` and implements:
- `initialize()`: Set up the app and load existing order list.
- `run()`: Main loop displaying menu and handling user input.
- `shutdown()`: Save the order list before exiting.
- Uses framework's `context.importData()` and `context.exportData()` for file operations.

### OrderAppRunner
Bootstrap configuration that:
- Registers the `OrderJSONAdapter` with `.withAdapters()`.
- Configures the app name and model class.
- Launches the application.

## Building and Running

### Prerequisites
- Java 17 or higher
- Maven 3.6 or higher

### Build the project

From the **project root** (not the examples/order directory):
```bash
mvn clean install
```

This will build both the jminiapp-core module and the order example.

### Run the application

Once you built the project, use the next command to execute the application on the examples/order carpet
```
mvn compile exec:java "-Dexec.mainClass=com.examples.order.OrderAppRunner"
```
Note: This command is considered to be used in the PowerShell Terminal


## Usage Example

### Basic Operations

```
List with 3 orders obtained
====== ORDER APPLICATION ======
Select An Option:
1) Add order
2) Consult order
3) Print ALL tickets
4) Delete order
5) Close App
===============================
Option: 1

--- MENU ---
1) Hamburger - $85.0
2) Pepperoni Pizza - $120.0
3) Order of Tacos(3) - $65.0
4) Can of Soda - $25.0
5) Stop Adding Products
Select a Product: 1
Quantity: 1
>> ADDED 1x HAMBURGER

--- MENU ---
1) Hamburger - $85.0
2) Pepperoni Pizza - $120.0
3) Order of Tacos(3) - $65.0
4) Can of Soda - $25.0
5) Stop Adding Products
Select a Product: 5
>> Order ORD-005 created successfully
Order: ORD-005 [2025-12-04]
--------------------------------
- Hamburger ($85.0)
--------------------------------
TOTAL: $85.0
====== ORDER APPLICATION ======
Select An Option:
1) Add order
2) Consult order
3) Print ALL tickets
4) Delete order
5) Close App
===============================
Option: 2

Insert Order ID to find: 3
Order not found!
====== ORDER APPLICATION ======
Select An Option:
1) Add order
2) Consult order
3) Print ALL tickets
4) Delete order
5) Close App
===============================
Option: 3

====== GENERAL REPORT ======
Order: ORD-001 [2025-12-04]
--------------------------------
- Hamburger ($85.0)
- Can of Soda ($25.0)
--------------------------------
TOTAL: $110.0
................................
Order: ORD-003 [2025-12-04]
--------------------------------
- Can of Soda ($25.0)
- Can of Soda ($25.0)
- Hamburger ($85.0)
- Pepperoni Pizza ($120.0)
--------------------------------
TOTAL: $255.0
................................
Order: ORD-004 [2025-12-04]
--------------------------------
- Order of Tacos(3) ($65.0)
- Order of Tacos(3) ($65.0)
- Order of Tacos(3) ($65.0)
--------------------------------
TOTAL: $195.0
................................
Order: ORD-005 [2025-12-04]
--------------------------------
- Hamburger ($85.0)
--------------------------------
TOTAL: $85.0
................................
>> End of report (4 orders listed).
====== ORDER APPLICATION ======
Select An Option:
1) Add order
2) Consult order
3) Print ALL tickets
4) Delete order
5) Close App
===============================
Option: 2

Insert Order ID to find: ORD-004
Order: ORD-004 [2025-12-04]
--------------------------------
- Order of Tacos(3) ($65.0)
- Order of Tacos(3) ($65.0)
- Order of Tacos(3) ($65.0)
--------------------------------
TOTAL: $195.0
====== ORDER APPLICATION ======
Select An Option:
1) Add order
2) Consult order
3) Print ALL tickets
4) Delete order
5) Close App
===============================
Option: 4

Insert Order ID to find: ORD-005
Order ORD-005 Deleted.
====== ORDER APPLICATION ======
Select An Option:
1) Add order
2) Consult order
3) Print ALL tickets
4) Delete order
5) Close App
===============================
Option: 5
List of Orders exported to orders.json
Goodbye user!
```

### Export to JSON


Because of the nature of the app, this app automatically imports the list of orders that already exists, and instantly export it when the app closes.


The exported JSON file will look like:
```json
[
  {
    "orderID": "ORD-001",
    "date": "2025-12-04",
    "productList": [
      {
        "name": "Hamburger",
        "price": 85.0
      },
      {
        "name": "Can of Soda",
        "price": 25.0
      }
    ],
    "totalAmount": 110.0
  },
  {
    "orderID": "ORD-003",
    "date": "2025-12-04",
    "productList": [
      {
        "name": "Can of Soda",
        "price": 25.0
      },
      {
        "name": "Can of Soda",
        "price": 25.0
      },
      {
        "name": "Hamburger",
        "price": 85.0
      },
      {
        "name": "Pepperoni Pizza",
        "price": 120.0
      }
    ],
    "totalAmount": 255.0
  },
  {
    "orderID": "ORD-004",
    "date": "2025-12-04",
    "productList": [
      {
        "name": "Order of Tacos(3)",
        "price": 65.0
      },
      {
        "name": "Order of Tacos(3)",
        "price": 65.0
      },
      {
        "name": "Order of Tacos(3)",
        "price": 65.0
      }
    ],
    "totalAmount": 195.0
  }
]
```

### Import from JSON

```
List with 3 orders obtained <--- Import
====== ORDER APPLICATION ======
Select An Option:
1) Add order
2) Consult order
3) Print ALL tickets
4) Delete order
5) Close App
===============================
Option: 1

====== ORDER APPLICATION ======
Select An Option:
1) Add order
2) Consult order
3) Print ALL tickets
4) Delete order
5) Close App
===============================
Option: 5
List of Orders exported to orders.json <--- Export
Goodbye user!
```

## Next Steps

You can try to expand this application by implementing some of the next ideas:
- Update the existing products of an order.
- Implement an order state (on wait, in process, ready, delivered)
- Implement a search by date function for the orders

## Author Data
Name: Pablo Gabriel Canto Pérez

Status: Tired af

Date: 05/12/2025

Version: 1.3 (idk ngl)

Project for: Software Architecture