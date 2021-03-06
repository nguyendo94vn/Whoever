package vn.whoever.views.fragments;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import vn.whoever.R;
import vn.whoever.TransConnection.ContactTransaction;
import vn.whoever.models.Contact;
import vn.whoever.adapters.ContactsAdapter;
import vn.whoever.models.dao.ConnDB;
import vn.whoever.utils.AlphaButton;
import vn.whoever.utils.Initgc;

/**
 * Created by Nguyen Van Do on 12/29/2015.
 * This class implement contact layout.
 */
public class ContactsFragment extends Fragment implements Initgc {

    private LinearLayout layoutToolBar;
    private FloatingActionButton btnAddAccount;
    private RecyclerView recyclerContact;
    private ImageButton btnSearchContacts;

    private ContactsAdapter contactsAdapter;
    private List<Contact> contactList;
    private Handler mHandler;
    private LinearLayoutManager linearLayoutManager;
    private boolean onCreate = false;
    private ContactTransaction contactTransaction;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contacts_layout, container , false);

        init(view);
        initListener(view);
        return view;
    }

    @Override
    public void init(View view) {
        layoutToolBar = (LinearLayout) view.findViewById(R.id.layoutHeaderContactList);
        btnAddAccount = (FloatingActionButton) view.findViewById(R.id.btnAddNewContact);
        recyclerContact = (RecyclerView) view.findViewById(R.id.recyclerContactList);
        contactList = getContactList();

        onCreate = true;
        mHandler = new Handler();
        contactTransaction = new ContactTransaction(getActivity());

        recyclerContact.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getActivity());

        recyclerContact.setLayoutManager(linearLayoutManager);
        contactsAdapter = new ContactsAdapter(this, contactList, recyclerContact);
        recyclerContact.setAdapter(contactsAdapter);

        btnSearchContacts = (ImageButton) layoutToolBar.findViewById(R.id.btnSearchContact);
    }

    @Override
    public void initListener(View view) {
        final GestureDetector gestureDetector = new GestureDetector(getActivity(),
                new GestureDetector.SimpleOnGestureListener(){

            @Override
            public boolean onDown(MotionEvent event) {
                return true;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

                final int SWIPE_MIN_DISTANCE = 30;
                final int SWIPE_MAX_OFF_PATH = 150;
                final int SWIPE_THRESHOLD_VELOCITY = 150;

                try {
                    if(Math.abs(e1.getX() - e2.getX()) >  SWIPE_MAX_OFF_PATH) {
                        return false;
                    }
                    if((e1.getY() - e2.getY()) > SWIPE_MIN_DISTANCE
                            && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                        if(btnAddAccount.getVisibility() == View.VISIBLE) {
                            new AlphaButton(btnAddAccount, 1.0f, 0.0f);
                        }
                    } else if(e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE
                            && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                        // down
                        if(btnAddAccount.getVisibility() != View.VISIBLE) {
                            new AlphaButton(btnAddAccount, 0.0f, 1.0f);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return super.onFling(e1, e2, velocityX, velocityY);
            }

        });

        recyclerContact.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return false;
            }
        });

        btnSearchContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToSearchContact(new SearchContactFragment(), "contactFrameToSearchContact");
            }
        });

        btnAddAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToAddContact(new AddContactFragment(), "contactFrameToAddContact");
                btnAddAccount.setVisibility(View.GONE);
            }
        });
    }

    int loop = 0;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        loop = 0;
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if(onCreate) {
                contactTransaction.getListFriends();
                (new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (loop < 15) {
                            List<Contact> temp = getContactList();
                            if(temp.size() > contactsAdapter.getItemCount()) {
                                contactsAdapter.swapData(temp);
                                loop = 15;
                            }
                            ++loop;
                            try {
                                Thread.sleep(200);
                            } catch (InterruptedException e) {}
                        }
                    }
                })).start();
            }
        }
        else {}
    }

    public List getContactList() {
        contactList = new ArrayList<>();
        SQLiteDatabase db = ConnDB.getConn().getReadableDatabase();
        Cursor cursor = db.rawQuery("select ssoId, nickName, latestOnline from Contact", null);
        while (cursor.moveToNext()) {
            Contact contact = new Contact();
            contact.setSsoId(cursor.getString(0));
            contact.setNickName(cursor.getString(1));
            contact.setLatestOnline(cursor.getString(2));
            String strHeader = contact.getNickName().substring(0,1);
            strHeader = strHeader.toUpperCase();
            contact.setGroupName(strHeader);
            contactList.add(contact);
        }

        for(Contact contact : contactList) {
            cursor.moveToFirst();
            String arg[] = {contact.getSsoId()};
            cursor = db.rawQuery("select contentText from Status where ssoIdPoster=? limit 1", arg);
            while (cursor.moveToNext()) {
                String str = cursor.getString(0);
                if(str.length() > 30) {
                    str = str.substring(0, 30) + "...";
                    contact.setLatestStatus(str);
                } else {
                    contact.setLatestStatus(""+str);
                }
            }
        }
        cursor.close();
        Collections.sort(contactList, new Comparator<Contact>() {
            @Override
            public int compare(Contact lhs, Contact rhs) {
                return lhs.getNickName().compareTo(rhs.getNickName());
            }
        });

        String group = "";

        for(int i = 0; i < contactList.size(); ++i) {
            if(!group.equals(contactList.get(i).getGroupName())) {
                group = contactList.get(i).getGroupName();
            } else {
                contactList.get(i).setGroupName("");
            }
        }
        return contactList;
    }

    public void navigateToSearchContact(Fragment fragment, String strStack) {
        getParentFragment().getChildFragmentManager().beginTransaction()
                .replace(R.id.majorFrame, fragment).addToBackStack(strStack).commit();
        getParentFragment().getChildFragmentManager().executePendingTransactions();
    }

    public void navigateToAddContact(Fragment fragment, String strStack) {
        getParentFragment().getChildFragmentManager().beginTransaction()
                .replace(R.id.contactFrame, fragment).addToBackStack(strStack).commit();
        getParentFragment().getChildFragmentManager().executePendingTransactions();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public void onResume() {
        super.onResume();
    }

    @Override
    public void initGc() {

    }
}
