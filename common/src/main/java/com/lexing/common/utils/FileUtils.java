package com.lexing.common.utils;

import android.app.Activity;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import com.lexing.common.base.ViewInject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.channels.FileChannel;

/**
 * 文件与流处理工具类<br>
 * <p>
 * <b>创建时间</b> 2014-8-14
 *
 * @version 1.1
 */
public final class FileUtils {

    /**
     * 检测SD卡是否存在
     */
    public static boolean hasSDcard() {
        return Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState());
    }

    /**
     * 将文件保存到本地
     */
    public static void saveFileCache(byte[] fileData, String folderPath,
                                     String fileName) {
        File folder = new File(folderPath);
        folder.mkdirs();
        File file = new File(folderPath, fileName);
        ByteArrayInputStream is = new ByteArrayInputStream(fileData);
        OutputStream os = null;
        try {
            file.createNewFile();
            os = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int len;
            while (-1 != (len = is.read(buffer))) {
                os.write(buffer, 0, len);
            }
            os.flush();
        } catch (Exception e) {
            throw new RuntimeException(
                    FileUtils.class.getClass().getName(), e);
        } finally {
            closeIO(is, os);
        }
    }

    /**
     * 将文件保存到本地
     */
    public static void writeFile(byte[] fileData, String folderPath,
                                 String fileName) {
        File folder = new File(folderPath);
        folder.mkdirs();
        File file = new File(folderPath, fileName);
        ByteArrayInputStream is = new ByteArrayInputStream(fileData);
        OutputStream os = null;
        if (!file.exists()) {
            try {
                file.createNewFile();
                os = new FileOutputStream(file);
                byte[] buffer = new byte[1024];
                int len;
                while (-1 != (len = is.read(buffer))) {
                    os.write(buffer, 0, len);
                }
                os.flush();
            } catch (Exception e) {
                throw new RuntimeException(
                        FileUtils.class.getClass().getName(), e);
            } finally {
                closeIO(is, os);
            }
        }
    }

    /**
     * 从指定文件夹获取文件
     *
     * @return 如果文件不存在则创建, 如果如果无法创建文件或文件名为空则返回null
     */
    public static File getSaveFile(String folderPath, String fileNmae) {
        File file = new File(getSavePath(folderPath) + File.separator
                + fileNmae);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * 获取SD卡下指定文件夹的绝对路径
     *
     * @return 返回SD卡下的指定文件夹的绝对路径
     */
    public static String getSavePath(String folderName) {
        return getSaveFolder(folderName).getAbsolutePath();
    }

    /**
     * 获取文件夹对象
     *
     * @return 返回SD卡下的指定文件夹对象，若文件夹不存在则创建
     */
    public static File getSaveFolder(String folderName) {
        File file = new File(getSDCardPath() + File.separator + folderName
                + File.separator);
        boolean makeResult = file.mkdirs();
        return file;
    }

    public static String getSDCardPath() {
        return Environment.getExternalStorageDirectory().getPath();
    }

    /**
     * 输入流转byte[]<br>
     */
    public static final byte[] input2byte(InputStream inStream) {
        if (inStream == null) {
            return null;
        }
        byte[] in2b = null;
        BufferedInputStream in = new BufferedInputStream(inStream);
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        int rc;
        try {
            while ((rc = in.read()) != -1) {
                swapStream.write(rc);
            }
            in2b = swapStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeIO(inStream, in, swapStream);
        }
        return in2b;
    }

    /**
     * 把uri转为File对象
     */
    public static File uri2File(Activity aty, Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(aty, uri, projection, null,
                null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return new File(cursor.getString(column_index));
    }

    /**
     * 复制文件
     *
     * @param from
     * @param to
     */
    public static void copyFile(File from, File to) {
        if (null == from || !from.exists()) {
            return;
        }
        if (null == to) {
            return;
        }
        FileInputStream is = null;
        FileOutputStream os = null;
        try {
            is = new FileInputStream(from);
            if (!to.exists()) {
                to.createNewFile();
            }
            os = new FileOutputStream(to);
            copyFileFast(is, os);
        } catch (Exception e) {
            throw new RuntimeException(FileUtils.class.getClass().getName(), e);
        } finally {
            closeIO(is, os);
        }
    }

    /**
     * 快速复制文件（采用nio操作）
     *
     * @param is 数据来源
     * @param os 数据目标
     * @throws java.io.IOException
     */
    public static void copyFileFast(FileInputStream is, FileOutputStream os)
            throws IOException {
        FileChannel in = is.getChannel();
        FileChannel out = os.getChannel();
        in.transferTo(0, in.size(), out);
    }

    /**
     * 关闭流
     *
     * @param closeables
     */
    public static void closeIO(Closeable... closeables) {
        if (null == closeables || closeables.length <= 0) {
            return;
        }
        for (Closeable cb : closeables) {
            try {
                if (null == cb) {
                    continue;
                }
                cb.close();
            } catch (IOException e) {
                throw new RuntimeException(
                        FileUtils.class.getClass().getName(), e);
            }
        }
    }


    /**
     * 向内存文件中写一行数据
     */
    public static void writeLineInternal(FileOutputStream internalOutput, String str) throws IOException {
        OutputStreamWriter osw = new OutputStreamWriter(internalOutput);
        try {
            osw.write(str);
            osw.flush();
        } finally {
            closeIO(osw);
        }
    }

    /**
     * 向sd卡文件中写一行数据
     */
    public static String readLineExternal(String filePath) throws IOException {
        InputStreamReader isr = null;
        try {
            isr = new InputStreamReader(new FileInputStream(filePath));
            BufferedReader br = new BufferedReader(isr);
            return br.readLine();
        } finally {
            closeIO(isr);
        }
    }

    /**
     * 向sd卡文件中写一行数据
     */
    public static void writeLineExternal(String filePath, String str, boolean expend) throws IOException {
        File file = new File(filePath.substring(0,
                filePath.lastIndexOf(File.separator)));
        if (!file.exists()) {
            file.mkdirs();
        }
        OutputStreamWriter out = null;
        try {
            out = new OutputStreamWriter(new FileOutputStream(filePath, expend));
            out.write(str);
            out.write("\n");
            out.flush();
        } finally {
            closeIO(out);
        }
    }

    /**
     * 向sd卡文件中写一行数据
     */
    public static void writeLineExternal(String filePath, String str) throws IOException {
        File file = new File(filePath.substring(0,
                filePath.lastIndexOf(File.separator)));
        if (!file.exists()) {
            file.mkdirs();
        }
        OutputStreamWriter out = null;
        try {
            out = new OutputStreamWriter(new FileOutputStream(filePath, true));
            out.write(str);
            out.write("\n");
            out.flush();
        } finally {
            closeIO(out);
        }
    }

    /**
     * 图片写入文件
     *
     * @param bitmap   图片
     * @param filePath 文件路径
     * @return 是否写入成功
     */
    public static boolean bitmapToFile(Bitmap bitmap, String filePath) {
        boolean isSuccess = false;
        if (bitmap == null) {
            return false;
        }
        File file = new File(filePath.substring(0,
                filePath.lastIndexOf(File.separator)));
        if (!file.exists()) {
            file.mkdirs();
        }

        OutputStream out = null;
        try {
            out = new BufferedOutputStream(new FileOutputStream(filePath),
                    8 * 1024);
            isSuccess = bitmap.compress(CompressFormat.PNG, 100, out);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeIO(out);
        }
        return isSuccess;
    }

    /**
     * 保存图片到系统图库
     */
    public static String saveImageToGallery(Context context, Bitmap bmp) throws FileNotFoundException {
        if (!hasSDcard()) {
            ViewInject.toast("没有检测到SD卡，无法保存");
            return null;
        }
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "tiantianzhuan");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".png";
        File file = new File(appDir, fileName);
        FileOutputStream fos = new FileOutputStream(file);
        bmp.compress(CompressFormat.PNG, 100, fos);
        try {
            fos.flush();
        } catch (IOException e) {
        } finally {
            closeIO(fos);
        }

        // 其次把文件插入到系统图库
        String path;
        path = MediaStore.Images.Media.insertImage(context.getContentResolver(),
                file.getAbsolutePath(), fileName, null);
        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + path)));
        return path;
    }

    public static Bitmap bitmapFromFile(Context context, String imgPath) throws FileNotFoundException {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        //This field works in conjuction with inPurgeable. If inPurgeable is false,
        //then this field is ignored. If inPurgeable is true,
        //then this field determines whether the bitmap can share run reference to the
        //input data (inputstream, array, etc.) or if it must make run deep copy.
        opt.inInputShareable = true;
        FileInputStream is = new FileInputStream(imgPath);
        //            LogUtil.d("img: " + imgPath + ", bitmap = " + bitmap);
        return BitmapFactory.decodeStream(is, null, opt);
    }

    /**
     * 从文件中读取文本
     *
     * @param filePath
     * @return
     */
    public static String readFile(String filePath) {
        InputStream is;
        try {
            is = new FileInputStream(filePath);
        } catch (Exception e) {
            throw new RuntimeException(FileUtils.class.getName()
                    + "readFile---->" + filePath + " not found");
        }
        return inputStream2String(is);
    }

    /**
     * 从内存中读取文本文件
     *
     * @param cxt      应用context
     * @param filePath 文件地址
     * @return 文本
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static String readMemFile(Context cxt, String filePath) throws IOException {
        String folder = cxt.getFilesDir().getAbsolutePath() + File.separator + filePath;
        FileInputStream fis = new FileInputStream(folder);
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));
        String str;
        StringBuilder sb = new StringBuilder();
        while ((str = br.readLine()) != null) {
            sb.append(str);
        }
        closeIO(fis);
        return sb.toString();
    }

    /**
     * 从assets中读取文本
     *
     * @param name
     * @return
     */
    public static String readFileFromAssets(Context context, String name) {
        InputStream is;
        try {
            is = context.getResources().getAssets().open(name);
        } catch (Exception e) {
            throw new RuntimeException(FileUtils.class.getName()
                    + ".readFileFromAssets---->" + name + " not found");
        }
        return inputStream2String(is);
    }

    /**
     * 输入流转字符串
     *
     * @param is
     * @return 一个流中的字符串
     */
    public static String inputStream2String(InputStream is) {
        if (null == is) {
            return null;
        }
        StringBuilder resultSb = null;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            resultSb = new StringBuilder();
            String len;
            while (null != (len = br.readLine())) {
                resultSb.append(len);
            }
        } catch (Exception ex) {
        } finally {
            closeIO(is);
        }
        return null == resultSb ? null : resultSb.toString();
    }

    /**
     * 拷贝assets文件夹到外部存储器并解压
     */
    public static void copyAssetsFolder(Context context, String assetFolder, String targetFolder) throws IOException {
        //Copy Zipped File
        String[] files;
        AssetManager am = context.getAssets();
        try {
            files = am.list(assetFolder);
        } catch (IOException e1) {
            return;
        }
        File cachePath = new File(targetFolder);
        if (!cachePath.exists()) {
            cachePath.mkdirs();
        }
        String fileName;
        InputStream in;
        OutputStream out;
        for (String file : files) {
            fileName = file;
            // we make sure file name not contains '.' to be a folder.
            in = am.open(assetFolder + File.separator + fileName, AssetManager.ACCESS_UNKNOWN);

            File outFile = new File(cachePath, fileName);
            boolean deleteRes = false;
            if (outFile.exists())
                deleteRes = outFile.delete();
            out = new FileOutputStream(outFile);

            // Transfer bytes from in to out
            byte[] buf = new byte[4096];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            closeIO(in);
            closeIO(out);
        }
    }

    public static void copyAssetsFile(Context context, String assetsFile, String targetFile) throws IOException {
        AssetManager am = context.getAssets();
        InputStream in;
        OutputStream out;
        in = am.open(assetsFile, AssetManager.ACCESS_UNKNOWN);

        File outFile = new File(targetFile);
        if (outFile.exists()) {
            outFile.delete();
        }
        File outFolder = new File(targetFile.substring(0, targetFile.lastIndexOf(File.separator)));
        if (!outFolder.exists()) {
            outFolder.mkdirs();
        }
        out = new FileOutputStream(outFile);
        // Transfer bytes from in to out
        byte[] buf = new byte[4096];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        closeIO(in);
        closeIO(out);
    }

    /**
     * 获取指定文件夹
     */
    public static long getFileSizes(File f) throws IOException {
        long size = 0;
        File flist[] = f.listFiles();
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                size = size + getFileSizes(flist[i]);
            } else {
                size = size + getFileSize(flist[i]);
            }
        }
        return size;
    }

    /**
     * 获取指定文件大小
     */
    public static long getFileSize(File file) throws IOException {
        long size = 0;
        if (file.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(file);
            size = fis.available();
            fis.close();
        } else {
            file.createNewFile();
        }
        return size;
    }
}