package modelo;

/**
 * Representa um usuário no sistema comum, contendo um identificador, um nome e uma senha.
 * 
 * @author Rubens e Wisla
 */
public class Usuario {

    private String id;
    private String nome;
    private String senha;
   
    /**
     * Construtor da classe Usuario.
     *
     * @param id    O identificador do usuário.
     * @param nome  O nome do usuário.
     * @param senha A senha do usuário.
     */
    public Usuario(String id, String nome, String senha) {
        this.id = id;
        this.nome = nome;
        this.senha = senha;
    }

    /**
     * Construtor da classe Usuario.
     *
     * @param id    O identificador do usuário.
     * @param senha A senha do usuário.
     */
    public Usuario(String id, String senha) {
        this.id = id;
        this.senha = senha;
    }

    /**
     * Obtém o identificador do usuário.
     *
     * @return O identificador do usuário.
     */
    public String getId() {
        return id;
    }

    /**
     * Define o identificador do usuário.
     *
     * @param id O novo identificador do usuário.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Obtém o nome do usuário.
     *
     * @return O nome do usuário.
     */
    public String getNome() {
        return nome;
    }

    /**
     * Define o nome do usuário.
     *
     * @param nome O novo nome do usuário.
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Obtém a senha do usuário.
     *
     * @return A senha do usuário.
     */
    public String getSenha() {
        return senha;
    }

    /**
     * Define a senha do usuário.
     *
     * @param senha A nova senha do usuário.
     */
    public void setSenha(String senha) {
        this.senha = senha;
    }
	
    /**
     * Verifica se dois usuários são iguais com base no identificador (ignorando maiúsculas/minúsculas).
     *
     * @param obj O objeto a ser comparado.
     * @return true se os usuários forem iguais, false caso contrário.
     */
	@Override
	public boolean equals(Object obj) {
	    if (this == obj) {
	        return true;
	    }

	    if (obj == null || getClass() != obj.getClass()) {
	        return false;
	    }
	    
	    Usuario usuario = (Usuario) obj;
	    
    	String outro = usuario.id.toLowerCase();
    	String atual = id.toLowerCase();
    	
	    return atual.equals(outro);
	}



}
