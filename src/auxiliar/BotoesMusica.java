package auxiliar;

import java.io.FileInputStream;

import javazoom.jl.decoder.Bitstream;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;
import modelo.Musica;

public class BotoesMusica {
	
	private AdvancedPlayer player;
    private boolean emReproducao = false;
    
    public void comcarMusica(Musica musica) {
        if (!emReproducao) {
            //Musica musica = new Musica("olivia", "C:\\Users\\wisla\\eclipse-wsl\\Player\\dados\\musicas\\Just_The_Two_Of_Us_Karaoke_Acoustic_-_Bill_Withers_Slow_Version___HQ_Audio_128_kbps.mp3");
            try {
                FileInputStream fileInputStream = new FileInputStream(musica.getCaminhoArquivo());
                Bitstream bitstream = new Bitstream(fileInputStream);
                int duration = bitstream.readFrame().max_number_of_frames((int) fileInputStream.getChannel().size());
                fileInputStream.close();

                FileInputStream fileInputStreamForPlayer = new FileInputStream(musica.getCaminhoArquivo());
                player = new AdvancedPlayer(fileInputStreamForPlayer);
                player.setPlayBackListener(new PlaybackListener() {
                    @Override
                    public void playbackFinished(PlaybackEvent evt) {
                        System.out.println("Reprodução concluída.");
                        stopMusic();
                    }
                });
                new Thread(() -> {
                    try {
                        player.play(0, duration);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally{
                    	emReproducao = false;
                    }
                }).start();
                
                emReproducao = false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    } 
	
    public void stopMusic() {
        // Você pode adicionar lógica adicional aqui, se necessário
        // Por exemplo, pausar a música em vez de parar completamente
    	if (emReproducao) {
            player.close();
            emReproducao = false;
        }
    }
}
