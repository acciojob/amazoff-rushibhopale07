package com.driver;
import java.util.*;

import org.springframework.stereotype.Repository;

import java.util.HashMap;

@Repository
public class OrderRepository {

    HashMap<String, Order> orderDB = new HashMap<>();
    HashMap<String, DeliveryPartner> partnerDB = new HashMap<>();
    HashMap<String, List<String>> pairDB = new HashMap<>();
    HashMap<String, String> assignDB = new HashMap<>();

    public String addOrder(Order order) {
        if(orderDB.containsValue(order))
            return "already value";
        orderDB.put(order.getId(), order);
        return "Added";
    }

    public String addPartner(String partnerId) {
        DeliveryPartner partner = new DeliveryPartner(partnerId);
        partnerDB.put(partnerId, partner);
        return "Added";
    }

    //pairdb is an hashmap of partnerid and list of order
    //assigndb is an hashmap of orderid and partnerid
    public String addOrderPartner(String orderId, String partnerId) {
        List<String> list = pairDB.getOrDefault(partnerId, new ArrayList<>());
        list.add(orderId);
        pairDB.put(partnerId, list);
        assignDB.put(orderId, partnerId);
        DeliveryPartner partner = partnerDB.get(partnerId);
        partner.setNumberOfOrders(list.size());
        return "Added";
    }

    public Order getOrderById(String orderId) {
        if(orderDB.containsKey(orderId))
            return orderDB.get(orderId);
        return null;
    }

    public DeliveryPartner getPartnerById(String partnerId) {
        if (partnerDB.containsKey(partnerId))
            return partnerDB.get(partnerId);
        return null;
    }

    public int getOrderCountByPartnerId(String partnerId) {
        int orderCount = pairDB.getOrDefault(partnerId, new ArrayList<>()).size();
        return orderCount;
    }

    public List<String> getOrdersByPartnerId(String partnerId) {
        List<String> ListOfOrders = pairDB.getOrDefault(partnerId, new ArrayList<>());
        return ListOfOrders;
    }

    public List<String> getAllOrders() {
        List<String> orders = new ArrayList<>();
        for (String s : orderDB.keySet()) {
            orders.add(s);
        }
        return orders;

    }

    public int getCountOfUnassignedOrders() {
        // Count of orders that have not been assigned to any DeliveryPartner
        int countOfOrders = orderDB.size() - assignDB.size();
        return countOfOrders;
    }

    public int getOrdersLeftAfterGivenTimeByPartnerId(String time, String partnerId) {
        // countOfOrders that are left after a particular time of a DeliveryPartner
        int countOfOrders = 0;
        List<String> list = pairDB.get(partnerId);
        int deliveryTime = Integer.parseInt(time.substring(0, 2)) * 60 + Integer.parseInt(time.substring(3));
        for (String s : list) {
            Order order = orderDB.get(s);
            if (order.getDeliveryTime() > deliveryTime) {
                countOfOrders++;
            }
        }
        return countOfOrders;
    }

    public String getLastDeliveryTimeByPartnerId(String partnerId) {
        // Return the time when that partnerId will deliver his last delivery order.
        //String time = "";
        List<String> list = pairDB.get(partnerId);
        int deliveryTime = 0;
        for (String s : list) {
            Order order = orderDB.get(s);
            deliveryTime = Math.max(deliveryTime, order.getDeliveryTime());
        }

        return deliveryTime+"";

    }

    public String deletePartnerById(String partnerId) {
        // Delete the partnerId
        // And push all his assigned orders to unassigned orders.
        partnerDB.remove(partnerId);

        List<String> list = pairDB.getOrDefault(partnerId, new ArrayList<>());
        ListIterator<String> itr = list.listIterator();
        while (itr.hasNext()) {
            String s = itr.next();
            assignDB.remove(s);
        }
        pairDB.remove(partnerId);
        return "Deleted";
    }

    public String deleteOrderById(String orderId) {

        // Delete an order and also
        // remove it from the assigned order of that partnerId
        orderDB.remove(orderId);
        String partnerId = assignDB.get(orderId);
        assignDB.remove(orderId);
        List<String> list = pairDB.get(partnerId);

        ListIterator<String> itr = list.listIterator();
        while (itr.hasNext()) {
            String s = itr.next();
            if (s.equals(orderId)) {
                itr.remove();
            }
        }
        pairDB.put(partnerId, list);

        return "Deleted";
    }
}