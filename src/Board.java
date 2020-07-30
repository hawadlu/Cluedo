
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Construct a board of positions
 * -Draws board for user
 * -Contains locations of players
 */
public class Board {
   HashMap<String, Room> rooms = new HashMap<>();
   Tile[][] board = new Tile[25][24];

   public Board(){

      for(int i =0; i< board.length; i++){
         for(int j =0; j< board[i].length; j++){
            board[i][j] = new Tile();
         }
      }

      //sets room tiles
      ArrayList<Tile> Kitchen = new ArrayList<>();
      Kitchen = allocateTiles(Kitchen, 0,5,0,6);
      rooms.put("Kitchen", new Room(Kitchen, "Kitchen"));

      ArrayList<Tile> BallRoom = new ArrayList<>();
      BallRoom = allocateTiles(BallRoom, 10,13,0,1);
      BallRoom = allocateTiles(BallRoom, 8,15,2,7);
      rooms.put("BallRoom", new Room(BallRoom, "BallRoom"));

      ArrayList<Tile> Conservatory = new ArrayList<>();
      Conservatory = allocateTiles(Conservatory, 18,23,0,4);
      Conservatory = allocateTiles(Conservatory, 19,23,5,5);
      rooms.put("Conservatory", new Room(Conservatory, "Conservatory"));

      ArrayList<Tile> DinningRoom = new ArrayList<>();
      DinningRoom = allocateTiles(DinningRoom, 0,4,9,9);
      DinningRoom = allocateTiles(DinningRoom, 0,7,10,15);
      rooms.put("DinningRoom", new Room(DinningRoom, "DinningRoom"));

      ArrayList<Tile> BilliardRoom = new ArrayList<>();
      BilliardRoom = allocateTiles(BilliardRoom, 18,23,8,12);
      rooms.put("BilliardRoom", new Room(BilliardRoom, "BilliardRoom"));

      ArrayList<Tile> Library = new ArrayList<>();
      Library = allocateTiles(Library, 17,17,15,17);
      Library = allocateTiles(Library, 18,23,14,18);
      rooms.put("Library", new Room(Library, "Library"));

      ArrayList<Tile> Lounge = new ArrayList<>();
      Lounge = allocateTiles(Lounge, 0,6,19,24);
      rooms.put("Lounge", new Room(Lounge, "Lounge"));

      ArrayList<Tile> Hall = new ArrayList<>();
      Hall = allocateTiles(Hall, 9,14,18,24);
      rooms.put("Hall", new Room(Hall, "Hall"));

      ArrayList<Tile> Study = new ArrayList<>();
      Study = allocateTiles(Study, 17,23,21,24);
      rooms.put("Study", new Room(Study, "Study"));

      //blocked tiles
      ArrayList<Tile> Blocked = new ArrayList<>();
      Blocked = allocateTiles(Blocked, 6,6,0,1);
      Blocked = allocateTiles(Blocked, 7,8,0,0);
      Blocked = allocateTiles(Blocked, 15,16,0,0);
      Blocked = allocateTiles(Blocked, 17,17,0,1);
      Blocked = allocateTiles(Blocked, 10,14,10,16);
      Blocked.add(board[8][0]);
      Blocked.add(board[7][23]);
      Blocked.add(board[13][23]);
      Blocked.add(board[16][0]);
      Blocked.add(board[18][0]);
      Blocked.add(board[20][23]);
      Blocked.add(board[24][8]);
      Blocked.add(board[24][15]);
      rooms.put("Blocked", new Room(Blocked, "Blocked"));

      //sets doors
      //Kitchen
      board[7][4].setDoor(true, rooms.get("Kitchen"));

      //ball room
      board[5][7].setDoor(true, rooms.get("BallRoom"));
      board[8][9].setDoor(true, rooms.get("BallRoom"));
      board[8][14].setDoor(true, rooms.get("BallRoom"));
      board[5][16].setDoor(true, rooms.get("BallRoom"));

      //conservatory
      board[5][18].setDoor(true, rooms.get("Conservatory"));

      //Dinning room
      board[12][8].setDoor(true, rooms.get("DinningRoom"));
      board[16][6].setDoor(true, rooms.get("DinningRoom"));

      //Billiard room
      board[9][17].setDoor(true, rooms.get("BilliardRoom"));
      board[13][22].setDoor(true, rooms.get("BilliardRoom"));
      board[13][22].setRoomName("BilliardRoom");

      //Library room
      board[13][20].setDoor(true, rooms.get("Library"));
      board[13][20].setRoomName("Library");
      board[16][16].setDoor(true, rooms.get("Library"));

      //Lounge room
      board[18][6].setDoor(true, rooms.get("Lounge"));

      //Hall room
      board[17][11].setDoor(true, rooms.get("Hall"));
      board[17][11].setRoomName("Hall");
      board[17][12].setDoor(true, rooms.get("Hall"));
      board[17][12].setRoomName("Hall");
      board[20][15].setDoor(true, rooms.get("Hall"));

      //Study room
      board[20][17].setDoor(true, rooms.get("Study"));

      //sets rooms standing spots
      rooms.get("Kitchen").setSeats(this,1,4);
      rooms.get("BallRoom").setSeats(this,10,5);
      rooms.get("Conservatory").setSeats(this,19,3);
      rooms.get("DinningRoom").setSeats(this,3,13);
      rooms.get("BilliardRoom").setSeats(this,19,9);
      rooms.get("Library").setSeats(this,19,16);
      rooms.get("Lounge").setSeats(this,2,20);
      rooms.get("Hall").setSeats(this,10,20);
      rooms.get("Study").setSeats(this,19,22);
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
         for(int j=x1; j<=x2; j++){
            tiles.add(board[i][j]);
         }
      }
      return tiles;
   }

    /**
     * Cheks to see if this move in the board class is valid.
     * @param current the x pos
     * @param next the y pos
     * @return boolean indicating valid/invalid.
     */
    public boolean isValidMove(Position current, Position next) {
       //checks if the next position is outside of the board
       if(next.x<0||next.x>23||next.y<0||next.y>24){
          return false;
       }

       //checks if another player is already on that tile
       if(board[next.y][next.x].player!=null){
          return false;
       }

       //checks non room to non room move
       if(!board[current.y][current.x].isRoom()&&!board[next.y][next.x].isRoom()){
          return true;
       }

       //checks non room to room move
       if(!board[current.y][current.x].isRoom()&&board[next.y][next.x].isRoom()){
          if(board[current.y][current.x].isDoor()){
             //checks player can use door to enter the room
             if(board[current.y][current.x].doorRoom.equals(board[next.y][next.x].room)||board[current.y][current.x].doorRoom==null){
                return true;
             }
          }
       }

       //checks room to non room move
       if(board[current.y][current.x].isRoom()&&!board[next.y][next.x].isRoom()){
          if(board[next.y][next.x].isDoor()) {
             if(board[next.y][next.x].doorRoom.equals(board[current.y][current.x].room)||board[next.y][next.x].doorRoom==null){
                return true;
             }
          }{

          }
       }

        return false;
    }

   /**gets Tile from x and y
    *
    * @param x
    * @param y
    * @return
    */
    public Tile getTile(int x, int y){
       return board[y][x];
    }

   /**moves player on board
    *
    * @param currentPos
    * @param nextPos
    */
    public void movePlayer(Position currentPos, Position nextPos){
       Tile currentTile = board[currentPos.y][currentPos.x];
       Tile nextTile = board[nextPos.y][nextPos.x];
       Player player = currentTile.player;
       currentTile.removePlayer(player);
       nextTile.addPlayer(player);
    }
    
    //todo implement this properly
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
