package com.uplink.carins.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import java.io.File;
import java.io.FileOutputStream;

import com.uplink.carins.R;
import com.uplink.carins.Own.AppFIlePath;
import com.uplink.carins.Own.Config;
import com.uplink.carins.ui.BaseFragmentActivity;
import com.uplink.carins.ui.bitmap.AbImageUtil;
import com.uplink.carins.ui.crop.CropImageView;
import com.uplink.carins.utils.AbFileUtil;
import com.uplink.carins.utils.LogUtil;

/**
 * 裁剪界面
 */
public class CropImageActivity extends BaseFragmentActivity implements View.OnClickListener {
    private static final String TAG = "ActiveCropImageActivity";
    /**
     * The file local.
     */
    public File FILE_LOCAL = null;
    // Static final constants
    private static final int DEFAULT_ASPECT_RATIO_VALUES = 10;// 默认宽高比值
    // private static final int ROTATE_NINETY_DEGREES = 90;
    private static final String ASPECT_RATIO_X = "ASPECT_RATIO_X";
    private static final String ASPECT_RATIO_Y = "ASPECT_RATIO_Y";
    // Instance variables
    private int mAspectRatioX = DEFAULT_ASPECT_RATIO_VALUES;
    private int mAspectRatioY = DEFAULT_ASPECT_RATIO_VALUES;

    private CropImageView mImageView;
    private Bitmap mBitmap;

    private Button mSave;
    private Button mCancel, rotateLeft, rotateRight;
    private String mPath = "CropImageActivity";
    // private int width = 500;
    // private int height = 500;
    private int quality = 95;
    private int if_fixed_x = 16;
    private int if_fixed_y = 10;

    private boolean if_fixed = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_image);
        init();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mBitmap != null) {
            mBitmap = null;
        }
    }

    // Saves the state upon rotating the screen/restarting the activity
    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putInt(ASPECT_RATIO_X, mAspectRatioX);
        bundle.putInt(ASPECT_RATIO_Y, mAspectRatioY);
    }

    // Restores the state upon rotating the screen/restarting the activity
    @Override
    protected void onRestoreInstanceState(Bundle bundle) {
        super.onRestoreInstanceState(bundle);
        mAspectRatioX = bundle.getInt(ASPECT_RATIO_X);
        mAspectRatioY = bundle.getInt(ASPECT_RATIO_Y);
    }

    private void init() {
        FILE_LOCAL = new File(AppFIlePath.getImageCacheFullDir());
        if (!FILE_LOCAL.exists()) {
            FILE_LOCAL.mkdirs();
        }

        mPath = getIntent().getStringExtra("PATH");
        if_fixed = getIntent().getBooleanExtra("if_fixed", false);
        if_fixed_x = getIntent().getIntExtra("if_fixed_x", 16);
        if_fixed_y = getIntent().getIntExtra("if_fixed_y", 10);

        // intent1.putExtra("width", width);
        // intent1.putExtra("height", height);
        // intent1.putExtra("quality", quality);
        // width = getIntent().getIntExtra("width", 500);
        // height = getIntent().getIntExtra("height", 500);
        quality = getIntent().getIntExtra("quality", 95);

        LogUtil.d(TAG, "将要进行裁剪的图片的路径是 = " + mPath);
        mImageView = (CropImageView) findViewById(R.id.crop_image);

        mImageView.setFixedAspectRatio(if_fixed);// 设置 是否 固定的长宽比 比例
        mImageView.setAspectRatio(if_fixed_x, if_fixed_y);// 设置纵横比

        mSave = (Button) this.findViewById(R.id.okBtn);
        mCancel = (Button) this.findViewById(R.id.cancelBtn);
        rotateLeft = (Button) this.findViewById(R.id.rotateLeft);
        rotateRight = (Button) this.findViewById(R.id.rotateRight);
        mSave.setOnClickListener(this);
        mCancel.setOnClickListener(this);
        rotateLeft.setOnClickListener(this);
        rotateRight.setOnClickListener(this);
        // 相册中原来的图片
        File mFile = new File(mPath);
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight() - 100;

        try {
            mBitmap = AbFileUtil.getBitmapFromSD2(mFile, AbImageUtil.SCALEIMG, width, height);
            if (mBitmap == null) {
                showToast("没有找到图片");
                finish();
            } else {
                resetImageView(mBitmap);
            }
        } catch (Exception e) {
            showToast("没有找到图片");
            finish();
        }
    }

    private void resetImageView(Bitmap b) {
        mImageView.setImageBitmap(b);
    }

    public String saveToLocal(Bitmap bitmap) {
        // 需要裁剪后保存为新图片
        String mFileName = System.currentTimeMillis() + ".jpg";
        String path = FILE_LOCAL + File.separator + mFileName;
        try {
            FileOutputStream fos = new FileOutputStream(path);
            bitmap.compress(CompressFormat.JPEG, quality, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return path;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancelBtn:
                finish();
                break;
            case R.id.okBtn:
                try {
                    String path = saveToLocal(mImageView.getCroppedImage());
                    LogUtil.d(TAG, "裁剪后图片的路径是 = " + path);
                    Intent intent = new Intent();
                    intent.putExtra("PATH", path);
                    setResult(RESULT_OK, intent);
                } catch (Exception e) {
                    if (Config.showDebug)
                        e.printStackTrace();
                    showToast("裁减出错");
                }
                finish();
                break;
            case R.id.rotateLeft:
                mImageView.rotateImage(270);
                break;
            case R.id.rotateRight:
                mImageView.rotateImage(90);
                break;

        }
    }

}
