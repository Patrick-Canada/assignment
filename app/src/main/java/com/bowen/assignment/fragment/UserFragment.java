package com.bowen.assignment.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bowen.assignment.GalleryActivity;
import com.bowen.assignment.R;

/**
 * Created by patrick on 2015-03-10.
 */
public class UserFragment extends Fragment {

    private Button choiceUserButton;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.user_fragment,container,false);
        choiceUserButton=(Button)view.findViewById(R.id.choice_user_button);
        choiceUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), GalleryActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }
}
