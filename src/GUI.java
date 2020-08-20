import java.awt.*;
import javax.swing.*;

public class GUI {
    public static void addComponentsToPane(Container pane) {
        pane.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        JPanel testPanel = new JPanel();
        testPanel.setBackground(Color.cyan);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.ipady = 500;
        c.ipadx = 100;
        pane.add(testPanel, c);

        testPanel = new JPanel();
        testPanel.setBackground(Color.orange);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.gridx = 1;
        c.gridy = 0;
        c.ipadx = 200;
        c.ipady = 500;
        c.gridwidth = 2;
        pane.add(testPanel, c);

        testPanel = new JPanel();
        testPanel.setBackground(Color.black);
        c.weightx = 0.0;
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 1;
        c.ipady = 100;
        c.ipadx = 100;
        c.gridwidth = 1;
        pane.add(testPanel, c);

        testPanel = new JPanel();
        testPanel.setBackground(Color.red);
        c.weightx = 0.0;
        c.gridwidth = 3;
        c.gridx = 1;
        c.gridy = 1;
        c.ipadx = 500;
        c.gridwidth = 1;
        pane.add(testPanel, c);
    }

    GUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("Cluedo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        

        //Set up the content pane.
        addComponentsToPane(frame.getContentPane());

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
}

class CustomGrid {

}