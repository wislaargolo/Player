package dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import modelo.Musica;
import modelo.Usuario;
import modelo.UsuarioVIP;

public class MusicaDAO {
	
	public static String caminhoUsuario(Usuario usuario) {
		return "dados/musicas/musicas_" + usuario.getId() + ".txt";
	}
	
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

	public static ArrayList<Musica> carregar(Usuario usuario) {
		String caminhoArquivo = caminhoUsuario(usuario);
		ArrayList<Musica> musicas = new ArrayList<Musica>();
		
		verificarCaminho(caminhoArquivo);
		
		try (BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo))) {
            String linha;

            while ((linha = br.readLine()) != null) {
            	String[] partes = linha.split(",");
                String nome = partes[0];
                String caminho = partes[1];
                musicas.add(new Musica(nome, caminho));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
		return musicas;
	}
	public static void adicionar(Usuario usuario, Musica musica) {
		String caminhoArquivo = caminhoUsuario(usuario);
		ArrayList<Musica> musicas = carregar(usuario);
		
		if (!musicas.contains(musica)) {
			musicas.add(musica);		
	
	        try (FileWriter fw = new FileWriter(caminhoArquivo, true)){
	            String conteudo = musica.getNome() + "," + musica.getCaminhoArquivo();
	            
	            fw.write(conteudo);
	            fw.write(System.lineSeparator());
	            
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
        }
	}
	
	public static void remover(Usuario usuario, Musica musica) {
		String caminhoArquivo = caminhoUsuario(usuario);
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
				e.printStackTrace();
			}
        }
		
	}
	

}
