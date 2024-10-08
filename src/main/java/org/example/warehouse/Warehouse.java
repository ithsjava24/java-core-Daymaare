package org.example.warehouse;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class Warehouse {
    private static final Map<String, Warehouse> instances = new HashMap<>();
    private final String name;
    private final Map<UUID, ProductRecord> products = new ConcurrentHashMap<>();
    private final Set<UUID> changedProducts = ConcurrentHashMap.newKeySet();


    public static void main(String[] args) {
    }


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
        if(uuid == null) {
            uuid = UUID.randomUUID();
        }
        if (products.containsKey(uuid)) {
            throw new IllegalArgumentException("Product with that id already exists, use updateProduct for updates.");
        }
        ProductRecord product = new ProductRecord(uuid, name, category, price);
        products.put(uuid, product);
        return product;
    }

    public boolean isEmpty() {
        return products.isEmpty();
    }

    public List<ProductRecord> getProducts() {
        if(products.isEmpty()) {
            return Collections.emptyList();
        }
        return List.copyOf(products.values()).reversed();
    }

    public Optional<ProductRecord> getProductById(UUID uuid) {
        return Optional.ofNullable(products.get(uuid));
    }

    public void updateProductPrice(UUID uuid, BigDecimal newPrice) {
        if (newPrice == null || newPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }
        ProductRecord product = products.get(uuid);
        if (product == null) {
            throw new IllegalArgumentException("Product with that id doesn't exist.");
        }
        changedProducts.add(uuid);
        products.put(uuid, new ProductRecord(uuid, product.name(), product.category(), newPrice));
    }

    public List<ProductRecord> getChangedProducts() {
        return products.values().stream()
                .filter(product -> changedProducts.contains(product.uuid()))
                .collect(Collectors.toList());
    }


    public Map<Category, List<ProductRecord>> getProductsGroupedByCategories() {
        return products.values().stream()
                .collect(Collectors.groupingBy(ProductRecord::category));
    }

    public List<ProductRecord> getProductsBy(Category category) {
        return products.values().stream()
                .filter(product -> product.category().equals(category))
                .collect(Collectors.toList());
    }
}
