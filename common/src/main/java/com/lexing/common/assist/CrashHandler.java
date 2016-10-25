package com.lexing.common.assist;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.Toast;

import com.lexing.common.base.ActivityStack;
import com.lexing.common.utils.FileUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Date;

/**
 * UncaughtExceptionHandler：线程未捕获异常控制器是用来处理未捕获异常的。 如果程序出现了未捕获异常默认情况下则会出现强行关闭对话框
 * 实现该接口并注册为程序中的默认未捕获异常处理 这样当未捕获异常发生时，就可以做些异常处理操作 例如：收集异常信息，发送错误报告 等。
 * UncaughtException处理类,当程序发生Uncaught异常的时候,由该类来接管程序,并记录发送错误报告. <br>
 * sample:
 * Class BaseApp extends Application{
 *     public void onCreate(){
 *         super.onCreate();
 *         CrashHandler.SAVE_CRASH_LOG = true;
 *         CrashHandler.attach(this);
 *     }
 * }
 * <b>创建时间</b> 2014-7-2
 */
public class CrashHandler implements UncaughtExceptionHandler {
    public static String BASE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath()
            + "/Android/data/";
    public static String BASE_LOG_PATH = BASE_PATH + "/logs";

    public static final String TAG = "CrashHandler";
    private final Context mContext;
    public static boolean SAVE_CRASH_LOG = false;
    // log文件的后缀名
    private static final String FILE_NAME_SUFFIX = ".log";
    private static final String FILE_NAME_PREFIX = "crash-";
    //系统默认的UncaughtException处理类
    private UncaughtExceptionHandler mDefaultHandler;

    private CrashHandler(Context cxt) {
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        // 将当前实例设为系统默认的异常处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
        // 获取Context，方便内部使用
        mContext = cxt.getApplicationContext();
        BASE_PATH = BASE_PATH + cxt.getPackageName();
        BASE_LOG_PATH = BASE_PATH + "/logs";
    }

    public synchronized static CrashHandler attach(Context cxt) {
        if (Thread.getDefaultUncaughtExceptionHandler() == null || !(Thread.getDefaultUncaughtExceptionHandler() instanceof CrashHandler)) {
            return new CrashHandler(cxt);
        } else {
            return (CrashHandler) Thread.getDefaultUncaughtExceptionHandler();
        }
    }

    /**
     * 这个是最关键的函数，当程序中有未被捕获的异常，系统将会自动调用#uncaughtException方法
     * thread为出现未捕获异常的线程，ex为未捕获的异常，有了这个ex，我们就可以得到异常信息。
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(thread, ex) && mDefaultHandler != null) {
            //如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                //DO nothing
            }
            //退出程序
//        Intent startMain = new Intent(Intent.ACTION_MAIN);
//        startMain.addCategory(Intent.CATEGORY_HOME);
//        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        mContext.startActivity(startMain);
            if(listener != null){
                listener.onKill();
            }
            ActivityStack.getInstance().AppExit();
            System.exit(0);
        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private boolean handleException(Thread thread, Throwable ex) {
        if (ex == null) {
            return false;
        }
        if(listener != null){
            listener.onCaught(ex);
        }
        //使用Toast来显示异常信息
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(mContext, "很抱歉,程序出现异常,即将退出.", Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }.start();
        Log.e(TAG, "Thread: " + thread.getName() + "-" + thread.getId() + " throws "  + ex.toString(), ex);
        if(SAVE_CRASH_LOG){
            saveToSDCard(thread, ex);
        }
        return true;
    }

    private void saveToSDCard(Thread thread, Throwable ex) {
        String time = DateFormat.format("yyyy-MM-dd-HH-mm-ss",new Date()).toString();
        String fileName = FILE_NAME_PREFIX + time + FILE_NAME_SUFFIX;
        File dir = new File(BASE_LOG_PATH);
        if(!dir.exists()){
            if(!dir.mkdirs()){
                Log.e(TAG, "Crash log dir can't be made");
                return;
            }
        }
        File file = new File(BASE_LOG_PATH,
                fileName);
        boolean append = false;
        if (System.currentTimeMillis() - file.lastModified() > 5000) {
            append = true;
        }
        PrintWriter pw = null;
        try{

            pw = new PrintWriter(new BufferedWriter(new FileWriter(
                    file, append)));

            pw.print("Thread: ");
            pw.print(thread.getName());
            pw.print(" : ");
            pw.println(thread.getId());
            pw.println();

            // 导出发生异常的时间
            pw.println(android.text.format.DateFormat.format("yyyy-MM-dd-HH-mm-ss",new Date()));
            // 导出手机信息
            dumpPhoneInfo(pw);
            pw.println();
            // 导出异常的调用栈信息
            ex.printStackTrace(pw);
            pw.println();
            pw.flush();
        } catch (Exception e){
            Log.e(TAG, "Log dump fail", e);
        } finally {
            FileUtils.closeIO(pw);
        }
    }

    private void dumpPhoneInfo(PrintWriter pw) throws NameNotFoundException {
        // 应用的版本名称和版本号
        PackageManager pm = mContext.getPackageManager();
        PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(),
                PackageManager.GET_ACTIVITIES);
        pw.print("App Version: ");
        pw.print(pi.versionName);
        pw.print('_');
        pw.println(pi.versionCode);
        pw.println();

        // android版本号
        pw.print("OS Version: ");
        pw.print(Build.VERSION.RELEASE);
        pw.print("_");
        pw.println(Build.VERSION.SDK_INT);
        pw.println();

        // 手机制造商
        pw.print("Vendor: ");
        pw.println(Build.MANUFACTURER);
        pw.println();

        // 手机型号
        pw.print("Model: ");
        pw.println(Build.MODEL);
        pw.println();

        // cpu架构
        pw.print("CPU ABI: ");
        pw.println(Build.CPU_ABI);
        pw.println();
    }

    private UncaughtExceptionListener listener;

    public interface UncaughtExceptionListener{
        void onCaught(Throwable ex);

        void onKill();
    }

    public void setUncaughtExceptionListener(UncaughtExceptionListener listener){
        this.listener = listener;
    }
}