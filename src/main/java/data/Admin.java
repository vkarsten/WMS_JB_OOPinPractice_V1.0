package data;

import java.util.List;

public class Admin extends Employee {
    public Admin(String userName, String password) {
        super(userName, password);
    }
    public Admin(String userName, String password, List<Employee> headOf) {
        super(userName, password, headOf);
    }
    @Override
    public void greet() {
        System.out.printf("Hello, %s!\n" +
                "Welcome to the Admin Panel.\nWith higher authority comes higher responsibility.\n", name);
    }
}
