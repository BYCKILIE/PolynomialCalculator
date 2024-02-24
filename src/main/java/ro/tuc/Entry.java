package ro.tuc;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Clipboard;
import java.awt.event.KeyEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Entry extends JPanel implements KeyEventDispatcher {
    private static final JTextField[] entries = new JTextField[3];
    private static boolean correct;
    private static String symbol;


    public Entry() {
        JPanel actionPanel = new JPanel();
        JPanel typePanel = new JPanel();
        FlowLayout layout = new FlowLayout();
        layout.setVgap(3);
        layout.setHgap(0);

        FlowLayout utilLayout = new FlowLayout(FlowLayout.RIGHT);
        utilLayout.setVgap(0);

        JPanel[] utilPanels = new JPanel[3];

        typePanel.setLayout(new FlowLayout());
        actionPanel.setLayout(layout);

        typePanel.setPreferredSize(new Dimension(80, 90));
        actionPanel.setPreferredSize(new Dimension(320, 90));

        this.setLayout(layout);

        JLabel[] types = new JLabel[3];
        String[] util = new String[]{"First:", "Second:", "Result:"};

        for (int i = 0; i < 3; i++) {
            utilPanels[i] = new JPanel();
            utilPanels[i].setLayout(utilLayout);
            utilPanels[i].setPreferredSize(new Dimension(80, 25));

            types[i] = new JLabel(util[i]);
            types[i].setFont(new Font("arial", Font.BOLD, 17));
            utilPanels[i].add(types[i]);
            typePanel.add(utilPanels[i]);

            entries[i] = new JTextField();
            entries[i].setPreferredSize(new Dimension(320, 27));
            entries[i].setFont(new Font("arial", Font.PLAIN, 14));
            actionPanel.add(entries[i]);
        }
        entries[2].setEditable(false);
        entries[2].setBackground(Color.WHITE);
        entries[2].setBorder(BorderFactory.createLineBorder(Color.BLACK));

        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(this);
        this.add(typePanel);
        this.add(actionPanel);
    }

    public static String getSymbol() {
        return symbol;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent e) {
        highlight();
        return false;
    }

    public static void executeAddition() {
        if (verify(2)) {
            entries[2].setText(' ' + Operations.addition(new Polynomial(entries[0].getText()),
                    new Polynomial(entries[1].getText())).toString());
        }
    }

    public static void executeSubtraction() {
        if (verify(2)) {
            entries[2].setText(' ' + Operations.subtraction(new Polynomial(entries[0].getText()),
                    new Polynomial(entries[1].getText())).toString());
        }
    }

    public static void executeMultiplication() {
        if (verify(2)) {
            entries[2].setText(' ' + Operations.multiplication(new Polynomial(entries[0].getText()),
                    new Polynomial(entries[1].getText())).toString());
        }
    }

    public static void executeDivision() {
        if (verify(2)) {
            entries[2].setText(' ' + Operations.division(new Polynomial(entries[0].getText()),
                    new Polynomial(entries[1].getText())));
        }
    }

    public static void executeDerivative() {
        if (verify(1)) {
            entries[1].setText("");
            entries[2].setText(' ' + Operations.derivative(new Polynomial(entries[0].getText())).toString());
        }
    }

    public static void executeIntegration() {
        if (verify(1)) {
            entries[1].setText("");
            entries[2].setText(' ' + Operations.integration(new Polynomial(entries[0].getText())).toString() + " + C");
        }
    }

    public static void executeUse() {
        if (verify(3)) {
            StringBuilder builder = new StringBuilder(entries[2].getText());
            builder.deleteCharAt(0);
            for (int i = 0; i < builder.length(); i++) {
                if (builder.charAt(i) == ',') {
                    builder.delete(i, builder.length());
                    break;
                }
            }
            if (builder.charAt(builder.length() - 1) == 'C') {
                builder.delete(builder.length() - 4, builder.length());
            }
            StringSelection stringSelection = new StringSelection(builder.toString());
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);
            entries[0].setText(builder.toString());
            entries[1].setText("");
            entries[2].setText("");
            highlight();
        }
    }

    public static void executeClear() {
        for (int i = 0; i < 3; i++) {
            entries[i].setText("");
        }
        entries[0].requestFocusInWindow();
    }

    private static boolean verify(int limit) {
        if (limit < 3) {
            for (int i = 0; i < limit; i++) {
                if (entries[i].getText().equals(""))
                    return false;
            }
            return true;
        } else
            return !entries[2].getText().equals("");
    }

    private static void highlight() {
        correct = true;
        boolean first = true, sameSymbol;
        Highlighter.HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(Color.pink);
        Pattern pattern = Pattern.compile("(([-+] ?)((([0-9]*)([.][0-9]+)?)?\\*?([A-Z]|[a-z])?\\^?(\\d+)?))");
        Pattern verifyPattern = Pattern.compile("(([+-] ?)(([^+-])+))");

        for (int i = 0; i < 2; i++) {
            String source = entries[i].getText();
            if (source.equals(""))
                continue;
            boolean flag = false;
            if (source.charAt(0) != '-') {
                source = '+' + source;
                flag = true;
            }
            Highlighter highlighter = entries[i].getHighlighter();
            Matcher matcher = pattern.matcher(source);
            Matcher verify = verifyPattern.matcher(source);
            StringBuilder ver;
            int p0, p1 = 0;

            highlighter.removeAllHighlights();
            while (matcher.find() & verify.find()) {
                sameSymbol = true;
                if (matcher.group(7) != null) {
                    if (first) {
                        symbol = matcher.group(7);
                        first = false;
                    } else if (!symbol.equals(matcher.group(7))) {
                        sameSymbol = false;
                    }
                }
                ver = finalBuilder(verify.group(3));
                p0 = source.indexOf(ver.toString(), p1 + 1);
                if (flag)
                    p0 -= 1;
                p1 = p0 + ver.length();
                if (ver.toString().equals(matcher.group(3)) & sameSymbol)
                    continue;

                correct = false;
                try {
                    highlighter.addHighlight(p0, p1, painter);
                } catch (BadLocationException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private static StringBuilder finalBuilder(String source) {
        StringBuilder res = new StringBuilder(source);
        if (res.length() == 0)
            return res;
        if (res.charAt(res.length() - 1) == ' ') {
            res.deleteCharAt(res.length() - 1);
        }
        return res;
    }

    public static void isCorrect() throws InvalidPolynomialException {
        if (!correct) {
            throw new InvalidPolynomialException("Invalid Polynomial");
        }
    }

    public static String test(String pol1, String pol2, Polynomial result) {
        entries[0].setText(pol1);
        entries[1].setText(pol2);
        highlight();
        try {
            isCorrect();
        } catch (InvalidPolynomialException e) {
            return "invalid";
        }
        return result.toString();
    }

}
