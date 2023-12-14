package dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import modelo.Musica;
import modelo.Usuario;
import util.DAOUtil;

/**
 * 
 * A classe é resonsavel por acessar e modificar os dados dos .txs's refrentes aos diretorios de músicas
 * 
 * @author Rubens e Wisla
 * 
 */
public class DiretorioDAO {
	
	private static String tipo = "diretorios";
	
	/**
     * Carrega os diretórios associados a um usuário a partir do arquivo de configuração.
     * Também carrega as músicas contidas nesses diretórios.
     *
     * @param usuario O usuário para o qual carregar os diretórios.
     * @return Uma lista de caminhos de diretórios associados ao usuário.
     * @throws IOException Se ocorrer um erro durante a leitura do arquivo.
     */
	public static ArrayList<String> carregar(Usuario usuario) throws IOException {
		String caminhoArquivo = DAOUtil.getCaminhoUsuario(usuario, tipo);
		ArrayList<String> diretorios = new ArrayList<>();
		
		DAOUtil.verificarCaminho(caminhoArquivo);
		
		try (BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo))) {
            String linha;

            while ((linha = br.readLine()) != null) {
            	File diretorio = new File(linha);
            	if(diretorio.isDirectory()) {
	            	diretorios.add(linha);
	            	carregarMusicas(usuario,diretorio);
            	}
            }

        } catch (IOException e) {
        	throw e;
        }
		return diretorios;

	}
	
	/**
     * Carrega as músicas contidas em um diretório e as adiciona ao usuário.
     *
     * @param usuario  O usuário para o qual adicionar as músicas.
     * @param diretorio O diretório a ser processado.
     * @throws IOException Se ocorrer um erro durante a leitura do diretório.
     */
	public static void carregarMusicas(Usuario usuario, File diretorio) throws IOException {
        File[] listaArquivos = diretorio.listFiles();
        if (listaArquivos != null) {
            for (File arquivo : listaArquivos) {
                if (arquivo.isFile() && arquivo.getName().toLowerCase().endsWith(".mp3")) {
                	Musica nova = new Musica(arquivo.getName(), arquivo.getAbsolutePath());
                    MusicaDAO.adicionar(usuario, nova);
                }
            }
        }
	}
	
	/**
     * Remove as músicas associadas a um diretório do usuário.
     *
     * @param diretorio O diretório a ser processado.
     * @param usuario   O usuário para o qual remover as músicas.
     * @throws IOException Se ocorrer um erro durante a leitura do diretório ou remoção das músicas.
     */
	private static void removerMusicas(File diretorio, Usuario usuario) throws IOException {
        File[] listaArquivos = diretorio.listFiles();
        if (listaArquivos != null) {
            for (File arquivo : listaArquivos) {
                if (arquivo.isFile() && arquivo.getName().toLowerCase().endsWith(".mp3")) {
                	MusicaDAO.remover(usuario, new Musica(arquivo.getName(), arquivo.getAbsolutePath()));
                }
            }
        }
	}

	/**
     * Adiciona um novo diretório ao usuário e carrega as músicas contidas nele.
     *
     * @param usuario         O usuário para o qual adicionar o diretório.
     * @param caminhoDiretorio O caminho do diretório a ser adicionado.
     * @throws IOException Se ocorrer um erro durante a leitura do diretório ou gravação do arquivo.
     */
	public static void adicionar(Usuario usuario, String caminhoDiretorio) throws IOException {
		String caminhoArquivo = DAOUtil.getCaminhoUsuario(usuario, tipo);
		ArrayList<String> diretorios = carregar(usuario);
		
	    if (!diretorios.contains(caminhoDiretorio)) {
	        File diretorio = new File(caminhoDiretorio);
	        if (diretorio.isDirectory()) {
	            diretorios.add(caminhoDiretorio); 
	            carregarMusicas(usuario,diretorio); 

	            try (FileWriter fw = new FileWriter(caminhoArquivo, true)) {
	                fw.write(caminhoDiretorio);
	                fw.write(System.lineSeparator());
	            } catch (IOException e) {
	            	 throw e;
	            }
	        }
	    }
        	
		
	}
	
	/**
     * Remove um diretório associado ao usuário e as músicas contidas nele.
     *
     * @param caminhoDiretorio O caminho do diretório a ser removido.
     * @param usuario          O usuário para o qual remover o diretório.
     * @throws IOException Se ocorrer um erro durante a leitura do diretório, remoção das músicas ou gravação do arquivo.
     */
	public static void remover(String caminhoDiretorio, Usuario usuario) throws IOException {
		String caminhoArquivo = DAOUtil.getCaminhoUsuario(usuario, tipo);
		ArrayList<String> diretorios = carregar(usuario);
		
		if (diretorios.contains(caminhoDiretorio)) {
	        File diretorio = new File(caminhoDiretorio);
	        if (diretorio.isDirectory()) {
	            diretorios.remove(caminhoDiretorio); 
	            removerMusicas(diretorio, usuario); 

	            try (FileWriter fw = new FileWriter(caminhoArquivo, false)) {
	            	for (String d : diretorios) {
						fw.write(d);
						fw.write(System.lineSeparator());
	                }
	            } catch (IOException e) {
	            	throw e;
	            }
	        }
	    }
        	
		
	}	


}
