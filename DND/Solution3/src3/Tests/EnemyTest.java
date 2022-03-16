package Tests;
import Entity.Enemy.Enemy;
import Entity.Player.Player;
import Entity.Tile.Empty;
import Entity.Tile.Pos;
import Entity.Tile.TileFrame;
import GameControl.Board;
import GameControl.Utils;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

class EnemyTest {

    Enemy e;
    Player p;
    @BeforeEach
    public void InitTest(){
        int n=new Random().nextInt(Utils.playerMap.size());
        p=Utils.getPlayer(n);
        e=Utils.getEnemy(5);
    }
    @Test
    void receiveMove1(){
        e.setDef(0);
        p.setAtt(5000);
        e.reciveMove(p);
        Assert.assertTrue(e.isDead);
    }
    @Test
    void receiveMove2(){
        e.setDef(5000);
        p.setAtt(0);
        Assert.assertTrue(e.reciveMove(p).contains(" has "+e.getHp().getCur()+"/"+e.getHp().getMax()+" health left."));
    }

    @Test
    void abilityKill() {
        e.setFrame(new TileFrame(e,new Pos(2,2),new Board()));
        e.abilityKill();
        Assert.assertSame(e.getFrame().tile.getClass(),Empty.class);
    }

    @Test
    public void Injured(){
        e.getHp().setCur(15);
        e.setFrame(new TileFrame(e,new Pos(2,2),new Board()));
        e.injured(15,p);
        Assert.assertTrue(e.isDead);
    }
    @Test
    public void def1(){
        Enemy e=Utils.getEnemy(4);
        p.setDef(0);
        p.setHp(10);
        e.setAtt(1000);
        p.def(e);
        Assert.assertEquals(0,p.getHp().getCur());
    }
    @Test
    public void def2(){
        Enemy e=Utils.getEnemy(4);
        p.setDef(1000);
        e.setAtt(0);
        Assert.assertTrue(p.def(e).contains("blocked the attack taking no damage."));
    }
}