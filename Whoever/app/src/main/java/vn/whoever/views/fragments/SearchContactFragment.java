package vn.whoever.views.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import vn.whoever.R;
import vn.whoever.utils.Initgc;

/**
 * Created by Nguyen Van Do on 4/9/2016.
 * This class isn't completed.
 */
public class SearchContactFragment extends Fragment implements Initgc {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_contact_layout, container, false);

        init(view);
        initListener(view);
        return view;
    }

    @Override
    public void init(View view) {}

    @Override
    public void initListener(View view) {}

    @Override
    public void initGc() {}
}
