import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class PlaseazaComanda extends JFrame {
    private JPanel PlaseazaComanda;
    private JLabel lfPlata;

    float textPlata;


    public PlaseazaComanda() {
        setVisible(true);
        setTitle("Succes!");
        setContentPane(PlaseazaComanda);
        setMinimumSize(new Dimension(350, 100));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLfPlata();
    }

    public void setLfPlata(){
        this.lfPlata.setText("Total de plata: " + textPlata + " lei.");
    }


}
