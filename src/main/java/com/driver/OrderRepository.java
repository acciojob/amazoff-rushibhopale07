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
        orderDB.put(order.getId(), order);
        return "Added";
    }

    public String addPartner(String partnerId) {
        DeliveryPartner partner = new DeliveryPartner(partnerId);
        partnerDB.put(partnerId, partner);
        return "Added";
    }

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
        for (String s : orderDB.keySet()) {
            if (s.equals(orderId))
                return orderDB.get(s);
        }
        return null;
    }

    public DeliveryPartner getPartnerById(String partnerId) {
        if (partnerDB.containsKey(partnerId))
            return partnerDB.get(partnerId);
        return null;
    }

    public int getOrderCountByPartnerId(String partnerId) {
        int order = pairDB.getOrDefault(partnerId, new ArrayList<>()).size();
        return order;
    }

    public List<String> getOrdersByPartnerId(String partnerId) {
        List<String> orders = pairDB.getOrDefault(partnerId, new ArrayList<>());
        return orders;
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
        String time = "";
        List<String> list = pairDB.get(partnerId);
        int deliveryTime = 0;
        for (String s : list) {
            Order order = orderDB.get(s);
            deliveryTime = Math.max(deliveryTime, order.getDeliveryTime());
        }
        int hour = deliveryTime / 60;
        String sHour = "";
        if (hour < 10) {
            sHour = "0" + String.valueOf(hour);
        } else {
            sHour = String.valueOf(hour);
        }

        int min = deliveryTime % 60;
        String sMin = "";
        if (min < 10) {
            sMin = "0" + String.valueOf(min);
        } else {
            sMin = String.valueOf(min);
        }

        time = sHour + ":" + sMin;

        return time;

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