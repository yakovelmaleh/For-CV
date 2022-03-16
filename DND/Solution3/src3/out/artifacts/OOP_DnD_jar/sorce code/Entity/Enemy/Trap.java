package Entity.Enemy;

import Entity.Player.Player;
import Entity.Tile.Empty;
import GameControl.Utils;

public class Trap extends Enemy {
    private static final int RANGE=2;
    public int tickCounter;
    public int visibility;
    public int inVisibility;
    public char chr2;
    public boolean visible;
    public Trap(char c, int att, int def, int EXP, String name, int HP,int a, int b) {
        super(c, att, def,EXP,name,HP);
        chr2= Empty.CHR;
        visible=true;
        tickCounter=1;
        setUpVisibility(a,b);
    }
    public void setUpVisibility(int a, int b){
        visibility=a;
        inVisibility=b;
    }

    public String attack(Player p){ //if player in range attack player and become visible
        String output=p.getName()+" walked withing "+RANGE+" tiles from "+name+" , "+name+" attempting to attack " +p.getName() +"\n";
        if(!visible) {
            visible = true;
            swapChar();
        }
        tickCounter = 0;
        return output+p.reciveMove(this);
    }
    public void swapChar(){ //upon changing visibility status swap char
        char temp=chr;
        chr=chr2;
        chr2=temp;
    }
    public String reciveMove(Player p){// visitor receiver, only react if visible
        return visible ? super.reciveMove(p) : p.getName()+" can not move to Tile in position:" +frame.getPos()+" there is an invisible trap in the way.";
    }

    @Override
    public String Turn(Player p) { //this trap performs its turn
        if (Utils.RANGE(frame,p.getFrame())<RANGE) {
            return attack(p);
        }
        return setStatus();
    }
    public String setStatus(){ //set visibility status
        if (visible &tickCounter>visibility){
            tickCounter=tickCounter-visibility;
            visible=false;
            swapChar();
            return  name+" at position: "+frame.getPos()+" became invisible.\n";
        }
        else if (!visible & tickCounter>inVisibility)
        {
            tickCounter=tickCounter-inVisibility;
            visible=true;
            swapChar();
            return  name+" at position: "+frame.getPos()+" became visible.\n";
        }
        tickCounter+=1;
        return "";
    }
}
