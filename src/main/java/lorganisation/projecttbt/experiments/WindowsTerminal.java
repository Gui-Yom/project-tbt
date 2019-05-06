package lorganisation.projecttbt.experiments;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.InputDecoder;
import com.googlecode.lanterna.input.KeyDecodingProfile;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.terminal.MouseCaptureMode;
import com.googlecode.lanterna.terminal.TerminalResizeListener;
import com.googlecode.lanterna.terminal.ansi.ANSITerminal;
import org.jline.terminal.Size;
import org.jline.terminal.Terminal;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

public class WindowsTerminal extends ANSITerminal {

    private Terminal term;

    public WindowsTerminal(Terminal term) {

        super(term.input(), term.output(), term.encoding());
        this.term = term;
    }

    @Override
    public void addResizeListener(TerminalResizeListener listener) {

        super.addResizeListener(listener);
    }

    @Override
    public void removeResizeListener(TerminalResizeListener listener) {

        super.removeResizeListener(listener);
    }

    @Override
    protected synchronized void onResized(int columns, int rows) {

        super.onResized(columns, rows);
    }

    @Override
    protected synchronized void onResized(TerminalSize newSize) {

        super.onResized(newSize);
    }

    @Override
    public TextGraphics newTextGraphics() throws IOException {

        return super.newTextGraphics();
    }

    @Override
    public void putCharacter(char c) throws IOException {

        term.output().write(c);
    }

    @Override
    protected void writeToTerminal(byte... bytes) throws IOException {

        term.output().write(bytes);
    }

    @Override
    public byte[] enquireTerminal(int timeout, TimeUnit timeoutTimeUnit) throws IOException {

        return super.enquireTerminal(timeout, timeoutTimeUnit);
    }

    @Override
    public void bell() throws IOException {

        // TODO handle bell
        putCharacter((char) 0x7);
    }

    @Override
    public InputDecoder getInputDecoder() {

        return super.getInputDecoder();
    }

    @Override
    public void flush() throws IOException {

        term.flush();
    }

    @Override
    protected Charset getCharset() {

        return super.getCharset();
    }

    @Override
    protected byte[] translateCharacter(char input) {

        return super.translateCharacter(input);
    }

    @Override
    protected KeyDecodingProfile getDefaultKeyDecodingProfile() {

        return super.getDefaultKeyDecodingProfile();
    }

    @Override
    protected TerminalSize findTerminalSize() throws IOException {

        Size size = term.getSize();
        return new TerminalSize(size.getColumns(), size.getRows());
    }

    @Override
    public void setTerminalSize(int columns, int rows) throws IOException {

        term.setSize(new Size(columns, rows));
    }

    @Override
    public void setTitle(String title) throws IOException {

        super.setTitle(title);
    }

    @Override
    public void setForegroundColor(TextColor color) throws IOException {

        super.setForegroundColor(color);
    }

    @Override
    public void setBackgroundColor(TextColor color) throws IOException {

        super.setBackgroundColor(color);
    }

    @Override
    public void enableSGR(SGR sgr) throws IOException {

        super.enableSGR(sgr);
    }

    @Override
    public void disableSGR(SGR sgr) throws IOException {

        super.disableSGR(sgr);
    }

    @Override
    public void resetColorAndSGR() throws IOException {

        super.resetColorAndSGR();
    }

    @Override
    public void clearScreen() throws IOException {

        super.clearScreen();
    }

    @Override
    public void enterPrivateMode() throws IOException {

        super.enterPrivateMode();
    }

    @Override
    public void exitPrivateMode() throws IOException {

        super.exitPrivateMode();
    }

    @Override
    public void close() throws IOException {

        super.close();
    }

    @Override
    public void setCursorPosition(int x, int y) throws IOException {

        super.setCursorPosition(x, y);
    }

    @Override
    public synchronized TerminalPosition getCursorPosition() throws IOException {

        return super.getCursorPosition();
    }

    @Override
    public void setCursorPosition(TerminalPosition position) throws IOException {

        super.setCursorPosition(position);
    }

    @Override
    public void setCursorVisible(boolean visible) throws IOException {

        super.setCursorVisible(visible);
    }

    @Override
    public KeyStroke readInput() throws IOException {

        return super.readInput();
    }

    @Override
    public KeyStroke pollInput() throws IOException {

        return super.pollInput();
    }

    @Override
    public void pushTitle() throws IOException {

        super.pushTitle();
    }

    @Override
    public void popTitle() throws IOException {

        super.popTitle();
    }

    @Override
    public void iconify() throws IOException {

        super.iconify();
    }

    @Override
    public void deiconify() throws IOException {

        super.deiconify();
    }

    @Override
    public void maximize() throws IOException {

        super.maximize();
    }

    @Override
    public void unmaximize() throws IOException {

        super.unmaximize();
    }

    @Override
    public void setMouseCaptureMode(MouseCaptureMode mouseCaptureMode) throws IOException {

        super.setMouseCaptureMode(mouseCaptureMode);
    }

    @Override
    public void scrollLines(int firstLine, int lastLine, int distance) throws IOException {

        super.scrollLines(firstLine, lastLine, distance);
    }
}
