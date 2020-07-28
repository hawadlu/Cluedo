import java.util.ArrayList;
import java.util.HashMap;

/**
 * Construct a board of positions
 * -Draws board for user
 * -Contains locations of players
 */
public class Board {
   HashMap<String, Room> room = new HashMap<>();
   Tile[][] board = new Tile[26][24];

   public Board(ArrayList<Player> players){

      for(int i =0; i< board.length; i++){
         for(int j =0; j< board[i].length; j++){
            board[i][j] = new Tile();
         }
      }

      ArrayList<Tile> Kitchen = new ArrayList<>();
      Kitchen = allocateTiles(Kitchen, 0,5,1,5);
      Kitchen = allocateTiles(Kitchen, 1,5,6,6);
      room.put("Kitchen", new Room(Kitchen, "Kitchen"));

      ArrayList<Tile> BallRoom = new ArrayList<>();
      BallRoom = allocateTiles(BallRoom, 10,13,1,1);
      BallRoom = allocateTiles(BallRoom, 8,15,2,7);
      room.put("BallRoom", new Room(BallRoom, "BallRoom"));

      ArrayList<Tile> Conservatory = new ArrayList<>();
      Conservatory = allocateTiles(Conservatory, 18,23,1,4);
      Conservatory = allocateTiles(Conservatory, 19,22,5,5);
      room.put("Conservatory", new Room(Conservatory, "Conservatory"));

      ArrayList<Tile> DinningRoom = new ArrayList<>();
      DinningRoom = allocateTiles(DinningRoom, 0,4,9,9);
      DinningRoom = allocateTiles(DinningRoom, 0,7,10,15);
      room.put("DinningRoom", new Room(DinningRoom, "DinningRoom"));

      ArrayList<Tile> BilliardRoom = new ArrayList<>();
      BilliardRoom = allocateTiles(BilliardRoom, 18,23,8,12);
      room.put("BilliardRoom", new Room(BilliardRoom, "BilliardRoom"));

      ArrayList<Tile> Library = new ArrayList<>();
      Library = allocateTiles(Library, 17,17,15,17);
      Library = allocateTiles(Library, 23,23,15,17);
      Library = allocateTiles(Library, 18,22,14,14);
      Library = allocateTiles(Library, 18,22,15,18);
      room.put("Library", new Room(Library, "Library"));

      ArrayList<Tile> Lounge = new ArrayList<>();
      Lounge = allocateTiles(Lounge, 0,6,19,23);
      Lounge = allocateTiles(Lounge, 0,5,24,24);
      room.put("Lounge", new Room(Lounge, "Lounge"));

      ArrayList<Tile> Hall = new ArrayList<>();
      Hall = allocateTiles(Hall, 9,14,18,24);
      room.put("Hall", new Room(Hall, "Hall"));

      ArrayList<Tile> Study = new ArrayList<>();
      Study = allocateTiles(Study, 17,23,21,23);
      Study = allocateTiles(Study, 18,23,24,24);
      room.put("Study", new Room(Study, "Study"));
   }

   /**Helper method to set room tiles
    *
    * @param tiles
    * @param x1
    * @param x2
    * @param y1
    * @param y2
    * @return
    */
   public ArrayList<Tile> allocateTiles(ArrayList<Tile> tiles, int x1, int x2, int y1, int y2){
      for(int i=y1; i<=y2; i++){
         for(int j=x1; i<=x2; i++){
            tiles.add(board[i][j]);
         }
      }
      return tiles;
   }






    /**
     * Cheks to see if this position in the board class is valid.
     * @param currentX the x pos
     * @param currentY the y pos
     * @return boolean indicating valid/invalid.
     */
    public boolean isValidPosition(int currentX, int currentY) {
        return true; //todo implement me
    }
}