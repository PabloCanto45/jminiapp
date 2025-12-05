package com.jminiapp.examples.order;

import com.jminiapp.core.adapters.JSONAdapter;

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
