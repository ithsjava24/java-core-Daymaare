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
        return getInstance(null);
    }

    public static Warehouse getInstance(String name) {
        return instances.computeIfAbsent(name != null ? name : "default", Warehouse::new);
    }

    public ProductRecord addProduct(UUID uuid, String name, Category category, BigDecimal price) {
        ProductRecord product = new ProductRecord(uuid, name, category, price);
        if(uuid == null) {
            uuid = UUID.randomUUID();
        }
        if (products.containsKey(uuid)) {
            throw new IllegalArgumentException("Product with that id already exists, use updateProduct for updates.");
        }
        products.put(uuid, product);
        //changedProducts.add(uuid);
        return product;
    }

    public boolean isEmpty() {
        return products.isEmpty();
    }

    public List<ProductRecord> getProducts() {
        return List.copyOf(products.values());
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
