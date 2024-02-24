package ro.tuc;

import javax.swing.*;
import java.awt.*;

public class AppFrame extends JFrame {

    public AppFrame() {
        this.setVisible(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setTitle("Polynomial Calculator");
        this.setIconImage(new ImageIcon("logo.png").getImage());
        this.setLayout(new BorderLayout());
        this.setResizable(false);
        JPanel entryPanel = new JPanel();
        JPanel boardPanel = new JPanel();

        entryPanel.add(new Entry());
        boardPanel.add(new Board());
        this.add(entryPanel, BorderLayout.NORTH);
        this.add(boardPanel, BorderLayout.SOUTH);

        this.pack();
    }

}
