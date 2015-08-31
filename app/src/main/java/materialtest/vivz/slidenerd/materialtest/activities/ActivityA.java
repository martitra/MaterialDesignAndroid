package materialtest.vivz.slidenerd.materialtest.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import materialtest.vivz.slidenerd.materialtest.R;

public class ActivityA extends AppCompatActivity implements View.OnClickListener {

    private ViewGroup mRoot;
    private Button mButton1, mButton2, mButton3, mButton4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
//        if (Build.VERSION.SDK_INT >= 21) {
//            Slide slide = new Slide();
//            slide.setDuration(5000);
//            getWindow().setEnterTransition(slide);
//        }
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            TransitionInflater inflater = TransitionInflater.from(this);
            Transition transition = inflater.inflateTransition(R.transition.transition_a);
            getWindow().setExitTransition(transition);
            Slide slide = new Slide();
            slide.setDuration(5000);
            getWindow().setReenterTransition(slide);
        }
        setContentView(R.layout.activity_a);
        mRoot = (ViewGroup) findViewById(R.id.container_a);
        mButton1 = (Button) findViewById(R.id.button_1);
        mButton2 = (Button) findViewById(R.id.button_2);
        mButton3 = (Button) findViewById(R.id.button_3);
        mButton4 = (Button) findViewById(R.id.button_4);
        mRoot.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_shared_a, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        //Explode fade = new Explode();//new Fade()
        //fade.setDuration(5000);
        //TransitionManager.beginDelayedTransition(mRoot);//(mRoot, fade)
        //toggleHeight(mButton1, mButton2, mButton3, mButton4);//toggleVisibility
        ActivityOptionsCompat compat = ActivityOptionsCompat.makeSceneTransitionAnimation(this, null);
        startActivity(new Intent(this, ActivityB.class), compat.toBundle());

    }

    public void toggleVisibility(View... views) {
        for (View current : views) {
            if (current.getVisibility() == View.VISIBLE) {
                current.setVisibility(View.INVISIBLE);
            }
        }
    }

    public void toggleHeight(View... views) {
        for (View current : views) {
            ViewGroup.LayoutParams params = current.getLayoutParams();
            params.height = 100;
            params.width = 50;
            current.setLayoutParams(params);
        }
    }
}
