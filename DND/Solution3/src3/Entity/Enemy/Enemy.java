package Entity.Enemy;

import Entity.Player.Player;
import Entity.Tile.*;
import Resource_based.Abilities.BossAbility;
import Resource_based.Abilities.PlayerAbility;

public abstract class Enemy extends Unit {

    protected final int EXP; // EXP given upon death

    public int getEXP() {
        return EXP;
    }


    public Enemy(char c, int att, int def, int EXP, String name, int HP) {
        super(c,att,def,name,HP);
        this.EXP=EXP;
    }
    public abstract String Turn(Player p);


    public String reciveMove(Enemy p) {
        return "";
    } //visitor receiver

    public String move(Tile t){ //move to a given tile
        String out=t.reciveMove(this);
        return out;
    }

    public String reciveMove(Player p){ // visitor receiver
        String out=def(p);
        if(hp.getCur()<=0) {
            out+=die(p);
            out+=p.kill(this);
            frame.setTile(new Empty());
            frame.swapTile(p.getFrame());
        }
        else{
            out+=name+" has "+hp.getCur()+"/"+hp.getMax()+" health left.\n";
        }
        return out;
    }
    public void abilityKill(){// if killed by ability update the frame
        Tile t=new Empty();
        frame.setTile(t);
        t.setFrame(frame);
    }

    public String injured(int cost, Player p){ // if injured by player ability
        StringBuilder output=new StringBuilder();
        output.append(super.injured(cost,p));//all shared actions of all units when injured by ability
        if (isDead){
            output.append(p.kill(this)); //if dead award exp
            abilityKill();
        }
        return output.toString();
    }



    public  String receiveCast(PlayerAbility a){ // visitor receiver
        return a.attack(this);
    }
    public  String receiveCast(BossAbility a){ // visitor receiver
        return "Boss decided to attack enemy";
    }



    public String kill(Enemy e){
        return name+" accidentally killed "+e.name+".\n";
    }

    public void Tick(){
         //override this in boss, dont override in monster or trap
    }
}
