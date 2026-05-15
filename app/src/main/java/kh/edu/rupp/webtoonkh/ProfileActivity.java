package kh.edu.rupp.webtoonkh;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class ProfileActivity extends AppCompatActivity {

    ImageView imgBookmarkCover, imgReadLaterCover, imgHistoryCover;
    TextView txtBookmarkTitle, txtReadLaterTitle, txtHistoryTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        imgBookmarkCover = findViewById(R.id.imgBookmarkCover);
        imgReadLaterCover = findViewById(R.id.imgReadLaterCover);
        imgHistoryCover = findViewById(R.id.imgHistoryCover);

        txtBookmarkTitle = findViewById(R.id.txtBookmarkTitle);
        txtReadLaterTitle = findViewById(R.id.txtReadLaterTitle);
        txtHistoryTitle = findViewById(R.id.txtHistoryTitle);

        loadProfileData();
    }

    private void loadProfileData() {
        SharedPreferences prefs = getSharedPreferences("profile_storage", MODE_PRIVATE);

        String bookmarkTitle = prefs.getString("bookmark_title", "No bookmark yet");
        String bookmarkCover = prefs.getString("bookmark_cover_url", "");

        String readLaterTitle = prefs.getString("read_later_title", "No read later yet");
        String readLaterCover = prefs.getString("read_later_cover_url", "");

        String historyTitle = prefs.getString("history_title", "No history yet");
        String historyCover = prefs.getString("history_cover_url", "");

        txtBookmarkTitle.setText(bookmarkTitle);
        txtReadLaterTitle.setText(readLaterTitle);
        txtHistoryTitle.setText(historyTitle);

        if (!bookmarkCover.isEmpty()) {
            Glide.with(this)
                    .load(bookmarkCover)
                    .into(imgBookmarkCover);
        }

        if (!readLaterCover.isEmpty()) {
            Glide.with(this)
                    .load(readLaterCover)
                    .into(imgReadLaterCover);
        }

        if (!historyCover.isEmpty()) {
            Glide.with(this)
                    .load(historyCover)
                    .into(imgHistoryCover);
        }
    }
}