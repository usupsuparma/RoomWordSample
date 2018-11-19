package id.usup.roomwordsample;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import id.usup.roomwordsample.utilities.WordWorker;

public class WordViewModel extends AndroidViewModel {
    private static final String TAG = "WordViewModel";
    private static int i = 1;
    private static Count c = new Count(i);

    private static WordRepository mRepository;
    // Using LiveData and caching what getAlphabetizedWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    private LiveData<List<Word>> mAllWords;

    public WordViewModel(Application application) {
        super(application);
        mRepository = new WordRepository(application);
        mAllWords = mRepository.getAllWords();

    }

    LiveData<List<Word>> getAllWords() {
        return mAllWords;
    }

    public static void insert(Word word) {
        Log.d(TAG, "insert: " + word);
        mRepository.insert(word);
    }

    public static void saveText() {
        Log.d(TAG, "startWorker: WordViewModel::StartWorker()");
        mRepository.insertData();
        // constraints digunakan untuk membatasi
        //Constraints constraints = new Constraints.Builder().build();


    }


}