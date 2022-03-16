package Entity.Enemy;

import Entity.Heroic;
import Entity.Player.Player;
import Entity.Tile.Unit;
import GameControl.Utils;
import Resource_based.Abilities.BossAbility;

import java.util.LinkedList;
import java.util.List;

public class Boss extends Monster implements Heroic {
    BossAbility ability;
    int frequency;
    int tick;
    public Boss(char c, int att, int def, int EXP, String name, int HP, int v,int f) {
        super(c, att, def, EXP, name, HP, v);
        frequency=f;
        tick=f; // boss starts with ability ready
        ability=new BossAbility();
        ability.SetBoss(this);
    }


    @Override
    public String cast(List<Unit> ls) { // cast this bosses ability
        if (ls==null)
            throw  new IllegalArgumentException("boss accept null");
        if (ls.size()>0) {
            tick=0;
            return ability.useAbility(ls);
        }
        return "";
    }
    public String Turn(Player p){ //this boss performs its turn
        if(Utils.RANGE(p.getFrame(),frame)<vision_range & tick==frequency){ // if player in range and ability ready cast ability
            List<Unit> u=new LinkedList<>();
            u.add(p);
            String output=cast(u);
            Tick();
            return output;
        }
        Tick();
        return super.Turn(p); //if player not in range or ability not ready act normally
    }

    public void Tick(){ //advance ability tick
        if (tick<frequency)
            tick++;
    }

}
