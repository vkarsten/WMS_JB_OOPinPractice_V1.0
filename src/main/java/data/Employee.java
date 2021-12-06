package data;

import java.util.ArrayList;
import java.util.List;

public class Employee extends User {
    private String password;
    private List<Employee> headOf = new ArrayList<Employee>();

    public Employee(String userName, String password) {
        super(userName);
        this.password = password;
    }
    public Employee(String userName, String password, List<Employee> headOf) {
        this(userName, password);
        this.headOf = headOf;
    }

    public String getPassword() {
        return password;
    }

    public boolean authenticate(String password) {
        this.isAuthenticated = password.equals(this.password);
        return this.isAuthenticated;
    }

    public void order(String item, int amount) {
        System.out.printf("%d %s%s successfully ordered.", amount, item, (amount == 1) ? "" : "s");
    }

    @Override
    public void greet() {
        System.out.printf("Hello, %s!\n" +
                "If you experience a problem with the system,\nplease contact technical support.\n", name);
    }

    @Override
    public void bye(List<String> actions) {
        System.out.printf("\nThank you for your visit, %s!\n", name);
        listSessionActions(actions);
    }

    private void listSessionActions(List<String> actions) {
        if (actions.size() > 0) {
            System.out.println("In this session you have: ");
            int actionNumber = 1;
            for (String action : actions) {
                System.out.printf("%d. %s \n", actionNumber, action);
                actionNumber++;
            }
        } else System.out.println("In this session you have not done anything.");
    }
}
