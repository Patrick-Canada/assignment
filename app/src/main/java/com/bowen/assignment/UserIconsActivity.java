package com.bowen.assignment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.bowen.assignment.entity.ImageEntity;
import com.bowen.assignment.vo.GalleryVO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by patrick on 2015-04-05.
 */
public class UserIconsActivity extends ActionBarActivity implements  UserAdapter.UserImageSelectedListener {

    private List<GalleryVO> dataSource=new ArrayList<>();

    public List<ImageEntity> getImageEntities(){
        List<ImageEntity> entities=new ArrayList<>();
        for (int i=0;i<5;i++){
            ImageEntity imageEntity=new ImageEntity();
            imageEntity.setName("icon "+i);
            imageEntity.setUri("icons/"+(i+1)+".gif");
            entities.add(imageEntity);
        }
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
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_image_list);
        prepareDataSource(getImageEntities());
        ListView listView=(ListView)findViewById(R.id.image_list_view);
        UserAdapter userAdapter=new UserAdapter(this,this.dataSource);
        userAdapter.setImageSelectedListener(this);
        listView.setAdapter(userAdapter);
    }

    @Override
    public void imageSelected(View view) {
        Intent intent=new Intent();
        intent.putExtra("icon_url",view.getTag().toString());
        setResult(RESULT_OK, intent);
        finish();
    }
}
