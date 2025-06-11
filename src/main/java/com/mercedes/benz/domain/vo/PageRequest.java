package com.mercedes.benz.domain.vo;

public record PageRequest(int offset, int limit) {
    public PageRequest {
        if (offset < 0 || limit <= 0) {
            throw new IllegalArgumentException("Offset must be >= 0 and limit > 0");
        }
    }
}
