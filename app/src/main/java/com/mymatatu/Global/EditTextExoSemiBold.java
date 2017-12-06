package com.mymatatu.Global;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

import com.mymatatu.R;

/**
 * Created by anonymous on 01-08-2017.
 */

public class EditTextExoSemiBold extends AppCompatEditText {

    public EditTextExoSemiBold(Context context) {
        super(context);
        init(null);
    }

    public EditTextExoSemiBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public EditTextExoSemiBold(Context context, AttributeSet attrs, int defStyleAttr) {
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
