package src;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class VueGestionCoureurs extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;
    private JButton btnAjouter;
    private JButton btnRetour;

    public VueGestionCoureurs() {
        setTitle("🏃‍♂️ Gestion des Coureurs - JavaStocks");
        setSize(650, 450); // Un peu plus large pour la nouvelle colonne
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(248, 249, 250));

        JLabel lblTitre = new JLabel("Liste des Coureurs Inscrits", SwingConstants.CENTER);
        lblTitre.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitre.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        add(lblTitre, BorderLayout.NORTH);

        // --- Tableau mis à jour (3 colonnes maintenant) ---
        tableModel = new DefaultTableModel(new String[]{"ID Coureur", "Identifiant / Nom", "Épreuve"}, 0) {
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

        table.getColumnModel().getColumn(0).setPreferredWidth(80);
        table.getColumnModel().getColumn(1).setPreferredWidth(250);
        table.getColumnModel().getColumn(2).setPreferredWidth(200);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        add(scrollPane, BorderLayout.CENTER);

        // --- Boutons ---
        JPanel panelBas = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
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

        btnAjouter = new JButton("AJOUTER UN COUREUR");
        btnAjouter.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnAjouter.setBackground(new Color(39, 174, 96)); 
        btnAjouter.setForeground(Color.WHITE); 
        btnAjouter.setFocusPainted(false);
        btnAjouter.setOpaque(true); 
        btnAjouter.setBorderPainted(false); 
        btnAjouter.setPreferredSize(new Dimension(220, 40));
        btnAjouter.setCursor(new Cursor(Cursor.HAND_CURSOR));

        panelBas.add(btnRetour);
        panelBas.add(btnAjouter);
        add(panelBas, BorderLayout.SOUTH);
    }

    public void viderTableau() { tableModel.setRowCount(0); }
    public void ajouterLigneTableau(int id, String nom, String epreuve) { tableModel.addRow(new Object[]{id, nom, epreuve}); }

    public JButton getBtnAjouter() { return btnAjouter; }
    public JButton getBtnRetour() { return btnRetour; }
}