package Uas_Pbo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * Dialog untuk mengedit data parkir
 */
public class EditDialog extends JDialog {
    private DefaultTableModel tableModel;
    private int selectedRow;
    private JTextField[] textFields;
    private JButton btnSave, btnCancel;
    
    private boolean saved = false;
    private String oldPlat;
    private String oldTglMasukDb;

    public EditDialog(JFrame parent, DefaultTableModel model, int row) {
        super(parent, "Edit Data Parkir", true);
        this.tableModel = model;
        this.selectedRow = row;
        
        // Simpan data lama sebelum diedit
        oldPlat = (String) model.getValueAt(row, 0);
        String tglMasukDisplay = (String) model.getValueAt(row, 2);
        oldTglMasukDb = tglMasukDisplay;
        try {
            if (tglMasukDisplay != null && !tglMasukDisplay.isEmpty()) {
                oldTglMasukDb = java.time.LocalDateTime.parse(
                    tglMasukDisplay, java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
                ).format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            }
        } catch (Exception e) {
            // fallback jika sudah format db
        }
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        
        setSize(440, 480);
        setLocationRelativeTo(parent);
    }
    
    /**
     * Inisialisasi komponen dialog
     */
    private void initializeComponents() {
        textFields = new JTextField[6];
        
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 16);
        
        for (int i = 0; i < textFields.length; i++) {
            textFields[i] = new JTextField();
            textFields[i].setFont(fieldFont);
            textFields[i].setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(33, 150, 243), 1),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
            ));
            
            // Set nilai dari table
            Object value = tableModel.getValueAt(selectedRow, i);
            textFields[i].setText(value == null ? "" : value.toString());
        }
        
        btnSave = new JButton("Simpan");
        btnCancel = new JButton("Batal");
        
        styleButton(btnSave, new Color(76, 175, 80));
        styleButton(btnCancel, new Color(244, 67, 54));
    }
    
    /**
     * Setup layout dialog
     */
    private void setupLayout() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(232, 245, 253));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));
        
        JPanel cardPanel = createCardPanel();
        JPanel buttonPanel = createButtonPanel();
        
        mainPanel.add(cardPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        setContentPane(mainPanel);
    }
    
    /**
     * Membuat panel card dengan form
     */
    private JPanel createCardPanel() {
        JPanel cardPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(Color.WHITE);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
                g2d.setColor(new Color(33, 150, 243, 40));
                g2d.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 18, 18);
            }
        };
        
        cardPanel.setOpaque(false);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(33, 150, 243), 2),
            BorderFactory.createEmptyBorder(18, 28, 18, 28)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        
        // Title
        JLabel lblTitle = new JLabel("Edit Data Parkir", JLabel.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(new Color(33, 150, 243));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        cardPanel.add(lblTitle, gbc);
        
        gbc.gridwidth = 1;
        
        Font labelFont = new Font("Segoe UI", Font.BOLD, 15);
        String[] labels = {"Plat:", "Jenis:", "Tanggal Masuk:", "Tanggal Keluar:", "Durasi (menit):", "Tarif:"};
        
        for (int i = 0; i < labels.length; i++) {
            gbc.gridy = i + 1;
            gbc.gridx = 0;
            
            JLabel label = new JLabel(labels[i], JLabel.LEFT);
            label.setFont(labelFont);
            cardPanel.add(label, gbc);
            
            gbc.gridx = 1;
            cardPanel.add(textFields[i], gbc);
        }
        
        return cardPanel;
    }
    
    /**
     * Membuat panel button
     */
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 18, 8));
        buttonPanel.setOpaque(false);
        
        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);
        
        return buttonPanel;
    }
    
    /**
     * Setup event handlers
     */
    private void setupEventHandlers() {
        btnSave.addActionListener(e -> saveData());
        btnCancel.addActionListener(e -> dispose());
        
        // ESC key untuk cancel
        getRootPane().registerKeyboardAction(e -> dispose(),
            KeyStroke.getKeyStroke("ESCAPE"), JComponent.WHEN_IN_FOCUSED_WINDOW);
    }
    
    /**
     * Simpan data yang diedit
     */
    private void saveData() {
        String[] values = new String[textFields.length];
        for (int i = 0; i < textFields.length; i++) {
            values[i] = textFields[i].getText().trim();
        }
        
        // Validasi data
        if (values[0].isEmpty()) {
            showMessage("Plat nomor tidak boleh kosong!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (values[1].isEmpty()) {
            showMessage("Jenis kendaraan tidak boleh kosong!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (values[2].isEmpty()) {
            showMessage("Tanggal masuk tidak boleh kosong!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Simpan ke table model
        for (int i = 0; i < values.length; i++) {
            tableModel.setValueAt(values[i], selectedRow, i);
        }
        
        // Simpan ke database
        updateDatabase(values);
        
        // Pada tombol simpan/OK, set saved = true
        saved = true;
    }
    
    /**
     * Update data ke database
     */
    private void updateDatabase(String[] values) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "UPDATE parkir SET plat=?, jenis=?, tanggal_masuk=?, tanggal_keluar=?, durasi_menit=?, tarif=? " +
                        "WHERE plat=? AND tanggal_masuk=?";
            
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, values[0]); // plat baru
            ps.setString(2, values[1]); // jenis
            ps.setString(3, values[2]); // tanggal masuk baru
            ps.setString(4, values[3].isEmpty() ? null : values[3]); // tanggal keluar
            ps.setString(5, values[4].isEmpty() ? null : values[4]); // durasi
            ps.setString(6, values[5].isEmpty() ? null : values[5]); // tarif
            
            // WHERE conditions (nilai asli)
            ps.setString(7, (String) tableModel.getValueAt(selectedRow, 0)); // plat asli
            ps.setString(8, (String) tableModel.getValueAt(selectedRow, 2)); // tanggal masuk asli
            
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                showMessage("Data berhasil diupdate.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                saved = true;
                dispose();
            }
        } catch (Exception ex) {
            showMessage("Gagal update data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Style untuk button
     */
    private void styleButton(JButton button, Color bgColor) {
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setPreferredSize(new Dimension(120, 35));
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
     * Menampilkan pesan dialog
     */
    private void showMessage(String message, String title, int messageType) {
        JOptionPane.showMessageDialog(this, message, title, messageType);
    }

    public boolean isSaved() {
        return saved;
    }

    public String getOldPlat() {
        return oldPlat;
    }

    public String getOldTglMasukDb() {
        return oldTglMasukDb;
    }
}