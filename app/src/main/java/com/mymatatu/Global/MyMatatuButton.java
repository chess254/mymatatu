package com.mymatatu.Global;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.Button;

import com.mymatatu.R;


/**
 * Created by comp on 8/2/2017.
 */

public class MyMatatuButton extends Button {
    public MyMatatuButton(Context context) {
        super(context);
        init(null);
    }

    public MyMatatuButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public MyMatatuButton(Context context, AttributeSet attrs, int defStyleAttr) {
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
