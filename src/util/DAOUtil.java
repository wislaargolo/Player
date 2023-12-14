package util;

import java.io.File;
import java.io.IOException;

import modelo.Usuario;

/**
 * Classe utilitária para operações comuns de DAO (Data Access Object).
 */
public class DAOUtil {
	
	/**
     * Obtém o caminho do arquivo de um usuário específico.
     * 
     * @param usuario O objeto usuário a partir do qual o caminho será gerado.
     * @param tipo O tipo de arquivo ser acessado. Por exemplo, arquivo de diretórios ou músicas.
     * @return Uma string representando o caminho do arquivo do usuário.
     */
    public static String getCaminhoUsuario(Usuario usuario, String tipo) {
        return "dados/" + tipo + "/" + tipo + "_" + usuario.getId() + ".txt";
    }

    /**
     * Verifica se o caminho do arquivo existe no sistema de arquivos.
     * Se o arquivo não existir, ele é criado.
     * 
     * @param caminhoArquivo O caminho do arquivo a ser verificado.
     */
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
