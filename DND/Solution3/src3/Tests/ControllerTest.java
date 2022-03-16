package Tests;
import Entity.Player.Player;
import GameControl.Controller;
import GameControl.Utils;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

class ControllerTest {
    public Player p;
    public Controller c;

    @BeforeEach
    public void initTest(){
        p=Utils.getPlayer(1);
        c=new Controller(p,"");
    }

    @Test
    void stats() {
        String check=c.stats();
        Assert.assertTrue("check attack",check.contains("Attack: "+p.getAtt()));
        Assert.assertTrue("check attack",check.contains("Defence: "+p.getDef()));
    }

    @Test
    void finish() {
        p.isDead=true;
        Assert.assertTrue(c.finish());
        p.isDead=false;
        Assert.assertFalse(c.finish());
    }

    @Test
    void endLevel() {
        c.enemyList.add(c.curBoard,new LinkedList<>());// insure that we finish this level.
        Assert.assertTrue("fail to finish the game",c.endLevel().contains("you finished level:"));
        c.enemyList.add(c.enemyList.size(),new LinkedList<>());
        c.curBoard=c.enemyList.size()-1;
        Assert.assertTrue("fail to finish the game",c.endLevel().contains("Congratulations, you won the game!"));
    }

    @Test
    void display() {
        p.isDead=true;
        Assert.assertEquals("display1",c.display(),"");
    }

    @Test
    void action1() {
        try{
            c.action("152");
            Assert.fail();
        }
        catch (IllegalArgumentException e){
            Assert.assertEquals("","","");
        }
        catch (Exception c){
            Assert.fail();
        }
    }

    @Test
    void action2() {
        try{
            c.action(null);
            Assert.fail();
        }
        catch (IllegalArgumentException e){
            Assert.assertEquals("","","");
        }
        catch (Exception c){
            Assert.fail();
        }
    }
    @Test
    void action3() {
        try{
            c.action("152");
            Assert.fail();
        }
        catch (IllegalArgumentException e){
            Assert.assertEquals("","","");
        }
        catch (Exception c){
            Assert.fail();
        }
    }

    @Test
    void enemyTurn() {
        c.enemyList.add(c.curBoard,new LinkedList<>());
        Assert.assertEquals("we check what happen is there are no enemies.", c.enemyTurn(),"");
    }
}