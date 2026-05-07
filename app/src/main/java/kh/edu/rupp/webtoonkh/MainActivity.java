package kh.edu.rupp.webtoonkh;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import kh.edu.rupp.webtoonkh.adapter.WebtoonAdapter;
import kh.edu.rupp.webtoonkh.api.ApiService;
import kh.edu.rupp.webtoonkh.api.RetrofitClient;
import kh.edu.rupp.webtoonkh.model.Webtoon;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    RecyclerView recyclerWebtoon;
    RecyclerView recyclerTopPicks;

    ImageView imgFeatured;

    TextView txtFeaturedTitle;
    TextView txtFeaturedAuthor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerWebtoon = findViewById(R.id.recyclerWebtoon);
        recyclerTopPicks = findViewById(R.id.recyclerTopPicks);

        imgFeatured = findViewById(R.id.imgFeatured);

        txtFeaturedTitle = findViewById(R.id.txtFeaturedTitle);
        txtFeaturedAuthor = findViewById(R.id.txtFeaturedAuthor);

        // Trending Horizontal
        recyclerWebtoon.setLayoutManager(
                new androidx.recyclerview.widget.LinearLayoutManager(
                        this,
                        androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL,
                        false
                )
        );

        // Top Picks Grid
        recyclerTopPicks.setLayoutManager(
                new GridLayoutManager(this, 3)
        );

        loadWebtoons();
    }

    private void loadWebtoons() {

        ApiService apiService = RetrofitClient
                .getClient()
                .create(ApiService.class);

        Call<List<Webtoon>> call = apiService.getWebtoons();

        call.enqueue(new Callback<List<Webtoon>>() {

            @Override
            public void onResponse(@NonNull Call<List<Webtoon>> call,
                                   @NonNull Response<List<Webtoon>> response) {

                if (response.isSuccessful() && response.body() != null) {

                    List<Webtoon> webtoonList = response.body();

                    // Featured Banner
                    if (!webtoonList.isEmpty()) {

                        Webtoon featured = webtoonList.get(0);

                        txtFeaturedTitle.setText(featured.getTitle());

                        txtFeaturedAuthor.setText(featured.getAuthor());

                        Glide.with(MainActivity.this)
                                .load(featured.getCover_url())
                                .into(imgFeatured);
                    }

                    // Trending Horizontal
                    WebtoonAdapter adapter =
                            new WebtoonAdapter(webtoonList);

                    recyclerWebtoon.setAdapter(adapter);

                    // Top Picks Grid
                    WebtoonAdapter topPickAdapter =
                            new WebtoonAdapter(webtoonList);

                    recyclerTopPicks.setAdapter(topPickAdapter);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Webtoon>> call,
                                  @NonNull Throwable t) {

                Log.e(TAG, "Failed to load webtoons", t);
            }
        });
    }
}