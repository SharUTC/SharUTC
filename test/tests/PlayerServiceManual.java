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

public class PlayerServiceManual implements PropertyChangeListener {

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
    private static long currentTimeSec;

    public static void main(String[] args) {
        PlayerServiceManual p = new PlayerServiceManual();
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
        String[] filenames = {"14 - End Credit Score.mp3", "Air - Moon Safari - Sexy Boy.mp3", "Sting & The Police - The Very Best Of Sting & The Police - 17 - Roxanne.mp3"};
        try {
            for (String mp3File : filenames) {
                Music music = fileService.createMusicFromFile(new File(TEST_MP3_FOLDER + mp3File));
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
            System.out.println("0 = EXIT");
            System.out.println("1 = PLAY");
            System.out.println("2 = PAUSE");
            System.out.println("3 = STOP");
            System.out.println("4 = NEXT");
            System.out.println("5 = PREV");
            System.out.println("6 = SET CURRENT TIME -= 15 sec");
            System.out.println("7 = SET CURRENT TIME += 15 sec");
            System.out.println("8 = SET VOLUME -= 10");
            System.out.println("9 = SET VOLUME += 10");
            System.out.println("10= MUTE ON/OFF");
            System.out.println("11..100= SET VOLUME += x");

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
                case 6:
                    System.out.println("SET CURRENT TIME(-= 15 sec)");
                    playerService.setCurrentTimeSec(Math.max(0, currentTimeSec - 15L));
                    break;
                case 7:
                    System.out.println("SET CURRENT TIME(+= 15 sec)");
                    playerService.setCurrentTimeSec(Math.min((playerService != null && playerService.getTotalTimeSec() != null) ? playerService.getTotalTimeSec() : Long.MAX_VALUE, currentTimeSec + 15L));
                    break;
                case 8:
                    System.out.println("SET VOLUME (50)");
                    playerService.setVolume(50);
                    break;
                case 9:
                    System.out.println("SET VOLUME (100)");
                    playerService.setVolume(100);
                    break;
                case 10:
                    System.out.println("MUTE ON/OFF");
                    playerService.setMute(!playerService.isMute());
                    break;
                default:
                    if (action >= 0 && action <= 100) {
                        System.out.println("SET VOLUME (" + action + ")");
                        playerService.setVolume(action);
                    }
                    break;
            }
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(PlayerServiceImpl.Property.CURRENT_TIME.name())) {
            currentTimeSec = (Long) evt.getNewValue();
            System.out.println("Time : " + currentTimeSec + " / " + playerService.getTotalTimeSec() + " seconds");
        } else if (evt.getPropertyName().equals(PlayerServiceImpl.Property.SELECTED_MUSIC.name())) {
            System.out.println("Music : " + evt.getNewValue());
        } else if (evt.getPropertyName().equals(PlayerServiceImpl.Property.MUTE.name())) {
            System.out.println("Mute : " + (((Boolean) evt.getNewValue()).booleanValue() == true ? "ON" : "OFF"));
        } else if (evt.getPropertyName().equals(PlayerServiceImpl.Property.VOLUME.name())) {
            System.out.println("Volume : " + evt.getNewValue());
        } else if (evt.getPropertyName().equals(PlayerServiceImpl.Property.PAUSE.name())) {
            System.out.println("Pause : " + (((Boolean) evt.getNewValue()).booleanValue() == true ? "ON" : "OFF"));
        }
    }
}
