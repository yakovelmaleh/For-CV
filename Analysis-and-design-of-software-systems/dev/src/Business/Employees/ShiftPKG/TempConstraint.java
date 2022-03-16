package Business.Employees.ShiftPKG;

import Business.Type.ShiftType;
import org.apache.log4j.Logger;
import java.time.LocalDate;

public class TempConstraint extends Constraint {
    final static Logger log = Logger.getLogger(TempConstraint.class);
    //-------------------------------------fields------------------------------------

    private final LocalDate date;

    //------------------------------------constructor--------------------------------
    public TempConstraint(int CID, int EID, LocalDate date, ShiftType shiftType, String reason) {
        super(CID, EID,shiftType,reason);
        this.date = date;
        log.debug("Temp constraint "+getCID()+" created");
    }

    //for db
    public TempConstraint(int CID, int EID, LocalDate date, String shiftType, String reason) {
        super(CID, EID,shiftType,reason);
        this.date = date;
        log.debug("Temp constraint "+getCID()+" created");
    }
    //--------------------------------------methods----------------------------------

    @Override
    public boolean relevant(LocalDate date, ShiftType shiftType) {
        return ((this.date.equals(date)) && (this.shiftType.equals(shiftType)));
    }

    @Override
    public String getStringDate() {
        return date.toString();
    }

    public LocalDate getDate() {
        return date;
    }
}
