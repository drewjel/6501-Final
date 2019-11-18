package edu.virginia.cs.mooncake.wada.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

public class ServiceUtil {

    public static boolean isMySensorServiceRunning(Context context, String serviceName) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            //if (WatchSensorService.class.getName().equals(service.service.getClassName())) {
            if (serviceName.equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

}
