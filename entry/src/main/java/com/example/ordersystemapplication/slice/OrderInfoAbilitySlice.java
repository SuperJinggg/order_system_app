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

public class OrderInfoAbilitySlice extends AbilitySlice implements Component.ClickedListener {
    private Image back;
    private Text orderId;
    private Text tableId;
    private Text orderStartT;
    private Text allPrice;
    private Text tablePrice;
    private ListContainer order_listContainer;
    private Order order;
    private Customer customer;
    private Result result = new Result();
    private Button payOrder;
    private Button continueOrder;
    private Double table_Price = 0.00;
    private Gson gson = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd").create();
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_order_info);
        order = intent.getSerializableParam("Order");
        customer = intent.getSerializableParam("Customer");
        back = (Image)findComponentById(ResourceTable.Id_back);
        orderId = (Text)findComponentById(ResourceTable.Id_order_id);
        tableId = (Text)findComponentById(ResourceTable.Id_table_id);
        orderStartT = (Text)findComponentById(ResourceTable.Id_order_startT);
        allPrice = (Text)findComponentById(ResourceTable.Id_order_all_price);
        tablePrice = (Text)findComponentById(ResourceTable.Id_table_price);
        payOrder = (Button)findComponentById(ResourceTable.Id_pay_order);
        continueOrder = (Button)findComponentById(ResourceTable.Id_continue_order);
        back.setClickedListener(this);
        payOrder.setClickedListener(this);
        continueOrder.setClickedListener(this);
        orderId.setText(order.getOrderId());
        tableId.setText(order.getTableId());
        orderStartT.setText(order.getCreateTime());

        switch (order.getTableId().split("")[0]){
            case "A":
                table_Price = 4.00;
                tablePrice.setText(table_Price+"   元");
                break;
            case "B":
                table_Price = 8.00;
                tablePrice.setText(table_Price+"   元");
                break;
            case "C":
                table_Price = 12.00;
                tablePrice.setText(table_Price+"   元");
                break;
            case "D":
                table_Price = 24.00;
                tablePrice.setText(table_Price+"   元");
                break;
            case "E":
                table_Price = 32.00;
                tablePrice.setText(table_Price+"   元");
                break;
        }
        String json = gson.toJson(order);
        ZZRHttp.postJson("http://101.132.74.147:8082/order/orderFoodList", json, new ZZRCallBack.CallBackString() {
            @Override
            public void onFailure(int i, String s) {
                new ToastDialog(OrderInfoAbilitySlice.this)
                        .setText("网络不稳定")
                        .show();
            }

            @Override
            public void onResponse(String s) {
                result = gson.fromJson(s,Result.class);
                String json = gson.toJson(result.getResult());
                List<OrderFood> list = gson.fromJson(json, new TypeToken<List<OrderFood>>() {}.getType());
                double price=0;
                for (OrderFood orderFood:list){
                    price += orderFood.getFoodPrice()*orderFood.getFoodNum();
                }
                allPrice.setText((price+table_Price)+"  元");
                if (list.size()>0){
                    System.out.println("连接成功");
                    order_listContainer = (ListContainer)findComponentById(ResourceTable.Id_order_listcontainer);
                    OrderProvider orderProvider = new OrderProvider(list,OrderInfoAbilitySlice.this);
                    order_listContainer.setItemProvider(orderProvider);
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
        if (component == back){
            terminate();
        }
        if (component == payOrder){
            //支付订单
            String json = gson.toJson(order);
            ZZRHttp.postJson("http://101.132.74.147:8082/order/payOrder", json, new ZZRCallBack.CallBackString() {
                @Override
                public void onFailure(int i, String s) {
                    new ToastDialog(OrderInfoAbilitySlice.this)
                            .setText("网络不稳定")
                            .show();
                }
                @Override
                public void onResponse(String s) {
                    result = gson.fromJson(s,Result.class);
                    if (result.getCode() == 200){
                        Intent intent = new Intent();
                        intent.setParam("Customer",customer);
                        present(new SuccessPaySlice(),intent);
                    } else if (result.getCode() == 400){
                        new ToastDialog(OrderInfoAbilitySlice.this)
                                .setText(result.getMsg())
                                .show();
                    }
                }
            });
        }
        if (component == continueOrder){
            Intent intent = new Intent();
            intent.setParam("Order",order);
            intent.setParam("Customer",customer);
            present(new OrderFoodAbilitySlice(),intent);
        }
    }
}
