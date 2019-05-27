package lorganisation.projecttbt.ui.widget;

import com.limelion.anscapes.Anscapes;
import lorganisation.projecttbt.player.AbstractPlayer;
import lorganisation.projecttbt.utils.Coords;
import lorganisation.projecttbt.utils.Pair;
import lorganisation.projecttbt.utils.StyledString;
import lorganisation.projecttbt.utils.Utils;
import org.jline.terminal.Size;

import java.util.ArrayList;
import java.util.List;

public class PlayerListWidget extends TextBoxWidget {

    private Integer selected;

    public PlayerListWidget(Coords coords, Size size, Utils.Align align, Utils.Align textAlign, StyledString title, Anscapes.Colors borderColor, Anscapes.Colors backgroundColor, StyledString... lines) {

        super(coords, size, align, textAlign, title, borderColor, backgroundColor, lines);


        setEnabled(false);
        setFocusable(false);
        setVisible(true);
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }

    public void updatePlayerList(List<AbstractPlayer> players) {
        List<StyledString> playerList = new ArrayList<>();

        for(int i = 0; i < players.size(); i++) {
            AbstractPlayer player = players.get(i);

            playerList.add(new StyledString("  " + (selected != null && selected == i ? " > ":" ") + player.getName(), Pair.of(0, player.getColor().bg()), Pair.of(2, Anscapes.Colors.BLACK.fg() + getBackgroundColor().bg())));

            if( i != players.size() - 1)
                playerList.add(new StyledString(" "));
        }

        setText(playerList);
    }
}
