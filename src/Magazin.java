import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
