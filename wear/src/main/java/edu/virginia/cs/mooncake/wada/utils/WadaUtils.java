package edu.virginia.cs.mooncake.wada.utils;

import android.content.Context;

/**
 * Created by TOSHIBA on 1/3/2016.
 */
public class WadaUtils {

    public static String getTag(Context context) {
        String subject = SharedPrefUtil.getSharedPref(ConstantsUtil.SHARED_PREF_SUBJECT, context);
        String hand = SharedPrefUtil.getSharedPref(ConstantsUtil.SHARED_PREF_HAND, context);
        String activity = SharedPrefUtil.getSharedPref(ConstantsUtil.SHARED_PREF_ACTIVITY, context);
        String info = SharedPrefUtil.getSharedPref(ConstantsUtil.SHARED_PREF_INFO, context);
        if(subject!=null)
            subject=subject.trim();
        if(activity!=null)
            activity = activity.trim();
        if(info!=null)
            info = info.trim();
        return subject + "-" + hand + "-" + activity + "-" + info;

    }

    public static String[][] defaultConfig() {
        String[][] config = {
                {"subject1", "subject2", "subject3", "subject4", "subject5"},
                {"activity1", "activity2", "activity3", "activity4", "activity5"},
                {"none", "info1", "info2", "info3", "info4"}
        };

        return config;
    }

    public static String[] tagList(int index) {
        String[] config;
        config = FileUtil.readConfig();
        if (config == null) {
            String[][] a = defaultConfig();
            return a[index];
        }

        String[] tagList = config[index].split(",");
        for (int i = 0; i < config.length; i++) {
            config[i] = config[i].trim().replace(' ','_');
        }

        return tagList;
    }

}
