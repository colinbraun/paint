package paint.controller;

import javafx.fxml.FXML;
import javafx.stage.Stage;

/**
 * A controller for the popup that happens when saving to an image type that is lossy.
 */
public class LossyFilePopupController extends BaseController {

    /**
     * The {@link Stage} that this controller is associated with
     */
    private Stage theStage;
    /**
     * The choice that was made
     */
    private int choice;

    /**
     * Set the stage. Should only be called on the construction of this controller
     * @param stage the stage this controller is controlling
     */
    public void setStage(Stage stage) {
        theStage = stage;
    }

    /**
     * Runs when yes is clicked
     */
    @FXML
    public void handleYes() {
        choice = 1;
        theStage.close();
    }

    /**
     * Runs when no is clicked
     */
    @FXML
    public void handleNo() {
        choice = 0;
        theStage.close();
    }

    /**
     * Get the choice that was made
     * @return the choice
     */
    public int getChoice() {
        return choice;
    }
}
