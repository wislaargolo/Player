package modelo;

public class UsuarioVIP extends Usuario {


	public UsuarioVIP(String id, String nome, String senha) {
		super(id, nome, senha);
	}
	
	public String printarCaminho() {
		String diretorioAtual = System.getProperty("user.dir");
		return diretorioAtual;
	}

}
