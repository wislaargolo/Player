package dao;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import modelo.Musica;
import modelo.Usuario;
import modelo.UsuarioVIP;
import util.DAOUtil;

/**
 * A classe é responsável por acessar e modificar os dados dos .txs's referentes às músicas associadas aos usuários.
 * 
 * @author Rubens e Wisla
 */
public class MusicaDAO {
	
	private static String tipo = "musicas";

	/**
     * Carrega as músicas associadas a um usuário a partir do arquivo de configuração.
     *
     * @param usuario O usuário para o qual carregar as músicas.
     * @return Uma lista de músicas associadas ao usuário.
     * @throws IOException Se ocorrer um erro durante a leitura do arquivo.
     */
	public static ArrayList<Musica> carregar(Usuario usuario) throws IOException {
		String caminhoArquivo = DAOUtil.getCaminhoUsuario(usuario, tipo);
		ArrayList<Musica> musicas = new ArrayList<Musica>();
		
		DAOUtil.verificarCaminho(caminhoArquivo);
		
		try (BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo))) {
            String linha;

            while ((linha = br.readLine()) != null) {
            	String[] partes = linha.split(",");
                String nome = partes[0];
                String caminho = partes[1];
                musicas.add(new Musica(nome, caminho));
            }
        } catch (IOException e) {
        	throw e;
        }
		return musicas;
	}
	
	/**
     * Adiciona uma nova música ao usuário e grava no arquivo de configuração.
     *
     * @param usuario O usuário para o qual adicionar a música.
     * @param musica A música a ser adicionada.
     * @throws IOException Se ocorrer um erro durante a gravação no arquivo.
     */
	public static void adicionar(Usuario usuario, Musica musica) throws IOException {
		String caminhoArquivo = DAOUtil.getCaminhoUsuario(usuario, tipo);
		ArrayList<Musica> musicas = carregar(usuario);
		
		if (!musicas.contains(musica)) {
			musicas.add(musica);		
	
	        try (FileWriter fw = new FileWriter(caminhoArquivo, true)){
	            String conteudo = musica.getNome() + "," + musica.getCaminhoArquivo();
	            
	            fw.write(conteudo);
	            fw.write(System.lineSeparator());
	            
	        } catch (IOException e) {
	        	throw e;
	        }
        }
	}
	
	/**
     * Remove uma música do usuário e atualiza o arquivo de configuração.
     *
     * @param usuario O usuário para o qual remover a música.
     * @param musica A música a ser removida.
     * @throws IOException Se ocorrer um erro durante a gravação no arquivo.
     */
	public static void remover(Usuario usuario, Musica musica) throws IOException {
		String caminhoArquivo = DAOUtil.getCaminhoUsuario(usuario, tipo);
		ArrayList<Musica> musicas = carregar(usuario);
		if(musicas.contains(musica)) {
			musicas.remove(musica);
        	
        	try (FileWriter fw = new FileWriter(caminhoArquivo, false)){
        		
        		for (Musica m : musicas) {
                    String conteudo = m.getNome() + "," + m.getCaminhoArquivo();
                    
                    fw.write(conteudo);
                    fw.write(System.lineSeparator());
                }
        		
        		
        		if(usuario instanceof UsuarioVIP) {
        			PlaylistDAO.removerMusica(musica,(UsuarioVIP) usuario);
        			
        		}
        		
			} catch (IOException e) {
				throw e;
			}
        }
		
	}
	

}
