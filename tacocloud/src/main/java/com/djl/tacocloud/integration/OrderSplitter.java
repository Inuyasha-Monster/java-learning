package com.djl.tacocloud.integration;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author djl
 * @create 2020/12/21 14:29
 */
public class OrderSplitter {
    public Collection<Object> splitOrderIntoParts(PurchaseOrder po) {
        ArrayList<Object> parts = new ArrayList<>();
        parts.add(po.getBillingInfo());
        parts.add(po.getLineItems());
        return parts;
    }
}
