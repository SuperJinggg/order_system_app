package com.example.ordersystemapplication.utils;

import com.example.ordersystemapplication.ResourceTable;
import com.example.ordersystemapplication.domain.OrderFood;
import com.example.ordersystemapplication.domain.Record;
import com.example.ordersystemapplication.provider.OrderProvider;
import com.example.ordersystemapplication.provider.RecorderProvider;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.interfaces.XPopupCallback;
import com.lxj.xpopup.provider.EasyProvider;
import com.lxj.xpopup.util.XPopupUtils;
import ohos.aafwk.ability.AbilitySlice;
import ohos.agp.components.*;
import ohos.agp.render.render3d.ViewHolder;
import ohos.app.Context;

import java.util.ArrayList;
import java.util.List;

public class Popup extends BottomPopupView implements Component.ClickedListener {
    private Image add;
    private Image decrease;
    private OrderFood orderFood;
    private ListContainer recorder_listContainer;
    private List<OrderFood> list;
    private AbilitySlice as;
    private Text allPrice;
    public Popup(Context context, List<OrderFood> list, Component allPrice) {
        super(context, null);
        this.list = list;
        this.as = (AbilitySlice) context;
        this.allPrice = (Text) allPrice;
    }
    @Override
    protected int getImplLayoutId() {
        return ResourceTable.Layout_custom_bottom_popup;
    }
    @Override
    protected void onCreate() {
        super.onCreate();
        recorder_listContainer = (ListContainer) findComponentById(ResourceTable.Id_recorder_listcontainer);
        RecorderProvider provider = new RecorderProvider(list,as);
        recorder_listContainer.setItemProvider(provider);
        recorder_listContainer.setItemClickedListener(new ListContainer.ItemClickedListener() {
            @Override
            public void onItemClicked(ListContainer listContainer, Component component, int i, long l) {
                add = (Image)component.findComponentById(ResourceTable.Id_add_record);
                decrease = (Image)component.findComponentById(ResourceTable.Id_decrease_record);
                add.setClickedListener(Popup.this::onClick);
                decrease.setClickedListener(Popup.this::onClick);
                orderFood = (OrderFood)recorder_listContainer.getItemProvider().getItem(i);
            }
        });
    }
    // 最大高度为Window的0.7
    @Override
    protected int getMaxHeight() {
        return (int) (XPopupUtils.getScreenHeight(as) * .5f);
    }




    @Override
    public void onClick(Component component) {
        if (component.getId() == ResourceTable.Id_decrease_record){
            Double danjia = orderFood.getFoodPrice()/orderFood.getFoodNum();
            orderFood.setFoodNum(orderFood.getFoodNum()-1);
            orderFood.setFoodPrice(danjia*orderFood.getFoodNum());
//            if (orderFood.getFoodNum() == 0){
//                for (int i=0;i<list.size();i++){
//                    if (orderFood.getFoodName().equals(list.get(i).getFoodName())){
//                        list.remove(i);
//                        break;
//                    }
//                }
//            }
            if (orderFood.getFoodNum() == 0){
                list.remove(orderFood);
            }
            double price=0;
            for (OrderFood orderFood:list){
                price += orderFood.getFoodPrice();
            }
            allPrice.setText(price+"");

            dismiss();
            new XPopup.Builder(as)
                    .isDarkTheme(false)
                    .asCustom(new Popup(as,list,allPrice))
                    .show();

        }
        if (component.getId() == ResourceTable.Id_add_record){
            Double danjia = orderFood.getFoodPrice()/orderFood.getFoodNum();
            orderFood.setFoodNum(orderFood.getFoodNum()+1);
            orderFood.setFoodPrice(danjia*orderFood.getFoodNum());
            double price=0;
            for (OrderFood orderFood:list){
                price += orderFood.getFoodPrice();
            }
            allPrice.setText(price+"");
            dismiss();
            new XPopup.Builder(as)
                    .isDarkTheme(false)
                    .asCustom(new Popup(as,list,allPrice))
                    .show();
        }
    }
}