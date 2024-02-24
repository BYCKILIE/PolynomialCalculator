package ro.tuc;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Board extends JPanel implements ActionListener {
    private final JButton[] buttons = new JButton[8];

    public Board() {
        JPanel firstRow = new JPanel();
        JPanel secondRow = new JPanel();
        FlowLayout layout = new FlowLayout();

        layout.setHgap(0);
        layout.setVgap(-1);
        this.setLayout(layout);
        firstRow.setLayout(layout);
        secondRow.setLayout(layout);
        firstRow.setPreferredSize(new Dimension(200, 160));
        secondRow.setPreferredSize(new Dimension(200, 160));
        String[] guide = new String[]{"ADD", "SUBTRACT", "MULTIPLY",
                "DIVIDE", "DERIVE", "INTEGRATE", "USE", "CLEAR"};

        for (int i = 0; i < 8; i++) {
            buttons[i] = new JButton();
            buttons[i].setPreferredSize(new Dimension(200, 40));
            buttons[i].setText(guide[i]);
            buttons[i].setFocusable(false);
            buttons[i].addActionListener(this);
            if (i % 2 == 0) {
                firstRow.add(buttons[i]);
            } else {
                secondRow.add(buttons[i]);
            }
        }
        this.add(firstRow);
        this.add(secondRow);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            Entry.isCorrect();
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(this, exception.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Object source = e.getSource();

        if (source.equals(buttons[0])) {
            Entry.executeAddition();
        } else if (source.equals(buttons[1])) {
            Entry.executeSubtraction();
        } else if (source.equals(buttons[2])) {
            Entry.executeMultiplication();
        } else if (source.equals(buttons[3])) {
            Entry.executeDivision();
        } else if (source.equals(buttons[4])) {
            Entry.executeDerivative();
        } else if (source.equals(buttons[5])) {
            Entry.executeIntegration();
        } else if (source.equals(buttons[6])) {
            Entry.executeUse();
        } else if (source.equals(buttons[7])) {
            Entry.executeClear();
        }
    }
}
