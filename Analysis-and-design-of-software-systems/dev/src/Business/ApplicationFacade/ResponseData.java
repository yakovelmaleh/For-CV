package Business.ApplicationFacade;
public class ResponseData<T> extends Response {
    public T data;
    public ResponseData(String error) {
        super(error);
    }
    public T getData() {
        return data;
    }
    public ResponseData(T data) {
        this.data = data;
    }
    public ResponseData(T data, String error) {
        super(error);
        this.data = data;
    }
}
