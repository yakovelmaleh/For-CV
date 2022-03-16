package Resource_based.Abilities;
import Entity.Enemy.Enemy;
import Entity.Tile.Unit;
import Resource_based.Resources.Cooldown;
import java.util.List;
import java.util.Random;

public class AvengersShield extends PlayerAbility {
    Cooldown cool;

    public AvengersShield(int a) {
        cool=new Cooldown(a);
    }

    @Override
    public String useAbility(List<Unit> ls ) {
        if (!canUse())
            return p.getName()+"can't use Avengers Shield.\n";
        if (ls.size()>0){
            Unit u=ls.get(selectNumber(ls.size()));
            String output=u.receiveCast(this);
            cool.setCur(cool.getMax());
            return output;
        }
        cool.setCur(cool.getMax());
        return p.getName()+" used Avengers Shield but has no enemies in range.\n";
    }

    public String attack(Enemy e){
        int defRoll = (int) (Math.random() * e.getDef());
        int attWar=(int)(0.1*p.getHp().getMax());
        if (attWar>defRoll)
        {
            return (e.injured(attWar,p));
        }
        return e.getName()+" defended against the attack.\n";
    }

    @Override
    public String info() {
        return  "Avengers Shield available"+(canUse()? "." : " in "+cool.getCur()+" turns.");
    }

    private int selectNumber(int a){
        Random r=new Random();
        return r.nextInt(a);
    }

    @Override
    protected boolean canUse() {
        return cool.getCur() == 0;
    }

    @Override
    public void LevelUp() {
        cool.LevelUp();
    }

    @Override
    public void Tick() {
         cool.Tick();
    }

    @Override
    public String toString(){
        return "Type: Warrior     Ability: Avengers Shield , Cooldown: "+cool.getMax();
    }

}
