package Business.Type;

// Pair class
public class Pair<U, V>
{
    private final  U fir;       // the first field of a pair
    private final V sec;      // the second field of a pair

    // Constructs a new pair with specified values
    public Pair(U first, V second)
    {
        this.fir = first;
        this.sec = second;
    }

    @Override
    // Checks specified object is "equal to" the current object or not
    public boolean equals(Object o)
    {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Pair<?, ?> pair = (Pair<?, ?>) o;

        // call `equals()` method of the underlying objects
        if (!fir.equals(pair.fir)) {
            return false;
        }
        return sec.equals(pair.sec);
    }

    @Override
    // Computes hash code for an object to support hash tables
    public int hashCode()
    {
        // use hash codes of the underlying objects
        return 31 * fir.hashCode() + sec.hashCode();
    }

    @Override
    public String toString() {
        return "( " + fir + ", Quantity: " + sec + ")";
    }

    public U getFir() {
        return fir;
    }

    public V getSec() {
        return sec;
    }
}

