package src;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class VueProduitsRupture extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;
    private JButton btnRetour;

    public VueProduitsRupture() {
        setTitle("🔴 Produits en Rupture - JavaStocks");
        setSize(700, 450);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(248, 249, 250));

        // --- Titre ---
        JLabel lblTitre = new JLabel("🚨 Alerte : Articles en Rupture de Stock", SwingConstants.CENTER);
        lblTitre.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitre.setForeground(new Color(231, 76, 60)); // Texte en rouge pour le côté "Alerte"
        lblTitre.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        add(lblTitre, BorderLayout.NORTH);

        // --- Tableau ---
        tableModel = new DefaultTableModel(new String[]{"ID Article", "Libellé de l'article", "Stock Actuel"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        table = new JTable(tableModel);
        table.setRowHeight(30);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setOpaque(false);
        table.getTableHeader().setBackground(new Color(231, 76, 60)); // En-tête rouge
        table.getTableHeader().setForeground(Color.WHITE);
        
        table.getTableHeader().setDefaultRenderer(new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground(new Color(231, 76, 60)); // On force le rouge
                c.setForeground(Color.WHITE);
                ((JLabel) c).setHorizontalAlignment(SwingConstants.CENTER);
                ((JLabel) c).setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, new Color(200, 200, 200)));
                return c;
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        add(scrollPane, BorderLayout.CENTER);

        // --- Bouton Retour ---
        JPanel panelBas = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBas.setOpaque(false);
        panelBas.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        btnRetour = new JButton("RETOUR AU MENU ALERTES");
        btnRetour.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnRetour.setBackground(new Color(149, 165, 166)); 
        btnRetour.setForeground(Color.WHITE);
        btnRetour.setFocusPainted(false);
        btnRetour.setOpaque(true);
        btnRetour.setBorderPainted(false);
        btnRetour.setPreferredSize(new Dimension(250, 40));
        btnRetour.setCursor(new Cursor(Cursor.HAND_CURSOR));

        panelBas.add(btnRetour);
        add(panelBas, BorderLayout.SOUTH);
    }

    public void viderTableau() { tableModel.setRowCount(0); }
    public void ajouterLigneTableau(Object[] ligne) { tableModel.addRow(ligne); }

    public JButton getBtnRetour() { return btnRetour; }
}