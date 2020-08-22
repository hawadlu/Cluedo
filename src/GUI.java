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
    JFrame window = new JFrame("Cluedo");
    CustomGrid baseLayout;

    int width = 1400;
    int height = 900;

    int widthFifths = width / 5;
    int heightSixths = height / 6;

    /**
     * These objects handle the four quadrants of the gui
     */
    ActionPanel actionPanel = new ActionPanel(new Dimension(widthFifths, heightSixths * 4));
    ConsolePanel consolePanel = new ConsolePanel(new Dimension(widthFifths, heightSixths * 4));
    BoardPanel boardPanel = new BoardPanel(ImageIO.read(new File("Assets/Test Files/Test 1.png")),  new Dimension(widthFifths * 3, heightSixths * 4));
    CardPanel cardPanel = new CardPanel();

    GUI() throws IOException {
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);// todo might update this at a later stage

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
    public void setup(CustomGrid customGrid) throws IOException {
        customGrid.setLayout(new GridBagLayout());
        customGrid.setConstraints(new GridBagConstraints());

        actionPanel.setBackground(Color.cyan);
        customGrid.setFill(GridBagConstraints.HORIZONTAL);
        customGrid.setAnchor(GridBagConstraints.FIRST_LINE_START);
        customGrid.setWeight(0, 0);
        customGrid.setGrid(0, 0, 1, 1);
//        customGrid.setPadding(widthFifths, heightSixths * 4);
        customGrid.addElement(actionPanel);

        boardPanel.setBackground(Color.orange);
        customGrid.setFill(GridBagConstraints.HORIZONTAL);
        customGrid.setAnchor(GridBagConstraints.CENTER);
        customGrid.setWeight(0, 0);
        customGrid.setGrid(1, 0, 1, 1);
//        customGrid.setPadding(widthFifths * 3, heightSixths * 4);
        customGrid.addElement(boardPanel);

        consolePanel.setBackground(Color.magenta);
        customGrid.setFill(GridBagConstraints.CENTER);
        customGrid.setAnchor(GridBagConstraints.CENTER);
        customGrid.setWeight(0, 0);
        customGrid.setGrid(2, 0, 1, 1);
//        customGrid.setPadding(widthFifths, heightSixths * 4);
        customGrid.addElement(consolePanel);

        cardPanel.setBackground(Color.red);
        //cardPanel.initialiseDefaultText(100);
        customGrid.setFill(GridBagConstraints.VERTICAL);
        customGrid.setAnchor(GridBagConstraints.CENTER);
        customGrid.setWeight(0, 0);
        customGrid.setGrid(0, 2, 3, 1);
//        customGrid.setPadding(widthFifths * 5, heightSixths * 2);
        customGrid.addElement(cardPanel);
        cardPanel.addCards();
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
        boardPanel.repaint();
    }
}

class ActionPanel extends JPanel {
    JPanel container = new JPanel();

    ActionPanel(Dimension size) {
        container.setPreferredSize(size);
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.add(new Button("I'm a button"));
        container.add(new Button("I'm also a button"));
        this.add(container);
    }
}

/**
 * This class handles displaying the console.
 * The console contains the last 30 actions of the game
 */
class ConsolePanel extends JPanel {
    //todo maybe make the text bottom up?
    ArrayList<String> consoleMessages = new ArrayList<>();
    JTextArea textArea = new JTextArea();
    JScrollPane scroll;

    ConsolePanel(Dimension size) {
        this.setPreferredSize(size);

        this.setBorder(new EmptyBorder(0, 0, 0, 0));
        this.setLayout(new BorderLayout(0, 0));

        textArea = new JTextArea(1, 5);
        textArea.setEditable(false);
        textArea.setBackground(Color.YELLOW);

        buildMessages();
        this.add(textArea, BorderLayout.CENTER);

        scroll = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.add(scroll);
    }

    /**
     * Add all of the console messages to the gui
     */
    void buildMessages () {
        for (String str: consoleMessages)
            textArea.append(str+"\n");
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
//todo expand this to take the entire height
class BoardPanel extends JPanel {
    private BufferedImage board;

    BoardPanel(BufferedImage image, Dimension size) {
        this.board = image;
        this.setPreferredSize(size);
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
        int xOffset = (this.getWidth() / 2) - ((board.getWidth() * 26) / 2);
        int yOffset = (this.getHeight() / 2) - ((board.getWidth() * 25) / 2);
        for (int i = xOffset; i < (board.getWidth() * 26) + xOffset; i += 20) {
            for (int j = yOffset; j < (board.getWidth() * 25) + yOffset; j += 20) {
                g.drawImage(board, i, j, this);
            }
        }
    }
}

//todo make this into a pullout panel from the bottom.
class CardPanel extends JPanel {

    JScrollPane scroll;
    JPanel container;

    public void addCards() throws IOException {
        this.removeAll(); //might not be necessary
        JPanel container = new JPanel();
        JScrollPane scroll;
        container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));
        this.setBorder(new EmptyBorder(0, 0, 0, 0));
        this.setLayout(new BorderLayout(0, 0));

        for (int i = 0; i < 9; i++) {
            container.add(container.add(new JLabel(new ImageIcon(ImageIO.read(new File("Assets/Test Files/Test Card 1.png"))))));
        }
        this.add(container, BorderLayout.CENTER);

        scroll = new JScrollPane(container, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.add(scroll);
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