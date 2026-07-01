package kh.edu.rupp.webtoonkh;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import kh.edu.rupp.webtoonkh.adapter.WebtoonAdapter;
import kh.edu.rupp.webtoonkh.api.ApiService;
import kh.edu.rupp.webtoonkh.api.RetrofitClient;
import kh.edu.rupp.webtoonkh.model.Webtoon;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryActivity extends BaseNavigationActivity {

    private static final String TAG = "CategoryActivity";

    RecyclerView recyclerDrama;
    RecyclerView recyclerRomance;
    RecyclerView recyclerFantasy;
    RecyclerView recyclerAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        recyclerDrama = findViewById(R.id.recyclerDrama);
        recyclerRomance = findViewById(R.id.recyclerRomance);
        recyclerFantasy = findViewById(R.id.recyclerFantasy);
        recyclerAction = findViewById(R.id.recyclerAction);

        setupFloatingBottomNavigation(1);

        recyclerDrama.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerRomance.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerFantasy.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerAction.setLayoutManager(new GridLayoutManager(this, 3));

        loadCategoryData();
    }

    private void loadCategoryData() {

        ApiService apiService = RetrofitClient
                .getClient()
                .create(ApiService.class);

        Call<List<Webtoon>> call = apiService.getWebtoons();

        call.enqueue(new Callback<List<Webtoon>>() {
            @Override
            public void onResponse(@NonNull Call<List<Webtoon>> call,
                                   @NonNull Response<List<Webtoon>> response) {

                if (response.isSuccessful() && response.body() != null) {
                    List<Webtoon> allWebtoons = new ArrayList<>(response.body());

                    if (allWebtoons.isEmpty()) {
                        allWebtoons = createFallbackWebtoons();
                    }

                    displayCategoryData(allWebtoons);
                } else {
                    Log.e(TAG, "Category request failed: " + response.code());
                    displayCategoryData(createFallbackWebtoons());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Webtoon>> call,
                                  @NonNull Throwable t) {
                Log.e(TAG, "Failed to load category data", t);
                displayCategoryData(createFallbackWebtoons());
            }
        });
    }

    private void displayCategoryData(List<Webtoon> allWebtoons) {
        List<Webtoon> dramaList = new ArrayList<>();
        List<Webtoon> romanceList = new ArrayList<>();
        List<Webtoon> fantasyList = new ArrayList<>();
        List<Webtoon> actionList = new ArrayList<>();

        for (Webtoon webtoon : allWebtoons) {
            String genre = webtoon.getGenre();

            if (genre == null) {
                continue;
            }

            String normalizedGenre = genre.toLowerCase();

            if (normalizedGenre.contains("drama")) {
                dramaList.add(webtoon);
            }
            if (normalizedGenre.contains("romance")) {
                romanceList.add(webtoon);
            }
            if (normalizedGenre.contains("fantasy")) {
                fantasyList.add(webtoon);
            }
            if (normalizedGenre.contains("action")) {
                actionList.add(webtoon);
            }
        }

        recyclerDrama.setAdapter(new WebtoonAdapter(dramaList));
        recyclerRomance.setAdapter(new WebtoonAdapter(romanceList));
        recyclerFantasy.setAdapter(new WebtoonAdapter(fantasyList));
        recyclerAction.setAdapter(new WebtoonAdapter(actionList));
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
                "Drama, Romance",
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
                "Action, Fantasy",
                "https://chcfaoytsjbnbpiwfgay.supabase.co/storage/v1/object/public/webtoon-images/cover4.jpg",
                "A college romance story.",
                ""
        ));

        return webtoons;
    }
}
