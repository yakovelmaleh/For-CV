package Entity.Player;

import Resource_based.Abilities.Blizzard;

public class Mage extends Player {
    public Mage(int att, int def, String name, int HP, int range, int manaPool,int cost, int sp, int hit) {
        super(att, def, name, HP,range,new Blizzard( manaPool, cost,hit,sp));
    }

}
