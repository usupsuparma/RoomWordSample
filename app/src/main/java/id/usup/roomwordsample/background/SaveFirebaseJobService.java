package id.usup.roomwordsample.background;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.os.AsyncTask;

public class SaveFirebaseJobService extends JobService {
    private AsyncTask mBackgroundTaks;
    @Override
    public boolean onStartJob(final JobParameters params) {
        mBackgroundTaks = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                Context context = SaveFirebaseJobService.this;
                ReminderTask.executeTask(context, ReminderTask.ACTION_TIME);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                jobFinished(params, false);
            }
        };
        mBackgroundTaks.execute();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        if (mBackgroundTaks != null) mBackgroundTaks.cancel(true);
        return true;
    }
}
