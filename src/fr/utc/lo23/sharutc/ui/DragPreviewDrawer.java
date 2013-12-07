package fr.utc.lo23.sharutc.ui;


import fr.utc.lo23.sharutc.ui.custom.card.DraggableCard;
import javafx.geometry.Insets;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

import java.util.ArrayList;

public abstract class DragPreviewDrawer implements RighpaneInterface {

    protected StackPane mDragPreview;

    public void init(StackPane dragPreview) {
        mDragPreview = dragPreview;

    }

    /**
     * Add card snapshot to the preview and move it to the right place
     * Override this method to add item in the preview
     *
     * @param event
     */
    protected void updateDragPreview(MouseEvent event, ArrayList<? extends DraggableCard> list) {
        for (int i = 0; i < list.size(); i++) {
            final DraggableCard card = list.get(i);
            final ImageView preview = new ImageView(card.snapshot(null, null));
            card.dragged();
            StackPane.setMargin(preview, new Insets(20 * i, 20 * i, 0, 0));
            mDragPreview.getChildren().add(preview);
        }
        mDragPreview.relocate(
                (int) (event.getSceneX() - mDragPreview.getBoundsInParent().getWidth() / 2),
                (int) (event.getSceneY() - mDragPreview.getBoundsInParent().getHeight() / 2));

    }

    /**
     * Don't forget to call this method when drag stopped to hide the preview
     */
    protected void hideDragPreview() {
        mDragPreview.getChildren().clear();
    }

}
