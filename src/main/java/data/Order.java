package data;

import java.util.Date;
import java.util.List;

public class Order {
    private String status;
    private boolean isPaymentDone;
    private int warehouse;
    private Date dateOfOrder;
    private List<OrderItem> orderItems;
    private double totalCost;

    public Order(String status, boolean isPaymentDone, int warehouse,
                 Date dateOfOrder, List<OrderItem> orderItems, double totalCost) {
        this.status = status;
        this.isPaymentDone = isPaymentDone;
        this.warehouse = warehouse;
        this.dateOfOrder = dateOfOrder;
        this.orderItems = orderItems;
        this.totalCost = totalCost;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isPaymentDone() {
        return isPaymentDone;
    }

    public void setPaymentDone(boolean paymentDone) {
        isPaymentDone = paymentDone;
    }

    public int getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(int warehouse) {
        this.warehouse = warehouse;
    }

    public Date getDateOfOrder() {
        return dateOfOrder;
    }

    public void setDateOfOrder(Date dateOfOrder) {
        this.dateOfOrder = dateOfOrder;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }
}
