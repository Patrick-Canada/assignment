package com.bowen.assignment.fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.bowen.assignment.GalleryActivity;
import com.bowen.assignment.R;

/**
 * Created by patrick on 2015-03-10.
 */
public class UserFragment extends Fragment {

    private ImageButton choiceUserButton;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.user_fragment,container,false);
        ImageButton imageButton=(ImageButton)view.findViewById(R.id.action_back_button);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        choiceUserButton=(ImageButton)view.findViewById(R.id.user_image_button);
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
