import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
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
    BoardPanel boardPanel = new BoardPanel(ImageIO.read(new File("Assets/Test Files/Test 1.png")));
    CardPanel cardPanel = new CardPanel();

    JFrame window = new JFrame("Cluedo");
    CustomGrid baseLayout;

    int width = 1440;
    int height = 900;

    int widthFifths = width / 5;
    int heightThirds = height / 3;

    GUI() throws IOException {
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
        customGrid.setFill(GridBagConstraints.HORIZONTAL);
        customGrid.setAnchor(GridBagConstraints.CENTER);
        customGrid.setWeight(0, 0);
        customGrid.setGrid(0, 0, 1, 1);
        customGrid.setPadding(widthFifths, heightThirds * 2);
        customGrid.addElement(actionPanel);

        boardPanel.setBackground(Color.orange);
        customGrid.setFill(GridBagConstraints.HORIZONTAL);
        customGrid.setAnchor(GridBagConstraints.CENTER);
        customGrid.setWeight(0, 0);
        customGrid.setGrid(1, 0, 2, 1);
        customGrid.setPadding(widthFifths * 4, heightThirds * 2);
        customGrid.addElement(boardPanel);

        consolePanel.setBackground(Color.magenta);
        customGrid.setFill(GridBagConstraints.CENTER);
        customGrid.setAnchor(GridBagConstraints.CENTER);
        customGrid.setWeight(0, 0);
        customGrid.setGrid(0, 1, 1, 1);
        customGrid.setPadding(widthFifths, heightThirds);
        customGrid.addElement(consolePanel);

        cardPanel.setBackground(Color.red);
        cardPanel.initialiseDefaultText(100);
        customGrid.setFill(GridBagConstraints.HORIZONTAL);
        customGrid.setAnchor(GridBagConstraints.CENTER);
        customGrid.setWeight(0, 0);
        customGrid.setGrid(1, 1, 2, 1);
        customGrid.setPadding(widthFifths * 4, heightThirds - cardPanel.getElemHeight());
        customGrid.addElement(cardPanel);

        System.out.println("set padding to: " + (heightThirds - cardPanel.getElemHeight()) + " height thirds: " + heightThirds + " elem height: " + cardPanel.getElemHeight());
    }

    /**
     * Add a message to the console
     * @param message the message
     */
    public void addToConsole(String message) {consolePanel.addMessage(message);}

    /**
     * Update the board image
     * @param newBoard the new board
     */
    public void updateBoard(BufferedImage newBoard) {
        boardPanel.updateImage(newBoard);
    }

    /**
     * redraw the gui
     */
    public void redraw() {
        consolePanel.redraw();
        boardPanel.repaint();
    }
}

class ActionPanel extends JPanel {}

/**
 * This class handles displaying the console.
 * The console contains the last 30 actions of the game
 */
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

/**
 * This class handles displaying the board
 */
class BoardPanel extends JPanel {
    private BufferedImage board;

    BoardPanel(BufferedImage image) {
        this.board = image;
    }

    /**
     * Change to a new image of the board
     * @param newImage
     */
    public void updateImage(BufferedImage newImage) {
        this.board = newImage;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int xOffest = (this.getWidth() / 2) - (board.getWidth() / 2);
        int yOffest = (this.getHeight() / 2) - (board.getHeight() / 2);
        g.drawImage(board, xOffest, yOffest, this);
    }
}

class CardPanel extends JPanel {
    JLabel defaultText = new JLabel("Cluedo");
    int fontSize;

    void initialiseDefaultText(int size) {
        defaultText.setHorizontalAlignment(JLabel.CENTER);
        fontSize = size;
        defaultText.setFont(new Font(defaultText.getFont().getName(), Font.PLAIN, fontSize));
        this.add(defaultText);
    }

    int getElemHeight() {
        //todo add the cards
        return fontSize;
    }
}


/**
 * This class handles customs grids.
 */
class CustomGrid {
    Container gridContainer;
    GridBagConstraints constraints;

    CustomGrid(Container gridContainer) {
        this.gridContainer = gridContainer;
    }

    public void addElement(JPanel testPanel) {gridContainer.add(testPanel, constraints);}
    public void setFill(int fill) {constraints.fill = fill;}
    public void setAnchor(int anchor) {constraints.anchor = anchor;}
    public void setWeight(int weightX, int weightY) {
        constraints.weightx = weightX;
        constraints.weighty = weightY;
    }
    public void setGrid(int gridX, int gridY, int gridWidth, int gridHeight) {
        constraints.gridx = gridX;
        constraints.gridy = gridY;
        constraints.gridwidth = gridWidth;
        constraints.gridheight = gridHeight;
    }
    public void setPadding(int xPad, int yPad) {
        constraints.ipadx = xPad;
        constraints.ipady = yPad;
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