package com.jminiapp.examples.order;

import com.jminiapp.core.api.JMiniApp;
import com.jminiapp.core.api.JMiniAppConfig;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

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
