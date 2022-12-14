import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.*;

public class Magazin extends JFrame {
    private JPanel Shop;
    private JTable inventar;

    private JTable cumparaturi;
    private JButton btnAdauga;
    private JButton btnSterge;
    private JButton btnComanda;
    private Component panell;

    static float Total = 0;


    public void main(String[] args) {
        JFrame f = new JFrame("Magazin");
        f.setSize(300, 300);
        f.add(new Magazin().panell);
        f.setVisible(true);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }



    public Magazin(){
        setVisible(true);
        setTitle("Magazin");
        setContentPane(Shop);
        setMinimumSize(new Dimension(750, 400));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        try {
            Image logo = ImageIO.read(new File("src/asset/cupcake.png"));
            setIconImage(logo);
        } catch (IOException e) {
            e.printStackTrace();
        }


        btnAdauga.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPanel AdaugaInCos = new JPanel();
                AdaugaProdusInCos adaugaProdusInCos = new AdaugaProdusInCos();
            }
        });
        btnComanda.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPanel PlaseazaComanda = new JPanel();
                PlaseazaComanda plaseazaComanda = new PlaseazaComanda();
                dispose();
            }
        });
        btnSterge.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get the selected row index from the cumparaturi table
                int selectedRowIndex = cumparaturi.getSelectedRow();

                // Check if a row is selected
                if (selectedRowIndex != -1) {
                    // Use the getValueAt() method of the JTable class to get the value of the ID column in the selected row
                    int selectedID = (int)cumparaturi.getValueAt(selectedRowIndex, 0);

                    // Execute a DELETE query with the selected ID to delete the row from the database
                    try {
                        Connection c = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                        Statement s = c.createStatement();
                        s.executeUpdate("DELETE FROM cos_cumparaturi WHERE ID = " + selectedID);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }

                    // Use the removeRow() method of the DefaultTableModel class to remove the selected row
                    ((DefaultTableModel)cumparaturi.getModel()).removeRow(selectedRowIndex);
                }

            }
        });
    }

    final String URL = "jdbc:mysql://localhost:3306/cofetarie";
    final String USERNAME = "root";
    final String PASSWORD = "password123";
    String header[] = {"ID", "Tort", "Pret", "Cantitate"};

    public void createUIComponents() {
        // TODO: place custom component creation code here
        DefaultTableModel tabel_inventar = new DefaultTableModel (0,4);
        tabel_inventar.setColumnIdentifiers(header);
        try {
            inventar = new JTable(tabel_inventar);
            Connection c = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery("select * from tort");
            while(rs.next())
            {
                Object[] row = {rs.getInt("ID"),rs.getString("denumire"),rs.getFloat("pret"),rs.getInt("gramaj")};
                tabel_inventar.addRow(row);
            }

        } catch (SQLException e){
            e.printStackTrace();
        }


        DefaultTableModel cosCumparaturi = new DefaultTableModel (0,4);
        cosCumparaturi.setColumnIdentifiers(header);
        try {
            cumparaturi = new JTable(cosCumparaturi);
            Connection c = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery("select * from cos_cumparaturi");
            while(rs.next())
            {
                Object[] row = {rs.getInt("ID"),rs.getString("articol"),rs.getFloat("pret"),rs.getInt("gramaj")};
                cosCumparaturi.addRow(row);
                Total = Total + rs.getFloat("pret");
            }

        } catch (SQLException e){
            e.printStackTrace();
        }

    }

}
