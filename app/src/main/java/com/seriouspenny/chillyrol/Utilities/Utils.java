package com.seriouspenny.chillyrol.Utilities;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

import com.seriouspenny.chillyrol.Entities.Ally;
import com.seriouspenny.chillyrol.Entities.Creature;
import com.seriouspenny.chillyrol.R;

public class Utils {
    //Small animation that makes a view scale up and down as if it were clicked
    public static void animateClick(View v)
    {
        ScaleAnimation animation = new ScaleAnimation(1, 1.2f, 1, 1.2f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setInterpolator(new CycleInterpolator(1));
        animation.setDuration(200);

        v.startAnimation(animation);
    }

    //Small animation that makes a view move from right to left, indicating an error
    public static void animateError(View v)
    {
        TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0.2f, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
        animation.setInterpolator(new CycleInterpolator(2));
        animation.setDuration(200);

        v.startAnimation(animation);
    }

    public static int getCreatureImageId(Creature creature)
    {
        if(creature instanceof Ally)
            return R.drawable.player;
        else
            return R.drawable.enemy;
    }
}
