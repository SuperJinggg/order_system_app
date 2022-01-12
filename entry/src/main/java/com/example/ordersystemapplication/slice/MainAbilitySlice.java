package com.example.ordersystemapplication.slice;

import com.example.ordersystemapplication.ResourceTable;
import com.example.ordersystemapplication.UserSystem;
import com.example.ordersystemapplication.data.Result;
import com.example.ordersystemapplication.domain.Customer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zzrv5.mylibrary.ZZRCallBack;
import com.zzrv5.mylibrary.ZZRHttp;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.agp.components.Text;
import ohos.agp.components.TextField;
import ohos.agp.window.dialog.ToastDialog;

public class MainAbilitySlice extends AbilitySlice implements Component.ClickedListener {
    private Button register;
    private Button login;
    private Text forget_Pwd;
    private TextField loginName;
    private TextField loginPwd;
    Result result = new Result();
    private String url = "http://101.132.74.147:8082/customer";
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_login);
        forget_Pwd = (Text)findComponentById(ResourceTable.Id_forget_password);
        forget_Pwd.setClickedListener(this);
        login = (Button)findComponentById(ResourceTable.Id_login);
        login.setClickedListener(this);
        register = (Button)findComponentById(ResourceTable.Id_register_user);
        register.setClickedListener(this);
        loginName =(TextField)findComponentById(ResourceTable.Id_login_phonenum);
        loginPwd = (TextField)findComponentById(ResourceTable.Id_login_password);

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
        if (component == register){
            Intent intent = new Intent();
            present(new RegisterAbilitySlice(),intent);
        }
        if (component == forget_Pwd){
            Intent intent = new Intent();
            present(new ForgetPwdSlice(),intent);
        }
        if (component == login){
            if (loginName.getText().equals("")||loginPwd.getText().equals("")){
                new ToastDialog(this)
                        .setText("用户名或密码不能为空！")
                        .show();
            }else {
                Customer customer = new Customer();
                customer.setCustPhone(loginName.getText());
                customer.setPassword(loginPwd.getText());
                Gson gson = new GsonBuilder().serializeNulls().create();
                String json = gson.toJson(customer);
                ZZRHttp.postJson(url+"/login",json, new ZZRCallBack.CallBackString() {
                    @Override
                    public void onFailure(int i, String s) {
                        new ToastDialog(MainAbilitySlice.this)
                                .setText("网络不稳定")
                                .show();
                    }

                    @Override
                    public void onResponse(String s) {
                        //访问成功
                        //s为result的json格式
                        result = gson.fromJson(s,Result.class);
                        String json = gson.toJson(result.getResult());
                        if (result.getCode()==200){
                            Customer customer1 = null;
                            customer1 = gson.fromJson(json,Customer.class);
                            if (customer1 != null){
                                //用户名和密码都正确
                                //进应用
                                //登录主页面
                                Intent intent = new Intent();
                                intent.setParam("my",customer1);
                                Operation operation = new Intent.OperationBuilder()
                                        .withDeviceId("")
                                        .withBundleName("com.example.ordersystemapplication")
                                        .withAbilityName(UserSystem.class)
                                        .build();
                                intent.setOperation(operation);
                                startAbility(intent);
                            }
                        }else if (result.getCode() == 400){
                            new ToastDialog(MainAbilitySlice.this)
                                    .setText(result.getMsg())
                                    .show();
                        }
                    }
                });
            }
        }
    }
}

