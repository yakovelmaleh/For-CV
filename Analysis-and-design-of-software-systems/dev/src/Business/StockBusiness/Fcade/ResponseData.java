package Business.StockBusiness.Fcade;

public class ResponseData<T> extends Response{
    public T data;
    public ResponseData(String error) {
        super(error);
    }
    public T getData() {
        return data;
    }
    public ResponseData(T data) {
        isError=false;
        error="";
        this.data = data;
    }

    @Override
    public String toString() {
        if(isError()) return super.toString();
        return data.toString();
    }
}
