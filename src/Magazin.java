import javax.imageio.ImageIO;
import javax.swing.*;
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
        f.setSize(200, 300);
        f.add(new Magazin().panell);
        f.setVisible(true);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public Magazin(){
        setVisible(true);
        setTitle("Magazin");
        setContentPane(Shop);
        setMinimumSize(new Dimension(1000, 400));
        setLocationRelativeTo(null);
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
                int selectedRowIndex = cumparaturi.getSelectedRow();

                if (selectedRowIndex != -1) {
                    int selectedID = (int)cumparaturi.getValueAt(selectedRowIndex, 0);
                    Total = Total - (float) cumparaturi.getValueAt(selectedRowIndex,2);

                    try {
                        Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                        Statement statement = connection.createStatement();
                        statement.executeUpdate("DELETE FROM cos_cumparaturi WHERE ID = " + selectedID);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }

                    ((DefaultTableModel)cumparaturi.getModel()).removeRow(selectedRowIndex);
                }

            }
        });
    }

    final String URL = "jdbc:mysql://localhost:3306/cofetarie";
    final String USERNAME = "root";
    final String PASSWORD = "password123";

    String header[] = {"ID", "Tort", "Pret / 100g (RON)", "Cantitate in stoc"};

    String headerCos[] = {"ID", "Tort", "Pret (RON)", "Cantitate in grame"};


    public void createUIComponents() {

        JButton btnUpdateCos = new JButton("Actualizeaza Cos");
        JButton btnUpdateInventar = new JButton("Actualizeaza Inventar");
        JPanel mainPanel = new JPanel();
        mainPanel.add(btnUpdateCos);
        mainPanel.add(btnUpdateInventar);
        mainPanel.add(new JScrollPane(cumparaturi));
        mainPanel.add(new JScrollPane(inventar));
        JFrame mainFrame = new JFrame();
        mainFrame.setSize(250, 105);
        mainFrame.add(mainPanel);
        mainFrame.setLocation(900, 215);
        mainFrame.setVisible(true);
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);


        DefaultTableModel tabel_inventar = new DefaultTableModel (0,4);
        tabel_inventar.setColumnIdentifiers(header);
        inventar = new JTable(tabel_inventar);
        inventar.setEnabled(false);

        try {
            Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("select * from inventar");
            while(rs.next())
            {
                Object[] row = {rs.getInt("ID"),rs.getString("denumire"),rs.getFloat("pret"),rs.getInt("gramaj")};
                tabel_inventar.addRow(row);
            }

        } catch (SQLException e){
            e.printStackTrace();
        }

        DefaultTableModel cosCumparaturi = new DefaultTableModel (0,4);
        cosCumparaturi.setColumnIdentifiers(headerCos);
        cumparaturi = new JTable(cosCumparaturi);


        try {
            Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("select * from cos_cumparaturi");
            while(rs.next())
            {
                Object[] row = {rs.getInt("ID"),rs.getString("articol"),rs.getFloat("pret"),rs.getInt("gramaj")};
                cosCumparaturi.addRow(row);
                Total = Total + rs.getFloat("pret");
            }

            TableColumn idColumn = cumparaturi.getColumnModel().getColumn(0);
            idColumn.setMinWidth(0);
            idColumn.setMaxWidth(0);
            idColumn.setWidth(0);
            idColumn.setPreferredWidth(0);
            idColumn.setResizable(false);

        } catch (SQLException e1){
            e1.printStackTrace();
        }

        btnUpdateCos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    cosCumparaturi.setRowCount(0);

                    Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                    Statement statement = connection.createStatement();

                    ResultSet rs = statement.executeQuery("select * from cos_cumparaturi");

                    while(rs.next()) {
                        Object[] row = {rs.getInt("ID"), rs.getString("articol"), rs.getFloat("pret"), rs.getInt("gramaj")};
                        cosCumparaturi.addRow(row);
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
        btnUpdateInventar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    tabel_inventar.setRowCount(0);

                    Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                    Statement statement = connection.createStatement();
                    ResultSet rs = statement.executeQuery("select * from inventar");
                    while(rs.next())
                    {
                        Object[] row = {rs.getInt("ID"),rs.getString("denumire"),rs.getFloat("pret"),rs.getInt("gramaj")};
                        tabel_inventar.addRow(row);
                    }

                } catch (SQLException e1){
                    e1.printStackTrace();
                }
            }
        });





    }

}
