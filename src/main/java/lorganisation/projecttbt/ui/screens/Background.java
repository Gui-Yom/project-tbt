package lorganisation.projecttbt.ui.screens;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.AbstractComponent;
import com.googlecode.lanterna.gui2.ComponentRenderer;
import com.googlecode.lanterna.gui2.TextGUIGraphics;
import com.limelion.anscapes.ImgConverter;
import lorganisation.projecttbt.AssetsManager;
import lorganisation.projecttbt.IntegratedDevenv;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class Background extends AbstractComponent<Background> {

    @Override
    protected ComponentRenderer<Background> createDefaultRenderer() {

        return new ComponentRenderer<Background>() {
            @Override
            public TerminalSize getPreferredSize(Background component) {

                return null;
            }

            @Override
            public void drawComponent(TextGUIGraphics graphics, Background component) {

                try {
                    String image = IntegratedDevenv.convert(ImageIO.read(AssetsManager.openResource("assets/images/logo.png")),
                                                            ImgConverter.Mode.RGB,
                                                            5);

                    AtomicInteger i = new AtomicInteger();

                    Arrays.stream(image.split("\\r?\\n"))
                          .forEach(line -> graphics.putCSIStyledString(0, i.getAndIncrement(), line));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
    }
}
