package kh.edu.rupp.webtoonkh;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

public class CategoryActivity extends AppCompatActivity {

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

                    List<Webtoon> allWebtoons = response.body();

                    List<Webtoon> dramaList = new ArrayList<>();
                    List<Webtoon> romanceList = new ArrayList<>();
                    List<Webtoon> fantasyList = new ArrayList<>();
                    List<Webtoon> actionList = new ArrayList<>();

                    for (Webtoon webtoon : allWebtoons) {
                        String genre = webtoon.getGenre();

                        if (genre == null) continue;

                        if (genre.equalsIgnoreCase("Drama")) {
                            dramaList.add(webtoon);
                        } else if (genre.equalsIgnoreCase("Romance")) {
                            romanceList.add(webtoon);
                        } else if (genre.equalsIgnoreCase("Fantasy")) {
                            fantasyList.add(webtoon);
                        } else if (genre.equalsIgnoreCase("Action")) {
                            actionList.add(webtoon);
                        }
                    }

                    recyclerDrama.setAdapter(new WebtoonAdapter(dramaList));
                    recyclerRomance.setAdapter(new WebtoonAdapter(romanceList));
                    recyclerFantasy.setAdapter(new WebtoonAdapter(fantasyList));
                    recyclerAction.setAdapter(new WebtoonAdapter(actionList));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Webtoon>> call,
                                  @NonNull Throwable t) {
                Log.e(TAG, "Failed to load category data", t);
            }
        });
    }
}