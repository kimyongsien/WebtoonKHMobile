package kh.edu.rupp.webtoonkh;

import android.app.ActivityOptions;
import android.content.Intent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseNavigationActivity extends AppCompatActivity {

    private static final int NAV_HOME = 0;
    private static final int NAV_CATEGORY = 1;
    private static final int NAV_FEEDBACK = 2;
    private static final int NAV_SEARCH = 3;
    private static final int NAV_ANIMATION_DURATION = 240;

    private int currentSelectedIndex = NAV_HOME;

    protected void setupFloatingBottomNavigation(int selectedIndex) {
        currentSelectedIndex = selectedIndex;

        View navIndicator = findViewById(R.id.navIndicator);
        LinearLayout navItems = findViewById(R.id.navItems);

        if (navIndicator == null || navItems == null) {
            return;
        }

        navItems.post(() -> {
            int itemWidth = navItems.getWidth() / 3;
            FrameLayout.LayoutParams params =
                    (FrameLayout.LayoutParams) navIndicator.getLayoutParams();

            params.width = itemWidth;
            params.height = FrameLayout.LayoutParams.MATCH_PARENT;

            navIndicator.setLayoutParams(params);
            navIndicator.setTranslationX(itemWidth * Math.min(selectedIndex, NAV_FEEDBACK));
            selectNav(selectedIndex, false);
        });

        setNavClickListener(R.id.navHome, NAV_HOME, MainActivity.class, selectedIndex);
        setNavClickListener(R.id.navCategory, NAV_CATEGORY, CategoryActivity.class, selectedIndex);
        setNavClickListener(R.id.navFeedback, NAV_FEEDBACK, FeedbackActivity.class, selectedIndex);
        setNavClickListener(R.id.navSearch, NAV_SEARCH, SearchActivity.class, selectedIndex);
    }

    private void setNavClickListener(
            int viewId,
            int index,
            Class<?> destination,
            int selectedIndex
    ) {
        View navItem = findViewById(viewId);

        if (navItem == null) {
            return;
        }

        navItem.setOnClickListener(v -> {
            selectNav(index, true);

            if (index == selectedIndex) {
                return;
            }

            Intent intent = new Intent(this, destination);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            ActivityOptions options = ActivityOptions.makeCustomAnimation(
                    this,
                    android.R.anim.fade_in,
                    android.R.anim.fade_out
            );
            startActivity(intent, options.toBundle());
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        selectNav(currentSelectedIndex, false);
    }

    private void selectNav(int index, boolean animated) {
        View navIndicator = findViewById(R.id.navIndicator);
        LinearLayout navItems = findViewById(R.id.navItems);

        if (navIndicator == null || navItems == null) {
            return;
        }

        int itemWidth = navItems.getWidth() / 3;

        if (index < NAV_SEARCH) {
            navIndicator.setVisibility(View.VISIBLE);

            if (animated) {
                navIndicator.animate()
                        .translationX(itemWidth * index)
                        .setInterpolator(new DecelerateInterpolator())
                        .setDuration(NAV_ANIMATION_DURATION)
                        .start();
            } else {
                navIndicator.setTranslationX(itemWidth * index);
            }
        } else {
            navIndicator.setVisibility(View.INVISIBLE);
        }

        animateTab(findViewById(R.id.navHome), index == NAV_HOME, animated);
        animateTab(findViewById(R.id.navCategory), index == NAV_CATEGORY, animated);
        animateTab(findViewById(R.id.navFeedback), index == NAV_FEEDBACK, animated);
        animateTab(findViewById(R.id.navSearch), index == NAV_SEARCH, animated);
    }

    private void animateTab(View tab, boolean selected, boolean animated) {
        if (tab == null) {
            return;
        }

        float scale = selected ? 1.04f : 1f;
        float alpha = selected ? 1f : 0.75f;

        if (animated) {
            tab.animate()
                    .scaleX(scale)
                    .scaleY(scale)
                    .alpha(alpha)
                    .setInterpolator(new DecelerateInterpolator())
                    .setDuration(NAV_ANIMATION_DURATION)
                    .start();
        } else {
            tab.setScaleX(scale);
            tab.setScaleY(scale);
            tab.setAlpha(alpha);
        }
    }
}
