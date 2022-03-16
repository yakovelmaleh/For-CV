package DataAccess;

public abstract class DALObject {
    protected DalController DC;
    public DALObject(DalController dc){ DC=dc;}
    public abstract String getCreate();
    public abstract String getSelect();
    public abstract String getDelete();
    public abstract String getUpdate();
    public abstract String getInsert();

}
