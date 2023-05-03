package ui;

import javax.swing.*;
import java.awt.*;

//StepsPrinter is based on ScreenPrinter from AlarmController project on EDX

//A class representing a JInternalFrame with JTextArea to represent Recipe Steps.
public class StepsPrinter extends JInternalFrame {
    public static final int WIDTH = 175;
    public static final int HEIGHT = 300;
    private final JTextArea printArea;

    //EFFECTS: constructs new StepsPrinter for use with parent Component, with title and closable specified.
    public StepsPrinter(Component parent, String title, boolean closable) {
        super(title, false, closable);
        printArea = new JTextArea();
        printArea.setEditable(false);
        printArea.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(printArea);
        add(scrollPane);
        setSize(WIDTH, HEIGHT);
        setVisible(true);
    }

    //MODIFIES: this
    //EFFECTS: sets the text of JTextArea to string provided.
    public void printText(String text) {
        printArea.setText(text);
        repaint();
    }
}
