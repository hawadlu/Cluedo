import java.awt.*;
import javax.swing.*;

public class GUI {
    ActionPanel actionPanel = new ActionPanel();
    ConsolePanel consolePanel = new ConsolePanel();
    BoardPanel boardPanel = new BoardPanel();
    CardPanel cardPanel = new CardPanel();

    GUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("Cluedo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        CustomGrid baseLayout = new CustomGrid(frame.getContentPane());
        setup(baseLayout);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public void setup(CustomGrid customGrid) {
        customGrid.setLayout(new GridBagLayout());
        customGrid.setConstraints(new GridBagConstraints());

        actionPanel.setBackground(Color.cyan);
        customGrid.addElement(GridBagConstraints.HORIZONTAL,0, 0, 0, 500, 100, 1, 1, actionPanel);

        boardPanel.setBackground(Color.orange);
        customGrid.addElement(GridBagConstraints.HORIZONTAL,0, 1, 0, 500, 200, 2, 1, boardPanel);

        consolePanel.setBackground(Color.black);
        customGrid.addElement(GridBagConstraints.HORIZONTAL,0, 3, 0, 100, 100, 1, 2, consolePanel);

        cardPanel.setBackground(Color.red);
        customGrid.addElement(GridBagConstraints.HORIZONTAL,0, 0, 1, 100, 500, 3, 1, cardPanel);

    }
}

class ActionPanel extends JPanel {}

class ConsolePanel extends JPanel {}

class BoardPanel extends JPanel {}

class CardPanel extends JPanel {}

class CustomGrid {
    Container gridContainer;
    GridBagConstraints constraints;

    CustomGrid(Container gridContainer) {
        this.gridContainer = gridContainer;
    }

    public void addElement(int fill, int weightX, int gridx, int gridy, int ipady, int ipadx, int gridWidth, int gridHeight, JPanel testPanel) {
        constraints.fill = fill;
        constraints.weightx = weightX;
        constraints.gridx = gridx;
        constraints.gridy = gridy;
        constraints.ipady = ipady;
        constraints.ipadx = ipadx;
        constraints.gridwidth = gridWidth;
        constraints.gridheight = gridHeight;
        gridContainer.add(testPanel, constraints);
    }

    public void setLayout(GridBagLayout layout) {
        gridContainer.setLayout(layout);
    }

    public void setConstraints(GridBagConstraints gridBagConstraints) {
        this.constraints = gridBagConstraints;
    }
}