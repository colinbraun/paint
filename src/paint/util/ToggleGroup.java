package paint.util;

import javafx.scene.control.ToggleButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A class to manage a collection of {@link ToggleButton}.
 * This will manage the untoggling of all other toggles when a button is toggled
 * @author Colin Braun
 */
public class ToggleGroup {

    /**
     * The list of {@link ToggleButton}s that this ToggleGroup controls
     */
    private List<ToggleButton> toggles;

    /**
     * Construct a ToggleGroup from a collection of toggles
     * @param toggles the toggles to be put in this ToggleGroup
     */
    public ToggleGroup(ToggleButton... toggles) {
        this.toggles = new ArrayList<>();
        this.toggles.addAll(Arrays.asList(toggles));
    }

    /**
     * Default constructor, no toggles by default, must be added with #addToggles()
     */
    public ToggleGroup() {
        toggles = new ArrayList<>();
    }

    /**
     * Add toggles to the ToggleGroup
     * @param toggles the toggles to be added
     */
    public void addToggles(ToggleButton... toggles) {
        this.toggles.addAll(Arrays.asList(toggles));
    }

    /**
     * Untoggle all of the {@link ToggleButton}s except the one that is passed as a parameter
     * @param toggleButton the button to leave toggled
     */
    public void unToggleAllBut(ToggleButton toggleButton) {
        for(ToggleButton toggle : toggles) {
            if(toggle != toggleButton)
                toggle.setSelected(false);
        }
    }

}
