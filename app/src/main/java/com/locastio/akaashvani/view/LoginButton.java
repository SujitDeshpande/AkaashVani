package com.locastio.akaashvani.view;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import com.digits.sdk.android.DigitsAuthButton;
import com.locastio.akaashvani.R;

;

/**
 * Created by Sujit on 11/09/15.
 */
public class LoginButton extends DigitsAuthButton {
    public LoginButton(Context context) {
        super(context);
        init();
    }

    public LoginButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }



    public LoginButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }
    private void init() {
        if (isInEditMode()){
            return;
        }
        final Drawable phone = getResources().getDrawable(R.drawable.ic_signin_phone);
        phone.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        setCompoundDrawablesWithIntrinsicBounds(phone, null, null, null);
        setBackgroundResource(R.drawable.digits_button);
        setText("Sign In/ Register");
        setTextSize(16);
        setTextColor(getResources().getColor(R.color.white));
        //setTypeface(App.getInstance().getTypeface());
    }
}
