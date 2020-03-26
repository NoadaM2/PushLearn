package com.noadam.pushlearn.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.noadam.pushlearn.R;
import com.noadam.pushlearn.internet.PushLearnServerCallBack;
import com.noadam.pushlearn.internet.PushLearnServerResponse;
import com.vk.sdk.VKSdk;

import java.util.regex.Pattern;

public class RegistrationFragment extends Fragment {
    private Context context;
    private Toolbar toolbar;
    private Button SingUpButton;
    private Button LogInButton;
    private ImageButton GoogleSignInImageButton;
    private ImageButton VKSignInImageButton;
    private ImageButton InstagramSignInImageButton;
    private ImageButton FacebookSignInImageButton;
    private CheckBox TermsOfUseCheckBox;
    private TextInputLayout NickNameTextInputLayout;
    private TextInputLayout EmailTextInputLayout;
    private TextInputLayout PasswordTextInputLayout;
    private GoogleSignInClient mGoogleSignInClient;

    final int RC_SIGN_IN = 33;

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    "(?=.*[a-z])" +         //at least 1 lower case letter
                    "(?=.*[A-Z])" +         //at least 1 upper case letter
                  //  "(?=.*[a-zA-Z])" +      //any letter
                   // "(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{8,}" +               //at least 4 characters
                    "$");

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        context = getActivity();
        View view = inflater.inflate(R.layout.frag_registration, null);
        toolbar = view.findViewById(R.id.my_profile_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        GoogleSignInImageButton = view.findViewById(R.id.log_in_using_google_imageButton);
        GoogleSignInImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(context);
                if (account == null) {
                    Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                    startActivityForResult(signInIntent, RC_SIGN_IN);
                }
            }
        });
        VKSignInImageButton = view.findViewById(R.id.log_in_using_vk_imageButton);
        VKSignInImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VKSdk.login(getActivity());
            }
        });

        InstagramSignInImageButton = view.findViewById(R.id.log_in_using_instagram_imageButton);
        FacebookSignInImageButton = view.findViewById(R.id.log_in_using_facebook_imageButton);

        NickNameTextInputLayout = view.findViewById(R.id.nick_input_layout);
        EmailTextInputLayout = view.findViewById(R.id.email_input_layout);
        PasswordTextInputLayout = view.findViewById(R.id.password_input_layout);



        SingUpButton = view.findViewById(R.id.sing_up_button);
        SingUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ifBusyEmail(EmailTextInputLayout.getEditText().getText().toString());
            }
        });


        LogInButton = view.findViewById(R.id.log_in_button);
        LogInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new LogInFragment());
            }
        });


        TermsOfUseCheckBox = view.findViewById(R.id.terms_and_conditions_checkBox);
        TermsOfUseCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) { SingUpButton.setClickable(true); }
                else { SingUpButton.setClickable(false); }
            }
        });

        // Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            //    .requestProfile()
                .requestEmail()
              //  .requestIdToken("699493798150-4l3jatsk35sdovrninlelhj7163dhn21.apps.googleusercontent.com")
                //.requestId()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
         mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

        return view;
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_for_my_profile, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.my_profile_toolbar_settings:
                Fragment fragment = new SettingsFragment();
                loadFragment(fragment);
                break;
        }
        return true;
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


    private boolean validateEmail() {
        String emailInput = EmailTextInputLayout.getEditText().getText().toString().trim();

        if (emailInput.isEmpty()) {
            EmailTextInputLayout.setError(getString(R.string.empty_field));
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            EmailTextInputLayout.setError(getString(R.string.please_enter_valid_email));
            return false;
        } else {
            EmailTextInputLayout.setError(null);
            return true;
        }
    }

    private boolean validateUsername() {
        String usernameInput = NickNameTextInputLayout.getEditText().getText().toString().trim();

        if (usernameInput.isEmpty()) {
            NickNameTextInputLayout.setError(getString(R.string.empty_field));
            return false;
        } else if (usernameInput.length() > 15) {
            NickNameTextInputLayout.setError(getString(R.string.username_too_long));
            return false;
        } else {
            NickNameTextInputLayout.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {
        String passwordInput = PasswordTextInputLayout.getEditText().getText().toString().trim();

        if (passwordInput.isEmpty()) {
            PasswordTextInputLayout.setError(getString(R.string.empty_field));
            return false;
        } else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            PasswordTextInputLayout.setError(getString(R.string.password_should_be_longer));
            return false;
        } else {
            PasswordTextInputLayout.setError(null);
            return true;
        }
    }


    private void TryToSignUp(String email, String password, String nickname, int language_id) {
        if (!validateEmail() | !validateUsername() | !validatePassword()) {
            return;
        }
        PushLearnServerResponse response = new PushLearnServerResponse(context);
        response.sendSignUpResponse(email, password,nickname, language_id, new PushLearnServerCallBack() {
            @Override
            public void onResponse(String value) {
                if (value.contains("email_busy")) {
                    Toast.makeText(context, getString(R.string.something_is_wrong_try_another_email), Toast.LENGTH_SHORT).show();
                }  else {
                   SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("account_hash", value);
                    editor.putString("login", email);
                    editor.putString("password", password);
                    editor.putString("nickname", nickname);
                    editor.putInt("account_language", language_id);
                    editor.apply();
                    loadFragment(new MyProfileFragment());
                    Toast.makeText(context, getString(R.string.successful_registration), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onError(Throwable t) {

            }
        });
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

    private void ifBusyEmail(String email) {
        PushLearnServerResponse response = new PushLearnServerResponse(context);
        response.sendBusyEmailResponse(email, new PushLearnServerCallBack() {
            @Override
            public void onResponse(String value) {
                if(value.equals("ok")) { ifBusyNickName(NickNameTextInputLayout.getEditText().getText().toString(),email);
                } else {
                    EmailTextInputLayout.setError(getString(R.string.this_email_is_busy));
                }
            }
            @Override
            public void onError(Throwable t) {

            }
        });
    }

    private void getNickNameByHashResponse(String hash) {
        PushLearnServerResponse response = new PushLearnServerResponse(context);
        response.sendGetNickNameByHashResponse(hash, new PushLearnServerCallBack() {
            @Override
            public void onResponse(String value) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("nickname", value);
                editor.apply();
            }
            @Override
            public void onError(Throwable t) {

            }
        });
    }

    private void ifBusyNickName(String nickname, String email) {
        PushLearnServerResponse response = new PushLearnServerResponse(context);
        response.sendBusyNickNameResponse(nickname, new PushLearnServerCallBack() {
            @Override
            public void onResponse(String value) {
                int language_id = getLanguageId(getSystemLanguage());
                if(value.equals("ok")) { TryToSignUp(email, PasswordTextInputLayout.getEditText().getText().toString(), nickname, language_id);
                } else {
                    NickNameTextInputLayout.setError(getString(R.string.this_nickname_is_busy));
                }
            }
            @Override
            public void onError(Throwable t) {

            }
        });
    }

}
