package fr.utc.lo23.sharutc.ui.widget;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class RatingStar extends Button {

    private static final Image STAR_EMPTY =
            new Image("fr/utc/lo23/sharutc/ui/drawable/star_empty_small.png");
    private static final Image STAR_FILL =
            new Image("fr/utc/lo23/sharutc/ui/drawable/star_fill_small.png");
    private final ImageView mImageView;
    private boolean mIsFill;

    public RatingStar() {
        super();
        mImageView = new ImageView(STAR_EMPTY);
        mIsFill = false;
        setGraphic(mImageView);
        getStyleClass().add("imageButton");
    }

    public void fill(boolean isFill) {
        mIsFill = isFill;
        if (isFill) {
            mImageView.setImage(STAR_FILL);
        } else {
            mImageView.setImage(STAR_EMPTY);
        }
    }

    public boolean isFilled() {
        return mIsFill;
    }
}
