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
import ohos.agp.components.TextField;
import ohos.agp.window.dialog.ToastDialog;

public class ForgetPwdSlice extends AbilitySlice implements Component.ClickedListener {
    private Button sure_update;
    private Button back_to_login;
    private TextField phone;
    private TextField pwd;
    private TextField surePwd;
    Result result = new Result();
    private String url = "http://101.132.74.147:8082/customer";

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_forget_password);
        phone = (TextField)findComponentById(ResourceTable.Id_forget_phonenum);
        pwd = (TextField)findComponentById(ResourceTable.Id_forget_password);
        surePwd = (TextField)findComponentById(ResourceTable.Id_forget_confirm_password);
        sure_update = (Button)findComponentById(ResourceTable.Id_update_pwd);
        back_to_login = (Button)findComponentById(ResourceTable.Id_back_to_login);
        sure_update.setClickedListener(this);
        back_to_login.setClickedListener(this);

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
        if (component == back_to_login) {
            Intent intent = new Intent();
            present(new MainAbilitySlice(), intent);
        }
        if (component == sure_update) {
            if (phone.getText().equals("") || pwd.getText().equals("")) {
                new ToastDialog(this)
                        .setText("用户名或密码不能为空！")
                        .show();
            } else if (!pwd.getText().equals(surePwd.getText())) {
                //判断两次输入的密码是否相同
                ToastDialog toastDialog = new ToastDialog(this);
                toastDialog.setText("两次输入的密码不一样！");
                toastDialog.show();
            }else {
                Customer customer = new Customer();
                customer.setCustPhone(phone.getText());
                customer.setPassword(pwd.getText());
                customer.setCustName(surePwd.getText());
                Gson gson = new GsonBuilder().serializeNulls().create();
                String json = gson.toJson(customer);
                ZZRHttp.postJson(url+"/forgetPassword", json, new ZZRCallBack.CallBackString() {
                    @Override
                    public void onFailure(int i, String s) {
                        new ToastDialog(ForgetPwdSlice.this)
                                .setText("网络不稳定")
                                .show();
                    }

                    @Override
                    public void onResponse(String s) {
                        result = gson.fromJson(s, Result.class);
                        String json = gson.toJson(result.getResult());
                        if (result.getCode() == 200) {
                            Intent intent = new Intent();
                            present(new SureForgetPwdSlice(), intent);
                        } else if (result.getCode() == 400) {
                            new ToastDialog(ForgetPwdSlice.this)
                                    .setText(result.getMsg())
                                    .show();
                        }
                    }
                });
            }
        }
    }
}