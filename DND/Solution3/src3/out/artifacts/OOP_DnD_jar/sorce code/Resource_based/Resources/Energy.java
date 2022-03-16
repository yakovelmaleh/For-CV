package Resource_based.Resources;

public class Energy extends Resource {
    int cost;
    public Energy(int max,int cost) {
        super(max);
        this.cost=cost;
        cur=max;
    }

    public void Tick(){
        setCur(cur+10);
    }

}
