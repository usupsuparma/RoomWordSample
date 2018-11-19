package id.usup.roomwordsample;

import android.app.Service;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import id.usup.roomwordsample.sycn.BackgroundService;
import id.usup.roomwordsample.utilities.WakeLocker;
import id.usup.roomwordsample.utilities.WordWorker;


public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    public static final int NEW_WORD_ACTIVITY_REQUEST_CODE = 1;
    private static final String TAG = "MainActivity";
    private static final long START_TIME_IN_MILLIS = 5000;

    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;
    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;


    private WordViewModel mWordViewModel;
    IntentFilter mTimeIntentFilter;
    private Word mWord;
    private RecyclerView recyclerView;
    private WordListAdapter adapter;

    private WordRoomDatabase db;

    private TextView textView;
    private int counter = 0;
    private Button mStartWorker, mCancelWorker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        textView = findViewById(R.id.tv_counter);
        mStartWorker = findViewById(R.id.bt_begin);
        mCancelWorker = findViewById(R.id.bt_canc);

        mStartWorker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mStartWorker.setVisibility(View.INVISIBLE);
                mCancelWorker.setVisibility(View.VISIBLE);

                mWordViewModel.saveText();
            }
        });

        mCancelWorker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cancelWork();
                mCancelWorker.setVisibility(View.INVISIBLE);
                mStartWorker.setVisibility(View.VISIBLE);
            }
        });


        recyclerView = findViewById(R.id.recyclerview);
        recycle();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, NewWordActivity.class);
                startActivityForResult(intent, NEW_WORD_ACTIVITY_REQUEST_CODE);
            }
        });
//        Word word = new Word();
//        word.setTime("aku");
//        word.setWord("test");
//        insertData(word);
        //proses();

        //startTimer();

    }



    public static void cancelWork(){
        Log.d(TAG, "cancelWork: di cancel");

        WorkManager workManager = WorkManager.getInstance();
        workManager.cancelAllWork();
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_WORD_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            String kata = data.getStringExtra(NewWordActivity.EXTRA_REPLY);
            String waktu = data.getStringExtra(NewWordActivity.EXTRA_TIME);
            Word word = new Word(kata, waktu);
            mWordViewModel.insert(word);

        } else {
            Toast.makeText(
                    getApplicationContext(),
                    R.string.empty_not_saved,
                    Toast.LENGTH_LONG).show();
            recycle();
        }
    }

    @Override
    protected void onResume() {

        super.onResume();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: work on destroy");
        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(WordWorker.class)
                .setInitialDelay(5, TimeUnit.SECONDS).build();
        WorkManager.getInstance().beginWith(workRequest);
        super.onDestroy();

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }


    //save data ke database
    private String saveData() {

        String word = "test";
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        String formatDate = dateFormat.format(date);
//        mWord = new Word(word,formatDate);
//        mWordViewModel.insert(mWord);
        Log.d(TAG, "saveData: " + word + " " + formatDate);
        return formatDate;


    }


    public void recycle() {
        adapter = new WordListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Get a new or existing ViewModel from the ViewModelProvider.
        mWordViewModel = ViewModelProviders.of(this).get(WordViewModel.class);

        // Add an observer on the LiveData returned by getAlphabetizedWords.
        // The onChanged() method fires when the observed data changes and the activity is
        // in the foreground.
        mWordViewModel.getAllWords().observe(this, new Observer<List<Word>>() {
            @Override
            public void onChanged(@Nullable final List<Word> words) {
                // Update the cached copy of the words in the adapter.
                adapter.setWords(words);
            }
        });
    }

    private void proses() {
        ComponentName componentName = new ComponentName(MainActivity.this, BackgroundService.class);
        PersistableBundle bundle = new PersistableBundle();
        String time = saveData();
        bundle.putString("word", "hello");
        bundle.putString("time", time);
        JobInfo info = new JobInfo.Builder(123, componentName)
                .setRequiresCharging(true)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                .setPersisted(true)
                .setExtras(bundle)
                .setPeriodic(15 * 60 * 1000)
                .build();

        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        int resultCode = scheduler.schedule(info);
        if (resultCode == JobScheduler.RESULT_SUCCESS) {
            Log.d(TAG, "Job scheduled");
            // Get a new or existing ViewModel from the ViewModelProvider.
            //recycle();
        } else {
            Log.d(TAG, "Job scheduling failed");
        }
    }

    private void insertData(final Word word) {
        Log.d(TAG, "insertData: " + word.getTime());
        Log.d(TAG, "insertData: " + word.getWord());

        new AsyncTask<Void, Void, Long>() {
            @Override
            protected Long doInBackground(Void... voids) {

                long status = db.wordDao().insert(word);
                return status;
            }

            @Override
            protected void onPostExecute(Long aLong) {
                Log.d(TAG, "onPostExecute: " + aLong);
                super.onPostExecute(aLong);
            }
        }.execute();

    }
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Waking up mobile if it is sleeping
            WakeLocker.acquire(getApplicationContext());
            // do something
            WakeLocker.release();
        }
    };


    private void startTimer() {

        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
                String c = String.valueOf(counter);
                String test = "test " + c;
                mWord = new Word(test, c);

            }

            @Override
            public void onFinish() {
                mTimerRunning = true;


                mWordViewModel.insert(mWord);
                counter++;
                resetTimer();

            }
        }.start();

        mTimerRunning = false;

    }

    private void pauseTimer() {
        //mCountDownTimer.cancel();
        mTimerRunning = false;
//        mButtonStartPause.setText("Start");
//        mButtonReset.setVisibility(View.VISIBLE);
    }

    private void resetTimer() {
        mTimeLeftInMillis = 5000;
        updateCountDownText();
//        mButtonReset.setVisibility(View.INVISIBLE);
//        mButtonStartPause.setVisibility(View.VISIBLE);
        mCountDownTimer.start();
    }

    private void updateCountDownText() {
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        textView.setText(timeLeftFormatted);
    }
}
