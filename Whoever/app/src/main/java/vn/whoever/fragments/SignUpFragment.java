package vn.whoever.fragments;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import vn.whoever.R;
import vn.whoever.StartActivity;
import vn.whoever.utils.Initgc;
import vn.whoever.utils.RegexUtils;

/**
 * Created by spider man on 12/24/2015.
 */
public class SignUpFragment extends Fragment implements Initgc {

    private String nickName = "";
    private String email = "";
    private String password = "";

    private EditText editTextNickName;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private CheckBox checkBoxAgreeTerm;
    private Button btnCreateAccount;
    private TextView textViewSignIn;
    private TextView textViewTerm;
    private Toast toast;

    private boolean isCheckTerm = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sign_up_layout, null);

        init(view);
        initListener(view);

        return view;
    }

    @Override
    public void init(View view) {
        editTextNickName = (EditText) view.findViewById(R.id.textEditNickNameRegister);
        editTextNickName.setTextColor(Color.parseColor("#ffffff"));
        editTextEmail = (EditText) view.findViewById(R.id.textEditEmailRegister);
        editTextEmail.setTextColor(Color.parseColor("#ffffff"));
        editTextPassword = (EditText) view.findViewById(R.id.textEditPasswordRegister);
        editTextPassword.setTextColor(Color.parseColor("#ffffff"));

        textViewSignIn = (TextView) view.findViewById(R.id.textHaveAAccount);
        btnCreateAccount = (Button) view.findViewById(R.id.signUpButton);
        checkBoxAgreeTerm = (CheckBox) view.findViewById(R.id.checkAgreeTermService);
        textViewTerm = (TextView) view.findViewById(R.id.textTermUserInfor);
    }

    @Override
    public void initListener(View view) {
        textViewSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * TODO: naviagte to login layout
                 */
                StartActivity.frgStartTransaction = StartActivity.frgStartManager.beginTransaction();
                StartActivity.frgStartTransaction.replace(R.id.layoutStartApp, new SignInFragment()).commit();
            }
        });

        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * TODO: check email, password, check agree
                 */
                if(toast != null) {
                    toast.cancel();
                }
                if(checkBoxAgreeTerm.isChecked()) {
                    StartActivity.frgStartTransaction = StartActivity.frgStartManager.beginTransaction();
                    StartActivity.frgStartTransaction.replace(R.id.layoutStartApp, new WelcomeFragment()).commit();
                } else {
                    toast = Toast.makeText(getActivity().getApplicationContext(), "Choice agree to the Term", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        checkBoxAgreeTerm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(toast != null) {
                    toast.cancel();
                }
                if(isCheckTerm) {
                    isCheckTerm = false;
                    toast = Toast.makeText(getActivity().getApplicationContext(), "Not agree to the Term, don't create new account", Toast.LENGTH_LONG);
                    toast.show();
                } else {
                    isCheckTerm = true;
                    toast = Toast.makeText(getActivity().getApplicationContext(), "Greate, can create new account", Toast.LENGTH_LONG);
                 //   toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 100);
                    toast.show();
                }
            }
        });

        textViewTerm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        editTextNickName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    boolean check = RegexUtils.checkNickName(editTextNickName.getText().toString());
                    if(!check) {
                        if(toast != null) {
                            toast.cancel();
                        }
                        toast = Toast.makeText(getActivity(), "Nickname isn't standard of Nickname", Toast.LENGTH_LONG);
                        toast.show();
                    }
                }
            }
        });

        editTextEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    boolean check = RegexUtils.checkEmail(editTextEmail.getText().toString());
                    if(!check) {
                        if(toast != null) {
                            toast.cancel();
                        }
                        toast = Toast.makeText(getActivity(), "Email isn't standard of email", Toast.LENGTH_LONG);
                        toast.show();
                    }
                }
            }
        });

        editTextPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    boolean check = RegexUtils.checkPassword(editTextPassword.getText().toString());
                    if(!check) {
                        if(toast != null) {
                            toast.cancel();
                        }
                        toast = Toast.makeText(getActivity(), "Password isn't standard of password", Toast.LENGTH_LONG);
                        toast.show();
                    }
                }
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        System.gc();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(toast != null) {
            toast.cancel();
        }
    }

    @Override
    public void initGc() {

    }
}
