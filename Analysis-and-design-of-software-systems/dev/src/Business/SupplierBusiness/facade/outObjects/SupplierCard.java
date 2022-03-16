package Business.SupplierBusiness.facade.outObjects;

import java.util.Dictionary;
import java.util.Enumeration;


public class SupplierCard{
    private final int supplierBN;
    private final String supplierName;
    private final int bankNumber;
    private final int branchNumber;
    private final int accountNumber;
    private final String payWay;
    private final Dictionary<String , String> contactPhone;
    private final Dictionary<String , String> contactEmail;

    public SupplierCard(Business.SupplierBusiness.SupplierCard supplierCard) {
        supplierBN = supplierCard.getSupplierBN();
        supplierName = supplierCard.getSupplierName();
        bankNumber = supplierCard.getSupplierBankNumber();
        branchNumber = supplierCard.getSupplierBranchNumber();
        accountNumber = supplierCard.getSupplierAccountNumber();
        payWay = supplierCard.getSupplierPayWay();
        contactPhone = supplierCard.getContactPhone();
        contactEmail =  supplierCard.getContactEmail();
    }

    public String toString() {
        return "SupplierCard: \n" +
                "\tsupplierBN: " + supplierBN + "\n" +
                "\tsupplier name: " + supplierName + "\n" +
                "\tbank number: " + bankNumber + "\n" +
                "\tbrunch number: " + branchNumber + "\n" +
                "\taccount number: " + accountNumber + "\n" +
                "\tpayWay: " + payWay + "\n" +
                "\tcontact Phone: " + helpPrint(contactPhone , true) + "\n" +
                "\tcontact Email: " + helpPrint(contactEmail , false);
    }

    private String helpPrint(Dictionary<String , String> dictionary , boolean p_e){
        if (dictionary == null)
            return "dictionary not exist";
        int size = 0;
        String toReturn = "";
        Enumeration<String> e = dictionary.keys();
        while (e.hasMoreElements() && size < dictionary.size()){
            String element = e.nextElement();
            if(p_e) toReturn = toReturn + "\n\t\tname: " + dictionary.get(element) + " , phone: " + element;
            else toReturn = toReturn + "\n\t\tname: " + dictionary.get(element) + " , email: " + element;
            size ++;
        }
        return toReturn;
    }

    public String toStringId(){ return supplierBN + "";}

    public int getSupplierBN() {
        return supplierBN;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public String getPayWay() {
        return payWay;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public int getBankNumber() {
        return bankNumber;
    }

    public int getBrunchNumber() {
        return branchNumber;
    }
}
