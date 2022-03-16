package Business.SupplierBusiness.facade;

public class Tresponse <T> extends response{
    private T outObject;

    public Tresponse(T outObject) { this.outObject = outObject;}

    public Tresponse(String msg){ super(msg);}

    public T getOutObject() { return outObject; }
}
