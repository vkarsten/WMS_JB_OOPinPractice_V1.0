package app;

import java.util.ArrayList;
import java.util.List;

import data.AdminServiceImpl;
//import data.OrderRepository;
import data.User;
import data.UserRepository;

public class TheWarehouseApp {
    /**
     * Execute the <i>app.TheWarehouseApp</i>
     *
     * @param args
     */

    public static List<String> SESSION_ACTIONS = new ArrayList<String>();
    public static boolean IS_EMPLOYEE = false;
    public static User SESSION_USER;

    public static void main(String[] args) {
        TheWarehouseManager theManager = new TheWarehouseManager();

        // Welcome User
        theManager.welcomeUser();

        // Get the user's choice of action and perform action
        do {
            if(!UserRepository.isUserAdmin(TheWarehouseApp.SESSION_USER.getName())) {
                int choice = theManager.getUsersChoice();
                theManager.performAction(choice);

            } else { //If user is admin

                // prompt for password and allow further actions if authenticated:

                AdminServiceImpl adminService = new AdminServiceImpl();

                if(!SESSION_USER.isAuthenticated()) {
                    adminService.authenticateAdmin();
                }

                int choice = adminService.getAdminsChoice();

                adminService.performAction(choice);

            }
            // confirm to do more
            if (!theManager.confirm("Do you want to perform another action?")) {
                theManager.quit();
            }

        } while (true);
    }
}
