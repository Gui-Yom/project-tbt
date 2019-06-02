package lorganisation.projecttbt.ui.widget;

import com.limelion.anscapes.Anscapes;
import lorganisation.projecttbt.player.AbstractPlayer;
import lorganisation.projecttbt.player.Character;
import lorganisation.projecttbt.utils.Coords;
import lorganisation.projecttbt.utils.Pair;
import lorganisation.projecttbt.utils.StyledString;
import lorganisation.projecttbt.utils.Utils;
import org.jline.terminal.Size;

import java.util.ArrayList;
import java.util.List;

public class CharacterListWidget extends PlayerListWidget {

    private Integer selected = 0;

    public CharacterListWidget(Coords coords, Size size, Utils.Align align, Utils.Align textAlign, StyledString title, Anscapes.Colors borderColor, Anscapes.Colors backgroundColor, List<AbstractPlayer> players) {

        super(coords, size, align, textAlign, title, borderColor, backgroundColor);
    }


    public void setSelected(int selected) {

        this.selected = selected;
    }

    public void updatePlayerList(List<AbstractPlayer> players) {

        List<StyledString> lines = new ArrayList<>();

        for (int i = 0; i < players.size(); i++) {
            AbstractPlayer player = players.get(i);

            for (int j = 0; j < player.getCharacters().size(); j++) {

                Character character = player.getCharacters().get(j);
                lines.add(new StyledString("  " + (selected != null && selected == i ? " > " : " ") + character.getType(), Pair.of(0, player.getColor().bg()), Pair.of(2, player.getColor().fg() + getBackgroundColor().bg())));

                lines.add(makeHealthBar(size.getColumns() - 4, (float) character.getHealth() / character.getMaxHealth(), Anscapes.Colors.MAGENTA_BRIGHT, Anscapes.Colors.CYAN_BRIGHT));
            }

            if (i != players.size() - 1)
                lines.add(new StyledString(" "));
        }

        setText(lines);
    }

    public StyledString makeHealthBar(int length, float percent, Anscapes.Colors missing, Anscapes.Colors left) {

        int leftCount = Math.round(length * percent);

        StyledString bar = new StyledString(Utils.repeatString(" ", length));
        bar.modifiers().put(0, left.bg());
        bar.modifiers().put(leftCount, missing.bg());

        return bar;
    }
}

/*

final render:

  G## MAGE
  --------_____

> R## ARCHER
  -----________

  G## KNIGHT
  -----------__
 */