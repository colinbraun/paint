package paint.controller;

import javafx.fxml.FXML;
import javafx.stage.Stage;

public class LossyFilePopupController extends BaseController {

    private Stage theStage;
    int choice;
    public void setStage(Stage stage) {
        theStage = stage;
    }
    @FXML
    public void handleYes() {
        choice = 1;
        theStage.close();
    }

    @FXML
    public void handleNo() {
        choice = 0;
        theStage.close();
    }

    public int getChoice() {
        return choice;
    }
}
