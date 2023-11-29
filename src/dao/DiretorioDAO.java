package dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import modelo.Musica;
import modelo.Usuario;

public class DiretorioDAO {
	
	public static String caminhoUsuario(Usuario usuario) {
		return "dados/diretorios/diretorios_" + usuario.getId() + ".txt";
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

	public static ArrayList<String> carregar(Usuario usuario) {
		String caminhoArquivo = caminhoUsuario(usuario);
		ArrayList<String> diretorios = new ArrayList<>();
		
		verificarCaminho(caminhoArquivo);
		
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
            e.printStackTrace();
        }
		return diretorios;

	}
	
	public static void carregarMusicas(Usuario usuario, File diretorio) {
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
	
	private static void removerMusicas(File diretorio, Usuario usuario) {
        File[] listaArquivos = diretorio.listFiles();
        if (listaArquivos != null) {
            for (File arquivo : listaArquivos) {
                if (arquivo.isFile() && arquivo.getName().toLowerCase().endsWith(".mp3")) {
                    MusicaDAO.remover(usuario, new Musica(arquivo.getName(), arquivo.getAbsolutePath()));
                }
            }
        }
	}

	public static void adicionar(Usuario usuario, String caminhoDiretorio) {
		String caminhoArquivo = caminhoUsuario(usuario);
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
	                e.printStackTrace();
	            }
	        }
	    }
        	
		
	}
	
	public static void remover(String caminhoDiretorio, Usuario usuario) {
		String caminhoArquivo = caminhoUsuario(usuario);
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
	                e.printStackTrace();
	            }
	        }
	    }
        	
		
	}	


}
