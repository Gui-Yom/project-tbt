package lorganisation.projecttbt.ui;

import com.limelion.anscapes.Anscapes;
import lorganisation.projecttbt.utils.Coords;
import lorganisation.projecttbt.utils.StyledString;
import lorganisation.projecttbt.utils.TerminalUtils;
import lorganisation.projecttbt.utils.Utils;
import org.jline.terminal.Size;
import org.jline.terminal.Terminal;

import java.util.List;

public class TextBoxWidget extends Widget {

    private List<StyledString> lines;
    private boolean visible = false;
    private Utils.Align align;
    private Utils.Align textAlign;
    private StyledString title;
    private Size size;
    private Anscapes.Colors backgroundColor, borderColor;


    public TextBoxWidget(Coords coords, Size size, Utils.Align align, Utils.Align textAlign, StyledString title, Anscapes.Colors borderColor, Anscapes.Colors backgroundColor, StyledString... lines) {

        super(coords);

        this.size = size;
        this.align = align;
        this.title = title;
        this.textAlign = textAlign;
        this.borderColor = borderColor;
        this.lines = Utils.arrayToList(lines);
        this.backgroundColor = backgroundColor;

        setFocusable(false);
        setEnabled(false);
        setVisible(true);
    }

    @Override
    public String paint(Terminal terminal) {

        StringBuilder visual = designList(coords, size, terminal.getSize(), title);

        return visual.append(Anscapes.RESET).toString();
    }

    public void toggle() {

        visible = !visible;
    }

    public boolean isVisible() {

        return this.visible;
    }

    public void setVisible(boolean visible) {

        this.visible = visible;
    }

    public void setBackgroundColor(Anscapes.Colors c) {

        this.backgroundColor = c;
    }

    public Anscapes.Colors getBackgroundColor() {

        return this.backgroundColor;
    }

    public void setText(List<StyledString> lines) {

        this.lines = lines;
    }

    private StringBuilder designList(Coords coords, Size boxSize, Size terminalSize, StyledString title) {

        int longestString = Utils.findLongestSequence(lines); // get longest line in list
        int minWidth = (longestString != 0 ? longestString : 10) + 4 /* bordersWidth = 1 & margin = 1 */; // determine minimum width of box to fit longest line

        if (minWidth > boxSize.getColumns())
            boxSize.setColumns(minWidth); // update size according to minimum width

        if (boxSize.getRows() <= lines.size() + 4) // if box is not high enough to fit all the lines
            boxSize.setRows(lines.size() + 4); // set Y size to lines.size() + 4 (2 for horizontal borders and 2 for margins)

        coords = TerminalUtils.coordinatesOfAlignedObject(coords.getY(), coords.getX(), boxSize.getColumns(), align, terminalSize.getColumns()); // determine coordinates

        if (title.length() > boxSize.getColumns()) // if title is longer than box is wide
            title.setText(title.subSequence(0, boxSize.getRows() - 3) + "."); // shorten string so as to make it like : TheLongestStringYouHaveEverWritten -> TheLongest. (also doesn't overlap the first and last column)

        return designList(coords, boxSize, title);
    }

    private StringBuilder designList(Coords coords, Size boxSize, StyledString title) {

        Coords titlePos = new Coords(coords.getX() + (boxSize.getColumns() - title.length()) / 2, coords.getY()); // determine title coordinates

        StringBuilder box = new StringBuilder(); // make builder (lots of String modifications are gonna happen)
        box.append(Anscapes.cursorPos(coords.getY(), coords.getX())) // place cursor at top left corner
           .append(borderColor.bg()) // apply borderColor (in background)
           .append(Utils.repeatString(" ", titlePos.getX() - coords.getX())) // fill with space to draw line until we reach title
           .append(title.toString()) // add title
           .append(Utils.repeatString(" ", coords.getX() + boxSize.getColumns() - titlePos.getX() - title.length())) // finish line
           .append(Anscapes.moveDown(1)).append(Anscapes.moveLeft(boxSize.getColumns())) // go down and back to line start
           .append(" ").append(backgroundColor.bg()).append(Utils.repeatString(" ", boxSize.getColumns() - 2)).append(borderColor.bg()).append(" "); // add margin (empty line)

        for (int i = 0; i < lines.size(); i++) { // for each line (no borders & margin) (i = 2 -> skip border & margin)
            StyledString line = lines.get(i); // get line string
            Coords lineCoords = TerminalUtils.coordinatesOfAlignedObject(0, align == Utils.Align.CENTER ? 0 : 1, line.length(), textAlign, boxSize.getColumns() - 2); // get line offset to align it
            box.append(Anscapes.cursorPos(coords.getY() + i + 2, coords.getX())) // set cursor pos at row start
               .append(" ").append(backgroundColor.bg()) // border pixel
               .append(Utils.repeatString(" ", lineCoords.getX())) // add space before line
               .append(line.toString()) // add line content
               .append(backgroundColor.bg())
               .append(Utils.repeatString(" ", boxSize.getColumns() - lineCoords.getX() - line.length() - 2)) // print line and fill rest with spaces
               .append(borderColor.bg()).append(" "); // right border
        }

        for (int i = lines.size(); i < boxSize.getRows() - 4; i++) {
            box.append(Anscapes.cursorPos(coords.getY() + i + 2, coords.getX())) // set cursor pos at row start
               .append(" ").append(backgroundColor.bg()).append(Utils.repeatString(" ", boxSize.getColumns() - 2)) // print line and fill rest with spaces
               .append(borderColor.bg()).append(" "); // right border
        }

        box.append(Anscapes.moveDown(1)).append(Anscapes.moveLeft(boxSize.getColumns())) // go down and back to line start
           .append(" ").append(backgroundColor.bg()).append(Utils.repeatString(" ", boxSize.getColumns() - 2)).append(borderColor.bg()).append(" ") // add margin (empty line)
           .append(Anscapes.cursorPos(coords.getY() + boxSize.getRows() - 1, coords.getX()))
           .append(Utils.repeatString(" ", boxSize.getColumns()));

        return box.append(Anscapes.RESET);
    }

}
