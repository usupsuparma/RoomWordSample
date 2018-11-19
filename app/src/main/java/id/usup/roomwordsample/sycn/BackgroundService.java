package id.usup.roomwordsample.sycn;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;

import id.usup.roomwordsample.Word;
import id.usup.roomwordsample.WordDao;
import id.usup.roomwordsample.WordRoomDatabase;
import id.usup.roomwordsample.WordViewModel;

public class BackgroundService extends JobService {
    WordRoomDatabase db;
    private WordDao wordDao;
    private static final String TAG="Service";
    private boolean jobCancelled = false;
    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d(TAG, "onStartJob: job Started");
        doBackgroundProses(params);
        return true;
    }

    private void doBackgroundProses(final JobParameters params) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                //while (true) {
                    for (int i = 0; i < 10; i++) {
                        Log.d(TAG, "run: " + i);
                        if (jobCancelled) {
                            return;
                        }
                        try {
                            Log.d(TAG, "finish");

                            String mWord = (String) params.getExtras().get("word");
                            String time = (String) params.getExtras().get("time");

                            Log.d(TAG, "run: "+mWord);
                            Log.d(TAG, "run: "+time);
                            Word word = new Word(mWord,time);
                            insertData(word);


                            //mWordViewModel.insert(word);
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                   // }
                }
            }
        }).start();
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.d(TAG, "Job cancelled before completion");
        jobCancelled = true;
        return true;
    }

    private void insertData(final Word word){
        new AsyncTask<Void, Void, Long>() {
            @Override
            protected Long doInBackground(Void... voids) {
               long status = db.wordDao().insert(word);
                return status;
            }

            @Override
            protected void onPostExecute(Long aLong) {
                Log.d(TAG, "onPostExecute: "+aLong);
                super.onPostExecute(aLong);
            }
        }.execute();

    }
}
