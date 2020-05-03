package com.noadam.pushlearn.fragments;


import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import androidx.appcompat.widget.Toolbar;
import android.widget.Toast;

import com.noadam.pushlearn.R;
import com.noadam.pushlearn.internet.PushLearnServerStringCallBack;
import com.noadam.pushlearn.internet.PushLearnServerResponse;

public class LogInFragment extends Fragment {
    private Context context;
    private Toolbar toolbar;
    private ImageButton GoogleSignInImageButton;
    private ImageButton VKSignInImageButton;
    private ImageButton InstagramSignInImageButton;
    private ImageButton FacebookSignInImageButton;
    private EditText EmailEditText;
    private EditText PasswordEditText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        context = getActivity();
        View view = inflater.inflate(R.layout.frag_log_in, null);
        toolbar = view.findViewById(R.id.my_profile_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.welcome_back_to_PL_communtity);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new RegistrationFragment());
            }
        });

        GoogleSignInImageButton = view.findViewById(R.id.log_in_using_google_in_log_in_imageButton);
        VKSignInImageButton = view.findViewById(R.id.log_in_using_vk_in_log_in_imageButton);
        InstagramSignInImageButton = view.findViewById(R.id.log_in_using_instagram_in_log_in_imageButton);
        FacebookSignInImageButton = view.findViewById(R.id.log_in_using_facebook_in_log_in_imageButton);

        EmailEditText = view.findViewById(R.id.enter_email_in_log_in_editText);
        PasswordEditText = view.findViewById(R.id.enter_password_in_log_in_editText);

        ImageButton LogInButton = view.findViewById(R.id.next_in_log_in_imageButton);
        LogInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TryToSignIn(String.valueOf(EmailEditText.getText()),String.valueOf(PasswordEditText.getText()));
            }
        });

        return view;
    }

    private void TryToSignIn(String email, String password) {
        PushLearnServerResponse response = new PushLearnServerResponse(context);
        response.sendSignInResponse(email, password, new PushLearnServerStringCallBack() {
            @Override
            public void onResponse(String hash) {
                if (hash.contains("sign_in")) {
                    PasswordEditText.setError(getString(R.string.incorrect_password_or_email));
                    EmailEditText.setError(getString(R.string.incorrect_password_or_email));
                }  else {
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("account_hash", hash);
                    editor.putString("login", email);
                    editor.putString("password", password);
                    editor.putInt("account_language", getLanguageId(getSystemLanguage()));
                    editor.apply();
                    loadFragment(new MyProfileFragment());
                    Toast.makeText(context, getString(R.string.successful_sign_in), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onError(Throwable t) {

            }
        });
    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
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


}
