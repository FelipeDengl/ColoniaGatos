package modelo_de_capas.igu;

import java.awt.Component;
import java.awt.event.ActionEvent;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import modelo_de_capas.logica.Controladora;
import modelo_de_capas.logica.Usuario;
import modelo_de_capas.logica.Voluntario;
import modelo_de_capas.logica.Familia;
import modelo_de_capas.logica.Veterinario;

public class RegisterDialog extends javax.swing.JDialog {

    private Controladora control;

    public RegisterDialog(java.awt.Frame parent, boolean modal, Controladora control) {
        super(parent, modal);
        this.control = control;
        initComponents();
        cargarTipos();
        actualizarCamposSegunRol();
        setLocationRelativeTo(null);
        setTitle("Registro de usuario");
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        lblUsuario = new javax.swing.JLabel("Usuario:");
        lblPass = new javax.swing.JLabel("Contraseña:");
        lblPass2 = new javax.swing.JLabel("Repetir contraseña:");
        lblGmail = new javax.swing.JLabel("Gmail:");
        lblTipo = new javax.swing.JLabel("Tipo:");
        lblNombre = new javax.swing.JLabel("Nombre:");
        lblDni = new javax.swing.JLabel("DNI:");
        lblTelefono = new javax.swing.JLabel("Teléfono:");
        lblDireccion = new javax.swing.JLabel("Dirección:");
        lblIntegrantes = new javax.swing.JLabel("Integrantes:");
        lblResponsable = new javax.swing.JLabel("Responsable:");
        lblReputacion = new javax.swing.JLabel("Reputación:");

        txtUsuario = new javax.swing.JTextField();
        txtPass = new javax.swing.JPasswordField();
        txtPass2 = new javax.swing.JPasswordField();
        txtGmail = new javax.swing.JTextField();
        txtNombre = new javax.swing.JTextField();
        txtDni = new javax.swing.JTextField();
        txtTelefono = new javax.swing.JTextField();
        txtDireccion = new javax.swing.JTextField();
        txtIntegrantes = new javax.swing.JTextField();
        txtResponsable = new javax.swing.JTextField();
        txtReputacion = new javax.swing.JTextField();

        cmbTipoUsuario = new javax.swing.JComboBox<>();
        cmbTipoUsuario.addActionListener((e) -> actualizarCamposSegunRol());

        btnRegistrar = new javax.swing.JButton("Registrar");
        btnCancelar = new javax.swing.JButton("Cancelar");

        btnRegistrar.addActionListener((ActionEvent evt) -> btnRegistrarActionPerformed(evt));
        btnCancelar.addActionListener((ActionEvent evt) -> dispose());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);

        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup().addGap(20)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(lblUsuario)
                        .addComponent(txtUsuario)
                        .addComponent(lblPass)
                        .addComponent(txtPass)
                        .addComponent(lblPass2)
                        .addComponent(txtPass2)
                        .addComponent(lblGmail)
                        .addComponent(txtGmail)
                        .addComponent(lblTipo)
                        .addComponent(cmbTipoUsuario)
                        .addComponent(lblNombre)
                        .addComponent(txtNombre)
                        .addComponent(lblDni)
                        .addComponent(txtDni)
                        .addComponent(lblTelefono)
                        .addComponent(txtTelefono)
                        .addComponent(lblDireccion)
                        .addComponent(txtDireccion)
                        .addComponent(lblIntegrantes)
                        .addComponent(txtIntegrantes)
                        .addComponent(lblResponsable)
                        .addComponent(txtResponsable)
                        .addComponent(lblReputacion)
                        .addComponent(txtReputacion)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(btnRegistrar, 120, 120, 120)
                            .addGap(20)
                            .addComponent(btnCancelar, 120, 120, 120))
                    ).addGap(20))
        );

        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addGap(15)
                .addComponent(lblUsuario)
                .addComponent(txtUsuario, 28, 28, 28)
                .addGap(10)
                .addComponent(lblPass)
                .addComponent(txtPass, 28, 28, 28)
                .addGap(10)
                .addComponent(lblPass2)
                .addComponent(txtPass2, 28, 28, 28)
                .addGap(10)
                .addComponent(lblGmail)
                .addComponent(txtGmail, 28, 28, 28)
                .addGap(10)
                .addComponent(lblTipo)
                .addComponent(cmbTipoUsuario, 28, 28, 28)
                .addGap(15)
                .addComponent(lblNombre)
                .addComponent(txtNombre, 28, 28, 28)
                .addGap(10)
                .addComponent(lblDni)
                .addComponent(txtDni, 28, 28, 28)
                .addGap(10)
                .addComponent(lblTelefono)
                .addComponent(txtTelefono, 28, 28, 28)
                .addGap(10)
                .addComponent(lblDireccion)
                .addComponent(txtDireccion, 28, 28, 28)
                .addGap(10)
                .addComponent(lblIntegrantes)
                .addComponent(txtIntegrantes, 28, 28, 28)
                .addGap(10)
                .addComponent(lblResponsable)
                .addComponent(txtResponsable, 28, 28, 28)
                .addGap(10)
                .addComponent(lblReputacion)
                .addComponent(txtReputacion, 28, 28, 28)
                .addGap(20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnRegistrar, 30, 30, 30)
                    .addComponent(btnCancelar, 30, 30, 30))
                .addGap(20)
        );

        pack();
    }

    private void cargarTipos() {
        String[] tipos = {"Voluntario", "Familia", "Veterinario"};
        cmbTipoUsuario.setModel(new DefaultComboBoxModel<>(tipos));
    }

    private void actualizarCamposSegunRol() {
        String tipo = (String) cmbTipoUsuario.getSelectedItem();
        if (tipo == null) return;

        setDynamicVisible(false, false, false, false, false, false, false);

        switch (tipo) {
            case "Voluntario" ->
                setDynamicVisible(true, true, false, false, false, false, true);

            case "Familia" ->
                setDynamicVisible(true, false, true, true, true, true, true);

            case "Veterinario" ->
                setDynamicVisible(true, true, true, false, false, false, false);
        }
    }

    private void setDynamicVisible(
            boolean nombre, boolean dni, boolean telefono,
            boolean direccion, boolean integrantes,
            boolean responsable, boolean reputacion
    ) {
        lblNombre.setVisible(nombre); txtNombre.setVisible(nombre);
        lblDni.setVisible(dni); txtDni.setVisible(dni);
        lblTelefono.setVisible(telefono); txtTelefono.setVisible(telefono);
        lblDireccion.setVisible(direccion); txtDireccion.setVisible(direccion);
        lblIntegrantes.setVisible(integrantes); txtIntegrantes.setVisible(integrantes);
        lblResponsable.setVisible(responsable); txtResponsable.setVisible(responsable);
        lblReputacion.setVisible(reputacion); txtReputacion.setVisible(reputacion);
    }

    private void btnRegistrarActionPerformed(ActionEvent evt) {

        String username = txtUsuario.getText().trim();
        String pass1 = new String(txtPass.getPassword()).trim();
        String pass2 = new String(txtPass2.getPassword()).trim();
        String gmail = txtGmail.getText().trim();
        String tipo = (String) cmbTipoUsuario.getSelectedItem();

        if (username.isEmpty() || pass1.isEmpty() || pass2.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Complete todos los campos obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!pass1.equals(pass2)) {
            JOptionPane.showMessageDialog(this, "Las contraseñas no coinciden", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (control.existeUsuario(username)) {
            JOptionPane.showMessageDialog(this, "Ese usuario ya existe", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Usuario u = new Usuario();
        u.setUsername(username);
        u.setPassword(pass1);
        u.setGmail(gmail.isEmpty() ? null : gmail);

        control.crearUsuario(u);

        switch (tipo) {
            case "Voluntario" -> {
                Voluntario v = new Voluntario();
                v.setUsuario(u);
                v.setNombre(txtNombre.getText().trim());
                v.setDni(txtDni.getText().trim());
                v.setReputacion(parseDouble(txtReputacion.getText().trim()));
                v.setUsername(username);
                v.setPassword(pass1);
                control.crearVoluntario(v);
            }
            case "Familia" -> {
                Familia f = new Familia();
                f.setUsuario(u);
                f.setNombre(txtNombre.getText().trim());
                f.setNumeroTelefono(txtTelefono.getText().trim());
                f.setCantIntegrantes(parseInt(txtIntegrantes.getText().trim()));
                f.setDireccion(txtDireccion.getText().trim());
                f.setResponsable(txtResponsable.getText().trim());
                f.setReputacion(parseDouble(txtReputacion.getText().trim()));
                f.setUsername(username);
                f.setPassword(pass1);
                control.crearFamilia(f);
            }
            case "Veterinario" -> {
                Veterinario vt = new Veterinario();
                vt.setUsuario(u);
                vt.setNombre(txtNombre.getText().trim());
                vt.setNumeroTelefono(txtTelefono.getText().trim());
                vt.setDni(txtDni.getText().trim());
                control.crearVeterinario(vt);
            }
        }

        JOptionPane.showMessageDialog(this, "Usuario registrado!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }

    private Double parseDouble(String s) {
        try { return Double.parseDouble(s); } catch (Exception e) { return 0.0; }
    }

    private Integer parseInt(String s) {
        try { return Integer.parseInt(s); } catch (Exception e) { return 0; }
    }

    private javax.swing.JLabel lblUsuario, lblPass, lblPass2, lblGmail, lblTipo;
    private javax.swing.JLabel lblNombre, lblDni, lblTelefono, lblDireccion, lblIntegrantes, lblResponsable, lblReputacion;

    private javax.swing.JTextField txtUsuario, txtGmail, txtNombre, txtDni, txtTelefono, txtDireccion, txtIntegrantes, txtResponsable, txtReputacion;
    private javax.swing.JPasswordField txtPass, txtPass2;

    private javax.swing.JComboBox<String> cmbTipoUsuario;
    private javax.swing.JButton btnRegistrar, btnCancelar;
}
