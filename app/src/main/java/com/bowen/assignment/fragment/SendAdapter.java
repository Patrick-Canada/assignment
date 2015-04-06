package com.bowen.assignment.fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bowen.assignment.R;
import com.bowen.assignment.vo.LocalServerVO;

import java.util.List;

/**
 * Created by patrick on 2015-03-10.
 */
public class SendAdapter extends ArrayAdapter<LocalServerVO> {

    public SendAdapter(Context context,List<LocalServerVO> vos ){
        super(context, R.layout.network_cell,vos);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LocalServerVO vo= getItem(position);
        View view=convertView;
        if (view==null){
            view= LayoutInflater.from(getContext()).inflate(R.layout.network_cell, parent, false);
        }
        TextView textView=(TextView) view.findViewById(R.id.net_work_text_view);
        textView.setText(vo.getName());
        return view;
    }
}
