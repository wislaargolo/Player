package util;

import java.io.File;
import java.io.IOException;

import modelo.Usuario;

public class DAOUtil {

    public static String getCaminhoUsuario(Usuario usuario, String tipo) {
        return "dados/" + tipo + "/" + tipo + "_" + usuario.getId() + ".txt";
    }

    public static void verificarCaminho(String caminhoArquivo) {
        File arquivo = new File(caminhoArquivo);
        if (!arquivo.exists()) {
            try {
                arquivo.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
