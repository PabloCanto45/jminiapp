package com.jminiapp.examples.order;

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
