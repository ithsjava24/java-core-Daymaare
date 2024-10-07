package org.example.warehouse;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductRecord(UUID uuid, String name, Category category, BigDecimal price) {
    public ProductRecord {
        if (uuid == null) {
            throw new IllegalArgumentException();
        }
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException();
        }
        if (category == null) {
            throw new IllegalArgumentException();
        }
        if (price == null) {
            price = BigDecimal.ZERO; // Set price to zero if null
        }
        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public String toString() {
        return String.format("ProductRecord{uuid=%s, name='%s', category=%s, price=%s}",
                uuid, name, category.getName(), price);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductRecord that)) return false;
        return uuid.equals(that.uuid);
    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }
}
