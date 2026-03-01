package modelo_de_capas.igu;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import modelo_de_capas.logica.Controladora;
import modelo_de_capas.logica.Gato;
import modelo_de_capas.logica.PuntoAvistamiento;

public class PerfilGatoDialog extends JDialog {

    private final Controladora control;
    private final Gato gato;

    private JLabel lblFoto;
    private JLabel lblNombre;
    private JLabel lblInfo;
    private JTable tablaAvistamientos;
    private DefaultTableModel modeloTabla;

    public PerfilGatoDialog(Frame parent, boolean modal, Controladora control, Gato gato) {
        super(parent, modal);
        this.control = control;
        this.gato = gato;
        initComponents();
        cargarDatosGato();
        cargarAvistamientos();
        setLocationRelativeTo(parent);
        setTitle("Perfil del gato");
    }

    private void initComponents() {
        setSize(1000, 650);
        setLayout(new BorderLayout());

        //---------- panel superior
        JPanel panelTop = new JPanel(new BorderLayout());
        panelTop.setBackground(new Color(200, 200, 200));
        panelTop.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        //---------- ftoto
        lblFoto = new JLabel();
        lblFoto.setPreferredSize(new Dimension(300, 300));
        lblFoto.setHorizontalAlignment(SwingConstants.CENTER);
        lblFoto.setVerticalAlignment(SwingConstants.CENTER);
        lblFoto.setOpaque(true);
        lblFoto.setBackground(new Color(230, 230, 230));

        //---------- info
        JPanel panelInfo = new JPanel();
        panelInfo.setLayout(new BoxLayout(panelInfo, BoxLayout.Y_AXIS));
        panelInfo.setBackground(new Color(200, 200, 200));
        panelInfo.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        lblNombre = new JLabel("Gato");
        lblNombre.setFont(new Font("SansSerif", Font.BOLD, 28));
        lblNombre.setAlignmentX(Component.LEFT_ALIGNMENT);

        lblInfo = new JLabel();
        lblInfo.setFont(new Font("SansSerif", Font.PLAIN, 18));
        lblInfo.setAlignmentX(Component.LEFT_ALIGNMENT);

        panelInfo.add(lblNombre);
        panelInfo.add(Box.createVerticalStrut(20));
        panelInfo.add(lblInfo);

        panelTop.add(lblFoto, BorderLayout.WEST);
        panelTop.add(panelInfo, BorderLayout.CENTER);

        add(panelTop, BorderLayout.NORTH);

        //---------- lista de avistamientos
        JPanel panelBottom = new JPanel(new BorderLayout());
        panelBottom.setBorder(BorderFactory.createEmptyBorder(15, 20, 20, 20));
        panelBottom.setBackground(new Color(220, 220, 220));

        JLabel lblTituloAv = new JLabel("Avistamientos:");
        lblTituloAv.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblTituloAv.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        panelBottom.add(lblTituloAv, BorderLayout.NORTH);

        modeloTabla = new DefaultTableModel(
                new Object[]{"Avistamiento", "Dirección", "Fecha", "Hora", "Estado de salud"},
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // solo lectura
            }
        };

        tablaAvistamientos = new JTable(modeloTabla);
        tablaAvistamientos.setRowHeight(22);

        JScrollPane scrollTabla = new JScrollPane(tablaAvistamientos);
        panelBottom.add(scrollTabla, BorderLayout.CENTER);

        add(panelBottom, BorderLayout.CENTER);
    }

    private void cargarDatosGato() {
        if (gato == null) return;

        lblNombre.setText(gato.getNombre() != null ? gato.getNombre() : "Gato");

        StringBuilder sb = new StringBuilder("<html>");
        sb.append("Color: ").append(gato.getColor() != null ? gato.getColor() : "Sin datos");
        sb.append("<br><br>");
        sb.append("Características: ").append(gato.getCaracteristicas() != null ? gato.getCaracteristicas() : "Sin datos");
        sb.append("</html>");

        lblInfo.setText(sb.toString());

        try {
            String ruta = gato.getFotoPath();
            if (ruta != null && !ruta.isEmpty()) {
                ImageIcon original = new ImageIcon(ruta);
                Image scaled = original.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);
                lblFoto.setIcon(new ImageIcon(scaled));
            } else {
                lblFoto.setIcon(new ImageIcon("placeholder_gato.png"));
            }
        } catch (Exception e) {
            lblFoto.setIcon(new ImageIcon("placeholder_gato.png"));
        }
    }

    private void cargarAvistamientos() {
        modeloTabla.setRowCount(0);

        List<PuntoAvistamiento> lista = control.listarAvistamientosPorGato(gato);
        if (lista == null || lista.isEmpty()) {
            return;
        }

        for (PuntoAvistamiento p : lista) {
            String avistamiento = p.getVoluntario() != null
                    ? "Por " + p.getVoluntario().getNombre()
                    : "Sin voluntario";
            String direccion = p.getCoordena() != null ? p.getCoordena() : "";
            String fecha = p.getFecha() != null ? p.getFecha().toString() : "";
            String hora = p.getHora() != null ? p.getHora().toString() : "";
            String estadoSalud = p.getEstadosalud() != null ? p.getEstadosalud() : "";

            modeloTabla.addRow(new Object[]{avistamiento, direccion, fecha, hora, estadoSalud});
        }
    }
}
