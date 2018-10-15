package com.lifecyclehealth.lifecyclehealth.custome;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.lifecyclehealth.lifecyclehealth.R;

/**
 * Created by norman on 3/8/15.
 */
public class CustomFontTextView extends TextView {

    public static final String ANDROID_SCHEMA = "http://schemas.android.com/apk/res/android";

    public CustomFontTextView(Context context) {
        super(context);
        init(null);
    }

    public CustomFontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(attrs);
    }

    public CustomFontTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        init(attrs);
    }

    private void init(AttributeSet attrs) {
       /* if (attrs != null) {
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

  /*  public void applyCustomFont(AttributeSet attrs) {
        TypedArray attributeArray = context.obtainStyledAttributes(
                attrs, R.styleable.CustomFontTextView);

        String fontName = attributeArray.getString(R.styleable.CustomFontTextView_font);

        // check if a special textStyle was used (e.g. extra bold)
        int textStyle = attributeArray.getInt(R.styleable.CustomFontTextView_textStyle, 0);

        // if nothing extra was used, fall back to regular android:textStyle parameter
        if (textStyle == 0) {
            textStyle = attrs.getAttributeIntValue(ANDROID_SCHEMA, "textStyle", Typeface.NORMAL);
        }

        Typeface customFont = selectTypeface(getContext(), fontName, textStyle);
        customFontTextView.setTypeface(customFont);

        attributeArray.recycle();
    }

    private Typeface selectTypeface(Context context, String fontName, int textStyle) {
        if (fontName.contentEquals(context.getString(R.string.font_name_fontawesome))) {
            return FontCache.getTypeface("HelveticaNeue-Regular.ttf", context);
        } else if (fontName.contentEquals(context.getString(R.string.font_name_source_sans_pro))) {


            switch (textStyle) {
                case Typeface.BOLD: // bold
                    return FontCache.getTypeface("HelveticaNeue-Bold.ttf", context);

                case Typeface.NORMAL: // regular
                    return FontCache.getTypeface("HelveticaNeue-Regular.ttf", context);
                default:
                    return FontCache.getTypeface("HelveticaNeue-Regular.ttf", context);
            }
        } else {
            // no matching font found
            // return null so Android just uses the standard font (Roboto)
            return null;
        }
    }*/

}