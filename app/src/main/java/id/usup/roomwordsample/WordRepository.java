package id.usup.roomwordsample;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import id.usup.roomwordsample.utilities.WordWorker;

/**
 * Abstracted Repository as promoted by the Architecture Guide.
 * https://developer.android.com/topic/libraries/architecture/guide.html
 */

public class WordRepository {
    private static int i = 1;

    private static final String TAG="WordRepository";

    private WordDao mWordDao;
    private LiveData<List<Word>> mAllWords;

    // Note that in order to unit test the WordRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    public WordRepository(Application application) {
        WordRoomDatabase db = WordRoomDatabase.getDatabase(application);
        mWordDao = db.wordDao();
        mAllWords = mWordDao.getAlphabetizedWords();
    }



    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    LiveData<List<Word>> getAllWords() {
        return mAllWords;
    }

    // You must call this on a non-UI thread or your app will crash.
    // Like this, Room ensures that you're not doing any long running operations on the main
    // thread, blocking the UI.
    void insert(Word word) {
        new insertAsyncTask(mWordDao).execute(word);
    }

    public static class insertAsyncTask extends AsyncTask<Word, Void, Void> {

        private WordDao mAsyncTaskDao;

        insertAsyncTask(WordDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Word... params) {
            Log.d(TAG, "doInBackground: param test"+params);
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

   public static void insertData(){
        Log.d(TAG, "insertData: "+i);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm:ss");
        String strDate = mdformat.format(calendar.getTime());
        Data data = new Data.Builder()
                .putString(WordWorker.USERNAME,"username")
                .putString(WordWorker.TIME,strDate)
                .build();

        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(WordWorker.class)
                .setInitialDelay(1, TimeUnit.SECONDS)
                .addTag("save")
                .setInputData(data)
                .build();

        i++;
       //Log.d(TAG, "insertData: "+i);
        WorkManager.getInstance().enqueue(workRequest);

    }
}