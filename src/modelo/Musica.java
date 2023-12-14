package modelo;


/**
 * Representa uma música no sistema.
 * 
 * @author Rubens e Wisla
 */
public class Musica {
	
	private String nome;
	private String caminhoArquivo;

	/**
     * Construtor padrão da classe Musica.
     */
	public Musica() {
		
	}
	
	/**
     * Construtor da classe Musica.
     *
     * @param nome           O nome da música.
     * @param caminhoArquivo O caminho do arquivo da música.
     */
    public Musica(String nome, String caminhoArquivo) {
        this.nome = nome;
        this.caminhoArquivo = caminhoArquivo;
    }

    /**
     * Obtém o nome da música.
     *
     * @return O nome da música.
     */
    public String getNome() {
        return nome;
    }

    /**
     * Define o nome da música.
     *
     * @param nome O novo nome da música.
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Obtém o caminho do arquivo da música.
     *
     * @return O caminho do arquivo da música.
     */
    public String getCaminhoArquivo() {
        return caminhoArquivo;
    }

    /**
     * Define o caminho do arquivo da música.
     *
     * @param caminhoArquivo O novo caminho do arquivo da música.
     */
    public void setCaminhoArquivo(String caminhoArquivo) {
        this.caminhoArquivo = caminhoArquivo;
    }


    /**
     * Verifica se duas músicas são iguais com base no nome e no caminho do arquivo.
     *
     * @param obj O objeto a ser comparado.
     * @return true se as músicas forem iguais, false caso contrário.
     */
	@Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Musica musica = (Musica) obj;

        return nome.equals(musica.nome) &&
               caminhoArquivo.equals(musica.caminhoArquivo);
    }
	
	/**
     * Retorna uma representação em string da música (apenas o nome).
     *
     * @return O nome da música em formato de string.
     */
	@Override
    public String toString() {
        return nome;
    }
	
}
