package com.driver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    @Autowired
    OrderRepository orderRepository;

    public void addOrder(Order order) {
        orderRepository.addOrder(order);
    }

    public Order getOrder(String orderid) {
        return orderRepository.getOrder(orderid);
    }

    public void addPartner(String partner) {
        orderRepository.addPartner(partner);
    }

    public DeliveryPartner getPartner(String id) {
        return orderRepository.getPartner(id);
    }

    public void addOrderPartnerPair(String orderId, String partnerId) {
        orderRepository.addOrderPartnerPair(orderId, partnerId);
    }

}
