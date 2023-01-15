import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.*;

public class AdminInterface extends JFrame{
    private JTextField tfID;
    private JTextField tfDenumire;
    private JTextField tfPret;
    private JTextField tfGramaj;
    private JButton btnAdauga;
    private JButton btnMagazin;
    private JButton btnSterge;
    private JPanel adminIU;
    private JTable inventar;
    boolean idBool = false;
    int id;
    int idUpdate;
    boolean update = false;
    boolean denumireBool = false;
    String denumire ;
    boolean pretBool = false;
    float pret;
    double gramaj;
    private Component panell;

    public AdminInterface() {
        setVisible(true);
        setTitle("Admin Interface");
        setContentPane(adminIU);
        setMinimumSize(new Dimension(750, 400));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        try {
            Image logo = ImageIO.read(new File("src/asset/cupcake.png"));
            setIconImage(logo);
        } catch (
                IOException e) {
            e.printStackTrace();
        }

        final String URL = "jdbc:mysql://localhost:3306/cofetarie";
        final String USERNAME = "root";
        final String PASSWORD = "password123";





        btnAdauga.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                    Statement statement = connection.createStatement();

                    ResultSet rs = statement.executeQuery("select * from inventar");

                    while (rs.next()) {
                        if (Integer.valueOf(rs.getInt("ID")) == Integer.valueOf(tfID.getText())) {
                            idBool = true;
                            id = Integer.valueOf(tfID.getText());
                            idUpdate = rs.getInt("ID");
                            if (String.valueOf(rs.getString("denumire")) == String.valueOf(tfDenumire.getText())) {
                                denumireBool = true;
                                denumire = String.valueOf(tfDenumire.getText());
                                if (Float.valueOf(rs.getFloat("pret")) == Float.valueOf(tfPret.getText())) {
                                    pretBool = true;
                                    pret = Float.valueOf(tfPret.getText());
                                } else {
                                    pretBool = false;
                                }
                            } else {
                                denumireBool = false;
                            }
                        } else {
                            idBool = false;
                        }
                    }

                    statement.close();
                    connection.close();

                } catch (SQLException e1) {
                    e1.printStackTrace();
                }

                try {
                    Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                    Statement statement = connection.createStatement();

                    String query = "INSERT INTO inventar (id, denumire, pret, gramaj) VALUES (?, ?, ?, ?)";


                    if (idBool == true) {
                        update = true;
                        if (denumireBool == true) {
                            if (pretBool == true) {
                                gramaj = Double.parseDouble(tfGramaj.getText());
                            } else if (pretBool == false) {
                                pret = Float.parseFloat(tfPret.getText());
                                gramaj = Double.parseDouble(tfGramaj.getText());
                            }
                        } else if (denumireBool == false) {
                            denumire = String.valueOf(tfDenumire.getText());
                            pret = Float.parseFloat(tfPret.getText());
                            gramaj = Double.parseDouble(tfGramaj.getText());
                        }

                    } else if (idBool == false) {
                        id = Integer.parseInt(tfID.getText());
                        denumire = String.valueOf(tfDenumire.getText());
                        pret = Float.parseFloat(tfPret.getText());
                        gramaj = Double.parseDouble(tfGramaj.getText());

                        PreparedStatement preparedStatement = connection.prepareStatement(query);
                        preparedStatement.setInt(1, id);
                        preparedStatement.setString(2, denumire);
                        preparedStatement.setFloat(3, pret);
                        preparedStatement.setDouble(4, gramaj);
                        preparedStatement.executeUpdate();
                        Object[] row = {id, denumire, pret, gramaj};
                        ((DefaultTableModel)inventar.getModel()).addRow(row);

                    }

                    if(update == true)
                    {
                        PreparedStatement updateStatement = connection.prepareStatement("UPDATE inventar SET denumire = ?, pret = ?, gramaj = ? WHERE id = ?");

                        updateStatement.setString(1, denumire);
                        updateStatement.setFloat(2, pret);
                        updateStatement.setDouble(3, gramaj);
                        updateStatement.setInt(4, idUpdate);
                        updateStatement.executeUpdate();
                        update = false;
                    }

                    connection.close();

                } catch (SQLException e1) {
                    e1.printStackTrace();
                }

            }
        });
        btnMagazin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPanel Shop = new JPanel();
                Magazin magazin = new Magazin();
                dispose();
            }
        });
        btnSterge.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get the selected row index from the cumparaturi table
                int selectedRowIndex = inventar.getSelectedRow();

                // Check if a row is selected
                if (selectedRowIndex != -1) {
                    // Use the getValueAt() method of the JTable class to get the value of the ID column in the selected row
                    int selectedID = (int)inventar.getValueAt(selectedRowIndex, 0);

                    // Execute a DELETE query with the selected ID to delete the row from the database
                    try {
                        Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                        Statement statement = connection.createStatement();
                        statement.executeUpdate("DELETE FROM inventar WHERE ID = " + selectedID);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }

                    // Use the removeRow() method of the DefaultTableModel class to remove the selected row
                    ((DefaultTableModel)inventar.getModel()).removeRow(selectedRowIndex);
                }

            }
        });

    }


    final String URL = "jdbc:mysql://localhost:3306/cofetarie";
    final String USERNAME = "root";
    final String PASSWORD = "password123";

    String header[] = {"ID", "Tort", "Pret / 100g", "Cantitate in stoc"};

    private void createUIComponents() {
        // TODO: place custom component creation code here

        DefaultTableModel tableModel = new DefaultTableModel(0, 4);
        tableModel.setColumnIdentifiers(header);
        inventar = new JTable(tableModel);
        try{
            Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("select * from inventar");

            while(rs.next())
            {
                Object[] row = {rs.getInt("ID"),rs.getString("denumire"),rs.getFloat("pret"),rs.getInt("gramaj")};
                tableModel.addRow(row);
            }

        }catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        JFrame f = new JFrame("Inventar");
        f.setSize(200,300);
        f.add(new AdminInterface().panell);
        f.setVisible(true);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}
