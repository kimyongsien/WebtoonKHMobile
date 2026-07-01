package kh.edu.rupp.webtoonkh;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
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

public class SearchActivity extends BaseNavigationActivity {

    private static final String TAG = "SearchActivity";

    private EditText inputSearch;
    private RecyclerView recyclerPopularWebtoon;
    private final List<Webtoon> webtoonList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        inputSearch = findViewById(R.id.inputSearch);
        recyclerPopularWebtoon = findViewById(R.id.recyclerPopularWebtoon);

        setupFloatingBottomNavigation(3);

        recyclerPopularWebtoon.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        );

        setupSearchActions();
        loadPopularWebtoons();
    }

    private void setupSearchActions() {
        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                displayWebtoons(filterWebtoons(s.toString()));
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        setChipSearch(R.id.chipAction);
        setChipSearch(R.id.chipRomance);
        setChipSearch(R.id.chipHorror);
    }

    private void setChipSearch(int chipId) {
        TextView chip = findViewById(chipId);

        if (chip == null) {
            return;
        }

        chip.setOnClickListener(v -> inputSearch.setText(chip.getText()));
    }

    private void loadPopularWebtoons() {
        ApiService apiService = RetrofitClient
                .getClient()
                .create(ApiService.class);

        Call<List<Webtoon>> call = apiService.getWebtoons();

        call.enqueue(new Callback<List<Webtoon>>() {
            @Override
            public void onResponse(@NonNull Call<List<Webtoon>> call,
                                   @NonNull Response<List<Webtoon>> response) {

                if (response.isSuccessful() && response.body() != null) {
                    webtoonList.clear();
                    webtoonList.addAll(response.body());
                    if (webtoonList.isEmpty()) {
                        webtoonList.addAll(createFallbackWebtoons());
                    }
                    displayWebtoons(filterWebtoons(inputSearch.getText().toString()));
                } else {
                    Log.e(TAG, "Popular webtoon request failed: " + response.code());
                    displayFallbackWebtoons();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Webtoon>> call,
                                  @NonNull Throwable t) {
                Log.e(TAG, "Failed to load popular webtoons", t);
                displayFallbackWebtoons();
            }
        });
    }

    private void displayFallbackWebtoons() {
        webtoonList.clear();
        webtoonList.addAll(createFallbackWebtoons());
        displayWebtoons(filterWebtoons(inputSearch.getText().toString()));
    }

    private List<Webtoon> filterWebtoons(String query) {
        String normalizedQuery = query.trim().toLowerCase();

        if (normalizedQuery.isEmpty()) {
            return webtoonList;
        }

        List<Webtoon> filteredList = new ArrayList<>();

        for (Webtoon webtoon : webtoonList) {
            if (containsQuery(webtoon.getTitle(), normalizedQuery)
                    || containsQuery(webtoon.getAuthor(), normalizedQuery)
                    || containsQuery(webtoon.getGenre(), normalizedQuery)) {
                filteredList.add(webtoon);
            }
        }

        return filteredList;
    }

    private boolean containsQuery(String value, String query) {
        return value != null && value.toLowerCase().contains(query);
    }

    private void displayWebtoons(List<Webtoon> webtoons) {
        recyclerPopularWebtoon.setAdapter(new WebtoonAdapter(webtoons));
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
