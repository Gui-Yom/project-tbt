package lorganisation.projecttbt.ui.screen;

import com.limelion.anscapes.Anscapes;
import lorganisation.projecttbt.AssetsManager;
import lorganisation.projecttbt.Game;
import lorganisation.projecttbt.TerminalGameInput;
import lorganisation.projecttbt.TerminalGameRenderer;
import lorganisation.projecttbt.player.AbstractPlayer;
import lorganisation.projecttbt.player.Bot;
import lorganisation.projecttbt.player.Character;
import lorganisation.projecttbt.player.CharacterTemplate;
import lorganisation.projecttbt.ui.widget.*;
import lorganisation.projecttbt.utils.*;
import org.jline.terminal.Size;
import org.jline.terminal.Terminal;

import javax.swing.KeyStroke;

public class CharacterSelectionScreen extends Screen {

    private Game associatedGame;

    private PlayerListWidget playerList;
    private boolean skip = false;
    private int maxCharacterCount;

    public CharacterSelectionScreen(Game game, int maxCharacterCount) {

        super(game.getRenderer().getTerminal());

        this.maxCharacterCount = maxCharacterCount;

        Terminal terminal = game.getRenderer().getTerminal();


        this.associatedGame = game;

        Label mapName = new Label(new Coords(0, 2),
                                  new StyledString("Map - " + game.getMap().getName()),
                                  Utils.Align.CENTER);
        addComponent(mapName);

        Label desc = new Label(new Coords(0, 3),
                               new StyledString("Préparation de la partie - Personnages (" + maxCharacterCount + " max.)"),
                               Utils.Align.CENTER);
        addComponent(desc);


        addComponent(new InvisibleButton(this::prevFocus, KeyStroke.getKeyStroke('q')));
        addComponent(new InvisibleButton(this::nextFocus, KeyStroke.getKeyStroke('d')));

        playerList = new PlayerListWidget(new Coords(0, 1),
                                          new Size(terminal.getWidth() / 8, terminal.getHeight() - 2),
                                          Utils.Align.RIGHT,
                                          Utils.Align.LEFT,
                                          new StyledString("Joueurs"),
                                          Anscapes.Colors.BLUE_BRIGHT,
                                          Anscapes.Colors.WHITE_BRIGHT);
        addComponent(playerList);


        int i = 1;
        int charCount = AssetsManager.gameCharacterNames().size();
        int perLine = 3 + (int) Math.ceil(charCount / 10);

        float usableWidth = .85f * terminal.getWidth();
        float usableHeight = .9f * terminal.getHeight();

        int imageHeight = (int) (usableHeight / Math.ceil(charCount / (float) perLine));
        int imageWidth = (int) (usableWidth / Math.min(perLine, charCount));

        int imgSize = Math.min(imageHeight, imageWidth);

        int xSpace = (int) (usableWidth - Math.min(charCount, perLine) * imgSize) / ((Math.min(charCount, perLine)) + 1);

        int lineCount = (int) Math.ceil(charCount / (float) perLine);
        int ySpace = (int) (usableHeight - lineCount * imgSize / 2) / (lineCount + 1);

        Size imageSize = new Size(imgSize, imgSize);

        for (String name : AssetsManager.gameCharacterNames()) {

            int l = (int) Math.ceil(i / (float) perLine);
            int c = i - perLine * (l - 1);


            int xPos = xSpace + (c - 1) * (imgSize + xSpace);

            int yPos = 2 * terminal.getHeight() / 10 + (l - 1) * (ySpace + imgSize / 2);

            ImageButtonWidget characterBox = new ImageButtonWidget(new Coords(xPos, yPos),
                                                                   imageSize,
                                                                   Utils.Align.LEFT,
                                                                   "assets/characters/sprites/" + name + ".png",
                                                                   true,
                                                                   new StyledString(name.toUpperCase()),
                                                                   () -> {
                                                                       AbstractPlayer p = associatedGame.getPlayers().current();
                                                                       if (!p.hasCharacter(name)) {
                                                                           Character character = CharacterTemplate.getCharacterTemplate(name).createCharacter();
                                                                           associatedGame.getMap().getNextStartPos().setCharacter(character);
                                                                           p.addCharacter(character);
                                                                       }
                                                                   }, KeyUtils.KEY_ENTER);
            addComponent(characterBox);
            setFocused(characterBox);

            ++i;
        }
        nextFocus();

    }

    private void botPick(Bot bot) {

        // simulate character pick
        int rndId;

        do {
            rndId = (int) (AssetsManager.gameCharacterNames().size() * Math.random());

            int j = 0;
            for (String characterName : AssetsManager.gameCharacterNames()) {
                if (j == rndId && !bot.hasCharacter(characterName)) {
                    Character character = CharacterTemplate.getCharacterTemplate(characterName).createCharacter();
                    associatedGame.getMap().getNextStartPos().setCharacter(character);
                    bot.addCharacter(character);
                }


                ++j;
            }
        }
        while (bot.getCharacters().size() < maxCharacterCount);
    }

    @Override
    public void display(TerminalGameInput input, TerminalGameRenderer renderer) {

        AbstractPlayer current = associatedGame.getPlayers().current();

        while (!skip) {

            playerList.updatePlayerList(associatedGame.getPlayers());
            playerList.setSelected(associatedGame.getPlayers().indexOf(current));

            TerminalUtils.clearTerm();
            renderer.render(this);

            if (getFocusedWidget() instanceof ImageButtonWidget) {
                ImageButtonWidget imageButton = (ImageButtonWidget) getFocusedWidget();
                imageButton.onFocus();
            }

            for (Widget widget : getComponents())
                if (widget instanceof ImageButtonWidget) {
                    ImageButtonWidget imageButton = ((ImageButtonWidget) widget);
                    String characterType = imageButton.getText().rawText();

                    if (current.hasCharacter(characterType)) {
                        System.out.print(TerminalUtils.makeLine(imageButton.getCoords(), new Coords(imageButton.getCoords().getX() + imageButton.getSize().getColumns(), imageButton.getCoords().getY() + imageButton.getSize().getRows()), ' ', Anscapes.Colors.RED.bg()));
                        System.out.print(TerminalUtils.makeLine(imageButton.getCoords().getX() + imageButton.getSize().getColumns(), imageButton.getCoords().getY(), imageButton.getCoords().getX(), imageButton.getCoords().getY() + imageButton.getSize().getRows(), ' ', Anscapes.Colors.RED.bg()));
                    }

                }


            if (current instanceof Bot)
                botPick((Bot) current);
            else
                keyPressed(input.readKey());


            if (current.getCharacters().size() == maxCharacterCount) {
                current = associatedGame.getPlayers().next();

                skip = true;
                for (AbstractPlayer player : associatedGame.getPlayers())
                    skip = skip && player.getCharacters().size() == maxCharacterCount;
            }
        }

        associatedGame.getPlayers().reset();
    }
}
