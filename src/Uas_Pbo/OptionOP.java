package Uas_Pbo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class OptionOP extends JFrame {
    private String username;
    private String role;
    private Timer clockTimer;
    private JLabel clockLabel;

    public OptionOP(String username, String role) {
        this.username = username;
        this.role = role;
        
        setTitle("Parkir Management System - Dashboard");
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

        JPanel card = createMainCard();
        mainPanel.add(card);
        setContentPane(mainPanel);

        startClock();
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
        card.setBorder(BorderFactory.createEmptyBorder(60, 80, 60, 80));
        card.setPreferredSize(new Dimension(580, 600)); 

        // Header section
        JPanel headerPanel = createHeaderPanel();
        card.add(headerPanel);
        card.add(Box.createVerticalStrut(30));

        // Menu cards section
        JPanel menuPanel = createMenuPanel();
        card.add(menuPanel);
        card.add(Box.createVerticalStrut(30));

        // Footer section
        JPanel footerPanel = createFooterPanel();
        card.add(footerPanel);

        return card;
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setOpaque(false);

        // Welcome section
        JLabel welcomeLabel = new JLabel("Selamat Datang!");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        welcomeLabel.setForeground(Color.BLACK);
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Clock
        clockLabel = new JLabel();
        clockLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        clockLabel.setForeground(Color.BLACK);
        clockLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(welcomeLabel);
        headerPanel.add(Box.createVerticalStrut(10));
        headerPanel.add(clockLabel);
        headerPanel.add(Box.createVerticalStrut(10));

        return headerPanel;
    }

    private JPanel createMenuPanel() {
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setOpaque(false);

        // Menu title
        JLabel menuTitle = new JLabel("Pilih Menu Operasi");
        menuTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        menuTitle.setForeground(new Color(55, 65, 81));
        menuTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        menuPanel.add(menuTitle);
        menuPanel.add(Box.createVerticalStrut(30));

        // Create menu cards
        JPanel cardsContainer = new JPanel();
        cardsContainer.setLayout(new BoxLayout(cardsContainer, BoxLayout.Y_AXIS));
        cardsContainer.setOpaque(false);

        // Card Kendaraan Masuk
        JPanel entryCard = createMenuCard(
            "Kendaraan Masuk",
            "Registrasi kendaraan yang masuk area parkir",
            null, null, 
            e -> {
                new Masuk(username, role).setVisible(true);
                dispose();
            }
        );

        // Card Kendaraan Keluar
        JPanel exitCard = createMenuCard(
            "Kendaraan Keluar",
            "Proses kendaraan yang akan keluar parkir",
            null, null,
            e -> {
                new Keluar(username, role).setVisible(true);
                dispose();
            }
        );

        cardsContainer.add(entryCard);
        cardsContainer.add(Box.createVerticalStrut(20));
        cardsContainer.add(exitCard);

        menuPanel.add(cardsContainer);

        return menuPanel;
    }

    private JPanel createMenuCard(String title, String description, Color primaryColor, Color hoverColor, ActionListener action) {
        final Color blue = new Color(33, 150, 243);
        final Color blueDark = new Color(25, 118, 210);
        final boolean[] isHovered = {false};
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                GradientPaint gradient = new GradientPaint(
                    0, 0, isHovered[0] ? blueDark : blue,
                    getWidth(), getHeight(), isHovered[0] ? blueDark.darker() : blue.darker()
                );
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);

                g2d.setColor(new Color(255,255,255,40));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight()/2, 18, 18);
            }
        };
        card.setLayout(new BorderLayout());
        card.setOpaque(false);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        card.setPreferredSize(new Dimension(450, 120));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        // Content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel descLabel = new JLabel(description);
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        descLabel.setForeground(new Color(255, 255, 255, 200));
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        contentPanel.add(titleLabel);
        contentPanel.add(Box.createVerticalStrut(8));
        contentPanel.add(descLabel);

        JLabel arrowLabel = new JLabel("→");
        arrowLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        arrowLabel.setForeground(Color.WHITE);

        card.add(contentPanel, BorderLayout.CENTER);
        card.add(arrowLabel, BorderLayout.EAST);

        // Mouse listeners for hover effect and click
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered[0] = true;
                card.repaint();
            }
            @Override
            public void mouseExited(MouseEvent e) {
                isHovered[0] = false;
                card.repaint();
            }
            @Override
            public void mouseClicked(MouseEvent e) {
                action.actionPerformed(new ActionEvent(card, ActionEvent.ACTION_PERFORMED, "click"));
            }
        });

        return card;
    }
    
    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel();
        footerPanel.setLayout(new BoxLayout(footerPanel, BoxLayout.Y_AXIS));
        footerPanel.setOpaque(false);
        
        JButton logoutButton = createStyledButton(
            "Logout",
            new Color(239, 68, 68),
            new Color(220, 38, 38)
        );
        
        logoutButton.addActionListener(e -> {
            int result = showStyledConfirmDialog(
                "Apakah Anda yakin ingin keluar dari sistem?",
                "Konfirmasi Logout"
            );
            
            if (result == JOptionPane.YES_OPTION) {
                if (clockTimer != null) {
                    clockTimer.stop();
                }
                dispose();
                new LoginForm().setVisible(true);
            }
        });
    
        JLabel infoLabel = new JLabel("© 2025 Sistem Parkir - Kelompok 5 PBO");
        infoLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        infoLabel.setForeground(new Color(107, 114, 128));
        infoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        footerPanel.add(logoutButton);
        footerPanel.add(Box.createVerticalStrut(20));
        footerPanel.add(infoLabel);
        
        return footerPanel;
    }
    
    private JButton createStyledButton(String text, Color bgColor, Color hoverColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setMaximumSize(new Dimension(200, 45));
        button.setPreferredSize(new Dimension(200, 45));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverColor);
                button.setOpaque(true);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
                button.setOpaque(true);
            }
        });
        button.setBackground(bgColor);
        button.setOpaque(true);

        return button;
    }
    
    private void startClock() {
        clockTimer = new Timer(1000, e -> {
            String currentTime = LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
            );
            clockLabel.setText("" + currentTime);
        });
        clockTimer.start();
        
        // Set initial time
        String currentTime = LocalDateTime.now().format(
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
        );
        clockLabel.setText("" + currentTime);
    }
    
    private int showStyledConfirmDialog(String message, String title) {
        UIManager.put("OptionPane.messageFont", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("OptionPane.buttonFont", new Font("Segoe UI", Font.BOLD, 12));
        
        return JOptionPane.showConfirmDialog(
            this,
            message,
            title,
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
    }
    
    @Override
    public void dispose() {
        if (clockTimer != null) {
            clockTimer.stop();
        }
        super.dispose();
    }
}