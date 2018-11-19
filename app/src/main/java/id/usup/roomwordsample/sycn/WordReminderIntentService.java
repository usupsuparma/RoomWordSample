package id.usup.roomwordsample.sycn;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class WordReminderIntentService extends IntentService {
    private final static String TAG="wordReminder";


    public WordReminderIntentService() {
        super("WordReminderIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "onHandleIntent: water Reminder intent service");
//      TODO (12) Get the action from the Intent that started this Service
        String action = intent.getAction();
        String result = intent.getStringExtra("word");
        String time = intent.getStringExtra("time");
//      TODO (13) Call ReminderTasks.executeTaskForTag and pass in the action to be performed
        ReminderTasks.executeTask(this, action,result,time);

    }
}
