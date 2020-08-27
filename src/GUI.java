import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
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
            new Dimension(widthFifths, heightSixths * 2), new Dimension(widthFifths, heightSixths));
    ConsolePanel consolePanel = new ConsolePanel(new Dimension(widthFifths, heightSixths * 4));
    BoardPanel boardPanel = new BoardPanel(new Dimension(widthFifths * 3, heightSixths * 4));
    CardPanel cardPanel = new CardPanel(this);

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
    public void setupGameLayout() {
        //Setup the menu bar
        generateMenuBar(true);

        content.removeAll();
        gameLayout = new CustomGrid(content);

        gameLayout.setLayout(new GridBagLayout());
        gameLayout.setConstraints(new GridBagConstraints());

        //actionPanel.setBackground(Color.cyan);
        gameLayout.setFill(GridBagConstraints.HORIZONTAL);
        //gameLayout.setAnchor(GridBagConstraints.FIRST_LINE_START);
        gameLayout.setWeight(0, 0);
        gameLayout.setGrid(0, 0, 1, 1);
//        customGrid.setPadding(widthFifths, heightSixths * 4);
        gameLayout.addElement(actionPanel);

        boardPanel.setBackground(new Color(36, 123, 22));
        gameLayout.setFill(GridBagConstraints.HORIZONTAL);
        gameLayout.setAnchor(GridBagConstraints.CENTER);
        gameLayout.setWeight(0, 0);
        gameLayout.setGrid(1, 0, 1, 1);
//        customGrid.setPadding(widthFifths * 3, heightSixths * 4);
        gameLayout.addElement(boardPanel);

        consolePanel.setBackground(Color.red);
        gameLayout.setFill(GridBagConstraints.CENTER);
        gameLayout.setAnchor(GridBagConstraints.CENTER);
        gameLayout.setWeight(0, 0);
        gameLayout.setGrid(2, 0, 1, 1);
//        customGrid.setPadding(widthFifths, heightSixths * 4);
        gameLayout.addElement(consolePanel);

        cardPanel.setBackground(Color.white);
        //cardPanel.initialiseDefaultText(100);
        gameLayout.setFill(GridBagConstraints.VERTICAL);
        gameLayout.setAnchor(GridBagConstraints.CENTER);
        gameLayout.setWeight(0, 0);
        gameLayout.setGrid(0, 2, 3, 1);
//        customGrid.setPadding(widthFifths * 5, heightSixths * 2);
        gameLayout.addElement(cardPanel);
        try { cardPanel.setupCards();
        }catch(InvalidFileException ignored){}
        
        redraw();
    }

    /**
     * Generates the menu bar
     */
    private void generateMenuBar(Boolean showInstructions) {
        topBar.removeAll();

        //choose instructions title
        String instructionTitle;
        if (showInstructions) instructionTitle = "Show Instructions";
        else instructionTitle = "Hide Instructions";

        //Menu Headings
        JMenuBar menuBar = new JMenuBar();
        JMenuItem debug = new JMenu("Debug");
        JMenuItem quit = new JMenuItem("Quit");

        //Items
        JMenuItem instructions = new JMenuItem(instructionTitle);
        JMenuItem printCurrentPlayer = new JMenuItem("Print Current Player");
        JMenuItem printCurrentPlayerCards = new JMenuItem("Print Current Player Cards");
        JMenuItem printActivePlayers = new JMenuItem("Print Active Players");
        JMenuItem printNPCPlayers = new JMenuItem("Print NPC Players");
        JMenuItem printFinal = new JMenuItem("Print Winning combo");
        JMenuItem printTileInfo = new JMenuItem("Print Tile info");

        //Add components
        debug.add(printCurrentPlayer);
        debug.add(printCurrentPlayerCards);
        debug.add(printActivePlayers);
        debug.add(printFinal);
        debug.add(printNPCPlayers);
        debug.add(printTileInfo);

        menuBar.add(instructions);
        menuBar.add(debug);
        menuBar.add(quit);
        topBar.add(menuBar);

        //Bind the menu items
        quit.addActionListener(actionEvent -> System.exit(0)); //exit the program
        printCurrentPlayer.addActionListener(actionEvent -> System.out.println("Active player: " + Game.currentPlayer));
        printCurrentPlayerCards.addActionListener(actionEvent -> {
            System.out.println("Active player: " + Game.currentPlayer);
            System.out.println("Cards: " + Game.currentPlayer.getHand());
        });
        printActivePlayers.addActionListener(actionEvent -> System.out.println("Active players: " + Game.getActivePlayers()));
        printNPCPlayers.addActionListener(actionEvent -> {
            ArrayList<Player> npc = new ArrayList<>(Game.players);
            npc.removeAll(Game.getActivePlayers());
            System.out.println("NPC: " + npc);
        });
        printFinal.addActionListener(actionEvent -> System.out.println("Murderer (" + Game.murderer + ")\nRoom (" + Game.murderRoom + ")\nWeapon (" + Game.murderWeapon + ")"));
        printTileInfo.addActionListener(actionEvent -> {
            int xCoordinate = Integer.parseInt(JOptionPane.showInputDialog("Enter x coordinate"));
            int yCoordinate = Integer.parseInt(JOptionPane.showInputDialog("Enter y coordinate"));
            Tile tile = Game.board.getTile(new Position(xCoordinate, yCoordinate));
            System.out.println("Highlighted: " + tile.highlighted);
            System.out.println("Player: " + tile.player);
            System.out.println("Position: " + tile.pos);
            System.out.println("Is door: " + tile.isDoor());
            System.out.println("Is hallway: " + tile.isHallway());
            System.out.println("Is not null room: " + tile.isNotNullRoom());
        });

        //show/hide instructions
        if (showInstructions) {
            instructions.addActionListener(actionEvent -> {
                try {
                    showInstructions();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } else {
            instructions.addActionListener(actionEvent -> setupGameLayout());
        }

    }

    /**
     * Show the instructions
     */
    private void showInstructions() throws IOException {
        generateMenuBar(false);
        content.removeAll();
        content.setLayout(new GridBagLayout());

        // create the middle panel components
        JTextArea display = new JTextArea ( 50, 40);
        display.setEditable (false); // set textArea non-editable
        display.setLineWrap(true);
        display.setWrapStyleWord(true);
        JScrollPane scroll = new JScrollPane ( display );
        scroll.setBorder(null);
        scroll.setVerticalScrollBarPolicy ( ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED );

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
     * Set the hover text that appears in the top left of the board
     * @param text
     */
    public void setHoverText(String text) {
        boardPanel.setHoverText(text);
    }

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
    JPanel dice;

    ActionPanel(Dimension size, Dimension buttonSize, Dimension diceSize) throws InvalidFileException{
        //Top container with logo, player and dice
        container = new JPanel();
        container.setPreferredSize(size);
        container.setLayout(new FlowLayout(FlowLayout.CENTER));

        dice = new JPanel();
        dice.setPreferredSize(diceSize);
        dice.setLayout(new BoxLayout(dice, BoxLayout.X_AXIS));

        //Bottom container with buttons
        buttons = new JPanel();
        buttons.setPreferredSize(buttonSize);
        buttons.setLayout(new GridLayout(5,1));

        try {
            container.add(container.add(new JLabel(new ImageIcon(ImageIO.read(
                    new File("Assets/Other/CLUEDO_LOGO.png"))))));
        }catch(IOException e){ throw new InvalidFileException("Assets/Other/CLUEDO_LOGO.png"); }
        this.add(container);
    }

    public void drawButtons(Player.Actions[] actions, Player player, Die[] die) throws InvalidFileException{
        container.removeAll();
        dice.removeAll();
        buttons.removeAll();

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

        //Drawing dice
        dice.add(Box.createHorizontalStrut(20));
        dice.add(die[0].getImage());
        dice.add(Box.createHorizontalStrut(40));
        dice.add(die[1].getImage());
        container.add(dice);

        //Making buttons
        //Bottom container with buttons

        //Creating space if buttons arent present
        buttons.removeAll();
        for(int i=0; i<actions.length-4; i++){
            buttons.add(Box.createVerticalStrut(1));
        }

        //Creating buttons
        for (Player.Actions action : actions) {
            JButton button = new JButton(action.toString().replace("_", " "));
            button.setPreferredSize(new Dimension(100, 90));
            button.addActionListener(e -> {
                try {
                    player.takeAction(action, Game.board);
                } catch (InvalidFileException invalidFileException) {
                    invalidFileException.printStackTrace();
                }
            });
            buttons.add(button);
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
    JTextArea textArea;
    JScrollPane scroll;

    ConsolePanel(Dimension size) {
        this.setPreferredSize(size);

        this.setBorder(new EmptyBorder(0, 0, 0, 0));
        this.setLayout(new BorderLayout(0, 0));

        textArea = new JTextArea(1, 5);
        textArea.setEditable(false);
        textArea.setBackground(Color.WHITE);
        //textArea.set

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

    JTextArea hoverInfo = new JTextArea(20, 10);

    BoardPanel(Dimension size) throws IOException {
        this.setLayout(new FlowLayout(FlowLayout.LEFT));

        //Setup the hover text
        this.add(hoverInfo);
        hoverInfo.setEditable(false);
        hoverInfo.setCaretColor(Color.white);
        hoverInfo.setLineWrap(true);
        hoverInfo.setWrapStyleWord(true);
        hoverInfo.setBackground(new Color(36, 123, 22));

        this.setPreferredSize(size);
        //Find the appropriate image width
        imgWidth = ImageIO.read(new File("Assets/TilePieces/hallway.png")).getWidth();

        //listen for clicking
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Tile clickedTile = calcTilePos(new Position(e.getX(), e.getY()));
                if (clickedTile != null && clickedTile.isHighlighted()) Game.currentPlayer.moveTo(clickedTile);
            }
        });

        //listen for movement
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                //System.out.println("Mouse moved");

                //Check if the mouse has entered known coordinated of a player or weapon
                Object piece = pieceAtMouseLocation(e.getPoint());
                if (piece != null) {
                    setHoverText(piece.toString());
                } else {
                    setHoverText("");
                }
            }
        });
    }

    public void setHoverText(String text) {
        hoverInfo.setText(text);
    }

    /**
     * Checks a location on the board to see if there is a player or a weapon there.
     * @return the player / weapon found at this tile
     */
    private Object pieceAtMouseLocation(Point p) {
        Tile tilePosition = calcTilePos(new Position(p.x, p.y));
        if (tilePosition == null) return null;

        if (tilePosition.hasPlayer()) return tilePosition.getPlayer();

        if (tilePosition instanceof RoomTile) {
            RoomTile roomTile = (RoomTile) tilePosition;
            if (roomTile.hasWeapon()) return roomTile.getWeapon();
        }

        return null;
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
                //System.out.println("Calculated tile position x " + relativePos.x + " y " + relativePos.y);
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
    GUI gui;

    CardPanel(GUI gui) {
        this.gui = gui;
    }

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
            Card<?> currentCard = cards.get(i);
            JComponent cardImage = currentCard.getImage();
            container.add(cardImage);
            if(i<cards.size()-1) container.add(Box.createHorizontalStrut((12-cards.size())*2));

            cardImage.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    gui.setHoverText(currentCard.getExtraInfo());
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    gui.setHoverText("");
                }
            });
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