package Entity.Player;

import Resource_based.Abilities.FanOfKnives;

public class Rogue extends Player {
    public Rogue(int att, int def, String name, int HP,int cost) {
        super(att, def, name, HP,2,new FanOfKnives(cost));
    }
    @Override
    public void levelUpSpacialAbility(){
        att=att+(lvl*3);
        super.levelUpSpacialAbility();
    }
}
