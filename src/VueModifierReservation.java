package src;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class VueModifierReservation extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtNouvelleQuantite;
    private JComboBox<String> comboStatut;
    private JButton btnModifier;
    private JButton btnRetour;

    public VueModifierReservation() {
        setTitle("✏️ Modifier une Réservation - JavaStocks");
        setSize(800, 500); // Fenêtre plus grande pour le tableau
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(248, 249, 250));

        JLabel lblTitre = new JLabel("Gestion des Réservations", SwingConstants.CENTER);
        lblTitre.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitre.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        add(lblTitre, BorderLayout.NORTH);

        // --- Tableau ---
        tableModel = new DefaultTableModel(new String[]{"ID Rèsa", "Coureur", "Article", "Quantité", "Statut"}, 0) {
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

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        add(scrollPane, BorderLayout.CENTER);

        // --- Formulaire de modification (en bas) ---
        JPanel panelBas = new JPanel(new BorderLayout());
        panelBas.setOpaque(false);
        panelBas.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        JPanel panelForm = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panelForm.setOpaque(false);
        
        panelForm.add(new JLabel("Nouvelle Quantité :"));
        txtNouvelleQuantite = new JTextField(5);
        panelForm.add(txtNouvelleQuantite);

        panelForm.add(new JLabel("Nouveau Statut :"));
        comboStatut = new JComboBox<>(new String[]{"En cours", "Terminée", "Annulée"});
        panelForm.add(comboStatut);

        panelBas.add(panelForm, BorderLayout.NORTH);

        // --- Boutons ---
        JPanel panelBoutons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelBoutons.setOpaque(false);

        btnRetour = new JButton("RETOUR");
        styleButton(btnRetour, new Color(149, 165, 166));

        btnModifier = new JButton("ENREGISTRER LA MODIFICATION");
        styleButton(btnModifier, new Color(243, 156, 18)); // Orange

        panelBoutons.add(btnRetour);
        panelBoutons.add(btnModifier);
        panelBas.add(panelBoutons, BorderLayout.SOUTH);

        add(panelBas, BorderLayout.SOUTH);
    }

    private void styleButton(JButton btn, Color color) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public void viderTableau() { tableModel.setRowCount(0); }
    public void ajouterLigneTableau(Object[] ligne) { tableModel.addRow(ligne); }

    public JTable getTable() { return table; }
    public JTextField getTxtNouvelleQuantite() { return txtNouvelleQuantite; }
    public JComboBox<String> getComboStatut() { return comboStatut; }
    public JButton getBtnModifier() { return btnModifier; }
    public JButton getBtnRetour() { return btnRetour; }
}