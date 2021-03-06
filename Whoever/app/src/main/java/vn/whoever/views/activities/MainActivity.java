package vn.whoever.views.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.io.File;
import java.net.CookieHandler;
import java.net.CookieManager;

import vn.whoever.R;
import vn.whoever.TransConnection.InfoTransaction;
import vn.whoever.models.dao.ConnDB;
import vn.whoever.views.fragments.LoadFragment;
import vn.whoever.views.fragments.SignInFragment;
/**
 * Created by Nguyen Van Do on 12/20/2015.
 * This main class - begin execute at this class
 */
public class MainActivity extends AppCompatActivity {

    public static FragmentManager frgtManager;
    public static FragmentTransaction frgTrans;
    public static SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPref = getSharedPreferences("SETTING_SYSTEM", MODE_PRIVATE);
        boolean isLogged = sharedPref.getBoolean("isLogged", false);

        frgtManager = getSupportFragmentManager();
        frgTrans = frgtManager.beginTransaction();

        if (isLogged) {
            frgTrans.replace(R.id.mainFrame, new LoadFragment()).commit();
        } else {
            frgTrans.replace(R.id.mainFrame, new SignInFragment()).commit();
        }
        hiddenSoftInput();

        //TODO: for keep session connection to server
        CookieHandler.setDefault(new CookieManager());
    }

    // Hiding virtual keyboard
    public void hiddenSoftInput() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    // Listener back pressed on keyboard
    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getFragments() != null) {
            for (Fragment frag : fm.getFragments()) {
                if (frag != null && frag.isVisible()) {
                    FragmentManager childFm = frag.getChildFragmentManager();
                    if (childFm.getBackStackEntryCount() > 0) {
                        childFm.popBackStack();
                        return;
                    }
                }
            }
        }
        if (getFragmentManager() != null && getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStackImmediate();
        }
        super.onBackPressed();
    }

    @Override
    public void onResume() {
        // Send reconnection to server when application resume
        (new InfoTransaction(this)).getReConnect();
        super.onResume();
    }

    @Override
    public void onStart() {
        // Opening connection into database when user open application
        ConnDB.getConn(this);
        ConnDB.getConn().openDataBase();
        super.onStart();
    }

    @Override
    public void onStop() {
        // Closing connection into database
        ConnDB.getConn().close();
        super.onStop();
    }
}
