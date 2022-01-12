package com.example.ordersystemapplication.slice;


import com.example.ordersystemapplication.MainAbility;
import com.example.ordersystemapplication.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.Button;
import ohos.agp.components.Component;

public class SureRegisterSlice extends AbilitySlice implements Component.ClickedListener {
    private Button button;
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_sure_register);
        button = (Button) findComponentById(ResourceTable.Id_sure_register);
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
        if (component.getId() == ResourceTable.Id_sure_register){
            Intent intent = new Intent();
            Operation operation = new Intent.OperationBuilder()
                    .withDeviceId("")
                    .withBundleName("com.example.ordersystemapplication")
                    .withAbilityName(MainAbility.class)
                    .build();
            intent.setOperation(operation);
            startAbility(intent);
        }
    }
}

