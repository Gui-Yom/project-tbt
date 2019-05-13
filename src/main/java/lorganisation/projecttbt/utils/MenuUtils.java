package lorganisation.projecttbt.utils;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.Label;

public class MenuUtils {

    public static Label getMainTitle(TerminalSize size, int y, String str) {

        Label title = new Label(str).setForegroundColor(TextColor.ANSI.RED).addStyle(SGR.BOLD).addStyle(SGR.UNDERLINE);
        title.setPosition(new TerminalPosition((size.getColumns() - title.getText().length()) / 2, y));
        title.setSize(new TerminalSize(title.getText().length(), 1));

        return title;
    }

    public static Label getMainTitle(TerminalSize size, int y) {

        return getMainTitle(size, y, "Project TBT");
    }

    public static Label makeSubtitle(TerminalSize size, int y, String text) {

        Label subtitle = new Label(text).setForegroundColor(TextColor.ANSI.YELLOW).addStyle(SGR.BOLD);
        subtitle.setPosition(new TerminalPosition((size.getColumns() - subtitle.getText().length()) / 2, y));
        subtitle.setSize(new TerminalSize(subtitle.getText().length(), 1));

        return subtitle;
    }

    public static Button makeButton(TerminalSize size, int x, int y, String text, Utils.Align alignement, Runnable runnable) {

        Button button = new Button(text, runnable);

        Coords buttonCoords = Utils.coordinatesOfAlignedObject(y, x, button.getLabel().length(), alignement, size.getColumns());

        button.setPosition(new TerminalPosition(buttonCoords.getX(), buttonCoords.getY()));
        button.setSize(new TerminalSize(button.getLabel().length(), 1));

        return button;

    }
}
