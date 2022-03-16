package Resource_based.Abilities;

import Entity.Enemy.Enemy;
import Entity.Player.Player;
import Entity.Tile.Unit;

import java.util.List;

public abstract class PlayerAbility{ //generic player ability
    Player p; //holds the player who casts it
    public void setPlayer(Player p){this.p=p;}
    public abstract String useAbility(List<Unit> ls); // cast on a list of all enemies in range
    protected abstract boolean canUse();
    public abstract void LevelUp();
    public abstract void Tick();
    public abstract String attack(Enemy e); // cast on single enemy
    public abstract String info();
}
