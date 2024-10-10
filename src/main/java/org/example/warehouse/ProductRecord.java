package org.example.warehouse;

import java.math.BigDecimal;
import java.util.UUID;
import java.time.LocalDateTime;

public record ProductRecord(UUID uuid, String name, Category category, BigDecimal price, LocalDateTime updatedAt) {
    public ProductRecord {
        if (uuid == null) {
            uuid = UUID.randomUUID();
        }
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Product name can't be null or empty.");
        }
        if (category == null) {
            throw new IllegalArgumentException("Category can't be null.");
        }
        if (price == null) {
            price = BigDecimal.ZERO;
        }
        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }
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
