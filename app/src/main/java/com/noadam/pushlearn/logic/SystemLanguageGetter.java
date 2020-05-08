package com.noadam.pushlearn.logic;

import android.content.Context;
import android.os.Build;

public class SystemLanguageGetter {
    private Context context;

    public SystemLanguageGetter(Context context) {
        this.context = context;
    }

    private String getSystemLanguage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return String.valueOf(context.getResources().getConfiguration().getLocales().get(0)); }
        else { return String.valueOf(context.getResources().getConfiguration().locale); }
    }

    private int getLanguageId(String language) {
        if(language.contains("en_EN")) { return 1; }
        if(language.contains("en_US")) { return 11; }
        if(language.contains("ru_")) { return 2; }
        return 1;
    }
    public int get() {
       return getLanguageId(getSystemLanguage());
    }
}
