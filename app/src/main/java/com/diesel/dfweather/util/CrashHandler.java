package com.diesel.dfweather.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.os.Process;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 异常捕获工具类
 *
 * @author Diesel
 *
 *         Time: 2016/8/12
 *
 *         Modified By:
 *         Modified Date:
 *         Why & What is modified:
 * @version 1.0.0
 */
public class CrashHandler implements UncaughtExceptionHandler {

    private final String TAG = "CrashHandler";

    private static final String FILE_NAME_SUFFIX = ".trace";

    private static CrashHandler sInstance = new CrashHandler();

    private UncaughtExceptionHandler mDefaultCrashHandler;

    private Context mContext;

    private String mCrashLogPath = null;

    private CrashHandler() {
    }

    public static CrashHandler getInstance() {
        return sInstance;
    }

    public void init(Context context) {
        this.mContext = context;
        mDefaultCrashHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);

        mContext = context.getApplicationContext();
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            mCrashLogPath = Environment.getExternalStorageDirectory().getPath()
                    + "/dfweather/log/crash";
        } else {
            mCrashLogPath = "/data/data/" + mContext.getPackageName() + "/log/crash";
        }
        Log.e(TAG, "init()--> LogPath=" + mCrashLogPath);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        try {
            dumpExceptionToSDCard(ex);
            uploadExceptionToServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ex.printStackTrace();
        if (mDefaultCrashHandler != null) {
            mDefaultCrashHandler.uncaughtException(thread, ex);
        } else {
            Process.killProcess(Process.myPid());
        }
    }

    private void dumpExceptionToSDCard(Throwable ex) throws IOException {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Log.e(TAG, "sdcard unmounted, skip dump exception");
            return;
        }
        File filePath = new File(mCrashLogPath);
        if (!filePath.exists()) {
            if (!filePath.mkdirs()) {
                return;
            }
        }
        String fileName = new SimpleDateFormat("yyyy-MM-dd HH", Locale.getDefault())
                .format(new Date()).concat(FILE_NAME_SUFFIX);
        File saveFile = new File(filePath + File.separator + fileName);
        if (!saveFile.exists()) {
            if (!saveFile.createNewFile()) {
                return;
            }
        }

        Log.d(TAG, "dumpExceptionToSDCard() saveFile=" + saveFile.getName());

        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                .format(new Date());
        try {
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(saveFile)));
            pw.println(time);
            pw.println("------------------------------------------");
            dumpPhoneInfo(pw);
            pw.println();
            ex.printStackTrace(pw);
            pw.println("------------------------------------------");
            pw.println();
            pw.close();
        } catch (Exception e) {
            Log.e(TAG, "dump crash info failed");
        }
    }

    private void dumpPhoneInfo(PrintWriter pw) throws NameNotFoundException {
        PackageManager pm = mContext.getPackageManager();
        PackageInfo pi = pm
                .getPackageInfo(mContext.getPackageName(), PackageManager.GET_ACTIVITIES);
        pw.print("App Version: ");
        pw.print(pi.versionName);
        pw.print('_');
        pw.println(pi.versionCode);
        pw.print("OS Version: ");
        pw.print(Build.VERSION.RELEASE);
        pw.print("_");
        pw.println(Build.VERSION.SDK_INT);
        pw.print("Vendor: ");
        pw.println(Build.MANUFACTURER);
        pw.print("Model: ");
        pw.println(Build.MODEL);
        pw.print("CPU ABI: ");
        pw.println(Build.CPU_ABI);
    }

    private void uploadExceptionToServer() {
        //TODO Upload Exception Message To Your Web Server
    }

}
