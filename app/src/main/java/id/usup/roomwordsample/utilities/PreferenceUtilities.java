package id.usup.roomwordsample.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import id.usup.roomwordsample.Word;

public class PreferenceUtilities {
    private final static String TAG="PreferenceUtilities";

    public static final String KEY_WATER_COUNT = "key";
    public static final String KEY_CHARGING_REMINDER_COUNT = "hitung";

    private static final int DEFAULT_COUNT = 0;
    private static Word word;

    synchronized private static void setWaterCount(Context context, int glassesOfWater) {
        Log.d(TAG, "setWaterCount: setWater Count");
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(KEY_WATER_COUNT, glassesOfWater);
        editor.apply();
    }

    public static int getWaterCount(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        int glassesOfWater = prefs.getInt(KEY_WATER_COUNT, DEFAULT_COUNT);
        return glassesOfWater;
    }

    synchronized public static void saveBackGround(Context context, String result, String time) {
        //int waterCount = PreferenceUtilities.getWaterCount(context);
        //PreferenceUtilities.setWaterCount(context, ++waterCount);
        word = new Word(result,time);
    }

    synchronized public static void test(Context context){
        //Toast.makeText(context, "testing",Toast.LENGTH_LONG).show();
        Log.d(TAG, "test: test ====================");
    }

    synchronized public static void incrementChargingReminderCount(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        int chargingReminders = prefs.getInt(KEY_CHARGING_REMINDER_COUNT, DEFAULT_COUNT);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(KEY_CHARGING_REMINDER_COUNT, ++chargingReminders);
        editor.apply();
    }

    public static int getChargingReminderCount(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        int chargingReminders = prefs.getInt(KEY_CHARGING_REMINDER_COUNT, DEFAULT_COUNT);
        return chargingReminders;
    }
}
