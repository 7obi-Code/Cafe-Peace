package ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.EventQueue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

public class MenuScreen extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JPanel menuPanel;     // the hide/show bar with buttons
    private JPanel cardPanel;     // center area with screens
    private CardLayout cardLayout;

    
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
        JButton btnLager = new JButton("Lageroversigt");
        JButton btnIndkob = new JButton("Orderlist");

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

        cardPanel.add(createLabelPanel("Aflagre Varer - skærm"), "AFLAGRE");
        cardPanel.add(createLabelPanel("Indlagre Varer - skærm"), "INDLAGRE");
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

        cardLayout.show(cardPanel, "AFLAGRE");
    }
    private JPanel createLabelPanel(String text) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(label.getFont().deriveFont(20f));
        panel.add(label, BorderLayout.CENTER);
        return panel;
    }
}