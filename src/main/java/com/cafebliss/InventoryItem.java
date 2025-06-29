package com.cafebliss;

public class InventoryItem {
    private String name;
    private int quantity;
    private int minimumStock;
    private String unit; // e.g., "kg", "pieces", "liters"
    private double pricePerUnit;

    public InventoryItem(String name, int quantity, int minimumStock, String unit, double pricePerUnit) {
        this.name = name;
        this.quantity = quantity;
        this.minimumStock = minimumStock;
        this.unit = unit;
        this.pricePerUnit = pricePerUnit;
    }

    // Getters
    public String getName() { return name; }
    public int getQuantity() { return quantity; }
    public int getMinimumStock() { return minimumStock; }
    public String getUnit() { return unit; }
    public double getPricePerUnit() { return pricePerUnit; }

    // Setters
    public void setName(String name) { this.name = name; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setMinimumStock(int minimumStock) { this.minimumStock = minimumStock; }
    public void setUnit(String unit) { this.unit = unit; }
    public void setPricePerUnit(double pricePerUnit) { this.pricePerUnit = pricePerUnit; }

    // Business Logic
    public boolean isLowStock() {
        return quantity <= minimumStock;
    }

    public void addStock(int amount) {
        if (amount > 0) {
            quantity += amount;
        }
    }

    public boolean removeStock(int amount) {
        if (amount > 0 && amount <= quantity) {
            quantity -= amount;
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return String.format("%s - %d %s (Min: %d)", name, quantity, unit, minimumStock);
    }
} 