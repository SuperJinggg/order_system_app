package com.example.ordersystemapplication.slice;

import com.example.ordersystemapplication.ResourceTable;
import com.example.ordersystemapplication.domain.Reserve;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Component;
import ohos.agp.components.Image;
import ohos.agp.components.Text;


public class CheckReserveInfoSlice extends AbilitySlice implements Component.ClickedListener {
    private Text reserve_table_id;
    private Text reserve_customer;
    private Text reserve_table_startT;
    private Text reserve_table_endT;
    private Image back;
    private Reserve reserve;
    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_check_reserve_table);
        reserve = intent.getSerializableParam("Reserve");
        reserve_table_id = (Text) findComponentById(ResourceTable.Id_check_reserve_table_id);
        reserve_customer = (Text) findComponentById(ResourceTable.Id_check_reserve_customer);
        reserve_table_startT = (Text) findComponentById(ResourceTable.Id_check_reserve_table_startT);
        reserve_table_endT = (Text) findComponentById(ResourceTable.Id_check_reserve_table_endT);
        back = (Image)findComponentById(ResourceTable.Id_back);
        back.setClickedListener(this);
        reserve_table_endT.setText(reserve.getEndTime());
        reserve_table_startT.setText(reserve.getStartTime());
        reserve_table_id.setText(reserve.getTableId());
        reserve_customer.setText(reserve.getCustPhone());
    }

    @Override
    protected void onActive() {
        super.onActive();
    }

    @Override
    protected void onInactive() {
        super.onInactive();
    }

    @Override
    protected void onForeground(Intent intent) {
        super.onForeground(intent);
    }

    @Override
    protected void onBackground() {
        super.onBackground();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onClick(Component component) {
        if (component.getId() == ResourceTable.Id_back){
            terminate();
        }
    }
}
