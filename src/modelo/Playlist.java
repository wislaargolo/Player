package modelo;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Representa uma playlist no sistema, contendo um nome e uma lista de músicas.
 * 
 * @author Rubens e Wisla
 */
public class Playlist {

    private String nome;
    private ArrayList<Musica> musicas;

    /**
     * Construtor padrão da classe Playlist.
     */
    public Playlist() {
        this.musicas = new ArrayList<>();
    }

    /**
     * Construtor da classe Playlist.
     *
     * @param nome    O nome da playlist.
     * @param musicas A lista de músicas da playlist.
     */
    public Playlist(String nome, ArrayList<Musica> musicas) {
        this.nome = nome;
        this.musicas = musicas;
    }

    /**
     * Construtor da classe Playlist.
     *
     * @param nome O nome da playlist.
     */
    public Playlist(String nome) {
        this.nome = nome;
        this.musicas = new ArrayList<>();
    }

    /**
     * Adiciona uma música à playlist.
     *
     * @param musica A música a ser adicionada.
     */
    public void adicionarMusica(Musica musica) {
        musicas.add(musica);
    }

    /**
     * Remove uma música da playlist.
     *
     * @param musica A música a ser removida.
     */
    public void removerMusica(Musica musica) {
        musicas.remove(musica);
    }

    /**
     * Obtém o nome da playlist.
     *
     * @return O nome da playlist.
     */
    public String getNome() {
        return nome;
    }

    /**
     * Define o nome da playlist.
     *
     * @param nome O novo nome da playlist.
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Obtém a lista de músicas da playlist.
     *
     * @return A lista de músicas da playlist.
     */
    public ArrayList<Musica> getMusicas() {
        return musicas;
    }

    /**
     * Define a lista de músicas da playlist.
     *
     * @param musicas A nova lista de músicas da playlist.
     */
    public void setMusicas(ArrayList<Musica> musicas) {
        this.musicas = musicas;
    }
	
	
    /**
     * Verifica se duas playlists são iguais com base no nome e na lista de músicas.
     *
     * @param o O objeto a ser comparado.
     * @return true se as playlists forem iguais, false caso contrário.
     */
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
	
	/**
     * Retorna uma representação em string da playlist (apenas o nome).
     *
     * @return O nome da playlist em formato de string.
     */
	@Override
    public String toString() {
        return nome;
    }
	

}
