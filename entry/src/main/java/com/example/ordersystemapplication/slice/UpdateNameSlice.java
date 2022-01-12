package com.example.ordersystemapplication.slice;

import com.example.ordersystemapplication.ResourceTable;
import com.example.ordersystemapplication.data.Result;
import com.example.ordersystemapplication.domain.Customer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zzrv5.mylibrary.ZZRCallBack;
import com.zzrv5.mylibrary.ZZRHttp;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.*;
import ohos.agp.window.dialog.ToastDialog;

public class UpdateNameSlice extends AbilitySlice implements Component.ClickedListener {
    private Button button;
    private Image back;
    private Customer customer;
    private Text userPhone;
    private Text oldName;
    private TextField newName;
    private Result result = new Result();
    private Gson gson = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd").create();
    private String url = "http://101.132.74.147:8082/customer/changeName";
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_update_name);
        customer = intent.getSerializableParam("Customer");
        button = (Button) findComponentById(ResourceTable.Id_sure_update_name);
        back = (Image)findComponentById(ResourceTable.Id_back);
        back.setClickedListener(this);
        button.setClickedListener(this);
        userPhone = (Text) findComponentById(ResourceTable.Id_update_name_phone);
        oldName = (Text) findComponentById(ResourceTable.Id_old_name);
        userPhone.setText(customer.getCustPhone());
        oldName.setText(customer.getCustName());
        newName = (TextField) findComponentById(ResourceTable.Id_update_name);
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
        if (component.getId() == ResourceTable.Id_back){
            terminate();
        }
        if (component.getId() == ResourceTable.Id_sure_update_name){
            if(newName.getText()!=null){
                customer.setCustName(newName.getText());
                String json = gson.toJson(customer);
                ZZRHttp.postJson(url, json, new ZZRCallBack.CallBackString() {
                    @Override
                    public void onFailure(int i, String s) {
                        new ToastDialog(UpdateNameSlice.this)
                                .setText("网络不稳定")
                                .show();
                    }
                    @Override
                    public void onResponse(String s) {
                        result = gson.fromJson(s,Result.class);
                        if (result.getCode() == 200){
                            new ToastDialog(UpdateNameSlice.this)
                                    .setText(result.getMsg())
                                    .show();
                            terminate();
                        } else if (result.getCode() == 400){
                            new ToastDialog(UpdateNameSlice.this)
                                    .setText(result.getMsg())
                                    .show();
                        }
                    }
                });
            }else {
                new ToastDialog(UpdateNameSlice.this)
                        .setText("用户名不能为空")
                        .show();
            }

        }
    }
}
