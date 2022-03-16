package Resource_based.Resources;

public class Arrows extends Resource {
    int tick=0;
    int lvl;

    public Arrows(int max) {
        super(max);
        cur=max;
        lvl=1;
    }

    public void Tick(){
        if(tick==10){
            SetArrows(cur+lvl);
            tick=0;
        }
        else tick++;
    }
    public void SetArrows(int arr){
        if (arr>=0)
            cur=arr;
    }
    public void LevelUp(){
        lvl++;
        SetArrows(cur+10*lvl);
    }
    public void use(){
        SetArrows(cur-1);
    }

}
