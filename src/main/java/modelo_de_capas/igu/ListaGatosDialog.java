package modelo_de_capas.igu;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.*;

import modelo_de_capas.logica.Controladora;
import modelo_de_capas.logica.Gato;

public class ListaGatosDialog extends JDialog {

    private final Controladora control;
    private JPanel panelLista;

    public ListaGatosDialog(Frame parent, boolean modal, Controladora control) {
        super(parent, modal);
        this.control = control;
        initComponents();
        cargarGatos();
        setLocationRelativeTo(parent);
        setTitle("Listado de gatos");
    }

    private void initComponents() {
        setSize(900, 600);
        setLayout(new BorderLayout());

        JLabel titulo = new JLabel("Gatos disponibles", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 24));
        titulo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(titulo, BorderLayout.NORTH);

        panelLista = new JPanel();
        panelLista.setLayout(new BoxLayout(panelLista, BoxLayout.Y_AXIS));
        panelLista.setBackground(new Color(240, 240, 240));

        JScrollPane scroll = new JScrollPane(panelLista);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(18);
        add(scroll, BorderLayout.CENTER);
    }

    private void cargarGatos() {
        panelLista.removeAll();

        List<Gato> gatos = control.listarGatos();
        if (gatos == null || gatos.isEmpty()) {
            JLabel lbl = new JLabel("No hay gatos registrados.");
            lbl.setFont(new Font("SansSerif", Font.PLAIN, 16));
            lbl.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            panelLista.add(lbl);
        } else {
            for (Gato g : gatos) {
                JPanel card = crearCardGato(g);
                panelLista.add(card);
                panelLista.add(Box.createVerticalStrut(15));
            }
        }

        panelLista.revalidate();
        panelLista.repaint();
    }

    private JPanel crearCardGato(Gato g) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(new Color(220, 220, 220));
        card.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Imagen mas chica
        JLabel lblImg = new JLabel();
        lblImg.setPreferredSize(new Dimension(150, 150));

        try {
            String ruta = g.getFotoPath();
            if (ruta != null && !ruta.isEmpty()) {
                ImageIcon original = new ImageIcon(ruta);
                Image scaled = original.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                lblImg.setIcon(new ImageIcon(scaled));
            } else {
                lblImg.setIcon(new ImageIcon("placeholder_gato.png"));
            }
        } catch (Exception e) {
            lblImg.setIcon(new ImageIcon("placeholder_gato.png"));
        }

        // Info basica
        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setBackground(new Color(235, 235, 235));
        info.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        Font fTitulo = new Font("SansSerif", Font.BOLD, 18);
        Font fTexto = new Font("SansSerif", Font.PLAIN, 14);

        JLabel lblNombre = new JLabel(g.getNombre() != null ? g.getNombre() : "Gato sin nombre");
        lblNombre.setFont(fTitulo);

        JLabel lblColor = new JLabel("Color: " + (g.getColor() != null ? g.getColor() : "Sin datos"));
        lblColor.setFont(fTexto);

        JLabel lblCaract = new JLabel("Características: " + (g.getCaracteristicas() != null ? g.getCaracteristicas() : ""));
        lblCaract.setFont(fTexto);

        info.add(lblNombre);
        info.add(Box.createVerticalStrut(5));
        info.add(lblColor);
        info.add(Box.createVerticalStrut(5));
        info.add(lblCaract);

        card.add(lblImg, BorderLayout.WEST);
        card.add(info, BorderLayout.CENTER);

        // Click en toda la tarjeta -> abre el perfil del gato
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                abrirPerfilGato(g);
            }
        });

        return card;
    }

    private void abrirPerfilGato(Gato g) {
        PerfilGatoDialog dialog = new PerfilGatoDialog((Frame) getParent(), true, control, g);
        dialog.setVisible(true);
    }
}
