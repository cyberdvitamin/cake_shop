import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.*;

public class PlaseazaComanda extends JFrame {
    private JPanel PlaseazaComanda;
    private JLabel lfPlata;

    float textPlata = Magazin.Total;


    public PlaseazaComanda() {
        setVisible(true);
        setTitle("Succes!");
        setContentPane(PlaseazaComanda);
        setMinimumSize(new Dimension(350, 100));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        try {
            Image logo = ImageIO.read(new File("src/asset/cupcake.png"));
            setIconImage(logo);
        } catch (IOException e) {
            e.printStackTrace();
        }

        setLfPlata();
    }

    public void setLfPlata(){
        this.lfPlata.setText("Total de plata: " + textPlata + " lei.");
    }


}
