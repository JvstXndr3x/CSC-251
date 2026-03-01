import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;

// This is the best I could do in the time provided. Using AI as a tool, I got the picture of what it should be.
// Compared to the finalized code, this is a different picture of what is possible for the farm system, arranged into a table.
// Not much functionality is given but it is enough to be considered a starter for the system before we gave the finalized code.

// Item class
class Item {
    private String name;
    private double price;
    private int quantity;
    private int totalSold;

    public Item(String name, double price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.totalSold = 0;
    }

    public boolean sellItem(int amount) {
        if (amount <= 0) return false;
        if (quantity >= amount) {
            quantity -= amount;
            totalSold += amount;
            return true;
        }
        return false;
    }

    public void restock(int amount) { if (amount > 0) quantity += amount; }

    public String getName()   { return name; }
    public double getPrice()  { return price; }
    public int getQuantity()  { return quantity; }
    public int getTotalSold() { return totalSold; }

    public void setPrice(double price) { if (price >= 0) this.price = price; }

    @Override
    public String toString() {
        return String.format("%-20s | Price: $%-8.2f | Stock: %-5d | Sold: %d",
                name, price, quantity, totalSold);
    }
}

// Service class
class Service {
    public enum Status { SCHEDULED, COMPLETED, CANCELLED }

    private static int nextId = 1;

    private int id;
    private String name;
    private double cost;
    private String customerName;
    private String scheduledDate;
    private Status status;
    private boolean paymentReceived;

    public Service(String name, double cost, String customerName, String scheduledDate) {
        this.id = nextId++;
        this.name = name;
        this.cost = cost;
        this.customerName = customerName;
        this.scheduledDate = scheduledDate;
        this.status = Status.SCHEDULED;
        this.paymentReceived = false;
    }

    public void complete()      { if (status == Status.SCHEDULED) status = Status.COMPLETED; }
    public void cancel()        { if (status == Status.SCHEDULED) status = Status.CANCELLED; }
    public void recordPayment() { paymentReceived = true; }

    public int getId()               { return id; }
    public String getName()          { return name; }
    public double getCost()          { return cost; }
    public String getCustomerName()  { return customerName; }
    public String getScheduledDate() { return scheduledDate; }
    public Status getStatus()        { return status; }
    public boolean isPaymentReceived() { return paymentReceived; }

    public void setScheduledDate(String date) { this.scheduledDate = date; }
    public void setCost(double cost) { if (cost >= 0) this.cost = cost; }

    @Override
    public String toString() {
        return String.format("[ID:%d] %-20s | Customer: %-15s | Date: %s | $%.2f | %s | Paid: %s",
                id, name, customerName, scheduledDate, cost,
                status, paymentReceived ? "Yes" : "No");
    }
}

// Animal class
class Animal {
    public enum AnimalType { DUCK, CHICKEN, HAMSTER, RABBIT, OTHER }
    public enum SourceType { FARM_RAISED, BREEDER_RESALE }

    private static int nextId = 1;

    private int id;
    private AnimalType type;
    private String species;
    private String breed;
    private int ageMonths;
    private double price;
    private boolean available;
    private SourceType sourceType;
    private String breederName;
    private String healthStatus;

    public Animal(AnimalType type, String species, String breed, int ageMonths, double price) {
        this.id = nextId++;
        this.type = type;
        this.species = species;
        this.breed = breed;
        this.ageMonths = ageMonths;
        this.price = price;
        this.available = true;
        this.sourceType = SourceType.FARM_RAISED;
        this.breederName = "N/A";
        this.healthStatus = "Healthy";
    }

    public Animal(AnimalType type, String species, String breed,
                  int ageMonths, double price, String breederName) {
        this(type, species, breed, ageMonths, price);
        this.sourceType = SourceType.BREEDER_RESALE;
        this.breederName = breederName;
    }

    public boolean sell() {
        if (available) { available = false; return true; }
        return false;
    }

    public void markAsAvailable() { available = true; }

    public int getId()                { return id; }
    public AnimalType getType()       { return type; }
    public String getSpecies()        { return species; }
    public String getBreed()          { return breed; }
    public int getAgeMonths()         { return ageMonths; }
    public double getPrice()          { return price; }
    public boolean isAvailable()      { return available; }
    public SourceType getSourceType() { return sourceType; }
    public String getBreederName()    { return breederName; }
    public String getHealthStatus()   { return healthStatus; }

    public void setSpecies(String species)     { this.species = species; }
    public void setBreed(String breed)         { this.breed = breed; }
    public void setAgeMonths(int months)       { if (months >= 0) this.ageMonths = months; }
    public void setPrice(double price)         { if (price >= 0) this.price = price; }
    public void setHealthStatus(String status) { this.healthStatus = status; }
    public void setBreederName(String name)    { this.breederName = name; }

    @Override
    public String toString() {
        return String.format("[ID:%d] %-10s | %-20s | Breed: %-15s | Age: %d mo | $%.2f | %s | Source: %s%s | Health: %s",
                id, type, species, breed, ageMonths, price,
                available ? "Available" : "Sold",
                sourceType,
                sourceType == SourceType.BREEDER_RESALE ? " (" + breederName + ")" : "",
                healthStatus);
    }
}

class FarmManager {
    private List<Item>    inventory = new ArrayList<>();
    private List<Service> services  = new ArrayList<>();
    private List<Animal>  animals   = new ArrayList<>();

    public void addItem(Item item)   { inventory.add(item); }
    public List<Item> getInventory() { return Collections.unmodifiableList(inventory); }

    public Item findItem(String name) {
        return inventory.stream()
                .filter(i -> i.getName().equalsIgnoreCase(name))
                .findFirst().orElse(null);
    }

    public void scheduleService(Service s) { services.add(s); }
    public List<Service> getServices()     { return Collections.unmodifiableList(services); }

    public Service findService(int id) {
        return services.stream()
                .filter(s -> s.getId() == id)
                .findFirst().orElse(null);
    }

    public void addAnimal(Animal a)  { animals.add(a); }
    public List<Animal> getAnimals() { return Collections.unmodifiableList(animals); }

    public Animal findAnimal(int id) {
        return animals.stream()
                .filter(a -> a.getId() == id)
                .findFirst().orElse(null);
    }

    public List<Animal> getAvailableAnimals() {
        List<Animal> available = new ArrayList<>();
        for (Animal a : animals) if (a.isAvailable()) available.add(a);
        return available;
    }

    public double getTotalInventoryValue() {
        return inventory.stream().mapToDouble(i -> i.getPrice() * i.getQuantity()).sum();
    }

    public double getTotalServiceRevenue() {
        return services.stream()
                .filter(Service::isPaymentReceived)
                .mapToDouble(Service::getCost).sum();
    }

    public double getPendingServiceRevenue() {
        return services.stream()
                .filter(s -> s.getStatus() == Service.Status.COMPLETED && !s.isPaymentReceived())
                .mapToDouble(Service::getCost).sum();
    }
}

// Start the program
public class M3_JavaCode_GroupProject_02_POLISHED_MejiasAndrea {

    private static FarmManager manager = new FarmManager();

    public static void main(String[] args) {
        seedData();
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Farm Business Management System");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(900, 650);
            frame.setLocationRelativeTo(null);

            JTabbedPane tabs = new JTabbedPane();
            tabs.addTab("🐾 Animals",   buildAnimalPanel());
            tabs.addTab("🛒 Inventory", buildInventoryPanel());
            tabs.addTab("🔧 Services",  buildServicePanel());
            tabs.addTab("📊 Summary",   buildSummaryPanel());

            JButton saveBtn = new JButton("💾 Save to CSV");
            saveBtn.addActionListener(e -> saveToCSV(frame));
            JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 4));
            toolbar.add(saveBtn);

            frame.setLayout(new BorderLayout());
            frame.add(toolbar, BorderLayout.NORTH);
            frame.add(tabs, BorderLayout.CENTER);
            frame.setVisible(true);

            JOptionPane.showMessageDialog(frame,
                "Welcome to the Farm Business Management System!\n" +
                "Use the tabs above to manage Animals, Inventory, and Services.",
                "Welcome", JOptionPane.INFORMATION_MESSAGE);
        });
    }

    // Saves all inventory, animal, and service data to a CSV file chosen by the user
    private static void saveToCSV(JFrame parent) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Save Farm Data as CSV");
        chooser.setSelectedFile(new File("farm_data.csv"));
        if (chooser.showSaveDialog(parent) != JFileChooser.APPROVE_OPTION) return;

        File file = chooser.getSelectedFile();
        if (!file.getName().toLowerCase().endsWith(".csv")) {
            file = new File(file.getAbsolutePath() + ".csv");
        }

        try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {

            // Inventory section
            pw.println("=== INVENTORY ===");
            pw.println("Name,Price,Quantity,TotalSold");
            for (Item i : manager.getInventory()) {
                pw.printf("\"%s\",%.2f,%d,%d%n",
                        i.getName(), i.getPrice(), i.getQuantity(), i.getTotalSold());
            }

            pw.println();

            // Animals section
            pw.println("=== ANIMALS ===");
            pw.println("ID,Type,Species,Breed,AgeMonths,Price,Available,SourceType,BreederName,HealthStatus");
            for (Animal a : manager.getAnimals()) {
                pw.printf("%d,%s,\"%s\",\"%s\",%d,%.2f,%b,%s,\"%s\",\"%s\"%n",
                        a.getId(), a.getType(), a.getSpecies(), a.getBreed(),
                        a.getAgeMonths(), a.getPrice(), a.isAvailable(),
                        a.getSourceType(), a.getBreederName(), a.getHealthStatus());
            }

            pw.println();

            // Services section
            pw.println("=== SERVICES ===");
            pw.println("ID,Name,CustomerName,ScheduledDate,Cost,Status,PaymentReceived");
            for (Service s : manager.getServices()) {
                pw.printf("%d,\"%s\",\"%s\",%s,%.2f,%s,%b%n",
                        s.getId(), s.getName(), s.getCustomerName(),
                        s.getScheduledDate(), s.getCost(),
                        s.getStatus(), s.isPaymentReceived());
            }

            JOptionPane.showMessageDialog(parent,
                    "Data saved successfully to:\n" + file.getAbsolutePath(),
                    "Save Successful", JOptionPane.INFORMATION_MESSAGE);

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(parent,
                    "Error saving file:\n" + ex.getMessage(),
                    "Save Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static JPanel buildAnimalPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] cols = {"ID", "Type", "Species", "Breed", "Age (mo)", "Price", "Status", "Source", "Breeder", "Health"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(model);
        refreshAnimalTable(model);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton addBtn = new JButton("Add Animal");
        addBtn.addActionListener(e -> { showAddAnimalDialog(); refreshAnimalTable(model); });

        JButton sellBtn = new JButton("Sell Selected");
        sellBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(null, "Select an animal first."); return; }
            int id = (int) model.getValueAt(row, 0);
            Animal a = manager.findAnimal(id);
            if (a != null && a.sell()) {
                JOptionPane.showMessageDialog(null, "Animal sold successfully!");
                refreshAnimalTable(model);
            } else {
                JOptionPane.showMessageDialog(null, "Animal is not available for sale.");
            }
        });

        JButton healthBtn = new JButton("Update Health");
        healthBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(null, "Select an animal first."); return; }
            int id = (int) model.getValueAt(row, 0);
            Animal a = manager.findAnimal(id);
            if (a != null) {
                String status = JOptionPane.showInputDialog(null, "Enter health status:", a.getHealthStatus());
                if (status != null && !status.isBlank()) { a.setHealthStatus(status); refreshAnimalTable(model); }
            }
        });

        btnPanel.add(addBtn);
        btnPanel.add(sellBtn);
        btnPanel.add(healthBtn);

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(btnPanel, BorderLayout.SOUTH);
        return panel;
    }

    private static void showAddAnimalDialog() {
        JPanel form = new JPanel(new GridLayout(0, 2, 5, 5));

        String[] types = Arrays.stream(Animal.AnimalType.values()).map(Enum::name).toArray(String[]::new);
        JComboBox<String> typeBox   = new JComboBox<>(types);
        JTextField speciesField     = new JTextField();
        JTextField breedField       = new JTextField();
        JTextField ageField         = new JTextField();
        JTextField priceField       = new JTextField();
        JComboBox<String> sourceBox = new JComboBox<>(new String[]{"Farm Raised", "Breeder Resale"});
        JTextField breederField     = new JTextField();

        form.add(new JLabel("Type:"));                         form.add(typeBox);
        form.add(new JLabel("Species:"));                      form.add(speciesField);
        form.add(new JLabel("Breed:"));                        form.add(breedField);
        form.add(new JLabel("Age (months):"));                 form.add(ageField);
        form.add(new JLabel("Price ($):"));                    form.add(priceField);
        form.add(new JLabel("Source:"));                       form.add(sourceBox);
        form.add(new JLabel("Breeder Name (if applicable):")); form.add(breederField);

        int result = JOptionPane.showConfirmDialog(null, form, "Add Animal",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) return;

        try {
            Animal.AnimalType type = Animal.AnimalType.valueOf((String) typeBox.getSelectedItem());
            String species = speciesField.getText().trim();
            String breed   = breedField.getText().trim();
            int age        = Integer.parseInt(ageField.getText().trim());
            double price   = Double.parseDouble(priceField.getText().trim());

            if (species.isEmpty() || breed.isEmpty()) throw new IllegalArgumentException("Fields required.");

            Animal animal;
            if (sourceBox.getSelectedIndex() == 1) {
                String breeder = breederField.getText().trim();
                if (breeder.isEmpty()) breeder = "Unknown Breeder";
                animal = new Animal(type, species, breed, age, price, breeder);
            } else {
                animal = new Animal(type, species, breed, age, price);
            }
            manager.addAnimal(animal);
            JOptionPane.showMessageDialog(null, "Animal added successfully!");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Invalid number entered. Please check Age and Price.");
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
        }
    }

    private static void refreshAnimalTable(DefaultTableModel model) {
        model.setRowCount(0);
        for (Animal a : manager.getAnimals()) {
            model.addRow(new Object[]{
                a.getId(), a.getType(), a.getSpecies(), a.getBreed(),
                a.getAgeMonths(), String.format("$%.2f", a.getPrice()),
                a.isAvailable() ? "Available" : "Sold",
                a.getSourceType(), a.getBreederName(), a.getHealthStatus()
            });
        }
    }

    private static JPanel buildInventoryPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] cols = {"Name", "Price", "Quantity", "Total Sold"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(model);
        refreshInventoryTable(model);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton addBtn = new JButton("Add Item");
        addBtn.addActionListener(e -> {
            JTextField nameF  = new JTextField();
            JTextField priceF = new JTextField();
            JTextField qtyF   = new JTextField();
            JPanel form = new JPanel(new GridLayout(0, 2, 5, 5));
            form.add(new JLabel("Item Name:")); form.add(nameF);
            form.add(new JLabel("Price ($):")); form.add(priceF);
            form.add(new JLabel("Quantity:"));  form.add(qtyF);
            int res = JOptionPane.showConfirmDialog(null, form, "Add Item",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (res == JOptionPane.OK_OPTION) {
                try {
                    manager.addItem(new Item(nameF.getText().trim(),
                            Double.parseDouble(priceF.getText().trim()),
                            Integer.parseInt(qtyF.getText().trim())));
                    refreshInventoryTable(model);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid number entered.");
                }
            }
        });

        JButton sellBtn = new JButton("Sell Selected");
        sellBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(null, "Select an item first."); return; }
            String name = (String) model.getValueAt(row, 0);
            Item item = manager.findItem(name);
            if (item == null) return;
            String input = JOptionPane.showInputDialog("How many to sell?");
            if (input == null) return;
            try {
                int qty = Integer.parseInt(input.trim());
                if (item.sellItem(qty)) {
                    JOptionPane.showMessageDialog(null, "Sold " + qty + " x " + name);
                } else {
                    JOptionPane.showMessageDialog(null, "Not enough stock.");
                }
                refreshInventoryTable(model);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid quantity.");
            }
        });

        JButton restockBtn = new JButton("Restock");
        restockBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(null, "Select an item first."); return; }
            String name = (String) model.getValueAt(row, 0);
            Item item = manager.findItem(name);
            if (item == null) return;
            String input = JOptionPane.showInputDialog("How many to add?");
            if (input == null) return;
            try {
                item.restock(Integer.parseInt(input.trim()));
                refreshInventoryTable(model);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid quantity.");
            }
        });

        btnPanel.add(addBtn);
        btnPanel.add(sellBtn);
        btnPanel.add(restockBtn);

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(btnPanel, BorderLayout.SOUTH);
        return panel;
    }

    private static void refreshInventoryTable(DefaultTableModel model) {
        model.setRowCount(0);
        for (Item i : manager.getInventory()) {
            model.addRow(new Object[]{
                i.getName(), String.format("$%.2f", i.getPrice()),
                i.getQuantity(), i.getTotalSold()
            });
        }
    }

    private static JPanel buildServicePanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] cols = {"ID", "Service", "Customer", "Date", "Cost", "Status", "Paid"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(model);
        refreshServiceTable(model);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton schedBtn = new JButton("Schedule Service");
        schedBtn.addActionListener(e -> {
            JTextField svcF  = new JTextField();
            JTextField custF = new JTextField();
            JTextField dateF = new JTextField("YYYY-MM-DD");
            JTextField costF = new JTextField();
            JPanel form = new JPanel(new GridLayout(0, 2, 5, 5));
            form.add(new JLabel("Service Name:"));  form.add(svcF);
            form.add(new JLabel("Customer Name:")); form.add(custF);
            form.add(new JLabel("Date:"));          form.add(dateF);
            form.add(new JLabel("Cost ($):"));      form.add(costF);
            int res = JOptionPane.showConfirmDialog(null, form, "Schedule Service",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (res == JOptionPane.OK_OPTION) {
                try {
                    manager.scheduleService(new Service(
                            svcF.getText().trim(), Double.parseDouble(costF.getText().trim()),
                            custF.getText().trim(), dateF.getText().trim()));
                    refreshServiceTable(model);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid cost entered.");
                }
            }
        });

        JButton completeBtn = new JButton("Mark Complete");
        completeBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(null, "Select a service first."); return; }
            int id = (int) model.getValueAt(row, 0);
            Service s = manager.findService(id);
            if (s != null) { s.complete(); refreshServiceTable(model); }
        });

        JButton payBtn = new JButton("Record Payment");
        payBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(null, "Select a service first."); return; }
            int id = (int) model.getValueAt(row, 0);
            Service s = manager.findService(id);
            if (s != null) {
                s.recordPayment();
                refreshServiceTable(model);
                JOptionPane.showMessageDialog(null, "Payment recorded for service ID " + id);
            }
        });

        JButton cancelBtn = new JButton("Cancel Service");
        cancelBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(null, "Select a service first."); return; }
            int id = (int) model.getValueAt(row, 0);
            Service s = manager.findService(id);
            if (s != null) { s.cancel(); refreshServiceTable(model); }
        });

        btnPanel.add(schedBtn);
        btnPanel.add(completeBtn);
        btnPanel.add(payBtn);
        btnPanel.add(cancelBtn);

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(btnPanel, BorderLayout.SOUTH);
        return panel;
    }

    private static void refreshServiceTable(DefaultTableModel model) {
        model.setRowCount(0);
        for (Service s : manager.getServices()) {
            model.addRow(new Object[]{
                s.getId(), s.getName(), s.getCustomerName(), s.getScheduledDate(),
                String.format("$%.2f", s.getCost()), s.getStatus(),
                s.isPaymentReceived() ? "Yes" : "No"
            });
        }
    }

    private static JPanel buildSummaryPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextArea text = new JTextArea();
        text.setEditable(false);
        text.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));

        JButton refreshBtn = new JButton("Refresh Summary");
        refreshBtn.addActionListener(e -> updateSummary(text));
        updateSummary(text);

        panel.add(new JScrollPane(text), BorderLayout.CENTER);
        panel.add(refreshBtn, BorderLayout.SOUTH);
        return panel;
    }

    private static void updateSummary(JTextArea text) {
        StringBuilder sb = new StringBuilder();
        sb.append("========================================\n");
        sb.append("   FARM BUSINESS MANAGEMENT SUMMARY\n");
        sb.append("========================================\n\n");

        sb.append("--- INVENTORY ---\n");
        sb.append(String.format("Total Inventory Value:   $%.2f%n", manager.getTotalInventoryValue()));
        sb.append("Items:\n");
        for (Item i : manager.getInventory()) sb.append("  ").append(i).append("\n");

        sb.append("\n--- ANIMALS ---\n");
        long available = manager.getAnimals().stream().filter(Animal::isAvailable).count();
        long sold      = manager.getAnimals().stream().filter(a -> !a.isAvailable()).count();
        sb.append(String.format("  Available: %d   |   Sold: %d%n", available, sold));
        sb.append("  Available Listings:\n");
        for (Animal a : manager.getAvailableAnimals()) sb.append("    ").append(a).append("\n");

        sb.append("\n--- SERVICES ---\n");
        sb.append(String.format("  Revenue Received:  $%.2f%n", manager.getTotalServiceRevenue()));
        sb.append(String.format("  Pending Payment:   $%.2f%n", manager.getPendingServiceRevenue()));
        for (Service s : manager.getServices()) sb.append("  ").append(s).append("\n");

        text.setText(sb.toString());
    }

    private static void seedData() {
        manager.addItem(new Item("Chicken Feed (50lb)",  18.99, 20));
        manager.addItem(new Item("Duck Feed (50lb)",     19.99, 15));
        manager.addItem(new Item("Rabbit Pellets (5lb)", 12.49, 30));
        manager.addItem(new Item("Straw Bale",            6.99, 40));
        manager.addItem(new Item("Wood Shavings",         9.99, 25));
        manager.addItem(new Item("Water Feeder",         14.99, 10));

        manager.addAnimal(new Animal(Animal.AnimalType.CHICKEN, "Rhode Island Red", "Rhode Island Red", 4,  25.00));
        manager.addAnimal(new Animal(Animal.AnimalType.DUCK,    "Pekin Duck",       "Pekin",            6,  35.00));
        manager.addAnimal(new Animal(Animal.AnimalType.RABBIT,  "Holland Lop",      "Holland Lop",      3,  45.00));
        manager.addAnimal(new Animal(Animal.AnimalType.HAMSTER, "Syrian Hamster",   "Syrian",           2,  15.00));

        manager.addAnimal(new Animal(Animal.AnimalType.RABBIT,  "Flemish Giant",       "Flemish Giant",  5,  80.00,  "Sunny Acres Rabbitry"));
        manager.addAnimal(new Animal(Animal.AnimalType.DUCK,    "Cayuga Duck",         "Cayuga",         8,  50.00,  "Green Pasture Farm"));
        manager.addAnimal(new Animal(Animal.AnimalType.OTHER,   "Nigerian Dwarf Goat", "Nigerian Dwarf", 12, 200.00, "Blue Ridge Breeders"));

        Service s1 = new Service("Animal Health Check", 50.00, "John Smith",  "2026-03-01");
        Service s2 = new Service("Hoof Trimming",       40.00, "Mary Johnson","2026-03-03");
        Service s3 = new Service("Pen Cleaning",        75.00, "Tom Davis",   "2026-02-20");
        s3.complete(); s3.recordPayment();
        Service s4 = new Service("Feed Consultation",   30.00, "Alice Brown", "2026-02-25");
        s4.complete();

        manager.scheduleService(s1);
        manager.scheduleService(s2);
        manager.scheduleService(s3);
        manager.scheduleService(s4);
    }
}