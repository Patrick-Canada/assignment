package com.bowen.assignment;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import com.bowen.assignment.common.FileUtil;
import com.bowen.assignment.entity.ImageEntity;
import com.bowen.assignment.vo.GalleryVO;

import java.util.List;

/**
 * Created by patrick on 2015-04-05.
 */
public class UserAdapter extends ArrayAdapter<GalleryVO> {

    private UserImageSelectedListener imageSelectedListener;

    private View.OnTouchListener touchListener;


    public interface UserImageSelectedListener{

        void imageSelected(View view);

    }


    protected class ViewHolder{

        ImageView imageView1;

        ImageView imageView2;

        ImageView imageView3;
        public void reset(){
            imageView1.setVisibility(View.GONE);

            imageView2.setVisibility(View.GONE);

            imageView3.setVisibility(View.GONE);
        }
    }


    public void setImageSelectedListener(UserImageSelectedListener listener){
        this.imageSelectedListener=listener;
    }


    public UserAdapter(Context context,List<GalleryVO> objects) {
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

    public View getView(int position, View convertView, ViewGroup parent) {
        GalleryVO vo= getItem(position);
        View view=convertView;
        if (view==null){
            view= LayoutInflater.from(getContext()).inflate(R.layout.user_icon_cell, parent, false);
            ViewHolder viewHolder=new ViewHolder();
            viewHolder.imageView1=(ImageView)view.findViewById(R.id.imageView1);
            viewHolder.imageView2=(ImageView)view.findViewById(R.id.imageView2);
            viewHolder.imageView3=(ImageView)view.findViewById(R.id.imageView3);
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
                viewHolder.imageView1.setVisibility(View.VISIBLE);
                if (!TextUtils.isEmpty(entity.getUri())){
                    viewHolder.imageView1.setTag(entity.getUri());
                    viewHolder.imageView1.setImageBitmap(FileUtil.
                            getBitmapFromAsset(viewHolder.imageView1.getContext(), entity.getUri()));
                }
            }
            if (i==1){
                viewHolder.imageView2.setVisibility(View.VISIBLE);
                if (!TextUtils.isEmpty(entity.getUri())){
                    viewHolder.imageView2.setTag(entity.getUri());
                    viewHolder.imageView2.setImageBitmap(FileUtil.
                            getBitmapFromAsset(viewHolder.imageView2.getContext(), entity.getUri()));
                }
            }

            if (i==2){
                viewHolder.imageView3.setVisibility(View.VISIBLE);
                if (!TextUtils.isEmpty(entity.getUri())){
                    viewHolder.imageView3.setTag(entity.getUri());
                    viewHolder.imageView3.setImageBitmap(FileUtil.
                            getBitmapFromAsset(viewHolder.imageView3.getContext(), entity.getUri()));
                }
            }
            i++;

        }

        return view;
    }
}
