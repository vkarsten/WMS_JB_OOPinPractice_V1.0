package app;

import data.*;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Provides necessary methods to deal through the Warehouse management actions
 *
 * @author riteshp
 */
public class TheWarehouseManager {
    // =====================================================================================
    // Member Variables
    // =====================================================================================

    // To read inputs from the console/CLI
    private final Scanner reader = new Scanner(System.in);
    private final String[] userOptions = {
            "1. List items by warehouse", "2. Search an item and place an order", "3. Browse by category", "4. Quit"
    };

    // To refer warehouses and categories
    private final List<Warehouse> warehouses = WarehouseRepository.getWarehouseList();
    private final Set<String> categories = WarehouseRepository.getCategories();

    // To refer to the items matching the current search
    private Map<Integer, List<Item>> matchingItemsPerWarehouse = new HashMap<>(warehouses.size());

    // =====================================================================================
    // Public Member Methods
    // =====================================================================================

    /** Welcome User */
    public void welcomeUser() {
        this.greetUser();
    }

    /** Ask for user's choice of action */
    public int getUsersChoice() {
        System.out.println("What would you like to do?");
        for (String option : this.userOptions) {
            System.out.println(option);
        }
        System.out.println("Type the number of the operation:");
        int choice;
        try {
            choice = this.reader.nextInt();
        } catch (InputMismatchException e) {
            choice = -1;
        }
        this.reader.nextLine();
        return choice;
    }

    /** Initiate an action based on given option */
    public void performAction(int option) {
        switch (option) {
            case 1:
                this.listItemsByWarehouse();
                break;
            case 2:
                this.searchItemAndPlaceOrder();
                break;
            case 3:
                this.browseByCategory();
                break;
            case 4:
                this.quit();
            default:
                System.out.println("The option you entered is not valid! Please try again.");
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

    /** End the application */
   public void quit() {
       // Pass session actions to user's bye() method.
       // Depending on the type of the user, the session actions will either be printed or not
        TheWarehouseApp.SESSION_USER.bye(TheWarehouseApp.SESSION_ACTIONS);
        System.exit(0);
   }

    // =====================================================================================
    // Private Methods
    // =====================================================================================

    // Methods for general execution flow
    // =====================================================================================

    /** Get user's name via CLI */
   private String seekUserName() {
        System.out.println("Please enter your user name:");
        String userName = this.reader.nextLine();
        // Session user will be a guest by default
        TheWarehouseApp.SESSION_USER = new Guest(userName);
        return userName;
   }

    /** Get user's password via CLI */
    private String askPassword() {
        System.out.println("Please enter your password:");
        return this.reader.nextLine();
    }

    /** log in the current user */
    private void logIn(String userName) {
        // prompt user for password while they are not a valid employee
        while (!TheWarehouseApp.IS_EMPLOYEE) {
            String password = this.askPassword();

            // if the given password matches with a valid employee user, the session user will be changed to an employee
            // Also tell the warehouse that the current user is an employee, so they do not have to log in again later on
            if (UserRepository.isEmployeeValid(userName, password)) {
                System.out.println("You logged in successfully");
                TheWarehouseApp.SESSION_USER = new Employee(userName, password);
                TheWarehouseApp.IS_EMPLOYEE = true;
            }
            // if the password does not match, the user is given the choice to try again with a new username (and password)
            // if they do not want to try again, they will continue as a Guest
            else if (this.confirm("This was not successful. Do you want to try again?")) {
                    userName = this.seekUserName();
                } else return;
        }
    }

    /** Print a welcome message with the given user's name */
    private void greetUser() {
        // ask for username
        String userName = this.seekUserName();
        // evaluate if user is an Admin. If yes, change session user from Guest to Admin
        if (UserRepository.isUserAdmin(userName)) TheWarehouseApp.SESSION_USER = new Admin(userName, "");
        // evaluate if user is an Employee. If yes, prompt user to login.
            // If the login is successful, the session user will be an employee
        else if (UserRepository.isUserEmployee(userName)) logIn(userName);
        // Finally, greet user (based on the type of the session user, a different greeting will be shown)
        TheWarehouseApp.SESSION_USER.greet();
    }

    /** log an action into the list of session actions */
    private void logSessionAction(String action) {
        TheWarehouseApp.SESSION_ACTIONS.add(action);
    }


    // Methods for menu option: list items by warehouse
    // =====================================================================================
    private void listItemsByWarehouse() {
        // Create a map to store the warehouse id together with the total items in each warehouse
        Map<Integer, Integer> totalItems = new HashMap<>(warehouses.size());

        for (Warehouse warehouse : warehouses) {
            // Listing all items with corresponding warehouse ids
            System.out.println("\nItems in Warehouse " + warehouse.getId());
            listItems(warehouse.getStock());
            // put the warehouse id and the total number of items inside in the map
            totalItems.put(warehouse.getId(), warehouse.occupancy());
        }

        // print the map at the end of all listed items
        listTotalItemsPerWarehouse(totalItems);

        logSessionAction("Listed " + getTotalListedItems() + " items.");
    }

    /**
     * prints the list of all items in a given warehouse
     * @param warehouseItems, List of Items
     */
    private void listItems(List<Item> warehouseItems) {
            for (Item item : warehouseItems) {
                System.out.printf("- %s\n", item.toString());
            }
    }

    /**
     * prints the total amounts of items per warehouse
     * @param totalItemsPerWarehouse, Map of Integer (Warehouse number) and Integer (Items in the Warehouse)
     */
    private void listTotalItemsPerWarehouse(Map<Integer, Integer> totalItemsPerWarehouse) {
        for (Map.Entry<Integer, Integer> entry : totalItemsPerWarehouse.entrySet()) {
            System.out.printf("Total items in warehouse %d: %s\n", entry.getKey(), entry.getValue());
        }
    }

    /** return the total amount of items in all warehouses */
    private int getTotalListedItems() {
        return WarehouseRepository.getAllItems().size();
    }

    // Methods for menu option: Search item and place order
    // =====================================================================================
    private void searchItemAndPlaceOrder() {
        // Get the name of the warehouse item that the user wants to find
        String itemName = askItemToOrder();

        // create lists of items matching the search term, grouped by warehouse
        // (stored for reuse in class field matchingItemLists)
        this.getMatchingItemLists(itemName);
        // calculate and print the total availability of the given item
        int totalAmount = this.getAvailableAmount();
        System.out.println("Amount available: " + totalAmount);

        // Performing actions based on how many items are in stock
        if (totalAmount == 0) {
            this.printLocation("Not in stock");
        } else {
            // list the occurrences of the item in the different warehouses
            this.listAllLocations();
            // if more than one warehouse has the item in stock, then show which warehouse stores the most
            if (this.matchingItemsPerWarehouse.size() > 1) {
                this.printMaximumAvailability();
            }
            // if the item is in stock, it can also be ordered.
            // Only employees are allowed to do this, so call the login() method for the session user.
            // If the user is already authenticated, the method will not do anything.
            // Afterwards, only if the user is authenticated as an employee, call the method for placing an order
            if (this.confirm("Would you like to order this item?")) {
                this.logIn(TheWarehouseApp.SESSION_USER.getName());
                if (TheWarehouseApp.IS_EMPLOYEE) this.askAmountAndConfirmOrder(totalAmount, itemName.toLowerCase());
            }
        }

        logSessionAction("Searched " + getAppropriateIndefiniteArticle(itemName) + " " + itemName + ".");
    }

    /**
     * Ask the user to specify an Item to Order
     *
     * @return String itemName
     */
    private String askItemToOrder() {
        System.out.println("What is the name of the item?");
        return this.reader.nextLine();
    }

    /**
     * Calculate availability of the given item
     * @return integer total amount
     */
    private int getAvailableAmount() {
        int totalAmount = 0;

         for (List<Item> warehouseItems : matchingItemsPerWarehouse.values()) {
             totalAmount += warehouseItems.size();
         }

        return totalAmount;
    }

    /**
     * Create a map of all the occurrences of an item separated by warehouse
     * @param itemName, String, the name of the item
     * @return allAmounts, the matching items per warehouse
     */
    private void getMatchingItemLists(String itemName) {
        for (Warehouse warehouse : warehouses) {
            List<Item> matchingItems = warehouse.search(itemName);
            if (matchingItems.size() > 0) {
                this.matchingItemsPerWarehouse.put(warehouse.getId(), matchingItems);
            } else this.matchingItemsPerWarehouse.clear();
        }
    }

    /** Print the location of an item without listing the available items
     *
     * @param location the location of the items
     */
    private void printLocation(String location) {
        System.out.println("Location: " + location);
    }

    /** Print the location of an item and lists the corresponding items and their warehouse */
    private void listAllLocations() {
        System.out.println("Location: ");

        for (Map.Entry<Integer, List<Item>> warehouseItems : matchingItemsPerWarehouse.entrySet()) {
            for (Item item : warehouseItems.getValue()) {
                System.out.printf("- Warehouse %d (in stock for %d days)\n", warehouseItems.getKey(), this.calculateNumberOfDaysInStock(item));
            }
        }
    }

    /**
     * Calculate the number of days a given item has been in stock
     * @param item Item
     * @return the number of days in stock, long
     */
    private long calculateNumberOfDaysInStock(Item item) {
        Date today = new Date();
        return TimeUnit.DAYS.convert(today.getTime() - item.getDateOfStock().getTime(), TimeUnit.MILLISECONDS);
    }

    /**
     * Print the location with the maximum availability of an item
     */
    private void printMaximumAvailability() {
        int maxSize = 0;
        int warehouse = 0;

        for (Map.Entry<Integer, List<Item>> warehouseItems : matchingItemsPerWarehouse.entrySet()) {
            if (warehouseItems.getValue().size() > maxSize) {
                maxSize = warehouseItems.getValue().size();
                warehouse = warehouseItems.getKey();
            }
        }

        System.out.printf("Maximum availability: %d in Warehouse %d\n", maxSize, warehouse);
    }


    /** Ask order amount and confirm order */
    private void askAmountAndConfirmOrder(int availableAmount, String item) {
        System.out.println("How many would you like to order?");
        int order = getOrderAmount(availableAmount);
        if (order > 0) {
            System.out.printf("Your order of %d %s is confirmed.\n", order, (order == 1) ? item : item+"s");
        } else {
            System.out.println("No order has been placed.");
        }
    }

    /**
     * Get amount of order
     *
     * @param availableAmount the total available amount of the item in question
     * @return desiredAmount the amount to be ordered
     */
    private int getOrderAmount(int availableAmount) {
        int desiredAmount;

        if (this.reader.hasNextInt()) {
            desiredAmount = this.reader.nextInt();
            this.reader.nextLine();
        } else {
            this.reader.nextLine();
            return -1;
        }

        if (desiredAmount > availableAmount) {
            System.out.println("There are not this many available. The maximum amount that can be ordered is " + availableAmount);
            return (this.confirm("Would you like to order this amount?")) ? availableAmount : -1;
        }

        return desiredAmount;
    }

    /**
     * Return the appropriate indefinite article for a given String, depending on whether it starts with a vowel
     * @param word String
     * @return the appropriate article, String
     */
    private String getAppropriateIndefiniteArticle(String word) {
        String vowels = "aeiou";
        return (vowels.indexOf(Character.toLowerCase(word.charAt(0))) != -1) ? "an" : "a";
    }

    // Methods for menu option: browse by category
    // =====================================================================================
    private void browseByCategory() {
        // Map menu numbers to category names for the category menu to be printed
        Map<Integer, String> categoryList = this.getCategoryMenu();
        this.showCategoryMenu(categoryList);

        // Get the category choice from the user, and if it is valid, list the items in this category
        int categoryNumber = this.getCategoryChoice();
        if (categoryNumber > 0 && categoryNumber <= categoryList.size()) {
            String category = categoryList.get(categoryNumber);
            this.printCategoryItems(category);
            logSessionAction("Browsed the category " + category  + ".");
        } else {
            System.out.println("This is not a valid category.");
        }
    }

    /** Create a menu of categories with corresponding numbers
     *
     * @return categoryList, a map of categories and their numbers
     */
    private Map<Integer, String> getCategoryMenu() {
        Map<Integer, String> categoryList = new HashMap<>();
        int count = 1;

        for (String category : this.categories) {
            categoryList.put(count, category);
            count++;
        }

        return categoryList;
    }

    /**
     * Print the menu of categories and the number of items in each category
     * @param categoryList a map of the categories and their numbers
     */
    private void showCategoryMenu(Map<Integer, String> categoryList) {
        for (Map.Entry<Integer, String> entry : categoryList.entrySet()) {
            int categoryNumber = entry.getKey();
            String category = entry.getValue();

            System.out.printf("%d. %s (%d)\n", categoryNumber, category, getAmountPerCategory(category));
        }
    }

    /**
     * Return the amount of items in a category
     * @param category, String, name of the category
     * @return int, amount of items
     */
    private int getAmountPerCategory(String category) {
        return WarehouseRepository.getItemsByCategory(category).size();
    }

    /**
     * Ask for the user's choice of category
     * @return int, the chosen category
     */
    private int getCategoryChoice() {
        System.out.println("Type the number of the category to browse:");
        int choice;
        try {
            choice = this.reader.nextInt();
        } catch (InputMismatchException e) {
            choice = -1;
        }
        this.reader.nextLine();
        return choice;
    }

    /**
     * Print the available items in a chosen category and their location
     * @param category, String, the name of the category
     */
    private void printCategoryItems(String category) {
        System.out.printf("List of %ss available:\n", category.toLowerCase());

        for (Item item : WarehouseRepository.getItemsByCategory(category)) {
            System.out.printf("%s %s, Warehouse %d\n", item.getState(), item.getCategory(), item.getWarehouse());
        }
    }
}
