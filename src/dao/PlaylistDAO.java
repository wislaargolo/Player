package dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import modelo.Musica;
import modelo.Playlist;
import modelo.UsuarioVIP;
import util.DAOUtil;

/**
 * A classe é responsável por acessar e modificar os dados das playlists associadas aos usuários VIP.
 * 
 * @author Rubens e Wisla
 */
public class PlaylistDAO {
	private static String caminhoArquivo = "dados/playlists/playlistsGeral.txt";

	private PlaylistDAO() {
	}
	
	
	/**
     * Carrega as playlists associadas a um usuário VIP a partir do arquivo de configuração.
     *
     * @param usuario O usuário VIP para o qual carregar as playlists.
     * @return Uma lista de playlists associadas ao usuário VIP.
     * @throws IOException Se ocorrer um erro durante a leitura do arquivo.
     */
	public static ArrayList<Playlist> carregar(UsuarioVIP usuario) throws IOException {
		ArrayList<Playlist> playlists = new ArrayList<Playlist>();
		ArrayList<String> nomes = new ArrayList<>();
		ArrayList<String> caminhos = new ArrayList<>();
		
		DAOUtil.verificarCaminho(caminhoArquivo);
		
		// Le o arquivo com todas as playlists
		try (BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo))) {
			String linha;

			while ((linha = br.readLine()) != null) {
				String[] partes = linha.split(",");
				nomes.add(partes[0]);
				caminhos.add(partes[1]);
			}
		} catch (IOException e) {
			throw e;
		}

		// abre arquivo por arquivo e testa se pertence ao úsuario
		
		for (int i = 0; i < nomes.size(); i++) {
			ArrayList<String> playlistAtual = new ArrayList<>();
			ArrayList<Musica> musicas = new ArrayList<>();
			try (BufferedReader br = new BufferedReader(new FileReader(caminhos.get(i)))) {
				String linha;
				Stream<String> partes = null;
				while ((linha = br.readLine()) != null) {
					partes = Arrays.stream(linha.split(","));
					partes.forEach(playlistAtual::add);

				}

				if (playlistAtual.get(0).equals(usuario.getNome()) && playlistAtual.get(1).equals(usuario.getId())) {
					for (int j = 3; j < playlistAtual.size() - 1; j += 2) {
	
						Musica aux = new Musica(playlistAtual.get(j), playlistAtual.get(j + 1));
						if (!musicas.contains(aux))
							musicas.add(aux);
						

					}
					Playlist playTemp;
					playTemp = new Playlist(playlistAtual.get(2), musicas);
					playlists.add(playTemp);
				}
				
				
			} catch (IOException e) {
				throw e;
			}
		}
		return playlists;

	}
	
	/**
     * Adiciona uma nova playlist para o usuário VIP e atualiza o arquivo de configuração.
     *
     * @param playlist A playlist a ser adicionada.
     * @param usuario O usuário VIP para o qual adicionar a playlist.
     * @throws IOException Se ocorrer um erro durante a gravação no arquivo.
     */
	public static void adicionar(Playlist playlist, UsuarioVIP usuario) throws IOException {
		ArrayList<Playlist> playlists = PlaylistDAO.carregar(usuario);
		
		boolean teste = playlists.contains(playlist);
		
		if (!playlists.contains(playlist)) {
			playlists.add(playlist);

			String diretorioAtual = System.getProperty("user.dir");
			String caminho = diretorioAtual + "/dados/playlists/playlist_" + usuario.getId() + "_" + playlist.getNome()
					+ ".txt";

			// cria e escreve no arquivo inividual da playlist
			try (FileWriter fw = new FileWriter(caminho, false)) {

				String conteudo = usuario.getNome() + "," + usuario.getId();
				fw.write(conteudo);
				fw.write(System.lineSeparator());

				conteudo = playlist.getNome();
				fw.write(conteudo);
				fw.write(System.lineSeparator());

				for (Musica musica : playlist.getMusicas()) {
					conteudo = musica.getNome() + "," + musica.getCaminhoArquivo();

					fw.write(conteudo);
					fw.write(System.lineSeparator());
				}

			} catch (IOException e) {
				throw e;
			}

			// escrever no arquivo geral
			try (FileWriter fw = new FileWriter(caminhoArquivo, true)) {
				String conteudo = playlist.getNome() + "," + caminho;

				fw.write(conteudo);
				fw.write(System.lineSeparator());

			} catch (IOException e) {
				throw e;
			}
		}

	}

	/**
     * Remove uma playlist do usuário VIP e atualiza o arquivo de configuração.
     *
     * @param playlist A playlist a ser removida.
     * @param usuario O usuário VIP para o qual remover a playlist.
     * @throws IOException Se ocorrer um erro durante a gravação no arquivo.
     */
	public static void remover(Playlist playlist, UsuarioVIP usuario) throws IOException {
		ArrayList<Playlist> playlists = PlaylistDAO.carregar(usuario);

		if (playlists.contains(playlist)) {
			playlists.remove(playlist);

			String diretorioAtual = System.getProperty("user.dir");
			String caminho = diretorioAtual + "/dados/playlists/playlist_" + usuario.getId() + "_" + playlist.getNome()
					+ ".txt";

			try {
				File arquivo = new File(caminho);
				if (arquivo.exists()) {
					arquivo.delete();
				}
			} catch (Exception e) {
				throw e;
			}

			ArrayList<String> nomes = new ArrayList<>();
			ArrayList<String> caminhos = new ArrayList<>();
			try (BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo))) {
				String linha;

				while ((linha = br.readLine()) != null) {
					String[] partes = linha.split(",");
					nomes.add(partes[0]);
					caminhos.add(partes[1]);
				}
			} catch (IOException e) {
				throw e;
			}

			try (FileWriter fw = new FileWriter(caminhoArquivo, false)) {

				for (int i = 0; i < nomes.size(); i++) {
					if (!(nomes.get(i).equals(playlist.getNome()))) {
						String conteudo = nomes.get(i) + "," + caminhos.get(i);

						fw.write(conteudo);
						fw.write(System.lineSeparator());
					}
				}

			} catch (IOException e) {
				throw e;
			}
		}
	}

	/**
     * Remove todas as playlists associadas ao usuário VIP, tanto dos arquivos individuais quanto do arquivo geral.
     *
     * @param usuario O usuário VIP para o qual remover todas as playlists.
     * @throws IOException Se ocorrer um erro durante a remoção dos arquivos ou a atualização do arquivo geral.
     */
	public static void removerTodasPlaylists(UsuarioVIP usuario) throws IOException {
		ArrayList<Playlist> playlists = PlaylistDAO.carregar(usuario);
		String diretorioAtual = System.getProperty("user.dir");
		String caminho;
		
		for (Playlist playlist : playlists) {
			
			caminho = diretorioAtual + "/dados/playlists/playlist_" + usuario.getId() + "_" + playlist.getNome()
					+ ".txt";
			
			try {
				File arquivo = new File(caminho);
				if (arquivo.exists()) {
					arquivo.delete();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		ArrayList<String> nomes = new ArrayList<>();
		ArrayList<String> caminhos = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo))) {
			String linha;

			while ((linha = br.readLine()) != null) {
				String[] partes = linha.split(",");
				nomes.add(partes[0]);
				caminhos.add(partes[1]);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		int posicao;
		for (Playlist playlist : playlists) {
			caminho = diretorioAtual + "/dados/playlists/playlist_" + usuario.getId() + "_" + playlist.getNome()
			+ ".txt";
			
			posicao = caminhos.indexOf(caminho);
			nomes.remove(posicao);			
			caminhos.remove(posicao);
		}
		
		try (FileWriter fw = new FileWriter(caminhoArquivo, false)) {
			for (int i = 0; i < nomes.size(); i++) {
				String conteudo = nomes.get(i) + "," + caminhos.get(i);				
					fw.write(conteudo);
					fw.write(System.lineSeparator());
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		playlists.clear();
	}
	
	
	/**
     * Remove uma música de uma playlist específica associada ao usuário VIP e atualiza o arquivo da playlist.
     *
     * @param musica A música a ser removida.
     * @param playlist A playlist da qual remover a música.
     * @param usuario O usuário VIP associado à playlist.
     */
	public static void removerMusica(Musica musica, Playlist playlist, UsuarioVIP usuario) {

		playlist.removerMusica(musica);

		String diretorioAtual = System.getProperty("user.dir");
		String caminho = diretorioAtual + "/dados/playlists/playlist_" + usuario.getId() + "_" + playlist.getNome()
				+ ".txt";

		try (FileWriter fw = new FileWriter(caminho, false)) {
			String conteudo = usuario.getNome() + "," + usuario.getId();
			fw.write(conteudo);
			fw.write(System.lineSeparator());

			conteudo = playlist.getNome();
			fw.write(conteudo);
			fw.write(System.lineSeparator());

			for (Musica musicaAtual : playlist.getMusicas()) {
				conteudo = musicaAtual.getNome() + "," + musicaAtual.getCaminhoArquivo();
				fw.write(conteudo);
				fw.write(System.lineSeparator());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
     * Remove uma música de todas as playlists associadas ao usuário VIP.
     *
     * @param musica A música a ser removida de todas as playlists.
     * @param usuario O usuário VIP associado às playlists.
     * @throws IOException Se ocorrer um erro durante a remoção das músicas das playlists.
     */
	public static void removerMusica(Musica musica, UsuarioVIP usuario) throws IOException {
	    ArrayList<Playlist> playlists = PlaylistDAO.carregar(usuario);

	    Map<Playlist, Musica> paraRemover = new HashMap<>();

	    for (Playlist p : playlists) {
	        for (Musica m : p.getMusicas()) {
	            if (musica.equals(m)) {
	            	paraRemover.put(p, m);
	            }
	        }
	        
	    }

	    paraRemover.forEach((playlist, m) -> {
	    	 removerMusica(m, playlist, usuario); 
	    });
	}

	
	/**
     * Adiciona uma música a uma playlist específica associada ao usuário VIP e atualiza o arquivo da playlist.
     *
     * @param musica A música a ser adicionada à playlist.
     * @param playlist A playlist à qual adicionar a música.
     * @param usuario O usuário VIP associado à playlist.
     */
	public static void adicionarMusica(Musica musica, Playlist playlist, UsuarioVIP usuario) {
		playlist.adicionarMusica(musica);

		String diretorioAtual = System.getProperty("user.dir");
		String caminho = diretorioAtual + "/dados/playlists/playlist_" + usuario.getId() + "_" + playlist.getNome()
				+ ".txt";

		try (FileWriter fw = new FileWriter(caminho, true)) {
			String conteudo = musica.getNome() + "," + musica.getCaminhoArquivo();
			fw.write(conteudo);
			fw.write(System.lineSeparator());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
