package data;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The Data Repository
 *
 * @author pujanov
 *
 */
public class OrderRepository {

    private static List<Order> ORDER_LIST = new ArrayList<Order>();

    /**
     * Load order, records from the order.json file
     */
    static {
        // System.out.println("Loading items");
        BufferedReader reader = null;
        try {
            ORDER_LIST.clear();

            reader = new BufferedReader(new FileReader("src/main/resources/order.json"));
            Object data = JSONValue.parse(reader);
            if (data instanceof JSONArray) {
                JSONArray dataArray = (JSONArray) data;
                for (Object obj : dataArray) {
                    if (obj instanceof JSONObject) {
                        JSONObject jsonData = (JSONObject) obj;
                        String status = jsonData.get("status").toString();
                        boolean isPaymentDone = (boolean) jsonData.get("isPaymentDone");
                        Long warehouseLong = (long) jsonData.get("warehouse");
                        int warehouse = warehouseLong.intValue();

                        String dateOfOrderString = jsonData.get("date_of_order").toString();
                        Date dateOfOrder = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(dateOfOrderString);

                        List<OrderItem> orderItems = (List<OrderItem>) jsonData.get("orderItems");
                        double totalCost = (double) jsonData.get("total_cost");

                        Order order = new Order(status, isPaymentDone, warehouse, dateOfOrder, orderItems, totalCost);
                        ORDER_LIST.add(order);

                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                }
            }
        }
    }

    /**
     * Get All orders
     *
     * @return
     */
    public static List<Order> getAllOrders() {
        return ORDER_LIST;
    }
}
