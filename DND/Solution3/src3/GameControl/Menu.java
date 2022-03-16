package GameControl;
import Entity.Player.Player;

import java.util.LinkedList;

public class Menu {

    public LinkedList<Player> playerList;

    public Menu(){
        playerList=new LinkedList<Player>();
        playerList.add(Utils.getPlayer(0));
        playerList.add(Utils.getPlayer(1));
        playerList.add(Utils.getPlayer(2));
        playerList.add(Utils.getPlayer(3));
        playerList.add(Utils.getPlayer(4));
        playerList.add(Utils.getPlayer(5));
        playerList.add(Utils.getPlayer(6));



    }
    public String options() { //returns a String of all possible players
        StringBuilder output=new StringBuilder();
        int c=1;
        for(Player p: playerList){
            output.append("\n"+c+")  ").append(p.options()).append("\n");
            c++;
        }
        output.append("\n\nchoose your player (enter Number): ");
        return output.toString();
    }

    public Player getPlayer(String nextLine) { //creates a player according to the given name
        if (nextLine.length()!=1 || nextLine.charAt(0)-'0'>(playerList.size())|nextLine.charAt(0)<='0'){
            throw new IllegalArgumentException(nextLine+" is not one of the options, please try again.");
        }
        return playerList.get(nextLine.charAt(0)-'0'-1);
    }
}
