package kh.edu.rupp.webtoonkh;

import android.os.Bundle;

public class SearchActivity extends BaseNavigationActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setupFloatingBottomNavigation(3);
    }
}
