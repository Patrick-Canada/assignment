package com.bowen.assignment.model;

/**
 * Created by patrick on 2015-03-26.
 */
public interface ModelDelegate {


    void didFinishLoad(BaseModel model);

    void didLoadError(BaseModel model);
}
