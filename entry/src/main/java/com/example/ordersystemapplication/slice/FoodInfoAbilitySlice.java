package com.example.ordersystemapplication.slice;

import com.example.ordersystemapplication.ResourceTable;
import com.example.ordersystemapplication.domain.Food;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.*;

public class FoodInfoAbilitySlice extends AbilitySlice implements Component.ClickedListener {
    private Food food;
    private Image back;
    private Text food_name;
    private Text food_price;
    private Text food_content;
    private Button add_food;
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_food_info);
        food = intent.getSerializableParam("food");
        add_food = (Button)findComponentById(ResourceTable.Id_add_food);
        back = (Image)findComponentById(ResourceTable.Id_back);
        food_name = (Text)findComponentById(ResourceTable.Id_info_food_name);
        food_price = (Text)findComponentById(ResourceTable.Id_info_food_price);
        food_content = (Text)findComponentById(ResourceTable.Id_info_food_content);
        back.setClickedListener(this);
        add_food.setClickedListener(this);
        food_name.setText(food.getFoodName());
        food_price.setText("￥" + food.getFoodPrice() + "");
        food_content.setText(food.getFoodDesc());
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
            Intent intent = new Intent();
            intent.setParam("flag","");
            intent.setParam("food",food);
            setResult(intent);
            terminate();
        }
        if (component.getId() == ResourceTable.Id_add_food){
            Intent intent = new Intent();
            intent.setParam("flag","1");
            intent.setParam("food",food);
            setResult(intent);
            terminate();
        }
    }
}
