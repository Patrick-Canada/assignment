package com.bowen.assignment.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.bowen.assignment.R;
import com.bowen.assignment.vo.LocalServerVO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by patrick on 2015-03-10.
 */
public class SendFragment extends Fragment {

    private View contentView;

    private List<LocalServerVO> dataSource=new ArrayList<>();

    public void prepareDataSource(){

        dataSource.clear();
        for (int i=0;i<5;i++){
            LocalServerVO serverVO=new LocalServerVO();
            serverVO.setName("Server "+i);
            serverVO.setIpAddress("192.168.1."+i);
            dataSource.add(serverVO);
        }
    }


    public void showUserFragment(){
        UserFragment fragment=new UserFragment();
        FragmentTransaction fragmentTransaction=getFragmentManager().beginTransaction();
        fragmentTransaction.replace(contentView.getId(),
                fragment, getString(R.string.user_fragment_tag));
        fragmentTransaction.addToBackStack(getString(R.string.user_fragment_tag));
        fragmentTransaction.commit();
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                          Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_net_work,container,false);
        contentView=view.findViewById(R.id.send_fragment);
        ListView listView=(ListView) view.findViewById(R.id.server_list_view);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showUserFragment();
            }
        });
        prepareDataSource();
        listView.setAdapter(new SendAdapter(this.getActivity(),dataSource));
        return view;
    }

}
