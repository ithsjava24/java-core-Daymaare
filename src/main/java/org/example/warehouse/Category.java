package org.example.warehouse;

import java.math.BigDecimal;

public class Category {
    private final String name;

    private Category(String product) {
        this.name = product;
    }

    public static Category of(String product) {
        return new Category(product);
    }


    public String getName() {
        return name;
    }

}
