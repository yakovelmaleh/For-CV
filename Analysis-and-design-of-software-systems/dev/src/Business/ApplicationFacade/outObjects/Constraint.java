package Business.ApplicationFacade.outObjects;

public class Constraint {
    public int CID;
    public int EID;
    public String shiftType;
    public String reason;
    public String date;


    public Constraint(Business.Employees.ShiftPKG.Constraint c){
        this.CID = c.getCID();
        this.EID = c.getEID();
        this.reason = c.getReason();
        this.shiftType = c.getShiftType().name();
        this.date = c.getStringDate();
    }
    @Override
    public String toString() {
        return
                "Constraint ID: " + CID +
                 ", Date: " + date +
                ", ShiftType: '" + shiftType + '\'' +
                ", Reason: '" + reason + '\'';
    }
}
