package View;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.nio.file.Paths;

public class Music {

    //media player for each sound
    private static MediaPlayer mediaPlayerGame;
    private static MediaPlayer mediaPlayerTarget;
    private static MediaPlayer mediaPlayerCrash;
    private static boolean soundGame= true; //if the sound game is on or not

    public static MediaPlayer getMediaPlayerGame() {
        return mediaPlayerGame;
    }

    public static boolean isPlay(){ //checking if the music play
        return soundGame;
    }

    public static void changeSoundGame(){ //change sound from mute to on or the opposite
        Music.soundGame = !Music.soundGame;
        if(soundGame) {
            playGameMusic();
        }
        else
            mediaPlayerGame.stop();
    }

    public static void playCrash(){ //play on sound crash when the player trying to do valid move
        if(mediaPlayerCrash == null) {
            Media h = new Media(Paths.get("./src/Resources/Sounds/crash-sound.mp3").toUri().toString());
            mediaPlayerCrash = new MediaPlayer(h);
        }

        if(!mediaPlayerGame.isMute())
                mediaPlayerGame.stop();

        mediaPlayerCrash.play();
        mediaPlayerCrash.setOnEndOfMedia(new Runnable() {
                public void run() {
                mediaPlayerCrash.stop();
                if(soundGame)
                    mediaPlayerGame.play();
            }
        });
    }

    public  static void playTarget(){
        if(mediaPlayerTarget == null) {
            Media h = new Media(Paths.get("./src/Resources/Sounds/Tada-sound.mp3").toUri().toString());
            mediaPlayerTarget = new MediaPlayer(h);
        }
        if(!mediaPlayerGame.isMute())
            mediaPlayerGame.stop();
        mediaPlayerTarget.play();
        mediaPlayerTarget.setOnEndOfMedia(new Runnable() {
            public void run() {
                mediaPlayerTarget.stop();
                if(soundGame)
                    mediaPlayerGame.play();
            }
        });
    }

    public static void playGameMusic(){
        if(mediaPlayerGame == null) {
            Media h = new Media(Paths.get("./src/Resources/Sounds/SupermanTheme.mp3").toUri().toString());
            mediaPlayerGame = new MediaPlayer(h);
            mediaPlayerGame.setVolume(0.08);
        }
        if(soundGame) {
            mediaPlayerGame.play();
            mediaPlayerGame.setOnEndOfMedia(new Runnable() {
                public void run() {
                    mediaPlayerGame.seek(Duration.ZERO);
                }
            });
            mediaPlayerGame.play();

        }

    }
    public static void stopGameMusic(){
            mediaPlayerGame.stop();
    }
}
