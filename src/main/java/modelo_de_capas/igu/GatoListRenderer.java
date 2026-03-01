package modelo_de_capas.igu;

import modelo_de_capas.logica.Gato;
import javax.swing.*;
import java.awt.*;

public class GatoListRenderer extends JPanel implements ListCellRenderer<Gato> {

    private JLabel lblImg = new JLabel();
    private JLabel lblNombre = new JLabel();

    public GatoListRenderer() {
        setLayout(new BorderLayout(10, 10));
        lblNombre.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblImg.setPreferredSize(new Dimension(60, 60));
        add(lblImg, BorderLayout.WEST);
        add(lblNombre, BorderLayout.CENTER);
    }

    @Override
    public Component getListCellRendererComponent(
            JList<? extends Gato> list,
            Gato gato,
            int index,
            boolean isSelected,
            boolean cellHasFocus) {

        // Texto
        lblNombre.setText(gato.getNombre());

        // Imagen
        try {
            ImageIcon icon = new ImageIcon(gato.getFotoPath());
            Image scaled = icon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
            lblImg.setIcon(new ImageIcon(scaled));
        } catch (Exception e) {
            lblImg.setIcon(new ImageIcon("placeholder_gato.png"));
        }

        // Colores
        setBackground(isSelected ? Color.LIGHT_GRAY : Color.WHITE);
        return this;
    }
}
