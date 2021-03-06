package profilecom.connectgujarat.HomePage.transforms;

/**
 * Copyright (C) 2015 Kaelaela
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

public class DefaultTransformer implements ViewPager.PageTransformer {

 /*   @Override
    public void transformPage(View view, float position) {
        float alpha = 0;
        if (0 <= position && position <= 1) {
            alpha = 1 - position;
        } else if (-1 < position && position < 0) {
            alpha = position + 1;
        }
        view.setAlpha(alpha);
        view.setTranslationX(view.getWidth() * -position);
        float yPosition = position * view.getHeight();
        view.setTranslationY(yPosition);


    }*/


    private static final String TAG = "DepthTransformer";
    private static float MIN_SCALE = 0.75f;

    @Override
    public void transformPage(View view, float position) {
        int pageWidth = view.getWidth();
        Log.d(TAG, "VIew " + view + " Position: " + position);

        if (position <= -1) { // [-Infinity,-1) ] ***

            // RLP> I Changed to include "-1" as well: When position is -1, the view is not visible

            // This page is way off-screen to the left.

            view.setAlpha(0);
            Log.d(TAG, "VIew " + view + " Position: " + position + ", way left");
            view.setVisibility(View.GONE);

        } else if (position <= 0) { // [ (-1,0]
            // Use the default slide transition when moving to the left page
            view.setAlpha(1);
            view.setTranslationX(0);
            view.setScaleX(1);
            view.setScaleY(1);
            if (position == 0) {
                Log.d(TAG, "View " + view + " focused now?");
            }

            if (view.getVisibility() != View.VISIBLE)
                view.setVisibility(View.VISIBLE);

        } else if (position <= 1) { // (0,1]

            // Fade the page out.
            view.setAlpha(1 - position);

            // Counteract the default slide transition

            // I THINK THIS IS WHAT BREAKS EVERYTHING
            // ViewPager normally has the views one after another, but this makes all views on top

            view.setTranslationX(pageWidth * -position);

            // Scale the page down (between MIN_SCALE and 1)

            float scaleFactor = MIN_SCALE + (1 - MIN_SCALE) * (1 - Math.abs(position));
            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);

            if (position == 1) {

                Log.d(TAG, "View " + view + " invisible now?");
                view.setVisibility(View.GONE);
                // we totally hide the view. This seems to solve focus issue

            } else {
                if (view.getVisibility() != View.VISIBLE)
                    view.setVisibility(View.VISIBLE);
            }

        } else { // (1,+Infinity]
            // This page is way off-screen to the right.
            view.setAlpha(0);

            // we totally hide the view. This seems to solve focus issue
            // I have to check for strange side-effects, but so far I found none :)

            view.setVisibility(View.GONE);

            Log.d(TAG, "VIew " + view + " Position: " + position + ", way right");
        }
    }


}
