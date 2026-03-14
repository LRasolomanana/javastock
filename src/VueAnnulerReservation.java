package src;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class VueAnnulerReservation extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;
    private JButton btnAnnulerResa;
    private JButton btnRetour;

    public VueAnnulerReservation() {
        setTitle("❌ Annuler une Réservation - JavaStocks");
        setSize(850, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(248, 249, 250));

        JLabel lblTitre = new JLabel("Annulation de Réservation", SwingConstants.CENTER);
        lblTitre.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitre.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        add(lblTitre, BorderLayout.NORTH);

        // --- Tableau ---
        tableModel = new DefaultTableModel(new String[]{"ID", "Coureur", "Article", "Quantité", "Statut"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(30);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setOpaque(false);
        table.getTableHeader().setBackground(new Color(41, 128, 185));
        table.getTableHeader().setForeground(Color.WHITE);
        
        table.getTableHeader().setDefaultRenderer(new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground(new Color(41, 128, 185));
                c.setForeground(Color.WHITE);
                ((JLabel) c).setHorizontalAlignment(SwingConstants.CENTER);
                ((JLabel) c).setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, new Color(200, 200, 200)));
                return c;
            }
        });

        table.getColumnModel().getColumn(0).setPreferredWidth(50);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        add(scrollPane, BorderLayout.CENTER);

        // --- Boutons du bas ---
        JPanel panelBas = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelBas.setOpaque(false);
        panelBas.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        btnRetour = new JButton("RETOUR");
        styleButton(btnRetour, new Color(149, 165, 166)); // Gris

        btnAnnulerResa = new JButton("SUPPRIMER LA RÉSERVATION");
        styleButton(btnAnnulerResa, new Color(231, 76, 60)); // Rouge

        panelBas.add(btnRetour);
        panelBas.add(btnAnnulerResa);
        add(panelBas, BorderLayout.SOUTH);
    }

    private void styleButton(JButton btn, Color color) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(250, 40));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public void viderTableau() { tableModel.setRowCount(0); }
    public void ajouterLigneTableau(Object[] ligne) { tableModel.addRow(ligne); }

    public JTable getTable() { return table; }
    public JButton getBtnAnnulerResa() { return btnAnnulerResa; }
    public JButton getBtnRetour() { return btnRetour; }
}