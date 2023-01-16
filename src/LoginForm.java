import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.io.*;

public class LoginForm extends JDialog {
    private JTextField tfUsername;
    private JPasswordField tfPassword;
    private JButton btnLogin;
    private JButton btnCancel;
    private JPanel LoginPanel;

    public LoginForm(JFrame parent){
        super(parent);
        setTitle("Login");
        setContentPane(LoginPanel);
        setMinimumSize(new Dimension(450, 474));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        try {
            Image logo = ImageIO.read(new File("src/asset/cupcake.png"));
            setIconImage(logo);
        } catch (IOException e) {
            e.printStackTrace();
        }


        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = tfUsername.getText();
                String password = String.valueOf(tfPassword.getPassword());

                user = getAuthenticatedUser(username,password);

                if(user != null){
                    dispose();
                }
                else{
                    JOptionPane.showMessageDialog(LoginForm.this,"Username or password invalid","Try again",JOptionPane.ERROR_MESSAGE);

                }
            }
        });
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        setVisible(true);
    }

    public User user;

    static final String URL = "jdbc:mysql://localhost:3306/cofetarie";
    static final String USERNAME = "root";
    static final String PASSWORD = "password123";

    private User getAuthenticatedUser(String username, String password){
        User user = null;

        try {
            Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            Statement statement = connection.createStatement();
            String sql = "SELECT * FROM user WHERE username=? AND password=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){

                user = new User();
                user.username = resultSet.getString("username");
                user.password = resultSet.getString("password");
                user.checkAdmin = resultSet.getInt("checkAdmin");

            }

            statement.close();
            connection.close();

        }catch(Exception e){
            e.printStackTrace();
        }

        return user;
    }


    public static void main(String[] args){
        LoginForm loginForm = new LoginForm(null);
        User user = loginForm.user;
        if(user!=null && user.checkAdmin == 0){
            try {
                Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                Statement statement = connection.createStatement();
                statement.executeUpdate("DELETE FROM cos_cumparaturi");
            } catch (SQLException e1){
                e1.printStackTrace();
            }

            System.out.println("User Logged");
            JPanel Shop = new JPanel();
            Magazin magazin = new Magazin();
        }
        else {
            if (user!=null && user.checkAdmin == 1) {
                try {
                    Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                    Statement statement = connection.createStatement();
                    statement.executeUpdate("DELETE FROM cos_cumparaturi");
                } catch (SQLException e1){
                    e1.printStackTrace();
                }

                System.out.println("Admin Logged");
                JPanel adminIU = new JPanel();
                AdminInterface adminInterface = new AdminInterface();
            }
            else{
                System.out.println("Authentication canceled");
            }
        }
    }

}
