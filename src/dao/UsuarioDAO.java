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
import util.DAOUtil;

/**
 * A classe é responsável por acessar e modificar os dados dos usuários no sistema.
 * 
 * @author Rubens e Wisla
 */
public class UsuarioDAO {

	private static String caminhoArquivo = "dados/usuarios.txt";

	public static ArrayList<Usuario> carregar() throws IOException {
		ArrayList<Usuario> usuarios = new ArrayList<>();
		
		DAOUtil.verificarCaminho(caminhoArquivo);
	
		/**
	     * Carrega a lista de usuários a partir do arquivo de configuração.
	     *
	     * @return Uma lista de usuários.
	     * @throws IOException Se ocorrer um erro durante a leitura do arquivo.
	     */
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
            throw e;
        }
		return usuarios;
		
	}

	/**
     * Adiciona um novo usuário ao sistema e atualiza o arquivo de configuração.
     *
     * @param usuario O usuário a ser adicionado.
     * @throws IOException Se ocorrer um erro durante a gravação no arquivo.
     * @throws ExcecaoPersonalizada Se o usuário já existir no sistema.
     */
    public static void adicionar(Usuario usuario) throws IOException {
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
	        	throw e;
	        }
	        return ;
        }
        
        throw new ExcecaoPersonalizada("Erro ao cadastrar usuário: usuário já existe");
        
    }
	
    /**
     * Remove arquivos associados a um usuário, como arquivos de músicas e playlists, ao ser removido do sistema.
     *
     * @param usuario O usuário a ser removido.
     * @throws IOException Se ocorrer um erro durante a gravação no arquivo.
     * @throws ExcecaoPersonalizada Se o usuário não existir no sistema.
     */
    private static void removerArquivos(Usuario usuario) throws IOException {
    	try {
    		File arquivoMusicas = new File(DAOUtil.getCaminhoUsuario(usuario, "musicas"));
    		File arquivoDiretorios = new File(DAOUtil.getCaminhoUsuario(usuario, "diretorios"));
    		
    		arquivoMusicas.delete();
    		arquivoDiretorios.delete();
    		
        } catch(Exception e) {
            
            e.printStackTrace();
        }     
    	
    	if(usuario instanceof UsuarioVIP) {
    		PlaylistDAO.removerTodasPlaylists((UsuarioVIP) usuario);
    	}
    }
    
    /**
     * Remove um usuário do sistema e atualiza o arquivo de configuração, além de remover arquivos associados.
     *
     * @param usuario O usuário para o qual remover os arquivos associados.
     * @throws IOException Se ocorrer um erro durante a remoção dos arquivos.
     */
    public static void remover(Usuario usuario) throws IOException {	
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
				throw e;
			}
        	return ;
        }
        throw new ExcecaoPersonalizada("Erro ao remover usuário: usuário não existe existe");
    }

    
 

 

}
