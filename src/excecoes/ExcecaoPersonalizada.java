package excecoes;

/**
 * Exceção personalizada que estende RuntimeException para representar situações específicas
 * da aplicação.
 */
public class ExcecaoPersonalizada extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
     * Construtor que cria uma nova ExcecaoPersonalizada com uma mensagem específica.
     *
     * @param message A mensagem de erro que descreve o que causou a exceção.               
     */
	public ExcecaoPersonalizada(String message) {
        super(message);
    }
}
