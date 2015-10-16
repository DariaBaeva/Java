package com.example;


public class Product {
    private String name;
    private Integer sumPrices;
    private Integer number;

    public Product(String name, Integer price) {
        this.name = name;
        this.sumPrices = price;
        this.number = 1;
    }

    public void addPrice(Integer price) {
        sumPrices += price;
        number++;
    }

    public Double getPrice() {
        return (double) sumPrices / number;
    }

    public String getName() {
        return name;
    }
}
