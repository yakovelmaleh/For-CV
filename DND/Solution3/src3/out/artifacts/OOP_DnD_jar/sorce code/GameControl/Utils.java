package GameControl;
import Entity.Enemy.Enemy;
import Entity.Enemy.Monster;
import Entity.Enemy.Trap;
import Entity.Player.Hunter;
import Entity.Player.Mage;
import Entity.Player.Rogue;
import Entity.Player.Warrior;
import Entity.Tile.*;
import Entity.Player.Player;
import Entity.Enemy.Boss;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class Utils {

    public static List<Supplier<Player>> playerMap = Arrays.asList(//public for testing
            // map function for players
            () -> new Warrior(30, 4, "Jon Snow", 300, 3),
            () -> new Warrior(20, 6, "The Hound", 400, 5),
            () -> new Mage(5, 1, "Melisandre", 100, 6, 300, 30, 15, 5),
            () -> new Mage(25, 4, "Thoros of Myr", 250, 4, 150, 20, 20, 3),
            () -> new Rogue(40, 2, "Arya Stark", 150, 20),
            () -> new Rogue(35, 3, "Bronn", 250, 50),
            () -> new Hunter(30, 2, "Ygritte", 220, 6)
    );

    private static List<Supplier<Enemy>> enemyMap = Arrays.asList( //map function for enemies
            () -> new Boss('K', 300, 150, 5000, "Night’s King", 5000, 8, 10),
            () -> new Boss('C', 10, 10, 1000, "Queen Cersei", 100,1, 1),
            () -> new Boss('M', 60, 25, 500, "The Mountain", 1000, 6,6),
            () -> new Monster('b', 75, 30, 250, "Bear Wrigh",1000,4),
            () -> new Monster('g', 100, 40, 500, "Giant Wright",1500,5),
            () -> new Monster('k', 14, 8, 50, "Lannister Knight",200,4),
            () -> new Monster('s', 8, 3, 25, "Lannister Solider",80,3),
            () -> new Monster('q', 20,15, 100, "Queen’s Guard",400,5),
            () -> new Monster('w', 150,50,1000, "White Walker",200,6),
            () -> new Monster('z', 30,15,100,"Wright",600,3),
            () -> new Trap('B',1,1,250,"Bonus Trap",1,1,5),
            () -> new Trap('D',100,20,250,"Death Trap",500,1,10),
            () -> new Trap('Q',50,10,100,"Queen's Trap",250,3,7)
    );


    public static Double RANGE(TileFrame a, TileFrame b){ //find the distance between 2 frames
        double x=(a.getx().doubleValue()-b.getx().doubleValue());
        double y=(a.gety().doubleValue()-b.gety().doubleValue());
        return Math.sqrt(x*x+y*y);
    }

    public static Tuple<Tile, Enemy> getTile(char c){ //per char factory to build the board
        switch (c){
            case '.': return new Tuple(new Empty(),null);
            case '#': return new Tuple(new Wall(),null);
            case '@': return new Tuple(null,null);
            case 'K': return new Tuple(getEnemy(0),getEnemy(0));
            case 'C': return new Tuple(getEnemy(1),getEnemy(1));
            case 'M': return new Tuple(getEnemy(2),getEnemy(2));
            case 'b': return new Tuple(getEnemy(3),getEnemy(3));
            case 'g': return new Tuple(getEnemy(4),getEnemy(4));
            case 'k': return new Tuple(getEnemy(5),getEnemy(5));
            case 's': return new Tuple(getEnemy(6),getEnemy(6));
            case 'q': return new Tuple(getEnemy(7),getEnemy(7));
            case 'w': return new Tuple(getEnemy(9),getEnemy(8));
            case 'z': return new Tuple(getEnemy(9),getEnemy(9));
            case 'B': return new Tuple(getEnemy(10),getEnemy(10));
            case 'D': return new Tuple(getEnemy(11),getEnemy(11));
            case 'Q': return new Tuple(getEnemy(12),getEnemy(12));
            default:throw new IllegalArgumentException(c+" char is illegal");
        }
    }


    public static Player getPlayer(int i){
        return playerMap.get(i).get();
    }
    public static Enemy getEnemy(int i) { return enemyMap.get(i).get(); }

    /*

    //testing factory

    public static Tuple getTile(char c){//for testing
        switch (c){
            case 'C':
            case 'M':
            case 's':
            case 'k':
            case 'q':
            case 'z':
            case 'b':
            case 'g':
            case 'w':
            case 'B':
            case 'Q':
            case 'D':
            case '.': return new Tuple(new Empty(),null);
            case '@': return new Tuple(null,null);
            case  '#': return new Tuple(new Wall(),null);
            case 'K': return new Tuple(getEnemy(0),getEnemy(0));
            default:throw new IllegalArgumentException(c+" char is illegal");
        }

    }
     */



}
