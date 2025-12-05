package com.jminiapp.examples.order;

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