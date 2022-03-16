package GameControl;
import Entity.Enemy.Enemy;
import Entity.Player.Player;
import Entity.Tile.Pos;
import Entity.Tile.Tile;
import Entity.Tile.TileFrame;
import java.util.LinkedList;
import java.util.List;

public class Board {
    private TileFrame[][] boardArray; //the board is saved as a 2D array
    private int sizeX;
    private int sizeY;
    private TileFrame playerFrame;

    public Board(){
    }
    public String display(){ //return a string representing the position of all tiles in the board
        StringBuilder output=new StringBuilder();
        for (int i=0; i<sizeX;i++){
            for (int j=0; j<sizeY; j++){
                output.append(boardArray[i][j].toString());}
            output.append("\n");
        }
        return output.toString();
    }

    public List<TileFrame> action(TileFrame frame,char c){ //return a tile to move to corresponding to a given action
        List<TileFrame> out=new LinkedList<>();
        if (c==Controller.LEFT && frame.gety()>0)
            out.add(boardArray[frame.getx()][frame.gety()-1]);
        if (c==Controller.RIGHT && frame.gety()<sizeY-1)
            out.add( boardArray[frame.getx()][frame.gety()+1]);
        if (c==Controller.UP && frame.getx()>0)
            out.add( boardArray[frame.getx()-1][frame.gety()]);
        if (c==Controller.DOWN && frame.getx()<sizeX-1)
            out.add( boardArray[frame.getx()+1][frame.gety()]);
        return out;
    }
    public List<Enemy> Load(List<String> lines, Player player){ //generate board data according to a given list of strings (each string is a horizontal layer)
        //and a given player
        boardArray=new TileFrame[lines.size()][lines.get(0).length()];
        List<Enemy> out=new LinkedList<>();

        for (int i=0; i<lines.size();i++) {
            for (int j = 0; j < lines.get(i).length(); j++) {
                Tuple<Tile,Enemy> tup=Utils.getTile(lines.get(i).charAt(j)); // get a tuple of the tile on a given space and the enemy on that space if any
                Enemy e=tup.getE();
                if(e!=null) { //if an enemy treat space as the enemy and add it to the enemy list
                    out.add(e);
                    boardArray[i][j] = new TileFrame(e,new Pos(i,j),this);
                    e.setFrame(boardArray[i][j]);
                }
                else { // if there is no enemy treat space as the given tile
                    Tile t = tup.getT();
                    boardArray[i][j] = new TileFrame(t, new Pos(i, j), this);
                    if (t != null) t.setFrame(boardArray[i][j]);
                    else { //if no tile and no enemy this is the starting position of the player
                        boardArray[i][j].setTile(player);
                        playerFrame = boardArray[i][j];
                        player.setFrame(playerFrame);
                    }
                }
            }
        }
        sizeX=lines.size();
        sizeY=lines.get(0).length();
        return out;
    }
    public TileFrame getPlayerFrame(){
        return playerFrame;
    }

    public void setPlayerFrame(Player p) {
        playerFrame.setTile(p);
    }
}
