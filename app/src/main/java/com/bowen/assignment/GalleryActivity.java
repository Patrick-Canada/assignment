package com.bowen.assignment;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.bowen.assignment.dao.MImageDao;
import com.bowen.assignment.entity.ImageEntity;
import com.bowen.assignment.vo.GalleryVO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by patrick on 2015-03-10.
 */
public class GalleryActivity extends ActionBarActivity implements GalleryAdapter.GalleryImageSelectedListener{


    private final static String TAG="GalleryActivity";

    private List<GalleryVO> dataSource=new ArrayList<>();

    private MImageDao imageDao;


    protected List<ImageEntity> getImageEntities(){
        List<ImageEntity> entities=imageDao.getAllImages();
        return entities;
    }


    private void prepareDataSource(List<ImageEntity> entities){

        int row=entities.size()/3;

        if (entities.size()%3!=0){
            row=row+1;
        }

        dataSource.clear();
        for (int i=0;i<row;i++){

            GalleryVO galleryVO=new GalleryVO();
            int endIndex=(i+1)*3;
            if (endIndex>entities.size()){
                endIndex=entities.size();
            }
            List<ImageEntity> entityList= entities.subList(i*3,endIndex);
            galleryVO.setImageEntityList(entityList);
            this.dataSource.add(galleryVO);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_image_list);
        imageDao = new MImageDao(this);
        try{
            imageDao.open();
        }catch (Exception SQLException){
            Log.e(TAG,"SQLException");
        }
        prepareDataSource(getImageEntities());
        ListView listView=(ListView)findViewById(R.id.image_list_view);
        GalleryAdapter galleryAdapter=new GalleryAdapter(this,this.dataSource);
        galleryAdapter.setImageSelectedListener(this);
        listView.setAdapter(galleryAdapter);
    }

    @Override
    public void imageSelected(View view) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                this.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        try{
            imageDao.open();
        }catch (Exception SQLException){
            Log.e(TAG,"SQLException");
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        imageDao.close();
        super.onPause();
    }


}
