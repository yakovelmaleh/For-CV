package Business.Employees.ShiftPKG;

import Business.Type.ShiftType;
import org.apache.log4j.Logger;

import java.time.LocalDate;
import java.time.DayOfWeek;

public class ConstConstraint extends Constraint {
    final static Logger log = Logger.getLogger(ConstConstraint.class);
    //-------------------------------------fields------------------------------------

    private final DayOfWeek day;

    //------------------------------------constructor--------------------------------

    public ConstConstraint(int CID, int EID, DayOfWeek day, ShiftType shiftType, String reason) {
        super(CID, EID, shiftType, reason);
        this.day = day;
        log.debug("Const constraint " + getCID() + " created");
    }

    //for db
    public ConstConstraint(int CID, int EID, DayOfWeek day, String shiftType, String reason) {
        super(CID, EID, shiftType, reason);
        this.day = day;
        log.debug("Const constraint " + getCID() + " created");
    }

    //--------------------------------------methods----------------------------------

    @Override
    public boolean relevant(LocalDate date, ShiftType shiftType) {
        return ((date.getDayOfWeek().equals(day)) && ((this.shiftType).equals(shiftType)));
    }

    @Override
    public String getStringDate() {
        return "Every " + day;
    }


    public DayOfWeek getDay() {
        return day;
    }



}
