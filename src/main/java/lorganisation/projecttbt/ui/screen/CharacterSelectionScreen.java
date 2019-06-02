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

import javax.swing.KeyStroke;
import java.util.ArrayList;
import java.util.List;

public class CharacterSelectionScreen extends Screen {

    private Game game = Game.getInstance();

    private PlayerListWidget playerListW;
    private boolean skip = false;
    private int characterPerPlayer;

    public CharacterSelectionScreen(int characterPerPlayer) {

        super(Game.getInstance().getRenderer().getTerminal());

        this.characterPerPlayer = characterPerPlayer;

        Size termSize = game.getInput().getTerminal().getSize();

        addComponent(TerminalUtils.getTitle());

        Label mapName = new Label(new Coords(0, 2),
                                  new StyledString("Map - " + game.getMap().getName()),
                                  Utils.Align.CENTER);
        addComponent(mapName);

        Label desc = new Label(new Coords(0, 3),
                               new StyledString("Préparation de la partie - Personnages (" + characterPerPlayer + " max.)"),
                               Utils.Align.CENTER);
        addComponent(desc);

        addComponent(new InvisibleButton(this::prevFocus, KeyStroke.getKeyStroke('q'), "'q' to cycle focus backward"));
        addComponent(new InvisibleButton(this::nextFocus, KeyStroke.getKeyStroke('d'), "'d' to cycle focus forward"));

        // La liste de joueurs sur la droite
        playerListW = new PlayerListWidget(new Coords(0, 1),
                                           new Size(termSize.getColumns() / 8, termSize.getRows() - 2),
                                           Utils.Align.RIGHT,
                                           Utils.Align.LEFT,
                                           new StyledString("Joueurs"),
                                           Anscapes.Colors.BLUE_BRIGHT,
                                           Anscapes.Colors.WHITE_BRIGHT);
        playerListW.setSelected(0);
        addComponent(playerListW);

        // Placement des images
        int i = 1;
        int charCount = AssetsManager.gameCharacterNames().size();
        int perLine = 3 + (int) Math.ceil(charCount / 10);

        float usableWidth = .85f * termSize.getColumns();
        float usableHeight = .9f * termSize.getRows();

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

            int yPos = 2 * termSize.getRows() / 10 + (l - 1) * (ySpace + imgSize / 2);

            String imagePath = "assets/images/characters/dqzdqd_missing_dqzdqd1.jpg";
            if (AssetsManager.getResource("assets/images/characters/" + name + ".png") != null)
                imagePath = "assets/images/characters/" + name + ".png";

            ImageButtonWidget characterBox = new ImageButtonWidget(new Coords(xPos, yPos),
                                                                   imageSize,
                                                                   Utils.Align.LEFT,
                                                                   imagePath,
                                                                   true,
                                                                   new StyledString(name.toUpperCase()),
                                                                   () -> {
                                                                       AbstractPlayer p = game.getPlayers().current();
                                                                       if (!p.hasCharacter(name)) {
                                                                           Character character = new Character(CharacterTemplate.getCharacterTemplate(name), p);
                                                                           character.setPos(game.getMap().getStartPos().next());
                                                                           p.addCharacter(character);
                                                                       }
                                                                   }, KeyUtils.KEY_ENTER);
            addComponent(characterBox);
            setFocused(characterBox);

            ++i;
        }
        nextFocus();

    }

    @Override
    public void display(TerminalGameInput input, TerminalGameRenderer renderer) {

        game.getPlayers().reset();

        AbstractPlayer current = game.getPlayers().next();

        while (!skip) {

            playerListW.updatePlayerList(game.getPlayers());

            TerminalUtils.clearTerm();
            renderer.render(this);

            for (Widget widget : getComponents())
                if (widget instanceof ImageButtonWidget) {
                    ImageButtonWidget imageButton = ((ImageButtonWidget) widget);
                    String characterType = imageButton.getText().rawText();

                    if (current.hasCharacter(characterType)) {
                        System.out.print(TerminalUtils.makeLine(imageButton.getCoords(),
                                                                new Coords(imageButton.getCoords().getX() + imageButton.getSize().getColumns(), imageButton.getCoords().getY() + imageButton.getSize().getRows()),
                                                                ' ',
                                                                Anscapes.Colors.RED.bg()));
                        System.out.print(TerminalUtils.makeLine(imageButton.getCoords().getX() + imageButton.getSize().getColumns(),
                                                                imageButton.getCoords().getY(),
                                                                imageButton.getCoords().getX(),
                                                                imageButton.getCoords().getY() + imageButton.getSize().getRows(),
                                                                ' ',
                                                                Anscapes.Colors.RED.bg()));
                    }
                }

            if (getFocusedWidget() instanceof ImageButtonWidget) {
                ImageButtonWidget imageButton = (ImageButtonWidget) getFocusedWidget();
                imageButton.onFocus();
            }

            if (current instanceof Bot) {
                List<String> tempList = new ArrayList<>(AssetsManager.gameCharacterNames());
                ((Bot) current).pickCharacters(tempList, characterPerPlayer);
            } else {
                keyPressed(input.readKey());
            }

            if (current.getCharacters().size() == characterPerPlayer) {
                current = game.getPlayers().next();

                playerListW.setSelected(game.getPlayers().getIndex());

                skip = true;
                for (AbstractPlayer player : game.getPlayers())
                    skip = skip && player.getCharacters().size() == characterPerPlayer;
            }
        }

        game.getPlayers().reset();
    }
}
