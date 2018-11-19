package id.usup.roomwordsample.sycn;

import android.app.Application;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.util.Log;

import id.usup.roomwordsample.Word;
import id.usup.roomwordsample.WordViewModel;
import id.usup.roomwordsample.utilities.PreferenceUtilities;

public class ReminderTasks {

    private static final String TAG="REminder";

    // TODO (2) Create a public static constant String called ACTION_INCREMENT_WATER_COUNT
    public static final String ACTION_INCREMENT_WATER_COUNT="increment-water-count";

    // TODO (6) Create a public static void method called executeTask
    public static void executeTask(Context context, String action, String result, String time) {
        Log.d(TAG, "executeTask: test");

        Word word = new Word(result,time);
        word.setWord(result);
        word.setTime(time);

// TODO (7) Add a Context called context and String parameter called action to the parameter list

// TODO (8) If the action equals ACTION_INCREMENT_WATER_COUNT, call this class's incrementWaterCount
//        if (ACTION_INCREMENT_WATER_COUNT.equals(action)){
//            saveData(context,result,time);
//        }

    }
    // TODO (3) Create a private static void method called incrementWaterCount
    private static void saveData(Context context, String result, String time) {
// TODO (4) Add a Context called context to the argument list
// TODO (5) From incrementWaterCount, call the PreferenceUtility method that will ultimately update the water count
        PreferenceUtilities.saveBackGround(context,result, time);
    }
}

