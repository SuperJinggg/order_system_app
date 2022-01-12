package com.example.ordersystemapplication.slice;


import com.example.ordersystemapplication.ResourceTable;
import com.example.ordersystemapplication.data.Result;
import com.example.ordersystemapplication.domain.Customer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zzrv5.mylibrary.ZZRCallBack;
import com.zzrv5.mylibrary.ZZRHttp;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.agp.components.Image;
import ohos.agp.components.TextField;
import ohos.agp.window.dialog.CommonDialog;
import ohos.agp.window.dialog.IDialog;
import ohos.agp.window.dialog.ToastDialog;

public class RechargeSlice extends AbilitySlice implements Component.ClickedListener {
        private Image back;
        private Button sureCharge;
        private TextField moneyNum;
        private Customer customer;
        @Override
        public void onStart(Intent intent) {
            super.onStart(intent);
            super.setUIContent(ResourceTable.Layout_ability_recharge);
            customer = intent.getSerializableParam("Customer");
            sureCharge = (Button) findComponentById(ResourceTable.Id_sure_charge_money);
            moneyNum = (TextField)findComponentById(ResourceTable.Id_money_num);
            back = (Image)findComponentById(ResourceTable.Id_back);
            moneyNum.setText("");
            back.setClickedListener(this);
            sureCharge.setClickedListener(this);
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
            if (component.getId() == ResourceTable.Id_sure_charge_money){
                if (moneyNum.getText().equals("")){
                    new ToastDialog(RechargeSlice.this)
                            .setText("充值金额不能为空！")
                            .show();
                    return;
                } else if (Double.parseDouble(moneyNum.getText())>50000){
                    new ToastDialog(RechargeSlice.this)
                            .setText("充值金额超过单笔上限！")
                            .show();
                    return;
                }
                String url =  "http://101.132.74.147:8082/customer/recharge";
                Gson gson = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd").create();
                customer.setCustBalance(Double.parseDouble(moneyNum.getText())+customer.getCustBalance());
                String json = gson.toJson(customer);
                ZZRHttp.postJson(url, json, new ZZRCallBack.CallBackString() {
                    @Override
                    public void onFailure(int i, String s) {
                        new ToastDialog(RechargeSlice.this)
                                .setText("网络不稳定")
                                .show();
                    }
                    @Override
                    public void onResponse(String s) {
                        Result result = gson.fromJson(s,Result.class);
                        if (result.getCode() == 200){
                            //创建弹框对象
                            CommonDialog commonDialog = new CommonDialog(RechargeSlice.this);
                            //设置弹框标题
                            commonDialog.setTitleText("提示");
                            //设置弹框提示内容
                            commonDialog.setContentText("充值成功！！！");
                            //点击弹框外部可关闭弹框
                            commonDialog.setAutoClosable(true);
                            //设置弹框选择按钮
                            commonDialog.setButton(0, "确定", new IDialog.ClickedListener() {
                                @Override
                                public void onClick(IDialog iDialog, int i) {
                                    commonDialog.destroy();
                                }
                            });
                            //将弹框展示出来
                            commonDialog.show();
                            moneyNum.setText("");

                        }else if (result.getCode() == 400){
                            new ToastDialog(RechargeSlice.this)
                                    .setText(result.getMsg())
                                    .setDuration(5000)
                                    .show();
                        }
                    }
                });
            }
        }
}
