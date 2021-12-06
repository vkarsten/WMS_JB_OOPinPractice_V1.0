package data;

import java.util.List;

public abstract class User {
    protected String name = "Anonymous";
    protected boolean isAuthenticated = false;

    public User(String userName) {
        this.name = userName;
    }

    public String getName() {
        return name;
    }

    public boolean isAuthenticated() {return isAuthenticated;}

    public abstract boolean authenticate(String password);

    public boolean isNamed(String name) {
        return name.equals(this.name);
    }

    public abstract void greet();

    public abstract void bye(List<String> actions);

    protected String showUserName() {
        return (name.equals("Anonymous")) ? "" : (", ".concat(name));
    }
}
