package paint.popup;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import paint.controller.BaseController;

import java.io.IOException;

/**
 * A Popup class to simplify the window creation process
 * @param <T> The controller type used to manage this popup. Should extend {@link BaseController}
 */
public abstract class Popup<T> extends Stage {

    protected T controller;
    protected Parent root;

    // TODO: Implement this class where we are using Stages

    /**
     * Construct a popup from an fxml file
     * @param title the title of the popup window
     * @param fxmlFile the file to load from
     */
    public Popup(String title, String fxmlFile) {
        super();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/paint/fxml/" + fxmlFile));
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        controller = fxmlLoader.getController();
        this.setScene(new  Scene(root));
        setTitle(title);
    }

    /**
     * Construct a popup without using an fxml file. Requires the root to be initialized by the parent
     * @param title
     */
    public Popup(String title) {
        this.setTitle(title);
    }
}
