package Entity.Player;

import Resource_based.Abilities.AvengersShield;

public class Warrior extends Player {

    public Warrior(int att,int def,String name, int HP,int ab){
        super(att,def,name,HP,3,new AvengersShield(ab));
    }


    @Override
    public void levelUpSpacialAbility(){
        hp.setMax(hp.getMax()+5*lvl);
        hp.setCur(hp.getMax());
        att=att+2*lvl;
        def+=1;
        super.levelUpSpacialAbility();
    }

}
