package ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.Map;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Dimension;

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

import controllers.InvoiceCtrl;
import controllers.ProductCtrl;
import dao.DataAccessException;
import modules.Invoice;
import modules.InvoiceLine;
import javax.swing.JOptionPane;


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

    //Kode implementering fields
    private final InvoiceCtrl invoiceCtrl;
    private final ProductCtrl productCtrl;

    
    
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

    public MenuScreen() throws DataAccessException{
    	
        this.invoiceCtrl = new InvoiceCtrl();
        this.productCtrl = new ProductCtrl();
        
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

        // ========== VENSTRE: FAKTURAVARER ==========
        JPanel invoicePanel = new JPanel(new BorderLayout());

        JPanel leftHeader = new JPanel(new BorderLayout());
        JLabel lblLeftTitle = new JLabel("Fakturavarer", SwingConstants.CENTER);
        lblLeftTitle.setFont(lblLeftTitle.getFont().deriveFont(16f));
        leftHeader.add(lblLeftTitle, BorderLayout.CENTER);
        // fast højde så venstre og højre header matcher
        leftHeader.setPreferredSize(new Dimension(1, 40));
        invoicePanel.add(leftHeader, BorderLayout.NORTH);

        String[] invoiceColumns = { "Varenr", "Navn", "Faktura-antal"};
        invoiceModel = new DefaultTableModel(invoiceColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        invoiceTable = new JTable(invoiceModel);
        invoicePanel.add(new JScrollPane(invoiceTable), BorderLayout.CENTER);

        // ========== HØJRE: OPTÆLLING ==========
        JPanel countingPanel = new JPanel(new BorderLayout());

        // Header: titel + grøn "Bekræft"-knap
        JPanel rightHeader = new JPanel(new BorderLayout());
        JLabel lblRightTitle = new JLabel("Optælling", SwingConstants.CENTER);
        lblRightTitle.setFont(lblRightTitle.getFont().deriveFont(16f));
        rightHeader.add(lblRightTitle, BorderLayout.CENTER);

        JButton btnConfirm = new JButton("Bekræft");
        btnConfirm.setBackground(new Color(0, 180, 0));
        btnConfirm.setForeground(Color.WHITE);
        btnConfirm.setFocusPainted(false);

        JPanel btnWrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 7));
        btnWrapper.setOpaque(false);
        btnWrapper.add(btnConfirm);
        rightHeader.add(btnWrapper, BorderLayout.EAST);

        // samme faste højde som venstre header
        rightHeader.setPreferredSize(new Dimension(1, 40));
        countingPanel.add(rightHeader, BorderLayout.NORTH);

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

        // senere kan du koble btnConfirm til ProductCtrl.confirmDeposit()
        btnConfirm.addActionListener(e -> {
            HashMap<Integer, Integer> qtyByProductId = new HashMap<>();

            // Loop through table rows
            for (int i = 0; i < countingModel.getRowCount(); i++) {
                Object prodIdObj = countingModel.getValueAt(i, 0); // Varenr (ProductId)
                Object countedObj = countingModel.getValueAt(i, 3); // Optalt antal (User entered)

                if (prodIdObj != null && countedObj != null) {
                    try {
                        int prodId = (Integer) prodIdObj;
                        int countedQty = Integer.parseInt(countedObj.toString());
                        qtyByProductId.put(prodId, countedQty);
                    } catch (NumberFormatException err) {
                        // Optionally, notify user about bad input, or just skip
                        // JOptionPane.showMessageDialog(this, ...);
                    }
                }
            }

            // Call ProductCtrl to confirm deposit
            try {
                productCtrl.confirmDeposit(qtyByProductId);
                JOptionPane.showMessageDialog(this,
                    "Lager opdateret med optalte antal!",
                    "Succes",
                    JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this,
                    "Fejl ved opdatering af lager: " + ex.getMessage(),
                    "Fejl",
                    JOptionPane.ERROR_MESSAGE);
            }
        });

        return root;
    }

 // Loader faktura via controller og putter linjer i tabellerne
    private void loadInvoice() {
        String text = txtInvoiceNumber.getText().trim();
        if (text.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Indtast et fakturanummer.",
                    "Manglende input",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int invoiceNo;
        try {
            invoiceNo = Integer.parseInt(text);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Fakturanummer skal være et tal.",
                    "Ugyldigt fakturanummer",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Invoice invoice = productCtrl.insertInvoice(invoiceNo);
            if (invoice == null) {
                JOptionPane.showMessageDialog(this,
                        "Ingen faktura fundet med nummer: " + invoiceNo,
                        "Ikke fundet",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // ryd gamle data
            invoiceModel.setRowCount(0);
            countingModel.setRowCount(0);

            // udfyld tabeller ud fra fakturalinjerne
            for (InvoiceLine line : invoice.getInvoiceLines()) {
                var product = line.getProduct();

                // fakturavarer (venstre tabel)
                Object[] leftRow = {
                        product.getProductId(),
                        product.getName(),
                        line.getQuantity(),
             
                };
                invoiceModel.addRow(leftRow);

                // optælling (højre tabel) – system-antal er ukendt her,
                // I kan evt. hente det senere fra lageret.
                Object[] rightRow = {
                        product.getProductId(),
                        product.getName(),
                        product.getStock().getAmount(),
                        null    // Optalt antal (brugeren skriver selv)
                };
                countingModel.addRow(rightRow);
            }

        } catch (DataAccessException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Fejl ved hentning af faktura: " + ex.getMessage(),
                    "Databasefejl",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
