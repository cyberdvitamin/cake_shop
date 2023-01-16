import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.*;

public class AdaugaProdusInCos extends JFrame {
    private JTextField tfTort;
    private JTextField tfCantitate;
    private JButton btnAdauga;
    private JPanel AdaugaInCos;

    static float pret = 0;
    static String denumire_tort;
    static double cantitate = 0;
    boolean correctID = false;

    public AdaugaProdusInCos() {
        setVisible(true);
        setTitle("Adauga Produs in Cos");
        setContentPane(AdaugaInCos);
        setMinimumSize(new Dimension(310, 300));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);


        try {
            Image logo = ImageIO.read(new File("src/asset/cupcake.png"));
            setIconImage(logo);
        } catch (IOException e) {
            e.printStackTrace();
        }

        final String URL = "jdbc:mysql://localhost:3306/cofetarie";
        final String USERNAME = "root";
        final String PASSWORD = "password123";

        btnAdauga.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Connection connection1 = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                    Statement statement = connection1.createStatement();

                    ResultSet rs = statement.executeQuery("select * from inventar");

                    while(rs.next())
                    {
                        if (Integer.valueOf(rs.getInt("ID")) == Integer.valueOf(tfTort.getText()))
                        {
                            pret = Float.valueOf(rs.getFloat("pret"));
                            pret = pret * (Float.parseFloat(tfCantitate.getText()) / 100) ;

                            denumire_tort = String.valueOf(rs.getString("denumire"));

                            correctID = true;

                            break;
                        }
                    }

                    statement.close();
                    connection1.close();

                } catch (SQLException e1){
                    e1.printStackTrace();
                }


                if (correctID == true) {
                    try {
                        Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

                        String query = "INSERT INTO cos_cumparaturi (articol, pret, gramaj) VALUES (?, ?, ?)";

                        cantitate = Double.parseDouble(tfCantitate.getText());

                        PreparedStatement preparedStatement = connection.prepareStatement(query);
                        preparedStatement.setString(1, String.valueOf(denumire_tort));
                        preparedStatement.setFloat(2, pret);
                        preparedStatement.setDouble(3, cantitate);
                        preparedStatement.executeUpdate();

                        Magazin.Total = Magazin.Total + pret;

                        connection.close();

                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }

                    dispose();
                }
                else
                {
                    JOptionPane optionPane = new JOptionPane("Ai introdus un ID inexistent!", JOptionPane.INFORMATION_MESSAGE);
                    JDialog dialog = optionPane.createDialog("ERROR");
                    dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                    dialog.setVisible(true);
                    dialog.dispose();
                }
            }
        });
    }


}
