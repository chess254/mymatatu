package com.mymatatu.Global;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.mymatatu.R;

/**
 * Created by anonymous on 09-08-2017.
 */

public class ButtonExoSemiBold extends android.support.v7.widget.AppCompatButton {

    public ButtonExoSemiBold(Context context) {
        super(context);
        init(null);
    }

    public ButtonExoSemiBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public ButtonExoSemiBold(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }


    private void init(AttributeSet attrs){
        if (attrs != null){
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.TextViewExoSemiBold);
            String fontName = typedArray.getString(R.styleable.TextViewExoSemiBold_fontName);
            if (fontName != null){
                setTypeface(Typefaces.getTypeFace(getContext(),fontName));
            }
            typedArray.recycle();

        }
    }
}
