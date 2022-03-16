package Tests;
import Entity.Enemy.Boss;
import Entity.Enemy.Enemy;
import GameControl.Utils;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import Entity.Player.*;

import java.util.Random;


class PlayerTest {
    Player p;

    @BeforeEach
    public void InitTest(){
        int n=new Random().nextInt(Utils.playerMap.size());
        p=Utils.getPlayer(n);
    }
    @Test
    public void checkConst1(){
        Assert.assertEquals(1,p.lvl);
    }
    @Test
    public void checkConst2(){

        Assert.assertEquals(0,p.exp);
    }
    @Test
    public void levelUp1(){
        p.exp=0;
        p.nextExp=100;
        Assert.assertEquals("check what happen if does not exist exp",p.levelUp(),"");
    }
    @Test
    public void levelUp2(){
        p.exp=0;
        p.nextExp=100;
        Assert.assertEquals("check level",1,p.lvl);
    }


    @Test
    public void levelUp3(){
        p.exp=100;
        p.nextExp=100;
        int att=p.getAtt();
        p.levelUp();
        Assert.assertTrue(p.getAtt()>=att+4*p.lvl);

    }
    @Test
    public void levelUp4(){
        p.exp=100;
        p.nextExp=100;
        p.levelUp();
        Assert.assertEquals("check if exp change",0,p.exp);
    }

    @Test
    public void levelUp5(){
        p.exp=100;
        p.nextExp=100;
        p.levelUp();
        Assert.assertEquals("check if exp change",150,p.nextExp);
    }
    @Test
    public void levelUp6(){
        p.exp=100;
        p.nextExp=100;
        p.levelUp();
        Assert.assertEquals("check level",2,p.lvl);
    }
    @Test
    public void levelUp7(){
        p.exp=100;
        p.nextExp=100;
        int def=p.getDef();
        p.levelUp();
        Assert.assertTrue(p.getDef()>=def+p.lvl);
    }
    @Test
    public void levelUp8(){
        p.exp=100;
        p.nextExp=100;
        Assert.assertEquals(p.getHp().getCur(), p.getHp().getMax());
        p.levelUp();
        Assert.assertEquals( p.getHp().getMax(),p.getHp().getCur());
    }
    @Test
    public void die(){
        p.die(p);
        Assert.assertEquals('X',p.getChr());
    }
    @Test
    public void checkLevelUp(){
        Assert.assertEquals("",p.checkLevelUp());
    }
    @Test
    public void Injured1(){
        Boss b=new Boss('K', 300, 150, 5000, "Night’s King", 5000, 8, 10);
        p.getHp().setCur(15);
        p.injured(1,b);
        Assert.assertEquals(14,p.getHp().getCur());
    }
    @Test
    public void Injured2(){
        Boss b=new Boss('K', 300, 150, 5000, "Night’s King", 5000, 8, 10);
        p.getHp().setCur(15);
        p.injured(15,b);
        Assert.assertTrue(p.isDead);
    }
    @Test
    public void Injured3(){
        Boss b=new Boss('K', 300, 150, 5000, "Night’s King", 5000, 8, 10);
        p.getHp().setCur(15);
        p.injured(15,b);
        Assert.assertEquals('X',p.getChr());
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