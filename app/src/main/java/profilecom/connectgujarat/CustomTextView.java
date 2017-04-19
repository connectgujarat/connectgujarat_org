package profilecom.connectgujarat;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import profilecom.connectgujarat.R;


public class CustomTextView extends TextView {
    // Utility utility = new Utility();

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //Typeface.createFromAsset doesn't work in the layout editor. Skipping...
        if (isInEditMode()) {
            return;
        }

        TypedArray styledAttrs = context.obtainStyledAttributes(attrs, R.styleable.TypefacedTextView);
        String fontNos = styledAttrs.getString(R.styleable.TypefacedTextView_typeface);
        styledAttrs.recycle();
            Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/" + "Roboto-Italic.ttf");
            setTypeface(typeface);

    }
}