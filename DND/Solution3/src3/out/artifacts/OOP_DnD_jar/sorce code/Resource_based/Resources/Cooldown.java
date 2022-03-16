package Resource_based.Resources;

public class Cooldown extends Resource {
    public Cooldown(int max) {
        super(max);
        setCur(0);
    }

    public void Tick() {
        setCur(cur-1);
    }
    public void LevelUp(){setCur(0);}
}
