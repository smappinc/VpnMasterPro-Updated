package com.smappinc.vpnmaster.pro.Activity.Server_Feature;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;



import java.util.Locale;

@SuppressLint("AppCompatCustomView")
public class Server_Flag extends ImageView {

    private static final String TAG = Server_Flag.class.getCanonicalName();

    private String countryCode;

    public Server_Flag(Context context) {
        super(context);
        init(null);
    }

    public Server_Flag(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public Server_Flag(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {

        super.setScaleType(ScaleType.CENTER_CROP);  // Scale to max width or height
        super.setAdjustViewBounds(true); // Definitely the right ratio

        if (isInEditMode()) return;

        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, com.smappinc.vpnmaster.pro.R.styleable.Server_Flag, 0, 0);
            try {
                String countryCode = typedArray.getString(com.smappinc.vpnmaster.pro.R.styleable.Server_Flag_countryCode);
                if (countryCode != null && !countryCode.isEmpty())
                    setCountryCode(countryCode);
                else
                    defaultLocal();
            } finally {
                typedArray.recycle();
            }
        }
    }

    @Deprecated
    @Override
    public void setScaleType(ScaleType scaleType) {
        // Remove
    }

    @Deprecated
    @Override
    public void setAdjustViewBounds(boolean adjustViewBounds) {
        // Remove
    }

    public void defaultLocal() {
        setCountryCode(Locale.getDefault().getCountry());
    }

    public void setCountryCode(String countryCode) {
        countryCode = countryCode != null && !countryCode.isEmpty() ? countryCode.toLowerCase() : "";
        if (!countryCode.equals(this.countryCode)) {
            this.countryCode = countryCode;
            updateDrawableWithCountryCode();
        }
    }

    private void updateDrawableWithCountryCode() {
        if (this.countryCode.isEmpty()) {
            setImageResource(0);
        } else {
            Resources resources = getResources();
            final String resName = "flag_" + this.countryCode;
            final int resourceId = resources.getIdentifier(resName, "drawable",
                    getContext().getPackageName());
            if (resourceId == 0) {
            }
            setImageResource(resourceId); // resourceId = 0 is not found
        }
    }
}
