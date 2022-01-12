package com.example.ordersystemapplication.slice;

import com.example.ordersystemapplication.ResourceTable;
import com.example.ordersystemapplication.Saomiao;
import com.example.ordersystemapplication.data.Result;
import com.example.ordersystemapplication.domain.Customer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.*;

public class UserSystemSlice extends AbilitySlice implements Component.ClickedListener {
    private Text username;
    private AbilitySlice slice = this;
    private Button order;
    private Button reserve;
    private int tabLastPosition;
    private Component componentLayout;
    private TabList tabList;
    private String[] str = {"首页","我的"};
    private TabListSelect listSelect = new TabListSelect();
    private Customer customer;
    private Result result = new Result();
    private Gson gson = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd").create();
    private String url = "http://101.132.74.147:8082";
    private Text myBalance;
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_navigation_bar);

        customer = intent.getSerializableParam("my");
        System.out.println("首页的参数："+ customer);

        tabList = (TabList) findComponentById(ResourceTable.Id_tab_list_bottom);
        for (int i = 0; i < str.length; i++) {
            TabList.Tab tab = tabList.new Tab(getContext());
            tab.setText(str[i]);
            tabList.addTab(tab);
        }
        tabList.setFixedMode(true);
        tabList.addTabSelectedListener(listSelect);
        //默认第一个被选中
        tabList.getTabAt(0).select();


    }
    class TabListSelect implements TabList.TabSelectedListener{
        @Override
        public void onSelected(TabList.Tab tab) {
            // 当某个Tab从未选中状态变为选中状态时的回调
            ComponentContainer container = (ComponentContainer) findComponentById(ResourceTable.Id_tab_container);
            //根据tab接收的数据，匹配字符串，选择相应功能
            if(tab.getText().equals("首页")){
                container.removeAllComponents();
                componentLayout = LayoutScatter.getInstance(slice).parse(ResourceTable.Layout_ability_user_system,null,false);
                implementUserSystem(componentLayout);
                container.addComponent(componentLayout);
            }else if(tab.getText().equals("我的")){
                container.removeAllComponents();
                componentLayout = LayoutScatter.getInstance(slice).parse(ResourceTable.Layout_ability_user_info,null,false);
                implementUserInfo(componentLayout);
                container.addComponent(componentLayout);
            }
        }

        @Override
        public void onUnselected(TabList.Tab tab) {
            // 当某个Tab从选中状态变为未选中状态时的回调
            tabLastPosition = tab.getPosition();
        }

        @Override
        public void onReselected(TabList.Tab tab) {
            // 当某个Tab已处于选中状态，再次被点击时的状态回调
        }
    }
    private void implementUserSystem(Component componentLayout) {
        username = (Text)componentLayout.findComponentById(ResourceTable.Id_user_phone);
        order = (Button)componentLayout.findComponentById(ResourceTable.Id_order);
        reserve = (Button)componentLayout.findComponentById(ResourceTable.Id_reserve);
        order.setClickedListener(this);
        reserve.setClickedListener(this);
        username.setText(customer.getCustName());
    }
    private void implementUserInfo(Component componentLayout) {
        Text username = (Text) componentLayout.findComponentById(ResourceTable.Id_customer_name);

        myBalance = (Text)componentLayout.findComponentById(ResourceTable.Id_my_money);
        Button check_reserve = (Button)componentLayout.findComponentById(ResourceTable.Id_check_reserve_table);
        Button check_order = (Button)componentLayout.findComponentById(ResourceTable.Id_check_order);
        Button recharge = (Button)componentLayout.findComponentById(ResourceTable.Id_recharge);
        Button update_name = (Button)componentLayout.findComponentById(ResourceTable.Id_update_name);
        username.setText(customer.getCustName());
        myBalance.setText(customer.getCustBalance()+"");
        check_order.setClickedListener(UserSystemSlice.this);
        check_reserve.setClickedListener(UserSystemSlice.this);
        recharge.setClickedListener(UserSystemSlice.this);
        update_name.setClickedListener(this);
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
        if (component.getId() == ResourceTable.Id_order) {
            Intent intent = new Intent();
            intent.setParam("my",customer);
            Operation operation = new Intent.OperationBuilder()
                    .withDeviceId("")
                    .withBundleName("com.example.ordersystemapplication")
                    .withAbilityName(Saomiao.class)
                    .build();
            intent.setOperation(operation);
            startAbility(intent);
        }
        if (component.getId() == ResourceTable.Id_reserve){
            Intent intent = new Intent();
            intent.setParam("Customer",customer);
            present(new ReserveTableSlice(),intent);
        }
        if (component.getId() == ResourceTable.Id_check_order){
            Intent intent =  new Intent();
            intent.setParam("Customer",customer);
            present(new MyOrderSlice(),intent);

        }
        if (component.getId() == ResourceTable.Id_check_reserve_table){
            Intent intent = new Intent();
            intent.setParam("Customer",customer);
            present(new CheckReserveSlice(),intent);
        }
        if (component.getId() == ResourceTable.Id_recharge){
            Intent intent = new Intent();
            intent.setParam("Customer",customer);
            present(new RechargeSlice(),intent);
        }
        if (component.getId() == ResourceTable.Id_update_name){
            Intent intent = new Intent();
            intent.setParam("Customer",customer);
            present(new UpdateNameSlice(),intent);
        }


    }
}
