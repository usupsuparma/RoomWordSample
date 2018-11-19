package id.usup.roomwordsample.utilities;


import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class WorkKillManager extends Worker {
    public WorkKillManager(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d("woker", "doWork: dipanggil ondestroy");

        return Result.SUCCESS;
    }
}
