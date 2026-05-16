package kh.edu.rupp.webtoonkh;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;

public class ProfileActivity extends AppCompatActivity {

    ImageView imgHistoryCover;
    TextView txtHistoryTitle;

    LinearLayout bookmarkContainer;
    LinearLayout readLaterContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        bookmarkContainer = findViewById(R.id.bookmarkContainer);
        readLaterContainer = findViewById(R.id.readLaterContainer);

        imgHistoryCover = findViewById(R.id.imgHistoryCover);
        txtHistoryTitle = findViewById(R.id.txtHistoryTitle);

        loadProfileData();

        findViewById(R.id.cardViewHistory).setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, ViewHistoryActivity.class);
            startActivity(intent);
        });
    }

    private void loadProfileData() {
        SharedPreferences prefs = getSharedPreferences("profile_storage", MODE_PRIVATE);

        loadStoryList(bookmarkContainer, prefs.getString("bookmark_list", ""), true);
        loadStoryList(readLaterContainer, prefs.getString("read_later_list", ""), false);

        String historyTitle = prefs.getString("history_title", "No history yet");
        String historyCover = prefs.getString("history_cover_url", "");

        txtHistoryTitle.setText(historyTitle);

        if (!historyCover.isEmpty()) {
            Glide.with(this)
                    .load(historyCover)
                    .into(imgHistoryCover);
        }
    }

    private void loadStoryList(LinearLayout container, String data, boolean circleImage) {
        container.removeAllViews();

        if (data.isEmpty()) {
            TextView emptyText = new TextView(this);
            emptyText.setText("No story yet");
            emptyText.setTextSize(14);
            emptyText.setTextColor(0xFF111111);
            container.addView(emptyText);
            return;
        }

        String[] items = data.split(";;");

        for (String item : items) {
            String[] parts = item.split("\\|");

            if (parts.length >= 6) {
                String title = parts[1];
                String coverUrl = parts[5];

                LinearLayout storyLayout = new LinearLayout(this);
                storyLayout.setOrientation(LinearLayout.VERTICAL);
                storyLayout.setGravity(Gravity.CENTER);

                LinearLayout.LayoutParams storyParams =
                        new LinearLayout.LayoutParams(dp(132), LinearLayout.LayoutParams.WRAP_CONTENT);
                storyParams.setMargins(0, 0, dp(18), 0);
                storyLayout.setLayoutParams(storyParams);

                ShapeableImageView imageView = new ShapeableImageView(this);
                int cornerSize = circleImage ? dp(54) : dp(10);
                imageView.setShapeAppearanceModel(
                        imageView.getShapeAppearanceModel()
                                .toBuilder()
                                .setAllCornerSizes(cornerSize)
                                .build()
                );

                LinearLayout.LayoutParams imageParams =
                        circleImage
                                ? new LinearLayout.LayoutParams(dp(108), dp(108))
                                : new LinearLayout.LayoutParams(dp(112), dp(142));
                imageView.setLayoutParams(imageParams);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setElevation(circleImage ? 0 : dp(4));

                TextView titleView = new TextView(this);
                titleView.setText(title);
                titleView.setTextSize(12);
                titleView.setTextColor(0xFF111111);
                titleView.setGravity(Gravity.CENTER);
                titleView.setMaxLines(2);
                titleView.setLineSpacing(dp(2), 1f);

                LinearLayout.LayoutParams titleParams =
                        new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                titleParams.setMargins(0, dp(12), 0, 0);
                titleView.setLayoutParams(titleParams);

                if (circleImage) {
                    Glide.with(this)
                            .load(coverUrl)
                            .circleCrop()
                            .into(imageView);
                } else {
                    Glide.with(this)
                            .load(coverUrl)
                            .into(imageView);
                }

                storyLayout.addView(imageView);
                storyLayout.addView(titleView);

                final String finalTitle = title;
                final String finalCoverUrl = coverUrl;
                final String finalAuthor = parts[2];
                final String finalGenre = parts[3];
                final String finalDescription = parts[4];
                final int finalId = Integer.parseInt(parts[0]);

                storyLayout.setOnClickListener(v -> {

                    Intent intent = new Intent(ProfileActivity.this, StoryDetailActivity.class);

                    intent.putExtra("webtoon_id", finalId);
                    intent.putExtra("title", finalTitle);
                    intent.putExtra("author", finalAuthor);
                    intent.putExtra("genre", finalGenre);
                    intent.putExtra("description", finalDescription);
                    intent.putExtra("cover_url", finalCoverUrl);

                    startActivity(intent);
                });

                container.addView(storyLayout);
            }
        }
    }

    private int dp(int value) {
        return (int) (value * getResources().getDisplayMetrics().density);
    }
}
