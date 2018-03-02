package com.uplink.carins.device;

import android.graphics.Bitmap;
import android.os.Handler;

import com.newland.me.ConnUtils;
import com.newland.me.DeviceManager;
import com.newland.mtype.ConnectionCloseEvent;
import com.newland.mtype.Device;
import com.newland.mtype.ExModuleType;
import com.newland.mtype.ModuleType;
import com.newland.mtype.event.DeviceEventListener;
import com.newland.mtype.module.common.cardreader.K21CardReader;
import com.newland.mtype.module.common.emv.EmvModule;
import com.newland.mtype.module.common.iccard.ICCardModule;
import com.newland.mtype.module.common.light.IndicatorLight;
import com.newland.mtype.module.common.pin.K21Pininput;
import com.newland.mtype.module.common.printer.PrintContext;
import com.newland.mtype.module.common.printer.Printer;
import com.newland.mtype.module.common.printer.PrinterResult;
import com.newland.mtype.module.common.printer.PrinterStatus;
import com.newland.mtype.module.common.rfcard.K21RFCardModule;
import com.newland.mtype.module.common.scanner.BarcodeScanner;
import com.newland.mtype.module.common.scanner.BarcodeScannerManager;
import com.newland.mtype.module.common.security.K21SecurityModule;
import com.newland.mtype.module.common.serialport.SerialModule;
import com.newland.mtype.module.common.sm.SmModule;
import com.newland.mtype.module.common.storage.Storage;
import com.newland.mtype.module.common.swiper.K21Swiper;
import com.newland.mtypex.nseries.NSConnV100ConnParams;
import com.newland.mtypex.nseries3.NS3ConnParams;
import com.uplink.carins.ui.BaseFragmentActivity;
import com.uplink.carins.utils.LogUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

//import com.newland.mtype.ExModuleType;
//import com.newland.mtype.module.common.serialport.SerialModule;6

public class N900Device extends AbstractDevice {
    private static final String K21_DRIVER_NAME = "com.newland.me.K21Driver";
//	private static final String K21_DRIVER_NAME = "com.newland.me.MT90Driver";

    private static BaseFragmentActivity baseActivity;
    private static N900Device n900Device = null;
    private static DeviceManager deviceManager;

    private N900Device(BaseFragmentActivity baseactivity) {
        N900Device.baseActivity = baseactivity;
    }

    public static N900Device getInstance(BaseFragmentActivity baseactivity) {
        if (n900Device == null) {
            synchronized (N900Device.class) {
                if (n900Device == null) {
                    n900Device = new N900Device(baseactivity);
                }
            }
        }
        N900Device.baseActivity = baseactivity;
        return n900Device;
    }

    @Override
    public void connectDevice() {

        LogUtil.i("设备连接中..");
        try {
            deviceManager = ConnUtils.getDeviceManager();
            deviceManager.init(baseActivity, K21_DRIVER_NAME, new NSConnV100ConnParams(), new DeviceEventListener<ConnectionCloseEvent>() {
                @Override
                public void onEvent(ConnectionCloseEvent event, Handler handler) {
                    if (event.isSuccess()) {
                        LogUtil.i("设备被客户主动断开！");
                    }
                    if (event.isFailed()) {
                        LogUtil.e("设备链接异常断开！");
                    }
                }

                @Override
                public Handler getUIHandler() {
                    return null;
                }
            });
            LogUtil.i("N900设备控制器已初始化!");
            deviceManager.connect();

            deviceManager.getDevice().setBundle(new NS3ConnParams());
            LogUtil.i("设备连接成功.");
            //baseActivity.btnStateToWaitingConn();


        } catch (Exception e1) {
            e1.printStackTrace();
            LogUtil.e("链接异常,请检查设备或重新连接...");
        }
    }

    @Override
    public void disconnect() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (deviceManager != null) {
                        deviceManager.disconnect();
                        deviceManager = null;
                        LogUtil.i("设备断开成功...");
                        //baseActivity.btnStateToWaitingInit();
                    }
                } catch (Exception e) {
                    LogUtil.e("设备断开异常:");
                }
            }
        }).start();
    }

    @Override
    public boolean isDeviceAlive() {
        boolean ifConnected = (deviceManager == null ? false : deviceManager.getDevice().isAlive());
        return ifConnected;
    }

    @Override
    public K21CardReader getCardReaderModuleType() {
        K21CardReader cardReader = (K21CardReader) deviceManager.getDevice().getStandardModule(ModuleType.COMMON_CARDREADER);
        return cardReader;
    }

    @Override
    public EmvModule getEmvModuleType() {
        EmvModule emvModule = (EmvModule) deviceManager.getDevice().getExModule("EMV_INNERLEVEL2");
        return emvModule;
    }

    @Override
    public ICCardModule getICCardModule() {
        ICCardModule iCCardModule = (ICCardModule) deviceManager.getDevice().getStandardModule(ModuleType.COMMON_ICCARDREADER);
        return iCCardModule;
    }

    @Override
    public IndicatorLight getIndicatorLight() {
        IndicatorLight indicatorLight = (IndicatorLight) deviceManager.getDevice().getStandardModule(ModuleType.COMMON_INDICATOR_LIGHT);
        return indicatorLight;
    }

    @Override
    public K21Pininput getK21Pininput() {
        K21Pininput k21Pininput = (K21Pininput) deviceManager.getDevice().getStandardModule(ModuleType.COMMON_PININPUT);
        return k21Pininput;
    }

    @Override
    public Printer getPrinter() {

        if (deviceManager == null) {
            LogUtil.e("deviceManager，deviceManager，deviceManager，deviceManager为空");
            return null;
        }

        Device device = deviceManager.getDevice();

        //todo 已经知道这里为空
        if (device == null) {
            LogUtil.e("device，device，device，device为空");
            return null;
        }

        Printer printer = (Printer) device.getStandardModule(ModuleType.COMMON_PRINTER);

        printer.init();

        return printer;

    }

    @Override
    public K21RFCardModule getRFCardModule() {
        K21RFCardModule rFCardModule = (K21RFCardModule) deviceManager.getDevice().getStandardModule(ModuleType.COMMON_RFCARDREADER);
        return rFCardModule;
    }

    @Override
    public BarcodeScanner getBarcodeScanner() {
        BarcodeScannerManager barcodeScannerManager = (BarcodeScannerManager) deviceManager.getDevice().getStandardModule(ModuleType.COMMON_BARCODESCANNER);
        BarcodeScanner scanner = barcodeScannerManager.getDefault();
        return scanner;
    }

    @Override
    public K21SecurityModule getSecurityModule() {
        K21SecurityModule securityModule = (K21SecurityModule) deviceManager.getDevice().getStandardModule(ModuleType.COMMON_SECURITY);
        return securityModule;
    }

    @Override
    public Storage getStorage() {
        Storage storage = (Storage) deviceManager.getDevice().getStandardModule(ModuleType.COMMON_STORAGE);
        return storage;
    }

    @Override
    public K21Swiper getK21Swiper() {
        K21Swiper k21Swiper = (K21Swiper) deviceManager.getDevice().getStandardModule(ModuleType.COMMON_SWIPER);
        return k21Swiper;
    }

    @Override
    public Device getDevice() {
        return deviceManager.getDevice();
    }

    @Override
    public SerialModule getUsbSerial() {
        SerialModule k21Serial = (SerialModule) deviceManager.getDevice().getExModule(ExModuleType.USBSERIAL);
        return k21Serial;
    }

    @Override
    public SmModule getSmModule() {
        SmModule smModule = (SmModule) deviceManager.getDevice().getStandardModule(ModuleType.COMMON_SM);
        return smModule;
    }


}
