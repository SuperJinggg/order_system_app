package com.example.ordersystemapplication.slice;

import com.example.ordersystemapplication.ResourceTable;
import com.example.ordersystemapplication.data.Result;
import com.example.ordersystemapplication.domain.Customer;
import com.example.ordersystemapplication.domain.Table;
import com.example.ordersystemapplication.provider.ReserveTableProvider;
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

public class ReserveTableSlice extends AbilitySlice implements Component.ClickedListener {
    private ListContainer reserve_listContainer;
    private Result result = new Result();
    private Gson gson = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd").create();
    private String url = "http://101.132.74.147:8082";
    private Image back;
    private Customer customer;
    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_reserve_table);
        back = (Image)findComponentById(ResourceTable.Id_back);
        back.setClickedListener(this);
        customer = intent.getSerializableParam("Customer");
        reserve_listContainer = (ListContainer) findComponentById(ResourceTable.Id_reserve_listcontainer);
        ZZRHttp.get(url + "/table/findTableList", new ZZRCallBack.CallBackString() {
            @Override
            public void onFailure(int i, String s) {
                new ToastDialog(ReserveTableSlice.this)
                        .setText("网络不稳定")
                        .show();
            }

            @Override
            public void onResponse(String s) {
                result = gson.fromJson(s,Result.class);
                String json = gson.toJson(result.getResult());
                List<Table> list = gson.fromJson(json, new TypeToken<List<Table>>() {}.getType());
                ReserveTableProvider reserveTableProvider = new ReserveTableProvider(list,ReserveTableSlice.this);
                reserve_listContainer.setItemProvider(reserveTableProvider);
                reserve_listContainer.setItemClickedListener(new ListContainer.ItemClickedListener() {
                    @Override
                    public void onItemClicked(ListContainer listContainer, Component component, int i, long l) {
                        Table table = (Table) reserve_listContainer.getItemProvider().getItem(i);
                        Intent intent1 = new Intent();
                        intent1.setParam("table",table);
                        intent1.setParam("Customer",customer);
                        present(new ReserveInfoSlice(),intent1);
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
