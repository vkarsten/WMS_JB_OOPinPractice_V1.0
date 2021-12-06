package data;

import java.util.ArrayList;
import java.util.List;

public class Warehouse {
    private int id;

    private List<Item> stock;

    public Warehouse(int warehouseId) {
        this.id = warehouseId;
        this.stock = new ArrayList<Item>();
    }

    public int getId() {
        return id;
    }

    public List<Item> getStock() {
        return stock;
    }

    public int occupancy() {
        return stock.size();
    }

    public void addItem(Item item) {
        this.stock.add(item);
    }

    public List<Item> search(String searchTerm) {
        List<Item> matchingItems = new ArrayList<Item>();
        for (Item item : this.stock) {
            if (item.toString().equalsIgnoreCase(searchTerm)) matchingItems.add(item);
        }

        return matchingItems;
    }

    @Override
    public String toString() {
        return "Warehouse{" +
                "id=" + id +
                ", stock=" + stock +
                '}';
    }
}
