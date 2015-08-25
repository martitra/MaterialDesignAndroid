package materialtest.vivz.slidenerd.materialtest.materialtest;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import materialtest.vivz.slidenerd.materialtest.R;
import materialtest.vivz.slidenerd.materialtest.tabs.SlidingTabLayout;

public class MainActivity extends AppCompatActivity {

    private ViewPager mPager;
    private SlidingTabLayout mTabs;

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

        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        mTabs = (SlidingTabLayout) findViewById(R.id.tabs);
        mTabs.setCustomTabView(R.layout.custom_tab_view, R.id.tabText);
        mTabs.setDistributeEvenly(true);
        // la linea de arriba hace que la linea de arriba de las tabs se mueva cuando deslizamos
        // si no nos pone una a lo largo y al desplazarnos se quita
        mTabs.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.colorPrimary));
        mTabs.setSelectedIndicatorColors(ContextCompat.getColor(getBaseContext(), R.color.accentColor));
        mTabs.setViewPager(mPager);
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

    class MyPagerAdapter extends FragmentPagerAdapter {
        int icons[] = {R.drawable.ic_action_home, R.drawable.ic_action_articles,
                R.drawable.ic_action_personal};
        String[] tabText = getResources().getStringArray(R.array.tabs);

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
            tabText = getResources().getStringArray(R.array.tabs);
        }

        @Override
        public Fragment getItem(int position) {
            MyFragment myFragment = MyFragment.getInstance(position);
            return myFragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Drawable drawable = getResources().getDrawable(icons[position], getTheme());
            assert drawable != null;
            drawable.setBounds(0, 0, 64, 64);// left, top, right, bottom
            ImageSpan imageSpan = new ImageSpan(drawable);
            SpannableString spannableString = new SpannableString(" ");
            spannableString.setSpan(imageSpan, 0, spannableString.length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            return spannableString;
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}
