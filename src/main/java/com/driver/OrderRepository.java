package com.driver;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;

@Repository
public class OrderRepository {

    HashMap<String, Order> orderMap= new HashMap<>();

    HashMap<String, DeliveryPartner> deliveryPartnerList = new HashMap<>();

    HashMap<DeliveryPartner,Order > orderDeliveryPartnerHashMap = new HashMap<>();
    public void addOrder(Order order)
    {
        String id=order.getId();
        orderMap.put(id,order);
    }

    public Order getOrder(String orderid)
    {
        if(orderMap.containsKey(orderid))
        return orderMap.get(orderid);
        else
            return null;
    }

    public void addPartner(String partnerId)
    {
        DeliveryPartner partner = new DeliveryPartner(partnerId);
        deliveryPartnerList.put(partnerId, partner);
    }

    public DeliveryPartner getPartner(String id)
    {
        if(deliveryPartnerList.containsKey(id))
            return deliveryPartnerList.get(id);
        else
            return null;
    }

    public void addOrderPartnerPair(String orderId, String partnerId){

        Order order;
        DeliveryPartner partner;
        if(orderMap.containsKey(orderId) && deliveryPartnerList.containsKey(partnerId)) {
            order = orderMap.get(orderId);
            partner = deliveryPartnerList.get(partnerId);
            partner.setNumberOfOrders(partner.getNumberOfOrders() + 1);
            deliveryPartnerList.put(partnerId,partner);

            orderDeliveryPartnerHashMap.put(partner , order);
        }
        //This is basically assigning that order to that partnerId
    }
}
