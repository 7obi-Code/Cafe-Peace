package ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.EventQueue;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class MenuScreen extends JFrame {

    private static final long serialVersionUID = 1L;

    private JPanel contentPane;
    private JPanel menuPanel;     // the hide/show bar with buttons
    private JPanel cardPanel;     // center area with screens
    private CardLayout cardLayout;

    // ---- Indlagre-felter ----
    private JTable invoiceTable;
    private JTable countingTable;
    private DefaultTableModel invoiceModel;
    private DefaultTableModel countingModel;
    private JTextField txtInvoiceNumber;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }

                MenuScreen frame = new MenuScreen();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public MenuScreen() {
        setTitle("Cafe Peace - Lagersystem");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);

        contentPane = new JPanel(new BorderLayout());
        setContentPane(contentPane);

        // ---------- TOP PANEL (contains toggle + menu bar) ----------
        JPanel topPanel = new JPanel(new BorderLayout());

        // Toggle button
        JToggleButton menuToggle = new JToggleButton("Menu ▲");
        menuToggle.setFocusPainted(false);
        topPanel.add(menuToggle, BorderLayout.NORTH);

        // Menu bar with "tabs"
        menuPanel = new JPanel(); // FlowLayout = row of buttons
        JButton btnAflagre = new JButton("Aflagre Varer");
        JButton btnIndlagre = new JButton("Indlagre Varer");
        JButton btnLager   = new JButton("Lageroversigt");
        JButton btnIndkob  = new JButton("Orderlist");

        menuPanel.add(btnAflagre);
        menuPanel.add(btnIndlagre);
        menuPanel.add(btnLager);
        menuPanel.add(btnIndkob);

        topPanel.add(menuPanel, BorderLayout.CENTER);

        // Add the whole topPanel once to the NORTH
        contentPane.add(topPanel, BorderLayout.NORTH);

        // ---------- CENTER AREA WITH SCREENS ----------
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        contentPane.add(cardPanel, BorderLayout.CENTER);

        // Register "screens"
        cardPanel.add(createLabelPanel("Aflagre Varer - skærm"), "AFLAGRE");
        cardPanel.add(createIndlagrePanel(), "INDLAGRE");
        cardPanel.add(createLabelPanel("Lageroversigt - skærm"), "LAGER");
        cardPanel.add(createLabelPanel("Indkøbsliste - skærm"), "ORDER");

        // Button actions (tabs)
        btnAflagre.addActionListener(e -> cardLayout.show(cardPanel, "AFLAGRE"));
        btnIndlagre.addActionListener(e -> cardLayout.show(cardPanel, "INDLAGRE"));
        btnLager.addActionListener(e -> cardLayout.show(cardPanel, "LAGER"));
        btnIndkob.addActionListener(e -> cardLayout.show(cardPanel, "ORDER"));

        // Toggle menu visibility
        menuToggle.setSelected(true); // start visible
        menuToggle.addActionListener(e -> {
            boolean visible = menuToggle.isSelected();
            menuPanel.setVisible(visible);
            menuToggle.setText(visible ? "Menu ▲" : "Menu ▼");
            topPanel.revalidate();
            topPanel.repaint();
        });

        // Default screen
        cardLayout.show(cardPanel, "AFLAGRE");
    }

    private JPanel createLabelPanel(String text) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(label.getFont().deriveFont(20f));
        panel.add(label, BorderLayout.CENTER);
        return panel;
    }

    // ---- Indlagre: Faktura-varer + Optælling i samme vindue ----
    private JPanel createIndlagrePanel() {
        JPanel root = new JPanel(new BorderLayout(10, 10));

        // ---------- TOP: Faktura-nummer + knap ----------
        JPanel top = new JPanel();
        JLabel lblInvoice = new JLabel("Fakturanr:");
        txtInvoiceNumber = new JTextField(10);
        JButton btnLoadInvoice = new JButton("Hent faktura");

        top.add(lblInvoice);
        top.add(txtInvoiceNumber);
        top.add(btnLoadInvoice);

        root.add(top, BorderLayout.NORTH);

        // ---------- CENTER: To tabeller side om side ----------
        JPanel center = new JPanel(new GridLayout(1, 2, 10, 0)); // 1 række, 2 kolonner

        // Venstre: Fakturavarer
        JPanel invoicePanel = new JPanel(new BorderLayout());
        JLabel lblLeftTitle = new JLabel("Fakturavarer", SwingConstants.CENTER);
        lblLeftTitle.setFont(lblLeftTitle.getFont().deriveFont(16f));
        invoicePanel.add(lblLeftTitle, BorderLayout.NORTH);

        String[] invoiceColumns = { "Varenr", "Navn", "Faktura-antal", "Enhedspris" };
        invoiceModel = new DefaultTableModel(invoiceColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Faktura-data ændres normalt ikke her
            }
        };
        invoiceTable = new JTable(invoiceModel);
        invoicePanel.add(new JScrollPane(invoiceTable), BorderLayout.CENTER);

        // Højre: Optælling
        JPanel countingPanel = new JPanel(new BorderLayout());
        JLabel lblRightTitle = new JLabel("Optælling", SwingConstants.CENTER);
        lblRightTitle.setFont(lblRightTitle.getFont().deriveFont(16f));
        countingPanel.add(lblRightTitle, BorderLayout.NORTH);

        String[] countingColumns = { "Varenr", "Navn", "System-antal", "Optalt antal" };
        countingModel = new DefaultTableModel(countingColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Kun "Optalt antal" (kolonne 3) må ændres
                return column == 3;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 2 || columnIndex == 3) {
                    return Integer.class;
                }
                return String.class;
            }
        };
        countingTable = new JTable(countingModel);
        countingPanel.add(new JScrollPane(countingTable), BorderLayout.CENTER);

        // Tilføj begge paneler til center
        center.add(invoicePanel);
        center.add(countingPanel);

        root.add(center, BorderLayout.CENTER);

        // ---------- Knap-action: Hent faktura ----------
        btnLoadInvoice.addActionListener(e -> loadInvoice());

        return root;
    }

    // TODO: Kobl denne til jeres rigtige faktura-læsemetode
    private void loadInvoice() {
        String invoiceNo = txtInvoiceNumber.getText().trim();
        if (invoiceNo.isEmpty()) {
            System.out.println("Ingen fakturanr indtastet");
            return;
        }

        // Ryd gamle rækker
        invoiceModel.setRowCount(0);
        countingModel.setRowCount(0);

        // 🔧 DEMO-DATA (erstattes med data fra jeres faktura)
        invoiceModel.addRow(new Object[] { "1001", "Eksempel vare A", 10, 25.00 });
        invoiceModel.addRow(new Object[] { "1002", "Eksempel vare B", 5, 12.50 });

        countingModel.addRow(new Object[] { "1001", "Eksempel vare A", 100, null });
        countingModel.addRow(new Object[] { "1002", "Eksempel vare B", 50, null });
    }
}
