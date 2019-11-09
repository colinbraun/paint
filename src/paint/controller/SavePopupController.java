package paint.controller;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import paint.Main;
import paint.constant.SaveChoice;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the save popup
 * @author Colin Braun
 */
public class SavePopupController extends BaseController {

    /**
     * The stage that this popup controller is assigned to
     */
    private Stage stage;

    /**
     * The decision that was made. 0 -> No. 1 -> Yes. 2 -> Cancel.
     */
    private SaveChoice choice;

    /**
     * Runs when yes is clicked
     */
    @FXML
    public void handleYes() {
        choice = SaveChoice.YES;
        Main.mainController.handleSave();
        stage.close();
    }

    /**
     * Runs when no is clicked
     */
    @FXML
    public void handleNo() {
        choice = SaveChoice.NO;
        stage.close();
    }

    /**
     * Runs when cancel is clicked
     */
    @FXML
    public void handleCancel() {
        choice = SaveChoice.CANCEL;
        stage.close();
    }

    /**
     * Set the stage that this controller controls
     *
     * @param stage the stage that this controller controls
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Get the choice that was selected
     * @return the selected choice
     */
    public SaveChoice getChoice() {
        return choice;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
    }
}