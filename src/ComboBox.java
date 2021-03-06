import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Creates a Combobox for suggestion/accusation.
 * Given info, create a popup which then
 * creates an accusation/suggestion after OK is pressed
 */
public class ComboBox extends JFrame implements ItemListener, ActionListener {
    private final JDialog frame;
    private final String action;
    private final Player player;

    private final JComboBox<Game.Suspects> suspect;
    private final JComboBox<Game.Weapons> weapon;
    private JComboBox<Game.Rooms> room;

    private Game.Suspects chosenSuspect;
    private Game.Weapons chosenWeapon;
    private Game.Rooms chosenRoom;

    ComboBox(String action, Game.Rooms givenRoom, Player player) {
        //Setup Combobox
        frame = new JDialog();
        frame.setModal(true);
        frame.setLayout(new FlowLayout(FlowLayout.CENTER));
        frame.setSize(new Dimension(360, 180));
        frame.add(new JLabel("You chose to "+action+", press CONFIRM to finish."));
        this.action = action;
        this.player = player;

        //Create suspect
        suspect = new JComboBox<>(Game.Suspects.values());
        chosenSuspect = Game.Suspects.values()[0];
        suspect.addItemListener(this);
        frame.add(suspect);

        //Create weapon
        weapon = new JComboBox<>(Game.Weapons.values());
        frame.add(weapon);
        chosenWeapon = Game.Weapons.values()[0];
        weapon.addItemListener(this);

        //Create Room
        if (givenRoom == null) {
            room = new JComboBox<>(Game.Rooms.values());
            frame.add(room);
            chosenRoom = Game.Rooms.values()[0];
            room.addItemListener(this);
        } else {
            frame.add(new JLabel("Room: " + givenRoom));
            chosenRoom = givenRoom;
        }

        //Add button
        frame.add(Box.createVerticalStrut(65));
        JButton button = new JButton("CONFIRM");
        frame.add(button);
        button.addActionListener(this);

        //Display
        frame.revalidate();
        frame.repaint();
        frame.setVisible(true);
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if(e.getSource() == suspect){
            chosenSuspect = (Game.Suspects) suspect.getSelectedItem();
        }else if(e.getSource() == weapon){
            chosenWeapon = (Game.Weapons) weapon.getSelectedItem();
        }else if(e.getSource() == room){
            chosenRoom = (Game.Rooms) room.getSelectedItem();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String press = e.getActionCommand();
        if(press.equals("CONFIRM")){
            if(action.equals("Suggest")){
                player.setSuggested();
                new Suggest(chosenRoom, chosenSuspect, chosenWeapon, player).apply();
            }else{
                new Accuse(chosenRoom, chosenSuspect, chosenWeapon, player).apply();
            }
            frame.dispose();
        }
    }
}
