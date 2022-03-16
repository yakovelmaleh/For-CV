package Entity.Tile;

import Entity.Enemy.Enemy;
import Entity.Player.Player;

public class Empty extends Tile {
    public static final char CHR='.';
    public Empty(){
        super(CHR);

    }

    @Override
    public String reciveMove(Enemy e) { //visitor receiver
        swapFrame(e);
        return "";
    }
    public String reciveMove(Player p) { //visitor receiver
        swapFrame(p);
        return p.getName()+" walked to position "+p.getFrame().getPos()+"\n";
    }

    @Override
    public boolean isDead(){
        return false;
    }
}
