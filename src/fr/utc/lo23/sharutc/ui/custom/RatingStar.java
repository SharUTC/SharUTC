package fr.utc.lo23.sharutc.ui.custom;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * A simple {@link Button} with a nice look of a star.
 *
 * This class is used to rate something. It has to style : one when the star is
 * filled and one when the star is not filled.
 */
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

    /**
     * Set the proper style according to the fill state.
     *
     * @param isFill is the star filled.
     */
    public void fill(boolean isFill) {
        mIsFill = isFill;
        if (isFill) {
            mImageView.setImage(STAR_FILL);
        } else {
            mImageView.setImage(STAR_EMPTY);
        }
    }

    /**
     * Get the current fill state of the button.
     *
     * @return true is the star is filled, false otherwise.
     */
    public boolean isFilled() {
        return mIsFill;
    }
}
