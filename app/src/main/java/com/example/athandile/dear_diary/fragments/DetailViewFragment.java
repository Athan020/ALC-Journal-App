package com.example.athandile.dear_diary.fragments;

import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.example.athandile.dear_diary.R;

import butterknife.BindView;

public class DetailViewFragment extends DialogFragment {
    @BindView(R.id.view_title)
    TextView mTitle;
    @BindView(R.id.view_content)
    TextView mContent;

    public DetailViewFragment() {
    }

    public DetailViewFragment newInstance(String id, String title, String description){
        DetailViewFragment detail = new DetailViewFragment();
        Bundle viewData = new Bundle();

        viewData.putString(getString(R.string.entry_id),id);
        viewData.putString(getString(R.string.entry_title),title);
        viewData.putString(getString(R.string.entry_description),description);
        detail.setArguments(viewData);

        return  detail;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
      super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_detail_view,container,false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (isAdded()) {
            Bundle data = getArguments();
            mTitle =(TextView)view.findViewById(R.id.view_title);
            mContent =(TextView)view.findViewById(R.id.view_content);

            mTitle.setText(data.getString(getString(R.string.entry_id)));
            mContent.setText(data.getString(getString(R.string.entry_description)));
        }


    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog=  super.onCreateDialog(savedInstanceState);

        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }
}
