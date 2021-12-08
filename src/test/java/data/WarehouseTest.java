package data;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WarehouseTest {

    @Test
    void occupancy() {
        Warehouse wh = new Warehouse(1);
        assertEquals(0, wh.occupancy());
    }

    @Test
    void addItem() {
        Warehouse wh = new Warehouse(1);
        Item test = new Item();
        wh.addItem(test);
        wh.addItem(test);
        wh.addItem(test);

        assertEquals(3, wh.occupancy());
    }

    @Test
    void search() {
        Warehouse wh = new Warehouse(1);
        Item test = new Item();
        test.setState("new");
        test.setCategory("phone");
        wh.addItem(test);

        assertEquals(1, wh.search("new phone").size());
        assertEquals(0, wh.search("old phone").size());
    }
}