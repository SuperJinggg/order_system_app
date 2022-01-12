package com.example.ordersystemapplication.slice;

import com.example.ordersystemapplication.ResourceTable;
import com.example.ordersystemapplication.data.Result;
import com.example.ordersystemapplication.domain.*;
import com.example.ordersystemapplication.provider.FoodProvider;
import com.example.ordersystemapplication.utils.Popup;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnSelectListener;
import com.zzrv5.mylibrary.ZZRCallBack;
import com.zzrv5.mylibrary.ZZRHttp;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.*;
import ohos.agp.window.dialog.ToastDialog;

import java.util.ArrayList;
import java.util.List;

public class OrderFoodAbilitySlice extends AbilitySlice implements Component.ClickedListener {
    private Text No_Desk;
    private Text allPrice;
    private Result result = new Result();
    private Gson gson = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd").create();
    private AbilitySlice slice = this;
    private Food food;
    private Order order;
    private ListContainer foodList;
    private Image back;
    private Image search;
    private DirectionalLayout check_order;
    private TextField input_search;
    private Button fenleiFood;
    private Button submit_order;
    private Customer customer;
    private List<OrderFood> orderFoodList=new ArrayList<>();
    private String url = "http://101.132.74.147:8082/food";
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_order_food);
        order = intent.getSerializableParam("Order");
        customer = intent.getSerializableParam("Customer");
        check_order = (DirectionalLayout)findComponentById(ResourceTable.Id_check_order);
        No_Desk = (Text) findComponentById(ResourceTable.Id_No_desk);
        allPrice = (Text)findComponentById(ResourceTable.Id_all_price);
        input_search = (TextField)findComponentById(ResourceTable.Id_input_search);
        fenleiFood = (Button)findComponentById(ResourceTable.Id_fenlei_food);
        submit_order = (Button)findComponentById(ResourceTable.Id_submit_order);
        foodList = (ListContainer) findComponentById(ResourceTable.Id_food_listcontainer);
        search = (Image)findComponentById(ResourceTable.Id_search);
        back = (Image)findComponentById(ResourceTable.Id_back);
        No_Desk.setText(order.getTableId());

        //接收foodList
        ZZRHttp.get(url + "/foodList", new ZZRCallBack.CallBackString() {
            @Override
            public void onFailure(int i, String s) {
                new ToastDialog(slice)
                        .setText("网络不稳定")
                        .show();
            }
            @Override
            public void onResponse(String s) {
                result = gson.fromJson(s,Result.class);
                String json = gson.toJson(result.getResult());
                List<Food> list = gson.fromJson(json, new TypeToken<List<Food>>() {}.getType());
                FoodProvider foodProvider = new FoodProvider(list,OrderFoodAbilitySlice.this);
                foodList.setItemProvider(foodProvider);
                foodList.setItemProvider(foodProvider);
                foodList.setItemClickedListener(new ListContainer.ItemClickedListener() {
                    @Override
                    public void onItemClicked(ListContainer listContainer, Component component, int i, long l) {
                        food = (Food) foodList.getItemProvider().getItem(i);
                        double price = Double.parseDouble(allPrice.getText());
                        price += food.getFoodPrice();
                        allPrice.setText(price+"");
                        Boolean flag = false;
                        for (OrderFood orderFood:orderFoodList){
                            if (food.getFoodName().equals(orderFood.getFoodName())){
                                orderFood.setFoodNum(orderFood.getFoodNum()+1);
                                orderFood.setFoodPrice(orderFood.getFoodPrice()+food.getFoodPrice());
                                flag = true;
                                break;
                            }
                        }
                        if (!flag){
                            OrderFood orderFood = new OrderFood();
                            orderFood.setFoodId(food.getFoodId());
                            orderFood.setFoodName(food.getFoodName());
                            orderFood.setFoodNum(1);
                            orderFood.setFoodPrice(food.getFoodPrice());
                            orderFoodList.add(orderFood);
                        }
                    }
                });
                foodList.setItemLongClickedListener(new ListContainer.ItemLongClickedListener() {
                    @Override
                    public boolean onItemLongClicked(ListContainer listContainer, Component component, int i, long l) {
                        food = (Food) foodList.getItemProvider().getItem(i);
                        Intent intent1 = new Intent();
                        intent1.setParam("food",food);
                        presentForResult(new FoodInfoAbilitySlice(),intent1,100);
                        return true;
                    }
                });
            }
        });



        back.setClickedListener(this);
        search.setClickedListener(this);
        fenleiFood.setClickedListener(this);
        submit_order.setClickedListener(this);
        check_order.setClickedListener(this);

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
        if (component.getId() == ResourceTable.Id_search){
            new ToastDialog(OrderFoodAbilitySlice.this)
                    .setText(input_search.getText())
                    .show();
            fenleiFood.setText("菜品类目");
            ZZRHttp.get(url + "/findFoodList?foodName=" + input_search.getText(), new ZZRCallBack.CallBackString() {
                @Override
                public void onFailure(int i, String s) {
                    new ToastDialog(slice)
                            .setText("网络不稳定")
                            .show();
                    System.out.println(url + "/findFoodList?foodName=" + input_search.getText());
                }

                @Override
                public void onResponse(String s) {
                    result = gson.fromJson(s,Result.class);
                    String json = gson.toJson(result.getResult());
                    List<Food> list = gson.fromJson(json, new TypeToken<List<Food>>() {}.getType());
                    if (list.size()>0){
                        FoodProvider foodProvider = new FoodProvider(list,OrderFoodAbilitySlice.this);
                        foodList.setItemProvider(foodProvider);
                        foodList.setItemProvider(foodProvider);
                        foodList.setItemClickedListener(new ListContainer.ItemClickedListener() {
                            @Override
                            public void onItemClicked(ListContainer listContainer, Component component, int i, long l) {
                                food = (Food) foodList.getItemProvider().getItem(i);
                                double price = Double.parseDouble(allPrice.getText());
                                price += food.getFoodPrice();
                                allPrice.setText(price+"");
                                Boolean flag = false;
                                for (OrderFood orderFood:orderFoodList){
                                    if (food.getFoodName().equals(orderFood.getFoodName())){
                                        orderFood.setFoodNum(orderFood.getFoodNum()+1);
                                        orderFood.setFoodPrice(orderFood.getFoodPrice()+food.getFoodPrice());
                                        flag = true;
                                        break;
                                    }
                                }
                                if (!flag){
                                    OrderFood orderFood = new OrderFood();
                                    orderFood.setFoodId(food.getFoodId());
                                    orderFood.setFoodName(food.getFoodName());
                                    orderFood.setFoodNum(1);
                                    orderFood.setFoodPrice(food.getFoodPrice());
                                    orderFoodList.add(orderFood);
                                }
                            }
                        });
                        foodList.setItemLongClickedListener(new ListContainer.ItemLongClickedListener() {
                            @Override
                            public boolean onItemLongClicked(ListContainer listContainer, Component component, int i, long l) {
                                food = (Food) foodList.getItemProvider().getItem(i);
                                Intent intent1 = new Intent();
                                intent1.setParam("food",food);
                                presentForResult(new FoodInfoAbilitySlice(),intent1,100);
                                return true;
                            }
                        });
                    }else {
                        new ToastDialog(OrderFoodAbilitySlice.this)
                                .setText("未找到相应结果")
                                .show();
                        input_search.setText("未找到结果...");
                    }
                }
            });
        }
        if (component.getId() == ResourceTable.Id_fenlei_food){

            ZZRHttp.get("http://101.132.74.147:8082/category/categoryList", new ZZRCallBack.CallBackString() {
                @Override
                public void onFailure(int i, String s) {
                    new ToastDialog(slice)
                            .setText("网络不稳定")
                            .show();
                }

                @Override
                public void onResponse(String s) {
                    result = gson.fromJson(s,Result.class);
                    String json1 = gson.toJson(result.getResult());
                    List<Category> list = gson.fromJson(json1, new TypeToken<List<Category>>() {}.getType());
                    String[] categoryList = new String[list.size()];
                    for (int i=0;i<list.size();i++){
                        categoryList[i] = list.get(i).getCategoryName();
                    }
                    new XPopup.Builder(getContext())
                            .isDestroyOnDismiss(true) // 对于只使用一次的弹窗，推荐设置这个
                            .asBottomList("请选择菜品类目", categoryList,
                                    null, 2,
                                    new OnSelectListener() {
                                        @Override
                                        public void onSelect(int position, String text) {
                                            fenleiFood.setText(text);
                                            if (!fenleiFood.equals("菜品类目")){
                                                Category category = new Category();
                                                category.setCategoryName(fenleiFood.getText());
                                                String json = gson.toJson(category);
                                                ZZRHttp.postJson(url + "/findFoodListByCategory", json, new ZZRCallBack.CallBackString() {
                                                    @Override
                                                    public void onFailure(int i, String s) {
                                                        new ToastDialog(slice)
                                                                .setText("网络不稳定")
                                                                .show();
                                                    }

                                                    @Override
                                                    public void onResponse(String s) {
                                                        result = gson.fromJson(s,Result.class);
                                                        String json2 = gson.toJson(result.getResult());
                                                        List<Food> list = gson.fromJson(json2, new TypeToken<List<Food>>() {}.getType());
                                                        if (list.size()>0){
                                                            FoodProvider foodProvider = new FoodProvider(list,OrderFoodAbilitySlice.this);
                                                            foodList.setItemProvider(foodProvider);
                                                            foodList.setItemProvider(foodProvider);
                                                            foodList.setItemClickedListener(new ListContainer.ItemClickedListener() {
                                                                @Override
                                                                public void onItemClicked(ListContainer listContainer, Component component, int i, long l) {
                                                                    food = (Food) foodList.getItemProvider().getItem(i);
                                                                    double price = Double.parseDouble(allPrice.getText());
                                                                    price += food.getFoodPrice();
                                                                    allPrice.setText(price+"");
                                                                    Boolean flag = false;
                                                                    for (OrderFood orderFood:orderFoodList){
                                                                        if (food.getFoodName().equals(orderFood.getFoodName())){
                                                                            orderFood.setFoodNum(orderFood.getFoodNum()+1);
                                                                            orderFood.setFoodPrice(orderFood.getFoodPrice()+food.getFoodPrice());
                                                                            flag = true;
                                                                            break;
                                                                        }
                                                                    }
                                                                    if (!flag){
                                                                        OrderFood orderFood = new OrderFood();
                                                                        orderFood.setFoodId(food.getFoodId());
                                                                        orderFood.setFoodName(food.getFoodName());
                                                                        orderFood.setFoodNum(1);
                                                                        orderFood.setFoodPrice(food.getFoodPrice());
                                                                        orderFoodList.add(orderFood);
                                                                    }
                                                                }
                                                            });
                                                            foodList.setItemLongClickedListener(new ListContainer.ItemLongClickedListener() {
                                                                @Override
                                                                public boolean onItemLongClicked(ListContainer listContainer, Component component, int i, long l) {
                                                                    food = (Food) foodList.getItemProvider().getItem(i);
                                                                    Intent intent1 = new Intent();
                                                                    intent1.setParam("food",food);
                                                                    presentForResult(new FoodInfoAbilitySlice(),intent1,100);
                                                                    return true;
                                                                }
                                                            });
                                                        }else {
                                                            new ToastDialog(OrderFoodAbilitySlice.this)
                                                                    .setText("未找到相应结果")
                                                                    .show();
                                                            fenleiFood.setText("菜品类目");
                                                        }
                                                    }
                                                });
                                            }
                                        }
                                    })
                            .show();

                }
            });
        }
        if (component.getId() == ResourceTable.Id_check_order){

            new XPopup.Builder(this)
                    .isDarkTheme(false)
                    .asCustom(new Popup(this,orderFoodList,allPrice))
                    .show();

            System.out.println(orderFoodList.get(0).getFoodPrice());
        }
        if (component.getId() == ResourceTable.Id_submit_order){

            List<Record> recordList = new ArrayList<>();
            for (int i = 0;i<orderFoodList.size();i++){
                recordList.add(new Record());
                recordList.get(i).setOrderId(order.getOrderId());
                recordList.get(i).setFoodId(orderFoodList.get(i).getFoodId());
                recordList.get(i).setFoodNum(orderFoodList.get(i).getFoodNum());
            }
            String json = gson.toJson(recordList);
            System.out.println(json);

            ZZRHttp.postJson("http://101.132.74.147:8082/record/addRecord", json, new ZZRCallBack.CallBackString() {
                @Override
                public void onFailure(int i, String s) {
                    new ToastDialog(slice)
                            .setText("网络不稳定")
                            .show();
                }
                @Override
                public void onResponse(String s) {
                    new ToastDialog(slice)
                            .setText("订单创建成功！")
                            .show();
                    Intent intent = new Intent();
                    intent.setParam("Order",order);
                    intent.setParam("Customer",customer);
                    present(new OrderInfoAbilitySlice(),intent);
                }
            });
        }
        if (component.getId() == ResourceTable.Id_add_food){

        }
        if (component.getId() == ResourceTable.Id_dl_food){

        }
    }


    @Override
    protected void onResult(int requestCode, Intent resultIntent) {
        super.onResult(requestCode, resultIntent);
        if (requestCode == 100){
            food = resultIntent.getSerializableParam("food");
            String tiaozhuan_flag = resultIntent.getSerializableParam("flag");
            if (tiaozhuan_flag.equals("")){
                return;
            }
            double price = Double.parseDouble(allPrice.getText());
            price += food.getFoodPrice();
            allPrice.setText(price+"");
            Boolean flag = false;
            for (OrderFood orderFood:orderFoodList){
                if (food.getFoodName().equals(orderFood.getFoodName())){
                    orderFood.setFoodNum(orderFood.getFoodNum()+1);
                    orderFood.setFoodPrice(orderFood.getFoodPrice()+food.getFoodPrice());
                    flag = true;
                    break;
                }
            }
            if (!flag){
                OrderFood orderFood = new OrderFood();
                orderFood.setFoodId(food.getFoodId());
                orderFood.setFoodName(food.getFoodName());
                orderFood.setFoodNum(1);
                orderFood.setFoodPrice(food.getFoodPrice());
                orderFoodList.add(orderFood);
            }
        }
    }
}
