package vn.whoever.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import vn.whoever.R;
import vn.whoever.dialogs.DialogPrivacyPostStatus;
import vn.whoever.utils.AppGc;

/**
 * Created by spider man on 1/14/2016.
 */
public class PostStatusActivity extends AppCompatActivity implements AppGc {

    private RelativeLayout toolbarPostStatus;
    private RelativeLayout toobarPrivacy;

    private ImageButton btnBackHome;
    private ImageButton btnPost;

    private EditText editContentStatus;

    private String status;

    public final static String SETTING_POST_STATUS = "SETTING_POST_STATUS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_status_layout);



        init();
        initListener();
    }

    public void init() {
        toolbarPostStatus = (RelativeLayout) findViewById(R.id.toolBarFromPostStatus);
        toobarPrivacy = (RelativeLayout) findViewById(R.id.toolBarChoicePrivacy);

        editContentStatus = (EditText) findViewById(R.id.contentTextInputStatus);
        btnBackHome = (ImageButton) toolbarPostStatus.findViewById(R.id.btnBackFromPostStatus);
        btnPost = (ImageButton) toolbarPostStatus.findViewById(R.id.btnSendStatus);
    }

    public void initListener() {
        btnBackHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status = editContentStatus.getText().toString();
                /**
                 * TODO: update for new feed & database SQLite
                 */

            }
        });

        toobarPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogPrivacyPostStatus privacyPost = new DialogPrivacyPostStatus();
                privacyPost.show(getFragmentManager(), "Privacy Post Status");
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
    }

    @Override
    public void initGc() {

    }
}
