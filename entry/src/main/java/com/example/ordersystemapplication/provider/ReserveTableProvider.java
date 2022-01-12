package com.example.ordersystemapplication.provider;

import com.example.ordersystemapplication.ResourceTable;
import com.example.ordersystemapplication.domain.OrderFood;
import com.example.ordersystemapplication.domain.Table;
import ohos.aafwk.ability.AbilitySlice;
import ohos.agp.components.*;

import java.util.List;

public class ReserveTableProvider extends BaseItemProvider {
    private List<Table> list;
    private AbilitySlice as;

    public ReserveTableProvider(List<Table> list, AbilitySlice as) {
        this.list = list;
        this.as = as;
    }
    //返回item中要加载的布局对象
    //参数一：当前要加载哪一行item（item的索引
    //参数二：表示要销毁的item的布局对象
    //参数三：优化
    @Override
    public Component getComponent(int i, Component component, ComponentContainer componentContainer) {
        DirectionalLayout dl;
        //当有要销毁的布局对象，直接拿来用，就不需要不停地加载新的同一个布局对象了
        if (component!=null){
            dl = (DirectionalLayout)component;
        }else {
            //当没有要销毁的布局对象，说明没有创建到布局对象，所以新建一个
            //获取每一个item里面的布局对象
            dl = (DirectionalLayout) LayoutScatter.getInstance(as).parse(ResourceTable.Layout_table_item, null, false);
        }
        Table table = list.get(i);
        Text table_id = (Text) dl.findComponentById(ResourceTable.Id_table_id);
        Text table_price = (Text) dl.findComponentById(ResourceTable.Id_table_price);
        Text table_state = (Text) dl.findComponentById(ResourceTable.Id_table_state);
        Text table_people = (Text) dl.findComponentById(ResourceTable.Id_table_people);

        table_id.setText(table.getTableId());
        table_price.setText(table.getTablePrice()+"");

        if (table.getTableState() == 1){
            table_state.setText("占用");
        }else if (table.getTableState() == 2){
            table_state.setText("空闲");
        }

        table_people.setText(table.getFullPeople()+"");


        //当上面的四行代码执行完成后，就获取到了一个有数据的布局对象
        //此时我们只要把布局对象dl返回出去就可以了
        //因为在Item当中，最外层的就是这个dl
        return dl;
    }
    //总数据的个数（item个数）
    @Override
    public int getCount() {
        return list.size();
    }
    //参数i表示索引
    //根据索引返回数据
    @Override
    public Object getItem(int i) {
        if (list!=null&&i>=0&&i<list.size()){
            return list.get(i);
        }
        return null;
    }
    //返回某一项的id
    @Override
    public long getItemId(int i) {
        return i;
    }


}

