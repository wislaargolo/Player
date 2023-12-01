package excecoes;

public class ExcecaoPersonalizada extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ExcecaoPersonalizada(String message) {
        super(message);
    }
}
