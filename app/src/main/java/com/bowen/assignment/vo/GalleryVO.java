package com.bowen.assignment.vo;

import com.bowen.assignment.entity.ImageEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by patrick on 2015-03-10.
 */
public class GalleryVO {

    private List<ImageEntity> imageEntityList=new ArrayList<>();

    public List<ImageEntity> getImageEntityList() {
        return imageEntityList;
    }

    public void setImageEntityList(List<ImageEntity> imageEntityList) {
        this.imageEntityList = imageEntityList;
    }
}
