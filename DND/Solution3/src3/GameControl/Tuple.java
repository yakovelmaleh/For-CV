package GameControl;

import Entity.Enemy.Enemy;
import Entity.Tile.Tile;

public class Tuple<T,E>{ //standard tuple, not native in java
    private T t;
    private E e;
    Tuple(T t, E e){
        this.t=t;
        this.e=e;
    }

    public T getT() {
        return t;
    }

    public E getE() {
        return e;
    }

    @Override
    public String toString(){
        return t.toString()+" , "+e.toString();
    }
}
