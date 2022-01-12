package com.example.ordersystemapplication;

import com.example.ordersystemapplication.slice.UserSystemSlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

public class UserSystem extends Ability {

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(UserSystemSlice.class.getName());


    }
}
