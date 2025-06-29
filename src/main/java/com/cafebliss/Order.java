package com.cafebliss;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private List<MenuItem> items;
    private List<Integer> quantities;
    private boolean receiptGenerated = false;

    public Order() {
        items = new ArrayList<>();
        quantities = new ArrayList<>();
    }

    public void addItem(MenuItem item, int quantity) {
        // Check if item already exists in order
        int existingIndex = findItemIndex(item);
        if (existingIndex != -1) {
            // Update quantity if item exists
            quantities.set(existingIndex, quantities.get(existingIndex) + quantity);
        } else {
            // Add new item
            items.add(item);
            quantities.add(quantity);
        }
    }

    public void removeItem(int index) {
        if (index >= 0 && index < items.size()) {
            items.remove(index);
            quantities.remove(index);
        }
    }

    public void updateQuantity(int index, int newQuantity) {
        if (index >= 0 && index < items.size() && newQuantity > 0) {
            quantities.set(index, newQuantity);
        }
    }

    private int findItemIndex(MenuItem item) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getName().equals(item.getName())) {
                return i;
            }
        }
        return -1;
    }

    public List<MenuItem> getItems() {
        return new ArrayList<>(items);
    }

    public List<Integer> getQuantities() {
        return new ArrayList<>(quantities);
    }

    public int getItemCount() {
        return items.size();
    }

    public double getTotal() {
        double total = 0;
        for (int i = 0; i < items.size(); i++) {
            total += items.get(i).getPrice() * quantities.get(i);
        }
        return total;
    }

    public String getOrderSummary() {
        StringBuilder summary = new StringBuilder();
        for (int i = 0; i < items.size(); i++) {
            summary.append(items.get(i).getName())
                   .append(" x").append(quantities.get(i))
                   .append(" - $").append(String.format("%.2f", items.get(i).getPrice() * quantities.get(i)))
                   .append("\n");
        }
        summary.append("Total: $").append(String.format("%.2f", getTotal()));
        return summary.toString();
    }

    public void generateReceipt() {
        try (FileWriter writer = new FileWriter("receipt.txt")) {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            writer.write("===== Cafe Bliss Receipt =====\n");
            writer.write("Date: " + now.format(formatter) + "\n\n");
            writer.write("Items Ordered:\n");
            writer.write(getOrderSummary());
            writer.write("\nThank you for your order!\n");
            writer.write("===========================\n");
            receiptGenerated = true;
        } catch (IOException e) {
            receiptGenerated = false;
            throw new RuntimeException("Failed to generate receipt: " + e.getMessage(), e);
        }
    }

    public boolean isReceiptGenerated() {
        return receiptGenerated;
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }
}