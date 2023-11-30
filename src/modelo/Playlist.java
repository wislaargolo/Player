package modelo;

import java.util.ArrayList;
import java.util.Objects;

public class Playlist {
	
	private String nome; 
	private ArrayList<Musica> musicas;

	public Playlist() {
		this.musicas = new ArrayList<>();
	}
	
	public Playlist(String nome, ArrayList<Musica> musicas) {
		this.nome = nome;
		this.musicas = musicas;
	}
	
	public Playlist(String nome) {
		this.nome = nome;
		this.musicas = new ArrayList<>();
	}
	
	public void adicionarMusica(Musica musica) {
		musicas.add(musica);
	}
	
	public void removerMusica(Musica musica) {
		musicas.remove(musica);
	}


	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public ArrayList<Musica> getMusicas() {
		return musicas;
	}

	public void setMusicas(ArrayList<Musica> musicas) {
		this.musicas = musicas;
	}
	
//	@Override
//    public boolean equals(Object obj) {
//        if (this == obj) {
//            return true;
//        }
//
//        if (obj == null || getClass() != obj.getClass()) {
//            return false;
//        }
//
//        Playlist playlist = (Playlist) obj;
//        
//        for(Musica musica : musicas) {
//        	for(Musica outra : playlist.musicas) {
//        		if(musica.equals(outra)) return false;
//        	}
//        }
//
//        return nome.equals(playlist.nome);
//    }
	
	
	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        
        if (o == null || getClass() != o.getClass()) return false;
        
        Playlist playlist = (Playlist) o;
        
        if (Objects.equals(nome, playlist.nome)) {
        	return true;
        }
        
        return Objects.equals(nome, playlist.nome) &&
                Objects.equals(musicas, playlist.musicas);
    }
	
	@Override
    public String toString() {
        return nome;
    }
	

}
