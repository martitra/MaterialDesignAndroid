package materialtest.vivz.slidenerd.materialtest.activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;
import materialtest.vivz.slidenerd.materialtest.R;
import materialtest.vivz.slidenerd.materialtest.fragments.FragmentBoxOffice;
import materialtest.vivz.slidenerd.materialtest.fragments.FragmentSearch;
import materialtest.vivz.slidenerd.materialtest.fragments.NavigationDrawerFragment;

public class MainActivity extends AppCompatActivity implements MaterialTabListener {

    public static final int MOVIES_SEARCH_RESULTS = 0;
    public static final int MOVIES_HITS = 1;
    public static final int MOVIES_UPCOMING = 2;
    private ViewPager viewPager;
    // private SlidingTabLayout mTabs;
    private MaterialTabHost tabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);

        tabHost = (MaterialTabHost) findViewById(R.id.materialTabHost);
        viewPager = (ViewPager) findViewById(R.id.pager);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        //viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                tabHost.setSelectedNavigationItem(position);
            }
        });

        for (int i = 0; i < adapter.getCount(); i++) {
            tabHost.addTab(
                    tabHost.newTab()
                            //.setText(adapter.getPageTitle(i))
                            .setIcon(adapter.getIcon(i))
                            .setTabListener(this)
            );
        }
        /*mTabs = (SlidingTabLayout) findViewById(R.id.tabs);
        mTabs.setCustomTabView(R.layout.custom_tab_view, R.id.tabText);
        mTabs.setDistributeEvenly(true);
        // la linea de arriba hace que la linea de arriba de las tabs se mueva cuando deslizamos
        // si no nos pone una a lo largo y al desplazarnos se quita
        mTabs.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.colorPrimary));
        mTabs.setSelectedIndicatorColors(ContextCompat.getColor(getBaseContext(), R.color.accentColor));
        mTabs.setViewPager(viewPager);*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(MainActivity.this, "Hey you just hit " + item.getTitle(),
                    Toast.LENGTH_SHORT).show();
            return true;
        }
        if (id == R.id.navigate) {
            startActivity(new Intent(this, SubActivity.class));
        }
        if (id == R.id.action_tabs_using_library) {
            startActivity(new Intent(this, ActivityUsingTabLibrary.class));
        }
        if (id == R.id.action_vector_test_activity) {
            startActivity(new Intent(this, VectorTestActivity.class));
        }

        if (id == R.id.action_activity_with_sliding_tab_layout) {
            startActivity(new Intent(this, ActivityWithSlidingTabLayout.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(MaterialTab materialTab) {

    }

    @Override
    public void onTabReselected(MaterialTab materialTab) {

    }

    @Override
    public void onTabUnselected(MaterialTab materialTab) {

    }

    public static class MyFragment extends Fragment {
        private TextView textView;

        public static MyFragment getInstance(int position) {
            MyFragment myFragment = new MyFragment();
            Bundle args = new Bundle();
            args.putInt("position", position);
            myFragment.setArguments(args);
            return myFragment;
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View layout = inflater.inflate(R.layout.fragment_my, container, false);
            textView = (TextView) layout.findViewById(R.id.position);
            Bundle bundle = getArguments();
            if (bundle != null) {
                textView.setText("The Page Number is " + bundle.getInt("position"));
            }
            return layout;
        }
    }

    public class ViewPagerAdapter extends FragmentStatePagerAdapter {
        int icons[] = {R.drawable.ic_action_home,
                R.drawable.ic_action_articles,
                R.drawable.ic_action_personal
                // R.drawable.ic_action_home,
                //R.drawable.ic_action_articles,
                // R.drawable.ic_action_personal,
                //R.drawable.ic_action_home,
                //R.drawable.ic_action_articles,
                //R.drawable.ic_action_personal
        };

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position) {
                case MOVIES_SEARCH_RESULTS:
                    fragment = FragmentSearch.newInstance("", "");
                    break;
                case MOVIES_HITS:
                    fragment = FragmentBoxOffice.newInstance("", "");
                    break;
                case MOVIES_UPCOMING:
                    fragment = FragmentUpcoming.newInstance("", "");
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return getResources().getStringArray(R.array.tabs)[position];
        }

        private Drawable getIcon(int position) {
            return getResources().getDrawable(icons[position], getTheme());
        }
    }
}
