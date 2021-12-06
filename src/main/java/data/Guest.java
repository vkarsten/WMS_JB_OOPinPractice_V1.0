package data;

import java.util.List;

public class Guest extends User {
    public Guest(String userName) {
        super(userName);
    }

    public void greet() {
        System.out.printf("Hello%s!\nWelcome to our Warehouse Database.\nIf you don't find what you are looking for," +
                "\nplease ask one of our staff members to assist you.\n", showUserName());
    }

    public void bye(List<String> actions) {
        System.out.printf("Thank you for your visit%s!\n", showUserName());
    }

    public boolean authenticate(String password) {return false;};
}
