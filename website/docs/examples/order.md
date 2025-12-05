# Order Simulation Application

A simple counter application demonstrating a little more complex lifecycle and state management of orders for a restaurant.

**Features:**
- Register new orders for the restaurant
- State persistence
- JSON import/export
- Interactive menu system on the terminal

**Source Code:** [examples/order](https://github.com/jminiapp/jminiapp/tree/main/examples/order)

### Key Concepts Demonstrated

- Application lifecycle (initialize, run, shutdown)
- In-memory state management
- JSON format adapter implementation
- Framework-based import/export

### Quick Start

```bash
--> On the root of the folder<-- 
mvn clean install
--> On the examples/order folder <--
mvn compile exec:java "-Dexec.mainClass=com.examples.order.OrderAppRunner"
```

## Code Highlights

### State Models:

Order
```java
public class Order
    {
    private String orderID;
    private String date;
    private List<Product> productList;
    private double totalAmount;


    public Order(String orderID, String date)
        {
        this.orderID = orderID;
        this.date = date;
        this.productList = new ArrayList<>();
        }

    public void addProduct(Product product)
        {
        this.productList.add(product);
        }

    public void calculateTotal()
        {
        for(Product selectedProduct : productList)
            {
            totalAmount += selectedProduct.getPrice();
            }
        }

    public void printTicket()
        {
        System.out.println("Order: " + orderID + " [" + date + "]");
        System.out.println("--------------------------------");
        for(Product selectedProduct : productList)
            {
            System.out.println("- " + selectedProduct.toString());
            }
        System.out.println("--------------------------------");
        System.out.println("TOTAL: $" + totalAmount);
        }

    public String getOrderID()
        {
        return orderID;
        }

    public String getDate()
        {
        return date;
        }

    public double getTotalAmount()
        {
        return totalAmount;
        }

    public List<Product> getProductList()
        {
        return productList;
        }
    }
```

Product
```java
public class Product
{
    private final String name;
    private final double price;

    public Product(String name, double cost)
        {
        this.name = name;
        this.price = cost;
        }
    public double getPrice()
        {
        return price;
        }

    public String getName()
        {
        return name;
        }

    @Override
    public String toString()
        {
        return name + " ($" + price + ")";
        }


    }
```
**OrdersApp**
```java
public class OrdersApp extends JMiniApp
    {
    private List<Order> ordersList;

    public OrdersApp(JMiniAppConfig config)
        {
        super(config);
        }

    @Override
    protected void initialize()
        {
        try
            {
            context.importData("orders.json", "json");
            }
        catch(Exception e)
            {
            System.out.println("File with previous orders not found, initializing a new List");
            }

        ordersList = context.getData();
        System.out.println("List with " + ordersList.size() + " orders obtained");
        }

    public enum menuOption
        {
        ADD,
        CONSULT,
        PRINT_ALL,
        DELETE,
        CLOSE
        }

    @Override
    protected void run()
        {
        boolean runnable = true;
        Scanner input = new Scanner(System.in);
        while(runnable)
            {
            System.out.println("====== ORDER APPLICATION ======");
            System.out.println("Select An Option:");
            System.out.println("1) Add order");
            System.out.println("2) Consult order");
            System.out.println("3) Print ALL tickets");
            System.out.println("4) Delete order");
            System.out.println("5) Close App");
            System.out.println("===============================");

            System.out.print("Option: ");

            int userOption = readSafeInt(input);
            input.nextLine();

            if (userOption < 1 || userOption > 5)
                {
                System.out.println("Invalid Option");
                continue;
                }

            menuOption optionSelected = menuOption.values()[userOption - 1];
            switch (optionSelected)
                {
                case ADD:
                    addOrder(input);
                    break;
                case CONSULT:
                    System.out.print("\nInsert Order ID to find: ");
                    String orderToSearch = input.nextLine();
                    consultOrder(orderToSearch);
                    break;
                case PRINT_ALL:
                    printAllOrders();
                    break;
                case DELETE:
                    System.out.print("\nInsert Order ID to find: ");
                    String orderToDelete = input.nextLine();
                    deleteOrder(orderToDelete);
                    break;
                case CLOSE:
                    runnable = false;
                    break;
                }

            }
        }

    @Override
    protected void shutdown()
        {
        context.setData(ordersList);
        try
            {
            context.exportData("orders.json", "json");
            System.out.println("List of Orders exported to orders.json");
            }
        catch (Exception e)
            {
            System.err.println("Failed to export: " + e.getMessage());
            }
        System.out.println("Goodbye user!");
        }

    // Subrutines
    public void addOrder(Scanner input)
        {
        String newId = generateNextOrderId();
        String currentDate = LocalDate.now().toString();

        Order newOrder = new Order(newId, currentDate);

        addProductsToOrder(input, newOrder);

        newOrder.calculateTotal();

        ordersList.add(newOrder);

        System.out.println(">> Order " + newId + " created successfully");
        newOrder.printTicket();
        }

    private String generateNextOrderId()
        {
        if (ordersList.isEmpty())
            {
            return "ORD-001";
            }

        int maxId = 0;

        for (Order order : ordersList)
            {
            // "ORD-XXX" Format
            try
                {
                String[] partes = order.getOrderID().split("-");
                if (partes.length < 2) continue; // Ignores incorrect IDs
                int idNum = Integer.parseInt(partes[1]);

                if (idNum > maxId)
                    {
                    maxId = idNum;
                    }
                }
            catch(NumberFormatException e)
                {
                continue;
                }
            }
        return String.format("ORD-%03d", maxId + 1);
        }

    public void addProductsToOrder(Scanner input, Order actualOrder)
        {
        boolean cont = true;

        while (cont)
            {
            System.out.println("\n--- MENU ---");
            Menu[] menu = Menu.values();

            for (int i = 0; i < menu.length; i++)
                {
                System.out.println((i + 1) + ") " + menu[i].getDetails());
                }
            System.out.println((menu.length + 1) + ") Stop Adding Products");

            System.out.print("Select a Product: ");
            int productOption = readSafeInt(input);
            input.nextLine();

            if (productOption == menu.length + 1)
                {
                cont = false;
                break;
                }

            if (productOption < 1 || productOption > menu.length)
                {
                System.out.println("Invalid Option!");
                continue;
                }

            System.out.print("Quantity: ");
            int quantity = readSafeInt(input);
            input.nextLine();

            Menu itemSelected = menu[productOption - 1];

            for (int quantityIndex = 0; quantityIndex < quantity; quantityIndex++)
                {
                Product newProduct = itemSelected.createProduct();
                actualOrder.addProduct(newProduct);
                }

            System.out.println(">> ADDED " + quantity + "x " + itemSelected.name() );
            }
        }

    public void consultOrder(String orderToSearch)
        {
        boolean orderFound = false;
        for(Order orderIndex : ordersList)
            {
            if(orderIndex.getOrderID().equals(orderToSearch) )
                {
                orderIndex.printTicket();
                orderFound = true;
                break;
                }
            }

        if(!orderFound)
            {
            System.out.println("Order not found!");
            }
        }

    public void deleteOrder(String orderToDelete)
        {
        boolean removed = ordersList.removeIf(order -> order.getOrderID().equals(orderToDelete));

        if (removed)
            {
            System.out.println("Order " + orderToDelete + " Deleted.");
            }
        else
            {
            System.out.println("Order not found.");
            }
        }

    private int readSafeInt(Scanner input)
        {
        while (true)
            {
            try
                {
                return input.nextInt();
                }
            catch (java.util.InputMismatchException e)
                {
                input.nextLine();
                System.out.print("Error: Invalid Number, try again: ");
                }
            }
        }

    public void printAllOrders()
        {
        System.out.println("\n====== GENERAL REPORT ======");

        if (ordersList.isEmpty())
            {
            System.out.println(">> No active orders in the system.");
            return;
            }

        for (Order order : ordersList)
            {
            order.printTicket();
            System.out.println("................................");
            }

        System.out.println(">> End of report (" + ordersList.size() + " orders listed).");
        }
    }
```

**OrderAppRunner:**
```java
public class OrderAppRunner
    {
    public static void main(String[] args)
        {
        JMiniAppRunner
                .forApp(OrdersApp.class)
                .withState(Order.class)
                .withAdapters(new OrderJSONAdapter())
                .run(args);
        }

    }

```

**Menu**
```java
public enum Menu
    {
    HAMBURGER("Hamburger", 85.00),
    PIZZA("Pepperoni Pizza", 120.00),
    TACOS("Order of Tacos(3)", 65.00),
    SODA("Can of Soda", 25.00);

    private final String name;
    private final double price;

    private Menu(String name, double price)
        {
        this.name = name;
        this.price = price;
        }

    public Product createProduct()
        {
        return new Product(this.name, this.price);
        }

    public String getDetails()
        {
        return name + " - $" + price;
        }

    }
```

**OrderJSONAdapter:**
```java
public class OrderJSONAdapter implements JSONAdapter<Order>
    {
    public OrderJSONAdapter()
        {
        super();
        }

    @Override
    public Class<Order> getstateClass()
        {
        return Order.class;
        }
    }
```


This simple example demonstrates the core concepts of JMiniApp: lifecycle management, state handling, and user interaction. The framework takes care of the infrastructure while you focus on building features.
Also, this code is pretty legible ngl (Author's note)