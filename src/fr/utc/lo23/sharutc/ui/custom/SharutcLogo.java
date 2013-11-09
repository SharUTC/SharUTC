package fr.utc.lo23.sharutc.ui.custom;

import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

public class SharutcLogo extends StackPane {

    //Images
    private static final Image LOGO_PART_1 = new Image("fr/utc/lo23/sharutc/ui/drawable/logo_1.png");
    private static final Image LOGO_PART_2 = new Image("fr/utc/lo23/sharutc/ui/drawable/logo_2.png");
    private static final Image LOGO_PART_3 = new Image("fr/utc/lo23/sharutc/ui/drawable/logo_3.png");
    private static final Image LOGO_PART_4 = new Image("fr/utc/lo23/sharutc/ui/drawable/logo_4.png");
    
    //top inset 
    private static final double BASE_TOP_INSET_LOGO_PART_2 = 135.0;
    private static final double BASE_TOP_INSET_LOGO_PART_3 = 135.0;
    private static final double BASE_TOP_INSET_LOGO_PART_4 = 130.0;
    private static final double DELTA_TOP_INSET_LOGO_PART_2 = -5.0;
    private static final double DELTA_TOP_INSET_LOGO_PART_3 = 5.0;
    private static final double DELTA_TOP_INSET_LOGO_PART_4 = -26.0;
    
    //right inset
    private static final double BASE_RIGHT_INSET_LOGO_PART_2 = 0.0;
    private static final double BASE_RIGHT_INSET_LOGO_PART_3 = 0.0;
    private static final double BASE_RIGHT_INSET_LOGO_PART_4 = 0.0;            
    private static final double DELTA_RIGHT_INSET_LOGO_PART_2 = 8.0;
    private static final double DELTA_RIGHT_INSET_LOGO_PART_3 = -8.0;
    private static final double DELTA_RIGHT_INSET_LOGO_PART_4 = 80.0;
    
    private final ImageView mLogoPart1;
    private final ImageView mLogoPart2;
    private final ImageView mLogoPart3;
    private final ImageView mLogoPart4;

    public SharutcLogo() {
        mLogoPart1 = new ImageView();
        mLogoPart1.setCache(true);
        mLogoPart1.setSmooth(true);
        mLogoPart1.setPreserveRatio(true);
        mLogoPart1.setImage(LOGO_PART_1);

        mLogoPart2 = new ImageView();
        mLogoPart2.setCache(true);
        mLogoPart2.setSmooth(true);
        mLogoPart2.setPreserveRatio(true);
        mLogoPart2.setImage(LOGO_PART_2);

        mLogoPart3 = new ImageView();
        mLogoPart3.setCache(true);
        mLogoPart3.setSmooth(true);
        mLogoPart3.setPreserveRatio(true);
        mLogoPart3.setImage(LOGO_PART_3);

        mLogoPart4 = new ImageView();
        mLogoPart4.setCache(true);
        mLogoPart4.setSmooth(true);
        mLogoPart4.setPreserveRatio(true);
        mLogoPart4.setImage(LOGO_PART_4);

        getChildren().addAll(mLogoPart1, mLogoPart2, mLogoPart3, mLogoPart4);
        
        StackPane.setMargin(mLogoPart2, new Insets(BASE_TOP_INSET_LOGO_PART_2, BASE_RIGHT_INSET_LOGO_PART_2, 0, 0));
        StackPane.setMargin(mLogoPart3, new Insets(BASE_TOP_INSET_LOGO_PART_3, BASE_RIGHT_INSET_LOGO_PART_3, 0, 0));
        StackPane.setMargin(mLogoPart4, new Insets(BASE_TOP_INSET_LOGO_PART_4, BASE_RIGHT_INSET_LOGO_PART_4, 0, 0));
    }

    public void animate(MouseEvent mouseEvent) {
        final double mouseX = mouseEvent.getSceneX();
        final double mouseY = mouseEvent.getSceneY();
        final double centerX = getScene().getWidth() / 2;
        final double centerY = getScene().getHeight() / 2;

        final double normalizedX = (mouseX - centerX) / centerX;
        final double normalizedY = (mouseY - centerY) / centerY;

        final double rightInsetPart2 = BASE_RIGHT_INSET_LOGO_PART_2
                + DELTA_RIGHT_INSET_LOGO_PART_2 * normalizedX;
        final double topInsetPart2 = BASE_TOP_INSET_LOGO_PART_2
                + DELTA_TOP_INSET_LOGO_PART_2 * normalizedY;

        final double rightInsetPart3 = BASE_RIGHT_INSET_LOGO_PART_3
                + DELTA_RIGHT_INSET_LOGO_PART_3 * normalizedX;
        final double topInsetPart3 = BASE_TOP_INSET_LOGO_PART_3
                + DELTA_TOP_INSET_LOGO_PART_3 * normalizedY;

        final double rightInsetPart4 = BASE_RIGHT_INSET_LOGO_PART_4
                + DELTA_RIGHT_INSET_LOGO_PART_4 * normalizedX;
        final double topInsetPart4 = BASE_TOP_INSET_LOGO_PART_4
                + DELTA_TOP_INSET_LOGO_PART_4 * normalizedY;


        StackPane.setMargin(mLogoPart2, new Insets(topInsetPart2, rightInsetPart2, 0, 0));
        StackPane.setMargin(mLogoPart3, new Insets(topInsetPart3, rightInsetPart3, 0, 0));
        StackPane.setMargin(mLogoPart4, new Insets(topInsetPart4, rightInsetPart4, 0, 0));
    }
}
