package lorganisation.projecttbt.ui;

import com.limelion.anscapes.Anscapes;
import lorganisation.projecttbt.utils.Coords;
import lorganisation.projecttbt.utils.StyledString;
import lorganisation.projecttbt.utils.Utils;
import org.jline.terminal.Terminal;

public class Text extends MenuComponent {

    protected StyledString stext;
    protected Utils.Align alignement;

    public Text(Coords coords, StyledString text, Utils.Align alignement) {

        this.coords = coords;
        this.stext = text;
        this.alignement = alignement;
    }

    public StyledString getStyledText() {

        return stext;
    }

    public String getText() {

        return stext.text();
    }

    public Utils.Align getAlignement() {

        return this.alignement;
    }

    @Override
    public String render(Terminal term) {
        //tu peux pas faire une formatted line ici psk il faut la width du terminal (pour qu'il puisse l'aligner)
        return Utils.formattedLine(coords.getX(), coords.getY(), stext, alignement, term.getWidth()) + Anscapes.RESET;
    }
}
