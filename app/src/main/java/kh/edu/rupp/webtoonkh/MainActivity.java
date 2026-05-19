package kh.edu.rupp.webtoonkh;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import kh.edu.rupp.webtoonkh.adapter.WebtoonAdapter;
import kh.edu.rupp.webtoonkh.api.ApiService;
import kh.edu.rupp.webtoonkh.api.RetrofitClient;
import kh.edu.rupp.webtoonkh.model.Webtoon;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseNavigationActivity {

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

        setupFloatingBottomNavigation(0);
        setupClickListeners();

        recyclerWebtoon.setLayoutManager(
                new androidx.recyclerview.widget.LinearLayoutManager(
                        this,
                        androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL,
                        false
                )
        );

        recyclerTopPicks.setLayoutManager(new GridLayoutManager(this, 3));

        loadWebtoons();
    }

    private void setupClickListeners() {
        findViewById(R.id.btnProfile).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(intent);
        });

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
                    displayWebtoons(response.body());
                } else {
                    Log.e(TAG, "Webtoon request failed: " + response.code());
                    displayWebtoons(createFallbackWebtoons());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Webtoon>> call,
                                  @NonNull Throwable t) {
                Log.e(TAG, "Failed to load webtoons", t);
                displayWebtoons(createFallbackWebtoons());
            }
        });
    }

    private void displayWebtoons(List<Webtoon> webtoonList) {
        if (webtoonList.isEmpty()) {
            webtoonList = createFallbackWebtoons();
        }

        Webtoon featured = webtoonList.get(0);

        txtFeaturedTitle.setText(featured.getTitle());
        txtFeaturedAuthor.setText(featured.getAuthor());

        Glide.with(MainActivity.this)
                .load(featured.getCover_url())
                .centerCrop()
                .into(imgFeatured);

        recyclerWebtoon.setAdapter(new WebtoonAdapter(webtoonList));
        recyclerTopPicks.setAdapter(new WebtoonAdapter(webtoonList));
    }

    private List<Webtoon> createFallbackWebtoons() {
        List<Webtoon> webtoons = new ArrayList<>();

        webtoons.add(new Webtoon(
                1,
                "A Decade of Us",
                "Unknown",
                "Drama",
                "https://chcfaoytsjbnbpiwfgay.supabase.co/storage/v1/object/public/webtoon-images/cover1.jpg",
                "A romantic drama story.",
                ""
        ));

        webtoons.add(new Webtoon(
                2,
                "Cry, or Better Yet, Beg",
                "VAN.J, Solche",
                "Drama",
                "https://chcfaoytsjbnbpiwfgay.supabase.co/storage/v1/object/public/webtoon-images/cover2.jpg",
                "A dramatic fantasy romance story.",
                ""
        ));

        webtoons.add(new Webtoon(
                3,
                "Tears on a Withered Flower",
                "Unknown",
                "Romance",
                "https://chcfaoytsjbnbpiwfgay.supabase.co/storage/v1/object/public/webtoon-images/cover3.jpg",
                "Slow burn romance story.",
                ""
        ));

        webtoons.add(new Webtoon(
                4,
                "Childhood Friend Complex",
                "Unknown",
                "Romance",
                "https://chcfaoytsjbnbpiwfgay.supabase.co/storage/v1/object/public/webtoon-images/cover4.jpg",
                "A college romance story.",
                ""
        ));

        return webtoons;
    }
}
