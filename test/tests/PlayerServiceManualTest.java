package tests;

import fr.utc.lo23.sharutc.controler.service.FileServiceImpl;
import fr.utc.lo23.sharutc.controler.service.PlayerServiceImpl;
import fr.utc.lo23.sharutc.model.AppModelImpl;
import fr.utc.lo23.sharutc.model.domain.Music;
import fr.utc.lo23.sharutc.model.userdata.Profile;
import fr.utc.lo23.sharutc.model.userdata.UserInfo;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.Scanner;

public class PlayerServiceManualTest implements PropertyChangeListener {

    private final static int EXIT = 0;
    private final static int PLAY = 1;
    private final static int PAUSE = 2;
    private final static int STOP = 3;
    private final static int NEXT = 4;
    private final static int PREV = 5;
    private final static AppModelImpl appModel = new AppModelImpl();
    private final static FileServiceImpl fileService = new FileServiceImpl(appModel);
    private final static PlayerServiceImpl playerService = new PlayerServiceImpl(fileService);
    private final static Scanner scanIn = new Scanner(System.in);

    public static void main(String[] args) {
        PlayerServiceManualTest p = new PlayerServiceManualTest();
        p.run();
    }

    private void run() {
        playerService.addPropertyChangeListener(this);
        Profile p = new Profile(new UserInfo());
        appModel.setProfile(p);
        addMusicToPlaylist();
        runConsole();
    }

    private static void addMusicToPlaylist() {
        String TEST_MP3_FOLDER = "";
        try {
            TEST_MP3_FOLDER = new File(".").getCanonicalPath() + "\\test\\mp3\\";
        } catch (Exception ex) {
            System.err.println(ex.toString());
        }
        String[] filenames = {"14 - End Credit Score.mp3","Air - Moon Safari - Sexy Boy.mp3", "Sting & The Police - The Very Best Of Sting & The Police - 17 - Roxanne.mp3"};
        try {
            for (String mp3File : filenames) {
                Music music = fileService.readFile(new File(TEST_MP3_FOLDER + mp3File));
                playerService.addToPlaylist(music);
                System.out.println("Added : " + music.getTitle() + " from artist " + music.getArtist());
            }
        } catch (Exception ex) {
            System.err.println("Failed to add musics");
            ex.printStackTrace();
        }
    }

    private static void runConsole() {
        boolean exit = false;
        while (!exit) {
            System.out.println("0=EXIT");
            System.out.println("1=PLAY");
            System.out.println("2=PAUSE");
            System.out.println("3=STOP");
            System.out.println("4=NEXT");
            System.out.println("5=PREV");
            System.out.println("Input : ");
            String actionTxt = scanIn.nextLine();
            System.out.println("");
            int action;
            try {
                action = Integer.parseInt(actionTxt);
            } catch (NumberFormatException nfe) {
                action = 99;
            }
            switch (action) {
                case EXIT:
                    System.out.println("EXIT");
                    playerService.playerStop();
                    scanIn.close();
                    exit = true;
                    break;
                case PLAY:
                    System.out.println("PLAY");
                    playerService.playerPlay();
                    break;
                case PAUSE:
                    System.out.println("PAUSE");
                    playerService.playerPause();
                    break;
                case STOP:
                    System.out.println("STOP");
                    playerService.playerStop();
                    break;
                case NEXT:
                    System.out.println("NEXT");
                    playerService.playerNext();
                    break;
                case PREV:
                    System.out.println("PREV");
                    playerService.playerPrevious();
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(PlayerServiceImpl.Property.CURRENT_TIME.name())) {
            System.out.println("time : " + evt.getNewValue() + " / " + playerService.getTotalTimeMs());
        } else if (evt.getPropertyName().equals(PlayerServiceImpl.Property.SELECTED_MUSIC.name())) {
            System.out.println("Music : " + evt.getNewValue());
        }
    }
}
