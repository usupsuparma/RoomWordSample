package id.usup.roomwordsample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class NewWordActivity extends AppCompatActivity {

    public static final String EXTRA_REPLY = "com.example.android.wordlistsql.REPLY";
    public static final String EXTRA_TIME = "com.example.android.wordlistsql.TIME";

    private EditText mEditWordView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_word);
        mEditWordView = findViewById(R.id.edit_word);

        final Button button = findViewById(R.id.button_save);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent replyIntent = new Intent();
                if (TextUtils.isEmpty(mEditWordView.getText())) {
                    setResult(RESULT_CANCELED, replyIntent);
                } else {
                    Calendar cal = Calendar.getInstance();
                    Date date = cal.getTime();
                    DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
                    String formatDate = dateFormat.format(date);
                    String word = mEditWordView.getText().toString();
                    replyIntent.putExtra(EXTRA_REPLY, word);
                    replyIntent.putExtra(EXTRA_TIME, formatDate);
                    setResult(RESULT_OK, replyIntent);
                }
                finish();
            }
        });
    }
}