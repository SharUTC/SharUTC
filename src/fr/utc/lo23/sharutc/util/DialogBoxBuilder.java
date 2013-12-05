package fr.utc.lo23.sharutc.util;


import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonBuilder;
import javafx.scene.control.LabelBuilder;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFieldBuilder;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.layout.HBoxBuilder;
import javafx.scene.layout.VBoxBuilder;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class DialogBoxBuilder {


    /**
     * Create a DialogBox with a TextField
     *
     * @param label    instruction displayed to the user
     * @param hint     hint for the TextField
     * @param style    stylesheet class
     * @param root     root view
     * @param callback catch on validate clicked
     * @return Stage to show
     */
    public static Stage createEditBox(final String label, final String hint, final String style, final Parent root, final IEditBox callback) {
        final Stage dialog = new Stage(StageStyle.TRANSPARENT);
        dialog.initModality(Modality.APPLICATION_MODAL);
        final TextField editableName = TextFieldBuilder.create().editable(true).styleClass("text-field").text(hint).build();

        //set effect
        applyEffect(true, root);

        dialog.setScene(
                new Scene(
                        VBoxBuilder.create().styleClass("modal-dialog").children(
                                HBoxBuilder.create().styleClass("modal-dialog").children(
                                        LabelBuilder.create().text(label).build(),
                                        editableName)
                                        .build(),
                                ButtonBuilder.create().styleClass("positiveButton").text("Validate").defaultButton(true).onAction(new EventHandler<ActionEvent>() {
                                    @Override
                                    public void handle(ActionEvent actionEvent) {
                                        // take action and close the dialog.
                                        callback.onValidate(editableName.getText());

                                        //remove effect
                                        applyEffect(false, root);
                                        dialog.close();
                                    }
                                }).build()
                        ).build()
                )
        );
        dialog.getScene().getStylesheets().add(style);
        return dialog;
    }

    /**
     * Create a Confirm box : yes or no
     *
     * @param label    instruction displayed to the user
     * @param style    stylesheet class
     * @param root     root view
     * @param callback catch user choice
     * @return Stage to show
     */
    public static Stage createConfirmBox(final String label, final String style, final Parent root, final IConfirmBox callback) {
        final Stage dialog = new Stage(StageStyle.TRANSPARENT);
        dialog.initModality(Modality.APPLICATION_MODAL);

        //set effect
        applyEffect(true, root);

        dialog.setScene(
                new Scene(
                        VBoxBuilder.create().styleClass("modal-dialog").children(
                                LabelBuilder.create().text(label).build(),
                                HBoxBuilder.create().styleClass("modal-dialog").children(
                                        ButtonBuilder.create().styleClass("positiveButton").text("Yes").defaultButton(true).onAction(new EventHandler<ActionEvent>() {
                                            @Override
                                            public void handle(ActionEvent actionEvent) {
                                                callback.onChoiceMade(true);
                                                applyEffect(false, root);
                                                dialog.close();
                                            }
                                        }
                                        ).build(),
                                        ButtonBuilder.create().styleClass("negativeButton").text("No ").defaultButton(true).onAction(new EventHandler<ActionEvent>() {
                                            @Override
                                            public void handle(ActionEvent actionEvent) {
                                                callback.onChoiceMade(false);
                                                applyEffect(false, root);
                                                dialog.close();
                                            }
                                        }
                                        ).build()
                                ).build()

                        ).build()
                )
        );
        dialog.getScene().getStylesheets().add(style);
        return dialog;
    }

    /**
     * set effect to the Parent view
     *
     * @param apply true if effect has to be displayed, false if effect has to be hidden
     * @param root  parent view on which the effect will be applied
     */
    private static void applyEffect(boolean apply, Parent root) {
        if (apply) {
            root.setEffect(new ColorAdjust(0, 0, -0.5, -0.5));
        } else {
            root.setEffect(null);
        }

    }

    /**
     * Get callback from an EditDialogBox
     */
    public interface IEditBox {
        public void onValidate(String value);
    }

    /**
     * Get answer from a ConfirmDialogBox
     */
    public interface IConfirmBox {
        public void onChoiceMade(boolean answer);
    }
}
