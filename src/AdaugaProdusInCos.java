import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdaugaProdusInCos extends JFrame {
    private JTextField tfTort;
    private JTextField tfCantitate;
    private JButton btnAdaugaInterfata;
    private JPanel AdaugaInCos;

    public AdaugaProdusInCos() {
        setVisible(true);
        setTitle("Adauga Produs in Cos");
        setContentPane(AdaugaInCos);
        setMinimumSize(new Dimension(310, 300));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        btnAdaugaInterfata.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }
}
