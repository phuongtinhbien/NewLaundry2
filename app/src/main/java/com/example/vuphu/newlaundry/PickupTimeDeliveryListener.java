package com.example.vuphu.newlaundry;

import com.example.vuphu.newlaundry.Order.OBTimeSchedule;

public interface PickupTimeDeliveryListener {
    void onPickupTimeDeliveryClicked(OBTimeSchedule obTimeSchedule, String type, int pos);
    void onPickupTimeDeliveryUnClicked(String type);
}
