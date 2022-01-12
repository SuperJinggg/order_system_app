package com.example.ordersystemapplication.slice;


import com.example.ordersystemapplication.ResourceTable;
import com.example.ordersystemapplication.data.Result;
import com.example.ordersystemapplication.domain.Customer;
import com.example.ordersystemapplication.utils.VerifyCodeUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zzrv5.mylibrary.ZZRCallBack;
import com.zzrv5.mylibrary.ZZRHttp;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.*;
import ohos.agp.window.dialog.ToastDialog;

public class RegisterAbilitySlice extends AbilitySlice implements Component.ClickedListener {
    private Button register;
    private Button getCode;
    private TextField registerPhone;
    private TextField registerName;
    private TextField registerPwd;
    private TextField surePwd;
    private TextField inputCode;
    private DirectionalLayout directionalLayout;
    private String code;
    private Button backLogin;
    private Text changeCode;
    private Result result;
    private String url = "http://101.132.74.147:8082/customer";
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_register);
        register = (Button) findComponentById(ResourceTable.Id_register);
        registerPhone = (TextField) findComponentById(ResourceTable.Id_register_phonenum);
        registerName = (TextField) findComponentById(ResourceTable.Id_register_name);
        registerPwd = (TextField) findComponentById(ResourceTable.Id_register_password);
        surePwd = (TextField) findComponentById(ResourceTable.Id_sure_password);
        inputCode = (TextField)findComponentById(ResourceTable.Id_code);
        getCode = (Button)findComponentById(ResourceTable.Id_get_code);
        backLogin = (Button)findComponentById(ResourceTable.Id_back_login);
        changeCode = (Text)findComponentById(ResourceTable.Id_change_code);
        register.setClickedListener(this);
        directionalLayout = (DirectionalLayout) findComponentById(ResourceTable.Id_click_register);
        getCode.setClickedListener(this);
        backLogin.setClickedListener(this);
        changeCode.setClickedListener(this);
        getCode.setText("");

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
        if (component.getId() == ResourceTable.Id_register) {

            if (registerPwd.getText().equals("") || surePwd.getText().equals("") || registerPhone.getText().equals("")) {
                new ToastDialog(this)
                        .setText("手机号和密码不能为空")
                        .show();
            } else if (!registerPwd.getText().equals(surePwd.getText())) {
                //判断两次输入的密码是否相同
                ToastDialog toastDialog = new ToastDialog(this);
                toastDialog.setText("两次输入的密码不一样！");
                toastDialog.show();
            } else if (!code.equals(inputCode.getText())){
                ToastDialog toastDialog = new ToastDialog(this);
                toastDialog.setText("验证码错误！");
                toastDialog.show();
                code = VerifyCodeUtils.generateVerifyCode(4);
                getCode.setText(code);
            }
            else {
                Customer customer = new Customer();
                customer.setCustPhone(registerPhone.getText());
                customer.setPassword(registerPwd.getText());
                customer.setCustName(registerName.getText());
                Gson gson = new GsonBuilder().serializeNulls().create();
                String json = gson.toJson(customer);
                ZZRHttp.postJson(url+"/register", json, new ZZRCallBack.CallBackString() {
                    @Override
                    public void onFailure(int i, String s) {
                        new ToastDialog(RegisterAbilitySlice.this)
                                .setText("网络不稳定")
                                .show();
                    }

                    @Override
                    public void onResponse(String s) {
                        result = gson.fromJson(s, Result.class);
                        String json = gson.toJson(result.getResult());
                        if (result.getCode() == 200) {
                            Intent intent = new Intent();
                            present(new SureRegisterSlice(), intent);
                        } else if (result.getCode() == 400) {
                            new ToastDialog(RegisterAbilitySlice.this)
                                    .setText(result.getMsg())
                                    .show();
                        }
                    }
                });
            }
        }
        if (component.getId() == ResourceTable.Id_get_code){
            code = VerifyCodeUtils.generateVerifyCode(4);
            getCode.setText(code);
            getCode.setClickable(false);
        }
        if (component.getId() == ResourceTable.Id_change_code){
            code = VerifyCodeUtils.generateVerifyCode(4);
            getCode.setText(code);
        }
        if (component.getId() == ResourceTable.Id_back_login){
            terminate();
        }
    }
}