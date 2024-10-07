package org.example.warehouse;

import java.math.BigDecimal;
import java.util.UUID;

public class ProductRecord {


    public record Product(UUID uuid, String product, BigDecimal price, int quantity) {
    }

    public static Product createProduct(UUID uuid, String productName, BigDecimal price, int quantity) {
        return new Product(uuid, productName, price, quantity);
    }
}