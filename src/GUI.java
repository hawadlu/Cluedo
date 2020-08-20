import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * This class handles all of the drawing of the board
 */
public class GUI {
    /**
     * These objects handle the four quadrants of the gui
     */
    ActionPanel actionPanel = new ActionPanel();
    ConsolePanel consolePanel = new ConsolePanel();
    BoardPanel boardPanel = new BoardPanel();
    CardPanel cardPanel = new CardPanel();

    JFrame window = new JFrame("Cluedo");
    CustomGrid baseLayout;

    int width = 1440;
    int height = 900;

    int widthFifths = width / 5;
    int heightThirds = height / 3;

    GUI() {
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        baseLayout = new CustomGrid(window.getContentPane());
        setup(baseLayout);

        //Display the window.
        window.pack();
        window.setVisible(true);
    }

    /**
     * This sets up the gui.
     * @param customGrid the grid layout
     */
    public void setup(CustomGrid customGrid) {
        customGrid.setLayout(new GridBagLayout());
        customGrid.setConstraints(new GridBagConstraints());

        actionPanel.setBackground(Color.cyan);
        customGrid.addElement(GridBagConstraints.HORIZONTAL,0, 0, 0, heightThirds * 2, widthFifths, 1, 1, actionPanel);

        boardPanel.setBackground(Color.orange);
        customGrid.addElement(GridBagConstraints.HORIZONTAL,0, 1, 0, heightThirds * 2, widthFifths * 4, 2, 1, boardPanel);

        consolePanel.setBackground(Color.magenta);
        customGrid.addElement(GridBagConstraints.HORIZONTAL,0, 0, 1, heightThirds, widthFifths, 1, 1, consolePanel);

        cardPanel.setBackground(Color.red);
        customGrid.addElement(GridBagConstraints.HORIZONTAL,0, 1, 1, heightThirds, widthFifths * 4, 2, 1, cardPanel);
    }

    /**
     * Add a message to the console
     * @param message the message
     */
    public void addToConsole(String message) {consolePanel.addMessage(message);}

    /**
     * redraw the gui
     */
    public void redraw() {
        consolePanel.redraw();
    }
}

class ActionPanel extends JPanel {}

class ConsolePanel extends JPanel {
    ArrayList<String> consoleMessages = new ArrayList<>();
    JTextArea textArea;
    JScrollPane scroll;

    ConsolePanel() {
        this.setBorder(new EmptyBorder(0, 0, 0, 0));
        this.setLayout(new BorderLayout(0, 0));

        textArea = new JTextArea(1, 5);
        textArea.setEditable(false);

        buildMessages();
        this.add(textArea, BorderLayout.CENTER);

        scroll = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.add(scroll);
    }

    /**
     * Add all of the console messages to the gui
     */
    void buildMessages () {
        for (String str: consoleMessages) {
            textArea.append(str);
            textArea.append("\n");
        }
    }

    /**
     * Add a message to the console. Max len of console is 30
     * @param message the message
     */
    void addMessage(String message) {
        if (consoleMessages.size() >= 30) consoleMessages.remove(29);
        consoleMessages.add(0, message);
    }

    /**
     * redraw the console
     */
    public void redraw() {
        textArea.setText(null);
        buildMessages();
    }
}

class BoardPanel extends JPanel {}

class CardPanel extends JPanel {}

/**
 * This class handles customs grids.
 */
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

    /**
     * Set the layout
     * @param layout layout obj
     */
    public void setLayout(GridBagLayout layout) {
        gridContainer.setLayout(layout);
    }

    /**
     * Set the constraints
     * @param gridBagConstraints constraints obj
     */
    public void setConstraints(GridBagConstraints gridBagConstraints) {
        this.constraints = gridBagConstraints;
    }
}