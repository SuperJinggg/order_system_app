package com.example.ordersystemapplication.slice;

import com.example.ordersystemapplication.ResourceTable;
import com.example.ordersystemapplication.UserSystem;
import com.example.ordersystemapplication.data.Result;
import com.example.ordersystemapplication.domain.Customer;
import com.example.ordersystemapplication.domain.Reserve;
import com.example.ordersystemapplication.domain.Table;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zzrv5.mylibrary.ZZRCallBack;
import com.zzrv5.mylibrary.ZZRHttp;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.*;
import ohos.agp.window.dialog.ToastDialog;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ReserveInfoSlice extends AbilitySlice implements Component.ClickedListener {
    private Button submit_reserve;
    private Text reserve_table_id;
    private Text reserve_table_people;
    private Text reserve_table_state;
    private Text reserve_table_price;
    private Result result = new Result();
    private Gson gson = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    private String url = "http://101.132.74.147:8082";
    private Image back;
    private Table table;
    private TextField startTH;
    private TextField startTM;
    private TextField startTS;
    private TextField endTH;
    private TextField endTM;
    private TextField endTS;
    private Customer customer;
    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_reserve_info);
        table = intent.getSerializableParam("table");
        customer = intent.getSerializableParam("Customer");
        back = (Image)findComponentById(ResourceTable.Id_back);
        back.setClickedListener(this);
        reserve_table_id = (Text) findComponentById(ResourceTable.Id_reserve_table_id);
        reserve_table_people = (Text) findComponentById(ResourceTable.Id_reserve_table_num);
        reserve_table_state = (Text) findComponentById(ResourceTable.Id_reserve_table_state);
        reserve_table_price = (Text) findComponentById(ResourceTable.Id_reserve_table_price);
        startTH = (TextField)findComponentById(ResourceTable.Id_reserve_start_time_hour);
        startTM = (TextField)findComponentById(ResourceTable.Id_reserve_start_time_minutes);
        startTS = (TextField)findComponentById(ResourceTable.Id_reserve_start_time_second);
        endTH = (TextField)findComponentById(ResourceTable.Id_reserve_end_time_hour);
        endTM = (TextField)findComponentById(ResourceTable.Id_reserve_end_time_minutes);
        endTS = (TextField)findComponentById(ResourceTable.Id_reserve_end_time_second);
        reserve_table_id.setText(table.getTableId());
        reserve_table_people.setText(table.getFullPeople()+"");
        reserve_table_state.setText("空闲");
        reserve_table_price.setText(table.getTablePrice()+"");
        submit_reserve = (Button) findComponentById(ResourceTable.Id_submit_reserve_table);
        submit_reserve.setClickedListener(this);
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
        if (component.getId() == ResourceTable.Id_submit_reserve_table){
            String YHD = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

            String startT = YHD + " " + startTH.getText() + ":" + startTM.getText() + ":" + startTS.getText();
            String endT = YHD + " " + endTH.getText() + ":" + endTM.getText() + ":" + endTS.getText();
            if (startTH.getText().equals("")||startTM.getText().equals("")||startTS.getText().equals("")||endTH.getText().equals("")||endTM.getText().equals("")||endTS.getText().equals("")){
                new ToastDialog(this)
                        .setText("预约信息不能为空！")
                        .show();
                return;
            }

            Reserve reserve = new Reserve();
            reserve.setTableId(table.getTableId());
            reserve.setStartTime(startT);
            reserve.setEndTime(endT);
            reserve.setCustPhone(customer.getCustPhone());
            System.out.println(reserve);
            String json = gson.toJson(reserve);
            System.out.println(json);
            ZZRHttp.postJson(url + "/reserve/addReserve",json, new ZZRCallBack.CallBackString() {
                @Override
                public void onFailure(int i, String s) {
                    new ToastDialog(ReserveInfoSlice.this)
                            .setText("网络不稳定")
                            .show();
                }
                @Override
                public void onResponse(String s) {
                    result = gson.fromJson(s,Result.class);
                    if (result.getCode() == 200){
                        Intent intent = new Intent();
                        intent.setParam("my",customer);
                        Operation operation = new Intent.OperationBuilder()
                                .withDeviceId("")
                                .withBundleName("com.example.ordersystemapplication")
                                .withAbilityName(UserSystem.class)
                                .build();
                        intent.setOperation(operation);
                        startAbility(intent);
                    } else if (result.getCode() == 400){
                        new ToastDialog(ReserveInfoSlice.this)
                                .setText(result.getMsg())
                                .show();
                    }
                }
            });
        }
    }
}
