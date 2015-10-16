package com.example;

public class Color {
    private String name;
    private Integer price;

    public Color(String name, Integer price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }
}
