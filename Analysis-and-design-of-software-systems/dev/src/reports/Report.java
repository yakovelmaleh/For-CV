package reports;

import java.util.Date;

public interface Report {
    //field DateTime
    public String getType();
    public int getStore();
    public Date getDate();
    public int sizeOfList();
}
