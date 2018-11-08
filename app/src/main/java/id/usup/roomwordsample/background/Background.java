package id.usup.roomwordsample.background;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

public class Background extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public Background(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent.getAction();
        ReminderTask.executeTask(this,action);

    }
}
