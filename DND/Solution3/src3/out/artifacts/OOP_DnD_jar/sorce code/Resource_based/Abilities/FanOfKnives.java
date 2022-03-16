package Resource_based.Abilities;

import Entity.Enemy.Enemy;
import Entity.Tile.Unit;
import Resource_based.Resources.Energy;

import java.util.List;

public class FanOfKnives extends PlayerAbility {
    Energy energy;
    int cost;
    public FanOfKnives(int cost){
        energy=new Energy(100,cost);
        energy.setCur(energy.getMax());
        this.cost=cost;
    }
    @Override
    public String useAbility(List<Unit> ls) {
        if (!canUse())
            return p.getName()+" can't use Fan of Knives.\n";
        energy.setCur(energy.getCur()-cost);
        if (ls.size()==0)
            return p.getName()+" used Fan of Knives but has no enemies in range\n";
        StringBuilder output=new StringBuilder();
        for(Unit u: ls) {
            output.append(u.receiveCast(this));
        }
        return output.toString();
    }
    public String attack(Enemy e) {
        int defRoll = (int) (Math.random() * e.getDef());
        if (p.getAtt() > defRoll) {
            return e.injured(p.getAtt() - defRoll, p);
        }
        return e.getName()+" defended against the attack.\n";
    }

    @Override
    public String info() {
        return  "Energy: "+energy.getCur()+"/"+energy.getMax()+"."+(canUse()? " Fan of Knives available" : "");
    }

    @Override
    protected boolean canUse() {
        return cost<=energy.getCur();
    }

    @Override
    public void LevelUp() {
        energy.setCur(100);
    }

    @Override
    public void Tick() { energy.Tick();
    }

    @Override
    public String toString(){
        return "Type: Rogue       Ability: Fan of Knives , Cost: "+cost+" , Max Energy: "+energy.getMax();
    }
}
