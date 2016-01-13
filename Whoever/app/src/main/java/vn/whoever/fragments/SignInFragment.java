package vn.whoever.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import vn.whoever.MainActivity;
import vn.whoever.R;
import vn.whoever.StartActivity;
import vn.whoever.Transactions.UserTransaction;
import vn.whoever.models.User;

/**
 * Created by spider man on 12/24/2015.
 */
public class SignInFragment extends Fragment {

    private String email = "";
    private String password = "";

    private EditText emailText;
    private EditText passwordText;
    private TextView textSignUp;
    private TextView textTerm;
    private Button btnSignin;
    private Button btnSkipSignIn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sign_in_layout, null);

        init(view);
        initListener(view);

        return view;
    }

    public void init(View view) {
        textSignUp = (TextView) view.findViewById(R.id.textCreateNewUser);
        emailText = (EditText) view.findViewById(R.id.inputEmailStart);
        emailText.setTextColor(Color.parseColor("#ffffff"));
        passwordText = (EditText) view.findViewById(R.id.inputPasswordStart);
        passwordText.setTextColor(Color.parseColor("#ffffff"));
        btnSkipSignIn = (Button) view.findViewById(R.id.skipSignInButton);
        btnSignin = (Button) view.findViewById(R.id.signInButton);
        textTerm = (TextView) view.findViewById(R.id.textTermUserInfor);
    }

    public void initListener(View view) {
        textSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartActivity.frgStartTransaction = StartActivity.frgStartManager.beginTransaction();
                StartActivity.frgStartTransaction.replace(R.id.layoutStartApp, new SignUpFragment()).commit();
            }
        });

        emailText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // check key input

                return false;
            }
        });

        passwordText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                return false;
            }
        });

        btnSkipSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * TODO: get IMEI of phone send to server
                 */

                String serialNb = getSerialNumberUser();
                UserTransaction.getInstance(getActivity(), null).getRequestLoginAnonymous(serialNb);
                navigateToMain();
            }
        });

        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = emailText.getText().toString();
                password = passwordText.getText().toString();
                /**
                 * TODO: after check password and email => demo
                 */
                email = "nguyendo94vn@gmail.com";
                password = "12345678";

                UserTransaction.getInstance(getActivity(), null).getRequestLogin(email, password);
            }
        });
    }

    public String getSerialNumberUser() {
        TelephonyManager telephonyManager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }

    public void navigateToMain() {
        Intent intentMain = new Intent(getActivity(), MainActivity.class);
        startActivity(intentMain);
        getActivity().finish();
    }

}
