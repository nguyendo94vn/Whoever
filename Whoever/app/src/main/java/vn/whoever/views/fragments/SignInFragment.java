package vn.whoever.views.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;

import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import vn.whoever.transactionconn.HttpStatus;
import vn.whoever.views.activities.MainActivity;
import vn.whoever.R;
import vn.whoever.transactionconn.BeginTransaction;
import vn.whoever.utils.Initgc;
import vn.whoever.utils.RegexUtils;

/**
 * Created by spider man on 12/24/2015.
 */
public class SignInFragment extends Fragment implements Initgc {

    private String ssoId = "";
    private String password = "";

    private EditText editTextSsoId;
    private EditText editTextPassword;
    private TextView textSignUp;
    private TextView textTerm;
    private Button btnSignin;
    private Button btnSkipSignIn;
    private TextView logoText;
    private Handler handler = new Handler();

    private ProgressDialog progressDialog;

    private Toast toast;
    private int timeout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sign_in_layout, null);

        init(view);
        initListener(view);

        return view;
    }

    @Override
    public void init(View view) {
        textSignUp = (TextView) view.findViewById(R.id.textCreateNewUser);
        editTextSsoId = (EditText) view.findViewById(R.id.inputSsoIdStart);
        editTextSsoId.setTextColor(Color.parseColor("#ffffff"));
        editTextPassword = (EditText) view.findViewById(R.id.inputPasswordStart);
        editTextPassword.setTextColor(Color.parseColor("#ffffff"));
        btnSkipSignIn = (Button) view.findViewById(R.id.skipSignInButton);
        btnSignin = (Button) view.findViewById(R.id.signInButton);
        textTerm = (TextView) view.findViewById(R.id.textTermUserInfor);

        logoText = (TextView) view.findViewById(R.id.logoTextStartSignIn);
        Typeface bauhau93_font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/bauhau93.ttf");
        logoText.setTypeface(bauhau93_font);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#eb4949"));
        }
    }

    @Override
    public void initListener(final View view) {
        textSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("ssoId", ssoId);
                bundle.putString("password", password);
                SignUpFragment signUpFragment = new SignUpFragment();
                signUpFragment.setArguments(bundle);
                navigateFrame(signUpFragment, "signinFrameToSignUp");
            }
        });

        editTextSsoId.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // check key input
                return false;
            }
        });

        editTextPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                return false;
            }
        });

        editTextSsoId.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    boolean check = RegexUtils.getInstance().checkSsoId(editTextSsoId.getText().toString());
                    if(!check) {
                        if(toast != null) {
                            toast.cancel();
                        }
                        toast = Toast.makeText(getActivity(), "Account isn't standard of account require", Toast.LENGTH_LONG);
                        toast.show();
                    }
                }
            }
        });

        editTextPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    boolean check = RegexUtils.getInstance().checkPassword(editTextPassword.getText().toString());
                    if(!check) {
                        if(toast != null) {
                            toast.cancel();
                        }
                        toast = Toast.makeText(getActivity(), "Password isn't standard of email require", Toast.LENGTH_LONG);
                        toast.show();
                    }
                }
            }
        });

        btnSkipSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putBoolean(WelcomeFragment.KEY_USE_ACCOUNT, false);

                WelcomeFragment welcomeFragment = new WelcomeFragment();
                welcomeFragment.setArguments(bundle);

                navigateFrame(welcomeFragment, "signinFrameToWelcome");
            }
        });

        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ssoId = editTextSsoId.getText().toString();
                password = editTextPassword.getText().toString();
                /**
                 * TODO: after check password and ssoId => demo
                 */

                if (toast != null) {
                    toast.cancel();
                }

                if (RegexUtils.getInstance().checkSsoId(ssoId) && RegexUtils.getInstance().checkPassword(password)) {
                    //int stateLogin = BeginTransaction.getInstance(getActivity(), null).getRequestLogin(ssoId, password);
                    /**
                     * Account avaiable => Activity
                     * else
                     * => create new account
                     */
                    timeout = 40;
                    BeginTransaction.getTransaction(getActivity()).getRequestLogin(ssoId, password);
                    progressDialog = ProgressDialog.show(getActivity(), "", "Waiting for login...", true);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (timeout > 0) {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Integer httpCode = BeginTransaction.getTransaction(getActivity()).getHttpStatusCode();
                                        if (httpCode != null) {
                                            Log.d("httpCodeLogin",String.valueOf(httpCode));
                                            if(httpCode == HttpStatus.SC_NOT_FOUND) {
                                                navigateFrame(new SignUpFragment(), "signInFrameToSignUp");
                                            }

                                            if (HttpStatus.getStatus(getActivity()).codeSignIn(httpCode)) {
                                                loadDataActive();
                                            }
                                            timeout = 0;
                                            progressDialog.dismiss();
                                        }
                                        if(timeout == 1) {
                                            progressDialog.dismiss();
                                            HttpStatus.getStatus(getActivity()).codeSignIn(HttpStatus.SC_SERVICE_UNAVAIABLE);
                                        }
                                    }
                                });
                                --timeout;
                                try {
                                    Thread.sleep(150);
                                } catch (InterruptedException e) {
                                }
                            }
                        }
                    }).start();

                } else {
                    // TODO: show a toast alert: standard of email & password input fails
                    toast = Toast.makeText(getActivity(), "Check format for Account ID or Password, please", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });
    }

    public String getSerialNumberUser() {
        TelephonyManager telephonyManager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }

    private void navigateFrame(Fragment fragment, String strStack) {
        MainActivity.frgTransaction = MainActivity.frgtManager.beginTransaction();
        MainActivity.frgTransaction.replace(R.id.mainFrame, fragment).addToBackStack(strStack).commit();
    }

    public void loadDataActive() {
        MainActivity.frgTransaction = MainActivity.frgtManager.beginTransaction();
        MainActivity.frgtManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        MainActivity.frgTransaction.replace(R.id.mainFrame, new LoadFragment()).commit();
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