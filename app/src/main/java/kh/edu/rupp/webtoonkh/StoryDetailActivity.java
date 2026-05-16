package kh.edu.rupp.webtoonkh;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import kh.edu.rupp.webtoonkh.adapter.ChapterAdapter;
import kh.edu.rupp.webtoonkh.api.ApiService;
import kh.edu.rupp.webtoonkh.api.RetrofitClient;
import kh.edu.rupp.webtoonkh.model.Chapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.content.SharedPreferences;
import android.widget.Toast;

public class StoryDetailActivity extends AppCompatActivity {

    private static final String TAG = "StoryDetailActivity";

    ImageView imgDetailCover;
    TextView txtDetailTitle, txtDetailAuthor, txtDetailGenre, txtDetailDescription;
    RecyclerView recyclerChapters;
    String title, author, genre, description, coverUrl;

    int webtoonId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_detail);

        imgDetailCover = findViewById(R.id.imgDetailCover);
        txtDetailTitle = findViewById(R.id.txtDetailTitle);
        txtDetailAuthor = findViewById(R.id.txtDetailAuthor);
        txtDetailGenre = findViewById(R.id.txtDetailGenre);
        txtDetailDescription = findViewById(R.id.txtDetailDescription);
        recyclerChapters = findViewById(R.id.recyclerChapters);

        recyclerChapters.setLayoutManager(new LinearLayoutManager(this));

        webtoonId = getIntent().getIntExtra("webtoon_id", -1);

        title = getIntent().getStringExtra("title");
        author = getIntent().getStringExtra("author");
        genre = getIntent().getStringExtra("genre");
        description = getIntent().getStringExtra("description");
        coverUrl = getIntent().getStringExtra("cover_url");

        txtDetailTitle.setText(title);
        txtDetailAuthor.setText(author);
        txtDetailGenre.setText(genre);
        txtDetailDescription.setText(description);

        Glide.with(this)
                .load(coverUrl)
                .into(imgDetailCover);

        loadChapters();
        saveHistory();

        findViewById(R.id.btnReadLater).setOnClickListener(v -> {
            saveStory("read_later");
            Toast.makeText(this, "Added to Read Later", Toast.LENGTH_SHORT).show();
        });

        findViewById(R.id.btnBookmark).setOnClickListener(v -> {
            saveStory("bookmark");
            Toast.makeText(this, "Added to Bookmark", Toast.LENGTH_SHORT).show();
        });
    }

    private void loadChapters() {
        ApiService apiService = RetrofitClient
                .getClient()
                .create(ApiService.class);

        String filter = "eq." + webtoonId;

        Call<List<Chapter>> call = apiService.getChaptersByWebtoon(filter);

        call.enqueue(new Callback<List<Chapter>>() {
            @Override
            public void onResponse(@NonNull Call<List<Chapter>> call,
                                   @NonNull Response<List<Chapter>> response) {

                if (response.isSuccessful() && response.body() != null) {
                    ChapterAdapter adapter = new ChapterAdapter(response.body());
                    recyclerChapters.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Chapter>> call,
                                  @NonNull Throwable t) {
                Log.e(TAG, "Failed to load chapters", t);
            }

        });
    }
    private void saveStory(String keyPrefix) {
        SharedPreferences prefs = getSharedPreferences("profile_storage", MODE_PRIVATE);

        String oldData = prefs.getString(keyPrefix + "_list", "");

        String newItem = webtoonId + "|" + title + "|" + author + "|" + genre + "|" + description + "|" + coverUrl;

        if (!oldData.contains(webtoonId + "|")) {
            if (oldData.isEmpty()) {
                oldData = newItem;
            } else {
                oldData = newItem + ";;" + oldData;
            }
        }

        prefs.edit()
                .putString(keyPrefix + "_list", oldData)
                .putInt(keyPrefix + "_id", webtoonId)
                .putString(keyPrefix + "_title", title)
                .putString(keyPrefix + "_author", author)
                .putString(keyPrefix + "_genre", genre)
                .putString(keyPrefix + "_description", description)
                .putString(keyPrefix + "_cover_url", coverUrl)
                .apply();
    }
    private void saveHistory() {
        SharedPreferences prefs = getSharedPreferences("profile_storage", MODE_PRIVATE);

        String oldData = prefs.getString("history_list", "");

        String newItem = webtoonId + "|" + title + "|" + author + "|" + genre + "|" + description + "|" + coverUrl;

        String updatedData;

        if (oldData.contains(webtoonId + "|")) {
            updatedData = oldData;
        } else {
            if (oldData.isEmpty()) {
                updatedData = newItem;
            } else {
                updatedData = newItem + ";;" + oldData;
            }
        }

        prefs.edit()
                .putString("history_list", updatedData)
                .putInt("history_id", webtoonId)
                .putString("history_title", title)
                .putString("history_author", author)
                .putString("history_genre", genre)
                .putString("history_description", description)
                .putString("history_cover_url", coverUrl)
                .apply();
    }
}