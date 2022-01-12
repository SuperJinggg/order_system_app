package com.example.ordersystemapplication.slice;

import com.example.ordersystemapplication.ResourceTable;
import com.example.ordersystemapplication.data.Result;
import com.example.ordersystemapplication.domain.Customer;
import com.example.ordersystemapplication.domain.Reserve;
import com.example.ordersystemapplication.provider.ReserveProvider;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.zzrv5.mylibrary.ZZRCallBack;
import com.zzrv5.mylibrary.ZZRHttp;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Component;
import ohos.agp.components.Image;
import ohos.agp.components.ListContainer;
import ohos.agp.window.dialog.ToastDialog;

import java.util.List;

public class CheckReserveSlice extends AbilitySlice implements Component.ClickedListener {
    private Image back;
    private ListContainer reserve_listcon;
    private Customer customer;
    private Result result = new Result();
    private Gson gson = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_check_reserve);
        back = (Image)findComponentById(ResourceTable.Id_back);
        back.setClickedListener(this);
        reserve_listcon = (ListContainer) findComponentById(ResourceTable.Id_reserve_liscontainer);
        customer = intent.getSerializableParam("Customer");
        String json = gson.toJson(customer);
        ZZRHttp.postJson("http://101.132.74.147:8082/customer/myReserveList", json, new ZZRCallBack.CallBackString() {
            @Override
            public void onFailure(int i, String s) {
                new ToastDialog(CheckReserveSlice.this)
                        .setText("网络不稳定")
                        .show();
            }

            @Override
            public void onResponse(String s) {
                result = gson.fromJson(s, Result.class);
                String json = gson.toJson(result.getResult());
                List<Reserve> list = gson.fromJson(json, new TypeToken<List<Reserve>>() {}.getType());
                ReserveProvider reserveProvider = new ReserveProvider(list,CheckReserveSlice.this);
                reserve_listcon.setItemProvider(reserveProvider);
                reserve_listcon.setItemClickedListener(new ListContainer.ItemClickedListener() {
                    @Override
                    public void onItemClicked(ListContainer listContainer, Component component, int i, long l) {
                        Reserve reserve = (Reserve) reserve_listcon.getItemProvider().getItem(i);
                        Intent intent1 = new Intent();
                        intent1.setParam("Reserve",reserve);
                        present(new CheckReserveInfoSlice(),intent1);
                    }
                });

            }
        });
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
