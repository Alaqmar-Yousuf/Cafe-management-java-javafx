# Cafe Bliss - Cafe Management System

A professional desktop cafe and restaurant management application built with **Java 17**, **JavaFX**, and **Maven**. Designed with a modern dark theme and an intuitive user interface, this system provides seamless point-of-sale (POS) operations, table reservation handling, inventory tracking, and business analytics.

---

## 🚀 Features

### 1. 🔐 Secure Authentication
- Single-point administrator login to protect business records.
- Default administrative credentials for quick access.

### 2. ☕ Order & Point of Sale (POS)
- Real-time order creation with automated price calculation.
- Live quantity adjustment and single-click item removal in the order table.
- Interactive menu catalog management (add, edit, and remove menu items).
- Instant receipt export to `receipt.txt` upon completing orders.

### 3. 🪑 Table & Reservation Management
- Visual grid layout displaying table capacity and live status.
- Color-coded table states:
  - 🟢 **Free:** Available for seating
  - 🔴 **Occupied:** Currently dining
  - 🔵 **Reserved:** Booked in advance
  - ⚪ **Cleaning:** Being sanitized
- Complete reservation controls: book tables with customer details, edit capacity, update status, and manage reservations.

### 4. 📦 Inventory Control
- Track raw ingredients, supplies, unit metrics, and stock thresholds.
- Automatic **Low Stock** warnings when quantities drop below minimum safety levels.
- Direct restock functionality and item catalog editing.

### 5. 📊 Sales Analytics & Reporting
- Filter business performance over configurable periods (*Today*, *This Week*, *This Month*, *Last Month*, *All Time*).
- Visual **Top 5 Popular Items** bar chart using JavaFX Charts.
- Export comprehensive sales summaries and item performance reports.

---

## 🛠️ Technology Stack

- **Language:** Java 17 (LTS) / compatible with modern JDKs (JDK 17 to JDK 23)
- **GUI Framework:** JavaFX 17 (`javafx-controls`, `javafx-fxml`)
- **Build System:** Apache Maven 3.8+
- **Styling:** Custom JavaFX CSS (Modern Dark Theme)

---

## 📁 Project Structure

This project strictly adheres to standard Maven conventions:

```text
Cafe Management System/
├── pom.xml                                    # Maven project descriptor & JavaFX plugins
├── .gitignore                                 # Git rules for Maven target, IDEs, and OS artifacts
├── README.md                                  # Complete project documentation
└── src/
    └── main/
        ├── java/com/cafebliss/
        │   ├── Main.java                      # Application entry point
        │   ├── LoginController.java           # Authentication screen controller
        │   ├── CafeController.java            # Main POS & order dashboard controller
        │   ├── TableController.java           # Table grid & reservations controller
        │   ├── InventoryController.java       # Stock & inventory management controller
        │   ├── AnalyticsController.java       # Business intelligence & charts controller
        │   ├── MenuItem.java                  # Menu item entity
        │   ├── Order.java                     # Order calculations & receipt generation
        │   ├── Table.java                     # Table state & reservation model
        │   ├── InventoryItem.java             # Stock item entity
        │   └── SalesReport.java               # Sales aggregation entity
        │
        └── resources/
            ├── LoginView.fxml                 # Login UI layout
            ├── CafeView.fxml                  # Point of sale UI layout
            ├── TableView.fxml                 # Table management layout
            ├── InventoryView.fxml             # Inventory management layout
            ├── AnalyticsView.fxml             # Analytics & reporting layout
            └── styles.css                     # Global dark-theme styling sheet
```

---

## ⚙️ Prerequisites & Setup

### Prerequisites
1. **Java Development Kit (JDK) 17 or higher** installed. Verify with:
   ```bash
   java -version
   ```
2. **Apache Maven 3.8 or higher** installed. Verify with:
   ```bash
   mvn -version
   ```

---

## 🏃 Running the Application

### 1. Set JAVA_HOME (if not already set)
**PowerShell:**
```powershell
$env:JAVA_HOME = "C:\Program Files\Java\jdk-23"
```
**Command Prompt (cmd):**
```cmd
set JAVA_HOME=C:\Program Files\Java\jdk-23
```

### 2. Build and Compile
From the project root directory, run:
```bash
mvn clean compile
```

### 3. Launch the Application
Run the JavaFX application directly:
```bash
mvn javafx:run
```

---

## 🔑 Default Login Credentials

Upon launching the application, sign in with the default administrator credentials:

| Field | Value |
|---|---|
| **Username** | `admin` |
| **Password** | `admin` |

---

## 📝 Generated Files

- **`receipt.txt`**: Created in the application root directory whenever a sale/order is confirmed. It contains the breakdown of items, quantities, total bill, and timestamp.
