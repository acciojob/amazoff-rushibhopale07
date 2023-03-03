package com.driver;

import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
class OrderRepository
{
    private Map<String, Order> orderDb = new HashMap<>();
    private Map<String, DeliveryPartner> partnerDb = new HashMap<>();

    private Map<String, List<String>> partnerOrderDb = new HashMap<>();
    private Map<String, String> orderPartnerPair = new HashMap<>();


    public void addOrder(Order order) {orderDb.put(order.getId(), order);}


    public void addPartner(String partner)
    {
        DeliveryPartner pId = new DeliveryPartner(partner);
        partnerDb.put(pId.getId(),pId);
    }

    public void addOrderPartnerPair(String oId, String pId)
    {
        if (orderDb.containsKey(oId) && partnerDb.containsKey(pId))
        {
            List<String> orderList = new ArrayList<>();

            if (partnerOrderDb.containsKey(pId)) orderList = partnerOrderDb.get(pId);

            orderList.add(oId);

            partnerOrderDb.put(pId,orderList);
            orderPartnerPair.put(oId, pId);

            DeliveryPartner partner = partnerDb.get(pId);
            partner.setNumberOfOrders(orderList.size());
            partnerDb.put(pId,partner);
        }
    }

    public Order getOrderById(String oId) { return orderDb.get(oId);}

    public DeliveryPartner getPartnerById(String pId) {return partnerDb.get(pId);}


    public int getOrderCountByPartnerId(String pId)
    {
        List<String> orderList = new ArrayList<>();
        if (partnerOrderDb.containsKey(pId)) orderList = partnerOrderDb.get(pId);
        return orderList.size();
    }

    public List<String> getOrdersByPartnerId(String pId) {return partnerOrderDb.get(pId);}

    public List<String> getAllOrders()
    {
        List<String> ol = new ArrayList<>();
        for (String order: orderDb.keySet()) ol.add(order);
        return ol;
    }

    public int getCountOfUnassignedOrders()
    {
        //Allorder list - everypartner order carring
        int countParticularPartner = orderPartnerPair.size();
        int count_AllOrder = orderDb.size();

        return count_AllOrder - countParticularPartner;
    }

    public int getOrdersLeftAfterGivenTimeByPartnerId(String time, String pId)
    {
        int count = 0;

        String [] t = time.split(":");
        int convertTime = Integer.parseInt(t[0]) * 60 + Integer.parseInt(t[1]);

        if (partnerOrderDb.containsKey(pId))
        {
            for (String oId: partnerOrderDb.get(pId))
            {
                if (orderDb.get(oId).getDeliveryTime() > convertTime) count++;
            }
        }
        return count;
    }


    public String getLastDeliveryTimeByPartnerId(String pId)
    {
        List<Integer> time = new ArrayList<>();
        if (partnerOrderDb.containsKey(pId))
        {
            for (String oId: partnerOrderDb.get(pId))
            {
                Order order = orderDb.get(oId);
                time.add(order.getDeliveryTime());
            }
        }
        int maxTime = Collections.max(time);

        //convert int time to String time like (HH:MM)
        String startTime = "00:00";
        int HH = maxTime / 60;
        int MM = maxTime % 60;

        return String.format("%02d:%02d",HH,MM);
    }

    public void deletePartnerById(String pId)
    {
        HashSet<String> hs = new HashSet<>();
        if (partnerOrderDb.containsKey(pId) && partnerDb.containsKey(pId))
        {
            for (String oId: partnerOrderDb.get(pId)) //hs.add(oId);
            {
                if(orderPartnerPair.containsKey(oId)) orderPartnerPair.remove(oId);
            }
            partnerOrderDb.remove(pId);
            partnerDb.remove(pId);
        }
    }


    public void deleteOrderById(String oId)
    {
        String pId = null;
        List<String> list = new ArrayList<>();
        int size = 0;
        if (orderDb.containsKey(oId))
        {
            if (orderPartnerPair.containsKey(oId)) pId = orderPartnerPair.get(oId);
            orderPartnerPair.remove(oId);
            if (partnerOrderDb.containsKey(pId)) list = partnerOrderDb.get(pId);
            list.remove(oId);
            partnerOrderDb.put(pId,list);

            DeliveryPartner partner = partnerDb.get(pId);
            partner.setNumberOfOrders(list.size());
            partnerDb.put(pId,partner);
        }
        orderDb.remove(oId);
    }
}