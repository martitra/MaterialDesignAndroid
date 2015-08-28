package materialtest.vivz.slidenerd.materialtest.anim;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

/**
 * Creado por soft12 el 28/08/2015.
 */
public class AnimationUtils {

    public static void animate(RecyclerView.ViewHolder holder, boolean goesDown) {

        //YoYo.with(Techniques.Wobble)
        YoYo.with(Techniques.RubberBand)
                .duration(1000)
                .playOn(holder.itemView);

//        AnimatorSet animatorSet = new AnimatorSet();
//        ObjectAnimator animatorScaleX = ObjectAnimator.ofFloat(holder.itemView, "scaleX",
//                0.5F, 0.8F, 1.0F);
//        ObjectAnimator animatorScaleY = ObjectAnimator.ofFloat(holder.itemView, "scaleY",
//                0.5F, 0.8F, 1.0F);
//        ObjectAnimator animatorTranslateY = ObjectAnimator.ofFloat(holder.itemView, "translationY",
//                goesDown ? 300 : -300, 0);
//        ObjectAnimator animatorTranslateX = ObjectAnimator.ofFloat(holder.itemView, "translationX",
//                -50, 50, -30, 30, -20, 20, -5, 5, 0);
//        animatorSet.playTogether(animatorTranslateX, animatorTranslateY,
//                animatorScaleX, animatorScaleY);
//        animatorSet.setInterpolator(new AnticipateInterpolator());
//        animatorSet.setDuration(1000);
//        animatorSet.start();
    }

    public static void animateToolbar(View containerToolbar) {

    }
}
