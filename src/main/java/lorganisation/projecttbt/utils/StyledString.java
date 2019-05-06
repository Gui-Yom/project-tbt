package lorganisation.projecttbt.utils;

import com.limelion.anscapes.Anscapes;

import java.util.Map;
import java.util.TreeMap;

public class StyledString implements CharSequence {

    private Map<Integer, String> modifiers = null;
    private String text;

    @SafeVarargs
    public StyledString(String text, Pair<Integer, String>... modifiers) {

        this.text = text;
        if (modifiers != null && modifiers.length != 0)
            this.modifiers = new TreeMap<>(Utils.pairArrayToMap(modifiers));
    }

    public StyledString(String text, Map<Integer, String> modifiers, Pair<Integer, String>... otherModifiers) {

        this.text = text;

        for (Pair<Integer, String> modifier : otherModifiers)
            modifiers.put(modifier.getU(), modifier.getV());
        this.modifiers = modifiers;
    }

    public String text() {

        return text;
    }

    public void setText(String string) {
        this.text = string;
    }

    public Map<Integer, String> modifiers() {

        return modifiers;
    }

    @Override
    public int length() {

        return text.length();
    }

    @Override
    public char charAt(int index) {

        return text.charAt(index);
    }

    @Override
    public CharSequence subSequence(int start, int end) {

        return text.subSequence(start, end);
    }

    @Override
    public String toString() {

        if (modifiers != null && modifiers.size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < text.length(); ++i) {

                if (modifiers.containsKey(i))
                    sb.append(modifiers.get(i));

                sb.append(text.charAt(i));
            }

            sb.append(Anscapes.RESET);

            return sb.toString();
        } else
            return text;
    }

}
