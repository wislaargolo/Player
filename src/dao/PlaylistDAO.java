package dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Stream;

import modelo.Musica;
import modelo.Playlist;
import modelo.Usuario;
import modelo.UsuarioVIP;
import util.DAOUtil;

public class PlaylistDAO {
	private static String caminhoArquivo = "dados/playlists/playlistsGeral.txt";

	private PlaylistDAO() {
	}
	

	public static ArrayList<Playlist> carregar(UsuarioVIP usuario) {
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
			e.printStackTrace();
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
				e.printStackTrace();
			}
		}
		for(Playlist p : playlists) {
			System.out.println("CARREGAR"  + p.getNome());
		}
		return playlists;

	}

	public static void adicionar(Playlist playlist, UsuarioVIP usuario) {
		ArrayList<Playlist> playlists = PlaylistDAO.carregar(usuario);
		
		boolean teste = playlists.contains(playlist);
		
		if (teste) {
			System.out.println("foi");
		}else {
			System.out.println("não foi");
		}
		
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
				e.printStackTrace();
			}

			// escrever no arquivo geral
			try (FileWriter fw = new FileWriter(caminhoArquivo, true)) {
				String conteudo = playlist.getNome() + "," + caminho;

				fw.write(conteudo);
				fw.write(System.lineSeparator());

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public static void remover(Playlist playlist, UsuarioVIP usuario) {
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
				e.printStackTrace();
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

			try (FileWriter fw = new FileWriter(caminhoArquivo, false)) {

				for (int i = 0; i < nomes.size(); i++) {
					if (!(nomes.get(i).equals(playlist.getNome()))) {
						String conteudo = nomes.get(i) + "," + caminhos.get(i);

						fw.write(conteudo);
						fw.write(System.lineSeparator());
					}
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void atualizar(Playlist playlist, UsuarioVIP usuario, String novoNome) {
		ArrayList<Playlist> playlists = PlaylistDAO.carregar(usuario);
		if (playlists.contains(playlist)) {

			String diretorioAtual = System.getProperty("user.dir");
			String caminhoAtual = diretorioAtual + "/dados/playlists/playlist_" + usuario.getId() + "_"
					+ playlist.getNome() + ".txt";

			int posicao = playlists.indexOf(playlist);
			playlist.setNome(novoNome);
			playlists.set(posicao, playlist);

			String caminhoNovo = diretorioAtual + "/dados/playlists/playlist_" + usuario.getId() + "_"
					+ playlist.getNome() + ".txt";

			File arquivoAtual = new File(caminhoAtual);
			File novoArquivo = new File(caminhoNovo);

			try {
				arquivoAtual.renameTo(novoArquivo);

			} catch (SecurityException e) {
				System.err.println("Erro de segurança ao tentar renomear o arquivo.");
				e.printStackTrace();
			} catch (Exception e) {
				System.err.println("Ocorreu um erro inesperado durante a renomeação do arquivo.");
				e.printStackTrace();
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

			posicao = caminhos.indexOf(caminhoAtual);
			nomes.set(posicao, playlist.getNome());
			caminhos.set(posicao, caminhoNovo);

			try (FileWriter fw = new FileWriter(caminhoArquivo, false)) {

				for (int i = 0; i < nomes.size(); i++) {
					String conteudo = nomes.get(i) + "," + caminhos.get(i);

					fw.write(conteudo);
					fw.write(System.lineSeparator());
				}

			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}
	
	public static void removerTodasPlaylists(UsuarioVIP usuario) {
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

	public static void removerMusica(Musica musica, UsuarioVIP usuario) {
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
