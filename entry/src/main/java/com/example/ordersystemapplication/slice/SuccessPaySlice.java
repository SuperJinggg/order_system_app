package com.example.ordersystemapplication.slice;

import com.example.ordersystemapplication.ResourceTable;
import com.example.ordersystemapplication.UserSystem;
import com.example.ordersystemapplication.domain.Customer;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.Button;
import ohos.agp.components.Component;

public class SuccessPaySlice extends AbilitySlice implements Component.ClickedListener {
    private Button button;
    private Customer customer;
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_success_pay);
        customer = intent.getSerializableParam("Customer");
        button = (Button) findComponentById(ResourceTable.Id_sure_pay);
        button.setClickedListener(this);

    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }

    @Override
    public void onClick(Component component) {
        if (component.getId() == ResourceTable.Id_sure_pay){
            Intent intent = new Intent();
            intent.setParam("my",customer);
            Operation operation = new Intent.OperationBuilder()
                    .withDeviceId("")
                    .withBundleName("com.example.ordersystemapplication")
                    .withAbilityName(UserSystem.class)
                    .build();
            intent.setOperation(operation);
            startAbility(intent);
        }
    }
}
