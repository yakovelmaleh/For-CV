package Entity;

import Entity.Enemy.Enemy;
import Entity.Tile.Tile;
import Entity.Tile.Unit;

import java.util.List;

public interface Heroic {
    public String cast(List<Unit> ls);
}
