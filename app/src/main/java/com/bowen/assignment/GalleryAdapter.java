package com.bowen.assignment;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bowen.assignment.common.FileUtil;
import com.bowen.assignment.entity.ImageEntity;
import com.bowen.assignment.vo.GalleryVO;

import java.util.List;
/**
 * Created by patrick on 2015-03-10.
 */
public class GalleryAdapter extends ArrayAdapter<GalleryVO> {


    private GalleryImageSelectedListener imageSelectedListener;

    public interface GalleryImageSelectedListener{

       void imageSelected(View view);

    }


    public void setImageSelectedListener(GalleryImageSelectedListener listener){
        this.imageSelectedListener=listener;
    }


    public GalleryAdapter(Context context,List<GalleryVO> objects) {
        super(context, R.layout.gallery_cell,objects);
        touchListener=new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction()== MotionEvent.ACTION_DOWN){

                    if (imageSelectedListener!=null){
                        imageSelectedListener.imageSelected(v);
                    }

                    return true;
                }
                return false;
            }
        };
    }


    private View.OnTouchListener touchListener;


    protected class ViewHolder{

        ImageView imageView1;

        TextView textView1;

        ImageView imageView2;

        TextView textView2;

        ImageView imageView3;

        TextView textView3;


        public void reset(){

            imageView1.setVisibility(View.GONE);
            textView1.setVisibility(View.GONE);
            imageView2.setVisibility(View.GONE);
            textView2.setVisibility(View.GONE);
            imageView3.setVisibility(View.GONE);
            textView3.setVisibility(View.GONE);
        }

    }

    public View getView(int position, View convertView, ViewGroup parent) {
        GalleryVO vo= getItem(position);
        View view=convertView;
        if (view==null){
            view= LayoutInflater.from(getContext()).inflate(R.layout.gallery_cell, parent, false);
            ViewHolder viewHolder=new ViewHolder();
            viewHolder.imageView1=(ImageView)view.findViewById(R.id.imageView1);
            viewHolder.textView1=(TextView)view.findViewById(R.id.textView1);
            viewHolder.imageView2=(ImageView)view.findViewById(R.id.imageView2);
            viewHolder.textView2=(TextView)view.findViewById(R.id.textView2);
            viewHolder.imageView3=(ImageView)view.findViewById(R.id.imageView3);
            viewHolder.textView3=(TextView)view.findViewById(R.id.textView3);
            viewHolder.imageView2.setOnTouchListener(touchListener);
            viewHolder.imageView1.setOnTouchListener(touchListener);
            viewHolder.imageView3.setOnTouchListener(touchListener);
            view.setTag(viewHolder);
        }
        ViewHolder viewHolder=(ViewHolder) view.getTag();

        viewHolder.reset();

        int i=0;
        for (ImageEntity entity:vo.getImageEntityList()){

            if (i==0){
                viewHolder.textView1.setText(entity.getX()+" "+entity.getY());
                viewHolder.imageView1.setVisibility(View.VISIBLE);
                viewHolder.textView1.setVisibility(View.VISIBLE);
                if (!TextUtils.isEmpty(entity.getUri())){
                   viewHolder.imageView1.setImageBitmap(FileUtil.getFileByName(entity.getUri()));
                }
            }
            if (i==1){
                viewHolder.textView2.setText(entity.getX()+" "+entity.getY());
                viewHolder.imageView2.setVisibility(View.VISIBLE);
                viewHolder.textView2.setVisibility(View.VISIBLE);
                if (!TextUtils.isEmpty(entity.getUri())){
                    viewHolder.imageView2.setImageBitmap(FileUtil.getFileByName(entity.getUri()));
                }
            }

            if (i==2){
                viewHolder.textView3.setText(entity.getX()+" "+entity.getY());
                viewHolder.imageView3.setVisibility(View.VISIBLE);
                viewHolder.textView3.setVisibility(View.VISIBLE);
                if (!TextUtils.isEmpty(entity.getUri())){
                    viewHolder.imageView3.setImageBitmap(FileUtil.getFileByName(entity.getUri()));
                }
            }
            i++;

        }

        return view;
    }
}
