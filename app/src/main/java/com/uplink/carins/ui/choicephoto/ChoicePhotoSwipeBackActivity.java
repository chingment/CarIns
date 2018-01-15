package com.uplink.carins.ui.choicephoto;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;

import com.uplink.carins.R;
import com.uplink.carins.activity.CropImageActivity;
import com.uplink.carins.Own.AppFIlePath;
import com.uplink.carins.ui.bitmap.AbImageCache;
import com.uplink.carins.ui.bitmap.AbImageUtil;
import com.uplink.carins.ui.dialog.CustomDialog;
import com.uplink.carins.ui.swipebacklayout.SwipeBackActivity;
import com.uplink.carins.utils.AbFileUtil;
import com.uplink.carins.utils.LogUtil;
import com.uplink.carins.utils.StringUtil;

/**
 * 项目名称：Pro_carInsurance
 * 类描述：
 * 创建人：tuchg
 * 创建时间：17/1/7 09:21
 */

public abstract class ChoicePhotoSwipeBackActivity extends SwipeBackActivity implements View.OnClickListener {
    /**
     * 默认是需要裁减的<br>
     * need_crop_use_cropimage 需要裁减使用裁减框<br>
     * need_crop_no_cropimage 需要裁减不使用裁减框<br>
     * no_need_crop 不需要裁减
     */
    public enum CropType {
        need_crop_no_cropimage, need_crop_use_cropimage, no_need_crop
    }

    private CropType type = CropType.need_crop_use_cropimage;

    private String TAG = "BaseForCropActivity";

    public View mDialogView;
    public CustomDialog mChoicePhotoDialog;
    public Button albumButton, camButton, cancelButton;
    /* 用来标识请求照相功能的activity */
    private static final int CAMERA_WITH_DATA = 3023;
    /* 用来标识请求gallery的activity */
    private static final int PHOTO_PICKED_WITH_DATA = 3021;
    /* 用来标识请求裁剪图片后的activity */
    private static final int CAMERA_CROP_DATA = 3022;
    /* 拍照的照片存储位置 */
    private File PHOTO_DIR = null;
    // 照相机拍照得到的图片
    private File mCurrentPhotoFile;
    private String mFileName;
    // 裁减前的图片路径
    private String old_path = "";
    // 裁减后的图片路径
    private String new_path = "";
    // 裁减质量默认95
    private int quality = 65;
    // 裁减宽
    private int width = 500;
    // 裁减高
    private int height = 500;

    // 图片显示View
    private ImageView result_img;

    public LayoutInflater inflater;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);

        inflater = LayoutInflater.from(ChoicePhotoSwipeBackActivity.this);
        // 初始化图片保存路径
        String photo_dir = AppFIlePath.getImageCacheFullDir();
        if (StringUtil.isEmpty(photo_dir)) {
            showToast("存储卡不存在");
        } else {
            PHOTO_DIR = new File(photo_dir);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.choose_album:
                mChoicePhotoDialog.dismiss();
                // 从相册中去获取
                Intent intent = new Intent();
                try {
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
                } catch (ActivityNotFoundException e) {
                    showToast("没有找到照片");
                }
                break;
            case R.id.choose_cam: // 拍照获取
                mChoicePhotoDialog.dismiss();
                String status = Environment.getExternalStorageState();
                // 判断是否有SD卡,如果有sd卡存入sd卡在说，没有sd卡直接转换为图片
                if (status.equals(Environment.MEDIA_MOUNTED)) {
                    doTakePhoto();
                } else {
                    showToast("没有可用的存储卡");
                }
                break;
            case R.id.choose_cancel:
                mChoicePhotoDialog.dismiss();
                break;
            default:
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode != RESULT_OK) {
            LogUtil.d(TAG, "resultCode != RESULT_OK");
            return;
        }
        switch (requestCode) {
            case PHOTO_PICKED_WITH_DATA:
                Uri uri = intent.getData();
                old_path = getImageAbsolutePath(ChoicePhotoSwipeBackActivity.this, uri);
                if (old_path != null) {
                    DoCropChoice(old_path);
                } else {
                    OnCropFail("图片路径出错");
                }
                break;
            case CAMERA_WITH_DATA: // 相机拍照完成；
                try {
                    old_path = mCurrentPhotoFile.getPath();
                    if (old_path != null) {
                        DoCropChoice(old_path);
                    } else {
                        OnCropFail("图片路径出错");
                    }
                } catch (Exception e) {
                    OnCropFail("getPath()出错");
                }
                break;
            case CAMERA_CROP_DATA:
                new_path = intent.getStringExtra("PATH");
                Bitmap bitmap = null;
                LogUtil.d(TAG, "裁剪后得到的图片的路径是 = " + new_path);
                if (!StringUtil.isEmpty(new_path)) {
                    // 从缓存中获取图片，很重要否则会导致页面闪动
                    bitmap = AbImageCache.getBitmapFromCache(new_path);
                    bitmap = AbFileUtil.getBitmapFromSD(new File(new_path));
                    // 缓存中没有则从网络和SD卡获取
                    OnCropSuccess(new_path);
                } else {
                    OnCropFail("裁剪后出错");
                }
                setResulIMG(bitmap);
                break;
            default:
                break;
        }
    }

    private int if_fixed_x = 16;
    private int if_fixed_y = 10;
    private boolean if_fixed = false;

    /**
     * 设置裁减宽度
     */
    public void setIf_fixed_x(int if_fixed_x) {
        this.if_fixed_x = if_fixed_x;
    }

    /**
     * 设置裁减高度
     */
    public void setIf_fixed_y(int if_fixed_y) {
        this.if_fixed_y = if_fixed_y;
    }

    /**
     * 设置是否等比例裁减
     */
    public void setIf_fixed(boolean if_fixed) {
        this.if_fixed = if_fixed;
    }

    private void DoCropChoice(String path) {
        showProgressDialog("请稍后...", false);
        Bitmap bitmap = null;
        if (!StringUtil.isEmpty(old_path)) {
            LogUtil.d(TAG, "将要进行裁剪的图片的路径是 = " + old_path);
            switch (type) {
                case need_crop_use_cropimage:
                    // 裁剪
                    Intent intent1 = new Intent(this, CropImageActivity.class);
                    intent1.putExtra("PATH", path);
                    intent1.putExtra("if_fixed_x", if_fixed_x);
                    intent1.putExtra("if_fixed_y", if_fixed_y);
                    intent1.putExtra("if_fixed", if_fixed);
                    intent1.putExtra("quality", quality);
                    startActivityForResult(intent1, CAMERA_CROP_DATA);
                    break;
                case need_crop_no_cropimage:
                    // 裁剪
                    bitmap = AbFileUtil.getBitmapFromSD(new File(path), AbImageUtil.ORIGINALIMG, width, height);
//				    bitmap = AbFileUtil.getBitmapFromSD(new File(path), AbImageUtil.SCALEIMG, width, height);
                    if (bitmap != null) {
                        String saveToLocal = saveToLocal(bitmap);
                        if (saveToLocal != null) {
                            OnCropSuccess(saveToLocal);
                        } else {
                            OnCropFail("图片裁减出错");
                        }
                    } else {
                        OnCropFail("图片不存在bitmap");
                    }
                    setResulIMG(bitmap);
                    break;
                case no_need_crop:
                    bitmap = AbFileUtil.getBitmapFromSD(new File(path), AbImageUtil.ORIGINALIMG, 500, 500);
//				    bitmap = AbFileUtil.getBitmapFromSD(new File(path), AbImageUtil.SCALEIMG, 500, 500);
                    if (bitmap != null) {
                        // 不裁剪直接返回
                        OnCropSuccess(path);
                    } else {
                        OnCropFail("图片不存在bitmap");
                    }
                    setResulIMG(bitmap);
                    break;
                default:
                    break;
            }
        } else {
            OnCropFail("未在存储卡中找到这个文件");
        }
        removeProgressDialog();
    }

    /**
     * 为resultView 设置背景
     *
     * @param bitmap
     */
    private void setResulIMG(Bitmap bitmap) {
        if (result_img != null) {
            if (bitmap != null) {
                result_img.setImageBitmap(bitmap);
            } else {
                result_img.setImageResource(R.drawable.rect_default_bg);
            }
        }
    }

    /**
     * 从相册得到的url转换为SD卡中图片路径
     */
//	public String getPath(Uri uri) {
//		String path = null;
//		if (uri != null) {
//			if (!StringUtils.isEmpty(uri.getAuthority())) {
//				String[] projection = { MediaStore.Images.Media.DATA };
//				Cursor cursor = managedQuery(uri, projection, null, null, null);
//				int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//				cursor.moveToFirst();
//				path = cursor.getString(column_index);
//			} else {
//				path = uri.getPath();
//			}
//		}
//		if (path == null) {
//			path = uri.getPath();
//		}
//		return path;
//	}

    /**
     * 根据Uri获取图片绝对路径，解决Android4.4以上版本Uri转换
     *
     * @param imageUri
     * @author yaoxing
     * @date 2014-10-12
     */
    public static String getImageAbsolutePath(Activity context, Uri imageUri) {
        if (context == null || imageUri == null)
            return null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, imageUri)) {
            if (isExternalStorageDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(imageUri)) {
                String id = DocumentsContract.getDocumentId(imageUri);
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                String selection = MediaStore.Images.Media._ID + "=?";
                String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } // MediaStore (and general)
        else if ("content".equalsIgnoreCase(imageUri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(imageUri))
                return imageUri.getLastPathSegment();
            return getDataColumn(context, imageUri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(imageUri.getScheme())) {
            return imageUri.getPath();
        }
        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        String column = MediaStore.Images.Media.DATA;
        String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    /**
     * 拍照获取图片
     */
    protected void doTakePhoto() {
        try {
            mFileName = System.currentTimeMillis() + ".jpg";
            mCurrentPhotoFile = new File(PHOTO_DIR, mFileName);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mCurrentPhotoFile));
            startActivityForResult(intent, CAMERA_WITH_DATA);
        } catch (Exception e) {
            showToast("未找到系统相机程序");
        }
    }

    /**
     * 图片选自dialog<br>
     * imageView:裁减后的结果显示 设置裁减type<br>
     * quality 裁剪质量0-100 默认95<br>
     * height width 裁减像素,
     */
    public void showChoiceDialog(ImageView imageView, CropType type, int quality, int width, int height) {
        this.result_img = imageView;
        this.type = type;
        this.quality = quality;
        this.width = width;
        this.height = height;
        showChoiceDialog();
    }

    /**
     * 图片选自dialog<br>
     * imageView:裁减后的结果显示
     */
    public void showChoiceDialog(ImageView imageView) {
        this.result_img = imageView;
        showChoiceDialog();
    }

    /**
     * 图片选自dialog<br>
     * 设置是否裁减
     */
    public void showChoiceDialog(CropType type) {
        this.type = type;
        showChoiceDialog();
    }

    /**
     * 图片选自dialog<br>
     * 设置是否裁减 imageView:裁减后的结果显示
     */
    public void showChoiceDialog(CropType type, ImageView imageView) {
        this.type = type;
        this.result_img = imageView;
        showChoiceDialog();
    }

    /**
     * 图片选自dialog<br>
     * 设置是否裁减 imageView:裁减后的结果显示
     */
    public void showChoiceDialog(CropType type, ImageView imageView, boolean fixed, int fixX, int fixY) {
        this.type = type;
        this.result_img = imageView;
        setIf_fixed(fixed);
        setIf_fixed_x(fixX);
        setIf_fixed_y(fixY);
        showChoiceDialog();
    }

    /**
     * 图片选自dialog<br>
     * 默认直接裁减
     */
    public void showChoiceDialog() {
        // dialogview
        if (mDialogView == null) {
            mDialogView = inflater.inflate(R.layout.dialog_choose_photo, null);
            // view头像选择 方式
            albumButton = (Button) mDialogView.findViewById(R.id.choose_album);
            albumButton.setOnClickListener(this);
            camButton = (Button) mDialogView.findViewById(R.id.choose_cam);
            camButton.setOnClickListener(this);
            cancelButton = (Button) mDialogView.findViewById(R.id.choose_cancel);
            cancelButton.setOnClickListener(this);
        }
        if (mChoicePhotoDialog == null) {
            mChoicePhotoDialog = new CustomDialog(ChoicePhotoSwipeBackActivity.this, R.style.dialog_style, mDialogView);
            mChoicePhotoDialog.setCancelable(true);
        }
        mChoicePhotoDialog.show();
    }

    public String saveToLocal(Bitmap bitmap) {
        // 需要裁剪后保存为新图片
        String mFileName = System.currentTimeMillis() + ".jpg";
        String path = PHOTO_DIR + File.separator + mFileName;
        try {
            LogUtil.d("tupandaxiao=" + getBitmapsize(bitmap));
            // if (getBitmapsize(bitmap) > ) {
            //
            // }
            FileOutputStream fos = new FileOutputStream(path);
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return path;
    }

    @SuppressLint("NewApi")
    public long getBitmapsize(Bitmap bitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
            return bitmap.getByteCount();
        }
        // Pre HC-MR1
        return bitmap.getRowBytes() * bitmap.getHeight();
    }

    public abstract void OnCropSuccess(String photo_path);

    public abstract void OnCropFail(String error_tx);
}
