package modelo_de_capas;
import modelo_de_capas.igu.MainFrame;
import modelo_de_capas.igu.MainFrame;
import modelo_de_capas.logica.Controladora;

public class ColoniaGatos {

    public static void main(String[] args) {

        // Crear controladora (esto conecta lógica + persistencia)
        Controladora control = new Controladora();
        
        // Lanzar la interfaz en el hilo gráfico
        java.awt.EventQueue.invokeLater(() -> {
            MainFrame main = new MainFrame(control);
            main.setVisible(true);
        });
    }
}
