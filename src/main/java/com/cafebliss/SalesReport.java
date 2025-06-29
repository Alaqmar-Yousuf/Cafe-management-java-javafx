package com.cafebliss;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class SalesReport {
    private LocalDateTime date;
    private Map<MenuItem, Integer> itemsSold;
    private double totalRevenue;

    public SalesReport(LocalDateTime date) {
        this.date = date;
        this.itemsSold = new HashMap<>();
        this.totalRevenue = 0.0;
    }

    public void addItem(MenuItem item, int quantity) {
        itemsSold.merge(item, quantity, Integer::sum);
        totalRevenue += item.getPrice() * quantity;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public Map<MenuItem, Integer> getItemsSold() {
        return itemsSold;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public int getTotalItemsSold() {
        return itemsSold.values().stream().mapToInt(Integer::intValue).sum();
    }

    public String getFormattedDate() {
        return date.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Sales Report for ").append(getFormattedDate()).append("\n");
        sb.append("Total Revenue: $").append(String.format("%.2f", totalRevenue)).append("\n");
        sb.append("Total Items Sold: ").append(getTotalItemsSold()).append("\n");
        sb.append("\nItems Sold:\n");
        itemsSold.forEach((item, quantity) -> 
            sb.append(item.getName())
              .append(": ")
              .append(quantity)
              .append(" x $")
              .append(String.format("%.2f", item.getPrice()))
              .append(" = $")
              .append(String.format("%.2f", item.getPrice() * quantity))
              .append("\n")
        );
        return sb.toString();
    }
} 


