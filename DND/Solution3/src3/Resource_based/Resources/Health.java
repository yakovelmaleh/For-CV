package Resource_based.Resources;

public class Health extends Resource {
    public Health(int max){
        super(max);
        this.cur=max;
    }


    public void levelUpHP(int level){
        setMax(max+level*10);
        setCur(max);
    }

}
