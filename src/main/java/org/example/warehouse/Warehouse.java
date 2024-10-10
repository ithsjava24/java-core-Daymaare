package org.example.warehouse;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class Warehouse {
    private static final Map<String, Warehouse> instances = new HashMap<>();
    private final String name;
    private final List<ProductRecord> products = new ArrayList<>();

    private Warehouse(String name) {
        this.name = name;
    }

    public static Warehouse getInstance() {
        return new Warehouse("Default");
    }

    public static Warehouse getInstance(String name) {
        if (instances.containsKey(name)) {
            return instances.get(name);
        } else {
            Warehouse instance = new Warehouse(name);
            instances.put(name, instance);
            return instance;
        }
    }

    public ProductRecord addProduct(UUID uuid, String name, Category category, BigDecimal price) {
        if (name == null || name.isEmpty()) throw new IllegalArgumentException("Product name can't be null or empty.");
        if (category == null) throw new IllegalArgumentException("Category can't be null.");
        uuid = Objects.requireNonNullElse(uuid, UUID.randomUUID());

        final UUID finalId = uuid;
        getProductByUuid(finalId).ifPresentOrElse(_ -> {
            throw new IllegalArgumentException("Product with that id already exists, use updateProduct for updates.");
        }, () -> products.add(new ProductRecord(finalId, name, category, Objects.requireNonNullElse(price, BigDecimal.ZERO), null)));

        return products.getLast();
    }

    public boolean isEmpty() {
        return products.isEmpty();
    }

    public List<ProductRecord> getProducts() {
        return List.copyOf(products);
    }


    public Optional<ProductRecord> getProductByUuid(UUID uuid) {
        List<ProductRecord> filteredProducts = products.stream().filter(product -> product.uuid().equals(uuid)).toList();
        if (filteredProducts.isEmpty()) return Optional.empty();
        return Optional.of(filteredProducts.getFirst());
    }

    public void updateProductPrice(UUID uuid, BigDecimal price) {
        getProductByUuid(uuid).ifPresentOrElse(product ->
                        products.set(products.indexOf(product),
                                new ProductRecord(product.uuid(),
                                        product.name(),
                                        product.category(),
                                        price,
                                        LocalDateTime.now()))
                , () -> {
                    throw new IllegalArgumentException("Product with that id doesn't exist.");
                });
    }

    public List<ProductRecord> getChangedProducts() {
        return products.stream().filter(product -> product.updatedAt() != null).toList();
    }

    public Map<Category, List<ProductRecord>> getProductsGroupedByCategories() {
        return products.stream()
                .collect(Collectors.groupingBy(ProductRecord::category));
    }

    public List<ProductRecord> getProductsBy(Category category) {
        return products.stream().filter(product -> product.category().equals(category)).toList();
    }
}

