package com.example.ordersystemapplication.slice;

import com.example.ordersystemapplication.data.Result;
import com.example.ordersystemapplication.domain.Customer;
import com.example.ordersystemapplication.domain.Order;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.example.ordersystemapplication.ResourceTable;
import com.zzrv5.mylibrary.ZZRCallBack;
import com.zzrv5.mylibrary.ZZRHttp;
import net.sourceforge.zbar.*;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Text;
import ohos.agp.components.surfaceprovider.SurfaceProvider;
import ohos.agp.graphics.Surface;
import ohos.agp.graphics.SurfaceOps;
import ohos.agp.window.dialog.ToastDialog;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.media.camera.CameraKit;
import ohos.media.camera.device.Camera;
import ohos.media.camera.device.CameraConfig;
import ohos.media.camera.device.CameraStateCallback;
import ohos.media.camera.device.FrameConfig;
import ohos.media.common.BufferInfo;
import ohos.media.image.ImageReceiver;
import ohos.media.image.common.ImageFormat;

import java.nio.ByteBuffer;

import static ohos.media.camera.device.Camera.FrameConfigType.FRAME_CONFIG_PREVIEW;

public class SaomiaoSlice extends AbilitySlice{
    private ImageScanner scanner;
    private ImageReceiver imageReceiver;
    private CameraKit cameraKit;
    private Surface previewSurface;
    private Surface dataSurface;
    private Camera mcamera;
    private Text scanText;
    private EventHandler handler;
    SurfaceProvider surfaceProvider;
    public static final int VIDEO_WIDTH = 640;
    public static final int VIDEO_HEIGHT = 480;
    String tableId;
    private Customer customer;
    private Gson gson = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd").create();
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_zbar);

        customer = intent.getSerializableParam("my");

        handler = new EventHandler(EventRunner.getMainEventRunner());
        scanner = new ImageScanner();
        scanner.setConfig(0, Config.X_DENSITY, 3);
        scanner.setConfig(0, Config.Y_DENSITY, 3);
        //初始化UI和相机，实现视频帧的获取

        surfaceProvider = (SurfaceProvider) findComponentById(ResourceTable.Id_zbar_surfaceprovider);
        surfaceProvider.getSurfaceOps().get().addCallback(new SurfaceOps.Callback() {
            @Override
            public void surfaceCreated(SurfaceOps surfaceOps) {
                previewSurface = surfaceOps.getSurface();
                openCamera();
            }
            @Override
            public void surfaceChanged(SurfaceOps surfaceOps, int i, int i1, int i2) {
            }

            @Override
            public void surfaceDestroyed(SurfaceOps surfaceOps) {
            }
        });
        surfaceProvider.pinToZTop(true);

        scanText =(Text) findComponentById(ResourceTable.Id_zbar_text);
        //注册编码器，实现视频帧的编码
        imageReceiver = ImageReceiver.create(VIDEO_WIDTH, VIDEO_HEIGHT, ImageFormat.YUV420_888, 10);
        imageReceiver.setImageArrivalListener( new IImageArrivalListenerImpl());
    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private class IImageArrivalListenerImpl implements ImageReceiver.IImageArrivalListener {
        //对监听事件的响应逻辑，实现对图像数据处理和到编码器的传输
        @Override
        public void onImageArrival(ImageReceiver imageReceiver) {

            ohos.media.image.Image mImage = imageReceiver.readNextImage();
            if (mImage != null) {
                BufferInfo bufferInfo = new BufferInfo();
                ByteBuffer mBuffer;
                byte[] YUV_DATA = new byte[VIDEO_HEIGHT * VIDEO_WIDTH * 3 / 2];
                int i;
                //采集YUV格式数据
                mBuffer = mImage.getComponent(ImageFormat.ComponentType.YUV_Y).getBuffer();
                for (i = 0; i < VIDEO_WIDTH * VIDEO_HEIGHT; i++) {
                    YUV_DATA[i] = mBuffer.get(i);
                }
                mBuffer = mImage.getComponent(ImageFormat.ComponentType.YUV_V).getBuffer();
                for (i = 0; i < VIDEO_WIDTH * VIDEO_HEIGHT / 4; i++) {
                    YUV_DATA[(VIDEO_WIDTH * VIDEO_HEIGHT) + i * 2] =
                            mBuffer.get(i * 2);
                }
                mBuffer = mImage.getComponent(ImageFormat.ComponentType.YUV_U).getBuffer();
                for (i = 0; i < VIDEO_WIDTH * VIDEO_HEIGHT / 4; i++) {
                    YUV_DATA[(VIDEO_WIDTH * VIDEO_HEIGHT) + i * 2 + 1] = mBuffer.get(i * 2);
                }
                bufferInfo.setInfo(0, VIDEO_WIDTH * VIDEO_HEIGHT * 3 / 2, mImage.getTimestamp(), 0);
                Image barcode = new Image(mImage.getImageSize().width, mImage.getImageSize().height, "Y800");
                barcode.setData(YUV_DATA);


                if (scanner.scanImage(barcode) != 0 && tableId==null) {
                    for (Symbol sym1 : scanner.getResults()) {
                        if(sym1!=null){
                            tableId=sym1.getData();
                        }
                    }
                    handler.postTask(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println(tableId);
                            Order order = new Order();
                            order.setCustPhone(customer.getCustPhone());
                            order.setTableId(tableId);
                            String json = gson.toJson(order);
                            System.out.println("扫码json"+json);
                            ZZRHttp.postJson("http://101.132.74.147:8082/order/takeOrder", json, new ZZRCallBack.CallBackString() {
                                @Override
                                public void onFailure(int i, String s) {
                                    new ToastDialog(SaomiaoSlice.this)
                                            .setText("网络不稳定")
                                            .show();
                                }
                                @Override
                                public void onResponse(String s) {
                                    Result result1 = gson.fromJson(s, Result.class);
                                    String json = gson.toJson(result1.getResult());
                                    Order order1;
                                    order1 = gson.fromJson(json,Order.class);
                                    Intent intent = new Intent();
                                    intent.setParam("Order",order1);
                                    intent.setParam("Customer",customer);
                                    present(new OrderFoodAbilitySlice(), intent);
                                }
                            });

                        }
                    });
                }
                mImage.release();
                return;
            }
        }
    }
    private void openCamera(){
        // 获取 CameraKit 对象
        cameraKit = CameraKit.getInstance(this);
        if (cameraKit == null) {
            return;
        }
        try {
            // 获取当前设备的逻辑相机列表cameraIds
            String[] cameraIds = cameraKit.getCameraIds();
            // 创建相机！
            cameraKit.createCamera(cameraIds[0], new CameraStateCallbackImpl(), new EventHandler(EventRunner.create("CameraCb")));
        } catch (IllegalStateException e) {
            System.out.println("getCameraIds fail");
        }
    }


    private final class CameraStateCallbackImpl extends CameraStateCallback {
        //相机回调
        @Override
        public void onCreated(Camera camera) {
            mcamera = camera;
            //相机创建时回调
            CameraConfig.Builder cameraConfigBuilder = camera.getCameraConfigBuilder();
            if (cameraConfigBuilder == null) { return; }
            // 配置预览的 Surface
            cameraConfigBuilder.addSurface(previewSurface);
            // 配置拍照的 Surface
            dataSurface = imageReceiver.getRecevingSurface();
            cameraConfigBuilder.addSurface(dataSurface);
            try {
                // 相机设备配置
                camera.configure(cameraConfigBuilder.build());
            } catch (IllegalArgumentException e) {
                System.out.println("Argument Exception");
            } catch (IllegalStateException e) {
                System.out.println("State Exception");
            }
        }
        @Override
        public void onConfigured(Camera camera) {
            FrameConfig.Builder frameConfigBuilder = mcamera.getFrameConfigBuilder(FRAME_CONFIG_PREVIEW);
            // 配置预览 Surface
            frameConfigBuilder.addSurface(previewSurface);
            // 配置拍照的 Surface
            frameConfigBuilder.addSurface(dataSurface);
            try {
                // 启动循环帧捕获
                mcamera.triggerLoopingCapture(frameConfigBuilder.build());
            } catch (IllegalArgumentException e) {
                System.out.println("Argument Exception");
            } catch (IllegalStateException e) {
                System.out.println("State Exception");
            }
            //相机配置
        }
        @Override
        public void onReleased(Camera camera) {
            // 释放相机设备
            if (mcamera != null) {
                mcamera.stopLoopingCapture();
                mcamera.release();
                mcamera = null;
            }
        }
    }

    @Override
    protected void onBackground() {
        if (mcamera != null) {
            mcamera.stopLoopingCapture();
            mcamera.release();
            mcamera = null;
        }
        surfaceProvider.removeFromWindow();
        SaomiaoSlice.super.terminate();
    }

    @Override
    public void onForeground(Intent intent) {
        if (mcamera != null) {
            mcamera.stopLoopingCapture();
            mcamera.release();
            mcamera = null;
        }
        SaomiaoSlice.super.terminate();
    }
}
