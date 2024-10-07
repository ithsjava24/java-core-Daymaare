package org.example.warehouse;


import java.math.BigDecimal;
import java.util.*;

public class Warehouse {
    private final List<ProductRecord.Product> products;
    private static Warehouse instance;

    private Warehouse() {
        this.products = new ArrayList<>();
    }

    public static Warehouse getInstance(String myStore) {
        if (instance == null) {
            instance = new Warehouse();

        }
        return instance;
    }

    public boolean isEmpty() {
        return products.stream()
                .findAny()
                .isEmpty();
    }

    public List<ProductRecord.Product> getProducts() {
        return new ArrayList<>(products);
    }


    public ProductRecord.Product addProduct(UUID uuid, String productName, Category category, BigDecimal price, int quantity) {
        // Check if the product already exists using streams
        boolean exists = products.stream()
                .anyMatch(product -> product.product().equals(productName));

        if (!exists) {
            // Create and add the product only if it does not already exist
            ProductRecord.Product product = ProductRecord.createProduct(uuid, productName, price, quantity);
            products.add(product);
            return product; // Return the added product
        } else {
            // Optionally handle the case where the product already exists
            throw new IllegalArgumentException("Product already exists: " + productName);
        }
    }
//    public ProductRecord.Product addProduct(UUID uuid, String productName, BigDecimal price, int quantity) {
//        ProductRecord.Product product = ProductRecord.createProduct(uuid, productName, price, quantity);
//        products.add(product);
//        return product;
//    }






//    public boolean removeProduct(ProductRecord.Product product) {
//        return products.remove(product);
//    }


}






