package Uas_Pbo;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.imageio.ImageIO;

/**
 * Kelas untuk form login aplikasi parkir
 */
public class LoginForm extends JFrame {
    private JTextField tfUser;
    private JPasswordField pfPass;
    private JButton btnLogin;
    
    public LoginForm() {
        initializeComponents();
        setupLayout();
        setupEventHandlers();
    }
    
    /**
     * Inisialisasi komponen-komponen GUI
     */
    private void initializeComponents() {
        setTitle("Login Aplikasi Parkir");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        tfUser = new JTextField();
        pfPass = new JPasswordField();
        btnLogin = new JButton("LOGIN");
        
        styleTextField(tfUser, "Username");
        styleTextField(pfPass, "Password");
        styleLoginButton();
    }
    
    /**
     * Setup layout dan tata letak komponen
     */
    private void setupLayout() {
        JPanel mainPanel = createMainPanel();
        JPanel loginPanel = createLoginPanel();
        
        mainPanel.add(loginPanel);
        add(mainPanel);
    }
    
    /**
     * Membuat panel utama dengan background
     */
    private JPanel createMainPanel() {
        return new JPanel(new GridBagLayout()) {
            BufferedImage bgImage;
            {
                try {
                    bgImage = ImageIO.read(getClass().getResource("/Uas_Pbo/img/parkir.png"));
                } catch (Exception e) {
                    bgImage = null;
                    System.out.println("Background image tidak ditemukan, menggunakan warna default");
                }
            }
            
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (bgImage != null) {
                    g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
                } else {
                    // Fallback gradient background
                    Graphics2D g2d = (Graphics2D) g;
                    GradientPaint gp = new GradientPaint(0, 0, new Color(25, 118, 210), 
                                                        getWidth(), getHeight(), new Color(33, 150, 243));
                    g2d.setPaint(gp);
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
    }
    
    /**
     * Membuat panel login dengan styling modern
     */
    private JPanel createLoginPanel() {
        JPanel loginPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(Color.WHITE);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 32, 32);
                g2d.setColor(new Color(33, 150, 243, 40));
                g2d.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 32, 32);
            }
        };
        
        loginPanel.setOpaque(false);
        loginPanel.setBorder(BorderFactory.createEmptyBorder(32, 48, 32, 48));
        loginPanel.setPreferredSize(new Dimension(540, 520));
        
        addComponentsToLoginPanel(loginPanel);
        return loginPanel;
    }
    
    /**
     * Menambahkan komponen ke panel login
     */
    private void addComponentsToLoginPanel(JPanel loginPanel) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(16, 10, 16, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Title
        JLabel lblTitle = createTitleLabel();
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        loginPanel.add(lblTitle, gbc);
        
        // Subtitle
        JLabel lblSubtitle = createSubtitleLabel();
        gbc.gridy = 1;
        loginPanel.add(lblSubtitle, gbc);
        
        // Username panel
        JPanel userPanel = createFieldPanel("Username:", tfUser);
        gbc.insets = new Insets(24, 10, 8, 10);
        gbc.gridy = 3;
        loginPanel.add(userPanel, gbc);
        
        // Password panel
        JPanel passPanel = createFieldPanel("Password:", pfPass);
        gbc.gridy = 4;
        loginPanel.add(passPanel, gbc);
        
        // Login button
        gbc.insets = new Insets(32, 10, 10, 10);
        gbc.gridy = 5;
        loginPanel.add(btnLogin, gbc);
        
        // Copyright
        JLabel lblCopyright = createCopyrightLabel();
        gbc.gridy = 6;
        gbc.insets = new Insets(18, 10, 10, 10);
        loginPanel.add(lblCopyright, gbc);
    }
    
    /**
     * Membuat label title
     */
    private JLabel createTitleLabel() {
        JLabel lblTitle = new JLabel("SISTEM PARKIR", JLabel.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblTitle.setForeground(new Color(25, 118, 210));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        return lblTitle;
    }
    
    /**
     * Membuat label subtitle
     */
    private JLabel createSubtitleLabel() {
        JLabel lblSubtitle = new JLabel(
            "<html><div style='text-align:center;'>Selamat Datang!<br>" +
            "Silakan login menggunakan akun yang telah terdaftar.</div></html>", 
            SwingConstants.CENTER
        );
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSubtitle.setForeground(new Color(100, 100, 100));
        lblSubtitle.setHorizontalAlignment(SwingConstants.CENTER);
        return lblSubtitle;
    }
    
    /**
     * Membuat panel untuk field input
     */
    private JPanel createFieldPanel(String labelText, JTextField field) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 15));
        label.setPreferredSize(new Dimension(90, 40));
        
        panel.add(label, BorderLayout.WEST);
        panel.add(field, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Membuat label copyright
     */
    private JLabel createCopyrightLabel() {
        JLabel lblCopyright = new JLabel("© 2025 Sistem Parkir - Kelompok 5 PBO", JLabel.CENTER);
        lblCopyright.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblCopyright.setForeground(new Color(120, 120, 120));
        return lblCopyright;
    }
    
    /**
     * Style untuk text field
     */
    private void styleTextField(JTextField field, String placeholder) {
        field.setPreferredSize(new Dimension(320, 40));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(33, 150, 243), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        field.setBackground(new Color(245, 250, 255));
        field.setToolTipText(placeholder);
    }
    
    /**
     * Style untuk button login
     */
    private void styleLoginButton() {
        btnLogin.setBackground(new Color(25, 118, 210));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setBorderPainted(false);
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnLogin.setPreferredSize(new Dimension(200, 45));
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover effect
        btnLogin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnLogin.setBackground(new Color(33, 150, 243));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnLogin.setBackground(new Color(25, 118, 210));
            }
        });
    }
    
    /**
     * Setup event handlers
     */
    private void setupEventHandlers() {
        btnLogin.addActionListener(e -> performLogin());
        
        // Enter key pada password field
        pfPass.addActionListener(e -> performLogin());
        
        // Enter key pada username field
        tfUser.addActionListener(e -> pfPass.requestFocus());
    }
    
    /**
     * Melakukan proses login
     */
    private void performLogin() {
        String username = tfUser.getText().trim();
        String password = new String(pfPass.getPassword());
        
        if (username.isEmpty() || password.isEmpty()) {
            showMessage("Username dan password tidak boleh kosong!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT username, password, role FROM users WHERE username=? AND password=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String role = rs.getString("role");
                dispose();
                if ("operator".equalsIgnoreCase(role)) {
                    new OptionOP(username, role).setVisible(true);
                } else {
                    new ParkirAppSystem(username, role).setVisible(true);
                }
            } else {
                showMessage("Username atau password salah!", "Login Gagal", JOptionPane.ERROR_MESSAGE);
                clearFields();
            }
        } catch (Exception ex) {
            showMessage("Gagal koneksi ke database: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Menampilkan pesan dialog
     */
    private void showMessage(String message, String title, int messageType) {
        JOptionPane.showMessageDialog(this, message, title, messageType);
    }
    
    /**
     * Membersihkan field input
     */
    private void clearFields() {
        pfPass.setText("");
        tfUser.requestFocus();
    }
}