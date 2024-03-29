package lorganisation.projecttbt.ui.screen;

import com.limelion.anscapes.Anscapes;
import com.limelion.anscapes.ColorMode;
import lorganisation.projecttbt.AssetsManager;
import lorganisation.projecttbt.Game;
import lorganisation.projecttbt.IntegratedDevenv;
import lorganisation.projecttbt.player.AbstractPlayer;
import lorganisation.projecttbt.ui.widget.ImageWidget;
import lorganisation.projecttbt.ui.widget.InvisibleButton;
import lorganisation.projecttbt.ui.widget.TextBoxWidget;
import lorganisation.projecttbt.utils.*;
import org.jline.terminal.Size;
import org.jline.terminal.Terminal;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

//TODO Add some stats about the guy when we'll only have that left to do.

/**
 * L'écran de fin lorsque qu'un joueur a gagné la partie
 */
public class WinnerScreen extends Screen {

    public WinnerScreen(Game game) {

        super(game.getRenderer().getTerminal());

        Terminal terminal = game.getRenderer().getTerminal();

        try {

            BufferedImage image = ImageIO.read(AssetsManager.openResource("assets/images/logo.png"));

            ImageWidget background = new ImageWidget(new Coords(0, 0),
                                                     IntegratedDevenv.convert(image,
                                                                              ColorMode.RGB,
                                                                              (float) 130 / 800));

            addComponent(background);
        } catch (IOException e) {
            e.printStackTrace();
        }


        AbstractPlayer winner = null;

        if (game.getPlayers().size() > 0)
            winner = game.getPlayers().get(0);

        if (winner != null) {
            TextBoxWidget boxWidget = new TextBoxWidget(new Coords(0, terminal.getHeight() * 6 / 10),
                                                        new Size((int) (terminal.getWidth() * .5f), (int) (terminal.getHeight() * .1f)),
                                                        Utils.Align.CENTER,
                                                        Utils.Align.CENTER,
                                                        new StyledString("LOOKS LIKE WE'VE FOUND OURSELVES A WINNER", Pair.of(0, Anscapes.Colors.RED.fg())),
                                                        Anscapes.Colors.BLUE_BRIGHT,
                                                        Anscapes.Colors.RED_BRIGHT,
                                                        new StyledString(winner.getName().toUpperCase() + " JUST WON THIS GAME, YEEEHAAWWW"),
                                                        new StyledString(winner.getName().toUpperCase() + " Restart the game to play another round :D"));

            addComponent(boxWidget);
        }

        addComponent(new InvisibleButton(Game::shutdownGracefully, KeyUtils.KEY_ESCAPE, "ESC to exit"));
    }

    @Override
    public void display(Game game) {

        while (true) {

            game.getRenderer().render(this);

            keyPressed(game.getInput().readKey());
        }
    }
}
