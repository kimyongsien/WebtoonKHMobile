package kh.edu.rupp.webtoonkh;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import kh.edu.rupp.webtoonkh.adapter.ReaderPageAdapter;
import kh.edu.rupp.webtoonkh.api.ApiService;
import kh.edu.rupp.webtoonkh.api.RetrofitClient;
import kh.edu.rupp.webtoonkh.model.ChapterPage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReaderActivity extends AppCompatActivity {

    private static final String TAG = "ReaderActivity";

    RecyclerView recyclerReaderPages;

    int chapterId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);

        recyclerReaderPages = findViewById(R.id.recyclerReaderPages);

        recyclerReaderPages.setLayoutManager(
                new LinearLayoutManager(this)
        );

        chapterId = getIntent().getIntExtra("chapter_id", -1);

        loadPages();
    }

    private void loadPages() {

        ApiService apiService = RetrofitClient
                .getClient()
                .create(ApiService.class);

        String filter = "eq." + chapterId;

        Call<List<ChapterPage>> call =
                apiService.getPagesByChapter(filter);

        call.enqueue(new Callback<List<ChapterPage>>() {
            @Override
            public void onResponse(@NonNull Call<List<ChapterPage>> call,
                                   @NonNull Response<List<ChapterPage>> response) {

                if (response.isSuccessful() && response.body() != null) {

                    ReaderPageAdapter adapter =
                            new ReaderPageAdapter(response.body());

                    recyclerReaderPages.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ChapterPage>> call,
                                  @NonNull Throwable t) {

                Log.e(TAG, "Failed to load pages", t);
            }
        });
    }
}