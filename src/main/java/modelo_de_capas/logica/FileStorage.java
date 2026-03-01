package modelo_de_capas.logica;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

public class FileStorage {

    private static final String UPLOAD_DIR = "uploads/gatos"; // carpeta donde se guardan las fotos

    public static String saveImage(File sourceFile) throws IOException {

        // crear la carpeta si no existe
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // mantener la extensión original
        String extension = "";
        String fileName = sourceFile.getName();
        int i = fileName.lastIndexOf(".");
        if (i >= 0) {
            extension = fileName.substring(i);
        }

        // generar nombre único
        String newName = UUID.randomUUID().toString() + extension;
        Path target = uploadPath.resolve(newName);

        // copiar archivo
        Files.copy(sourceFile.toPath(), target, StandardCopyOption.REPLACE_EXISTING);

        return target.toString(); // devolvemos la ruta completa (o relativa)
    }
}
