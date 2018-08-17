package com.lifecyclehealth.lifecyclehealth.custome;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatRadioButton;
import android.util.AttributeSet;
import android.widget.RadioButton;

import com.lifecyclehealth.lifecyclehealth.R;

/**
 * Created by vaibhavi on 10-10-2017.
 */

public class CustomRadioButton extends android.support.v7.widget.AppCompatRadioButton {

    public CustomRadioButton(Context context) {
        super(context);
        init(null);
    }

    public CustomRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(attrs);
    }

    public CustomRadioButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        init(attrs);
    }

    private void init(AttributeSet attrs) {
        /*if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CustomFontTextView);
            String fontName = a.getString(R.styleable.CustomFontTextView_font);
            if (fontName != null) {
                Typeface myTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/" + fontName);
                setTypeface(myTypeface);
            } else {
                Typeface myTypeface = Typeface.createFromAsset(getContext().getAssets(), "Roboto-Regular.ttf");
                setTypeface(myTypeface);
            }
            a.recycle();
        }*/
    }


}