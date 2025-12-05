package com.jminiapp.examples.order;

import java.util.ArrayList;
import java.util.List;

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