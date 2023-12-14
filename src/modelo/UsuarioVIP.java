package modelo;

/**
 * Representa um usuário VIP no sistema, estendendo a classe Usuario com métodos adicionais.
 * 
 * @author Rubens e Wisla
 */
public class UsuarioVIP extends Usuario {

    /**
     * Construtor da classe UsuarioVIP.
     *
     * @param id    O identificador do usuário VIP.
     * @param nome  O nome do usuário VIP.
     * @param senha A senha do usuário VIP.
     */
    public UsuarioVIP(String id, String nome, String senha) {
        super(id, nome, senha);
    }

    /**
     * Obtém o diretório atual do sistema.
     *
     * @return O caminho para o diretório atual.
     */
    public String printarCaminho() {
        String diretorioAtual = System.getProperty("user.dir");
        return diretorioAtual;
    }

}
