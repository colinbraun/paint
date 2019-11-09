package paint.controller;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;

/**
 * A class to control the About Window
 * @author Colin Braun
 */
public class AboutController extends BaseController {

    /**
     * The content of this controller. Will contain text elements.
     */
    @FXML private VBox content;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Initializing " + this.getClass());
        List<String> toAdd = new ArrayList<>();
        try {
            URL url = getClass().getResource("release_notes.txt");
            Scanner scanner = new Scanner(new File(url.getPath()));
            while(scanner.hasNext()) {
                toAdd.add(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        for(String str : toAdd) {
            content.getChildren().add(new Text(str));
        }
    }
}
