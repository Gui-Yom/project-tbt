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
        else
            this.modifiers = new TreeMap<>();
    }

    @SafeVarargs
    public StyledString(String text, Map<Integer, String> modifiers, Pair<Integer, String>... otherModifiers) {

        this.text = text;

        for (Pair<Integer, String> modifier : otherModifiers)
            modifiers.put(modifier.getU(), modifier.getV());
        this.modifiers = modifiers;
    }

    public String rawText() {

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

    /**
     * end = -1 gives full size and all modifiers
     **/
    public String subStringWithFormat(int start, int end) {

        if (modifiers != null && modifiers.size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (int i = start; i < (end == -1 ? length() : end); ++i) {

                if (modifiers.containsKey(i))
                    sb.append(modifiers.get(i));

                sb.append(text.charAt(i));
            }

            int max = Utils.max(modifiers.keySet());
            if (max >= text.length() && end == -1) {
                for (int i = text.length(); i <= max; i++) {
                    sb.append(modifiers.get(i));
                }
            }

            sb.append(Anscapes.RESET);

            return sb.toString();
        } else
            return text;
    }

    @Override
    public String toString() {

        return subStringWithFormat(0, -1);
    }

}
