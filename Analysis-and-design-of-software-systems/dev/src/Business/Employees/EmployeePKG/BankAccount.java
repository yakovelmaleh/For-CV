package Business.Employees.EmployeePKG;

import org.apache.log4j.Logger;

public class BankAccount {
    final static Logger log = Logger.getLogger(BankAccount.class);
    private int accountNum;
    private int bankBranch;
    private int bankID;


    public BankAccount(int[] bankDetails){
        this.accountNum = bankDetails[0];
        this.bankBranch = bankDetails[1];
        this.bankID = bankDetails[2];
    }

    /**
     * copy constructor
     * @param other
     */
    public BankAccount(BankAccount other){
        this.accountNum = other.accountNum;
        this.bankBranch = other.bankBranch;
        this.bankID = other.bankID;
    }

    public int getAccountNum() {
        return accountNum;
    }

    public int getBankID() {
        return bankID;
    }

    public int getBankBranch() {
        return bankBranch;
    }

    public void setAccountNum(int accountNum) {
        this.accountNum = accountNum;
    }

    public void setBankID(int bankID)  {
        this.bankID = bankID;
    }

    public void setBankBranch(int bankBranch) {
        this.bankBranch = bankBranch;
    }


    public int[] toArr(){
        return new int[]{accountNum,bankBranch,bankID};
    }
}
