package lorganisation.projecttbt.ui;

import java.util.ArrayList;
import java.util.List;

public class Menu {

    private List<MenuComponent> components;

    public Menu() {

        components = new ArrayList<>();
    }

    public List<MenuComponent> getComponents() {

        return components;
    }
}
