package Business.ApplicationFacade;

public class Response {
    public String error;
    public boolean isError=false;

    public Response(String error) {
        this.error = error;
        isError=true;
    }
    public Response(){}

    public String getError() {
        return error;
    }

    public boolean isError() {
        return isError;
    }
}
