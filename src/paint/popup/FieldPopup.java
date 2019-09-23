package paint.popup;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.util.HashMap;
import java.util.Map;

/**
 * A Popup intended to simplify the construction of a simple enter values and submit window. Does not require FXML.
 */
public class FieldPopup extends Popup {

    /**
     * A reference to the root, stored as a {@link GridPane} for convenience
     */
    private GridPane gridRoot;
    private Map<Label, TextField> fields;
    /**
     * The index for the next item to be added to the popup
     */
    private int rowIndex;

    /**
     * Create a popup window that will be the size needed to fit the fields in it
     * @param title the title of the popup window
     */
    public FieldPopup(String title) {
        super(title);
        fields = new HashMap<>();
        this.root = new GridPane();
        this.gridRoot = (GridPane)root;
        setScene(new Scene(root));
        setResizable(false);
    }

    /**
     * Create a popup window with specific size
     * @param title the title of the popup window
     * @param width the width of the popup window
     * @param height the height of the popup window
     */
    public FieldPopup(String title, int width, int height) {
        this(title);
        setWidth(width);
        setHeight(height);
    }

    /**
     * Add a {@link TextField} to the popup
     * @param labelText the text of the label to the left of the field
     * @param defaultTextFieldValue the text of the initial field value
     * @return the added {@link TextField} (allows user to add listeners, etc.)
     */
    public TextField addField(String labelText, String defaultTextFieldValue) {
        Label label = new Label(labelText);
        TextField field = new TextField(defaultTextFieldValue);
        gridRoot.add(new HBox(label, field), 0, rowIndex);
        fields.put(label, field);
        rowIndex++;
        return field;
    }

    /**
     * Overload of the above, but no initial field value
     * @param labelText the text of the label to the left of the field
     * @return the added {@link TextField} (allows user to add listeners, etc.)
     */
    public TextField addField(String labelText) {
        return addField(labelText, "");
    }

    /**
     * Add a submit button to the popup. Note by default this submit WILL CLOSE THE WINDOW
     * @param buttonText the text of the submit button
     * @return the added {@link Button} (allows user to add listeners, etc.)
     */
    public Button addSubmitButton(String buttonText) {
        Button button = new Button(buttonText);
        gridRoot.add(button, 0, rowIndex);
        rowIndex++;
        button.setOnAction((event) -> close());
        return button;
    }

    /**
     * Add a generic controlled component to the window (in case this doesn't fit the typical needs)
     * @param control the control to be added
     * @return the added component back
     */
    public Control addComponent(Control control) {
        gridRoot.add(control, 0, rowIndex);
        rowIndex++;
        return control;
    }
}
