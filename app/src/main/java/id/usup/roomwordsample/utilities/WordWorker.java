package id.usup.roomwordsample.utilities;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import androidx.work.Worker;
import androidx.work.WorkerParameters;
import id.usup.roomwordsample.Word;
import id.usup.roomwordsample.WordDao;
import id.usup.roomwordsample.WordRepository;
import id.usup.roomwordsample.WordRoomDatabase;

import static id.usup.roomwordsample.WordRepository.insertData;
import static id.usup.roomwordsample.WordViewModel.insert;
import static id.usup.roomwordsample.WordViewModel.saveText;


public class WordWorker extends Worker {
    private static final String TAG = "WordWorker";
    private int count;
    public static final String USERNAME = "username";
    public static final String TIME = "time";

    private WordRepository mRepository;

    private WordDao mWordDao;
    private Word word;


    public WordWorker(@NonNull Context application, @NonNull WorkerParameters workerParams) {
        super(application, workerParams);
        Log.d(TAG, "WordWorker: "+workerParams);
        WordRoomDatabase db = WordRoomDatabase.getDatabase(application);
        mWordDao = db.wordDao();
    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            Log.d(TAG, "nilai coutn: "+count);

            String i = String.valueOf(count);
            String user = getInputData().getString(USERNAME);
            String time = getInputData().getString(TIME);
            word = new Word(user, time);
            mWordDao.insert(word);


            Log.d(TAG, "doWork: on going");
            Log.d(TAG, "doWork: nilai i: " + count);
            System.out.println("nilai i: " + count);


            insertData();
            count = count + 1;
            return Result.SUCCESS;
        }catch (Exception e){
            return Result.FAILURE;
        }

    }

}
