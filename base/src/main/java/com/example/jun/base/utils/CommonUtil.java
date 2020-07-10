package com.example.jun.base.utils;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;

import androidx.core.content.FileProvider;
import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.View;
import android.webkit.WebView;


import com.example.jun.base.MyApplication;
import com.example.jun.base.R;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.FileChannel;

/**
 * @className: CommonUtil
 * @classDescription: 通用工具类
 */
public class CommonUtil {

    public static String dictionaryName = "hsjskj";


    //判断有没有SD卡
    public static boolean hasSDCard() {
        String status = Environment.getExternalStorageState();
        if (!status.equals(Environment.MEDIA_MOUNTED)) {
            return false;
        }
        return true;
    }

    //俩个路径一个手机路径一个是SD卡路径
    public static String getRootFilePath() {
        File rootDir;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            File externalFilesDir = MyApplication.getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            if (hasSDCard() && externalFilesDir != null) {
                rootDir = externalFilesDir;
            } else {
                rootDir = MyApplication.getContext().getCacheDir();
            }
        } else {
            if (hasSDCard()) {
                rootDir = Environment.getExternalStorageDirectory();
            } else {
                rootDir = MyApplication.getContext().getCacheDir();
            }
        }
        return rootDir.getAbsolutePath() + "/";
//        if (hasSDCard()) {
//            return Environment.getExternalStorageDirectory().getAbsolutePath() + "/";// filePath:/sdcard/
//        } else {
//            return Environment.getDataDirectory().getAbsolutePath() + "/data/"; // filePath: /data/data/
//        }
    }

    //在现有的路径上新建自己要的文件夹名称
    public static String getSaveFilePath() {
        String filePath = CommonUtil.getRootFilePath() + dictionaryName;
        File dirs = new File(filePath);
        if (!dirs.exists())
            dirs.mkdirs();
        return filePath;
    }

    //判断公有目录文件是否存在，Android Q开始
    public static boolean isAndroidQFileExists(Context context, String path) {
        AssetFileDescriptor afd = null;
        ContentResolver cr = context.getContentResolver();
        try {
            Uri uri = Uri.parse(path);
            afd = cr.openAssetFileDescriptor(uri, "r");
            if (afd == null) {
                return false;
            } else {
                close(afd);
            }
        } catch (FileNotFoundException e) {
            return false;
        } finally {
            close(afd);
        }
        return true;
    }

    public static void close(AssetFileDescriptor is) {
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //扫描系统相册、视频等，图片、视频选择器
    public static String openMediaStoreQ(Context context) {
        final String[] IMAGE_PROJECTION = {
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.BUCKET_ID,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME};

        Cursor imageCursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                IMAGE_PROJECTION, null, null, IMAGE_PROJECTION[4] + " DESC");

        String path = imageCursor.getString(imageCursor.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
        String name = imageCursor.getString(imageCursor.getColumnIndexOrThrow(IMAGE_PROJECTION[1]));
        int id = imageCursor.getInt(imageCursor.getColumnIndexOrThrow(IMAGE_PROJECTION[2]));
        String folderPath = imageCursor.getString(imageCursor.getColumnIndexOrThrow(IMAGE_PROJECTION[3]));
        String folderName = imageCursor.getString(imageCursor.getColumnIndexOrThrow(IMAGE_PROJECTION[4]));

//Android Q 公有目录只能通过Content Uri + id的方式访问，以前的File路径全部无效，如果是Video，记得换成MediaStore.Videos
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            path = MediaStore.Images.Media
                    .EXTERNAL_CONTENT_URI
                    .buildUpon()
                    .appendPath(String.valueOf(id)).build().toString();
        }
        return path;
    }

    /**
     * 检查文件是否存在
     */
    private static String checkDirPath(String dirPath) {
        if (TextUtils.isEmpty(dirPath)) {
            return "";
        }
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dirPath;
    }


    public static String bitmap2File(Bitmap bitmap) {
        return bitmap2File(bitmap, String.valueOf(System.currentTimeMillis()));
    }

    public static String bitmap2File(Bitmap bitmap, String fileName) {

        String rootFilePath = CommonUtil.getSaveFilePath() + "/";

        File f = new File(rootFilePath + fileName + ".jpg");

        if (f.exists()) f.delete();
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.flush();
            fOut.close();
        } catch (IOException e) {
            return null;
        }
        return f.getAbsolutePath();
    }

    public static String compressPicReturnNewPath(String img_path) {

        Bitmap bitmap;
        FileOutputStream b = null;

        String newPath = checkDirPath(getSaveFilePath() + "/") + System.currentTimeMillis() + ".jpg";
        File file = new File(newPath);

        try {
            BitmapFactory.Options op = new BitmapFactory.Options();
            // 防止内存溢出
            op.inSampleSize = 1; // 这个数字越大,图片大小越小.
            bitmap = BitmapFactory.decodeFile(img_path, op);

            //压缩内存 中的 bitmap  压缩完成后 写入文件（硬盘）
            while (bitmap.getWidth() > 2560 || bitmap.getHeight() > 2560) {
                op.inSampleSize += 2;
                bitmap = BitmapFactory.decodeFile(img_path, op);
            }

            b = new FileOutputStream(file);

            if (bitmap != null) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b); // 不压缩质量   压缩质量反而耗费资源 使文件变大
            }
            b.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return newPath;
    }

    //获取指定尺寸大小的本地drawable
    public static Bitmap compressBitmap(Resources res, int id, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, id, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, id, options);
    }


    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // 原始图片尺寸
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            // 计算出实际宽高和目标宽高的比率
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            // 选择宽和高比率中最小的作为inSampleSize的值，这样可以保证最终生成图片的宽和高
            // 一定都会大于等于目标的宽和高。
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }

    /**
     * 文件 转 base64 字符串
     */
    public static String FileToBase64File(String path) throws Exception {
        File file = new File(path);
        FileInputStream inputFile = new FileInputStream(file);
        byte[] buffer = new byte[(int) file.length()];
        inputFile.read(buffer);
        inputFile.close();
        return Base64.encodeToString(buffer, Base64.DEFAULT);
    }

    /**
     * 文件 转 base64 字符串
     */
    public static StringBuffer file2base64(File file) {

        StringBuffer buffer = new StringBuffer();
        buffer.append("data:default_head_image/jpeg;base64,");

        FileInputStream inputFile = null;
        String base64 = "";
        try {
            inputFile = new FileInputStream(file);
            byte[] bytes = new byte[(int) file.length()];
            inputFile.read(bytes);
            inputFile.close();
            String string = Base64.encodeToString(bytes, Base64.DEFAULT);
            base64 = string.replaceAll("\n", "");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        buffer.append(base64);
        return buffer;
    }

    /**
     * base64 转 文件
     *
     * @param base64Code
     * @param savePath
     * @throws Exception
     */
    public static void Base64ToFile(String base64Code, String savePath) throws Exception {
        //byte[] buffer = new BASE64Decoder().decodeBuffer(base64Code);
        byte[] buffer = Base64.decode(base64Code, Base64.DEFAULT);
        FileOutputStream out = new FileOutputStream(savePath);
        out.write(buffer);
        out.close();
    }


    public static void copyFile(File fileSource, File fileTarget) {

        FileInputStream fi = null;
        FileOutputStream fo = null;
        FileChannel in = null;
        FileChannel out = null;
        try {
            fi = new FileInputStream(fileSource);
            fo = new FileOutputStream(fileTarget);
            in = fi.getChannel();//得到对应的文件通道
            out = fo.getChannel();//得到对应的文件通道
            in.transferTo(0, in.size(), out);//连接两个通道，并且从in通道读取，然后写入out通道
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fi.close();
                in.close();
                fo.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * Bitmap --> byte[]
     *
     * @param bmp
     * @return
     */
    public static byte[] readBitmap(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        try {
            baos.flush();
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return baos.toByteArray();
    }


    public static int getScreenWidth(AppCompatActivity activity) { // px

        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    public static int getScreenWidth(Activity activity) { // px

        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }


    public static int getScreenHeight(Activity activity) {  // px

        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    public static int getScreenHeight(AppCompatActivity activity) {  // px

        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }


    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int px2sp(Context context, float pxValue) {
        return (int) (pxValue / context.getResources().getDisplayMetrics().scaledDensity + 0.5f);
    }

    /**
     * 按照一定的宽高比例裁剪图片
     *
     * @param bitmap
     * @param num1   长边的比例
     * @param num2   短边的比例
     * @return
     */
    public static Bitmap ImageCrop(Bitmap bitmap, int num1, int num2,
                                   boolean isRecycled) {
        if (bitmap == null) {
            return null;
        }
        int w = bitmap.getWidth(); // 得到图片的宽，高
        int h = bitmap.getHeight();
        int retX, retY;
        int nw, nh;
        if (w > h) {
            if (h > w * num2 / num1) {
                nw = w;
                nh = w * num2 / num1;
                retX = 0;
                retY = (h - nh) / 2;
            } else {
                nw = h * num1 / num2;
                nh = h;
                retX = (w - nw) / 2;
                retY = 0;
            }
        } else {
            if (w > h * num2 / num1) {
                nh = h;
                nw = h * num2 / num1;
                retY = 0;
                retX = (w - nw) / 2;
            } else {
                nh = w * num1 / num2;
                nw = w;
                retY = (h - nh) / 2;
                retX = 0;
            }
        }
        Bitmap bmp = Bitmap.createBitmap(bitmap, retX, retY, nw, nh, null,
                false);
        if (isRecycled && bitmap != null && !bitmap.equals(bmp)
                && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }
        return bmp;// Bitmap.createBitmap(bitmap, retX, retY, nw, nh, null,
        // false);
    }

    //复制到剪贴板
    public static void copyString(Context context, String content) {
        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        clipboardManager.setPrimaryClip(ClipData.newPlainText(null, content));
//      if (clipboardManager.hasPrimaryClip()){
//          clipboardManager.getPrimaryClip().getItemAt(0).getText();
//      }
    }

    //粘贴
    public static String pasteString(Context context) {
        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboardManager != null) {
            ClipData primaryClip = clipboardManager.getPrimaryClip();
            if (primaryClip != null) {
                //获取到内容
                ClipData.Item item = primaryClip.getItemAt(0);
                return item.getText().toString();
            }
        }
        return "";
    }


    public static Bitmap downloadPic(String url) {
        URL imgUrl = null;
        Bitmap bitmap = null;
        try {
            imgUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) imgUrl.openConnection();
            conn.setConnectTimeout(10000);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }

    /**
     * 截取scrollview的屏幕
     *
     * @param
     * @return
     */
    public static Bitmap getBitmapByView(View view) {
//        int h = 0;
//        Bitmap bitmap = null;
//        // 获取scrollview实际高度
//        for (int i = 0; i < scrollView.getChildCount(); i++) {
//            h += scrollView.getChildAt(i).getHeight();
//            scrollView.getChildAt(i).setBackgroundColor(
//                   );
//        }
//        // 创建对应大小的bitmap
//        bitmap = Bitmap.createBitmap(scrollView.getWidth(), h,
//                Bitmap.Config.ARGB_8888);
//        final Canvas canvas = new Canvas(bitmap);
//        scrollView.draw(canvas);
//        return bitmap;
        view.setBackgroundColor(Color.parseColor("#ffffff"));
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),
                Bitmap.Config.ARGB_8888);
        //利用bitmap生成画布
        Canvas canvas = new Canvas(bitmap);

        //把view中的内容绘制在画布上
        view.draw(canvas);

        return bitmap;
    }


    /**
     * 截取webView可视区域的截图
     *
     * @param webView 前提：WebView要设置webView.setDrawingCacheEnabled(true);
     * @return
     */
    private static Bitmap captureWebViewVisibleSize(WebView webView) {
        Bitmap bmp = webView.getDrawingCache();
//        Picture snapShot = webView.capturePicture();
//        Bitmap bmp = Bitmap.createBitmap(snapShot.getWidth(), snapShot.getHeight(), Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(bmp);
//        snapShot.draw(canvas);
        return bmp;

    }

    public static Bitmap captureScreen(Activity context) {
        View cv = context.getWindow().getDecorView();
        Bitmap bmp = Bitmap.createBitmap(cv.getWidth(), cv.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        cv.draw(canvas);
        return bmp;
    }

    public static void captureWebViewFile(WebView webView) {
        Bitmap bitmap = captureWebViewVisibleSize(webView);

        if (bitmap != null) {
            saveImageToGallery(bitmap);
        } else {
            ToastUtils.showLongToast(MyApplication.getContext().getString(R.string.commonutil_save_failed));
        }
    }

    public static void captureScreenFile(Activity activity) {
        Bitmap bitmap = captureScreen(activity);
        if (bitmap != null) {
            saveImageToGallery(bitmap);
        } else {
            ToastUtils.showLongToast(MyApplication.getContext().getString(R.string.commonutil_save_failed));
        }
    }


    public static void decoderBase64File(String base64Code) {
        //byte[] buffer = new BASE64Decoder().decodeBuffer(base64Code);
        String[] base64Codes = base64Code.split(",");
        byte[] bytes = null;
        if (base64Codes.length == 0) {
            ToastUtils.showLongToast(MyApplication.getContext().getString(R.string.commonutil_save_failed));
            return;
        } else if (base64Codes.length == 1) {
            bytes = Base64.decode(base64Codes[0], Base64.DEFAULT);
        } else if (base64Codes.length == 2) {
            bytes = Base64.decode(base64Codes[1], Base64.DEFAULT);
        }
        FileOutputStream out = null;
        try {
            File appDir = new File(getRootFilePath() + "dke");
            if (!appDir.exists()) {
                appDir.mkdirs();
            }
            String fileName = "dke_" + System.currentTimeMillis() + ".jpg";
            File file = new File(appDir, fileName);
            out = new FileOutputStream(file);
            out.write(bytes);
            out.flush();
            out.close();

            MediaStore.Images.Media.insertImage(MyApplication.getContext().getContentResolver(), file.getAbsolutePath(), fileName, "DKE");
            // 最后通知图库更新
            Uri uri = Uri.fromFile(file);
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri);

            MyApplication.getContext().sendBroadcast(intent);
            ToastUtils.showLongToast(MyApplication.getContext().getString(R.string.commonutil_save_success) + file.getAbsolutePath());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            ToastUtils.showLongToast(MyApplication.getContext().getString(R.string.commonutil_save_failed));
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    //保存图片到本地 并更新图库
    public static String saveImageToGalleryTwo(Bitmap bmp) {
        // 首先保存图片 创建文件夹
        FileOutputStream fos = null;
        File file = null;
        try {
            File appDir = new File(getRootFilePath() + "ntc");
            if (!appDir.exists()) {
                appDir.mkdirs();
            }
            String fileName = "ntc_" + System.currentTimeMillis() + ".jpg";
            file = new File(appDir, fileName);
            fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                boolean b = saveImageWithAndroidQ(MyApplication.getContext(), file, fileName, dictionaryName);
                if (b) {
                    ToastUtils.showLongToast(MyApplication.getContext().getString(R.string.commonutil_save_success)
//                            + file.getAbsolutePath()
                    );
                } else {
                    ToastUtils.showLongToast(MyApplication.getContext().getString(R.string.commonutil_save_failed));
                }
            } else {
                // 其次把文件插入到系统图库
                MediaStore.Images.Media.insertImage(MyApplication.getContext().getContentResolver(), file.getAbsolutePath(), fileName, dictionaryName);
                // 最后通知图库更新
                Uri uri = Uri.fromFile(file);
                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri);
                MyApplication.getContext().sendBroadcast(intent);
                ToastUtils.showLongToast(MyApplication.getContext().getString(R.string.commonutil_save_success)
//                        + file.getAbsolutePath()
                );
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            ToastUtils.showLongToast(MyApplication.getContext().getString(R.string.commonutil_save_failed));
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return file.getAbsolutePath();
    }

    //保存图片到本地 并更新图库
    public static void saveImageToGallery(Bitmap bmp) {
        // 首先保存图片 创建文件夹
        FileOutputStream fos = null;
        try {
            File appDir = new File(getRootFilePath() + "ntc");
            if (!appDir.exists()) {
                appDir.mkdirs();
            }
            String fileName = "ntc_" + System.currentTimeMillis() + ".jpg";
            File file = new File(appDir, fileName);
            fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                boolean b = saveImageWithAndroidQ(MyApplication.getContext(), file, fileName, dictionaryName);
                if (b) {
                    ToastUtils.showLongToast(MyApplication.getContext().getString(R.string.commonutil_save_success)
//                            + file.getAbsolutePath()
                    );
                } else {
                    ToastUtils.showLongToast(MyApplication.getContext().getString(R.string.commonutil_save_failed));
                }
            } else {
                // 其次把文件插入到系统图库
                MediaStore.Images.Media.insertImage(MyApplication.getContext().getContentResolver(), file.getAbsolutePath(), fileName, dictionaryName);
                // 最后通知图库更新
                Uri uri = Uri.fromFile(file);
                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri);
                MyApplication.getContext().sendBroadcast(intent);
                ToastUtils.showLongToast(MyApplication.getContext().getString(R.string.commonutil_save_success)
//                        + file.getAbsolutePath()
                );
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            ToastUtils.showLongToast(MyApplication.getContext().getString(R.string.commonutil_save_failed));
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    //数字保留两位，不足的加上  比特币显示小数点后六位，其他币种显示三位，四舍五入的方式，所有币种如果是0显示0.00
    public static String notNull(String s) {
        if ("0".equals(s) || "".equals(s) || TextUtils.isEmpty(s)) {
            return "0.00";
        }

        if ("0".equals(numberFormatter(s))) {
            return "0.00";
        }
        return s;
    }

    public static String setScale2(String s) {
        if ("0".equals(s) || "".equals(s) || TextUtils.isEmpty(s)) {
            return "0.00";
        }
        s = numberFormatter(s);
        if ("0".equals(s)) {
            return "0.00";
        }

        BigDecimal bg = new BigDecimal(s);
        return bg.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
    }


    public static String setScale(String s, int newScale) {
//        return setScale2(s);
        if ("0".equals(s) || "".equals(s) || TextUtils.isEmpty(s)) {
            return "0.00";
        }
        if ("0".equals(numberFormatter(s))) {
            return "0.00";
        }
        if (newScale == 0) {
            return s;
        }
        BigDecimal bg = new BigDecimal(s);
        return bg.setScale(newScale, BigDecimal.ROUND_HALF_UP).toString();
    }


    //去掉多余的0
    public static String numberFormatter(String strNumber) {
        if ("".equals(strNumber) || strNumber == null) return "0";
        if (strNumber.indexOf(".") > 0) {
            strNumber = strNumber.replaceAll("0+?$", "");//去掉多余的0
            strNumber = strNumber.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return strNumber;
    }

    public static String numberFormatter2(String strNumber) {
        strNumber = numberFormatter(strNumber);
        if (!strNumber.contains(".")) {
            BigDecimal bg = new BigDecimal(strNumber);
            return bg.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
        }
        return strNumber;
    }


    //将数字，显示 万  个 单位
    public static String unitConversion(String s) {
        if ("".equals(s) || TextUtils.isEmpty(s)) return s;
        try {
            BigDecimal bigDecimal = new BigDecimal(s);
            BigDecimal decimal = new BigDecimal("10000");
            int i = bigDecimal.compareTo(decimal);//-1表示小于，0是等于，1是大于。
            if (i >= 0) {
                return bigDecimal.divide(decimal, 2, BigDecimal.ROUND_HALF_UP).toString() + "万";
            } else {
                return bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
            }
        } catch (Exception e) {
            return s;
        }
    }

    /**
     * 开启当前应用设置
     *
     * @param context
     */
    public static void startApplication(Context context) {
        //跳转应用设置页面--去设置权限开启和不开启
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(Uri.fromParts("package", context.getPackageName(), null));
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }
    //保存图片 Q

    /**
     * 通过MediaStore保存，兼容AndroidQ，保存成功自动添加到相册数据库，无需再发送广告告诉系统插入相册
     *
     * @param context      context
     * @param sourceFile   源文件
     * @param saveFileName 保存的文件名
     * @param saveDirName  picture子目录
     * @return 成功或者失败
     */
    public static boolean saveImageWithAndroidQ(Context context,
                                                File sourceFile,
                                                String saveFileName,
                                                String saveDirName) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DESCRIPTION, "This is an image");
        values.put(MediaStore.Images.Media.DISPLAY_NAME, saveFileName);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
        values.put(MediaStore.Images.Media.TITLE, "Image.png");
        values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/" + saveDirName);

        Uri external = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver resolver = context.getContentResolver();

        Uri insertUri = resolver.insert(external, values);
        BufferedInputStream inputStream = null;
        OutputStream os = null;
        boolean result = false;
        try {
            inputStream = new BufferedInputStream(new FileInputStream(sourceFile));
            if (insertUri != null) {
                os = resolver.openOutputStream(insertUri);
            }
            if (os != null) {
                byte[] buffer = new byte[1024 * 4];
                int len;
                while ((len = inputStream.read(buffer)) != -1) {
                    os.write(buffer, 0, len);
                }
                os.flush();
            }
            result = true;
        } catch (IOException e) {
            result = false;
        } finally {
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }


    //安装APK Intent及其它文件相关Intent
    /*
     * 自Android N开始，是通过FileProvider共享相关文件，但是Android Q对公有目录 File API进行了限制
     * 从代码上看，又变得和以前低版本一样了，只是必须加上权限代码Intent.FLAG_GRANT_READ_URI_PERMISSION
     */
    public static void installApk(Context context, String mFilePath) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            //适配Android Q,注意mFilePath是通过ContentResolver得到的，上述有相关代码
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse(mFilePath), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            context.startActivity(intent);
            return;
        }

        File file = new File(mFilePath);
        if (!file.exists())
            return;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(context.getApplicationContext(), MyApplication.getContext().getPackageName() + ".fileprovider", file);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    //跳转浏览器 打开url
    public static void downloadByBrowser(Context context, String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse(url));
        context.startActivity(intent);
    }
    public static void androidamap(Context context, String lat, String lon) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setData(Uri.parse("androidamap://navi?sourceApplication=货运管家&poiname=测试&lat="+lat+"&lon="+lon+"&dev=1"));
        intent.setPackage("com.autonavi.minimap");
        context.startActivity(intent);
    }

    //手机是否安装某应用 包名
    public static boolean isAppExist(Context context, String pkgName) {
        ApplicationInfo info;
        try {
            info = context.getPackageManager().getApplicationInfo(pkgName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            info = null;
        }

        return info != null;
    }

    public static void startCallView(Context context, String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 启动到应用商店app详情界面
     */
    public static void launchAppDetail(Context mContext) {
        launchAppDetail(mContext, mContext.getPackageName(), "");
    }

    public static void launchAppDetail(Context mContext, String appPkg, String marketPkg) {
        try {
            if (TextUtils.isEmpty(appPkg)) {
                return;
            }
            Uri uri = Uri.parse("market://details?id=" + appPkg);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            if (!TextUtils.isEmpty(marketPkg)) {
                intent.setPackage(marketPkg);
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
