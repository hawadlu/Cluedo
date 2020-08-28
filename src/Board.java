import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

/**
 * Construct a board of positions
 * -Draws board for user
 * -Contains locations of players
 */
public class Board {
   public static HashMap<Game.Rooms, Room> rooms = new HashMap<>();
   private final Tile[][] board = new Tile[25][24];
   private String[][] roomNames = new String[25][24];

   public Board(){
      //Read the room names
      try {
         roomNames = readRoomNames();
      } catch (IOException e) {
         e.printStackTrace();
      }

      for(int i =0; i< board.length; i++){
         for(int j =0; j< board[i].length; j++){
            board[i][j] = new HallwayTile(new Position(j,i));
         }
      }

      // Sets room tiles
      ArrayList<RoomTile> Kitchen = new ArrayList<>();
      allocateTiles(Kitchen, 0,5,1,5);
      allocateTiles(Kitchen, 1,5,6,6);
      rooms.put(Game.Rooms.KITCHEN, new Room(Kitchen, Game.Rooms.KITCHEN));

      ArrayList<RoomTile> BallRoom = new ArrayList<>();
      allocateTiles(BallRoom, 10,13,1,1);
      allocateTiles(BallRoom, 8,15,2,7);
      rooms.put(Game.Rooms.BALLROOM, new Room(BallRoom, Game.Rooms.BALLROOM));

      ArrayList<RoomTile> Conservatory = new ArrayList<>();
      allocateTiles(Conservatory, 18,23,1,4);
      allocateTiles(Conservatory, 19,23,5,5);
      rooms.put(Game.Rooms.CONSERVATORY, new Room(Conservatory, Game.Rooms.CONSERVATORY));

      ArrayList<RoomTile> DinningRoom = new ArrayList<>();
      allocateTiles(DinningRoom, 0,4,9,9);
      allocateTiles(DinningRoom, 0,7,10,15);
      rooms.put(Game.Rooms.DINING_ROOM, new Room(DinningRoom, Game.Rooms.DINING_ROOM));

      ArrayList<RoomTile> BilliardRoom = new ArrayList<>();
      allocateTiles(BilliardRoom, 18,23,8,12);
      rooms.put(Game.Rooms.BILLIARD_ROOM, new Room(BilliardRoom, Game.Rooms.BILLIARD_ROOM));

      ArrayList<RoomTile> Library = new ArrayList<>();
      allocateTiles(Library, 17,17,15,17);
      allocateTiles(Library, 18,22,14,18);
      allocateTiles(Library, 23,23,15,17);
      rooms.put(Game.Rooms.LIBRARY, new Room(Library, Game.Rooms.LIBRARY));

      ArrayList<RoomTile> Lounge = new ArrayList<>();
      allocateTiles(Lounge, 0,5,19,24);
      allocateTiles(Lounge, 6,6,19,23);
      rooms.put(Game.Rooms.LOUNGE, new Room(Lounge, Game.Rooms.LOUNGE));

      ArrayList<RoomTile> Hall = new ArrayList<>();
      allocateTiles(Hall, 9,14,18,23);
      allocateTiles(Hall, 10,13,24,24);
      rooms.put(Game.Rooms.HALL, new Room(Hall, Game.Rooms.HALL));

      ArrayList<RoomTile> Study = new ArrayList<>();
      allocateTiles(Study, 17,23,21,23);
      allocateTiles(Study, 18,23,24,24);
      rooms.put(Game.Rooms.STUDY, new Room(Study, Game.Rooms.STUDY));

      // Blocked tiles
      ArrayList<RoomTile> Blocked = new ArrayList<>();
      allocateTiles(Blocked, 6,6,1,1);
      allocateTiles(Blocked, 0,8,0,0);
      allocateTiles(Blocked, 0,0,6,6);
      allocateTiles(Blocked, 15,23,0,0);
      allocateTiles(Blocked, 17,17,1,1);
      allocateTiles(Blocked, 10,14,10,16);
      allocateTiles(Blocked, 0,0,8,8);
      allocateTiles(Blocked, 23,23,7,7);
      allocateTiles(Blocked, 23,23,13,14);
      allocateTiles(Blocked, 23,23,18,18);
      allocateTiles(Blocked, 0,0,16,16);
      allocateTiles(Blocked, 0,0,18,18);
      allocateTiles(Blocked, 23,23,20,20);

      allocateTiles(Blocked, 6,6,24,24);
      allocateTiles(Blocked, 17,17,24,24);
      allocateTiles(Blocked, 8,9,24,24);
      allocateTiles(Blocked, 14,15,24,24);
      rooms.put(null, new Room(Blocked, null));

      // Sets doors
      // Kitchen
      board[7][4] = new DoorTile(rooms.get(Game.Rooms.KITCHEN), new Position(4,7));

      // Ballroom
      board[5][7] = new DoorTile(rooms.get(Game.Rooms.BALLROOM), new Position(7,5));
      board[8][9] = new DoorTile(rooms.get(Game.Rooms.BALLROOM), new Position(9,8));
      board[8][14] = new DoorTile(rooms.get(Game.Rooms.BALLROOM), new Position(14,8));
      board[5][16] = new DoorTile(rooms.get(Game.Rooms.BALLROOM), new Position(16,5));

      // Conservatory
      board[5][18] = new DoorTile(rooms.get(Game.Rooms.CONSERVATORY), new Position(18,5));

      // Dining room
      board[12][8] = new DoorTile(rooms.get(Game.Rooms.DINING_ROOM), new Position(8,12));
      board[16][6] = new DoorTile(rooms.get(Game.Rooms.DINING_ROOM), new Position(6,16));

      // Billiard room
      board[9][17] = new DoorTile(rooms.get(Game.Rooms.BILLIARD_ROOM), new Position(17,9));
      board[13][22] = new DoorTile(rooms.get(Game.Rooms.BILLIARD_ROOM), new Position(22,13));

      // Library room
      board[13][20] = new DoorTile(rooms.get(Game.Rooms.LIBRARY), new Position(20,13));
      board[16][16] = new DoorTile(rooms.get(Game.Rooms.LIBRARY), new Position(16,16));

      // Lounge room
      board[18][6] = new DoorTile(rooms.get(Game.Rooms.LOUNGE), new Position(6,18));

      // Hall room
      board[17][11] = new DoorTile(rooms.get(Game.Rooms.HALL), new Position(11,17));
      board[17][12] = new DoorTile(rooms.get(Game.Rooms.HALL), new Position(12,17));
      board[20][15] = new DoorTile(rooms.get(Game.Rooms.HALL), new Position(15,20));

      // Study room
      board[20][17] = new DoorTile(rooms.get(Game.Rooms.STUDY), new Position(17,20));

      // Sets room standing spots
      rooms.get(Game.Rooms.KITCHEN).setSeats(this,1,4);
      rooms.get(Game.Rooms.BALLROOM).setSeats(this,10,5);
      rooms.get(Game.Rooms.CONSERVATORY).setSeats(this,20,3);
      rooms.get(Game.Rooms.DINING_ROOM).setSeats(this,3,13);
      rooms.get(Game.Rooms.BILLIARD_ROOM).setSeats(this,19,9);
      rooms.get(Game.Rooms.LIBRARY).setSeats(this,19,16);
      rooms.get(Game.Rooms.LOUNGE).setSeats(this,2,20);
      rooms.get(Game.Rooms.HALL).setSeats(this,10,20);
      rooms.get(Game.Rooms.STUDY).setSeats(this,19,22);

      // Sets weapon room spots
      rooms.get(Game.Rooms.KITCHEN).setWeapons(this,4,3);
      rooms.get(Game.Rooms.BALLROOM).setWeapons(this,13,4);
      rooms.get(Game.Rooms.CONSERVATORY).setWeapons(this,18,2);
      rooms.get(Game.Rooms.DINING_ROOM).setWeapons(this,1,12);
      rooms.get(Game.Rooms.BILLIARD_ROOM).setWeapons(this,19,9);
      rooms.get(Game.Rooms.LIBRARY).setWeapons(this,22,15);
      rooms.get(Game.Rooms.LOUNGE).setWeapons(this,0,20);
      rooms.get(Game.Rooms.HALL).setWeapons(this,13,19);
      rooms.get(Game.Rooms.STUDY).setWeapons(this,22,21);

   }

   /**
    * Get the length and height of the board
    */
   public int getLength() {return board.length;}
   public int getHeight() {return board[0].length;}

   /**
    * Helper method to set room tiles
    *
    * @param tiles list of tiles to add new tiles to
    * @param x1 left-most co-ord of area to add
    * @param x2 right-most co-ord of area to add
    * @param y1 top-most co-ord of area to add
    * @param y2 bottom-most co-ord of area to add
    */
   public void allocateTiles(ArrayList<RoomTile> tiles, int x1, int x2, int y1, int y2){
      for(int i=y1;i<=y2; i++){
         for(int j=x1; j<=x2; j++){
            RoomTile newRoomTile =new RoomTile(new Position(j,i));
            board[i][j] = newRoomTile;
            tiles.add(newRoomTile);
         }
      }
   }

    /**
     * Checks to see if this move in the board class is valid.
     *
     * @param current the x pos
     * @param next the y pos
     * @return boolean indicating valid/invalid.
     */
    public boolean isValidMove(Position current, Position next) {
       // Checks if the next position is outside of the board
       if(next.x < 0 || next.x > 23 || next.y < 0 || next.y > 24)
          return false;

       Tile nextTile = board[next.y][next.x];
       Tile currentTile = board[current.y][current.x];

       // Checks if another player is already on that tile
       if(nextTile.getPlayer() != null)
          return false;

       // Checks non room to non room move
       if(!currentTile.isRoom() && !nextTile.isRoom())
          return true;

       // Checks non room to room move
       if(!currentTile.isRoom() && currentTile.isDoor())
          return ((DoorTile) currentTile).getRoom() == ((RoomTile) nextTile).getRoom();

       return false;
    }

   /**
    * Gets Tile from position object
    *
    * @param pos the position of the tile wanted
    * @return the tile at the position
    */
   public Tile getTile(Position pos){
      return board[pos.y][pos.x];
   }

   public Room getRoom(Game.Rooms room){
      return rooms.get(room);
   }

   /**
    * Moves player on board
    *
    * @param currentPos the current position of the player
    * @param nextPos the new position of the player
    */
    public void movePlayer(Position currentPos, Position nextPos){
       Tile currentTile = getTile(currentPos);
       Tile nextTile = getTile(nextPos);
       Player player = currentTile.getPlayer();
       nextTile.addPlayer(player);
       currentTile.removePlayer();

    }

    /**
     * Create a 2d array of buffered images that represent this board
     * @return 2d array of buffered images that represents this board
     * @throws InvalidFileException if any png files cannot be found
     */
   public BufferedImage[][] draw() throws InvalidFileException {
      BufferedImage[][] images = new BufferedImage[25][24];
      String boardFile = "Assets/CluedoBoard.txt";

      try {
         Scanner sc = new Scanner(new File(boardFile));
         int posY=0;
         while (sc.hasNextLine()) {
            Scanner line = new Scanner(sc.nextLine());
            int posX=0;
            while (line.hasNext()) {
               String next = line.next();

               // Skip making an image for null tiles
                if(next.equals("N")) {
                    images[posY][posX] = null;
                    posX++;
                    continue;
                }

               StringBuilder fileName= new StringBuilder("Assets/");

               Tile currentTile = board[posY][posX];
               Tile aboveTile = posY > 0 ? board[posY-1][posX] : null;
               Tile belowTile = posY < 24 ? board[posY+1][posX] : null;
               Tile leftTile = posX > 0 ? board[posY][posX-1] : null;
               Tile rightTile = posX < 23 ? board[posY][posX+1] : null;

               // Switch between highlighted and unhighlighted tiles
               if (currentTile.isHighlighted()){ fileName.append("HighlightedPieces/"); }
               else { fileName.append("TilePieces/"); }

               try {
                   // Make walls
                   if (next.equals("T")) fileName.append("hallway");
                   else {
                       fileName.append("room");

                       if (next.equals("R")) {
                           // Put walls on the top/bottom of appropriate room tiles
                           if (posY == 0 || !aboveTile.isSameRoom(currentTile))
                               fileName.append("N");
                           else if (posY >= 24 || !belowTile.isSameRoom(currentTile))
                               fileName.append("S");

                           // Put walls on the left/right of appropriate room tiles
                           if (posX == 0 || !leftTile.isSameRoom(currentTile))
                               fileName.append("W");
                           else if (posX >= 23 || !rightTile.isSameRoom(currentTile))
                               fileName.append("E");

                           // Make the manually set walls
                       } else if(next.charAt(0) == 'W')
                           fileName.append(next.charAt(1));
                   }

                  fileName.append(".png");

                  // Combine tile image with any player or weapons on top of them
                 if(currentTile.hasPlayer() || (currentTile.isRoom() && ((RoomTile) currentTile).hasWeapon())){
                    BufferedImage image = ImageIO.read(new File(fileName.toString()));

                    if(currentTile.hasPlayer())
                        images[posY][posX] = overlayImages(image, currentTile.getPlayer().getImage(), roomNames, posX, posY);
                    else {
                       images[posY][posX] = overlayImages(image, ((RoomTile) currentTile).getWeapon().getImage(), roomNames, posX, posY);
                    }
                 } else images[posY][posX] = overlayImages(ImageIO.read(new File(fileName.toString())), null, roomNames,posX, posY);

               } catch (IOException e) { throw new InvalidFileException("Invalid filename: " + fileName); }
               posX++;
            }
            posY++;
         }
      } catch (FileNotFoundException e) { throw new InvalidFileException("Invalid filename: " + boardFile); }
      return images;
   }

   /**
    * Read the room names
    * @return 2d array containing the room names
    */
   private String[][] readRoomNames() throws IOException {
      String[][] names = new String[getHeight() + 1][getLength()];
      BufferedReader reader;
      reader = new BufferedReader(new FileReader(new File("Assets/RoomNames.txt")));

      String line;
      int count = 0;
      while((line = reader.readLine()) != null) {
         names[count] = line.split(",");
         count++;
      }

      return names;
   }

   /**
    * Lays one image on top of another
    * @param bottom image to go on the bottom
    * @param top image to go on the top
    * @param roomNames array containing the room names (aligned with the tile positions)
    * @param xPos x pos of the tile to draw
    * @param yPos y pos of the tile to draw
    * @return the composite image
    */
   private BufferedImage overlayImages(BufferedImage bottom, BufferedImage top, String[][] roomNames, int xPos, int yPos) {
      BufferedImage combined = new BufferedImage(bottom.getWidth(), bottom.getHeight(), BufferedImage.TYPE_INT_ARGB);
      Graphics2D g2 = combined.createGraphics();
      g2.drawImage(bottom, 0, 0, null);

      //todo move this out of this method?
      Font monofont = new Font(Font.MONOSPACED, Font.PLAIN, 12);

      //Overlay text
      if (!roomNames[yPos][xPos].equals("n")) {
         g2.setFont(monofont.deriveFont(12f));
         g2.drawString(roomNames[yPos][xPos], 0, 10);
      }

      if (top != null) g2.drawImage(top, 0, 0, null);
      g2.dispose();
      return combined;
   }


   @Override
   public String toString() {
      StringBuilder toReturn = new StringBuilder();
      try {
         Scanner sc = new Scanner(new File("Assets/textcluedo.txt"));
         int posY=0;
         while(sc.hasNextLine()){
            Scanner line = new Scanner(sc.nextLine());
            line.useDelimiter("");
            int posX=0;
            while(line.hasNext()){
               String next = line.next();
               if(next.equals("•")){
                  toReturn.append(board[posY-1][((posX-1)/3)].toString());
                  line.next();
                  posX++;
               }else{
                  toReturn.append(next);
               }
               posX++;
            }
            toReturn.append("\n");
            posY++;
         }
      } catch(FileNotFoundException e){
         System.out.println("File error: " + e);
      }
   return toReturn.toString();
   }
}
