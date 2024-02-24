package ro.tuc;

import java.util.Map;

public class Monomial {
    private final Map.Entry<Integer, Double> set;

    public Monomial(Map.Entry<Integer, Double> set) {
        this.set = set;
    }

    @Override
    public String toString() {
        StringBuilder val = new StringBuilder(String.valueOf(set.getValue()));
        String symbol = Entry.getSymbol();

        while (val.charAt(val.length() - 1) == '0')
            val.deleteCharAt(val.length() - 1);
        if (val.charAt(val.length() - 1) == '.')
            val.deleteCharAt(val.length() - 1);
        if (set.getKey().equals(0))
            return val.toString();
        else if (set.getKey().equals(1) & set.getValue().equals(1.0))
            return symbol;
        else if (set.getKey().equals(1))
            return val + symbol;
        else if (!set.getKey().equals(1) & set.getValue().equals(1.0))
            return symbol + '^' + set.getKey();

        return val + symbol + '^' + set.getKey();
    }
}
