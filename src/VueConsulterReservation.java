package src;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class VueConsulterReservation extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;
    private JButton btnRetour;

    public VueConsulterReservation() {
        setTitle("🔍 Consulter les Réservations - JavaStocks");
        setSize(850, 500); // Une fenêtre bien large pour voir toutes les infos
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(248, 249, 250));

        JLabel lblTitre = new JLabel("Historique des Réservations", SwingConstants.CENTER);
        lblTitre.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitre.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        add(lblTitre, BorderLayout.NORTH);

        // --- Tableau avec 6 colonnes (dont la Date) ---
        tableModel = new DefaultTableModel(new String[]{"ID", "Coureur", "Article", "Quantité", "Date et Heure", "Statut"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { 
                return false; // Totalement en lecture seule
            }
        };
        table = new JTable(tableModel);
        table.setRowHeight(30);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // Style de l'en-tête
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

        // Largeur personnalisée pour certaines colonnes (la date a besoin de place)
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(4).setPreferredWidth(180);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        add(scrollPane, BorderLayout.CENTER);

        // --- Bouton Retour ---
        JPanel panelBas = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBas.setOpaque(false);
        panelBas.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        btnRetour = new JButton("RETOUR");
        btnRetour.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnRetour.setBackground(new Color(149, 165, 166)); 
        btnRetour.setForeground(Color.WHITE);
        btnRetour.setFocusPainted(false);
        btnRetour.setOpaque(true);
        btnRetour.setBorderPainted(false);
        btnRetour.setPreferredSize(new Dimension(150, 40));
        btnRetour.setCursor(new Cursor(Cursor.HAND_CURSOR));

        panelBas.add(btnRetour);
        add(panelBas, BorderLayout.SOUTH);
    }

    public void viderTableau() { tableModel.setRowCount(0); }
    public void ajouterLigneTableau(Object[] ligne) { tableModel.addRow(ligne); }

    public JButton getBtnRetour() { return btnRetour; }
}