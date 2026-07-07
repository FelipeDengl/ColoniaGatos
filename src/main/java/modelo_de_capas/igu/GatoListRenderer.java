package modelo_de_capas.igu;

import modelo_de_capas.logica.Gato;
import modelo_de_capas.logica.ImageUtils;
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

        lblImg.setIcon(ImageUtils.loadImageScaledIcon(gato.getFotoPath(), 60, 60));

        // Colores
        setBackground(isSelected ? Color.LIGHT_GRAY : Color.WHITE);
        return this;
    }
}
