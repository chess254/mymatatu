package com.mymatatu.Global;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.mymatatu.R;


/**
 * Created by user1 on 24-04-2017.
 */

public class TextViewExoSemiBold extends AppCompatTextView {
    public TextViewExoSemiBold(Context context) {
        super(context);
        init(null);
    }

    public TextViewExoSemiBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public TextViewExoSemiBold(Context context, AttributeSet attrs, int defStyleAttr) {
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
