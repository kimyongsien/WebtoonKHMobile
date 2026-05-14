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

public class StoryDetailActivity extends AppCompatActivity {

    private static final String TAG = "StoryDetailActivity";

    ImageView imgDetailCover;
    TextView txtDetailTitle, txtDetailAuthor, txtDetailGenre, txtDetailDescription;
    RecyclerView recyclerChapters;

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

        String title = getIntent().getStringExtra("title");
        String author = getIntent().getStringExtra("author");
        String genre = getIntent().getStringExtra("genre");
        String description = getIntent().getStringExtra("description");
        String coverUrl = getIntent().getStringExtra("cover_url");

        txtDetailTitle.setText(title);
        txtDetailAuthor.setText(author);
        txtDetailGenre.setText(genre);
        txtDetailDescription.setText(description);

        Glide.with(this)
                .load(coverUrl)
                .into(imgDetailCover);

        loadChapters();
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
}