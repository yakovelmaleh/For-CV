package Entity.Player;

import Entity.Enemy.Boss;
import Entity.Enemy.Enemy;
import Entity.Heroic;
import Entity.Tile.*;
import GameControl.Controller;
import GameControl.Utils;
import Resource_based.Abilities.BossAbility;
import Resource_based.Abilities.PlayerAbility;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public abstract class Player extends Unit implements Heroic {
    //player constants
    private static final char DEFAULT_CHAR='@';
    private static final char DEAD_CHAR='X';
    private static final int START_NEXT_EXP=100;
    private static final int NEXT_EXP_MODIFIER=50;
    private static final int ATT_MODIFIER=4;
    private static final int DEF_MODIFIER=1;
    public int exp;
    public int lvl;
    public int nextExp;
    protected int range;
    protected  PlayerAbility ability;

    public Player(int att, int def, String name, int HP,int range,PlayerAbility ability){
        super(DEFAULT_CHAR,att,def,name,HP);
        ability.setPlayer(this);
        lvl=1;
        exp=0;
        nextExp=START_NEXT_EXP;
        this.range=range;
        this.ability=ability;
    }
    public String levelUp(){
        String out="";
        while(exp>=nextExp){ //if a single kill gives enough EXP to level up several times loop until all EXP exhausted
            exp=exp-(nextExp);
            nextExp+=lvl*NEXT_EXP_MODIFIER;
            lvl+=1;
            setUpAbilityLevel();

            out+="\n--------------------------------------\n"+name+" leveled up to level: "+lvl+ ".\n details: \n Attack: "+att+" \n Defense: "+def +"\n exp: "+exp
                    +"\n next level at: "+nextExp+"\n--------------------------------------\n";
        }
        return out;
    }
    public String move(Tile t){ //move to a given tile
        String output=name+" tried to move to position "+t.getFrame().getPos()+".\n";
        output+=t.reciveMove(this);
        return output;
    }

    public String die(Unit u){
        chr=DEAD_CHAR;
        return super.die(u);
    }
    public boolean isDead(){
        return isDead;
    }
    @Override
    public String reciveMove(Enemy e) {
        String out=def(e);
        if(hp.getCur()<=0) {
            out+=die(e);

        }
        else{
            out+=name+" has "+hp.getCur()+"/"+hp.getMax()+" health left.\n";
        }
        return out;
    }
    public String reciveMove(Player p){// visitor receiver
        throw new IllegalArgumentException("PVP is not yet supported.\n");
    }

    public String cast(List<Unit> ls) { //impl heroic
        return ability.useAbility(ls);
    }

    public void setUpAbilityLevel() {
        att = ATT_MODIFIER * lvl + att;
        def = DEF_MODIFIER*lvl + def;
        hp.levelUpHP(lvl);
        levelUpSpacialAbility();
    }
    public void levelUpSpacialAbility(){
        ability.LevelUp();
    }
    public String checkLevelUp(){
        return (exp>=nextExp)? levelUp() : "";
    }

    public String kill(Enemy e){ //if killed an enemy gain EXP
        exp+=e.getEXP();
        String out=name+" gained "+e.getEXP()+" experience from killing "+e.getName()+".\n";
        out+=checkLevelUp();
        return out;
    }

    private boolean isMove(char c){ //check if given action char is a move
        return c== Controller.LEFT|c==Controller.DOWN|c==Controller.RIGHT|c==Controller.UP|c==Controller.WAIT;
    }


    public String action(char c, List<Enemy> L){
        if(isMove(c)) return  super.action(c); //if given action char is a move then move like any unit
        else if(c==Controller.ABILITY){ //if given action char is an ability use cast an ability
            List<Unit> inRange=new LinkedList<>();
            for (Enemy e : L){ //ability can affect all enemies in range
                if (Utils.RANGE(this.frame, e.getFrame())<range)
                    inRange.add(e);
            }
            String out=cast(inRange);
            Tick();
            return out;
        }

        else {
            Tick();
            throw new IllegalArgumentException("illegal action in player class.");
        }
    }
    public String receiveCast(BossAbility a) { // visitor receiver
        return a.attack(this);
    }
    public String receiveCast(PlayerAbility a) {// visitor receiver
        return "PVP is not yet supported.\n";
    }

    public void Tick(){
        ability.Tick();
    }

    public String injured(int cost,Boss b){ //if injured by boss ability
        StringBuilder output=new StringBuilder();
        output.append(super.injured(cost,b)); //all shared actions of all units when injured by ability
        if(isDead)
            output.append(die(b)); //die as player if dead
        return output.toString();
    }

    public String stats(){ // return a string of all player stats
        StringBuilder out=new StringBuilder();
        out.append("Name: ").append(name).append("\nHealth: ").append(hp.getCur()).append("/").append(hp.getMax()).append("\nLevel: ");
        out.append(lvl).append("\nExperience: ").append(exp).append("\nAttack: ").append(att).append("\nDefence: ").append(def).append("\n").append(ability.info()).append("\n");
        return out.toString();
    }

    public String options(){
        StringBuilder out=new StringBuilder();
        String s;
        String[] t=new String[9];
        out.append("Name: ").append(name);
        s= name;
        for (int i=s.length(); i< 15 ; i++)
            out.append(" ");
        s=hp.getCur()+ "/"+ hp.getMax();
        out.append("Health: ").append(s);
        for (int i=s.length(); i< 12 ; i++)
            out.append(" ");
        s=att+"";
        out.append("Attack: ").append(s);
        for (int i=s.length(); i< 10 ; i++)
            out.append(" ");
        s=def+"";
        out.append("Defence: ").append(s);
        for (int i=s.length(); i< 10 ; i++)
            out.append(" ");
        out.append(ability.toString());
        return out.toString();
    }
}
