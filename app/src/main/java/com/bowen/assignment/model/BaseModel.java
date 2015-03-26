package com.bowen.assignment.model;
import com.bowen.assignment.common.Error;
import com.bowen.assignment.common.MConstant;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by patrick on 2015-03-26.
 */
public class BaseModel extends AsyncHttpClient{

    public ModelDelegate delegate;

    private String requestUrl;

    private Error error;

    private String method;

    private RequestParams requestParams;

    private List<Object> results=new ArrayList<>();

    private String action;


    public Error getError(){

        return this.error;
    }

    public ModelDelegate getDelegate() {

        return delegate;
    }


    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public List<Object> getResults(){
        return this.results;
    }

    public void setDelegate(ModelDelegate delegate) {

        this.delegate = delegate;
    }


    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public BaseModel(String url){

        super();
        this.method=MConstant.REST_GET;
        this.requestUrl=url;
    }

    public RequestParams getRequestParams(){
        if (this.requestParams==null){
            this.requestParams=new RequestParams();
        }
        return this.requestParams;
    }


    protected void parseJSONObject(JSONObject jsonObject){

    }


    protected void parseJSONArrayObject(JSONArray jsonArray){

    }


    protected void didLoadFinished(JSONObject jsonObject){

        this.parseJSONObject(jsonObject);

        if (this.getDelegate()!=null){

            this.getDelegate().didFinishLoad(this);
        }

    }


    protected void didLoadFinished(JSONArray jsonObject){

        this.parseJSONArrayObject(jsonObject);

        if (this.getDelegate()!=null){

            this.getDelegate().didFinishLoad(this);
        }
    }


    protected void didLoadError(Error error){
        this.error=error;
        if (this.getDelegate()!=null){
            this.getDelegate().didLoadError(this);
        }
    }


    public void startLoad(){
        if (this.getMethod().equals(MConstant.REST_GET)){
            this.get(this.requestUrl,this.requestParams,new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response){
                    didLoadFinished(response);
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    didLoadFinished(response);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Error error=new Error();
                    error.setCode(statusCode);
                    error.setDomain("Model");
                    error.setMessage(throwable.getMessage());
                    didLoadError(error);
                }
            });
        }else if (this.getMethod().equals(MConstant.REST_POST)){

            this.post(this.requestUrl, this.requestParams, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    didLoadFinished(response);
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    didLoadFinished(response);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Error error = new Error();
                    error.setCode(statusCode);
                    error.setDomain("Model");
                    error.setMessage(throwable.getMessage());
                    didLoadError(error);
                }
            });
        }

    }
}
