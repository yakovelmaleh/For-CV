package GameControl;

import Entity.Enemy.Enemy;
import Entity.Player.Player;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Controller {
    public static final char LEFT='a';
    public static final char RIGHT='d';
    public static final char UP='w';
    public static final char DOWN='s';
    public static final char WAIT='q';
    public static final char ABILITY='e';

    public Player player;
    public int curBoard;
    public List<Board> levels; //list of all currently loaded boards
    public List<List<Enemy>> enemyList; // list of all enemy groups in all boards

    public Controller(Player p,String path){
        levels=new LinkedList<>();
        player=p;
        enemyList=new LinkedList<>();
        loadBoards(path);
        curBoard=0;
        player.setFrame(levels.get(curBoard).getPlayerFrame());
    }

    public String getPlayerName(){return player.getName();}

    public void loadBoards(String path) { // load all of the levels into the game
        List<String> result=new LinkedList<>();
        try (Stream<Path> walk = Files.walk(Paths.get(path))) {
            result = walk.filter(Files::isRegularFile)
                    .map(x -> x.toString()).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (String s : result) {
            Board b=new Board();
            enemyList.add(b.Load(LoadBoard(s),player));
            levels.add(b);
        }
    }

    public List<String> LoadBoard(String path){ //parse each lvl into a list of strings (each layer is one string)
        List<String> output=new LinkedList<>();
        try {
            File myObj = new File(path);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
               String data = myReader.nextLine();
               output.add(data);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return output;
    }

    public String stats(){
        return player.stats();
    } //return player stats

    public void setPlayer(Player player) {
        this.player=player;
    }

    public boolean finish(){ //function to check if game needs to continue
        if (player.isDead() || levels.size()<=curBoard )
            return true;
        return false;
    }

    public String endLevel(){  // check if current lvl is finished and advance a level or end the game
        if (player.isDead())
            return display()+"\n-------------------------------- Game Over --------------------------------\n";
        if (enemyList.get(curBoard).size()==0){ // if no more enemies on the current board advance level
            curBoard++;
            if (!finish()) { // if game is not over advance
                levels.get(curBoard).setPlayerFrame(player);
                player.setFrame(levels.get(curBoard).getPlayerFrame());
                return "you finished level:"+curBoard+"\n";
            }
            return display()+"\n--------------------------------\nCongratulations, you won the game!\n--------------------------------\n";
        }
        return ""; //return nothing if no need to advance or end the game
    }

    public String display() { // return a string with the current board
        if(curBoard<levels.size()){
            return levels.get(curBoard).display();
        }
        return "";
    }

    public String action(String actionChar){ //perform an action if it's legal
        if (actionChar==null|| actionChar.length()!=1){
            throw new IllegalArgumentException("the action is illegal.");
        }
        StringBuilder log=new StringBuilder();
        log.append(player.action(actionChar.charAt(0),enemyList.get(curBoard)));
        return log.toString();
    }

    public String enemyTurn() { // loop through all enemies and make each one perform its turn
        StringBuilder output=new StringBuilder();
        for(int i=0; i<enemyList.get(curBoard).size();i++){
            Enemy temp=enemyList.get(curBoard).get(i);
            if(!temp.isDead()) {
                output.append(temp.Turn(player));
            }
            else {enemyList.get(curBoard).remove(temp);}
        }
        return output.toString();
    }
}
