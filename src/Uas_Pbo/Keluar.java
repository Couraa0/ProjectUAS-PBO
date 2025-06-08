package Uas_Pbo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class Keluar extends JFrame {
    private JTextField tfPlat;
    private JLabel lblJamKeluar, lblDurasi, lblTarif;
    private String username, role;
    private JPanel infoPanel;
    private boolean dataFound = false;
    private JTable tableParkir;
    private DefaultTableModel tableModel;

    public Keluar(String username, String role) {
        this.username = username;
        this.role = role;
        
        setTitle("Parkir Management - Kendaraan Keluar");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new GridBagLayout()) {
            Image bgImage;
            {
                try {
                    bgImage = javax.imageio.ImageIO.read(getClass().getResource("/Uas_Pbo/img/parkir2.png"));
                } catch (Exception e) {
                    bgImage = null;
                }
            }
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (bgImage != null) {
                    g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
                } else {
                    // fallback biru
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setColor(new Color(25, 118, 210));
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        mainPanel.setLayout(new GridBagLayout());

        // Panel utama dengan layout horizontal (card kiri, tabel kanan)
        JPanel horizontalPanel = new JPanel();
        horizontalPanel.setOpaque(false);
        horizontalPanel.setLayout(new BoxLayout(horizontalPanel, BoxLayout.X_AXIS));
        horizontalPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        // Card kendaraan keluar (kiri)
        JPanel card = createMainCard();
        horizontalPanel.add(card);
        horizontalPanel.add(Box.createHorizontalStrut(40));

        // Panel tabel kendaraan belum keluar (kanan)
        JScrollPane tableScroll = createTablePanel();

        // Card untuk tabel daftar kendaraan belum keluar
        JPanel tableCard = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Card background: putih solid dengan border biru
                g2d.setColor(Color.WHITE);
                g2d.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 24, 24);
                g2d.setColor(new Color(33, 150, 243, 180));
                g2d.setStroke(new BasicStroke(3));
                g2d.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 24, 24);
            }
        };
        tableCard.setLayout(new BoxLayout(tableCard, BoxLayout.Y_AXIS));
        tableCard.setOpaque(false);
        tableCard.setBorder(BorderFactory.createEmptyBorder(50, 40, 50, 40));
        tableCard.setPreferredSize(new Dimension(520, 700));

        JLabel tabelTitle = new JLabel("Daftar Kendaraan Belum Keluar");
        tabelTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        tabelTitle.setForeground(new Color(25, 118, 210));
        tabelTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        tableCard.add(tabelTitle);
        tableCard.add(Box.createVerticalStrut(20));
        tableCard.add(tableScroll);

        // Tambahkan event klik pada tabel: klik baris, plat nomor masuk ke tfPlat
        tableParkir.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tableParkir.getSelectedRow();
                if (row != -1) {
                    String plat = tableModel.getValueAt(row, 0).toString();
                    tfPlat.setText(plat);
                    tfPlat.requestFocus();
                }
            }
        });

        horizontalPanel.add(tableCard);

        mainPanel.add(horizontalPanel);
        setContentPane(mainPanel);

        // Muat data parkir yang belum keluar ke tabel saat frame dibuat
        loadTableData();
    }

    private JPanel createMainCard() {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(Color.WHITE);
                g2d.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 24, 24);
                g2d.setColor(new Color(33, 150, 243, 180));
                g2d.setStroke(new BasicStroke(3));
                g2d.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 24, 24);
            }
        };

        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setOpaque(false);
        card.setBorder(BorderFactory.createEmptyBorder(50, 60, 50, 60));
        card.setPreferredSize(new Dimension(550, 700));
        
        // Header section
        JPanel headerPanel = createHeaderPanel();
        card.add(headerPanel);
        card.add(Box.createVerticalStrut(40));
        
        // Search section
        JPanel searchPanel = createSearchPanel();
        card.add(searchPanel);
        card.add(Box.createVerticalStrut(30));
        
        // Info section
        infoPanel = createInfoPanel();
        card.add(infoPanel);
        card.add(Box.createVerticalStrut(20));

        // Button section
        JPanel buttonPanel = createButtonPanel();
        card.add(buttonPanel);

        return card;
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setOpaque(false);

        // Icon
        JLabel iconLabel = new JLabel("🚗💨");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Title
        JLabel titleLabel = new JLabel("Kendaraan Keluar");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(new Color(25, 118, 210));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Subtitle
        JLabel subtitleLabel = new JLabel("Proses kendaraan yang akan keluar");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitleLabel.setForeground(new Color(33, 150, 243));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(iconLabel);
        headerPanel.add(Box.createVerticalStrut(10));
        headerPanel.add(titleLabel);
        headerPanel.add(Box.createVerticalStrut(5));
        headerPanel.add(subtitleLabel);

        return headerPanel;
    }

    private JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.Y_AXIS));
        searchPanel.setOpaque(false);
        
        // Search label
        JLabel searchLabel = new JLabel("Masukkan Plat Nomor");
        searchLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        searchLabel.setForeground(new Color(55, 65, 81));
        searchLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Search field with button
        JPanel searchFieldPanel = new JPanel(new BorderLayout(10, 0));
        searchFieldPanel.setOpaque(false);
        searchFieldPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        
        tfPlat = createStyledTextField();
        JButton btnCari = createSearchButton();
        
        searchFieldPanel.add(tfPlat, BorderLayout.CENTER);
        searchFieldPanel.add(btnCari, BorderLayout.EAST);
        
        searchPanel.add(searchLabel);
        searchPanel.add(Box.createVerticalStrut(8));
        searchPanel.add(searchFieldPanel);
        

        tfPlat.addActionListener(e -> cariKendaraan());
        
        return searchPanel;
    }
    
    private JTextField createStyledTextField() {
        JTextField textField = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Background
                g2d.setColor(new Color(245, 250, 255));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);

                // Border
                if (hasFocus()) {
                    g2d.setColor(new Color(33, 150, 243));
                    g2d.setStroke(new BasicStroke(2));
                } else {
                    g2d.setColor(new Color(25, 118, 210));
                    g2d.setStroke(new BasicStroke(1));
                }
                g2d.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 12, 12);

                super.paintComponent(g);
            }
        };

        textField.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        textField.setPreferredSize(new Dimension(280, 45));
        textField.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));
        textField.setOpaque(false);
        textField.setBackground(new Color(245, 250, 255));
        
        return textField;
    }
    
    private JButton createSearchButton() {
        JButton button = new JButton("Cari") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Color bgColor = getModel().isPressed() ? new Color(33, 150, 243) : new Color(25, 118, 210);
                g2d.setColor(bgColor);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);

                super.paintComponent(g);
            }
        };
        
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setPreferredSize(new Dimension(80, 45));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addActionListener(e -> cariKendaraan());
        
        return button;
    }
    
    private JPanel createInfoPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(33, 150, 243), 2, true),
            "Informasi Parkir",
            0, 0,
            new Font("Segoe UI", Font.BOLD, 16),
            new Color(25, 118, 210)
        ));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 250));

        // Initialize labels
        lblJamKeluar = new JLabel("-");
        lblJamKeluar.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblJamKeluar.setForeground(new Color(25, 118, 210));
        lblJamKeluar.setAlignmentX(Component.LEFT_ALIGNMENT);

        lblDurasi = new JLabel("-");
        lblDurasi.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblDurasi.setForeground(new Color(25, 118, 210));
        lblDurasi.setAlignmentX(Component.LEFT_ALIGNMENT);

        lblTarif = new JLabel("-");
        lblTarif.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTarif.setForeground(new Color(25, 118, 210));
        lblTarif.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Label judul
        JLabel lblJamKeluarTitle = new JLabel("Waktu Keluar");
        lblJamKeluarTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblJamKeluarTitle.setForeground(new Color(33, 150, 243));
        JLabel lblDurasiTitle = new JLabel("Durasi Parkir");
        lblDurasiTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblDurasiTitle.setForeground(new Color(33, 150, 243));
        JLabel lblTarifTitle = new JLabel("Total Tarif");
        lblTarifTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTarifTitle.setForeground(new Color(33, 150, 243));

        // Panel baris info
        JPanel rowJamKeluar = new JPanel(new BorderLayout());
        rowJamKeluar.setOpaque(false);
        rowJamKeluar.add(lblJamKeluarTitle, BorderLayout.WEST);
        rowJamKeluar.add(lblJamKeluar, BorderLayout.EAST);

        JPanel rowDurasi = new JPanel(new BorderLayout());
        rowDurasi.setOpaque(false);
        rowDurasi.add(lblDurasiTitle, BorderLayout.WEST);
        rowDurasi.add(lblDurasi, BorderLayout.EAST);

        JPanel rowTarif = new JPanel(new BorderLayout());
        rowTarif.setOpaque(false);
        rowTarif.add(lblTarifTitle, BorderLayout.WEST);
        rowTarif.add(lblTarif, BorderLayout.EAST);

        panel.add(Box.createVerticalStrut(15));
        panel.add(rowJamKeluar);
        panel.add(Box.createVerticalStrut(15));
        panel.add(rowDurasi);
        panel.add(Box.createVerticalStrut(15));
        panel.add(rowTarif);
        panel.add(Box.createVerticalStrut(15));

        return panel;
    }
    
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false);

        // Process button
        JButton btnSimpan = createStyledButton("Proses Keluar", new Color(220, 38, 38), new Color(185, 28, 28));
        btnSimpan.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        btnSimpan.setPreferredSize(new Dimension(350, 50));
        btnSimpan.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnSimpan.addActionListener(e -> simpanKeluar());

        // Back button
        JButton btnBack = createStyledButton("Kembali", new Color(107, 114, 128), new Color(75, 85, 99));
        btnBack.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        btnBack.setPreferredSize(new Dimension(350, 50));
        btnBack.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnBack.addActionListener(e -> {
            new OptionOP(username, role).setVisible(true);
            dispose();
        });

        buttonPanel.add(btnSimpan);
        buttonPanel.add(Box.createVerticalStrut(15));
        buttonPanel.add(btnBack);

        return buttonPanel;
    }
    
    private JButton createStyledButton(String text, Color bgColor, Color hoverColor) {
        JButton button = new JButton(text) {
            private boolean isHovered = false;

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Button background
                if (isHovered) {
                    g2d.setColor(hoverColor);
                } else {
                    g2d.setColor(bgColor);
                }
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);

                // Shadow effect
                if (isHovered) {
                    g2d.setColor(new Color(0, 0, 0, 30));
                    g2d.fillRoundRect(2, 2, getWidth()-2, getHeight()-2, 12, 12);
                }

                super.paintComponent(g);
            }

            @Override
            protected void paintBorder(Graphics g) {
                // No border painting
            }
        };
        
        button.setFont(new Font("Segoe UI", Font.BOLD, 18));
        button.setForeground(Color.WHITE);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        button.setPreferredSize(new Dimension(350, 50));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover effects
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                ((JButton)e.getSource()).putClientProperty("isHovered", true);
                button.repaint();
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                ((JButton)e.getSource()).putClientProperty("isHovered", false);
                button.repaint();
            }
        });
        
        return button;
    }
    
    private JScrollPane createTablePanel() {
        // Kolom: Plat, Jenis, Tanggal Masuk
        tableModel = new DefaultTableModel(
            new String[]{"Plat Nomor", "Jenis", "Tanggal Masuk"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableParkir = new JTable(tableModel);
        tableParkir.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tableParkir.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tableParkir.setRowHeight(28);

        JScrollPane scrollPane = new JScrollPane(tableParkir);
        scrollPane.setPreferredSize(new Dimension(450, 600));
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(33, 150, 243), 2, true));
        return scrollPane;
    }

    private void loadTableData() {
        tableModel.setRowCount(0);
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT plat, jenis, tanggal_masuk FROM parkir WHERE tanggal_keluar IS NULL OR tanggal_keluar=''";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getString("plat"),
                    rs.getString("jenis"),
                    rs.getString("tanggal_masuk")
                });
            }
        } catch (Exception ex) {
            // Optional: tampilkan pesan error jika gagal load data
        }
    }
    
    private void cariKendaraan() {
        String plat = tfPlat.getText().trim().toUpperCase();
        if (plat.isEmpty()) {
            showStyledMessage("Masukkan plat nomor terlebih dahulu!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            tfPlat.requestFocus();
            return;
        }
        
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM parkir WHERE plat=? AND (tanggal_keluar IS NULL OR tanggal_keluar='')";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, plat);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String jamMasuk = rs.getString("tanggal_masuk");
                String jenis = rs.getString("jenis");
                String jamKeluar = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                
                long durasiMenit = calculateDuration(jamMasuk, jamKeluar);
                
                Kendaraan kendaraan = jenis.equalsIgnoreCase("Motor")
                    ? new Motor(plat, jamMasuk)
                    : new Mobil(plat, jamMasuk);

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime masuk = LocalDateTime.parse(jamMasuk, formatter);
                LocalDateTime keluar = LocalDateTime.parse(jamKeluar, formatter);
                long days = java.time.Duration.between(masuk.toLocalDate().atStartOfDay(),
                                                       keluar.toLocalDate().atStartOfDay()).toDays();

                long tarif = kendaraan.hitungTarif(durasiMenit, days);
                
                // Update UI
                lblJamKeluar.setText(jamKeluar);
                lblDurasi.setText(formatDuration(durasiMenit));
                lblTarif.setText("Rp " + String.format("%,d", tarif));
                
                dataFound = true;
                
                // Animate info panel
                animateInfoPanel();
                
            } else {
                showStyledMessage("Kendaraan tidak ditemukan atau sudah keluar!", "Data Tidak Ditemukan", JOptionPane.ERROR_MESSAGE);
                resetInfoPanel();
                dataFound = false;
            }
        } catch (Exception ex) {
            showStyledMessage("Error saat mencari data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            resetInfoPanel();
            dataFound = false;
        }
    }
    
    private void animateInfoPanel() {
        // Simple animation effect
        Timer timer = new Timer(50, null);
        final int[] alpha = {0};
        
        timer.addActionListener(e -> {
            alpha[0] += 15;
            if (alpha[0] >= 255) {
                alpha[0] = 255;
                timer.stop();
            }
            infoPanel.repaint();
        });
        timer.start();
    }
    
    private void resetInfoPanel() {
        lblJamKeluar.setText("-");
        lblDurasi.setText("-");
        lblTarif.setText("-");
    }
    
    private String formatDuration(long minutes) {
        if (minutes < 60) {
            return minutes + " menit";
        } else {
            long hours = minutes / 60;
            long remainingMinutes = minutes % 60;
            return hours + " jam " + remainingMinutes + " menit";
        }
    }

    private void simpanKeluar() {
        if (!dataFound) {
            showStyledMessage("Silakan cari kendaraan terlebih dahulu!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            tfPlat.requestFocus();
            return;
        }

        String plat = tfPlat.getText().trim().toUpperCase();
        // Format waktu keluar: dd/MM/yyyy HH:mm:ss
        String jamKeluar = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM parkir WHERE plat=? AND (tanggal_keluar IS NULL OR tanggal_keluar='')";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, plat);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                // Ambil waktu masuk dari database dan konversi ke format dd/MM/yyyy HH:mm:ss
                String jamMasukDb = rs.getString("tanggal_masuk");
                LocalDateTime jamMasukLdt = LocalDateTime.parse(jamMasukDb, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                String jamMasuk = jamMasukLdt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));

                String jenis = rs.getString("jenis");
                long durasiMenit = calculateDuration(jamMasukDb, LocalDateTime.parse(jamKeluar, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

                Kendaraan kendaraan = jenis.equalsIgnoreCase("Motor")
                    ? new Motor(plat, jamMasukDb)
                    : new Mobil(plat, jamMasukDb);

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime masuk = LocalDateTime.parse(jamMasukDb, formatter);
                LocalDateTime keluar = LocalDateTime.parse(LocalDateTime.parse(jamKeluar, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), formatter);
                long days = java.time.Duration.between(masuk.toLocalDate().atStartOfDay(),
                                                       keluar.toLocalDate().atStartOfDay()).toDays();

                long tarif = kendaraan.hitungTarif(durasiMenit, days);

                // Update database (tetap simpan dalam format yyyy-MM-dd HH:mm:ss)
                String jamKeluarDb = LocalDateTime.parse(jamKeluar, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                String updateSql = "UPDATE parkir SET tanggal_keluar=?, durasi_menit=?, tarif=? WHERE plat=? AND tanggal_masuk=?";
                PreparedStatement psUpdate = conn.prepareStatement(updateSql);
                psUpdate.setString(1, jamKeluarDb);
                psUpdate.setLong(2, durasiMenit);
                psUpdate.setLong(3, tarif);
                psUpdate.setString(4, plat);
                psUpdate.setString(5, jamMasukDb);
                psUpdate.executeUpdate();

                // Ambil data untuk struk, gunakan PrintDialog
                DefaultTableModel dummyModel = new DefaultTableModel(
                    new String[]{"Plat Nomor", "Jenis", "Tanggal Masuk", "Tanggal Keluar", "Durasi Parkir", "Tarif"}, 0
                );
                dummyModel.addRow(new Object[]{
                    plat,
                    jenis,
                    jamMasuk,
                    jamKeluar,
                    String.valueOf(durasiMenit),
                    String.valueOf(tarif)
                });

                PrintDialog printDialog = new PrintDialog(this, dummyModel, 0);
                printDialog.showStruk();

                // Reset form
                tfPlat.setText("");
                resetInfoPanel();
                dataFound = false;
                tfPlat.requestFocus();

                // Refresh tabel setelah kendaraan keluar
                loadTableData();

            } else {
                showStyledMessage("Data kendaraan tidak ditemukan!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            showStyledMessage("Gagal memproses: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Tambahkan jika belum ada
    private void showStyledMessage(String message, String title, int messageType) {
        UIManager.put("OptionPane.messageFont", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("OptionPane.buttonFont", new Font("Segoe UI", Font.BOLD, 12));
        JOptionPane.showMessageDialog(this, message, title, messageType);
    }

    // Ubah calculateDuration agar menerima string format yyyy-MM-dd HH:mm:ss
    private long calculateDuration(String masuk, String keluar) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime start = LocalDateTime.parse(masuk, formatter);
        LocalDateTime end = LocalDateTime.parse(keluar, formatter);
        return java.time.Duration.between(start, end).toMinutes();
    }
}