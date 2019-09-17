package paint.popup;

import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import paint.controller.BaseController;

import java.io.IOException;

/**
 * A Popup class to simplify the window creation process
 * @param <T> The controller type used to manage this popup. Must extend {@link BaseController}
 */
public class Popup<T extends BaseController> extends Stage {

    protected T controller;

    // TODO: Implement this class where we are using Stages
    public Popup(String title, String fxmlFile) {
        super();
        setTitle(title);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/paint/fxml/" + fxmlFile));
        try {
            controller = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
