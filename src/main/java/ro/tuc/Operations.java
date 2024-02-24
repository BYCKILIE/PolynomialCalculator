package ro.tuc;

import java.util.*;

public class Operations {
    public static Polynomial addition(Polynomial pol1, Polynomial pol2) {
        return utilAddSub(pol1, pol2, Operator.ADD);
    }
    public static Polynomial subtraction(Polynomial pol1, Polynomial pol2) {
        return utilAddSub(pol1, pol2, Operator.SUBTRACT);
    }

    private static Polynomial utilAddSub(Polynomial pol1, Polynomial pol2, Operator op) {
        Polynomial result = new Polynomial(pol1);

        for (int key : pol2.keySet()) {
            if (result.containsKey(key)) {
                result.replace(key, op.apply(result.get(key),pol2.get(key)));
            }
            else {
                result.put(key, op.apply(0, pol2.get(key)));
            }
        }
        result.entrySet().removeIf(entry -> (entry.getValue().equals(0.0) | entry.getValue().equals(-0.0)));

        return result;
    }

    private enum Operator {
        ADD() {
            @Override public double apply(double x1, double x2) {
                return x1 + x2;
            }
        }, SUBTRACT() {
            @Override public double apply(double x1, double x2) {
                return x1 - x2;
            }
        };
        abstract double apply(double x1, double x2);
    }

    public static Polynomial multiplication(Polynomial pol1, Polynomial pol2) {
        Polynomial polynomial = new Polynomial();

        for (int key1 : pol1.keySet()) {
            for (int key2 : pol2.keySet()) {
                Integer key = key1 + key2;
                Double value = pol1.get(key1) * pol2.get(key2);
                if (polynomial.containsKey(key)) {
                    polynomial.replace(key, polynomial.get(key) + value);
                } else {
                    polynomial.put(key, value);
                }
            }
        }
        polynomial.entrySet().removeIf(entry -> (entry.getValue().equals(0.0) | entry.getValue().equals(-0.0)));

        return polynomial;
    }

    public static String division(Polynomial pol1, Polynomial pol2) {
        if (pol2.size() == 0) {
            return "NaN";
        } else if (pol1.size() == 0) {
            return "0";
        }

        if (higherGradeSecond(pol1, pol2)) {
            return division(pol2, pol1);
        }
        Polynomial result = new Polynomial();

        if (pol1.size() == 1 & pol2.size() == 1) {
            if (degree(pol1) == 0 & degree(pol2) == 0) {
                result.put(0, leadingTerm(pol1) / leadingTerm(pol2));
                return result.toString();
            }
        }
        Polynomial util = new Polynomial(pol1);

        while (!util.isEmpty() & degree(util) >= degree(pol2)) {
            double co = leadingTerm(util) / leadingTerm(pol2);
            int exp = degree(util) - degree(pol2);
            result.put(exp, co);
            Polynomial temp = new Polynomial();
            for (int i : pol2.keySet()) {
                temp.put(i + exp, co * pol2.get(i));
            }
            util = subtraction(util, temp);
        }

        return result + ", R: " + util;
    }

    private static boolean higherGradeSecond(Polynomial pol1, Polynomial pol2) {
        return pol1.keySet().stream().max(Integer::compare).orElse(0)
                < pol2.keySet().stream().max(Integer::compare).orElse(0);
    }

    private static int degree(HashMap<Integer, Double> pol) {
        return pol.keySet().stream().max(Integer::compare).orElse(0);
    }

    private static double leadingTerm(HashMap<Integer, Double> pol) {
        Double util = pol.get(degree(pol));
        return util != null ? util : 0;
    }

    public static Polynomial derivative(Polynomial pol) {
        Polynomial result = new Polynomial();

        pol.forEach((key, value) -> result.put(key - 1, value * key));
        result.entrySet().removeIf(entry -> (entry.getValue().equals(0.0) | entry.getValue().equals(-0.0)));

        return result;
    }

    public static Polynomial integration(Polynomial pol) {
        Polynomial result = new Polynomial();

        pol.forEach((key, value) -> result.put(key + 1, value / (key + 1)));
        result.entrySet().removeIf(entry -> (entry.getValue().equals(0.0) | entry.getValue().equals(-0.0)));

        return result;
    }

}
