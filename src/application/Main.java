package application;
	

import java.io.IOException;
import java.util.ArrayList;

import dao.DiretorioDAO;
import dao.MusicaDAO;
import dao.PlaylistDAO;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import modelo.Musica;
import modelo.Playlist;
import modelo.UsuarioVIP;
import visao.GerenciadorCenas;


public class Main extends Application {
	@Override
	 public void start(Stage palcoPrincipal) {
        GerenciadorCenas.setPalcoPrincipal(palcoPrincipal);
        GerenciadorCenas.mudarCena("/visao/TelaLogin.fxml");
    }

    public static void main(String[] args) {
        //launch(args);
	
		UsuarioVIP usuario = new UsuarioVIP("RM777", "Rubens", "rrmm");
		MusicaDAO.carregar(usuario);
		DiretorioDAO.carregar(usuario);
		
		Musica musicaTeste = new Musica("tudo deve ser", "/dados/musicas/musicasss");
		Musica musicaT2 = new Musica("Como tudo lalal ala", "/dados/musicas/musicasss");
		
		MusicaDAO.adicionar(usuario, musicaTeste);
		MusicaDAO.adicionar(usuario, musicaT2);
		
		ArrayList<Musica> musicasss = new ArrayList<>();
		musicasss.add(musicaTeste);
		musicasss.add(musicaT2);
		
		Playlist playlist = new Playlist("playlistDeRubens", musicasss);
		PlaylistDAO.carregar(usuario);
		PlaylistDAO.adicionar(playlist, usuario);
		
		for(Playlist m : PlaylistDAO.carregar(usuario)) {
			for(Musica mus : m.getMusicas())
				System.out.println("teste" + mus.getNome());
		}
//		
//		MusicaDAO.remover(usuario,musicaTeste);
		

//		for(Musica m : playlist.getMusicas()) {
//			System.out.println(m.getNome());
//		}
//		
//		for (Playlist l : play.getPlaylists()) {
//			System.out.println(l.getNome());
//		}
//		
//		usuario.renomearPaylist(playlist, "novoNome");
//		
//		System.out.println("\n\n\n\n");
//		
//		play = usuario.getPlaylistDAO();
//		for (Playlist l : play.getPlaylists()) {
//			System.out.println(l.getNome());
//		}
		//System.out.println(diretorios.getCaminhoArquivo());
		
    }
}
