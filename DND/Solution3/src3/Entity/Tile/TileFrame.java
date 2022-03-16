package Entity.Tile;

import Entity.Heroic;
import GameControl.Board;

import java.util.LinkedList;
import java.util.List;

public class TileFrame { //the actual position in the board. holds the tile currently in this position
    public Tile tile;
    protected final Pos pos; // position is final on creation
    public final Board board;

    public Pos getPos() {
        return pos;
    }

    public TileFrame(Tile t, Pos p, Board b){
        tile=t;
        pos=p;
        board=b;
    }
    public void swapTile(TileFrame f){ //swap tiles with another frame
        Tile tmp=f.getTile();
        tile.setFrame(f);
        f.setTile(tile);
        tmp.setFrame(this);
        tile=tmp;
    }
    public String reciveMove(Unit u){ //visitor receiver
        String out=u.move(tile);
        return out;
    }


    public String move(Unit u,char c){ //perform an action from this frame
        List<TileFrame> targets=board.action(this,c);
        String out="";
        for (TileFrame tf : targets) {
            out+=u.move(tf);
        }
        return out;
    }

    public Tile getTile(){return tile;}

    public void setTile(Tile t){
        tile=t;
    }
    public Integer getx(){return pos.getX();}
    public Integer gety(){return pos.getY();}
    @Override
    public String toString() {
        return tile.toString();
    }
}
