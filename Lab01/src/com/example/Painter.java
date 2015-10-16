package com.example;

import java.io.*;
import java.util.*;
import java.util.zip.DataFormatException;

public class Painter {
    private List<File> assortmentFiles = new ArrayList<>();
    private File colorsFile;
    private boolean isColorsExists = true;
    private Set<Color> colors = new HashSet<>();
    private Map<String, Product> products = new TreeMap<>();

    public Painter(String assortment, String colors) throws IOException {
        File assortmentDir = new File(assortment);
        if (!assortmentDir.exists() || !assortmentDir.isDirectory()) {
            throw new FileNotFoundException("assortment dir not exists");
        }
        File[] files = assortmentDir.listFiles();
        if (files == null) {
            throw new IOException("error while reading assortment dir");
        }
        for (File file : files) {
            if (file.isFile() && file.getName().endsWith(".txt")) {
                assortmentFiles.add(file);
            }
        }
        if (assortmentFiles.isEmpty()) {
            System.out.println("Error! Empty assortment folder");
            System.exit(0);
        }
        colorsFile = new File(colors);
        if (!colorsFile.exists() || !colorsFile.getName().equals("color.txt")) {
            isColorsExists = false;
        }
    }

    public void paint(String fileName) throws IOException, DataFormatException {
        readAssortment();
        readColors();
        generateResult(fileName);
    }

    private void readAssortment() throws IOException, DataFormatException {
        for (File file : assortmentFiles) {
            Set<String> productsInFile = new HashSet<>();
            BufferedReader in = new BufferedReader(new FileReader(file));
            String line;
            while ((line = in.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts.length != 2) {
                    continue;
                }
                Integer price;
                try {
                    price = Integer.parseInt(parts[1]);
                } catch (NumberFormatException e) {
                    continue;
                }
                String name = parts[0];
                if (!productsInFile.contains(name)) {
                    productsInFile.add(name);
                    Product p = products.get(name);
                    if (p != null) {
                        p.addPrice(price);
                    } else {
                        products.put(name, new Product(name, price));
                    }
                }
            }
        }
        if (products.isEmpty()) {
            System.out.println("Error! No correct data in assortment");
            System.exit(0);
        }
    }

    private void readColors() throws IOException {
        if (isColorsExists) {
            BufferedReader in = new BufferedReader(new FileReader(colorsFile));
            String line;
            while ((line = in.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts.length != 2) {
                    continue;
                }
                Integer price;
                try {
                    price = Integer.parseInt(parts[1]);
                } catch (NumberFormatException e) {
                    continue;
                }
                colors.add(new Color(parts[0], price));
            }
            in.close();
            if (colors.isEmpty()) {
                isColorsExists = false;
            }
        }
    }

    private void generateResult(String fileName) throws IOException {
        PrintWriter out = new PrintWriter(fileName);
        for (Product p : products.values()) {
            if (!isColorsExists) {
                printFormatted(out, p.getName() + " ", p.getPrice());
            } else {
                for (Color c : colors) {
                    double price = p.getPrice() + c.getPrice();
                    printFormatted(out, p.getName() + " " + c.getName() + " ", price);
                }
            }
        }
        if (out.checkError()) {
            throw new IOException("Error while writing result to file");
        }
        out.close();
    }

    private void printFormatted(PrintWriter out, String name, double price) {
        if (price == (int) price) {
            out.println(name + (int) price);
        } else {
            out.println(name + price);
        }
    }

}
