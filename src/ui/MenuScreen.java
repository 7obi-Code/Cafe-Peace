package ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;

import controllers.AlertCtrl;
import controllers.InvoiceCtrl;
import controllers.ProductCtrl;
import dao.DataAccessException;
import modules.Alert;
import modules.Invoice;
import modules.InvoiceLine;

public class MenuScreen extends JFrame {

    private static final long serialVersionUID = 1L;

    private JPanel contentPane;
    private JPanel menuPanel;     // Hide/show top bar med knapperne
    private JPanel cardPanel;     // Center området med paneler
    private CardLayout cardLayout;

    // ---- Indlagre-felter ----
    private JTable invoiceTable;      // Venstre
    private JTable countingTable;     // Højre
    private DefaultTableModel invoiceModel;   // Venstre
    private DefaultTableModel countingModel;  // Højre
    private JTextField txtInvoiceNumber;      // Invoice Field
    private JTextField txtStaffNumber;        // Medlemsnummer Field
    
    //Alert infobox fields
    private DefaultListModel<String> alertListModel;
    private JList<String> alertList;
    
    // Kode implementering fields
    
    @SuppressWarnings({ "unused" })
	private final InvoiceCtrl invoiceCtrl;
    private final ProductCtrl productCtrl;
	private final AlertCtrl alertCtrl;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) { //Nimbus UI Skin
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

    																								// TopMenu Start
    public MenuScreen() throws DataAccessException {
        this.invoiceCtrl = new InvoiceCtrl();
        this.productCtrl = new ProductCtrl();
        this.alertCtrl   = new AlertCtrl();
        
        
        setTitle("Cafe Peace - Lagersystem");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1600, 1000);
        setLocationRelativeTo(null);

        contentPane = new JPanel(new BorderLayout());
        setContentPane(contentPane);

        
        
        
        
     // ---------- TOP PANEL WITHOUT TOGGLE ----------
        JPanel topPanel = new JPanel(new BorderLayout());

        // Top row: the four buttons (menu buttons)
        menuPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnAflagre = new JButton("Aflagre Varer");
        JButton btnIndlagre = new JButton("Indlagre Varer");
        JButton btnLager   = new JButton("Lageroversigt");
        JButton btnIndkob  = new JButton("Orderlist");

        menuPanel.add(btnAflagre);
        menuPanel.add(btnIndlagre);
        menuPanel.add(btnLager);
        menuPanel.add(btnIndkob);

        // Add button row at the top
        topPanel.add(menuPanel, BorderLayout.NORTH);

        // Second row: alerts
        topPanel.add(createAlertsPanel(), BorderLayout.CENTER);

        // Add topPanel to window
        contentPane.add(topPanel, BorderLayout.NORTH);


        // ---------- CENTER AREA WITH SCREENS ----------
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        contentPane.add(cardPanel, BorderLayout.CENTER);

        // Register "screens"
        cardPanel.add(createAflagrePanel(), "AFLAGRE");
        cardPanel.add(createIndlagrePanel(), "INDLAGRE");

        // Button actions (tabs)
        btnAflagre.addActionListener(e -> cardLayout.show(cardPanel, "AFLAGRE"));
        btnIndlagre.addActionListener(e -> cardLayout.show(cardPanel, "INDLAGRE"));
        btnLager.addActionListener(e -> cardLayout.show(cardPanel, "LAGER"));
        btnIndkob.addActionListener(e -> cardLayout.show(cardPanel, "ORDER"));

    }
    																								// TopMenu Slut.

    
    
    																								//AlertPanel Start.
    private JPanel createAlertsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(350, 200)); //Infobox størrelse
        panel.setMinimumSize(new Dimension(350, 200)); //Infobox størrelse
        panel.setBorder(BorderFactory.createTitledBorder("Alerts"));

        alertListModel = new DefaultListModel<>();
        alertList = new JList<>(alertListModel);
        alertList.setFocusable(false);

        JScrollPane scroll = new JScrollPane(alertList);
        panel.add(scroll, BorderLayout.CENTER);

        //Henter alerts første gang
        loadAlertsIntoList();

        return panel;
    }
    
    //Load Alerts
    private void loadAlertsIntoList() {
        alertListModel.clear();
        try {
            List<Alert> alerts = alertCtrl.getRecentAlerts();
            for (Alert a : alerts) {
                
                String line = "[" + a.getSeverity() + "] " + a.getDescription();
                alertListModel.addElement(line);
            }
            if (alerts.isEmpty()) {
                alertListModel.addElement("Ingen aktive alerts.");
            }
        } catch (DataAccessException ex) {
            alertListModel.addElement("Fejl ved hentning af alerts.");
            System.err.println(ex.getMessage());
        }
    }

    																								//AlertPanel Slut.
    
    
    
    																								// IndlagrePanel Start.
    @SuppressWarnings("serial")
    private JPanel createIndlagrePanel() {
        JPanel indlagrePanel = new JPanel(new BorderLayout(10, 10));

        // ---------- TOP: FÆLLES HEADER-RÆKKE ----------
        JPanel headerRow = new JPanel(new GridLayout(1, 2, 10, 0)); // venstre/højre side

        // ========== VENSTRE HEADER: "Fakturavarer" + Fakturanr ==========
        JPanel leftHeader = new JPanel(new BorderLayout());
        // tiny push to the right
        leftHeader.setBorder(BorderFactory.createEmptyBorder(0, 7, 0, 0));  


        JLabel lblLeftTitle = new JLabel("Fakturavarer");
        lblLeftTitle.setFont(lblLeftTitle.getFont().deriveFont(16f));
        // mere til venstre:
        lblLeftTitle.setHorizontalAlignment(SwingConstants.LEFT);
        leftHeader.add(lblLeftTitle, BorderLayout.WEST);

        // Fakturanr + tekstfelt + knap på samme linje
        JPanel invoiceTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        JLabel lblInvoice = new JLabel("Fakturanr:");
        txtInvoiceNumber = new JTextField(10);
        JButton btnLoadInvoice = new JButton("Hent faktura");

        invoiceTop.add(lblInvoice);
        invoiceTop.add(txtInvoiceNumber);
        invoiceTop.add(btnLoadInvoice);

        leftHeader.add(invoiceTop, BorderLayout.EAST);

        // ========== HØJRE HEADER: "Optælling" + Medarbejdernr + Bekræft ==========
        JPanel rightHeader = new JPanel(new BorderLayout());

        JLabel lblRightTitle = new JLabel("Optælling");
        lblRightTitle.setFont(lblRightTitle.getFont().deriveFont(16f));
        lblRightTitle.setHorizontalAlignment(SwingConstants.LEFT); // også venstrestillet
        rightHeader.add(lblRightTitle, BorderLayout.WEST);

        JButton btnConfirm = new JButton("Bekræft");
        btnConfirm.setBackground(new Color(0, 170, 0));
        btnConfirm.setForeground(Color.WHITE);
        btnConfirm.setFocusPainted(false);

        JPanel btnWrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        btnWrapper.setOpaque(false);

        JLabel lblStaff = new JLabel("Medarbejdernr:");
        txtStaffNumber = new JTextField(8);

        btnWrapper.add(lblStaff);
        btnWrapper.add(txtStaffNumber);
        btnWrapper.add(btnConfirm);

        rightHeader.add(btnWrapper, BorderLayout.EAST);

        // læg begge headers i top-rækken
        headerRow.add(leftHeader);
        headerRow.add(rightHeader);

        indlagrePanel.add(headerRow, BorderLayout.NORTH);

        // ---------- CENTER: To tabeller side om side ----------
        JPanel center = new JPanel(new GridLayout(1, 2, 10, 0)); // 1 række, 2 kolonner

        // ========== VENSTRE: FAKTURAVARER (kun tabel nu) ==========
        JPanel invoicePanel = new JPanel(new BorderLayout());

        String[] invoiceColumns = { "Varenr", "Navn", "Faktura-antal" };
        invoiceModel = new DefaultTableModel(invoiceColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        invoiceTable = new JTable(invoiceModel);
        invoicePanel.add(new JScrollPane(invoiceTable), BorderLayout.CENTER);

        // ========== HØJRE: OPTÆLLING (kun tabel nu) ==========
        JPanel countingPanel = new JPanel(new BorderLayout());

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

        indlagrePanel.add(center, BorderLayout.CENTER);

        // ---------- Knap-action: Hent faktura ----------
        btnLoadInvoice.addActionListener(e -> loadInvoice());

        // ---------- Bekræft-logik ----------
        btnConfirm.addActionListener(e -> {
            // --- Tjek medarbejdernr først ---
            String staffText = txtStaffNumber.getText().trim();
            if (staffText.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Indtast et medarbejdernr.",
                    "Manglende input",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            int staffNumber;
            try {
                staffNumber = Integer.parseInt(staffText); // Checker om staffnummeret er ugyldigt.
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                    "Medarbejdernr skal være et tal.",
                    "Ugyldigt medarbejdernr",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            HashMap<Integer, Integer> qtyByProductId = new HashMap<>();

            // Loop through table rows
            for (int i = 0; i < countingModel.getRowCount(); i++) {
                Object prodIdObj = countingModel.getValueAt(i, 0); // Varenr
                Object countedObj = countingModel.getValueAt(i, 3); // Optalt antal

                if (prodIdObj != null && countedObj != null) {
                    try {
                        int prodId = (Integer) prodIdObj;
                        int countedQty = Integer.parseInt(countedObj.toString());
                        qtyByProductId.put(prodId, countedQty);
                    } catch (NumberFormatException err) {
                        // ignorér ugyldige rækker
                    }
                }
            }

            try {
                productCtrl.confirmDeposit(qtyByProductId);
                JOptionPane.showMessageDialog(this,
                    "Lager opdateret med optalte antal for medarbejder " + staffNumber + "!",
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

        return indlagrePanel;
    }
    																								// IndlagrePanel slut

    
    
    																								// LoadInvoice Start
    																								// Finder invoice med insertInvoice(invoiceNo), igennem productCtrl.
    private void loadInvoice() {
        String text = txtInvoiceNumber.getText().trim();
        if (text.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Indtast et fakturanummer.",
                    "Manglende input",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int invoiceNo;
        try {
            invoiceNo = Integer.parseInt(text);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Fakturanummer skal være et tal.",
                    "Ugyldigt fakturanummer",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        try {
            Invoice invoice = productCtrl.insertInvoice(invoiceNo);
            if (invoice == null) {
                JOptionPane.showMessageDialog(
                        this,
                        "Ingen faktura fundet med nummer: " + invoiceNo,
                        "Ikke fundet",
                        JOptionPane.INFORMATION_MESSAGE
                );
                return;
            }

            // ryd gamle data
            invoiceModel.setRowCount(0);
            countingModel.setRowCount(0);

            // Udfylder tabeller ud fra fakturalinjerne
            for (InvoiceLine line : invoice.getInvoiceLines()) {
                var product = line.getProduct();

                // Fakturavarer (venstre tabel)
                Object[] leftRow = {
                        product.getProductId(),
                        product.getName(),
                        line.getQuantity()
                };
                invoiceModel.addRow(leftRow);

                // Optællings tabel til højre.
                Object[] rightRow = {
                        product.getProductId(),
                        product.getName(),
                        product.getStock().getAmount(),
                        null    // Optæller, der skrives selv optalt antal, derfor null.
                };
                countingModel.addRow(rightRow);
            }

            // Catcher til try hele segmentet (DataAccessException)
        } catch (DataAccessException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                    this,
                    "Fejl ved hentning af faktura: " + ex.getMessage(),
                    "Databasefejl",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
    																								// LoadInvoice Slut.

    
    
    																								// AflagerPanel Start.
    @SuppressWarnings("serial")
    private JPanel createAflagrePanel() {
        JPanel aflagerPanel = new JPanel(new BorderLayout(10, 10));

        // CENTER: To tabeller side om side (samme idé som Indlagre)
        JPanel center = new JPanel(new GridLayout(1, 2, 10, 0));

        // Venstre: Lageroversigt (hvad har vi lige nu)
        JPanel leftPanel = new JPanel(new BorderLayout());
        JLabel lblLeft = new JLabel("Lager (til rådighed)", SwingConstants.CENTER);
        lblLeft.setFont(lblLeft.getFont().deriveFont(16f));
        leftPanel.add(lblLeft, BorderLayout.NORTH);

        String[] leftCols = { "Varenr", "Navn", "Nuværende antal" };
        DefaultTableModel stockModel = new DefaultTableModel(leftCols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // lagerlisten ændres ikke direkte her
                return false;
            }
        };
        JTable stockTable = new JTable(stockModel);
        leftPanel.add(new JScrollPane(stockTable), BorderLayout.CENTER);

        // Højre: Aflagte varer (dem vi “bruger”/afskriver)
        JPanel rightPanel = new JPanel(new BorderLayout());
        JLabel lblRight = new JLabel("Aflagte varer", SwingConstants.CENTER);
        lblRight.setFont(lblRight.getFont().deriveFont(16f));
        rightPanel.add(lblRight, BorderLayout.NORTH);

        String[] rightCols = { "Varenr", "Navn", "Aflagret antal" };
        DefaultTableModel usedModel = new DefaultTableModel(rightCols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // kun Aflagret antal må ændres
                return column == 2;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 2 ? Integer.class : String.class;
            }
        };
        JTable usedTable = new JTable(usedModel);
        rightPanel.add(new JScrollPane(usedTable), BorderLayout.CENTER);

        center.add(leftPanel);
        center.add(rightPanel);

        aflagerPanel.add(center, BorderLayout.CENTER);

        // BUND: Bekræft-knap til at gemme aflagring (kan kobles til controller senere)
        JButton btnConfirmAflagre = new JButton("Bekræft aflagring");
        btnConfirmAflagre.setBackground(new Color(180, 0, 0));
        btnConfirmAflagre.setForeground(Color.WHITE);
        btnConfirmAflagre.setFocusPainted(false);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(btnConfirmAflagre);
        aflagerPanel.add(bottom, BorderLayout.SOUTH);

        return aflagerPanel;
    }
    																								//AflagerPanel Slut.
}
