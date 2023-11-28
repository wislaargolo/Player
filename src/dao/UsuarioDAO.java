package dao;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.NoSuchElementException;

import modelo.Usuario;
import modelo.UsuarioVIP;

public class UsuarioDAO {

	private ArrayList<Usuario> usuarios;
	private String caminhoArquivo;
	private static UsuarioDAO usuarioDAO;

	public UsuarioDAO() {
		String diretorioAtual = System.getProperty("user.dir");
		this.caminhoArquivo = diretorioAtual + "/dados/usuarios.txt";
		usuarios = new ArrayList<Usuario>();
	}

	public void carregar() {
	
        try (BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo))){	
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] partes = linha.split(",");
                if (partes.length == 4) {
                    String nome = partes[0];
                    String id = partes[1];
                    String senha = partes[2];
                    Usuario aux;
                    if (partes[3].equalsIgnoreCase("VIP")) {
                        aux = new UsuarioVIP(id, nome, senha);
                    } else {
                        aux = new Usuario(id, nome, senha);
                    }
                    usuarios.add(aux);
                   
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
		
	}

    public void adicionar(Usuario usuario) {
        if (!usuarios.contains(usuario)) {
	        usuarios.add(usuario);
	
	        try (FileWriter fw = new FileWriter(caminhoArquivo, true)){
	            String conteudo = usuario.getNome() + "," + usuario.getId() + "," 
	            				  + usuario.getSenha() + ",";
	            
	            if(usuario instanceof UsuarioVIP) {
	            	conteudo = conteudo + "VIP";
	            } else {
	            	conteudo = conteudo + "COMUM";
	            }
	            
	            fw.write(conteudo);
	            fw.write(System.lineSeparator());
	            
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
        }
        
    }
	
    public void remover(Usuario usuario) {		
        if(usuarios.contains(usuario)) {
        	usuarios.remove(usuario);
        	
        	try (FileWriter fw = new FileWriter(caminhoArquivo, false)){
        		
        		for (Usuario u : usuarios) {
                    String conteudo = u.getNome() + "," + u.getId() + "," + u.getSenha() + ",";
                    
                    if(usuario instanceof UsuarioVIP) {
    	            	conteudo = conteudo + "VIP";
    	            } else {
    	            	conteudo = conteudo + "COMUM";
    	            }
                    
                    fw.write(conteudo);
                    fw.write(System.lineSeparator());
                }
        		
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
    }
    
    public void autenticar(String login, String senha){
        for(Usuario usuario : usuarios){
            if(usuario.equals(new Usuario(login, senha))){
              return ;
            }
        }
        throw new NoSuchElementException("Argumentos de login inválidos");
        
    }

    
	public ArrayList<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(ArrayList<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public String getCaminhoArquivo() {
		return caminhoArquivo;
	}

	public void setCaminhoArquivo(String caminhoArquivo) {
		this.caminhoArquivo = caminhoArquivo;
	}
    public static UsuarioDAO getInstance(){
        if(usuarioDAO == null){
            usuarioDAO = new UsuarioDAO();
        }
        return usuarioDAO;
    }
    
    

}
