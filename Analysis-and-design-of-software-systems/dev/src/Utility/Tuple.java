package Utility;
public class Tuple<T,S> {
    public Tuple(T i1, S i2){
        item1=i1;
        item2=i2;
    }
    public T item1;
    public S item2;

    @Override
    public String toString() {
        return "Tuple{" +
                "item1=" + item1 +
                ", item2=" + item2 +
                '}';
    }
}
