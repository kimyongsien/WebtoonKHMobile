package kh.edu.rupp.webtoonkh;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import kh.edu.rupp.webtoonkh.adapter.WebtoonAdapter;
import kh.edu.rupp.webtoonkh.model.Webtoon;

public class ViewHistoryActivity extends AppCompatActivity {

    RecyclerView recyclerHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_history);

        recyclerHistory = findViewById(R.id.recyclerHistory);
        recyclerHistory.setLayoutManager(new GridLayoutManager(this, 2));

        loadHistory();
    }

    private void loadHistory() {
        SharedPreferences prefs = getSharedPreferences("profile_storage", MODE_PRIVATE);

        String historyData = prefs.getString("history_list", "");

        List<Webtoon> historyList = new ArrayList<>();

        if (!historyData.isEmpty()) {
            String[] items = historyData.split(";;");

            for (String item : items) {
                String[] parts = item.split("\\|");

                if (parts.length >= 6) {
                    int id = Integer.parseInt(parts[0]);
                    String title = parts[1];
                    String author = parts[2];
                    String genre = parts[3];
                    String description = parts[4];
                    String coverUrl = parts[5];

                    historyList.add(new Webtoon(
                            id,
                            title,
                            author,
                            genre,
                            coverUrl,
                            description,
                            ""
                    ));
                }
            }
        }

        recyclerHistory.setAdapter(new WebtoonAdapter(historyList));
    }
}