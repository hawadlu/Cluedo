import java.util.ArrayList;
import java.util.List;

public class Suggest implements Action {
    private Card<Game.Suspects> suspect;
    private Card<Game.Rooms> room;
    private Card<Game.Weapons> weapon;
    private final Player player;

    Suggest(Game.Rooms room, Game.Suspects suspect, Game.Weapons weapon, Player player) {
        try {
            this.suspect = new Card<>(suspect);
            this.room = new Card<>(room);
            this.weapon = new Card<>(weapon);
        }catch(InvalidFileException e){ System.out.println(e.toString()); }
        this.player = player;
    }

    @Override
    public void apply() {
        // Move the suggested player to the room
        Player suspectPlayer = Game.playerMap.get(suspect.getEnum());
        Position pos = suspectPlayer.getPos();
        Game.board.getTile(pos).removePlayer();
        Board.rooms.get(room.getEnum()).addPlayer(suspectPlayer);

        List<String> cannotProveWrong = new ArrayList<>();

        // Go through each players hand after this player looking for a match
        int indexOfPlayer = Game.players.indexOf(player);
        for (int i = (indexOfPlayer+1) % Game.players.size(); i != indexOfPlayer; i = (i+1) % Game.players.size()) {
            Player otherPlayer = Game.players.get(i);
            if (otherPlayer.getHand().size() == 0) continue;
            ArrayList<Card<?>> cardOptions = otherPlayer.addMatches(room, suspect, weapon);

            // A match has been found in this hand
            if (!cardOptions.isEmpty()) {
                // Allow the player to choose a card without current player seeing
                System.out.println(otherPlayer.getName() + " can prove you wrong, let them choose a card to show you"
                        +"\nPress Enter if you are "+otherPlayer.getName());
                Game.input.nextLine();

                Card<?> toShow = Game.chooseFromArray(cardOptions.toArray(new Card<?>[]{}), "Choose a card to show to "+player.getName());

                // Go back to the current players turn and display the chosen card
                System.out.println(otherPlayer.getName() + " has chosen to show you "+toShow
                        +"\nPress Enter if you are "+player.getName()+" to continue");
                Game.input.nextLine();

                // Generate console output text
                int size = cannotProveWrong.size();
                if (size > 0) {
                    StringBuilder output = new StringBuilder();

                    if (size == 1)
                        output.append(cannotProveWrong.get(0));
                    else
                        for (int j = 0; j < size; j++)
                            if (j == size-1)
                                output.append(cannotProveWrong.get(j));
                            else if (j == size-2)
                                output.append(cannotProveWrong.get(j)).append(" and ");
                            else output.append(cannotProveWrong.get(j)).append(", ");

                    Game.print(output + " couldn't prove " + player.getName() + " wrong");
                }
                Game.print(otherPlayer.getName()+" proved "+player.getName()+" wrong!");
                Game.print(suspect+" with the "+weapon+" in the "+room);
                Game.print(player.getName()+" suggested:");
                Game.print("\n");

                // Stop searching for further matches
                return;
            } else {
                // Inform the player of who can't prove them wrong
                cannotProveWrong.add(otherPlayer.getName()+"");
            }
        }
        // No matches were found
        Game.print("No one can prove "+player.getName()+" wrong!");
        Game.print(suspect+" with the "+weapon+" in the "+room);
        Game.print(player.getName()+" suggested:");
        Game.print("\n");
    }
}
