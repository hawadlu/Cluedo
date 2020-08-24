import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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


    JPanel topBar = new JPanel();
    JPanel content = new JPanel();

    /**
     * These objects handle the four quadrants of the gui
     */
    ActionPanel actionPanel = new ActionPanel(new Dimension(widthFifths, heightSixths * 4),
            new Dimension(widthFifths, heightSixths * 2));
    ConsolePanel consolePanel = new ConsolePanel(new Dimension(widthFifths, heightSixths * 4));
    BoardPanel boardPanel = new BoardPanel(new Dimension(widthFifths * 3, heightSixths * 4));
    CardPanel cardPanel = new CardPanel();

    //Add the content
    CustomGrid gameLayout;

    GUI() throws IOException, InvalidFileException {
        window.setLayout(new BoxLayout(window.getContentPane(), BoxLayout.Y_AXIS));
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);// todo might update this at a later stage

        window.add(topBar);
        window.add(content);

        //Setup the game layout
        setupGameLayout();

        //Display the window.
        window.pack();
        window.setVisible(true);
    }

    /**
     * This sets up the gui.
     */
    public void setupGameLayout() throws IOException {
        //Setup the menu bar
        generateMenuBar(true);

        content.removeAll();
        gameLayout = new CustomGrid(content);

        gameLayout.setLayout(new GridBagLayout());
        gameLayout.setConstraints(new GridBagConstraints());

        actionPanel.setBackground(Color.cyan);
        gameLayout.setFill(GridBagConstraints.HORIZONTAL);
        //gameLayout.setAnchor(GridBagConstraints.FIRST_LINE_START);
        gameLayout.setWeight(0, 0);
        gameLayout.setGrid(0, 0, 1, 1);
//        customGrid.setPadding(widthFifths, heightSixths * 4);
        gameLayout.addElement(actionPanel);

        boardPanel.setBackground(Color.orange);
        gameLayout.setFill(GridBagConstraints.HORIZONTAL);
        gameLayout.setAnchor(GridBagConstraints.CENTER);
        gameLayout.setWeight(0, 0);
        gameLayout.setGrid(1, 0, 1, 1);
//        customGrid.setPadding(widthFifths * 3, heightSixths * 4);
        gameLayout.addElement(boardPanel);

        consolePanel.setBackground(Color.magenta);
        gameLayout.setFill(GridBagConstraints.CENTER);
        gameLayout.setAnchor(GridBagConstraints.CENTER);
        gameLayout.setWeight(0, 0);
        gameLayout.setGrid(2, 0, 1, 1);
//        customGrid.setPadding(widthFifths, heightSixths * 4);
        gameLayout.addElement(consolePanel);

        cardPanel.setBackground(Color.red);
        //cardPanel.initialiseDefaultText(100);
        gameLayout.setFill(GridBagConstraints.VERTICAL);
        gameLayout.setAnchor(GridBagConstraints.CENTER);
        gameLayout.setWeight(0, 0);
        gameLayout.setGrid(0, 2, 3, 1);
//        customGrid.setPadding(widthFifths * 5, heightSixths * 2);
        gameLayout.addElement(cardPanel);
        try { cardPanel.setupCards();
        }catch(InvalidFileException e){}
        
        redraw();
    }

    /**
     * Generates the menu bar
     * @return
     */
    private void generateMenuBar(Boolean showInstructions) {
        topBar.removeAll();

        //choose instructions title
        String instructionTitle;
        if (showInstructions) instructionTitle = "Show Instructions";
        else instructionTitle = "Hide Instructions";

        //Menu Headings
        JMenuBar menuBar = new JMenuBar();
        JMenu playMenu = new JMenu("Game Options");
        JMenuItem debug = new JMenu("Debug");
        JMenuItem quit = new JMenuItem("Quit");

        //Items
        JMenuItem playGame = new JMenuItem("Play");
        JMenuItem restartGame = new JMenuItem("Restart");
        JMenuItem instructions = new JMenuItem(instructionTitle);
        JMenuItem printActivePlayer = new JMenuItem("Print Active Player");
        JMenuItem printActivePlayerCards = new JMenuItem("Print Active Player Cards");
        JMenuItem printGameRooms = new JMenuItem("Print Game Rooms");
        JMenuItem printFinal = new JMenuItem("Print Winning combo");
        JMenuItem printAllPlayers = new JMenuItem("Print All Players");
        JMenuItem printAllWeapons = new JMenuItem("Print All Weapons");


        //Add components
        playMenu.add(playGame);
        playMenu.add(restartGame);
        debug.add(printActivePlayer);
        debug.add(printActivePlayerCards);
        debug.add(printGameRooms);
        debug.add(printFinal);
        debug.add(printAllPlayers);
        debug.add(printAllWeapons);

        menuBar.add(playMenu);
        menuBar.add(instructions);
        menuBar.add(debug);
        menuBar.add(quit);
        topBar.add(menuBar);

        //Bind the menu items
        quit.addActionListener(actionEvent -> System.exit(0)); //exit the program
        if (showInstructions) {
            instructions.addActionListener(actionEvent -> {
                try {
                    showInstructions();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } else {
            instructions.addActionListener(actionEvent -> {
                try {
                    setupGameLayout();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    /**
     * Show the instructions
     */
    private void showInstructions() throws IOException {
        generateMenuBar(false);
        content.removeAll();

        // create the middle panel components
        JTextArea display = new JTextArea ( 50, 100);
        display.setEditable (false); // set textArea non-editable
        JScrollPane scroll = new JScrollPane ( display );
        scroll.setBorder(null);
        scroll.setVerticalScrollBarPolicy ( ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS );

        display.setBackground(new Color(238, 238, 238));

        //Parse the instructions
        File instructionFile = new File("Assets/Instructions.txt");
        FileReader fileIn = new FileReader(instructionFile);
        BufferedReader reader = new BufferedReader(fileIn);

        String line;
        while ((line = reader.readLine()) != null) {
            display.append(line);
            display.append("\n");
        }

        fileIn.close();

        //Add Textarea in to middle panel
        content.add(scroll);

        redraw();
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
        topBar.revalidate();
        content.revalidate();
        consolePanel.redraw();
        boardPanel.repaint();
        cardPanel.repaint();
    }
}

class ActionPanel extends JPanel {
    JPanel container;
    JPanel buttons;

    ActionPanel(Dimension size, Dimension buttonSize) throws InvalidFileException{
        //Top container with logo, player and dice
        container = new JPanel();
        container.setPreferredSize(size);
        container.setLayout(new FlowLayout(FlowLayout.CENTER));

        //Bottom container with buttons
        buttons = new JPanel();
        buttons.setPreferredSize(buttonSize);
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.Y_AXIS));

        try {
            container.add(container.add(new JLabel(new ImageIcon(ImageIO.read(
                    new File("Assets/Other/CLUEDO_LOGO.png"))))));
        }catch(IOException e){ throw new InvalidFileException("Assets/Other/CLUEDO_LOGO.png"); }
        this.add(container);
    }

    public void drawButtons(Player.Actions[] actions, Player player) throws InvalidFileException{
        container.removeAll();

        //Drawing logo
        try {
            JLabel logo = new JLabel(new ImageIcon(ImageIO.read(new File("Assets/Other/CLUEDO_LOGO.png"))));
            logo.setHorizontalAlignment(JLabel.CENTER);
            container.add(container.add(logo));
        }catch(IOException e){ throw new InvalidFileException("Assets/Other/CLUEDO_LOGO.png"); }

        //Writing text
        JTextArea textArea = new JTextArea(1, 1);
        textArea.setFont(textArea.getFont().deriveFont(18f));
        textArea.append(player.getSuspect() + "  |  " + player.getName());
        container.add(textArea);

        //Making buttons
        //Bottom container with buttons
        buttons.removeAll();
        for (Player.Actions action : actions) {
            //System.out.println(action.toString());
            JButton button = new JButton(action.toString());
            buttons.add(button);
            button.addActionListener(e -> {
                player.takeAction(action, Game.board);
            });
        }
        container.add(buttons);
        this.add(container);
        revalidate();
        repaint();
    }
}

/**
 * This class handles displaying the console.
 * The console contains the last 30 actions of the game
 */
class ConsolePanel extends JPanel {
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
        consoleMessages.add(0, " "+message);
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
    int imgWidth;
    int boardLength = Game.board.getLength() + 1;
    int boardHeight = Game.board.getHeight() + 1;

    BoardPanel(Dimension size) throws IOException {
        this.setPreferredSize(size);
        //Find the appropriate image width
        imgWidth = ImageIO.read(new File("Assets/TilePieces/hallway.png")).getWidth();

        //Setup the mouse listener
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Tile clickedTile = calcTilePos(new Position(e.getX(), e.getY()));
                if (clickedTile != null) {
                    System.out.println("Mouse clicked at x " + e.getX() + " y " + e.getY());
                    if (clickedTile.isHighlighted()) Game.currentPlayer.moveTo(clickedTile);
                }
                else System.out.println("The click was out of bounds for coordinates x " + e.getX() + " y " + e.getY());
            }
        });
    }

    /**
     * Based on the mouse click calculate the title that was clicked on.
     * @param position the position of the mouse click
     * @return The selected tile
     * Null if click is out of bounds.
     */
    private Tile calcTilePos(Position position) {
        //calculate the start four corners of the board
        Position topLeft = new Position(getXOffset(), getYOffset());
        Position bottomRight = new Position(getXOffset() + (imgWidth * (boardLength - 2)), getYOffset() + (imgWidth * boardHeight));

        //Check if the click lies within the board
        if (position.x > topLeft.x && position.x < bottomRight.x && position.y > topLeft.y && position.y < bottomRight.y) {
            Position relativePos = new Position(Math.floorDiv(position.x - getXOffset(), imgWidth), Math.floorDiv(position.y - getYOffset(), imgWidth));
            if (Game.board.getTile(relativePos) != null) {
                System.out.println("Calculated tile position x " + relativePos.x + " y " + relativePos.y);
                return Game.board.getTile(relativePos); //Make sure this is not a null tile
            }
        }
        return null;
    }

    int getXOffset() {return (this.getWidth() / 2 - imgWidth * boardLength / 2);}
    int getYOffset() {return ((this.getHeight() / 2 - (imgWidth * boardHeight) / 2) / 2);}

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int xOffset = getXOffset();
        int yOffset = getYOffset();

        try {
            BufferedImage[][] toDraw = Game.board.draw();

            //System.out.println(toDraw.length);

            for (int i = xOffset, x = 0; x < toDraw[0].length; i += toDraw[1][1].getHeight(), x++) {
                for (int j = yOffset, y = 0; y < toDraw.length; j += toDraw[1][1].getHeight(), y++) {
                    g.drawImage(toDraw[y][x], i, j, this);
                }
            }
        } catch (InvalidFileException e) {
            e.printStackTrace();
        }
    }
}

class CardPanel extends JPanel {

    /**
     * Sets up cards with default image
     * Populates container for start usage
     *
     * @throws InvalidFileException if default card file is not found
     */
    public void setupCards() throws InvalidFileException {
        //Setup variables
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));
        this.setBorder(new EmptyBorder(0, 0, 0, 0));
        this.setLayout(new BorderLayout(0, 0));

        //Draws cards with a 7px strut between cards
        try {
            for (int i = 0; i < 9; i++) {
                container.add(container.add(new JLabel(new ImageIcon(ImageIO.read(
                        new File("Assets/Cards/DEFAULT.png"))))));
                if (i < 8) container.add(Box.createHorizontalStrut(6));
            }
        }catch(IOException e){ throw new InvalidFileException("Assets/Cards/DEFAULT.png"); }
        this.add(container, BorderLayout.CENTER);
    }

    /**
     * Draws players cards on their turn
     *
     * @param cards list in the players hand.
     */
    public void drawCards(ArrayList<Card<?>> cards) {
        //Setup variables
        this.removeAll();
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));
        this.setBorder(new EmptyBorder(0, 0, 0, 0));
        this.setLayout(new BorderLayout(0, 0));

        //Draws each card with a strut
        for (int i = 0; i < cards.size(); i++) {
            container.add(container.add(cards.get(i).getImage()));
            if(i<cards.size()-1) container.add(Box.createHorizontalStrut((12-cards.size())*2));
        }
        this.add(container, BorderLayout.CENTER);

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