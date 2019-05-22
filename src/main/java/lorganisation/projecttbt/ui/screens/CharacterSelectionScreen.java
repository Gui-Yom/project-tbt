package lorganisation.projecttbt.ui.screens;

import com.limelion.anscapes.Anscapes;
import lorganisation.projecttbt.AssetsManager;
import lorganisation.projecttbt.Game;
import lorganisation.projecttbt.TerminalGameInput;
import lorganisation.projecttbt.TerminalGameRenderer;
import lorganisation.projecttbt.player.AbstractPlayer;
import lorganisation.projecttbt.player.Bot;
import lorganisation.projecttbt.player.CharacterTemplate;
import lorganisation.projecttbt.ui.ImageButtonWidget;
import lorganisation.projecttbt.ui.InvisibleButton;
import lorganisation.projecttbt.ui.PlayerListWidget;
import lorganisation.projecttbt.ui.Screen;
import lorganisation.projecttbt.utils.Coords;
import lorganisation.projecttbt.utils.KeyUtils;
import lorganisation.projecttbt.utils.StyledString;
import lorganisation.projecttbt.utils.Utils;
import org.jline.terminal.Size;
import org.jline.terminal.Terminal;

public class CharacterSelectionScreen extends Screen {

    private Game associatedGame;

    private PlayerListWidget playerList;
    private boolean skip = false;
    private int maxCharacterCount;

    // FIXME structure, needs CharacterImages implementation
    public CharacterSelectionScreen(Game game) {

        super(game.getRenderer().getTerminal());

        Terminal terminal = game.getRenderer().getTerminal();


        this.associatedGame = game;
        this.focusKey = KeyUtils.KEY_RIGHT_ARROW;


        addComponent(new InvisibleButton(this::previousFocused, KeyUtils.KEY_LEFT_ARROW));
        addComponent(new InvisibleButton(this::nextFocused, KeyUtils.KEY_RIGHT_ARROW));

        playerList = new PlayerListWidget(new Coords(0, 0),
                                          new Size(terminal.getWidth() / 8, terminal.getWidth() - 2),
                                          Utils.Align.RIGHT,
                                          Utils.Align.LEFT,
                                          new StyledString("Joueurs"),
                                          Anscapes.Colors.BLUE_BRIGHT,
                                          Anscapes.Colors.WHITE_BRIGHT);
        addComponent(playerList);

        Size imageSize = new Size(terminal.getWidth() / 5, terminal.getHeight() / 8);

        int i = 0;
        for (String name : AssetsManager.gameCharacterNames()) {

            ImageButtonWidget characterBox = new ImageButtonWidget(new Coords(terminal.getHeight() / 10 + (imageSize.getRows() + terminal.getHeight() / 20) * i, imageSize.getColumns()), //FIXME flemme de faire les coordonnées et là c'est vrm nmptk
                                                                   imageSize,
                                                                   Utils.Align.LEFT,
                                                                   AssetsManager.getResource("images/logo.png").getPath(),
                                                                   true,
                                                                   new StyledString(name),
                                                                   () -> {
                                                                       AbstractPlayer p = associatedGame.getPlayers().current();
                                                                       if (!p.hasCharacter(name))
                                                                           p.addCharacter(CharacterTemplate.getCharacterTemplate(name).createCharacter());
                                                                   });
            addComponent(characterBox);

            ++i;
        }
    }

    private void botPick(Bot bot) {

        // simulate character pick
        for (int i = 0; i < maxCharacterCount; i++) {
            int rndId;

            do {
                rndId = (int) (AssetsManager.gameCharacterNames().size() * Math.random());

                int j = 0;
                for (String characterName : AssetsManager.gameCharacterNames())
                    if (j++ == rndId && !bot.hasCharacter(characterName))
                        bot.addCharacter(CharacterTemplate.getCharacterTemplate(characterName).createCharacter());
            }
            while (bot.getCharacters().size() <= maxCharacterCount);
        }
    }

    public void setMaxCharacterCount(int count) {

        this.maxCharacterCount = count;
    }

    @Override
    public void display(TerminalGameInput input, TerminalGameRenderer renderer) {


        AbstractPlayer current = associatedGame.getPlayers().current();

        while (!skip) {


            renderer.render(this);

            if (getFocusedWidget() instanceof ImageButtonWidget) {
                ImageButtonWidget imageButton = (ImageButtonWidget)getFocusedWidget();
                imageButton.onFocus(); // TODO draw contours
            }

            if (current instanceof Bot) {
                botPick((Bot) current);
                continue;
            }

            keyPressed(input.readKey());


            if (current.getCharacters().size() >= maxCharacterCount)
                current = associatedGame.getPlayers().next();
            else {
                skip = true;
                for (AbstractPlayer player : associatedGame.getPlayers())
                    skip = skip && player.getCharacters().size() == maxCharacterCount;
            }
        }

        associatedGame.getPlayers().reset();
    }
}
