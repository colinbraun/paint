package paint.util;

import javafx.scene.control.ToggleButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A class to manage a collection of {@link ToggleButton}.
 * This will manage the untoggling of all other toggles when a button is toggled
 */
public class ToggleGroup {

    private List<ToggleButton> toggles;

    public ToggleGroup(ToggleButton... toggles) {
        this.toggles = new ArrayList<>();
        this.toggles.addAll(Arrays.asList(toggles));
    }

    public ToggleGroup() {
        toggles = new ArrayList<>();
    }

    public void addToggles(ToggleButton... toggles) {
        this.toggles.addAll(Arrays.asList(toggles));
    }

    public void unToggleAllBut(ToggleButton toggleButton) {
        for(ToggleButton toggle : toggles) {
            if(toggle != toggleButton)
                toggle.setSelected(false);
        }
    }

}
