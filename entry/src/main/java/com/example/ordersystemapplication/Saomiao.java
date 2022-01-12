package com.example.ordersystemapplication;

import com.example.ordersystemapplication.slice.SaomiaoSlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import ohos.bundle.IBundleManager;

public class Saomiao extends Ability {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(SaomiaoSlice.class.getName());
        //判断是否有相机权限
        if (verifySelfPermission("ohos.permission.CAMERA") != IBundleManager.PERMISSION_GRANTED) {
            // 应用未被授予权限，判断是否可以申请弹框授权(首次申请或者用户未选择禁止且不再提示)
            if (canRequestPermission("ohos.permission.CAMERA")) {
                //申请相机权限弹框
                requestPermissionsFromUser(new String[]{"ohos.permission.CAMERA"}, 1);
            } else {
                // 显示应用需要权限的理由，提示用户进入设置授权
            }
        } else {
            // 权限已被授予
        }
    }
}
