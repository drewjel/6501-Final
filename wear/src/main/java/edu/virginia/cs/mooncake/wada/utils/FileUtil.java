package edu.virginia.cs.mooncake.wada.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.regex.Pattern;

public class FileUtil {

    public static int fileCount() {
        String folderName = Environment.getExternalStorageDirectory() + "/wada/data";
        File folder = new File(folderName);
        if (folder.exists() == false) {
            folder.mkdir();
        }
        File[] fileList = folder.listFiles();
        if (fileList == null)
            return 0;
        return fileList.length;
    }

    public static boolean deleteFile(String fileName) {
        String folderName = Environment.getExternalStorageDirectory() + "/wada/data";
        String filePath = folderName + "/" + fileName;
        File file = new File(filePath);
        return file.delete();
    }

    public static void deleteAllFiles() {
        String folderName = Environment.getExternalStorageDirectory() + "/wada/data";
        File folder = new File(folderName);
        File[] fileList = folder.listFiles();

        for (int i = 0; fileList != null && i < fileList.length; i++) {
            fileList[i].delete();
        }
    }

    public static File[] getFileList() {
        String folderName = Environment.getExternalStorageDirectory() + "/wada/data";
        File folder = new File(folderName);
        return folder.listFiles();
    }

    public static String[] getFileListString() {
        String folderName = Environment.getExternalStorageDirectory() + "/wada/data";
        File folder = new File(folderName);
        return folder.list();
    }

    public static boolean saveStringToFile(String fileName, String data) {
        boolean flag = true;

        if (fileName.length() < 1)
            fileName = "X_" + System.currentTimeMillis();

        String folderName = Environment.getExternalStorageDirectory() + "/wada/data";
        File file = new File(folderName);
        if (file.exists() == false) {
            file.mkdirs();
        }

        String filePath = folderName + "/" + fileName;
        try {
            FileOutputStream fos = new FileOutputStream(filePath, true);
            fos.write(data.getBytes());
            fos.flush();
            fos.close();
        } catch (Exception ex) {
            flag = false;
            Log.e("File save error", ex.toString());
        }

        return flag;
    }

    public static boolean isConfigAvailable() {
        String folderName = Environment.getExternalStorageDirectory() + "/wada/config";
        File file = new File(folderName);
        if (file.exists() == false) {
            file.mkdir();
        }

        String filePath = folderName + "/config.txt";
        file = new File(filePath);
        return file.exists();
    }

    public static String[] readConfig() {
        String folderName = Environment.getExternalStorageDirectory() + "/wada/config";
        File file = new File(folderName);
        if (file.exists() == false) {
            file.mkdir();
        }

        String filePath = folderName + "/config.txt";
        file = new File(filePath);
        if (file.exists() == false) {
            return null;
        }

        String[] config = new String[3];

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            int lineCount = 0;
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.length() > 0) {
                    if (lineCount >= 3) {
                        return null;
                    }

                    Pattern p = Pattern.compile("[^a-zA-Z0-9_ ,]");
                    boolean hasOtherChar = p.matcher(line).find();
                    if (hasOtherChar) {
                        return null;
                    }

                    config[lineCount] = line;
                    lineCount++;
                }
            }

            if (lineCount < 2) {
                return null;
            }else if(lineCount==2){
                config[lineCount] = " ,info1,info2,info3,info4,info5";
            }

            br.close();
        } catch (Exception ex) {
            Log.i("Error", "Reading config.txt");
            return null;
        }

        return config;
    }

    public static boolean deleteConfig() {
        String filePath = Environment.getExternalStorageDirectory() + "/wada/config/config.txt";
        File file = new File(filePath);
        return file.delete();
    }

}
