package kh.edu.rupp.webtoonkh;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ReaderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int chapterId = getIntent().getIntExtra("chapter_id", -1);

        TextView textView = new TextView(this);
        textView.setText("Reader Page\nChapter ID: " + chapterId);
        textView.setTextSize(24);
        textView.setGravity(android.view.Gravity.CENTER);

        setContentView(textView);
    }
}