package materialtest.vivz.slidenerd.materialtest.activities;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;
import materialtest.vivz.slidenerd.materialtest.R;
import materialtest.vivz.slidenerd.materialtest.extras.SortListener;
import materialtest.vivz.slidenerd.materialtest.fragments.FragmentBoxOffice;
import materialtest.vivz.slidenerd.materialtest.fragments.FragmentDrawer;
import materialtest.vivz.slidenerd.materialtest.fragments.FragmentSearch;
import materialtest.vivz.slidenerd.materialtest.fragments.FragmentUpComing;
import materialtest.vivz.slidenerd.materialtest.services.MyService;
import me.tatarka.support.job.JobInfo;
import me.tatarka.support.job.JobScheduler;

public class MainActivity extends AppCompatActivity
        implements MaterialTabListener, View.OnClickListener {

    public static final int MOVIES_SEARCH_RESULTS = 0;
    public static final int MOVIES_HITS = 1;
    public static final int MOVIES_UPCOMING = 2;
    public static final String TAG_SORT_NAME = "sortName";
    public static final String TAG_SORT_DATE = "sortDate";
    public static final String TAG_SORT_RATING = "sortRatings";
    private static final int JOB_ID = 100;
    private static final long POLL_FREQUENCY = 2000;
    private JobScheduler mJobScheduler;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    // private SlidingTabLayout mTabs;
    private MaterialTabHost tabHost;
    private FloatingActionButton mFAB;
    private FloatingActionMenu mFABMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mJobScheduler = JobScheduler.getInstance(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                constructJob();
            }
        }, 30000);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        FragmentDrawer drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);

        tabHost = (MaterialTabHost) findViewById(R.id.materialTabHost);
        viewPager = (ViewPager) findViewById(R.id.pager);

        adapter = new ViewPagerAdapter(getSupportFragmentManager());
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

        buildFAB();

        /*
        mTabs = (SlidingTabLayout) findViewById(R.id.tabs);
        mTabs.setCustomTabView(R.layout.custom_tab_view, R.id.tabText);
        mTabs.setDistributeEvenly(true);
        // la linea de arriba hace que la linea de arriba de las tabs se mueva cuando deslizamos
        // si no nos pone una a lo largo y al desplazarnos se quita
        mTabs.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.colorPrimary));
        mTabs.setSelectedIndicatorColors(ContextCompat.getColor(getBaseContext(), R.color.accentColor));
        mTabs.setViewPager(viewPager);
        */
    }

    public void onDrawerItemClicked(int index) {
        viewPager.setCurrentItem(index);
    }

    private void constructJob() {
        JobInfo.Builder builder = new JobInfo.Builder(
                JOB_ID,
                new ComponentName(this, MyService.class));
        builder.setPeriodic(POLL_FREQUENCY)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                .setPersisted(true);
        mJobScheduler.schedule(builder.build());
    }

    private void buildFAB() {

        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.ic_action_new);

        mFAB = new FloatingActionButton.Builder(this)
                .setContentView(imageView)
                .setBackgroundDrawable(R.drawable.selector_button_red)
                .build();

        ImageView iconSortName = new ImageView(this);
        iconSortName.setImageResource(R.drawable.ic_action_alphabets);
        ImageView iconSortDate = new ImageView(this);
        iconSortDate.setImageResource(R.drawable.ic_action_calendar);
        ImageView iconSortRatings = new ImageView(this);
        iconSortRatings.setImageResource(R.drawable.ic_action_important);

        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);
        itemBuilder.setBackgroundDrawable(getResources().getDrawable(R.drawable
                .selector_sub_button_gray, getTheme()));

        SubActionButton buttonSortName = itemBuilder.setContentView(iconSortName).build();
        SubActionButton buttonSortDate = itemBuilder.setContentView(iconSortDate).build();
        SubActionButton buttonSortRating = itemBuilder.setContentView(iconSortRatings).build();

        buttonSortName.setTag(TAG_SORT_NAME);
        buttonSortDate.setTag(TAG_SORT_DATE);
        buttonSortRating.setTag(TAG_SORT_RATING);

        buttonSortName.setOnClickListener(this);
        buttonSortDate.setOnClickListener(this);
        buttonSortRating.setOnClickListener(this);

        mFABMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(buttonSortName)
                .addSubActionView(buttonSortDate)
                .addSubActionView(buttonSortRating)
                .attachTo(mFAB)
                .build();

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
        if (id == R.id.action_activity_recycler_item_animator) {
            startActivity(new Intent(this, ActivityRecyclerItemAnimations.class));
        }
        if (id == R.id.action_activity_a) {
            startActivity(new Intent(this, ActivityA.class));
        }
        if (id == R.id.action_activity_shared_a) {
            startActivity(new Intent(this, ActivitySharedA.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(MaterialTab materialTab) {
        viewPager.setCurrentItem(materialTab.getPosition());
    }

    @Override
    public void onTabReselected(MaterialTab materialTab) {

    }

    @Override
    public void onTabUnselected(MaterialTab materialTab) {

    }

    @Override
    public void onClick(View v) {

        //Fragment fragment = adapter.getItem(viewPager.getCurrentItem());
        // la forma de ponerlo en la linea de arriba peta
        Fragment fragment = (Fragment) adapter.instantiateItem(viewPager, viewPager.getCurrentItem());

        if (fragment instanceof SortListener) {
            if (v.getTag().equals(TAG_SORT_NAME)) {
                //L.t(this, "sort name was clicked");
                ((SortListener) fragment).onSortByName();
            }
            if (v.getTag().equals(TAG_SORT_DATE)) {
                //L.t(this, "sort date was clicked");
                ((SortListener) fragment).onSortByDate();
            }
            if (v.getTag().equals(TAG_SORT_RATING)) {
                //L.t(this, "sort rating was clicked");
                ((SortListener) fragment).onSortByRating();
            }
        }
    }

    public class ViewPagerAdapter extends FragmentStatePagerAdapter {
        int icons[] = {R.drawable.ic_action_search,
                R.drawable.ic_action_trending,
                R.drawable.ic_action_upcoming
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
                    fragment = FragmentUpComing.newInstance("", "");
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
