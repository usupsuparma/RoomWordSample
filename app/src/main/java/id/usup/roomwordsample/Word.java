package id.usup.roomwordsample;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "word_table")
public class Word {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "word")
    private String mWord;


    @ColumnInfo(name = "time")
    private String mTime;

    public String getTime() {
        return mTime;
    }

    public void setTime(String mTime) {
        this.mTime = mTime;
    }

    public Word(@NonNull String word, String mTime) {
        this.mWord = word;
        this.mTime = mTime;
    }

    @NonNull
    public String getWord() {
        return this.mWord;
    }

    public Word() {
    }

    public void setWord(@NonNull String mWord) {
        this.mWord = mWord;
    }
}
