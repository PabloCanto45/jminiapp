package com.jminiapp.examples.order;

import com.jminiapp.core.engine.JMiniAppRunner;

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
