package com.io.CoreBackend.order.entity;

public enum OrderStatus {
    PENDING,
    CONFIRMED,
    PREPARING,
    READY,
    CANCELLED;

    public boolean canTransitionTo(OrderStatus next) {
        switch (this) {
            case PENDING:
                return next == CONFIRMED || next == CANCELLED;
            case CONFIRMED:
                return next == PREPARING || next == CANCELLED;
            case PREPARING:
                return next == READY;
            case READY:
            case CANCELLED:
                return false;
            default:
                return false;
        }
    }
}