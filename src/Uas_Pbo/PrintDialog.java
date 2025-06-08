package Uas_Pbo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.filechooser.FileNameExtensionFilter;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPCell;

public class PrintDialog {
    private JFrame parent;
    private DefaultTableModel tableModel;
    private int selectedRow;

    public PrintDialog(JFrame parent, DefaultTableModel tableModel, int selectedRow) {
        this.parent = parent;
        this.tableModel = tableModel;
        this.selectedRow = selectedRow;
    }

    public void showStruk() {
        if (!isValidSelection()) return;

        StrukData data = extractDataFromTable();
        JPanel strukPanel = createStrukPanelRapi(data);

        // Buat tombol download PDF dengan warna lebih menarik
        JButton btnDownload = new JButton("Download PDF");
        btnDownload.setBackground(new Color(33, 150, 243));
        btnDownload.setForeground(Color.BLACK);
        btnDownload.setFocusPainted(false);
        btnDownload.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 15));
        btnDownload.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnDownload.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(33, 150, 243), 2, true),
            BorderFactory.createEmptyBorder(8, 18, 8, 18)
        ));
        // Hover effect
        btnDownload.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnDownload.setBackground(new Color(21, 101, 192));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnDownload.setBackground(new Color(33, 150, 243));
            }
        });
        btnDownload.addActionListener(e -> downloadStrukAsPDF(data));

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(strukPanel, BorderLayout.CENTER);
        JPanel btnPanel = new JPanel();
        btnPanel.setOpaque(false);
        btnPanel.add(btnDownload);
        wrapper.add(btnPanel, BorderLayout.SOUTH);

        // Size Struk Panel
        wrapper.setPreferredSize(new Dimension(500, 480));
        strukPanel.setPreferredSize(new Dimension(480, 400));

        JOptionPane.showMessageDialog(
            parent,
            wrapper,
            "Cetak Struk Parkir",
            JOptionPane.PLAIN_MESSAGE
        );
    }

    private JPanel createStrukPanelRapi(StrukData data) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(new Dimension(480, 400));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(33, 150, 243), 2, true),
            BorderFactory.createEmptyBorder(18, 28, 18, 28)
        ));

        JLabel title = new JLabel("STRUK PARKIR", SwingConstants.CENTER);
        title.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 22));
        title.setForeground(new Color(33, 150, 243));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel sub = new JLabel("SISTEM PARKIR OTOMATIS", SwingConstants.CENTER);
        sub.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 12));
        sub.setForeground(new Color(120, 120, 120));
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel waktuCetak = new JLabel("Dicetak: " +
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")), SwingConstants.CENTER);
        waktuCetak.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 11));
        waktuCetak.setForeground(new Color(160, 160, 160));
        waktuCetak.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(title);
        panel.add(Box.createVerticalStrut(2));
        panel.add(sub);
        panel.add(Box.createVerticalStrut(2));
        panel.add(waktuCetak);
        panel.add(Box.createVerticalStrut(10));
        panel.add(new JSeparator());

        panel.add(Box.createVerticalStrut(10));
        panel.add(createRow("Plat Nomor", data.plat));
        panel.add(createRow("Jenis Kendaraan", data.jenis));
        panel.add(createRow("Waktu Masuk", data.jamMasuk));
        panel.add(createRow("Waktu Keluar", data.jamKeluar));
        panel.add(createRow("Durasi Parkir", formatDurasi(data.durasi)));
        panel.add(Box.createVerticalStrut(10));
        panel.add(new JSeparator());
        panel.add(Box.createVerticalStrut(10));

        // Highlight tarif
        JPanel tarifPanel = new JPanel(new BorderLayout());
        tarifPanel.setBackground(new Color(248, 249, 250));
        tarifPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(33, 150, 243), 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        JLabel lblTarif = new JLabel("TOTAL BIAYA");
        lblTarif.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 15));
        lblTarif.setForeground(new Color(33, 150, 243));
        JLabel valTarif = new JLabel(formatTarif(data.tarif));
        valTarif.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 18));
        valTarif.setForeground(new Color(76, 175, 80));
        tarifPanel.add(lblTarif, BorderLayout.WEST);
        tarifPanel.add(valTarif, BorderLayout.EAST);
        panel.add(tarifPanel);

        panel.add(Box.createVerticalStrut(10));
        panel.add(new JSeparator());
        panel.add(Box.createVerticalStrut(10));

        JLabel thanks = new JLabel("Terima kasih atas kunjungan Anda!", SwingConstants.CENTER);
        thanks.setFont(new java.awt.Font("Segoe UI", java.awt.Font.ITALIC, 13));
        thanks.setForeground(new Color(76, 175, 80));
        thanks.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel info = new JLabel("Simpan struk ini sebagai bukti parkir", SwingConstants.CENTER);
        info.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 11));
        info.setForeground(new Color(120, 120, 120));
        info.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(thanks);
        panel.add(Box.createVerticalStrut(3));
        panel.add(info);

        return panel;
    }

    private JPanel createRow(String label, String value) {
        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(Color.WHITE);
        JLabel lbl = new JLabel(label);
        lbl.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
        lbl.setForeground(new Color(80, 80, 80));
        JLabel val = new JLabel(value);
        val.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
        val.setForeground(Color.BLACK);
        row.add(lbl, BorderLayout.WEST);
        row.add(val, BorderLayout.EAST);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 22));
        return row;
    }

    private boolean isValidSelection() {
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                parent,
                "Pilih baris data yang ingin dicetak struknya!",
                "Peringatan",
                JOptionPane.WARNING_MESSAGE
            );
            return false;
        }
        return true;
    }

    private StrukData extractDataFromTable() {
        StrukData data = new StrukData();
        data.plat = getValueOrDefault(0, "-");
        data.jenis = getValueOrDefault(1, "-");
        data.jamMasuk = getValueOrDefault(2, "-");
        data.jamKeluar = getValueOrDefault(3, "-");
        data.durasi = getValueOrDefault(4, "-");
        data.tarif = getValueOrDefault(5, "-");
        return data;
    }

    private String getValueOrDefault(int column, String defaultValue) {
        Object value = tableModel.getValueAt(selectedRow, column);
        if (value == null || value.toString().trim().isEmpty()) {
            return defaultValue;
        }
        return value.toString();
    }

    private String formatDurasi(String durasi) {
        if (durasi.equals("-") || durasi.trim().isEmpty()) return "-";
        try {
            int minutes = Integer.parseInt(durasi);
            if (minutes < 60) return minutes + " menit";
            int hours = minutes / 60;
            int mins = minutes % 60;
            return mins == 0 ? hours + " jam" : hours + " jam " + mins + " menit";
        } catch (NumberFormatException e) {
            return durasi + " menit";
        }
    }

    private String formatTarif(String tarif) {
        if (tarif.equals("-") || tarif.trim().isEmpty()) return "Gratis";
        try {
            long amount = Long.parseLong(tarif);
            if (amount == 0) return "Gratis";
            return String.format("Rp %,d", amount);
        } catch (NumberFormatException e) {
            return "Rp " + tarif;
        }
    }

    private void downloadStrukAsPDF(StrukData data) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Simpan Struk Parkir sebagai PDF");
        fileChooser.setFileFilter(new FileNameExtensionFilter("PDF Files", "pdf"));
        fileChooser.setSelectedFile(new java.io.File("struk-parkir-" + data.plat + ".pdf"));
        int userSelection = fileChooser.showSaveDialog(parent);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            java.io.File fileToSave = fileChooser.getSelectedFile();
            String filePath = fileToSave.getAbsolutePath();
            if (!filePath.toLowerCase().endsWith(".pdf")) {
                filePath += ".pdf";
            }
            try {
                createPDFRapi(filePath, data);
                JOptionPane.showMessageDialog(parent, "Struk berhasil disimpan ke:\n" + filePath, "Sukses", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(parent, "Gagal menyimpan PDF: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void createPDFRapi(String filePath, StrukData data) throws Exception {
        Document document = new Document(PageSize.A6, 24, 24, 24, 24);
        PdfWriter.getInstance(document, new FileOutputStream(filePath));
        document.open();

        com.itextpdf.text.Font titleFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 16, com.itextpdf.text.Font.BOLD, new BaseColor(33, 150, 243));
        com.itextpdf.text.Font subFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 9, com.itextpdf.text.Font.NORMAL, BaseColor.GRAY);
        com.itextpdf.text.Font labelFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 10, com.itextpdf.text.Font.NORMAL, BaseColor.BLACK);
        com.itextpdf.text.Font valueFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 10, com.itextpdf.text.Font.BOLD, BaseColor.BLACK);
        com.itextpdf.text.Font totalFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 12, com.itextpdf.text.Font.BOLD, new BaseColor(76, 175, 80));
        com.itextpdf.text.Font footerFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 10, com.itextpdf.text.Font.ITALIC, new BaseColor(76, 175, 80));

        Paragraph title = new Paragraph("STRUK PARKIR", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        Paragraph sub = new Paragraph("SISTEM PARKIR OTOMATIS", subFont);
        sub.setAlignment(Element.ALIGN_CENTER);
        document.add(sub);

        Paragraph waktuCetak = new Paragraph("Dicetak: " +
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")), subFont);
        waktuCetak.setAlignment(Element.ALIGN_CENTER);
        waktuCetak.setSpacingAfter(8);
        document.add(waktuCetak);

        document.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(2);
        table.setWidths(new float[]{1.2f, 1.5f});
        table.setWidthPercentage(100);

        addPDFRow(table, "Plat Nomor", data.plat, labelFont, valueFont);
        addPDFRow(table, "Jenis Kendaraan", data.jenis, labelFont, valueFont);
        addPDFRow(table, "Waktu Masuk", data.jamMasuk, labelFont, valueFont);
        addPDFRow(table, "Waktu Keluar", data.jamKeluar, labelFont, valueFont);
        addPDFRow(table, "Durasi Parkir", formatDurasi(data.durasi), labelFont, valueFont);

        document.add(table);

        document.add(new Paragraph(" "));

        PdfPTable totalTable = new PdfPTable(2);
        totalTable.setWidths(new float[]{1.2f, 1.5f});
        totalTable.setWidthPercentage(100);

        PdfPCell totalLabel = new PdfPCell(new Phrase("TOTAL BIAYA", labelFont));
        totalLabel.setBorder(0);
        totalLabel.setBackgroundColor(new BaseColor(248, 249, 250));
        totalLabel.setPadding(6);

        PdfPCell totalValue = new PdfPCell(new Phrase(formatTarif(data.tarif), totalFont));
        totalValue.setBorder(0);
        totalValue.setBackgroundColor(new BaseColor(248, 249, 250));
        totalValue.setPadding(6);
        totalValue.setHorizontalAlignment(Element.ALIGN_RIGHT);

        totalTable.addCell(totalLabel);
        totalTable.addCell(totalValue);

        document.add(totalTable);

        document.add(new Paragraph(" "));

        Paragraph thanks = new Paragraph("Terima kasih atas kunjungan Anda!", footerFont);
        thanks.setAlignment(Element.ALIGN_CENTER);
        document.add(thanks);

        Paragraph info = new Paragraph("Simpan struk ini sebagai bukti parkir", subFont);
        info.setAlignment(Element.ALIGN_CENTER);
        document.add(info);

        document.close();
    }

    private void addPDFRow(PdfPTable table, String label, String value, com.itextpdf.text.Font labelFont, com.itextpdf.text.Font valueFont) {
        PdfPCell cell1 = new PdfPCell(new Phrase(label, labelFont));
        cell1.setBorder(0);
        cell1.setPadding(4);
        PdfPCell cell2 = new PdfPCell(new Phrase(value, valueFont));
        cell2.setBorder(0);
        cell2.setPadding(4);
        cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell1);
        table.addCell(cell2);
    }

    private static class StrukData {
        String plat;
        String jenis;
        String jamMasuk;
        String jamKeluar;
        String durasi;
        String tarif;
    }
}