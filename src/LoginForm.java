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
    private User getAuthenticatedUser(String username, String password){
        User user = null;

        final String URL = "jdbc:mysql://localhost:3306/cofetarie";
        final String USERNAME = "root";
        final String PASSWORD = "password123";

        try {
            Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM utilizator WHERE username=? AND password=?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){

                user = new User();
                user.username = resultSet.getString("username");
                user.password = resultSet.getString("password");

            }

            stmt.close();
            conn.close();

        }catch(Exception e){
            e.printStackTrace();
        }

        return user;
    }

    public static void main(String[] args){
        LoginForm loginForm = new LoginForm(null);
        User user = loginForm.user;
        if(user!=null){
            JPanel Shop = new JPanel();
            Magazin magazin = new Magazin();
        }
        else{
            System.out.println("Authentication canceled");
        }
    }

}
