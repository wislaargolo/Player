package modelo;


public class Usuario {

	private String id;
	private String nome;
	private String senha;
	
	public Usuario(String id, String nome, String senha) {
		this.id = id;
		this.nome = nome;
		this.senha = senha;
	}
	
	public Usuario(String id, String senha) {
		this.id = id;
		this.senha = senha;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}
	
	
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
    	
    	System.out.println(usuario.id);
    	System.out.println(id);

	    return atual.equals(outro);
	}



}
