package lorganisation.projecttbt.ui.screen;

import com.limelion.anscapes.Anscapes;
import com.limelion.anscapes.ColorMode;
import lorganisation.projecttbt.*;
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
public class WinnerScreen extends Screen {

    public WinnerScreen() {

        super(Game.getInstance().getRenderer().getTerminal());

        Terminal terminal = Game.getInstance().getRenderer().getTerminal();

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

        if (Game.getInstance().getPlayers().size() > 0)
            winner = Game.getInstance().getPlayers().get(0);

        if (winner != null) {
            TextBoxWidget boxWidget = new TextBoxWidget(new Coords(0, terminal.getHeight() * 6 / 10),
                                                        new Size((int) (terminal.getWidth() * .5f), (int) (terminal.getHeight() * .1f)),
                                                        Utils.Align.CENTER,
                                                        Utils.Align.CENTER,
                                                        new StyledString("LOOKS LIKE WE'VE FOUND OURSELVES A WINNER", Pair.of(0, Anscapes.Colors.RED.fg())),
                                                        Anscapes.Colors.BLUE_BRIGHT,
                                                        Anscapes.Colors.RED_BRIGHT,
                                                        new StyledString(winner.getName().toUpperCase() + " JUST WON THIS GAME, YEEEHAAWWW"),
                                                        new StyledString(winner.getName().toUpperCase() + " we are lazy bastards, if u wanna play again just restart the game"));

            addComponent(boxWidget);
        }

        addComponent(new InvisibleButton(Game::shutdownGracefully, KeyUtils.KEY_ESCAPE, "ESC to exit"));
    }

    @Override
    public void display(TerminalGameInput input, TerminalGameRenderer renderer) {

        while (true) {

            renderer.render(this);

            keyPressed(input.readKey());
        }
    }
}
