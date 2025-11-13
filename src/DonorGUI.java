import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class DonorGUI extends JFrame {

    private DonorManager donorManager;

    // Form fields
    private JTextField firstNameField, lastNameField, emailField, phoneField, cityField, lastDonationField;
    private JComboBox<String> bloodTypeBox;
    private JCheckBox activeCheckBox;
    private JButton addButton, updateButton, loadButton, clearButton;

    // Table
    private JTable donorTable;
    private DefaultTableModel tableModel;
    private UUID selectedDonorId;

    // Filters
    private JComboBox<String> filterBloodBox;
    private JTextField filterCityField;
    private JCheckBox eligibleCheckBox;
    private JButton searchButton, refreshButton, removeButton;

    public DonorGUI() {
        donorManager = new DonorManager();
        setTitle("DonateLife - Donor Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1700, 1000);
        setLayout(new BorderLayout());

        initFormPanel();
        initTable();
        initFilterPanel();
        initActions();

        setVisible(true);
    }


    private void initFormPanel() {
        JPanel formPanel = new JPanel(new GridLayout(9, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder("Donor Form"));

        formPanel.add(new JLabel("First Name:"));
        firstNameField = new JTextField();
        formPanel.add(firstNameField);

        formPanel.add(new JLabel("Last Name:"));
        lastNameField = new JTextField();
        formPanel.add(lastNameField);

        formPanel.add(new JLabel("Blood Type:"));
        bloodTypeBox = new JComboBox<>(new String[]{"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"});
        formPanel.add(bloodTypeBox);

        formPanel.add(new JLabel("Email:"));
        emailField = new JTextField();
        formPanel.add(emailField);

        formPanel.add(new JLabel("Phone:"));
        phoneField = new JTextField();
        formPanel.add(phoneField);

        formPanel.add(new JLabel("City:"));
        cityField = new JTextField();
        formPanel.add(cityField);

        formPanel.add(new JLabel("Active:"));
        activeCheckBox = new JCheckBox();
        activeCheckBox.setSelected(true);
        formPanel.add(activeCheckBox);

        formPanel.add(new JLabel("Last Donation (YYYY-MM-DD):"));
        lastDonationField = new JTextField();
        formPanel.add(lastDonationField);

        JPanel buttonPanel = new JPanel();
        addButton = new JButton("Add Donor");
        loadButton = new JButton("Load Selected");
        updateButton = new JButton("Update Donor");
        clearButton = new JButton("Clear Fields");
        buttonPanel.add(addButton);
        buttonPanel.add(loadButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(clearButton);

        formPanel.add(buttonPanel);

        add(formPanel, BorderLayout.NORTH);
    }

    private void initTable() {
        String[] columns = {"ID", "Name", "Blood Type", "Email", "Phone", "City", "Last Donation", "Active", "Eligible"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        donorTable = new JTable(tableModel);
        add(new JScrollPane(donorTable), BorderLayout.CENTER);
    }

    private void initFilterPanel() {
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.setBorder(BorderFactory.createTitledBorder("Filters / Search"));

        filterPanel.add(new JLabel("Blood Type:"));
        filterBloodBox = new JComboBox<>(new String[]{"", "A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"});
        filterPanel.add(filterBloodBox);

        filterPanel.add(new JLabel("City:"));
        filterCityField = new JTextField(10);
        filterPanel.add(filterCityField);

        eligibleCheckBox = new JCheckBox("Eligible");
        filterPanel.add(eligibleCheckBox);

        searchButton = new JButton("Search");
        refreshButton = new JButton("Refresh");
        removeButton = new JButton("Remove Selected");
        filterPanel.add(searchButton);
        filterPanel.add(refreshButton);
        filterPanel.add(removeButton);

        add(filterPanel, BorderLayout.SOUTH);
    }

    private void initActions() {
        addButton.addActionListener(e -> addDonorAction());
        clearButton.addActionListener(e -> clearFields());
        loadButton.addActionListener(e -> loadSelectedDonor());
        updateButton.addActionListener(e -> updateDonorAction());
        searchButton.addActionListener(e -> searchDonorsAction());
        refreshButton.addActionListener(e -> refreshTable());
        removeButton.addActionListener(e -> removeSelectedDonor());
    }



    private void addDonorAction() {
        Donor donor = createDonorFromFields(UUID.randomUUID());
        donor.setActive(activeCheckBox.isSelected());
        donor.updateLastDonationDate(parseDateField());

        List<String> errors = donor.validate();
        if (!errors.isEmpty()) {
            JOptionPane.showMessageDialog(this, String.join("\n", errors), "Validation Errors", JOptionPane.ERROR_MESSAGE);
            return;
        }

        donorManager.addDonor(donor);
        refreshTable();
        clearFields();
    }

    private void loadSelectedDonor() {
        int selectedRow = donorTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a donor to load.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        selectedDonorId = (UUID) tableModel.getValueAt(selectedRow, 0);
        Donor donor = donorManager.findDonorById(selectedDonorId);
        if (donor != null) {
            firstNameField.setText(donor.getFirstName());
            lastNameField.setText(donor.getLastName());
            bloodTypeBox.setSelectedItem(donor.getBloodType());
            emailField.setText(donor.getEmail());
            phoneField.setText(donor.getPhone());
            cityField.setText(donor.getCity());
            activeCheckBox.setSelected(donor.isActive());
            lastDonationField.setText(donor.getLastDonationDate() != null ? donor.getLastDonationDate().toLocalDate().toString() : "");
        }
    }

    private void updateDonorAction() {
        if (selectedDonorId == null) {
            JOptionPane.showMessageDialog(this, "Load a donor first to update.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Donor donor = donorManager.findDonorById(selectedDonorId);
        if (donor == null) return;

        donor.setFirstName(firstNameField.getText());
        donor.setLastName(lastNameField.getText());
        donor.setBloodType((String) bloodTypeBox.getSelectedItem());
        donor.setEmail(emailField.getText());
        donor.setPhone(phoneField.getText());
        donor.setCity(cityField.getText());
        donor.setActive(activeCheckBox.isSelected());
        donor.updateLastDonationDate(parseDateField());

        List<String> errors = donor.validate();
        if (!errors.isEmpty()) {
            JOptionPane.showMessageDialog(this, String.join("\n", errors), "Validation Errors", JOptionPane.ERROR_MESSAGE);
            return;
        }

        refreshTable();
        clearFields();
    }

    private void searchDonorsAction() {
        String bloodType = (String) filterBloodBox.getSelectedItem();
        if (bloodType != null && bloodType.isEmpty()) bloodType = null;

        String city = filterCityField.getText();
        if (city != null && city.trim().isEmpty()) city = null;

        Boolean eligible = eligibleCheckBox.isSelected() ? true : null;

        List<Donor> results = donorManager.searchDonors(bloodType, city, eligible);
        tableModel.setRowCount(0);
        for (Donor donor : results) {
            tableModel.addRow(new Object[]{
                    donor.getId(),
                    donor.getFirstName() + " " + donor.getLastName(),
                    donor.getBloodType(),
                    donor.getEmail(),
                    donor.getPhone(),
                    donor.getCity(),
                    donor.getLastDonationDate() != null ? donor.getLastDonationDate().toLocalDate() : "",
                    donor.isActive(),
                    donor.isEligible()
            });
        }
    }

    private void removeSelectedDonor() {
        int selectedRow = donorTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a donor to remove.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        UUID id = (UUID) tableModel.getValueAt(selectedRow, 0);
        donorManager.removeDonorById(id);
        refreshTable();
    }

    // ---------- Helper Methods ----------

    private Donor createDonorFromFields(UUID id) {
        return new Donor(
                id,
                firstNameField.getText(),
                lastNameField.getText(),
                (String) bloodTypeBox.getSelectedItem(),
                emailField.getText(),
                phoneField.getText(),
                cityField.getText()
        );
    }

    private LocalDateTime parseDateField() {
        String dateText = lastDonationField.getText();
        if (dateText == null || dateText.trim().isEmpty()) return null;

        try {
            return LocalDate.parse(dateText.trim()).atStartOfDay();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid date format. Use YYYY-MM-DD.", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    private void clearFields() {
        firstNameField.setText("");
        lastNameField.setText("");
        emailField.setText("");
        phoneField.setText("");
        cityField.setText("");
        bloodTypeBox.setSelectedIndex(0);
        activeCheckBox.setSelected(true);
        lastDonationField.setText("");
        selectedDonorId = null;
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (Donor donor : donorManager.getDonorList()) {
            tableModel.addRow(new Object[]{
                    donor.getId(),
                    donor.getFirstName() + " " + donor.getLastName(),
                    donor.getBloodType(),
                    donor.getEmail(),
                    donor.getPhone(),
                    donor.getCity(),
                    donor.getLastDonationDate() != null ? donor.getLastDonationDate().toLocalDate() : "",
                    donor.isActive(),
                    donor.isEligible()
            });
        }
    }
}
