package com.uplink.carins.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nld.cloudpos.aidl.AidlDeviceService;
import com.nld.cloudpos.aidl.printer.AidlPrinter;
import com.nld.cloudpos.aidl.printer.AidlPrinterListener;
import com.nld.cloudpos.aidl.printer.PrintItemObj;
import com.nld.cloudpos.data.PrinterConstant;
import com.uplink.carins.R;
import com.uplink.carins.ui.swipebacklayout.SwipeBackActivity;
import com.uplink.carins.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

public class PrintDemoActivity extends FragmentActivity implements View.OnClickListener {

    private ImageView btnHeaderGoBack;
    private TextView txtHeaderTitle;
    Button btConnect;
    Button btPrinter;
    Button btPrintText;

    Button btCleanMsg;
    TextView textView;
    String log = "";

    AidlDeviceService aidlDeviceService = null;

    AidlPrinter aidlPrinter = null;
    private String TAG = "lyc";

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "bind device service");
            aidlDeviceService = AidlDeviceService.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "unbind device service");
            aidlDeviceService = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_printdemo);
        initView();
    }

    /**
     * 控件初始化
     */
    private void initView() {

        btnHeaderGoBack = (ImageView) findViewById(R.id.btn_main_header_goback);
        btnHeaderGoBack.setVisibility(View.VISIBLE);
        btnHeaderGoBack.setOnClickListener(this);
        txtHeaderTitle = (TextView) findViewById(R.id.txt_main_header_title);
        txtHeaderTitle.setText("支付信息");

        btPrintText = (Button) findViewById(R.id.btPrintText);
        btPrintText.setOnClickListener(this);

        btPrinter = (Button) findViewById(R.id.btPrinter);
        btPrinter.setOnClickListener(this);

        btConnect = (Button) findViewById(R.id.btConnect);
        btConnect.setOnClickListener(this);

        btCleanMsg = (Button) findViewById(R.id.btCleanMsg);
        btCleanMsg.setOnClickListener(this);

        textView = (TextView) findViewById(R.id.textView);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_main_header_goback:
                finish();
                break;
            case R.id.btPrintText:
                printText();
                break;
            case R.id.btPrinter:
                getPrinter();
                break;
            case R.id.btConnect:
                bindServiceConnection();
                break;
            default:
                Toast.makeText(PrintDemoActivity.this, "该控件无响应事件", Toast.LENGTH_LONG).show();
                break;
        }
    }

    /**
     * 打印文本
     *
     * @throws RemoteException
     */
    private void printText() {
        try {

            if (null != aidlPrinter) {
                //文本内容
                final List<PrintItemObj> data = new ArrayList<PrintItemObj>();
                data.add(new PrintItemObj("文本打印测试Test 字号5  非粗体1", PrinterConstant.FontScale.FONTSCALE_W_H, PrinterConstant.FontType.FONTTYPE_N, PrintItemObj.ALIGN.CENTER, false, 6));
                data.add(new PrintItemObj("文本打印测试Test 字号5  非粗体2", PrinterConstant.FontScale.FONTSCALE_DW_DH, PrinterConstant.FontType.FONTTYPE_N, PrintItemObj.ALIGN.CENTER, false, 6));
                data.add(new PrintItemObj("文本打印测试Test 字号5  非粗体3", PrinterConstant.FontScale.FONTSCALE_W_DH, PrinterConstant.FontType.FONTTYPE_N, PrintItemObj.ALIGN.CENTER, false, 6));
                data.add(new PrintItemObj("文本打印测试Test 字号5  非粗体4", PrinterConstant.FontScale.FONTSCALE_DW_H, PrinterConstant.FontType.FONTTYPE_N, PrintItemObj.ALIGN.CENTER, false, 6));
                data.add(new PrintItemObj("文本打印测试Test 字号5  非粗体5", PrinterConstant.FontScale.FONTSCALE_W_H, PrinterConstant.FontType.FONTTYPE_S, PrintItemObj.ALIGN.CENTER, false, 6));
                data.add(new PrintItemObj("文本打印测试Test 字号5  非粗体6", PrinterConstant.FontScale.FONTSCALE_DW_DH, PrinterConstant.FontType.FONTTYPE_S, PrintItemObj.ALIGN.CENTER, false, 6));
                data.add(new PrintItemObj("文本打印测试Test 字号5  非粗体7", PrinterConstant.FontScale.FONTSCALE_W_DH, PrinterConstant.FontType.FONTTYPE_S, PrintItemObj.ALIGN.CENTER, false, 6));
                data.add(new PrintItemObj("文本打印测试Test 字号5  非粗体8", PrinterConstant.FontScale.FONTSCALE_DW_H, PrinterConstant.FontType.FONTTYPE_S, PrintItemObj.ALIGN.CENTER, false, 6));
                data.add(new PrintItemObj("文本打印测试Test 字号5  非粗体9", PrinterConstant.FontScale.FONTSCALE_DW_H, PrinterConstant.FontType.FONTTYPE_S, PrintItemObj.ALIGN.CENTER, true, 6));
                data.add(new PrintItemObj("\r"));
                data.add(new PrintItemObj("\r"));
                data.add(new PrintItemObj("-------------------------------"));


                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (aidlPrinter != null) {
                            try {
                                aidlPrinter.open();
                                //打印文本
                                aidlPrinter.printText(data);
                                //打印图片
                                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
                                aidlPrinter.printImage(PrinterConstant.Align.ALIGN_CENTER, bitmap);
                                aidlPrinter.printQrCode(PrinterConstant.Align.ALIGN_RIGHT, 100, "12345");
                                aidlPrinter.printBarCode(PrinterConstant.Align.ALIGN_CENTER, 4, 64, "12345");

                                aidlPrinter.start(new AidlPrinterListener.Stub() {

                                    @Override
                                    public void onPrintFinish() throws RemoteException {
                                        Log.e(TAG, "打印结束");
                                        /**如果出现纸撕下部分有未输出的内容释放下面代码**/
                                        aidlPrinter.paperSkip(2);
                                        showMsgOnTextView("打印结束");
                                    }

                                    @Override
                                    public void onError(int errorCode) throws RemoteException {
                                        Log.e(TAG, "打印异常");
                                        showMsgOnTextView("打印异常码:" + errorCode);
                                    }
                                });

                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();

                aidlPrinter.printText(data);
            } else {
                showMsgOnTextView("请检查打印数据data和打印机状况");
            }

        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }


    /**
     * 初始化打印设备
     * @throws RemoteException
     */
    public void getPrinter() {
        Log.i(TAG, "获取打印机设备实例...");
        try {
            aidlPrinter = AidlPrinter.Stub.asInterface(aidlDeviceService.getPrinter());
            showMsgOnTextView("初始化打印机实例");
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    /**
     * 绑定服务
     */
    public void bindServiceConnection() {
        bindService(new Intent("nld_cloudpos_device_service"), serviceConnection,
                Context.BIND_AUTO_CREATE);
        showMsgOnTextView("服务绑定");
    }

    /**
     * 在textview显示消息
     *
     * @param string
     */
    public void showMsgOnTextView(String string) {
        log = string + "\n" + log;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.setText(log);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
        aidlPrinter = null;
    }
}
