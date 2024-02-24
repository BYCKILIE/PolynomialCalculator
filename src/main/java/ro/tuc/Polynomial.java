package ro.tuc;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Polynomial extends HashMap<Integer, Double> {

    public Polynomial() {}

    public Polynomial(Polynomial polynomial) {
        putAll(polynomial);
    }

    public Polynomial(String source) {
        String regex = "(([-+] ?)(([0-9]*)([.][0-9]+)?)?\\*?([A-Z]|[a-z])?\\^?(\\d+)?)";
        Pattern pattern = Pattern.compile(regex);

        if (source.charAt(0) != '-')
            source = '+' + source;
        Matcher matcher = pattern.matcher(source);

        while (matcher.find()) {
            int key;
            double value;

            if (matcher.group(6) == null) {
                key = 0;
                value = matcher.group(3).equals("") ? 0 : Double.parseDouble(matcher.group(3));
            } else {
                key = matcher.group(7) == null ? 1 : Integer.parseInt(matcher.group(7));
                value = matcher.group(3).equals("") ? 1 : Double.parseDouble(matcher.group(3));
            }
            if (matcher.group(2).equals("- ") | matcher.group(2).equals("-")) {
                value = -value;
            }
            if (containsKey(key))
                this.replace(key, get(key) + value);
            else
                this.put(key, value);
        }
        this.entrySet().removeIf(entry -> (entry.getValue().equals(0.0) | entry.getValue().equals(-0.0)));
    }

    @Override
    public String toString() {
        if (this.size() == 0)
            return "0";
        StringBuilder result = new StringBuilder();
        TreeMap<Integer, Double> sorted = new TreeMap<>(this);
        StringBuilder temp;

        for (Entry<Integer, Double> set : sorted.descendingMap().entrySet()) {
            temp = new StringBuilder(new Monomial(set).toString());
            if (temp.charAt(0) == '-') {
                temp.delete(0, 1);
                result.append(" - ");
            } else
                result.append(" + ");
            result.append(temp);
        }
        if (result.charAt(1) == '-') {
            result.deleteCharAt(0);
            result.deleteCharAt(1);
        } else
            result.delete(0, 3);

        return String.valueOf(result);
    }

}
