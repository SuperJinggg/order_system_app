package com.example.ordersystemapplication.slice;

import com.example.ordersystemapplication.ResourceTable;
import com.example.ordersystemapplication.data.Result;
import com.example.ordersystemapplication.domain.Customer;
import com.example.ordersystemapplication.domain.Order;
import com.example.ordersystemapplication.domain.OrderFood;
import com.example.ordersystemapplication.provider.OrderProvider;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.zzrv5.mylibrary.ZZRCallBack;
import com.zzrv5.mylibrary.ZZRHttp;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.*;
import ohos.agp.window.dialog.ToastDialog;

import java.util.List;

public class CheckOrderSlice extends AbilitySlice implements Component.ClickedListener {
    private Image back;
    private Text orderId;
    private Text tableId;
    private Text orderStartT;
    private Text orderEndT;
    private Text allPrice;
    private Text tablePrice;
    private ListContainer check_order_listContainer;
    private Customer customer;
    private Order order;
    private Result result = new Result();
    private Gson gson = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    private Double table_Price = 0.00;
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_check_order);
        customer = intent.getSerializableParam("Customer");
        order = intent.getSerializableParam("Order");
        back = (Image) findComponentById(ResourceTable.Id_back);
        orderId = (Text) findComponentById(ResourceTable.Id_check_order_id);
        tableId = (Text) findComponentById(ResourceTable.Id_check_table_id);
        orderStartT = (Text) findComponentById(ResourceTable.Id_check_order_startT);
        orderEndT = (Text) findComponentById(ResourceTable.Id_check_order_endT);
        allPrice = (Text) findComponentById(ResourceTable.Id_check_order_all_price);
        tablePrice = (Text) findComponentById(ResourceTable.Id_check_table_price);
        back.setClickedListener(this);
        orderId.setText(order.getOrderId());
        tableId.setText(order.getTableId());
        orderStartT.setText(order.getCreateTime());
        orderEndT.setText(order.getEndTime());
        switch (order.getTableId().split("")[0]) {
            case "A":
                table_Price = 4.00;
                tablePrice.setText(table_Price + "   元");
                break;
            case "B":
                table_Price = 8.00;
                tablePrice.setText(table_Price + "   元");
                break;
            case "C":
                table_Price = 12.00;
                tablePrice.setText(table_Price + "   元");
                break;
            case "D":
                table_Price = 24.00;
                tablePrice.setText(table_Price + "   元");
                break;
            case "E":
                table_Price = 32.00;
                tablePrice.setText(table_Price + "   元");
                break;
        }
        String json = gson.toJson(order);
        ZZRHttp.postJson("http://101.132.74.147:8082/order/orderFoodList", json, new ZZRCallBack.CallBackString() {
            @Override
            public void onFailure(int i, String s) {
                new ToastDialog(CheckOrderSlice.this)
                        .setText("网络不稳定")
                        .show();
            }

            @Override
            public void onResponse(String s) {
                result = gson.fromJson(s, Result.class);
                String json = gson.toJson(result.getResult());
                List<OrderFood> list = gson.fromJson(json, new TypeToken<List<OrderFood>>() {}.getType());
                double price = 0;
                for (OrderFood orderFood : list) {

                    price += orderFood.getFoodPrice() * orderFood.getFoodNum();
                }
                allPrice.setText((price + table_Price) + "  元");
                if (list.size() > 0) {
                    check_order_listContainer = (ListContainer) findComponentById(ResourceTable.Id_check_order_listcontainer);
                    OrderProvider orderProvider = new OrderProvider(list, CheckOrderSlice.this);
                    check_order_listContainer.setItemProvider(orderProvider);
                }
            }
        });


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
    }
}
