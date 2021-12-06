package data;

import java.util.List;

public interface AdminService {

    public void authenticateAdmin();

    public int getAdminsChoice();

    public void performAction(int option);

    public void listOrdersByWarehouse();

    public void listOrdersByStatus();

    public void listOrdersWhoseTotalCostGreaterThan();

    public void printsListOfOrdersToConsole(List<Order> orders);

    public void quit();

}
