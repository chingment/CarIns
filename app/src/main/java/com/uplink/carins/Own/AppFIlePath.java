package com.uplink.carins.Own;

import android.os.Environment;

import java.io.File;

/**
 * 项目名称：Pro_carInsurance
 * 类描述：
 * 创建人：tuchg
 * 创建时间：17/1/7 09:32
 */

public class AppFIlePath {


    /**
     * 缓存主目录
     */
    private static String cachePathRootDir = File.separator + "com.uplink.carins" + File.separator;
    /**
     * 默认缓存图片地址.
     */
    private static String fileCacheFullDir;
    /**
     * 默认缓存文件地址.
     */
    private static String imageCacheFullDir;

    public static String getImageCacheFullDir() {
        return imageCacheFullDir;
    }

    public static String getFileCacheFullDir() {
        return fileCacheFullDir;
    }

    static {
        initCacheFullDir();
    }

    /**
     * @return 完成的存储目录
     */
    private static void initCacheFullDir() {
        try {
            if (!isCanUseSD()) {
                return;
            }
            // 初始化图片保存路径
            File fileRoot = Environment.getExternalStorageDirectory();
            File imgdirFile = new File(fileRoot.getAbsolutePath() + cachePathRootDir + "cache_images" + File.separator);
            if (!imgdirFile.exists()) {
                imgdirFile.mkdirs();
            }
            imageCacheFullDir = imgdirFile.getPath();
            File filedirFile = new File(fileRoot.getAbsolutePath() + cachePathRootDir + "cache_files" + File.separator);
            if (!filedirFile.exists()) {
                filedirFile.mkdirs();
            }
            fileCacheFullDir = filedirFile.getPath();
        } catch (Exception e) {
        }
    }

    /**
     * 描述：SD卡是否能用.
     *
     * @return true 可用,false不可用
     */
    public static boolean isCanUseSD() {
        try {
            return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
