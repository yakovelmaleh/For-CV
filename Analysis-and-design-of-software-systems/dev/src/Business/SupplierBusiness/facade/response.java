package Business.SupplierBusiness.facade;

public class response {
    private final String error;
    private final boolean isError;

    public response(){
        error = null;
        isError = false;
    }

    public response(String error){
        this.error = error;
        isError = true;
    }

    public boolean isError() {
        return isError;
    }

    public String getError() {
        return error;
    }
}
