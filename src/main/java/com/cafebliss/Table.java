package com.cafebliss;

import java.time.LocalDateTime;

public class Table {
    private int tableNumber;
    private int capacity;
    private TableStatus status;
    private Order currentOrder;
    private LocalDateTime reservationTime;
    private String reservationName;

    public enum TableStatus {
        FREE("Free"),
        OCCUPIED("Occupied"),
        RESERVED("Reserved"),
        CLEANING("Cleaning");

        private final String displayName;

        TableStatus(String displayName) {
            this.displayName = displayName;
        }

        @Override
        public String toString() {
            return displayName;
        }
    }

    public Table(int tableNumber, int capacity) {
        this.tableNumber = tableNumber;
        this.capacity = capacity;
        this.status = TableStatus.FREE;
        this.currentOrder = null;
        this.reservationTime = null;
        this.reservationName = null;
    }

    // Getters
    public int getTableNumber() { return tableNumber; }
    public int getCapacity() { return capacity; }
    public TableStatus getStatus() { return status; }
    public Order getCurrentOrder() { return currentOrder; }
    public LocalDateTime getReservationTime() { return reservationTime; }
    public String getReservationName() { return reservationName; }

    // Setters
    public void setTableNumber(int tableNumber) { this.tableNumber = tableNumber; }
    public void setCapacity(int capacity) { this.capacity = capacity; }
    public void setStatus(TableStatus status) { this.status = status; }
    public void setCurrentOrder(Order currentOrder) { this.currentOrder = currentOrder; }
    public void setReservationTime(LocalDateTime reservationTime) { this.reservationTime = reservationTime; }
    public void setReservationName(String reservationName) { this.reservationName = reservationName; }

    // Business Logic
    public boolean isAvailable() {
        return status == TableStatus.FREE;
    }

    public boolean canReserve(LocalDateTime time) {
        return status == TableStatus.FREE || 
               (status == TableStatus.RESERVED && reservationTime.isBefore(time));
    }

    public void reserve(String name, LocalDateTime time) {
        if (canReserve(time)) {
            this.status = TableStatus.RESERVED;
            this.reservationName = name;
            this.reservationTime = time;
        }
    }

    public void occupy(Order order) {
        if (isAvailable()) {
            this.status = TableStatus.OCCUPIED;
            this.currentOrder = order;
        }
    }

    public void free() {
        this.status = TableStatus.CLEANING;
        this.currentOrder = null;
        this.reservationTime = null;
        this.reservationName = null;
    }

    public void markAsFree() {
        this.status = TableStatus.FREE;
    }

    @Override
    public String toString() {
        return String.format("Table %d (%d seats) - %s", tableNumber, capacity, status);
    }
} 