package id.usup.roomwordsample.background;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

public class ReminderTask {
    public static final String ACTION_WORD="word";
    public static final String ACTION_TIME="time";


    public static void executeTask(Context context, String action){
        // apa yang akan dilakukan
//        Intent replyIntent = new Intent();
//
//            Calendar cal = Calendar.getInstance();
//            Date date = cal.getTime();
//            DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
//            String formatDate = dateFormat.format(date);
//            String word = "TEST";
//            replyIntent.putExtra(ACTION_WORD, word);
//            replyIntent.putExtra(ACTION_TIME, formatDate);
//           // setResult(RESULT_OK, replyIntent);

        if (ACTION_TIME.equals(action)){
            issueSaveData(context);
        }

    }

    private static void issueSaveData(Context context) {

        String time = getTime();
    }

    private static String getTime(){
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        String formatDate = dateFormat.format(date);
        return formatDate;
    }
}
