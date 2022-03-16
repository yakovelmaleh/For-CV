package Presentation.Menu;

import Business.ApplicationFacade.outObjects.TransportationServiceDTO;
import Business.Type.Area;
import Presentation.Controllers;
import Presentation.TransportationController;

import java.util.List;
import java.util.Scanner;

public class TransportationMenu extends Menu{
    private int option;
    private int subOption;
    private final Area[] areas={Area.South,Area.North,Area.Center };
    private final TransportationController transportationController;
    private boolean finish;


    public TransportationMenu(Controllers r , Scanner input){
        super(r,input);
        transportationController =new TransportationController(r.getMc());
        this.option=0;
        subOption=0;
        finish=false;
    }

    /**
     * Main function of the menu.
     */
    @Override
    public void show(){
        while(!finish){
            chooseOption();
        }
    }


    /**
     *The starting choice of the user if to keep run the system or shut it off.
     */
    public void chooseOption(){
        System.out.println("\n****************** Transportation menu *******************\n");
        System.out.print("1) See all Transportations.\n2) Delete Transportation.\n3)Previous menu\nOption: ");
        option = chooseOp(3);
        switch (option){
            case 1:
                printAllTransportations();
                break;
            case 2:
                delete();
                break;
            case 3:
                finish=true;
                break;
        }
        System.out.println();
    }

    /**
     * Delete transportation
     */
    private void delete() {
        System.out.println("************ Delete a Transportation ************\n");
        System.out.print("Please enter order id to delete: ");
        long id= input.nextLong();
        try {
            if(transportationController.delete(id))
                System.out.println("Deleted transportation number "+id+" successfully." );
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }



    /**
     *Prints to the user all the available transportations in the database
     * Used for the transportations printing option in the menu.
     */
    public void printAllTransportations(){
        System.out.println("************ Transportations List ************\n");
        List<TransportationServiceDTO> sup= transportationController.getAllTransportations();
        if(!sup.isEmpty())
        for (TransportationServiceDTO tru:sup) { System.out.println(tru); }
        else{
            System.out.println("No transportations available.");
        }
    }

    /**
     *The starting menu of the system.
     * runs by the main of the project.
     * By user's input it keep running the system or shut it off.
     * @return : if to keep run the program or terminate it
     */

    /**
     *Method to receive an input from the user with boundary limit.
     * @param con : the num of options the user can type. For boundary check.
     * @return : the choice of the user.
     */
    private int chooseOp(int con){
        boolean validInput = false;
        int userOption = -1;
        while (!validInput) {
            userOption = input.nextInt();
            if((userOption <= con) && (userOption >= 0)) {
                validInput = true;
            }else {
                System.out.println("your choose without bounds");
            }
        }
        return userOption;
    }
}
