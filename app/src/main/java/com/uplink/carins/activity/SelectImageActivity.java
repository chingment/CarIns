package com.uplink.carins.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.uplink.carins.utils.LogUtil;
import com.uplink.carins.utils.StringUtil;

import java.io.File;

public class SelectImageActivity extends Activity {

    public static final String IMAGEURL = "imageUri";
    public static final String EXTRA = "Extra";
    public static final int OPENCAMERA = 1001;
    public static final int OPENALBUM = 1002;

    private Uri imageUri;
    private File outputImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        View view = new View(this);
        view.setBackgroundColor(Color.TRANSPARENT);
        setContentView(view);

        Intent intent = getIntent();
        int extra = intent.getIntExtra(EXTRA, 0);
        init(extra);

    }

    //初始化权限
    private void init(int extra) {
        if (extra == OPENALBUM) {

            if (ContextCompat.checkSelfPermission(SelectImageActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(SelectImageActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
            } else {
                openAlbum();
            }

        } else if (extra == OPENCAMERA) {

            if (ContextCompat.checkSelfPermission(SelectImageActivity.this,
                    Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(SelectImageActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED
                    ) {
                ActivityCompat.requestPermissions(SelectImageActivity.this,
                        new String[]{Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE}, 3);
            } else {
                startCamera();
            }
        }
    }

    //权限没有问题的前提下 打开相机
    private void startCamera() {
        outputImage = new File(getExternalCacheDir(), "output_image.png");
        if (outputImage.exists()) {
            outputImage.delete();
        }

        LogUtil.i("outputImage"+outputImage.getPath());

        ///storage/emulated/0/Android/data/com.yeahis.school/cache/output_image.png
        if (Build.VERSION.SDK_INT >= 24) {
            imageUri = FileProvider.getUriForFile(SelectImageActivity.this,
                    "fileprovider", outputImage);
        } else {
            imageUri = Uri.fromFile(outputImage);
        }
        //启动相机
        Intent intent_photo = new Intent("android.media.action.IMAGE_CAPTURE");
        //设置权限
        intent_photo.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent_photo.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent_photo, 111);
    }

    //   //权限没有问题的前提下 启动相册
    private void openAlbum() {
        Intent intent_album = new Intent("android.intent.action.GET_CONTENT");
        intent_album.setType("image/*");
        startActivityForResult(intent_album, 3);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 111 && resultCode == RESULT_OK) {//相机
            Intent intent_phone = new Intent();
            if (Build.VERSION.SDK_INT >= 24) {
                finishSelectImage(outputImage.toString());
            } else {
                finishSelectImage(imageUri.toString());
            }
        } else if (requestCode == 3 && resultCode == RESULT_OK) {
            if (Build.VERSION.SDK_INT >= 19) {//相册
                //如果是4.4以上系统
                handlerImageonKitkat(data);
            } else {
                handlerImageBeforeKitKat(data);
            }
        } else {
            finish();
        }
    }

    //回调相册数据
    private void handlerImageonKitkat(Intent data) {
        String imagepath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            //如果是document类型的uri，则通过document id处理
            String docid = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docid.split(":")[1];//解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagepath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contenturi = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(docid));
                imagepath = getImagePath(contenturi, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            //如果content类型的uri，则使用普通方式处理
            imagepath = getImagePath(uri);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            //如果是file类型的uri，直接获取图片路径即可
            imagepath = uri.getPath();
        }
        if (!StringUtil.isEmpty(imagepath)) {
            finishSelectImage(imagepath);
        } else {
            imagepath = getPath_above19(SelectImageActivity.this, uri);
            if (!StringUtil.isEmpty(imagepath)) {
                finishSelectImage(imagepath);
            } else {
                Toast.makeText(SelectImageActivity.this, "选择图片失败", Toast.LENGTH_SHORT).show();
            }
        }


    }

    //4.4以前系统，则使用普通方式处理
    private void handlerImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri);
        if (!StringUtil.isEmpty(imagePath)) {
            finishSelectImage(imagePath);
        } else {
            Toast.makeText(SelectImageActivity.this, "选择图片失败", Toast.LENGTH_SHORT).show();

        }
    }

    //返回图片
    private void finishSelectImage(String imagepath) {
        if (!StringUtil.isEmpty(imagepath)) {
            if (imagepath.contains("file://")) {
                imagepath = imagepath.replace("file://", "");
            }
        }
        Log.w("logs", imagepath);

        Intent intent_phone = new Intent();
        intent_phone.putExtra(IMAGEURL, imagepath);
        setResult(110, intent_phone);
        finish();
    }


    /**
     * 19以下sdk获取imagepath
     *
     * @param uri
     * @return
     */
    private String getImagePath(Uri uri) {
        //这里开始的第二部分，获取图片的路径：低版本的是没问题的，但是sdk>19会获取不到
        String[] proj = {MediaStore.Images.Media.DATA};
        //好像是android多媒体数据库的封装接口，具体的看Android文档
        Cursor cursor = getContentResolver().query(uri, proj, null, null, null);
        //获得用户选择的图片的索引值
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        System.out.println("***************" + column_index);
        //将光标移至开头 ，这个很重要，不小心很容易引起越界
        cursor.moveToFirst();
        //最后根据索引值获取图片路径   结果类似：/mnt/sdcard/DCIM/Camera/IMG_20151124_013332.jpg
        String imagePath = cursor.getString(column_index);
        return imagePath;
    }

    /**
     * APIlevel 19以上才有
     * 创建项目时，我们设置了最低版本API Level，比如我的是10，
     * 因此，AS检查我调用的API后，发现版本号不能向低版本兼容，
     * 比如我用的“DocumentsContract.isDocumentUri(context, uri)”是Level 19 以上才有的，
     * 自然超过了10，所以提示错误。
     * 添加    @TargetApi(Build.VERSION_CODES.KITKAT)即可。
     *
     * @param context
     * @param uri
     * @return
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getPath_above19(final Context context, final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
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
     * 19以上版本获取imagepath
     *
     * @param uri
     * @param selection
     * @return
     */
    private String getImagePath(Uri uri, String selection) {
        String path = null;
        if (!StringUtil.isEmpty(selection)) {
            Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                }
                cursor.close();
            }
        }
        return path;
    }

    //判断权限回调
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 2) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openAlbum();
            } else {
                Toast.makeText(SelectImageActivity.this, "您需要打开相册权限", Toast.LENGTH_SHORT).show();

            }
        }
        if (requestCode == 3) {
            // 如果权限被拒绝，grantResults 为空
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera();
            } else {
                Toast.makeText(SelectImageActivity.this, "需要相机和读写文件权限", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
