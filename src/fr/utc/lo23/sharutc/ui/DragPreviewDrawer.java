package fr.utc.lo23.sharutc.ui;


import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

public abstract class DragPreviewDrawer {

    protected StackPane mDragPreview;

    public void init(StackPane dragPreview) {
        mDragPreview = dragPreview;

    }

    /**
     * Only move the preview at the right place
     * Override this method to add item in the preview
     *
     * @param event
     */
    protected void updateDragPreview(MouseEvent event) {
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
