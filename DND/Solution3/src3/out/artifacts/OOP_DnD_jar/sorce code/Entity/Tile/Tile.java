package Entity.Tile;

import Entity.Enemy.Enemy;
import Entity.Player.Player;

public abstract class Tile {
    protected TileFrame frame=null; //the frame this tile is inside
    protected char chr;


    public TileFrame getFrame() {
        return frame;
    }

    public char getChr() {
        return chr;
    }



    public Tile(char c){
        chr=c;
    }
    public Tile(char c,TileFrame f){
        frame=f;
        chr=c;
    }
    public abstract String reciveMove(Enemy u); //visitor receiver
    public abstract String reciveMove(Player u);//visitor receiver
    public void setFrame(TileFrame f) {frame=f;}
    public String toString(){
        return ""+chr;
    }
    public abstract boolean isDead();
    public void swapFrame(Tile t){ //swap the frames of 2 tiles
        TileFrame tmp=frame;
        frame=t.getFrame();
        frame.setTile(this);
        t.setFrame(tmp);
        t.getFrame().setTile(t);
    }

}
