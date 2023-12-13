package com.unipi.vsmyris.mscjavaproject2;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        //new NewProductForm();
        DbProvider dbProvider = DbProvider.getInstance();

//        List<Product> products = new ArrayList<>();
//        products.add(new Product("23654tt1", "Product r", 52.0f, "good product", "various"));
//        products.add(new Product("2365d1", "Product 3", 52.0f, "good product", "various"));
//        products.add(new Product("2365Fs", "Product 2", 52.0f, "good product", "various"));
//        products.add(new Product("2365Fds", "Product 4", 52.0f, "good product", "various"));
//
//        dbProvider.insertNewBlock(products);
//        List<Product> results = dbProvider.selectAll();
        List<Product> results = dbProvider.getPriceHistory("23654tt1");

        for(Product r : results){
            System.out.println("\n" + r.toString());
        }

//        System.out.println("\n"+dbProvider.searchProduct("2365d1", true).toString());
//
//        System.out.println("\n"+dbProvider.searchProduct("2365d1", false).toString());

        dbProvider.close();
    }
}