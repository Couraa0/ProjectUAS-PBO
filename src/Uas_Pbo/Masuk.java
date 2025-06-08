package Uas_Pbo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class Masuk extends JFrame {
    private JTextField tfPlat;
    private JComboBox<String> cbJenis;
    private JLabel lblJamMasuk;
    private String username, role;
    private Timer timeUpdater;

    public Masuk(String username, String role) {
        this.username = username;
        this.role = role;
        
        setTitle("Parkir Management - Kendaraan Masuk");
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
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setColor(new Color(25, 118, 210));
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        
        // Create main card
        JPanel card = createMainCard();
        mainPanel.add(card);
        setContentPane(mainPanel);
        
        // Start time updater
        startTimeUpdater();
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
        card.setPreferredSize(new Dimension(500, 665));

        // Header section
        JPanel headerPanel = createHeaderPanel();
        headerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(headerPanel);
        card.add(Box.createVerticalStrut(40));

        // Form section (centered)
        JPanel formPanel = createFormPanel();
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(formPanel);
        card.add(Box.createVerticalStrut(40));

        // Button section (centered)
        JPanel buttonPanel = createButtonPanel();
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(buttonPanel);

        return card;
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setOpaque(false);

        // Icon
        JLabel iconLabel = new JLabel("🚗");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Title
        JLabel titleLabel = new JLabel("Kendaraan Masuk");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(new Color(25, 118, 210));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Subtitle
        JLabel subtitleLabel = new JLabel("Registrasi kendaraan baru");
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
    
    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false);
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.setMaximumSize(new Dimension(400, 350)); 

        // Plat field
        JPanel platPanel = createInputField("Plat Nomor");
        platPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        tfPlat = createStyledTextField();
        tfPlat.setAlignmentX(Component.CENTER_ALIGNMENT);
        platPanel.add(Box.createVerticalStrut(8));
        platPanel.add(tfPlat);

        // Jenis field
        JPanel jenisPanel = createInputField("Jenis Kendaraan");
        jenisPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        cbJenis = createStyledComboBox();
        cbJenis.setAlignmentX(Component.CENTER_ALIGNMENT);
        jenisPanel.add(Box.createVerticalStrut(8));
        jenisPanel.add(cbJenis);

        // Time field
        JPanel timePanel = createInputField("Waktu Masuk");
        timePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblJamMasuk = createTimeLabel();
        lblJamMasuk.setAlignmentX(Component.CENTER_ALIGNMENT);
        timePanel.add(Box.createVerticalStrut(8));
        timePanel.add(lblJamMasuk);

        formPanel.add(platPanel);
        formPanel.add(Box.createVerticalStrut(20));
        formPanel.add(jenisPanel);
        formPanel.add(Box.createVerticalStrut(20));
        formPanel.add(timePanel);

        return formPanel;
    }

    // Perbaiki signature: hanya butuh labelText
    private JPanel createInputField(String labelText) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 16));
        label.setForeground(new Color(25, 118, 210)); // biru
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(label);
        return panel;
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
        textField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        textField.setPreferredSize(new Dimension(350, 45));
        textField.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));
        textField.setOpaque(false);
        textField.setBackground(new Color(245, 250, 255));
        
        return textField;
    }
    
    private JComboBox<String> createStyledComboBox() {
        String[] options = {"Motor", "Mobil"};
        JComboBox<String> comboBox = new JComboBox<>(options);
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        comboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        comboBox.setPreferredSize(new Dimension(350, 45));
        comboBox.setBackground(new Color(245, 250, 255));
        comboBox.setForeground(new Color(25, 118, 210));
        comboBox.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(33, 150, 243), 1, true),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        
        return comboBox;
    }
    
    private JLabel createTimeLabel() {
        JLabel timeLabel = new JLabel();
        timeLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        timeLabel.setForeground(new Color(25, 118, 210));
        timeLabel.setOpaque(true);
        timeLabel.setBackground(new Color(232, 245, 253));
        timeLabel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(33, 150, 243), 1, true),
            BorderFactory.createEmptyBorder(12, 16, 12, 16)
        ));
        timeLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        timeLabel.setPreferredSize(new Dimension(350, 45));
        
        return timeLabel;
    }
    
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false);
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonPanel.setMaximumSize(new Dimension(400, 120));

        // Save button
        JButton btnSimpan = createStyledButton("Simpan Data", new Color(16, 185, 129), new Color(5, 150, 105));
        btnSimpan.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnSimpan.addActionListener(e -> simpanMasuk());

        // Back button
        JButton btnBack = createStyledButton("Kembali", new Color(107, 114, 128), new Color(75, 85, 99));
        btnBack.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnBack.addActionListener(e -> {
            timeUpdater.stop();
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
    
    private void startTimeUpdater() {
        timeUpdater = new Timer(1000, e -> {
            // Tampilkan waktu masuk dengan format dd/MM/yyyy HH:mm:ss
            String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
            lblJamMasuk.setText(currentTime);
        });
        timeUpdater.start();

        // Set initial time
        String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        lblJamMasuk.setText(currentTime);
    }
    
    private void simpanMasuk() {
        String plat = tfPlat.getText().trim().toUpperCase();
        String jenisSelection = (String) cbJenis.getSelectedItem();
        String jenis = jenisSelection.contains("Motor") ? "Motor" : "Mobil";
        // Format waktu masuk: dd/MM/yyyy HH:mm:ss
        String jamMasukDisplay = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        String jamMasukDb = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        if (plat.isEmpty()) {
            showStyledMessage("Plat nomor tidak boleh kosong!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            tfPlat.requestFocus();
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO parkir (plat, jenis, tanggal_masuk) VALUES (?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, plat);
            ps.setString(2, jenis);
            ps.setString(3, jamMasukDb);
            ps.executeUpdate();

            showStyledMessage("Kendaraan berhasil didaftarkan!\nWaktu Masuk: " + jamMasukDisplay, "Sukses", JOptionPane.INFORMATION_MESSAGE);
            tfPlat.setText("");
            tfPlat.requestFocus();
        } catch (Exception ex) {
            showStyledMessage("Gagal menyimpan data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void showStyledMessage(String message, String title, int messageType) {
        UIManager.put("OptionPane.messageFont", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("OptionPane.buttonFont", new Font("Segoe UI", Font.BOLD, 12));
        JOptionPane.showMessageDialog(this, message, title, messageType);
    }
    
    @Override
    public void dispose() {
        if (timeUpdater != null) {
            timeUpdater.stop();
        }
        super.dispose();
    }
}