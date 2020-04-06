package com.noadam.pushlearn.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.app.Fragment;

import com.noadam.pushlearn.R;
import com.noadam.pushlearn.internet.PushLearnServerCallBack;
import com.noadam.pushlearn.internet.PushLearnServerResponse;

public class ConfirmEmailFragment extends Fragment {
    private Context context;
    private Toolbar toolbar;
    private EditText confirmEmailEditText;
   private String email;
   private String password;
   private String nickname;
   private int language_id;

    public void setValues(String email, String password, String nickname, int language_id) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.language_id = language_id;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        context = getActivity();
        View view = inflater.inflate(R.layout.frag_confirm_email, null);
        toolbar = view.findViewById(R.id.confirm_email_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.confirm_your_email);

        confirmEmailEditText = view.findViewById(R.id.enter_code_from_your_email_editText);

        ImageButton LogInButton = view.findViewById(R.id.next_in_log_in_imageButton);
        LogInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkEmailVerificationCodeResponse(email,confirmEmailEditText.getText().toString());
            }
        });

        return view;
    }

    private void TryToSignUp(String email, String password, String nickname, int language_id) {
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

    private void checkEmailVerificationCodeResponse(String email, String code) {
        PushLearnServerResponse response = new PushLearnServerResponse(context);
        response.checkEmailVerificationCodeResponse(email,code, new PushLearnServerCallBack() {
            @Override
            public void onResponse(String value) {
                if(value.equals("ok")) { TryToSignUp(email,password,nickname,language_id);
                } else {
                    confirmEmailEditText.setError(getString(R.string.the_code_is_incorrect));
                }
            }
            @Override
            public void onError(Throwable t) {

            }
        });
    }
}
