package Uas_Pbo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Vector;
import java.sql.*;

/**
 * Kelas utama untuk sistem aplikasi parkir
 * Mengelola GUI dan fungsionalitas utama aplikasi
 */
public class ParkirAppSystem extends JFrame {
    // Komponen GUI
    private JTextField tfPlatMasuk, tfPlatKeluar, tfSearch;
    private JComboBox<String> cbJenisMasuk, cbSort;
    private JLabel lblJamMasuk, lblJamKeluar, lbldurasi_menit, lblTarif, lblClock;
    private Timer timer;
    private JTable table;
    private DefaultTableModel tableModel;
    
    // Data user
    private String currentRole;
    
    /**
     * Constructor untuk ParkirAppSystem
     * @param user username yang login
     * @param role role user (admin/operator)
     */
    public ParkirAppSystem(String user, String role) {
        this.currentRole = role;
        
        initializeFrame();
        createComponents();
        setupLayout();
        setupEventHandlers();
        loadDataFromDatabase();
        startClock();
    }
    
    /**
     * Inisialisasi frame utama
     */
    private void initializeFrame() {
        setTitle("Aplikasi Parkir - Form Masuk & Keluar");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
    }
    
    /**
     * Membuat komponen-komponen GUI
     */
    private void createComponents() {
        // Text fields
        tfPlatMasuk = new JTextField();
        tfPlatKeluar = new JTextField();
        tfSearch = new JTextField();
        
        // Combo boxes
        cbJenisMasuk = new JComboBox<>(new String[]{"Motor", "Mobil"});
        cbSort = new JComboBox<>(new String[]{"A-Z", "Z-A"});
        
        // Labels
        lblJamMasuk = new JLabel("Auto-generated");
        lblJamKeluar = new JLabel("Auto-generated");
        lbldurasi_menit = new JLabel("-");
        lblTarif = new JLabel("-");
        lblClock = new JLabel();
        
        // Setup styles
        setupComponentStyles();
    }
    
    /**
     * Setup styling untuk komponen
     */
    private void setupComponentStyles() {
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 16);
        
        tfPlatMasuk.setFont(fieldFont);
        tfPlatMasuk.setBorder(BorderFactory.createLineBorder(new Color(33, 150, 243), 1));
        
        tfPlatKeluar.setFont(fieldFont);
        tfPlatKeluar.setBorder(BorderFactory.createLineBorder(new Color(33, 150, 243), 1));
        
        tfSearch.setFont(new Font("Arial", Font.PLAIN, 15));
        tfSearch.setPreferredSize(new Dimension(350, 32));
        
        cbJenisMasuk.setFont(fieldFont);
        cbJenisMasuk.setBorder(BorderFactory.createLineBorder(new Color(33, 150, 243), 1));
        
        cbSort.setFont(new Font("Arial", Font.PLAIN, 15));
        cbSort.setPreferredSize(new Dimension(120, 32));
        
        lblJamMasuk.setFont(fieldFont);
        lblJamKeluar.setFont(fieldFont);
        lbldurasi_menit.setFont(fieldFont);
        lblTarif.setFont(fieldFont);
    }
    
    /**
     * Setup layout utama aplikasi
     */
    private void setupLayout() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(232, 245, 253));
        
        // Menu bar
        setJMenuBar(createMenuBar());
        
        // Clock panel
        mainPanel.add(createClockPanel(), BorderLayout.NORTH);
        
        // Tabbed pane for forms
        mainPanel.add(createTabbedPane(), BorderLayout.CENTER);
        
        // Table and search panel
        mainPanel.add(createSouthPanel(), BorderLayout.SOUTH);
        
        setContentPane(mainPanel);
    }
    
    /**
     * Membuat menu bar
     */
    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(new Color(25, 118, 210));

        JMenu menu = new JMenu(" Menu");
        menu.setFont(new Font("Arial", Font.BOLD, 16));
        menu.setForeground(Color.WHITE);
        menu.setOpaque(true);
        menu.setBackground(new Color(25, 118, 210));

        // Print menu item
        JMenuItem printItem = new JMenuItem("Cetak Struk");
        styleMenuItem(printItem, new Color(76, 175, 80));
        printItem.setBackground(new Color(76, 175, 80));
        printItem.setForeground(Color.WHITE);
        printItem.setOpaque(true);
        printItem.addActionListener(e -> showPrintDialog());
        menu.add(printItem);

        // Delete menu item
        JMenuItem deleteItem = new JMenuItem("Hapus Data");
        styleMenuItem(deleteItem, new Color(33, 150, 243));
        deleteItem.setBackground(new Color(33, 150, 243));
        deleteItem.setForeground(Color.WHITE);
        deleteItem.setOpaque(true);
        deleteItem.addActionListener(e -> deleteSelectedRow());
        menu.add(deleteItem);

        JMenuItem editItem = new JMenuItem("Edit Data Parkir");
        styleMenuItem(editItem, new Color(255, 152, 0));
        editItem.setBackground(new Color(255, 152, 0));
        editItem.setForeground(Color.WHITE);
        editItem.setOpaque(true);
        editItem.addActionListener(e -> showEditDialog());
        menu.add(editItem);
        
        menu.addSeparator();

        // Logout menu item
        JMenuItem logoutItem = new JMenuItem("Logout");
        styleMenuItem(logoutItem, new Color(244, 67, 54));
        logoutItem.setBackground(new Color(244, 67, 54));
        logoutItem.setForeground(Color.WHITE);
        logoutItem.setOpaque(true);
        logoutItem.addActionListener(e -> logout());
        menu.add(logoutItem);

        // Set warna background menu bar
        menuBar.setOpaque(true);
        menuBar.setBackground(new Color(25, 118, 210));
        menuBar.add(menu);
        return menuBar;
    }

    /**
     * Style untuk menu item
     */
    private void styleMenuItem(JMenuItem item, Color bgColor) {
        item.setBackground(bgColor);
        item.setForeground(Color.WHITE);
        item.setOpaque(true);
        item.setPreferredSize(new Dimension(180, 35));
        item.setFont(new Font("Arial", Font.BOLD, 14));
        item.setBorder(BorderFactory.createEmptyBorder(2, 10, 2, 10));
        // Hover effect
        item.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                item.setBackground(bgColor.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                item.setBackground(bgColor);
            }
        });
    }
    
    /**
     * Membuat panel clock
     */
    private JPanel createClockPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(25, 118, 210));
        panel.setPreferredSize(new Dimension(1000, 50));
        
        lblClock.setFont(new Font("Arial", Font.BOLD, 20));
        lblClock.setForeground(Color.WHITE);
        lblClock.setHorizontalAlignment(SwingConstants.RIGHT);
        panel.add(lblClock, BorderLayout.EAST);
        
        JLabel title = new JLabel(" SISTEM PARKIR", SwingConstants.LEFT);
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        panel.add(title, BorderLayout.WEST);
        
        return panel;
    }
    
    /**
     * Membuat tabbed pane untuk form masuk dan keluar
     */
    private JTabbedPane createTabbedPane() {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 15));
        tabbedPane.setBackground(new Color(197, 225, 165));
        
        JPanel masukPanel = createMasukPanel();
        JPanel keluarPanel = createKeluarPanel();
        
        masukPanel.setPreferredSize(new Dimension(400, 300));
        keluarPanel.setPreferredSize(new Dimension(400, 300));
        
        tabbedPane.addTab("Kendaraan Masuk", masukPanel);
        tabbedPane.addTab("Kendaraan Keluar", keluarPanel);
        
        return tabbedPane;
    }
    
    /**
     * Membuat panel untuk form kendaraan masuk
     */
    private JPanel createMasukPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(232, 245, 253));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        
        JPanel formPanel = createMasukFormPanel();
        panel.add(formPanel, gbc);
        
        return panel;
    }
    
    /**
     * Membuat form panel untuk kendaraan masuk
     */
    private JPanel createMasukFormPanel() {
        JPanel mainFormPanel = new JPanel(new GridBagLayout());
        mainFormPanel.setBackground(new Color(232, 245, 253));
        
        JPanel leftPanel = createMasukLeftPanel();
        JPanel rightPanel = createMasukRightPanel();
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridy = 0;
        
        gbc.gridx = 0;
        gbc.weightx = 0.8;
        gbc.weighty = 1.0;
        mainFormPanel.add(leftPanel, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.2;
        mainFormPanel.add(rightPanel, gbc);
        
        return mainFormPanel;
    }
    
    /**
     * Membuat left panel untuk form masuk
     */
    private JPanel createMasukLeftPanel() {
        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(33, 150, 243), 2),
            BorderFactory.createEmptyBorder(18, 18, 18, 18)
        ));
        
        Font labelFont = new Font("Segoe UI", Font.BOLD, 15);
        
        JLabel lblTitle = new JLabel("Form Kendaraan Masuk", JLabel.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(new Color(33, 150, 243));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        leftPanel.add(lblTitle, gbc);
        
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1;
        leftPanel.add(createLabel("Plat Nomor:", labelFont), gbc);
        gbc.gridx = 1;
        leftPanel.add(tfPlatMasuk, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        leftPanel.add(createLabel("Jenis Kendaraan:", labelFont), gbc);
        gbc.gridx = 1;
        leftPanel.add(cbJenisMasuk, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        leftPanel.add(createLabel("Tanggal Masuk:", labelFont), gbc);
        gbc.gridx = 1;
        leftPanel.add(lblJamMasuk, gbc);
        
        return leftPanel;
    }
    
    /**
     * Membuat right panel untuk form masuk
     */
    private JPanel createMasukRightPanel() {
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(33, 150, 243), 2),
            BorderFactory.createEmptyBorder(18, 18, 18, 18)
        ));
        
        JButton btnMasuk = new JButton("Simpan Masuk");
        styleButton(btnMasuk, new Color(76, 175, 80));
        btnMasuk.setFont(new Font("Segoe UI", Font.BOLD, 28));
        btnMasuk.addActionListener(e -> handleKendaraanMasuk());
        
        rightPanel.add(btnMasuk, BorderLayout.CENTER);
        return rightPanel;
    }
    
    /**
     * Membuat panel untuk form kendaraan keluar
     */
    private JPanel createKeluarPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(232, 245, 253));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        
        JPanel formPanel = createKeluarFormPanel();
        panel.add(formPanel, gbc);
        
        return panel;
    }
    
    /**
     * Membuat form panel untuk kendaraan keluar
     */
    private JPanel createKeluarFormPanel() {
        JPanel mainFormPanel = new JPanel(new GridBagLayout());
        mainFormPanel.setBackground(new Color(232, 245, 253));
        
        JPanel leftPanel = createKeluarLeftPanel();
        JPanel rightPanel = createKeluarRightPanel();
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridy = 0;
        
        gbc.gridx = 0;
        gbc.weightx = 0.8;
        gbc.weighty = 1.0;
        mainFormPanel.add(leftPanel, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.2;
        mainFormPanel.add(rightPanel, gbc);
        
        return mainFormPanel;
    }
    
    /**
     * Membuat left panel untuk form keluar
     */
    private JPanel createKeluarLeftPanel() {
        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(33, 150, 243), 2),
            BorderFactory.createEmptyBorder(18, 18, 18, 18)
        ));
        
        Font labelFont = new Font("Segoe UI", Font.BOLD, 15);
        
        JLabel lblTitle = new JLabel("Form Kendaraan Keluar", JLabel.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(new Color(33, 150, 243));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        leftPanel.add(lblTitle, gbc);
        
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1;
        leftPanel.add(createLabel("Plat Nomor:", labelFont), gbc);
        gbc.gridx = 1;
        leftPanel.add(tfPlatKeluar, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        leftPanel.add(createLabel("Tanggal Keluar:", labelFont), gbc);
        gbc.gridx = 1;
        leftPanel.add(lblJamKeluar, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        leftPanel.add(createLabel("Durasi Parkir:", labelFont), gbc);
        gbc.gridx = 1;
        leftPanel.add(lbldurasi_menit, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        leftPanel.add(createLabel("Tarif Parkir:", labelFont), gbc);
        gbc.gridx = 1;
        leftPanel.add(lblTarif, gbc);
        
        return leftPanel;
    }
    
    /**
     * Membuat right panel untuk form keluar
     */
    private JPanel createKeluarRightPanel() {
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(33, 150, 243), 2),
            BorderFactory.createEmptyBorder(18, 18, 18, 18)
        ));
        
        JButton btnKeluar = new JButton("Simpan Keluar");
        styleButton(btnKeluar, new Color(255, 152, 0));
        btnKeluar.setFont(new Font("Segoe UI", Font.BOLD, 28));
        btnKeluar.addActionListener(e -> handleKendaraanKeluar());
        
        rightPanel.add(btnKeluar, BorderLayout.CENTER);
        return rightPanel;
    }
    
    /**
     * Membuat panel bagian selatan dengan search dan table
     */
    private JPanel createSouthPanel() {
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.setBackground(new Color(232, 245, 253));
        
        southPanel.add(createSearchPanel(), BorderLayout.NORTH);
        southPanel.add(createTablePanel(), BorderLayout.CENTER);
        
        return southPanel;
    }
    
    private JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel(new GridBagLayout());
        searchPanel.setBackground(new Color(232, 245, 253));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JLabel lblSearch = new JLabel("Cari/Filter Plat Nomor: ");
        lblSearch.setFont(new Font("Arial", Font.BOLD, 15));
        
        JButton btnSearch = new JButton("Cari");
        styleButton(btnSearch, new Color(33, 150, 243));
        btnSearch.setFont(new Font("Arial", Font.BOLD, 14));
        btnSearch.setPreferredSize(new Dimension(100, 32));
        
        JButton btnReset = new JButton("Reset");
        styleButton(btnReset, new Color(76, 175, 80));
        btnReset.setFont(new Font("Arial", Font.BOLD, 14));
        btnReset.setPreferredSize(new Dimension(100, 32));
        
        JLabel lblSort = new JLabel("Urutkan: ");
        lblSort.setFont(new Font("Arial", Font.BOLD, 15));
        
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        searchPanel.add(lblSearch, gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        searchPanel.add(tfSearch, gbc);
        gbc.gridx = 2; gbc.weightx = 0;
        searchPanel.add(btnSearch, gbc);
        gbc.gridx = 3;
        searchPanel.add(btnReset, gbc);
        gbc.gridx = 4;
        searchPanel.add(Box.createHorizontalStrut(20), gbc);
        gbc.gridx = 5;
        searchPanel.add(lblSort, gbc);
        gbc.gridx = 6;
        searchPanel.add(cbSort, gbc);
        
        // Event handlers untuk search
        btnSearch.addActionListener(e -> performSearch());
        tfSearch.addActionListener(e -> performSearch());
        btnReset.addActionListener(e -> resetSearch());
        cbSort.addActionListener(e -> performSearch());
        
        return searchPanel;
    }
    
    /**
     * Membuat panel table
     */
    private JPanel createTablePanel() {
        initTable();
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(1000, 400));
        scrollPane.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(33, 150, 243), 2),
            "Data Parkir",
            0, 0,
            new Font("Arial", Font.BOLD, 16),
            new Color(33, 150, 243)
        ));
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Inisialisasi table
     */
    private void initTable() {
        String[] columnNames = {"Plat Nomor", "Jenis", "Tanggal Masuk", "Tanggal Keluar", "Durasi Parkir", "Tarif"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return "admin".equals(currentRole) && col != 0;
            }
        };
        
        table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        table.setRowHeight(32);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 18));
        table.setSelectionBackground(new Color(197, 225, 165));
        
        // Center alignment untuk semua kolom
        javax.swing.table.DefaultTableCellRenderer centerRenderer = 
            new javax.swing.table.DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        // Mouse click listener
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = table.getSelectedRow();
                if (row != -1) {
                    String plat = (String) tableModel.getValueAt(row, 0);
                    tfPlatKeluar.setText(plat);
                }
            }
        });
    }
    
    /**
     * Setup event handlers
     */
    private void setupEventHandlers() {
        // Event handlers sudah diatur di masing-masing method create
    }
    
    /**
     * Mulai timer untuk clock
     */
    private void startClock() {
        timer = new Timer(1000, e -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            lblClock.setText("Waktu Sekarang: " + LocalDateTime.now().format(formatter));
        });
        timer.start();
    }
    
    /**
     * Handle kendaraan masuk
     */
    private void handleKendaraanMasuk() {
        String plat = tfPlatMasuk.getText().trim();
        String jenis = (String) cbJenisMasuk.getSelectedItem();

        if (plat.isEmpty()) {
            showMessage("Plat nomor tidak boleh kosong!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Format waktu masuk: dd/MM/yyyy HH:mm:ss untuk display, yyyy-MM-dd HH:mm:ss untuk database
        String jamMasukDisplay = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        String jamMasukDb = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        lblJamMasuk.setText(jamMasukDisplay);

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO parkir (plat, jenis, tanggal_masuk) VALUES (?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, plat);
            ps.setString(2, jenis);
            ps.setString(3, jamMasukDb);
            ps.executeUpdate();

            // Tambah ke table
            Vector<String> row = new Vector<>();
            row.add(plat);
            row.add(jenis);
            row.add(jamMasukDisplay);
            row.add("");
            row.add("");
            row.add("");
            tableModel.addRow(row);

            showMessage("Kendaraan masuk dicatat.\nWaktu Masuk: " + jamMasukDisplay, "Sukses", JOptionPane.INFORMATION_MESSAGE);
            tfPlatMasuk.setText("");

        } catch (Exception ex) {
            showMessage("Gagal simpan ke database: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Handle kendaraan keluar
     */
    private void handleKendaraanKeluar() {
        String plat = tfPlatKeluar.getText().trim();

        if (plat.isEmpty()) {
            showMessage("Plat nomor tidak boleh kosong!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Format waktu keluar: dd/MM/yyyy HH:mm:ss untuk display, yyyy-MM-dd HH:mm:ss untuk database
        String jamKeluarDisplay = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        String jamKeluarDb = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        boolean found = false;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            if (tableModel.getValueAt(i, 0).equals(plat) &&
                (tableModel.getValueAt(i, 3) == null || tableModel.getValueAt(i, 3).toString().isEmpty())) {

                // Ambil waktu masuk dari tabel (bisa format display)
                String jamMasukDisplay = (String) tableModel.getValueAt(i, 2);
                // Konversi ke format database untuk perhitungan
                LocalDateTime jamMasukLdt;
                try {
                    jamMasukLdt = LocalDateTime.parse(jamMasukDisplay, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
                } catch (Exception e) {
                    // fallback jika data lama masih format yyyy-MM-dd HH:mm:ss
                    jamMasukLdt = LocalDateTime.parse(jamMasukDisplay, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                }
                String jamMasukDb = jamMasukLdt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                tableModel.setValueAt(jamKeluarDisplay, i, 3);
                lblJamKeluar.setText(jamKeluarDisplay);

                long durasiMenit = calculateDuration(jamMasukDb, jamKeluarDb);

                String jenis = (String) tableModel.getValueAt(i, 1);
                Kendaraan kendaraan = createKendaraan(jenis, plat, jamMasukDb);

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime masuk = LocalDateTime.parse(jamMasukDb, formatter);
                LocalDateTime keluar = LocalDateTime.parse(jamKeluarDb, formatter);
                long days = java.time.Duration.between(masuk.toLocalDate().atStartOfDay(),
                                                     keluar.toLocalDate().atStartOfDay()).toDays();

                long tarif = kendaraan.hitungTarif(durasiMenit, days);

                lbldurasi_menit.setText("" + durasiMenit + " menit");
                lblTarif.setText("Rp " + String.format("%,d", tarif));

                tableModel.setValueAt(String.valueOf(durasiMenit), i, 4);
                tableModel.setValueAt(String.valueOf(tarif), i, 5);

                updateDatabase(plat, jamMasukDb, jamKeluarDb, durasiMenit, tarif);

                found = true;
                table.setRowSelectionInterval(i, i);
                showPrintDialog();
                break;
            }
        }

        if (!found) {
            showMessage("Data kendaraan tidak ditemukan atau sudah keluar.", "Peringatan", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    /**
     * Update database dengan data keluar
     */
    private void updateDatabase(String plat, String jamMasuk, String jamKeluar, long durasi, long tarif) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "UPDATE parkir SET tanggal_keluar=?, durasi_menit=?, tarif=? WHERE plat=? AND tanggal_masuk=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, jamKeluar);
            ps.setLong(2, durasi);
            ps.setLong(3, tarif);
            ps.setString(4, plat);
            ps.setString(5, jamMasuk);
            ps.executeUpdate();
        } catch (Exception ex) {
            showMessage("Gagal update database: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Factory method untuk membuat objek kendaraan
     */
    private Kendaraan createKendaraan(String jenis, String plat, String jamMasuk) {
        if (jenis.equalsIgnoreCase("Motor")) {
            return new Motor(plat, jamMasuk);
        } else {
            return new Mobil(plat, jamMasuk);
        }
    }
    
    /**
     * Hitung durasi parkir dalam menit
     */
    private long calculateDuration(String masuk, String keluar) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime start = LocalDateTime.parse(masuk, formatter);
        LocalDateTime end = LocalDateTime.parse(keluar, formatter);
        return java.time.Duration.between(start, end).toMinutes();
    }
    
    /**
     * Load data dari database
     */
    private void loadDataFromDatabase() {
        tableModel.setRowCount(0);
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT plat, jenis, tanggal_masuk, IFNULL(tanggal_keluar, '') as tanggal_keluar, " +
                         "IFNULL(durasi_menit, '') as durasi_menit, IFNULL(tarif, '') as tarif FROM parkir";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Vector<String> row = new Vector<>();
                row.add(rs.getString("plat"));
                row.add(rs.getString("jenis"));
                // Format tanggal masuk dan keluar ke dd/MM/yyyy HH:mm:ss jika ada
                String tglMasuk = rs.getString("tanggal_masuk");
                String tglKeluar = rs.getString("tanggal_keluar");
                if (tglMasuk != null && !tglMasuk.isEmpty()) {
                    try {
                        LocalDateTime ldt = LocalDateTime.parse(tglMasuk, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                        tglMasuk = ldt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
                    } catch (Exception e) {}
                }
                if (tglKeluar != null && !tglKeluar.isEmpty()) {
                    try {
                        LocalDateTime ldt = LocalDateTime.parse(tglKeluar, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                        tglKeluar = ldt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
                    } catch (Exception e) {}
                }
                row.add(tglMasuk);
                row.add(tglKeluar);
                row.add(rs.getString("durasi_menit"));
                row.add(rs.getString("tarif"));
                tableModel.addRow(row);
            }
        } catch (Exception ex) {
            showMessage("Gagal load data dari database: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Perform search
     */
    private void performSearch() {
        String searchText = tfSearch.getText().trim();
        int sortIndex = cbSort.getSelectedIndex();
        searchByPlat(searchText, sortIndex);
    }
    
    /**
     * Reset search
     */
    private void resetSearch() {
        tfSearch.setText("");
        cbSort.setSelectedIndex(0);
        loadDataFromDatabase();
    }
    
    /**
     * Search data berdasarkan plat nomor
     */
    private void searchByPlat(String plat, int sortIndex) {
        tableModel.setRowCount(0);
        String order = (sortIndex == 0) ? "ASC" : "DESC";

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT plat, jenis, tanggal_masuk, IFNULL(tanggal_keluar, '') as tanggal_keluar, " +
                         "IFNULL(durasi_menit, '') as durasi_menit, IFNULL(tarif, '') as tarif FROM parkir";
            if (!plat.isEmpty()) {
                sql += " WHERE plat LIKE ?";
            }
            sql += " ORDER BY plat " + order;

            PreparedStatement ps = conn.prepareStatement(sql);
            if (!plat.isEmpty()) {
                ps.setString(1, "%" + plat + "%");
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Vector<String> row = new Vector<>();
                row.add(rs.getString("plat"));
                row.add(rs.getString("jenis"));
                // Format tanggal masuk dan keluar ke dd/MM/yyyy HH:mm:ss jika ada
                String tglMasuk = rs.getString("tanggal_masuk");
                String tglKeluar = rs.getString("tanggal_keluar");
                if (tglMasuk != null && !tglMasuk.isEmpty()) {
                    try {
                        LocalDateTime ldt = LocalDateTime.parse(tglMasuk, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                        tglMasuk = ldt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
                    } catch (Exception e) {}
                }
                if (tglKeluar != null && !tglKeluar.isEmpty()) {
                    try {
                        LocalDateTime ldt = LocalDateTime.parse(tglKeluar, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                        tglKeluar = ldt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
                    } catch (Exception e) {}
                }
                row.add(tglMasuk);
                row.add(tglKeluar);
                row.add(rs.getString("durasi_menit"));
                row.add(rs.getString("tarif"));
                tableModel.addRow(row);
            }
        } catch (Exception ex) {
            showMessage("Gagal mencari/mengurutkan data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Hapus baris yang dipilih dan dari database
     */
    private void deleteSelectedRow() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            int option = JOptionPane.showConfirmDialog(this, 
                "Apakah Anda yakin ingin menghapus data ini?", 
                "Konfirmasi Hapus", 
                JOptionPane.YES_NO_OPTION);

            if (option == JOptionPane.YES_OPTION) {
                // Ambil data kunci
                String plat = (String) tableModel.getValueAt(selectedRow, 0);
                String tglMasukDisplay = (String) tableModel.getValueAt(selectedRow, 2);
                String tglMasukDb = tglMasukDisplay;
                try {
                    if (tglMasukDisplay != null && !tglMasukDisplay.isEmpty()) {
                        tglMasukDb = LocalDateTime.parse(tglMasukDisplay, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))
                            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    }
                } catch (Exception e) {
                    // fallback jika sudah format db
                }
                // Hapus dari database
                try (Connection conn = DBConnection.getConnection()) {
                    String sql = "DELETE FROM parkir WHERE plat=? AND tanggal_masuk=?";
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setString(1, plat);
                    ps.setString(2, tglMasukDb);
                    ps.executeUpdate();
                } catch (Exception ex) {
                    showMessage("Gagal hapus data dari database: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
                // Refresh tabel
                loadDataFromDatabase();
            }
        } else {
            showMessage("Pilih baris data yang ingin dihapus!", "Peringatan", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Show edit dialog dan update database jika disimpan
     */
    private void showEditDialog() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            showMessage("Pilih baris data yang ingin diedit!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        EditDialog dialog = new EditDialog(this, tableModel, selectedRow);
        dialog.setVisible(true);

        if (dialog.isSaved()) {
            // Ambil data terbaru dari model
            String plat = (String) tableModel.getValueAt(selectedRow, 0);
            String jenis = (String) tableModel.getValueAt(selectedRow, 1);
            String tglMasukDisplay = (String) tableModel.getValueAt(selectedRow, 2);
            String tglKeluarDisplay = (String) tableModel.getValueAt(selectedRow, 3);
            String durasiStr = (String) tableModel.getValueAt(selectedRow, 4);
            String tarifStr = (String) tableModel.getValueAt(selectedRow, 5);

            // Konversi tanggal ke format database
            String tglMasukDb = tglMasukDisplay;
            String tglKeluarDb = tglKeluarDisplay;
            try {
                if (tglMasukDisplay != null && !tglMasukDisplay.isEmpty()) {
                    tglMasukDb = LocalDateTime.parse(tglMasukDisplay, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                }
                if (tglKeluarDisplay != null && !tglKeluarDisplay.isEmpty()) {
                    tglKeluarDb = LocalDateTime.parse(tglKeluarDisplay, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                }
            } catch (Exception e) {
                // fallback jika sudah format db
            }

            // Update ke database
            try (Connection conn = DBConnection.getConnection()) {
                String sql = "UPDATE parkir SET plat=?, jenis=?, tanggal_masuk=?, tanggal_keluar=?, durasi_menit=?, tarif=? WHERE plat=? AND tanggal_masuk=?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, plat);
                ps.setString(2, jenis);
                ps.setString(3, tglMasukDb);
                ps.setString(4, (tglKeluarDb == null || tglKeluarDb.isEmpty()) ? null : tglKeluarDb);
                ps.setObject(5, (durasiStr == null || durasiStr.isEmpty()) ? null : Integer.parseInt(durasiStr), java.sql.Types.INTEGER);
                ps.setObject(6, (tarifStr == null || tarifStr.isEmpty()) ? null : Long.parseLong(tarifStr.replaceAll("[^\\d]", "")), java.sql.Types.BIGINT);
                // WHERE plat dan tanggal_masuk lama (sebelum edit)
                String oldPlat = dialog.getOldPlat();
                String oldTglMasukDb = dialog.getOldTglMasukDb();
                ps.setString(7, oldPlat);
                ps.setString(8, oldTglMasukDb);
                ps.executeUpdate();
            } catch (Exception ex) {
                showMessage("Gagal update data ke database: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }

            // Refresh data dari database agar sinkron
            loadDataFromDatabase();
        }
    }
    
    /**
     * Show print dialog
     */
    private void showPrintDialog() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            showMessage("Pilih baris data yang ingin dicetak struknya!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        new PrintDialog(this, tableModel, selectedRow).showStruk();
    }
    
    /**
     * Logout dari aplikasi
     */
    private void logout() {
        int option = JOptionPane.showConfirmDialog(this, 
            "Apakah Anda yakin ingin logout?", 
            "Konfirmasi Logout", 
            JOptionPane.YES_NO_OPTION);
        
        if (option == JOptionPane.YES_OPTION) {
            if (timer != null) {
                timer.stop();
            }
            dispose();
            new LoginForm().setVisible(true);
        }
    }
    
    // Utility methods
    
    /**
     * Style untuk button
     */
    private void styleButton(AbstractButton button, Color bgColor) {
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(180, 35));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
    }
    
    /**
     * Membuat label dengan font
     */
    private JLabel createLabel(String text, Font font) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        return label;
    }
    
    /**
     * Menampilkan pesan dialog
     */
    private void showMessage(String message, String title, int messageType) {
        JOptionPane.showMessageDialog(this, message, title, messageType);
    }
}