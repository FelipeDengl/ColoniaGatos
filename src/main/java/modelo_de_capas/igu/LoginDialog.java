package modelo_de_capas.igu;

import javax.swing.*;
import java.awt.*;
import modelo_de_capas.logica.Controladora;

public class LoginDialog extends JDialog {

    private final Controladora control;
    private JTextField txtUsuarioCorreo;
    private JPasswordField txtPassword;
    private JLabel lblError;

    public LoginDialog(JFrame parent, Controladora control) {
        super(parent, "Iniciar sesión", true);
        this.control = control;

        setSize(500, 400);
        setLocationRelativeTo(parent);
        setLayout(new GridBagLayout()); // para centrar la tarjeta

        JPanel card = new JPanel();
        card.setBackground(new Color(245, 245, 245));
        card.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JLabel lblTitulo = new JLabel("Iniciar Sesión");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 26));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblUsuarioCorreo = new JLabel("Correo electrónico o Usuario");
        JLabel lblPass = new JLabel("Contraseña");

        txtUsuarioCorreo = new JTextField(20);
        txtPassword = new JPasswordField(20);

        txtUsuarioCorreo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        txtPassword.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        JButton btnLogin = new JButton("Iniciar sesión");
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogin.setMaximumSize(new Dimension(200, 35));

        lblError = new JLabel(" ");
        lblError.setForeground(Color.RED);
        lblError.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(lblTitulo);
        card.add(Box.createVerticalStrut(20));
        card.add(lblUsuarioCorreo);
        card.add(txtUsuarioCorreo);
        card.add(Box.createVerticalStrut(10));
        card.add(lblPass);
        card.add(txtPassword);
        card.add(Box.createVerticalStrut(20));
        card.add(btnLogin);
        card.add(Box.createVerticalStrut(10));
        card.add(lblError);

        add(card);

        // listener
        btnLogin.addActionListener(e -> intentarLogin());

        // enter también loguea
        txtPassword.addActionListener(e -> intentarLogin());
    }

    private void intentarLogin() {
        String userOrEmail = txtUsuarioCorreo.getText().trim();
        String pass = new String(txtPassword.getPassword());

        if (userOrEmail.isEmpty() || pass.isEmpty()) {
            lblError.setText("Complete usuario/correo y contraseña.");
            return;
        }

        boolean ok = control.login(userOrEmail, pass);

        if (ok) {
            if (getParent() instanceof MainFrame mf) {
                mf.onLoginSuccess();
            }
            dispose();
        } else {
            lblError.setText("Usuario o contraseña incorrectos.");
        }
    }
}
