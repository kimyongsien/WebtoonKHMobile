package kh.edu.rupp.webtoonkh;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import kh.edu.rupp.webtoonkh.adapter.ChapterAdapter;
import kh.edu.rupp.webtoonkh.api.ApiService;
import kh.edu.rupp.webtoonkh.api.RetrofitClient;
import kh.edu.rupp.webtoonkh.model.Chapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoryDetailActivity extends AppCompatActivity {

    private static final String TAG = "StoryDetailActivity";

    ImageView imgDetailCover;
    ImageView btnReadLater, btnBookmark;
    TextView txtDetailTitle, txtDetailAuthor, txtDetailGenre, txtDetailDescription;
    RecyclerView recyclerChapters;
    String title, author, genre, description, coverUrl;

    int webtoonId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_detail);

        imgDetailCover = findViewById(R.id.imgDetailCover);
        btnReadLater = findViewById(R.id.btnReadLater);
        btnBookmark = findViewById(R.id.btnBookmark);
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
        updateSaveButtons();

        btnReadLater.setOnClickListener(v -> {
            if (toggleStory("read_later")) {
                Toast.makeText(this, "Added to Read Later", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Removed from Read Later", Toast.LENGTH_SHORT).show();
            }
            updateSaveButtons();
        });

        btnBookmark.setOnClickListener(v -> {
            if (toggleStory("bookmark")) {
                Toast.makeText(this, "Added to Bookmark", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Removed from Bookmark", Toast.LENGTH_SHORT).show();
            }
            updateSaveButtons();
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
    private boolean toggleStory(String keyPrefix) {
        SharedPreferences prefs = getSharedPreferences("profile_storage", MODE_PRIVATE);

        String oldData = prefs.getString(keyPrefix + "_list", "");

        if (hasSavedStory(oldData)) {
            removeStory(keyPrefix, oldData);
            return false;
        }

        saveStory(keyPrefix, oldData);
        return true;
    }

    private void saveStory(String keyPrefix, String oldData) {
        SharedPreferences prefs = getSharedPreferences("profile_storage", MODE_PRIVATE);
        String newItem = webtoonId + "|" + title + "|" + author + "|" + genre + "|" + description + "|" + coverUrl;

        if (oldData.isEmpty()) {
            oldData = newItem;
        } else {
            oldData = newItem + ";;" + oldData;
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

    private void removeStory(String keyPrefix, String oldData) {
        SharedPreferences prefs = getSharedPreferences("profile_storage", MODE_PRIVATE);
        List<String> remainingItems = new ArrayList<>();

        for (String item : oldData.split(";;")) {
            if (!item.startsWith(webtoonId + "|")) {
                remainingItems.add(item);
            }
        }

        SharedPreferences.Editor editor = prefs.edit()
                .putString(keyPrefix + "_list", joinStoryItems(remainingItems));

        if (remainingItems.isEmpty()) {
            editor.remove(keyPrefix + "_id")
                    .remove(keyPrefix + "_title")
                    .remove(keyPrefix + "_author")
                    .remove(keyPrefix + "_genre")
                    .remove(keyPrefix + "_description")
                    .remove(keyPrefix + "_cover_url");
        } else {
            String[] firstStory = remainingItems.get(0).split("\\|");
            if (firstStory.length >= 6) {
                editor.putInt(keyPrefix + "_id", Integer.parseInt(firstStory[0]))
                        .putString(keyPrefix + "_title", firstStory[1])
                        .putString(keyPrefix + "_author", firstStory[2])
                        .putString(keyPrefix + "_genre", firstStory[3])
                        .putString(keyPrefix + "_description", firstStory[4])
                        .putString(keyPrefix + "_cover_url", firstStory[5]);
            }
        }

        editor.apply();
    }

    private String joinStoryItems(List<String> items) {
        StringBuilder builder = new StringBuilder();

        for (String item : items) {
            if (builder.length() > 0) {
                builder.append(";;");
            }
            builder.append(item);
        }

        return builder.toString();
    }

    private void updateSaveButtons() {
        SharedPreferences prefs = getSharedPreferences("profile_storage", MODE_PRIVATE);

        boolean readLaterSaved = hasSavedStory(prefs.getString("read_later_list", ""));
        boolean bookmarkSaved = hasSavedStory(prefs.getString("bookmark_list", ""));

        updateSaveButton(btnReadLater, readLaterSaved);
        updateSaveButton(btnBookmark, bookmarkSaved);
    }

    private void updateSaveButton(ImageView button, boolean saved) {
        button.setBackgroundResource(saved ? R.drawable.bg_chip_saved : R.drawable.bg_action_pill);
        button.setColorFilter(saved ? Color.rgb(20, 102, 170) : Color.BLACK);
        button.animate()
                .scaleX(saved ? 1.03f : 1f)
                .scaleY(saved ? 1.03f : 1f)
                .setDuration(160)
                .start();
    }

    private boolean hasSavedStory(String data) {
        if (data == null || data.isEmpty()) {
            return false;
        }

        String[] items = data.split(";;");

        for (String item : items) {
            if (item.startsWith(webtoonId + "|")) {
                return true;
            }
        }

        return false;
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
