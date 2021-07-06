package utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ApkUtils {
    /**
     * 以行为单位读取文件，常用于读面向行的格式化文件 
     */
    public static List<String> readFileByLines(String fileName) {
        ArrayList<String> strings = new ArrayList<>();
        File file = new File(fileName);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束  
            while ((tempString = reader.readLine()) != null) {
                // 显示行号  
                System.out.println("line " + line + ": " + tempString);
                strings.add(tempString);
                line++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {

                }
            }
            return strings;
        }
    }

    /**
     * 获取除自身外其他apk列表
     * */
    public static List<ResolveInfo> getAllApps(Context mContext) {
        List<ResolveInfo> allApp = new ArrayList<ResolveInfo>();
        PackageManager packageManager = mContext.getPackageManager();
        // 所有应用列表
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> tempAppList = packageManager.queryIntentActivities(mainIntent, 0);
        allApp.addAll(tempAppList);
        String name = mContext.getPackageName();

        for (ResolveInfo resolveInfo : tempAppList) {
            if (name.equals(resolveInfo.activityInfo.packageName)) {
                allApp.remove(resolveInfo);
                break;
            }
        }

        return allApp;
    }
}
