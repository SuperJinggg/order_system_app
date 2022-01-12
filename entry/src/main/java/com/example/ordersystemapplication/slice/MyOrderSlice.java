package com.example.ordersystemapplication.slice;

import com.example.ordersystemapplication.ResourceTable;
import com.example.ordersystemapplication.data.Result;
import com.example.ordersystemapplication.domain.Customer;
import com.example.ordersystemapplication.domain.Order;
import com.example.ordersystemapplication.provider.MyOrderProvider;
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

public class MyOrderSlice extends AbilitySlice implements Component.ClickedListener {
    private Image back;
    private ListContainer my_order_listcontainer;
    private Result result = new Result();
    private Gson gson = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    private Customer customer;
    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_my_order);
        customer = intent.getSerializableParam("Customer");
        back = (Image) findComponentById(ResourceTable.Id_back);
        my_order_listcontainer = (ListContainer) findComponentById(ResourceTable.Id_my_order_listcontainer);
        back.setClickedListener(this);

        String json = gson.toJson(customer);
        ZZRHttp.postJson("http://101.132.74.147:8082/customer/myOrderList", json, new ZZRCallBack.CallBackString() {
            @Override
            public void onFailure(int i, String s) {
                new ToastDialog(MyOrderSlice.this)
                        .setText("网络不稳定")
                        .show();
            }
            @Override
            public void onResponse(String s) {
                result = gson.fromJson(s, Result.class);
                String json = gson.toJson(result.getResult());
                List<Order> list = gson.fromJson(json, new TypeToken<List<Order>>() {
                }.getType());
                MyOrderProvider myOrderProvider = new MyOrderProvider(list, MyOrderSlice.this);
                my_order_listcontainer.setItemProvider(myOrderProvider);
                my_order_listcontainer.setItemClickedListener(new ListContainer.ItemClickedListener() {
                    @Override
                    public void onItemClicked(ListContainer listContainer, Component component, int i, long l) {
                        Order order = (Order) my_order_listcontainer.getItemProvider().getItem(i);
                        Intent intent1 = new Intent();
                        intent1.setParam("Order",order);
                        intent1.setParam("Customer",customer);
                        present(new CheckOrderSlice(),intent1);
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
