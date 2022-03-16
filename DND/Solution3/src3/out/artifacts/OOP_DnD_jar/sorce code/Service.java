import GameControl.Controller;
import GameControl.Menu;

import java.sql.SQLOutput;
import java.util.Scanner;

public class Service {
    public Menu menu;
    public Controller con;
    public Scanner scan;
    public String path;

    public Service(String path){//the path is the folder with the level.
        menu= new Menu();
        scan=new Scanner(System.in);
        this.path=path;
    }

    public void Start(){
        choosePlayer();
        startGame();
    }
    public void choosePlayer(){//the part where the user chooses his character
        boolean chosen=false;
        while(!chosen) { // continue asking until a proper character name is entered
            System.out.println(menu.options());
            try {
                con = new Controller(menu.getPlayer(scan.nextLine()),path); // create a new controller for the chosen player
                chosen=true;
            }
            catch(Exception e){
                System.out.println("invalid player number, please try again.\n");
            }
        }
    }
    
    public void startGame(){ // start the game
        StringBuilder output=new StringBuilder();
        System.out.println(con.stats());
        while(!con.finish()){ //main game loop
            System.out.println(con.display()); //display the board
            try {
                output.append(con.action(scan.nextLine())); // ask the user for an action and if the action is legal perform it
            } catch (Exception e) {
                System.out.print("invalid move entered, "+con.getPlayerName()+" did nothing.\n");
            }
            output.append("\n");
            output.append(con.enemyTurn()); // run all enemy turns
            output.append(con.stats());//display player stats at the end of the round
            output.append(con.endLevel()); //check if the game should progress to the next lvl or end

            System.out.println(output.toString()); //print everything to the screen
            output=new StringBuilder(); //reset output for next round
        }
        System.out.println("would you like to play again?\n press y to play again, press n other button to leave.\n");
        String s=scan.nextLine();

        while(s.length()!=1 || (s.charAt(0)!='y' & s.charAt(0)!='n')) { // loop for repeating the game or quitting
            System.out.println("\ninvalid answer entered,  please choose y or n.\n");
            s = scan.nextLine();
        }
        if (s.charAt(0)=='y'){
            menu=new Menu();
            Start();
        }

    }
}
