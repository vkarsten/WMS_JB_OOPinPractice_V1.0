package data;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import app.TheWarehouseApp;

public class AdminServiceImpl implements AdminService{

    // To read inputs from the console/CLI
    private Scanner reader = new Scanner(System.in);

    private String[] adminOptions = { "1. List orders by warehouse", "2. List orders by status",
            "3. List Orders whose total cost greater than provided value", "4. Quit" };

    //get all orders using OrderRepository
    private static final List<Order> allOrders = OrderRepository.getAllOrders();

    @Override
    public void authenticateAdmin() {
        //prompt for password
        while (!TheWarehouseApp.SESSION_USER.isAuthenticated()) {
            //prompt for password while admin is not valid i.e the password matches
            System.out.println("Please enter your password:");
            String password = reader.nextLine();

            if (UserRepository.isAdminValid(TheWarehouseApp.SESSION_USER.name, password))
                TheWarehouseApp.SESSION_USER = new Admin(TheWarehouseApp.SESSION_USER.name, password);
            else if (!this.confirm("This did not work! Try again?")) {
                this.quit();
                // How to continue as a guest?
            }
            //set the SESSION_USER's isAuthenticated property to true.
            TheWarehouseApp.SESSION_USER.authenticate(password);
        }

    }

    @Override
    public int getAdminsChoice() {
        // TODO implement
        int choice = 0;
        // List all options
        System.out.println("What would you like to do?");
        for (String option : this.adminOptions) {
            System.out.println(option);
        }

        System.out.print("Type the number of the operation: ");
        // Keep asking for admin's choice until a valid value is received
        do {
            String selectedOption = this.reader.nextLine();
            try {
                choice = Integer.parseInt(selectedOption);
            } catch (Exception e) {
                choice = 0;
            }
            // Guide the user to enter correct value
            if (choice < 1 || choice > adminOptions.length) {
                System.out.printf("Sorry! Enter an integer between 1 and %d. ", this.adminOptions.length);
            }
        } while (choice < 1 || choice > adminOptions.length);

        // return the valid choice
        return choice;
    }

    @Override
    public void performAction(int option) {
        // TODO implement

        switch (option) {
            case 1: // "1. List orders by warehouse"
                this.listOrdersByWarehouse();
                break;
            case 2: // "2. List order by status"
                this.listOrdersByStatus();
                break;
            case 3: // 3. Sort orders as per totalCost (descending)
                this.listOrdersWhoseTotalCostGreaterThan();
                break;
            case 4: // "4. Quit"
                this.quit();
                break;
            default:
                System.out.println("Sorry! Invalid option.");
        }
    }

    @Override
    public void listOrdersByWarehouse() {
        //prompt user to enter the warehouse id
        System.out.println("Please enter the warehouse id:");
        int id;
        try {
            id = Integer.parseInt(reader.nextLine());
        } catch (Exception e) {
            System.out.println("The id was not valid!");
            return;
        }

        //create an empty list of order
        List<Order> result = new ArrayList<>();

        //Iterate over all the orders obtained from OrderRepository and add the orders belonging to the desired warehouse to the list
        for (Order order : allOrders) {
            if (order.getWarehouse() == id) result.add(order);
        }

        // use the printsListOfOrdersToConsole method and pass the created list as argument.
        printsListOfOrdersToConsole(result);

        //Add the action detail string to the SESSION_ACTIONS in app.TheWarehouseApp. Eg: 'Listed Orders of warehouse ' + {warehouseId}
        logSessionAction("Listed orders of warehouse " + id);
    }

    @Override
    public void listOrdersByStatus() {
        // Keep asking for user's choice until a valid value is received
        int choice = 0;
        System.out.println("List Orders by Status : choose 1, 2 or 3");
        System.out.println("1. NEW");
        System.out.println("2. PROCESSING");
        System.out.println("3. DELIVERED");

        // prompt user to select 1, 2 or 3 unless valid choice is made.
        do {
            String selectedOption = this.reader.nextLine();
            try {
                choice = Integer.parseInt(selectedOption);
            } catch (Exception e) {
                choice = 0;
            }
            // Guide the user to enter correct value
            if (choice < 1 || choice > 3) {
                System.out.println("Sorry! Enter an integer between 1 and 3");
            }
        } while (choice < 1 || choice > 3);

        String status = "";

        switch(choice) {
            case 1:
                status = "NEW";
                break;
            case 2:
                status = "PROCESSING";
                break;
            case 3:
                status = "DELIVERED";
                break;
            default:
                break;

        }

        //initialize new empty list of order
        List<Order> result = new ArrayList<>();

        //iterate over all the orders from OrderRepository and add only the orders with desired status to the list
        for (Order order : allOrders) {
            if (order.getStatus().equals(status)) result.add(order);
        }

        // use the printsListOfOrdersToConsole method and pass the created list as argument.
        printsListOfOrdersToConsole(result);

        //Add the action detail string to the SESSION_ACTIONS in app.TheWarehouseApp. Eg: 'Listed Orders with status ' + {status}
        logSessionAction("Listed orders with status " + status);
    }

    @Override
    public void listOrdersWhoseTotalCostGreaterThan() {
        double marginalValue = 0;

        //prompt user to enter marginal value for total cost until a valid numerical value is entered.
        System.out.println("Enter a marginal value for the total cost:");
        do {
            String limit = this.reader.nextLine();
            try {
                marginalValue = Double.parseDouble(limit);
            } catch (Exception e) {
                marginalValue = -1;
            }
            // Guide the user to enter correct value
            if (marginalValue < 0) {
                System.out.println("Sorry! Enter a valid numerical value");
            }
        } while (marginalValue < 0);

        //initialize an empty list of orders
        List<Order> result = new ArrayList<Order>();

        //iterate over all the orders from OrderRepository and add only the orders that meets the criteria to the list
        for (Order order: allOrders) {
            if (order.getTotalCost() >= marginalValue) result.add(order);
        }

        // use the printsListOfOrdersToConsole method and pass the created list as argument.
        printsListOfOrdersToConsole(result);

        //Add the action detail string to the SESSION_ACTIONS in app.TheWarehouseApp. Eg: 'Listed Orders with total cost greater than ' + {marginalValue}
        logSessionAction("Listed orders with total cost greater than " + marginalValue);
    }


    @Override
    public void printsListOfOrdersToConsole(List<Order> orders) {
        if(orders.size() == 0) {
            System.out.println("No order found");
        }
        for(Order order: orders) {
            System.out.println("===============================================================================================================================");
            System.out.println("status : "+ order.getStatus() + ", isPaymentDone : " + order.isPaymentDone() + ", warehouse: " + order.getWarehouse() + ", dateOfOrder : " + order.getDateOfOrder()
                    + ", totalCost : " + order.getTotalCost());
            System.out.println( "orderItems : " + order.getOrderItems());
            System.out.println("================================================================================================================================");
        }

    }


    /**
     * Confirm an action
     *
     * @return action
     */
    public boolean confirm(String message) {
        System.out.printf("%s (y/n)\n", message);
        return (this.reader.nextLine().toLowerCase().startsWith("y"));
    }

    private void logSessionAction(String action) {
        TheWarehouseApp.SESSION_ACTIONS.add(action);
    }

    @Override
    public void quit() {
        //implement as similar to app.TheWarehouseManager.quit();
        TheWarehouseApp.SESSION_USER.bye(TheWarehouseApp.SESSION_ACTIONS);
        System.exit(0);
    }
}