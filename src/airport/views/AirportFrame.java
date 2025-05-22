/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package airport.views;

import airport.controllers.FlightController;
import airport.controllers.LocationController;
import airport.controllers.PassengerController;
import airport.controllers.PlaneController;
import airport.controllers.utils.Response;
import airport.models.Flight;
import airport.models.Location;
import airport.models.Passenger;
import airport.models.Plane;
import java.awt.Color;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author edangulo
 */
public class AirportFrame extends javax.swing.JFrame {

    /**
     * Creates new form AirportFrame
     */
    private int x, y;
    // private ArrayList<Passenger> passengers; // Removed
    // private ArrayList<Plane> planes; // Removed
    // private ArrayList<Location> locations; // Removed
    // private ArrayList<Flight> flights; // Removed

    private PassengerController passengerController;
    private PlaneController planeController;
    private LocationController locationController;
    private FlightController flightController;

    public AirportFrame() {
        initComponents();

        // Initialize controllers
        this.passengerController = new PassengerController();
        this.planeController = new PlaneController();
        this.locationController = new LocationController();
        this.flightController = new FlightController();

        // this.passengers = new ArrayList<>(); // Removed
        // this.planes = new ArrayList<>(); // Removed
        // this.locations = new ArrayList<>(); // Removed
        // this.flights = new ArrayList<>(); // Removed
        this.setBackground(new Color(0, 0, 0, 0));
        this.setLocationRelativeTo(null);

        this.generateMonths();
        this.generateDays();
        this.generateHours();
        this.generateMinutes();
        blockPanels();
        populateUserSelectComboBox();
        populatePlanesComboBox();
        populateLocationsComboBoxes();
        populateFlightsComboBox();

        if (!user.isSelected() && !administrator.isSelected()) {
            administrator.setSelected(true);
            administratorActionPerformed(null);
        } else if (administrator.isSelected()) {
            administratorActionPerformed(null);
        } else if (user.isSelected()) {
            userActionPerformed(null);
        }
    }

    private void populateFlightsComboBox() {
        addToFlightFlightComboBox.removeAllItems(); // For "Add to Flight" tab
        addToFlightFlightComboBox.addItem("Flight");
        delayFlightFlightIdComboBox.removeAllItems(); // For "Delay Flight" tab
        delayFlightFlightIdComboBox.addItem("ID"); // Or "Flight ID" - current uses "ID"

        Response response = flightController.getAllFlightsOrderedByDepartureDate();
        if (response.getStatusCode() == 200) {
            ArrayList<Flight> flightsList = (ArrayList<Flight>) response.getData();
            for (Flight f : flightsList) {
                addToFlightFlightComboBox.addItem(f.getId());
                delayFlightFlightIdComboBox.addItem(f.getId());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Error loading flights: " + response.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void populatePlanesComboBox() {
        flightPlaneComboBox.removeAllItems();
        flightPlaneComboBox.addItem("Plane");
        Response response = planeController.getAllPlanesOrderedById();
        if (response.getStatusCode() == 200) {
            ArrayList<Plane> planesList = (ArrayList<Plane>) response.getData();
            for (Plane p : planesList) {
                flightPlaneComboBox.addItem(p.getId());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Error loading planes: " + response.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void populateLocationsComboBoxes() {
        flightDepartureLocationComboBox.removeAllItems();
        flightDepartureLocationComboBox.addItem("Location");
        flightArrivalLocationComboBox.removeAllItems();
        flightArrivalLocationComboBox.addItem("Location");
        flightScaleLocationComboBox.removeAllItems();
        flightScaleLocationComboBox.addItem("No Scale"); // Added for optional scale
        flightScaleLocationComboBox.addItem("Location");

        Response response = locationController.getAllLocationsOrderedById();
        if (response.getStatusCode() == 200) {
            ArrayList<Location> locationsList = (ArrayList<Location>) response.getData();
            for (Location loc : locationsList) {
                flightDepartureLocationComboBox.addItem(loc.getAirportId());
                flightArrivalLocationComboBox.addItem(loc.getAirportId());
                flightScaleLocationComboBox.addItem(loc.getAirportId());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Error loading locations: " + response.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void populateUserSelectComboBox() {
        userSelect.removeAllItems();
        userSelect.addItem("Select User");
        Response response = passengerController.getAllPassengersOrderedById();
        if (response.getStatusCode() == 200) {
            ArrayList<Passenger> passengersList = (ArrayList<Passenger>) response.getData();
            for (Passenger p : passengersList) {
                userSelect.addItem(String.valueOf(p.getId()));
            }
        } else {
            JOptionPane.showMessageDialog(this, "Error loading users: " + response.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void blockPanels() {
        // Block all tabs initially except Administration (index 0)
        for (int i = 1; i < jTabbedPane1.getTabCount(); i++) {
            jTabbedPane1.setEnabledAt(i, false);
        }
    }

    private void generateMonths() {
        for (int i = 1; i < 13; i++) {
            passengerBirthMonthComboBox.addItem("" + i);
            flightDepartureMonthComboBox.addItem("" + i);
            updateInfoBirthMonthComboBox.addItem("" + i);
        }
    }

    private void generateDays() {
        for (int i = 1; i < 32; i++) {
            passengerBirthDayComboBox.addItem("" + i);
            flightDepartureDayComboBox.addItem("" + i);
            updateInfoBirthDayComboBox.addItem("" + i);
        }
    }

    private void generateHours() {
        for (int i = 0; i < 24; i++) {
            flightDepartureHourComboBox.addItem("" + i);
            flightArrivalDurationHourComboBox.addItem("" + i);
            flightScaleDurationHourComboBox.addItem("" + i);
            delayFlightHoursComboBox.addItem("" + i);
        }
    }

    private void generateMinutes() {
        for (int i = 0; i < 60; i++) {
            flightDepartureMinuteComboBox.addItem("" + i);
            flightArrivalDurationMinuteComboBox.addItem("" + i);
            flightScaleDurationMinuteComboBox.addItem("" + i);
            delayFlightMinutesComboBox.addItem("" + i);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelRound1 = new airport.views.PanelRound();
        panelRound2 = new airport.views.PanelRound();
        jButton13 = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        user = new javax.swing.JRadioButton();
        administrator = new javax.swing.JRadioButton();
        userSelect = new javax.swing.JComboBox<>();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        passengerPhoneCodeTextField = new javax.swing.JTextField();
        passengerIdTextField = new javax.swing.JTextField();
        passengerBirthYearTextField = new javax.swing.JTextField();
        passengerCountryTextField = new javax.swing.JTextField();
        passengerPhoneNumberTextField = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        passengerLastNameTextField = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        passengerBirthMonthComboBox = new javax.swing.JComboBox<>();
        passengerFirstNameTextField = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        passengerBirthDayComboBox = new javax.swing.JComboBox<>();
        registerPassengerButton = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        planeIdTextField = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        planeBrandTextField = new javax.swing.JTextField();
        planeModelTextField = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        planeMaxCapacityTextField = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        planeAirlineTextField = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        createPlaneButton = new javax.swing.JButton();
        jPanel13 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        locationIdTextField = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        locationNameTextField = new javax.swing.JTextField();
        locationCityTextField = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        locationCountryTextField = new javax.swing.JTextField();
        locationLatitudeTextField = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        locationLongitudeTextField = new javax.swing.JTextField();
        createLocationButton = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        flightIdTextField = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        flightPlaneComboBox = new javax.swing.JComboBox<>();
        flightDepartureLocationComboBox = new javax.swing.JComboBox<>();
        jLabel24 = new javax.swing.JLabel();
        flightArrivalLocationComboBox = new javax.swing.JComboBox<>();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        flightScaleLocationComboBox = new javax.swing.JComboBox<>();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        flightDepartureYearTextField = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        flightDepartureMonthComboBox = new javax.swing.JComboBox<>();
        jLabel31 = new javax.swing.JLabel();
        flightDepartureDayComboBox = new javax.swing.JComboBox<>();
        jLabel32 = new javax.swing.JLabel();
        flightDepartureHourComboBox = new javax.swing.JComboBox<>();
        jLabel33 = new javax.swing.JLabel();
        flightDepartureMinuteComboBox = new javax.swing.JComboBox<>();
        flightArrivalDurationHourComboBox = new javax.swing.JComboBox<>();
        jLabel34 = new javax.swing.JLabel();
        flightArrivalDurationMinuteComboBox = new javax.swing.JComboBox<>();
        jLabel35 = new javax.swing.JLabel();
        flightScaleDurationHourComboBox = new javax.swing.JComboBox<>();
        flightScaleDurationMinuteComboBox = new javax.swing.JComboBox<>();
        createFlightButton = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jLabel36 = new javax.swing.JLabel();
        updateInfoPassengerIdTextField = new javax.swing.JTextField();
        jLabel37 = new javax.swing.JLabel();
        updateInfoFirstNameTextField = new javax.swing.JTextField();
        jLabel38 = new javax.swing.JLabel();
        updateInfoLastNameTextField = new javax.swing.JTextField();
        jLabel39 = new javax.swing.JLabel();
        updateInfoBirthYearTextField = new javax.swing.JTextField();
        updateInfoBirthMonthComboBox = new javax.swing.JComboBox<>();
        updateInfoBirthDayComboBox = new javax.swing.JComboBox<>();
        updateInfoPhoneNumberTextField = new javax.swing.JTextField();
        jLabel40 = new javax.swing.JLabel();
        updateInfoPhoneCodeTextField = new javax.swing.JTextField();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        updateInfoCountryTextField = new javax.swing.JTextField();
        updateInfoButton = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        addToFlightPassengerIdTextField = new javax.swing.JTextField();
        jLabel44 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        addToFlightFlightComboBox = new javax.swing.JComboBox<>();
        addToFlightButton = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        myFlightsTable = new javax.swing.JTable();
        refreshMyFlightsButton = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        allPassengersTable = new javax.swing.JTable();
        refreshAllPassengersButton = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        allFlightsTable = new javax.swing.JTable();
        refreshAllFlightsButton = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        refreshAllPlanesButton = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        allPlanesTable = new javax.swing.JTable();
        jPanel11 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        allLocationsTable = new javax.swing.JTable();
        refreshAllLocationsButton = new javax.swing.JButton();
        jPanel12 = new javax.swing.JPanel();
        delayFlightHoursComboBox = new javax.swing.JComboBox<>();
        jLabel46 = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        delayFlightFlightIdComboBox = new javax.swing.JComboBox<>();
        jLabel48 = new javax.swing.JLabel();
        delayFlightMinutesComboBox = new javax.swing.JComboBox<>();
        delayFlightButton = new javax.swing.JButton();
        panelRound3 = new airport.views.PanelRound();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        panelRound1.setRadius(40);
        panelRound1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelRound2.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                panelRound2MouseDragged(evt);
            }
        });
        panelRound2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                panelRound2MousePressed(evt);
            }
        });

        jButton13.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jButton13.setText("X");
        jButton13.setBorderPainted(false);
        jButton13.setContentAreaFilled(false);
        jButton13.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelRound2Layout = new javax.swing.GroupLayout(panelRound2);
        panelRound2.setLayout(panelRound2Layout);
        panelRound2Layout.setHorizontalGroup(
            panelRound2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelRound2Layout.createSequentialGroup()
                .addContainerGap(1083, Short.MAX_VALUE)
                .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(17, 17, 17))
        );
        panelRound2Layout.setVerticalGroup(
            panelRound2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRound2Layout.createSequentialGroup()
                .addComponent(jButton13)
                .addGap(0, 12, Short.MAX_VALUE))
        );

        panelRound1.add(panelRound2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1150, -1));

        jTabbedPane1.setFont(new java.awt.Font("Yu Gothic UI", 0, 14)); // NOI18N

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        user.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        user.setText("User");
        user.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                userActionPerformed(evt);
            }
        });
        jPanel1.add(user, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 230, -1, -1));

        administrator.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        administrator.setText("Administrator");
        administrator.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                administratorActionPerformed(evt);
            }
        });
        jPanel1.add(administrator, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 164, -1, -1));

        userSelect.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        userSelect.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select User" }));
        userSelect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                userSelectActionPerformed(evt);
            }
        });
        jPanel1.add(userSelect, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 300, 130, -1));

        jTabbedPane1.addTab("Administration", jPanel1);

        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel1.setText("Country:");
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 400, -1, -1));

        jLabel2.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel2.setText("ID:");
        jPanel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 90, -1, -1));

        jLabel3.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel3.setText("First Name:");
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 160, -1, -1));

        jLabel4.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel4.setText("Last Name:");
        jPanel2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 220, -1, -1));

        jLabel5.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel5.setText("Birthdate:");
        jPanel2.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 280, -1, -1));

        jLabel6.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel6.setText("+");
        jPanel2.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 340, 20, -1));

        passengerPhoneCodeTextField.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jPanel2.add(passengerPhoneCodeTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 340, 50, -1));

        passengerIdTextField.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jPanel2.add(passengerIdTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 90, 130, -1));

        passengerBirthYearTextField.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jPanel2.add(passengerBirthYearTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 280, 90, -1));

        passengerCountryTextField.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jPanel2.add(passengerCountryTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 400, 130, -1));

        passengerPhoneNumberTextField.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jPanel2.add(passengerPhoneNumberTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 340, 130, -1));

        jLabel7.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel7.setText("Phone:");
        jPanel2.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 340, -1, -1));

        jLabel8.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel8.setText("-");
        jPanel2.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 280, 30, -1));

        passengerLastNameTextField.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jPanel2.add(passengerLastNameTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 220, 130, -1));

        jLabel9.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel9.setText("-");
        jPanel2.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 340, 30, -1));

        passengerBirthMonthComboBox.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        passengerBirthMonthComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Month" }));
        jPanel2.add(passengerBirthMonthComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 280, -1, -1));

        passengerFirstNameTextField.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jPanel2.add(passengerFirstNameTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 160, 130, -1));

        jLabel10.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel10.setText("-");
        jPanel2.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 280, 30, -1));

        passengerBirthDayComboBox.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        passengerBirthDayComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Day" }));
        jPanel2.add(passengerBirthDayComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 280, -1, -1));

        registerPassengerButton.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        registerPassengerButton.setText("Register");
        registerPassengerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                registerPassengerButtonActionPerformed(evt);
            }
        });
        jPanel2.add(registerPassengerButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 480, -1, -1));

        jTabbedPane1.addTab("Passenger registration", jPanel2);

        jPanel3.setLayout(null);

        jLabel11.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel11.setText("ID:");
        jPanel3.add(jLabel11);
        jLabel11.setBounds(53, 96, 22, 25);

        planeIdTextField.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jPanel3.add(planeIdTextField);
        planeIdTextField.setBounds(180, 93, 130, 31);

        jLabel12.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel12.setText("Brand:");
        jPanel3.add(jLabel12);
        jLabel12.setBounds(53, 157, 50, 25);

        planeBrandTextField.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jPanel3.add(planeBrandTextField);
        planeBrandTextField.setBounds(180, 154, 130, 31);

        planeModelTextField.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jPanel3.add(planeModelTextField);
        planeModelTextField.setBounds(180, 213, 130, 31);

        jLabel13.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel13.setText("Model:");
        jPanel3.add(jLabel13);
        jLabel13.setBounds(53, 216, 55, 25);

        planeMaxCapacityTextField.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jPanel3.add(planeMaxCapacityTextField);
        planeMaxCapacityTextField.setBounds(180, 273, 130, 31);

        jLabel14.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel14.setText("Max Capacity:");
        jPanel3.add(jLabel14);
        jLabel14.setBounds(53, 276, 109, 25);

        planeAirlineTextField.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jPanel3.add(planeAirlineTextField);
        planeAirlineTextField.setBounds(180, 333, 130, 31);

        jLabel15.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel15.setText("Airline:");
        jPanel3.add(jLabel15);
        jLabel15.setBounds(53, 336, 70, 25);

        createPlaneButton.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        createPlaneButton.setText("Create");
        createPlaneButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createPlaneButtonActionPerformed(evt);
            }
        });
        jPanel3.add(createPlaneButton);
        createPlaneButton.setBounds(490, 480, 120, 40);

        jTabbedPane1.addTab("Airplane registration", jPanel3);

        jLabel16.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel16.setText("Airport ID:");

        locationIdTextField.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N

        jLabel17.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel17.setText("Airport name:");

        locationNameTextField.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N

        locationCityTextField.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N

        jLabel18.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel18.setText("Airport city:");

        jLabel19.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel19.setText("Airport country:");

        locationCountryTextField.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N

        locationLatitudeTextField.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N

        jLabel20.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel20.setText("Airport latitude:");

        jLabel21.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel21.setText("Airport longitude:");

        locationLongitudeTextField.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N

        createLocationButton.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        createLocationButton.setText("Create");
        createLocationButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createLocationButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGap(52, 52, 52)
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel16)
                            .addComponent(jLabel17)
                            .addComponent(jLabel18)
                            .addComponent(jLabel19)
                            .addComponent(jLabel20)
                            .addComponent(jLabel21))
                        .addGap(80, 80, 80)
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(locationLongitudeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(locationIdTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(locationNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(locationCityTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(locationCountryTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(locationLatitudeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGap(515, 515, 515)
                        .addComponent(createLocationButton, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(515, 515, 515))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGap(71, 71, 71)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addComponent(jLabel16)
                        .addGap(36, 36, 36)
                        .addComponent(jLabel17)
                        .addGap(34, 34, 34)
                        .addComponent(jLabel18)
                        .addGap(35, 35, 35)
                        .addComponent(jLabel19)
                        .addGap(35, 35, 35)
                        .addComponent(jLabel20))
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addComponent(locationIdTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(locationNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28)
                        .addComponent(locationCityTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(29, 29, 29)
                        .addComponent(locationCountryTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(29, 29, 29)
                        .addComponent(locationLatitudeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(44, 44, 44)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21)
                    .addComponent(locationLongitudeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 51, Short.MAX_VALUE)
                .addComponent(createLocationButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(47, 47, 47))
        );

        jTabbedPane1.addTab("Location registration", jPanel13);

        jLabel22.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel22.setText("ID:");

        flightIdTextField.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N

        jLabel23.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel23.setText("Plane:");

        flightPlaneComboBox.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        flightPlaneComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Plane" }));

        flightDepartureLocationComboBox.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        flightDepartureLocationComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Location" }));

        jLabel24.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel24.setText("Departure location:");

        flightArrivalLocationComboBox.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        flightArrivalLocationComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Location" }));

        jLabel25.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel25.setText("Arrival location:");

        jLabel26.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel26.setText("Scale location:");

        flightScaleLocationComboBox.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        flightScaleLocationComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Location" }));

        jLabel27.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel27.setText("Duration:");

        jLabel28.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel28.setText("Duration:");

        jLabel29.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel29.setText("Departure date:");

        flightDepartureYearTextField.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N

        jLabel30.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel30.setText("-");

        flightDepartureMonthComboBox.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        flightDepartureMonthComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Month" }));

        jLabel31.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel31.setText("-");

        flightDepartureDayComboBox.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        flightDepartureDayComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Day" }));

        jLabel32.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel32.setText("-");

        flightDepartureHourComboBox.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        flightDepartureHourComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Hour" }));

        jLabel33.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel33.setText("-");

        flightDepartureMinuteComboBox.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        flightDepartureMinuteComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Minute" }));
        flightDepartureMinuteComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                flightDepartureMinuteComboBoxActionPerformed(evt);
            }
        });

        flightArrivalDurationHourComboBox.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        flightArrivalDurationHourComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Hour" }));

        jLabel34.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel34.setText("-");

        flightArrivalDurationMinuteComboBox.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        flightArrivalDurationMinuteComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Minute" }));

        jLabel35.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel35.setText("-");

        flightScaleDurationHourComboBox.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        flightScaleDurationHourComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Hour" }));

        flightScaleDurationMinuteComboBox.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        flightScaleDurationMinuteComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Minute" }));

        createFlightButton.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        createFlightButton.setText("Create");
        createFlightButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createFlightButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(73, 73, 73)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel26)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(flightScaleLocationComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel25)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(flightArrivalLocationComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel24)
                        .addGap(46, 46, 46)
                        .addComponent(flightDepartureLocationComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel22)
                            .addComponent(jLabel23))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(flightIdTextField)
                            .addComponent(flightPlaneComboBox, 0, 130, Short.MAX_VALUE))))
                .addGap(45, 45, 45)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel27)
                    .addComponent(jLabel28)
                    .addComponent(jLabel29))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(flightDepartureYearTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(flightDepartureMonthComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(14, 14, 14)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(flightDepartureDayComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(flightDepartureHourComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(14, 14, 14)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(flightDepartureMinuteComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(30, 30, 30))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(flightArrivalDurationHourComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(14, 14, 14)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addGap(20, 20, 20)
                                        .addComponent(flightArrivalDurationMinuteComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(flightScaleDurationHourComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(14, 14, 14)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addGap(20, 20, 20)
                                        .addComponent(flightScaleDurationMinuteComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(createFlightButton, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(530, 530, 530))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel22))
                    .addComponent(flightIdTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(flightPlaneComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(flightDepartureHourComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel32)
                    .addComponent(jLabel33)
                    .addComponent(flightDepartureMinuteComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel24)
                                .addComponent(flightDepartureLocationComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel29))
                            .addComponent(flightDepartureYearTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(flightDepartureMonthComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel30)
                            .addComponent(jLabel31)
                            .addComponent(flightDepartureDayComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(38, 38, 38)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel25)
                                .addComponent(flightArrivalLocationComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel28))
                            .addComponent(flightArrivalDurationHourComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel34)
                            .addComponent(flightArrivalDurationMinuteComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(34, 34, 34)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(flightScaleDurationHourComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel35)
                            .addComponent(flightScaleDurationMinuteComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel26)
                                .addComponent(flightScaleLocationComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel27)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 134, Short.MAX_VALUE)
                .addComponent(createFlightButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50))
        );

        jTabbedPane1.addTab("Flight registration", jPanel4);

        jLabel36.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel36.setText("ID:");

        updateInfoPassengerIdTextField.setEditable(false);
        updateInfoPassengerIdTextField.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        updateInfoPassengerIdTextField.setEnabled(false);
        updateInfoPassengerIdTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateInfoPassengerIdTextFieldActionPerformed(evt);
            }
        });

        jLabel37.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel37.setText("First Name:");

        updateInfoFirstNameTextField.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N

        jLabel38.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel38.setText("Last Name:");

        updateInfoLastNameTextField.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        updateInfoLastNameTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateInfoLastNameTextFieldActionPerformed(evt);
            }
        });

        jLabel39.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel39.setText("Birthdate:");

        updateInfoBirthYearTextField.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N

        updateInfoBirthMonthComboBox.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        updateInfoBirthMonthComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Month" }));

        updateInfoBirthDayComboBox.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        updateInfoBirthDayComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Day" }));

        updateInfoPhoneNumberTextField.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N

        jLabel40.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel40.setText("-");

        updateInfoPhoneCodeTextField.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N

        jLabel41.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel41.setText("+");

        jLabel42.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel42.setText("Phone:");

        jLabel43.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel43.setText("Country:");

        updateInfoCountryTextField.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N

        updateInfoButton.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        updateInfoButton.setText("Update");
        updateInfoButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateInfoButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(72, 72, 72)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel36)
                                .addGap(108, 108, 108)
                                .addComponent(updateInfoPassengerIdTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel37)
                                .addGap(41, 41, 41)
                                .addComponent(updateInfoFirstNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel38)
                                .addGap(43, 43, 43)
                                .addComponent(updateInfoLastNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel39)
                                .addGap(55, 55, 55)
                                .addComponent(updateInfoBirthYearTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(30, 30, 30)
                                .addComponent(updateInfoBirthMonthComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(34, 34, 34)
                                .addComponent(updateInfoBirthDayComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel42)
                                .addGap(56, 56, 56)
                                .addComponent(jLabel41, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(updateInfoPhoneCodeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(20, 20, 20)
                                .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(updateInfoPhoneNumberTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel43)
                                .addGap(63, 63, 63)
                                .addComponent(updateInfoCountryTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(507, 507, 507)
                        .addComponent(updateInfoButton)))
                .addContainerGap(555, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(59, 59, 59)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel36)
                    .addComponent(updateInfoPassengerIdTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(39, 39, 39)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel37)
                    .addComponent(updateInfoFirstNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel38)
                    .addComponent(updateInfoLastNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel39)
                    .addComponent(updateInfoBirthYearTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(updateInfoBirthMonthComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(updateInfoBirthDayComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel42)
                    .addComponent(jLabel41)
                    .addComponent(updateInfoPhoneCodeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel40)
                    .addComponent(updateInfoPhoneNumberTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel43)
                    .addComponent(updateInfoCountryTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                .addComponent(updateInfoButton)
                .addGap(113, 113, 113))
        );

        jTabbedPane1.addTab("Update info", jPanel5);

        addToFlightPassengerIdTextField.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        addToFlightPassengerIdTextField.setEnabled(false);

        jLabel44.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel44.setText("ID:");

        jLabel45.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel45.setText("Flight:");

        addToFlightFlightComboBox.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        addToFlightFlightComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Flight" }));

        addToFlightButton.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        addToFlightButton.setText("Add");
        addToFlightButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addToFlightButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(64, 64, 64)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel44)
                    .addComponent(jLabel45))
                .addGap(79, 79, 79)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(addToFlightFlightComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addToFlightPassengerIdTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(829, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(addToFlightButton, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(509, 509, 509))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel44))
                    .addComponent(addToFlightPassengerIdTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(35, 35, 35)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel45)
                    .addComponent(addToFlightFlightComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 288, Short.MAX_VALUE)
                .addComponent(addToFlightButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(85, 85, 85))
        );

        jTabbedPane1.addTab("Add to flight", jPanel6);

        myFlightsTable.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        myFlightsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "ID", "Departure Date", "Arrival Date"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(myFlightsTable);

        refreshMyFlightsButton.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        refreshMyFlightsButton.setText("Refresh");
        refreshMyFlightsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshMyFlightsButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(269, 269, 269)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 590, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(291, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(refreshMyFlightsButton)
                .addGap(527, 527, 527))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(61, 61, 61)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
                .addComponent(refreshMyFlightsButton)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Show my flights", jPanel7);

        allPassengersTable.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        allPassengersTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Name", "Birthdate", "Age", "Phone", "Country", "Num Flight"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Long.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(allPassengersTable);

        refreshAllPassengersButton.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        refreshAllPassengersButton.setText("Refresh");
        refreshAllPassengersButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshAllPassengersButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(489, 489, 489)
                        .addComponent(refreshAllPassengersButton))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(47, 47, 47)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 1078, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(25, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap(72, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(refreshAllPassengersButton)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Show all passengers", jPanel8);

        allFlightsTable.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        allFlightsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Departure Airport ID", "Arrival Airport ID", "Scale Airport ID", "Departure Date", "Arrival Date", "Plane ID", "Number Passengers"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(allFlightsTable);

        refreshAllFlightsButton.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        refreshAllFlightsButton.setText("Refresh");
        refreshAllFlightsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshAllFlightsButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 1100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(521, 521, 521)
                        .addComponent(refreshAllFlightsButton)))
                .addContainerGap(21, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(60, 60, 60)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(refreshAllFlightsButton)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Show all flights", jPanel9);

        refreshAllPlanesButton.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        refreshAllPlanesButton.setText("Refresh");
        refreshAllPlanesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshAllPlanesButtonActionPerformed(evt);
            }
        });

        allPlanesTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Brand", "Model", "Max Capacity", "Airline", "Number Flights"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane4.setViewportView(allPlanesTable);

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGap(508, 508, 508)
                        .addComponent(refreshAllPlanesButton))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGap(145, 145, 145)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 816, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(189, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addContainerGap(45, Short.MAX_VALUE)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34)
                .addComponent(refreshAllPlanesButton)
                .addGap(17, 17, 17))
        );

        jTabbedPane1.addTab("Show all planes", jPanel10);

        allLocationsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Airport ID", "Airport Name", "City", "Country"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane5.setViewportView(allLocationsTable);

        refreshAllLocationsButton.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        refreshAllLocationsButton.setText("Refresh");
        refreshAllLocationsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshAllLocationsButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGap(508, 508, 508)
                        .addComponent(refreshAllLocationsButton))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGap(226, 226, 226)
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 652, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(272, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addContainerGap(48, Short.MAX_VALUE)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addComponent(refreshAllLocationsButton)
                .addGap(17, 17, 17))
        );

        jTabbedPane1.addTab("Show all locations", jPanel11);

        delayFlightHoursComboBox.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        delayFlightHoursComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Hour" }));

        jLabel46.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel46.setText("Hours:");

        jLabel47.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel47.setText("ID:");

        delayFlightFlightIdComboBox.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        delayFlightFlightIdComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ID" }));

        jLabel48.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel48.setText("Minutes:");

        delayFlightMinutesComboBox.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        delayFlightMinutesComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Minute" }));

        delayFlightButton.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        delayFlightButton.setText("Delay");
        delayFlightButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                delayFlightButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(94, 94, 94)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(jLabel48)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(delayFlightMinutesComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel47)
                            .addComponent(jLabel46))
                        .addGap(79, 79, 79)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(delayFlightHoursComboBox, 0, 105, Short.MAX_VALUE)
                            .addComponent(delayFlightFlightIdComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(820, 820, 820))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(delayFlightButton)
                .addGap(531, 531, 531))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel47)
                    .addComponent(delayFlightFlightIdComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel46)
                    .addComponent(delayFlightHoursComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel48)
                    .addComponent(delayFlightMinutesComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 307, Short.MAX_VALUE)
                .addComponent(delayFlightButton)
                .addGap(33, 33, 33))
        );

        jTabbedPane1.addTab("Delay flight", jPanel12);

        panelRound1.add(jTabbedPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 41, 1150, 620));

        javax.swing.GroupLayout panelRound3Layout = new javax.swing.GroupLayout(panelRound3);
        panelRound3.setLayout(panelRound3Layout);
        panelRound3Layout.setHorizontalGroup(
            panelRound3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1150, Short.MAX_VALUE)
        );
        panelRound3Layout.setVerticalGroup(
            panelRound3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 36, Short.MAX_VALUE)
        );

        panelRound1.add(panelRound3, new org.netbeans.lib.awtextra.AbsoluteConstraints(-2, 660, 1150, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelRound1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelRound1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    private void panelRound2MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelRound2MousePressed
        x = evt.getX();
        y = evt.getY();
    }//GEN-LAST:event_panelRound2MousePressed

    private void panelRound2MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelRound2MouseDragged
        this.setLocation(this.getLocation().x + evt.getX() - x, this.getLocation().y + evt.getY() - y);
    }//GEN-LAST:event_panelRound2MouseDragged

    private void administratorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_administratorActionPerformed
        if (user.isSelected()) {
            user.setSelected(false);
        }
        userSelect.setEnabled(false);
        userSelect.setSelectedIndex(0);

        // Administrator Role:
        // Enabled tabs: Passenger reg (1), Airplane reg (2), Location reg (3), Flight reg (4),
        // Show all passengers (8), Show all flights (9), Show all planes (10), Show all locations (11), Delay flight (12).
        // Disabled tabs: Update info (5), Add to flight (6), Show my flights (7).
        int[] adminEnabledTabs = {1, 2, 3, 4, 8, 9, 10, 11, 12};
        int[] adminDisabledTabs = {5, 6, 7};

        for (int index : adminEnabledTabs) {
            if (index < jTabbedPane1.getTabCount()) {
                jTabbedPane1.setEnabledAt(index, true);
            }
        }
        for (int index : adminDisabledTabs) {
            if (index < jTabbedPane1.getTabCount()) {
                jTabbedPane1.setEnabledAt(index, false);
            }
        }

        if (evt != null) {
            refreshAllPassengersButtonActionPerformed(null);
            refreshAllFlightsButtonActionPerformed(null);
            refreshAllPlanesButtonActionPerformed(null);
            refreshAllLocationsButtonActionPerformed(null);
            DefaultTableModel myFlightsModel = (DefaultTableModel) myFlightsTable.getModel();
            if (myFlightsModel != null) {
                myFlightsModel.setRowCount(0);
            }
            clearUpdateInfoFields();
        }

    }//GEN-LAST:event_administratorActionPerformed

    private void userActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_userActionPerformed
        userSelect.setEnabled(true);

        // User Role:
        // Enabled tabs: Update info (5), Add to flight (6), Show my flights (7), 
        // Show all flights (9), Show all locations (11).
        // Disabled tabs: All others (1, 2, 3, 4, 8, 10, 12).
        int[] userEnabledTabs = {5, 6, 7, 9, 11};
        // All other operational tabs (1-4, 8, 10, 12) should be disabled
        for (int i = 1; i < jTabbedPane1.getTabCount(); i++) {
            jTabbedPane1.setEnabledAt(i, false); // Disable all first
        }
        for (int index : userEnabledTabs) {
            if (index < jTabbedPane1.getTabCount()) {
                jTabbedPane1.setEnabledAt(index, true);
            }
        }

        if (evt != null) {
            if (userSelect.getSelectedIndex() > 0) {
                userSelectActionPerformed(null);
            } else {
                DefaultTableModel myFlightsModel = (DefaultTableModel) myFlightsTable.getModel();
                if (myFlightsModel != null) {
                    myFlightsModel.setRowCount(0);
                }
                clearUpdateInfoFields();
            }
            refreshAllFlightsButtonActionPerformed(null);
            refreshAllLocationsButtonActionPerformed(null);
        }
    }//GEN-LAST:event_userActionPerformed

    private void registerPassengerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_registerPassengerButtonActionPerformed
        String idStr = passengerIdTextField.getText();
        String firstname = passengerFirstNameTextField.getText();
        String lastname = passengerLastNameTextField.getText();
        String yearStr = passengerBirthYearTextField.getText();
        String monthStr = (String) passengerBirthMonthComboBox.getSelectedItem();
        String dayStr = (String) passengerBirthDayComboBox.getSelectedItem();
        String phoneCodeStr = passengerPhoneCodeTextField.getText();
        String phoneNumberStr = passengerPhoneNumberTextField.getText();
        String country = passengerCountryTextField.getText();

        Response response = passengerController.registerPassenger(idStr, firstname, lastname, yearStr, monthStr, dayStr, phoneCodeStr, phoneNumberStr, country);

        if (response.getStatusCode() == 201) {
            JOptionPane.showMessageDialog(this, response.getMessage(), "Success", JOptionPane.INFORMATION_MESSAGE);
            passengerIdTextField.setText("");
            passengerFirstNameTextField.setText("");
            passengerLastNameTextField.setText("");
            passengerBirthYearTextField.setText("");
            passengerBirthMonthComboBox.setSelectedIndex(0);
            passengerBirthDayComboBox.setSelectedIndex(0);
            passengerPhoneCodeTextField.setText("");
            passengerPhoneNumberTextField.setText("");
            passengerCountryTextField.setText("");
            // this.userSelect.addItem("" + id); // Commented out as per instruction
            populateUserSelectComboBox();
        } else {
            JOptionPane.showMessageDialog(this, "Error: " + response.getMessage(), "Registration Failed", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_registerPassengerButtonActionPerformed

    private void clearUpdateInfoFields() {
        updateInfoPassengerIdTextField.setText("");
        updateInfoFirstNameTextField.setText("");
        updateInfoLastNameTextField.setText("");
        updateInfoBirthYearTextField.setText("");
        updateInfoBirthMonthComboBox.setSelectedIndex(0);
        updateInfoBirthDayComboBox.setSelectedIndex(0);
        updateInfoPhoneCodeTextField.setText("");
        updateInfoPhoneNumberTextField.setText("");
        updateInfoCountryTextField.setText("");
        addToFlightPassengerIdTextField.setText(""); // Also clear ID in "Add to Flight"
    }


    private void createPlaneButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createPlaneButtonActionPerformed
        String id = planeIdTextField.getText();
        String brand = planeBrandTextField.getText();
        String model = planeModelTextField.getText();
        String maxCapacityStr = planeMaxCapacityTextField.getText();
        String airline = planeAirlineTextField.getText();

        Response response = planeController.createPlane(id, brand, model, maxCapacityStr, airline);

        if (response.getStatusCode() == 201) {
            JOptionPane.showMessageDialog(this, response.getMessage(), "Success", JOptionPane.INFORMATION_MESSAGE);
            planeIdTextField.setText("");
            planeBrandTextField.setText("");
            planeModelTextField.setText("");
            planeMaxCapacityTextField.setText("");
            planeAirlineTextField.setText("");
            populatePlanesComboBox();
        } else {
            JOptionPane.showMessageDialog(this, "Error: " + response.getMessage(), "Plane Creation Failed", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_createPlaneButtonActionPerformed


    private void createLocationButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createLocationButtonActionPerformed
        String id = locationIdTextField.getText();
        String name = locationNameTextField.getText();
        String city = locationCityTextField.getText();
        String country = locationCountryTextField.getText();
        String latitudeStr = locationLatitudeTextField.getText();
        String longitudeStr = locationLongitudeTextField.getText();

        Response response = locationController.createLocation(id, name, city, country, latitudeStr, longitudeStr);

        if (response.getStatusCode() == 201) {
            JOptionPane.showMessageDialog(this, response.getMessage(), "Success", JOptionPane.INFORMATION_MESSAGE);
            locationIdTextField.setText("");
            locationNameTextField.setText("");
            locationCityTextField.setText("");
            locationCountryTextField.setText("");
            locationLatitudeTextField.setText("");
            locationLongitudeTextField.setText("");
            populateLocationsComboBoxes();
        } else {
            JOptionPane.showMessageDialog(this, "Error: " + response.getMessage(), "Location Creation Failed", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_createLocationButtonActionPerformed

    private void createFlightButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createFlightButtonActionPerformed
        String id = flightIdTextField.getText();
        String planeId = (String) flightPlaneComboBox.getSelectedItem();
        String departureLocationId = (String) flightDepartureLocationComboBox.getSelectedItem();
        String arrivalLocationId = (String) flightArrivalLocationComboBox.getSelectedItem();
        String scaleLocationIdStr = (String) flightScaleLocationComboBox.getSelectedItem();
        String scaleLocationId = "";

        String depYearStr = flightDepartureYearTextField.getText();
        String depMonthStr = (String) flightDepartureMonthComboBox.getSelectedItem();
        String depDayStr = (String) flightDepartureDayComboBox.getSelectedItem();
        String depHourStr = (String) flightDepartureHourComboBox.getSelectedItem();
        String depMinStr = (String) flightDepartureMinuteComboBox.getSelectedItem();

        String arrDurHoursStr = (String) flightArrivalDurationHourComboBox.getSelectedItem();
        String arrDurMinsStr = (String) flightArrivalDurationMinuteComboBox.getSelectedItem();

        String scaleDurHoursStr = (String) flightScaleDurationHourComboBox.getSelectedItem();
        String scaleDurMinsStr = (String) flightScaleDurationMinuteComboBox.getSelectedItem();

        Response response = flightController.createFlight(id, planeId, departureLocationId, arrivalLocationId, scaleLocationIdStr,
                depYearStr, depMonthStr, depDayStr, depHourStr, depMinStr,
                arrDurHoursStr, arrDurMinsStr, scaleDurHoursStr, scaleDurMinsStr);

        if (response.getStatusCode() == 201) {
            JOptionPane.showMessageDialog(this, response.getMessage(), "Success", JOptionPane.INFORMATION_MESSAGE);
            flightIdTextField.setText("");
            flightPlaneComboBox.setSelectedIndex(0);
            flightDepartureLocationComboBox.setSelectedIndex(0);
            flightArrivalLocationComboBox.setSelectedIndex(0);
            flightScaleLocationComboBox.setSelectedIndex(0);
            flightDepartureYearTextField.setText("");
            flightDepartureMonthComboBox.setSelectedIndex(0);
            flightDepartureDayComboBox.setSelectedIndex(0);
            flightDepartureMonthComboBox.setSelectedIndex(0);
            flightDepartureDayComboBox.setSelectedIndex(0);
            flightDepartureHourComboBox.setSelectedIndex(0);
            flightArrivalDurationMinuteComboBox.setSelectedIndex(0);
            flightScaleDurationHourComboBox.setSelectedIndex(0);
            flightDepartureMinuteComboBox.setSelectedIndex(0);
            populateFlightsComboBox();
        } else {
            JOptionPane.showMessageDialog(this, "Error: " + response.getMessage(), "Flight Creation Failed", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_createFlightButtonActionPerformed

    private void updateInfoButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateInfoButtonActionPerformed
        String idStr = updateInfoPassengerIdTextField.getText();
        if (idStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a passenger from the Administration tab first.", "Update Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        long id = Long.parseLong(idStr);
        String firstname = updateInfoFirstNameTextField.getText();
        String lastname = updateInfoLastNameTextField.getText();
        String yearStr = updateInfoBirthYearTextField.getText();
        String monthStr = (String) updateInfoBirthMonthComboBox.getSelectedItem();
        String dayStr = (String) updateInfoBirthDayComboBox.getSelectedItem();
        String phoneCodeStr = updateInfoPhoneCodeTextField.getText();
        String phoneNumberStr = updateInfoPhoneNumberTextField.getText();
        String country = updateInfoCountryTextField.getText();

        Response response = passengerController.updatePassengerInfo(id, firstname, lastname, yearStr, monthStr, dayStr, phoneCodeStr, phoneNumberStr, country);

        if (response.getStatusCode() == 200) {
            JOptionPane.showMessageDialog(this, response.getMessage(), "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Error: " + response.getMessage(), "Update Failed", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_updateInfoButtonActionPerformed

    private void addToFlightButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addToFlightButtonActionPerformed
        String passengerIdStr = addToFlightPassengerIdTextField.getText();
        if (passengerIdStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a passenger from the Administration tab first.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        long passengerId = Long.parseLong(passengerIdStr);
        String flightId = (String) addToFlightFlightComboBox.getSelectedItem();

        Response response = passengerController.addPassengerToFlight(passengerId, flightId);

        if (response.getStatusCode() == 200) {
            JOptionPane.showMessageDialog(this, response.getMessage(), "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Error: " + response.getMessage(), "Failed to Add Passenger to Flight", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_addToFlightButtonActionPerformed

    private void delayFlightButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_delayFlightButtonActionPerformed
        String flightId = (String) delayFlightFlightIdComboBox.getSelectedItem();
        String hoursStr = (String) delayFlightHoursComboBox.getSelectedItem();
        String minutesStr = (String) delayFlightMinutesComboBox.getSelectedItem();

        // Validation moved to controller

        Response response = flightController.delayFlight(flightId, hoursStr, minutesStr);

        if (response.getStatusCode() == 200) {
            JOptionPane.showMessageDialog(this, response.getMessage(), "Success", JOptionPane.INFORMATION_MESSAGE);
            delayFlightFlightIdComboBox.setSelectedIndex(0);
            delayFlightHoursComboBox.setSelectedIndex(0);
            delayFlightMinutesComboBox.setSelectedIndex(0);
            populateFlightsComboBox(); 
            refreshAllFlightsButtonActionPerformed(null); 
            if (user.isSelected() && userSelect.getSelectedIndex() > 0) { 
                 refreshMyFlightsButtonActionPerformed(null);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Error: " + response.getMessage(), "Delay Flight Failed", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_delayFlightButtonActionPerformed

    private void refreshMyFlightsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshMyFlightsButtonActionPerformed
        if (userSelect.getSelectedIndex() <= 0 || userSelect.getSelectedItem().equals("Select User")) { // No user selected or default item
            DefaultTableModel model = (DefaultTableModel) myFlightsTable.getModel();
            model.setRowCount(0); // Clear table
            return;
        }
        long passengerId = Long.parseLong(userSelect.getItemAt(userSelect.getSelectedIndex()));

        Response response = flightController.getFlightsForPassengerOrderedByDepartureDate(passengerId);
        DefaultTableModel model = (DefaultTableModel) myFlightsTable.getModel();
        model.setRowCount(0);

        if (response.getStatusCode() == 200) {
            ArrayList<Flight> flightsList = (ArrayList<Flight>) response.getData();
            for (Flight flight : flightsList) {
                model.addRow(new Object[]{flight.getId(), flight.getDepartureDate().toString(), flight.calculateArrivalDate().toString()});
            }
        } else {
            JOptionPane.showMessageDialog(this, "Error fetching passenger flights: " + response.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_refreshMyFlightsButtonActionPerformed

    private void refreshAllPassengersButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshAllPassengersButtonActionPerformed
        // TODO add your handling code here:
        DefaultTableModel model = (DefaultTableModel) allPassengersTable.getModel();
        model.setRowCount(0);
        Response response = passengerController.getAllPassengersOrderedById();
        if (response.getStatusCode() == 200) {
            ArrayList<Passenger> passengersList = (ArrayList<Passenger>) response.getData();
            for (Passenger passenger : passengersList) {
                model.addRow(new Object[]{passenger.getId(), passenger.getFullname(), passenger.getBirthDate().toString(), passenger.calculateAge(), passenger.generateFullPhone(), passenger.getCountry(), passenger.getNumFlights()});
            }
        } else {
            JOptionPane.showMessageDialog(this, "Error loading passengers: " + response.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_refreshAllPassengersButtonActionPerformed

    private void refreshAllFlightsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshAllFlightsButtonActionPerformed
        // TODO add your handling code here:
        DefaultTableModel model = (DefaultTableModel) allFlightsTable.getModel();
        model.setRowCount(0);
        Response response = flightController.getAllFlightsOrderedByDepartureDate();
        if (response.getStatusCode() == 200) {
            ArrayList<Flight> flightsList = (ArrayList<Flight>) response.getData();
            for (Flight flight : flightsList) {
                model.addRow(new Object[]{flight.getId(), flight.getDepartureLocation().getAirportId(), flight.getArrivalLocation().getAirportId(), (flight.getScaleLocation() == null ? "-" : flight.getScaleLocation().getAirportId()), flight.getDepartureDate().toString(), flight.calculateArrivalDate().toString(), flight.getPlane().getId(), flight.getNumPassengers()});
            }
        } else {
            JOptionPane.showMessageDialog(this, "Error loading flights: " + response.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_refreshAllFlightsButtonActionPerformed

    private void refreshAllPlanesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshAllPlanesButtonActionPerformed
        // TODO add your handling code here:
        DefaultTableModel model = (DefaultTableModel) allPlanesTable.getModel();
        model.setRowCount(0);
        Response response = planeController.getAllPlanesOrderedById();
        if (response.getStatusCode() == 200) {
            ArrayList<Plane> planesList = (ArrayList<Plane>) response.getData();
            for (Plane plane : planesList) {
                model.addRow(new Object[]{plane.getId(), plane.getBrand(), plane.getModel(), plane.getMaxCapacity(), plane.getAirline(), plane.getNumFlights()});
            }
        } else {
            JOptionPane.showMessageDialog(this, "Error loading planes: " + response.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_refreshAllPlanesButtonActionPerformed

    private void refreshAllLocationsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshAllLocationsButtonActionPerformed
        // TODO add your handling code here:
        DefaultTableModel model = (DefaultTableModel) allLocationsTable.getModel();
        model.setRowCount(0);
        Response response = locationController.getAllLocationsOrderedById();
        if (response.getStatusCode() == 200) {
            ArrayList<Location> locationsList = (ArrayList<Location>) response.getData();
            for (Location location : locationsList) {
                model.addRow(new Object[]{location.getAirportId(), location.getAirportName(), location.getAirportCity(), location.getAirportCountry()});
            }
        } else {
            JOptionPane.showMessageDialog(this, "Error loading locations: " + response.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_refreshAllLocationsButtonActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        System.exit(0);
    }//GEN-LAST:event_jButton13ActionPerformed

    private void userSelectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_userSelectActionPerformed
        try {
            String id = userSelect.getSelectedItem().toString();
            if (!id.equals(userSelect.getItemAt(0))) {
                updateInfoPassengerIdTextField.setText(id);
                addToFlightPassengerIdTextField.setText(id);
            } else {
                updateInfoPassengerIdTextField.setText("");
                addToFlightPassengerIdTextField.setText("");
            }
        } catch (Exception e) {
        }
    }//GEN-LAST:event_userSelectActionPerformed

    private void flightDepartureMinuteComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_flightDepartureMinuteComboBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_flightDepartureMinuteComboBoxActionPerformed

    private void updateInfoPassengerIdTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateInfoPassengerIdTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_updateInfoPassengerIdTextFieldActionPerformed

    private void updateInfoLastNameTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateInfoLastNameTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_updateInfoLastNameTextFieldActionPerformed

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addToFlightButton;
    private javax.swing.JComboBox<String> addToFlightFlightComboBox;
    private javax.swing.JTextField addToFlightPassengerIdTextField;
    private javax.swing.JRadioButton administrator;
    private javax.swing.JTable allFlightsTable;
    private javax.swing.JTable allLocationsTable;
    private javax.swing.JTable allPassengersTable;
    private javax.swing.JTable allPlanesTable;
    private javax.swing.JButton createFlightButton;
    private javax.swing.JButton createLocationButton;
    private javax.swing.JButton createPlaneButton;
    private javax.swing.JButton delayFlightButton;
    private javax.swing.JComboBox<String> delayFlightFlightIdComboBox;
    private javax.swing.JComboBox<String> delayFlightHoursComboBox;
    private javax.swing.JComboBox<String> delayFlightMinutesComboBox;
    private javax.swing.JComboBox<String> flightArrivalDurationHourComboBox;
    private javax.swing.JComboBox<String> flightArrivalDurationMinuteComboBox;
    private javax.swing.JComboBox<String> flightArrivalLocationComboBox;
    private javax.swing.JComboBox<String> flightDepartureDayComboBox;
    private javax.swing.JComboBox<String> flightDepartureHourComboBox;
    private javax.swing.JComboBox<String> flightDepartureLocationComboBox;
    private javax.swing.JComboBox<String> flightDepartureMinuteComboBox;
    private javax.swing.JComboBox<String> flightDepartureMonthComboBox;
    private javax.swing.JTextField flightDepartureYearTextField;
    private javax.swing.JTextField flightIdTextField;
    private javax.swing.JComboBox<String> flightPlaneComboBox;
    private javax.swing.JComboBox<String> flightScaleDurationHourComboBox;
    private javax.swing.JComboBox<String> flightScaleDurationMinuteComboBox;
    private javax.swing.JComboBox<String> flightScaleLocationComboBox;
    private javax.swing.JButton jButton13;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField locationCityTextField;
    private javax.swing.JTextField locationCountryTextField;
    private javax.swing.JTextField locationIdTextField;
    private javax.swing.JTextField locationLatitudeTextField;
    private javax.swing.JTextField locationLongitudeTextField;
    private javax.swing.JTextField locationNameTextField;
    private javax.swing.JTable myFlightsTable;
    private airport.views.PanelRound panelRound1;
    private airport.views.PanelRound panelRound2;
    private airport.views.PanelRound panelRound3;
    private javax.swing.JComboBox<String> passengerBirthDayComboBox;
    private javax.swing.JComboBox<String> passengerBirthMonthComboBox;
    private javax.swing.JTextField passengerBirthYearTextField;
    private javax.swing.JTextField passengerCountryTextField;
    private javax.swing.JTextField passengerFirstNameTextField;
    private javax.swing.JTextField passengerIdTextField;
    private javax.swing.JTextField passengerLastNameTextField;
    private javax.swing.JTextField passengerPhoneCodeTextField;
    private javax.swing.JTextField passengerPhoneNumberTextField;
    private javax.swing.JTextField planeAirlineTextField;
    private javax.swing.JTextField planeBrandTextField;
    private javax.swing.JTextField planeIdTextField;
    private javax.swing.JTextField planeMaxCapacityTextField;
    private javax.swing.JTextField planeModelTextField;
    private javax.swing.JButton refreshAllFlightsButton;
    private javax.swing.JButton refreshAllLocationsButton;
    private javax.swing.JButton refreshAllPassengersButton;
    private javax.swing.JButton refreshAllPlanesButton;
    private javax.swing.JButton refreshMyFlightsButton;
    private javax.swing.JButton registerPassengerButton;
    private javax.swing.JComboBox<String> updateInfoBirthDayComboBox;
    private javax.swing.JComboBox<String> updateInfoBirthMonthComboBox;
    private javax.swing.JTextField updateInfoBirthYearTextField;
    private javax.swing.JButton updateInfoButton;
    private javax.swing.JTextField updateInfoCountryTextField;
    private javax.swing.JTextField updateInfoFirstNameTextField;
    private javax.swing.JTextField updateInfoLastNameTextField;
    private javax.swing.JTextField updateInfoPassengerIdTextField;
    private javax.swing.JTextField updateInfoPhoneCodeTextField;
    private javax.swing.JTextField updateInfoPhoneNumberTextField;
    private javax.swing.JRadioButton user;
    private javax.swing.JComboBox<String> userSelect;
    // End of variables declaration//GEN-END:variables
}
