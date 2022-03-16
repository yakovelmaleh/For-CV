package Business.Employees.ShiftPKG;

import Business.Type.ShiftType;
import org.apache.log4j.Logger;
import java.time.LocalDate;

public abstract class Constraint {
    final static Logger log = Logger.getLogger(Constraint.class);
    //--------------------------------fields-----------------------------

    private final int CID;
    private final int EID;
    protected ShiftType shiftType;
    private  String reason;

    //------------------------------constructor---------------------------

    Constraint(int CID, int EID, ShiftType shiftType, String reason){
        this.CID = CID;
        this.EID = EID;
        this.shiftType = shiftType;
        this.reason = reason;
    }

    Constraint(int CID, int EID, String shiftType, String reason){
        this.CID = CID;
        this.EID = EID;
        this.shiftType = ShiftType.valueOf(shiftType);
        this.reason = reason;
    }
    //---------------------------------methods----------------------------

    public abstract boolean relevant(LocalDate date, ShiftType shiftType);

    public abstract String getStringDate();  //return date or "every"+day

    public void updateReason(String newReason){
        reason = newReason;
    }

    public void updateShiftType(ShiftType newShiftType){
        shiftType = newShiftType;
    }

    //-------------------------------getters&setters----------------------

    public int getCID() {
        return CID;
    }
    public int getEID() {return EID;}
    public ShiftType getShiftType() {return shiftType;}
    public String getReason() {return reason;}



}
