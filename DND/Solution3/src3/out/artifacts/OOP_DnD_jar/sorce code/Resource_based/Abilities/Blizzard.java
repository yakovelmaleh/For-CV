package Resource_based.Abilities;

import Entity.Enemy.Enemy;
import Entity.Tile.Unit;
import Resource_based.Resources.Mana;

import java.util.List;
import java.util.Random;

public class Blizzard extends PlayerAbility {
    public Mana mana;
    int cost;
    int hitsCount;
    int spellPower;
    int lvl;
    public Blizzard(int M,int cost,int hit,int sp){
        mana=new Mana(M,cost);
        this.cost=cost;
        hitsCount=hit;
        spellPower=sp;
        lvl=1;
    }

    @Override
    public String useAbility(List<Unit> ls) {
        if (!canUse())
            return p.getName()+" can't use Blizzard.\n";
        mana.use();
        if (ls.size()>0){
            int hits=0;
            StringBuilder output=new StringBuilder();
            output.append(p.getName()).append(" used Blizzard.\n");
            while (ls.size()>0 & hits<hitsCount)
            {
                Unit u=ls.get(selectNumber(ls.size()));
                output.append(u.receiveCast(this));
                if (u.isDead())
                    ls.remove(u); //dont cast again on a dead enemy
                hits += 1;
            }
            return output.toString();
        }
        return p.getName()+" used Blizzard but has no enemies in range.\n";
    }

    public String attack(Enemy e) {
        int defRoll = (int) (Math.random() * e.getDef());
        if (spellPower > defRoll) {
            String output=(e.injured(spellPower,p));
            return output;
        }
        return e.getName()+" defended against the attack.\n";
    }

    @Override
    public String info() {
        return  "Mana: "+mana.getCur()+"/"+mana.getMax()+"."+(canUse()? " Blizzard available" : "");
    }

    private int selectNumber(int a){
        Random r=new Random();
        return r.nextInt(a);
    }

    @Override
    protected boolean canUse() {
        return mana.getCur()>=cost;
    }


    @Override
    public void LevelUp() {
        mana.LevelUp();
        lvl+=1;
        spellPower=spellPower+10*lvl;
    }

    @Override
    public void Tick() {
        mana.Tick();
    }

    @Override
    public String toString(){
        return "Type: Mage        Ability: Blizzard , spellpower: "+spellPower+" , Cost: "+cost+" , Hits: "+hitsCount+" , Starting mana: "+mana.getMax();
    }
}
