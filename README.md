# Cafe Bliss - Cafe Management System

A professional, full-featured desktop cafe and restaurant management system built with **Java 17 (LTS)**, **JavaFX 17**, and **Apache Maven**. 

Engineered with a modern high-contrast dark theme, intuitive responsive UI layouts, and the **Model-View-Controller (MVC)** architectural pattern, this application delivers a seamless experience for point-of-sale (POS) operations, real-time table reservations, live inventory tracking, and business analytics.

---

## 🚀 Key Features

### 1. 🔐 Secure Administrator Authentication
- Single-point security gate protecting cafe operational data.
- Input validation and informative error prompts for incorrect credentials.
- Automatic stage maximization upon successful sign-in.

### 2. ☕ Point of Sale (POS) & Order Management
- **Menu Catalog Management:** Dynamic in-app options to add, update prices, or remove menu items with instant UI synchronization.
- **Interactive Order Building:** Add menu selections with real-time quantity validation (1 to 100).
- **Inline Quantity Editing:** Edit quantities directly inside the order table using JavaFX `TextFieldTableCell` and automatic recalculation.
- **Single-Click Line Removal:** Dedicated action buttons in each row to adjust orders on the fly.
- **Instant Receipt Generation:** Generates and exports an itemized customer receipt with timestamps to `receipt.txt` upon checkout confirmation.

### 3. 🪑 Visual Table & Reservation Management
- **Interactive Visual Grid:** Dynamic 4-column table matrix automatically scaling across monitor resolutions.
- **Color-Coded Status Badges:**
  - 🟢 **Free:** Available for walk-in seating
  - 🔴 **Occupied:** Currently dining
  - 🔵 **Reserved:** Booked in advance
  - ⚪ **Cleaning:** Being sanitized
- **Reservation Engine:** Complete guest reservation booking with customer name, party size, and reservation time tracking.
- **Table Administration:** Add new tables, edit seat capacities, or delete tables dynamically through modal dialogs.

### 4. 📦 Live Inventory & Stock Control
- Real-time monitoring of raw ingredients, unit metrics (kg, liters, packets), and safety thresholds.
- **Automated Low Stock Highlighting:** Rows dynamically flagged in red with warnings when stock dips below critical minimum levels.
- **Direct In-App Restocking:** One-click restock dialogues to replenish inventory quantities without disrupting baseline price formulas.

### 5. 📊 Sales Analytics & Visual Reporting
- **Multi-Period Filtering:** Filter performance metrics over *Today*, *This Week*, *This Month*, *Last Month*, and *All Time*.
- **Top 5 Bestsellers Chart:** Responsive JavaFX `BarChart` highlighting top-selling cafe items with vibrant, high-contrast palette bars.
- **Comprehensive Audit Summaries:** Computes gross revenue, total items sold, and average order value.
- **File Export:** Save formatted text audit reports directly to disk via the native system `FileChooser`.

### 6. 🖥️ Full-Screen Responsive Experience
- Application automatically maximizes on initial launch and preserves maximized full-screen dimensions across all view transitions.
- All layouts utilize JavaFX flex growth (`HBox.hgrow="ALWAYS"`, `VBox.vgrow="ALWAYS"`, and `CONSTRAINED_RESIZE_POLICY`) to fill ultra-wide and standard displays without letterboxing.

---

## 🛠️ Technology Stack & Architecture

- **Architecture:** Model-View-Controller (MVC)
- **Programming Language:** Java 17 LTS (Compatible up to JDK 23)
- **GUI Framework:** OpenJFX 17 (`javafx-controls`, `javafx-fxml`)
- **Build & Dependency Automation:** Apache Maven 3.8+
- **Styling:** Custom JavaFX CSS (Dark Theme, Segoe UI / Consolas Typography)

---

## 📁 Project Directory Structure

```text
Cafe Management System/
├── pom.xml                                    # Maven project descriptor & JavaFX build plugins
├── .gitignore                                 # Git rules for Maven target, IDEs, and OS artifacts
├── README.md                                  # Complete project documentation (this file)
├── Cafe_Bliss_Complete_Documentation.pdf      # Detailed technical architecture & viva defense guide
│
└── src/
    └── main/
        ├── java/com/cafebliss/
        │   ├── Main.java                      # Application entry point & primary stage setup
        │   ├── LoginController.java           # Authentication screen controller
        │   ├── CafeController.java            # Main POS & order dashboard controller
        │   ├── TableController.java           # Table grid & reservations controller
        │   ├── InventoryController.java       # Stock & inventory management controller
        │   ├── AnalyticsController.java       # Business intelligence & charts controller
        │   ├── MenuItem.java                  # Menu item data entity
        │   ├── Order.java                     # Order logic & receipt file writer
        │   ├── Table.java                     # Table state & reservation data model
        │   ├── InventoryItem.java             # Inventory stock data entity
        │   └── SalesReport.java               # Shift revenue & items sold data model
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

## ⚙️ Prerequisites

1. **Java Development Kit (JDK) 17 or higher** installed. Verify with:
   ```bash
   java -version
   ```
2. **Apache Maven 3.8 or higher** installed. Verify with:
   ```bash
   mvn -version
   ```

---

## 🏃 How to Run the Application

Navigate to the project root directory where `pom.xml` is located:
```text
D:\Projects and other\projects\cafe management system\Cafe Management System
```

### ⚡ Method 1: Windows PowerShell (Recommended — Single Command)
Open **PowerShell** in the project folder and paste this single line:

```powershell
$env:JAVA_HOME = "C:\Program Files\Java\jdk-23"; & "C:\Users\Dell\.m2\wrapper\dists\apache-maven-3.9.16\0daed3be3ebd1c706f0e69e8b07c6b73f5cc4ea3dfce72a8d0ec2e849ca2ddb0\bin\mvn.cmd" -o javafx:run
```

*The application will compile resources and immediately open the **Cafe Bliss - Login** window in full screen!*

---

### 💻 Method 2: Command Prompt (`cmd.exe`)
If you are using **Command Prompt**, run these two commands separately:

**Step 1 — Set `JAVA_HOME` (with enclosing quotes):**
```cmd
set "JAVA_HOME=C:\Program Files\Java\jdk-23"
```
*(Important: The quotes around `"JAVA_HOME=..."` are required in cmd to prevent trailing space errors).*

**Step 2 — Launch via Maven:**
```cmd
"C:\Users\Dell\.m2\wrapper\dists\apache-maven-3.9.16\0daed3be3ebd1c706f0e69e8b07c6b73f5cc4ea3dfce72a8d0ec2e849ca2ddb0\bin\mvn.cmd" -o javafx:run
```

---

### 🌐 Method 3: Standard Maven (if `mvn` is on your PATH)
In any newly opened terminal where `mvn` is globally available:
```bash
mvn -o javafx:run
```

---

### 🛠️ Common Troubleshooting

| Error | Root Cause | Solution |
|---|---|---|
| `The JAVA_HOME environment variable is not defined correctly` | Running `set JAVA_HOME=... &` in `cmd` accidentally appends a trailing space to the directory path. | Use PowerShell (Method 1) or run `set "JAVA_HOME=C:\Program Files\Java\jdk-23"` with quotes (Method 2). |
| `mvn is not recognized` | Maven is not added to system PATH. | Use the full path to `mvn.cmd` shown in Method 1 or 2. |
| `Can not create cache at .openjfx` | Restricted user home permissions. | Run directly in PowerShell or cmd outside restricted sandboxes. |

---

## 🔑 Default Login Credentials

Sign in with the default administrator credentials:

| Field | Value |
|---|---|
| **Username** | `admin` |
| **Password** | `admin` |

---

## 📄 Documentation & Presentation Guide

For exams, project presentations, code reviews, or architectural defense, refer to the included publication document:
- **[Cafe_Bliss_Complete_Documentation.pdf](Cafe_Bliss_Complete_Documentation.pdf)**: Comprehensive 5-page PDF covering MVC architecture, full file-by-file technical explanations, critical code snippets, and a complete Viva Q&A preparation guide.

---

## 📝 Generated Output Files

- **`receipt.txt`**: Automatically generated in the application root directory whenever an order is finalized. Contains item names, quantities, individual prices, total balance, and a transaction timestamp.
