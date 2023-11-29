package dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import excecoes.ExcecaoPersonalizada;
import modelo.Usuario;
import modelo.UsuarioVIP;

public class UsuarioDAO {

	private static String caminhoArquivo = "dados/usuarios.txt";
	
	private static void verificarCaminho(String caminhoArquivo) {
		File arquivo = new File(caminhoArquivo);

        if (!arquivo.exists()) {
            try {
				arquivo.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
	}

	public static ArrayList<Usuario> carregar() {
		ArrayList<Usuario> usuarios = new ArrayList<>();
		
		verificarCaminho(caminhoArquivo);
	
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
		return usuarios;
		
	}

    public static void adicionar(Usuario usuario) {
    	ArrayList<Usuario> usuarios = carregar();
    	
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
	        return ;
        }
        
        throw new ExcecaoPersonalizada("Erro ao cadastrar usuário: usuário já existe");
        
    }
	
    private static void removerArquivos(Usuario usuario) {
    	try {
    		File arquivoMusicas = new File(MusicaDAO.caminhoUsuario(usuario));
    		File arquivoDiretorios = new File(DiretorioDAO.caminhoUsuario(usuario));
    		
    		arquivoMusicas.delete();
    		arquivoDiretorios.delete();
    		
        } catch(Exception e) {
            
            e.printStackTrace();
        }     
    	
    	if(usuario instanceof UsuarioVIP) {
    		PlaylistDAO.removerTodasPlaylists((UsuarioVIP) usuario);
    	}
    }
    public static void remover(Usuario usuario) {	
    	ArrayList<Usuario> usuarios = carregar();
    	
        if(usuarios.contains(usuario)) {
        	usuarios.remove(usuario);
        	
        	removerArquivos(usuario);
        	
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
        	return ;
        }
        throw new ExcecaoPersonalizada("Erro ao remover usuário: usuário não existe existe");
    }
    
 

 

}
