package modelo_de_capas.logica;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ImageUtils {

    public static ImageIcon loadImageScaledIcon(String path, int w, int h) {
        try {
            File imageFile = resolveImageFile(path);
            if (imageFile == null) {
                return loadResourceScaledIcon("/placeholder_gato.png", w, h);
            }

            BufferedImage img = ImageIO.read(imageFile);
            if (img == null) {
                return loadResourceScaledIcon("/placeholder_gato.png", w, h);
            }

            Image scaled = img.getScaledInstance(w, h, Image.SCALE_SMOOTH);
            return new ImageIcon(scaled);
        } catch (IOException ex) {
            return loadResourceScaledIcon("/placeholder_gato.png", w, h);
        }
    }

    private static File resolveImageFile(String path) {
        if (path == null || path.isBlank()) {
            return null;
        }

        Path imagePath = Paths.get(path);
        if (imagePath.isAbsolute() && Files.isRegularFile(imagePath)) {
            return imagePath.toFile();
        }

        Path currentDir = Paths.get(System.getProperty("user.dir"));
        Path candidate = currentDir.resolve(imagePath).normalize();
        if (Files.isRegularFile(candidate)) {
            return candidate.toFile();
        }

        Path cursor = currentDir;
        for (int i = 0; i < 4 && cursor != null; i++) {
            candidate = cursor.resolve(imagePath).normalize();
            if (Files.isRegularFile(candidate)) {
                return candidate.toFile();
            }
            cursor = cursor.getParent();
        }

        return null;
    }

    public static ImageIcon loadResourceScaledIcon(String resourcePath, int w, int h) {
        try {
            URL u = ImageUtils.class.getResource(resourcePath);
            if (u == null) return new ImageIcon(new BufferedImage(w,h,BufferedImage.TYPE_INT_ARGB));
            BufferedImage img = ImageIO.read(u);
            Image scaled = img.getScaledInstance(w,h,Image.SCALE_SMOOTH);
            return new ImageIcon(scaled);
        } catch (IOException ex) {
            return new ImageIcon(new BufferedImage(w,h,BufferedImage.TYPE_INT_ARGB));
        }
    }

    public static ImageIcon scaleIcon(ImageIcon icon, int w, int h) {
        Image img = icon.getImage().getScaledInstance(w,h,Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }
    
    public static ImageIcon loadResourceScaled(String resourcePath, int width, int height) {
    try {
        URL url = ImageUtils.class.getResource(resourcePath);
        if (url == null) {
            System.out.println("⚠ No se encontró la imagen en: " + resourcePath);
            return null;
        }

        ImageIcon icon = new ImageIcon(url);
        Image img = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(img);

    } catch (Exception e) {
        System.out.println("❌ Error cargando imagen: " + resourcePath + " -> " + e.getMessage());
        return null;
    }
}
}
