package Resource_based.Abilities;

import Entity.Enemy.Enemy;
import Entity.Player.Player;
import Entity.Tile.Unit;
import GameControl.Utils;
import Resource_based.Resources.Arrows;


import java.util.List;

public class Shoot extends PlayerAbility {
    Arrows arr;
    public Shoot(){
        arr=new Arrows(10);
        arr.setCur(arr.getMax());
    }
    @Override
    public String useAbility(List<Unit> ls) {
        if (!canUse())
            return p.getName()+" does not have any arrows.\n";
        if (ls.size()==0)
            return p.getName()+" does not have enemies in range.\n";
        Unit u=selectClosest(ls,p);
        String output=u.receiveCast(this);
        arr.use();
        return output;
    }
    public String attack(Enemy e){
        int defRoll = (int) (Math.random() * e.getDef());
        if (p.getAtt()>defRoll){
            return e.injured(p.getAtt()-defRoll, p);
        }
        return e.getName()+" defended against the attack.\n";
    }

    @Override
    public String info() {
        return "Arrows left: "+arr.getCur();
    }

    private Unit selectClosest(List<Unit> units,Player p){
        Unit output=units.get(0);
        double MinRange=Utils.RANGE(output.getFrame(),p.getFrame());
        if (MinRange==1)
            return output;
        for (Unit u: units){
            double range=Utils.RANGE(u.getFrame(),p.getFrame());
            if (range==1)
                return u;
            if (range<MinRange)
            {
                MinRange=range;
                output=u;
            }
        }
        return output;
    }

    @Override
    protected boolean canUse() {
        return arr.getCur()>0;
    }

    @Override
    public void LevelUp() {
        arr.LevelUp();
    }

    @Override
    public void Tick() {
         arr.Tick();
    }

    @Override
    public String toString(){
        return "Type: Hunter      Ability: Shoot , starting arrows: "+arr.getCur();
    }
}
