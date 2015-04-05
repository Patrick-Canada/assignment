package com.bowen.assignment.model;
import android.os.AsyncTask;
import android.util.Log;

import com.bowen.assignment.common.Error;
import com.bowen.assignment.entity.Result;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by patrick on 2015-03-26.
 */
public class BaseModel extends AsyncTask<Void,String,Void>{

    private final static String TAG="BaseModel";

    public ModelDelegate delegate;

    private Error error;

    private String address;

    private int port;

    private String method;

    private int timeout;

    private boolean readFinished;

    private List<Object> results=new ArrayList<>();

    private StringBuilder builder=new StringBuilder();

    private Map<String,String> params=new HashMap<>();

    private String action;

    public Error getError(){

        return this.error;
    }

    public void addParam(String key,String value){

        params.put(key,value);
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

    public BaseModel(String address, int port, String method){
        super();
        this.timeout=50000;
        this.address=address;
        this.port=port;
        this.method=method;
    }


    protected void parseResult(String result){

    }


    private String getMessage(){
        Map<String,String> result=new HashMap<>();
        result.put("method",this.method);
        Gson gson=new Gson();
        String paramString=gson.toJson(this.params);
        result.put("params",paramString);
        StringBuilder message= new StringBuilder(gson.toJson(result));
        message.append("<EOF>");
        return message.toString();
    }

    protected void didLoadFinished(String result){
        this.parseResult(result);
        if (this.getDelegate()!=null){
            this.getDelegate().didFinishLoad(this);
        }
    }


    protected void didLoadError(Error error){
        this.cancel(true);
        readFinished=true;
        this.error=error;
    }

    @Override
    public void onCancelled (){
        super.onCancelled();
        if (this.error!=null){
            if (this.getDelegate()!=null){
                this.getDelegate().didLoadError(this);
            }
        }
    }


    public void startLoad(){
        this.execute();
    }

    @Override
    protected Void doInBackground(Void... params) {
        Socket socket=null;
        PrintWriter out=null;
        BufferedReader in=null;
        try {
            socket=new Socket(this.address,this.port);
            socket.setSoTimeout(timeout);
            out=new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),true);
            if (out!=null&&!out.checkError()){
                String message=this.getMessage();
                out.println(message);
                out.flush();
                in= new BufferedReader(new InputStreamReader(socket.getInputStream()));
                //Listen for the incoming messages while mRun = true
                while (!readFinished) {
                    String incomingMessage = in.readLine();
                    if (incomingMessage != null) {
                        publishProgress(incomingMessage);
                    }
                }
            }
        }catch (UnknownHostException e){
            Log.e(TAG,"unknown host:",e);
            Error unknownHostError=new Error();
            unknownHostError.setMessage(Error.UN_KNOW_HOST_MESSAGE);
            unknownHostError.setDomain(Error.ERROR_MODEL_DOMAIN);
            unknownHostError.setCode(Error.UN_KNOW_HOST_CODE);
            this.didLoadError(unknownHostError);
        }catch (IOException e){
            Log.e(TAG,"io exception:",e);
            Error ioError=new Error();
            ioError.setMessage(Error.IO_ERROR_MESSAGE);
            ioError.setDomain(Error.ERROR_MODEL_DOMAIN);
            ioError.setCode(Error.IO_ERROR_CODE);
            this.didLoadError(ioError);
        }finally {
            out.flush();
            out.close();
            try {
                in.close();
                socket.close();
            }catch (IOException e) {
                Error ioError=new Error();
                ioError.setMessage(Error.IO_ERROR_CLOSE_MESSAGE);
                ioError.setDomain(Error.ERROR_MODEL_DOMAIN);
                ioError.setCode(Error.IO_ERROR_CLOSE_CODE);
                this.didLoadError(ioError);
            }
        }
        return null;
    }


    /**
     * socket process
     * @param values
     */
    @Override
    public void onProgressUpdate(String... values){
        if (values.length>0) {
            String content = values[0];
            builder.append(content);
            readFinished=true;
        }
    }

    /**
     * socket result
     * @param result
     */
    @Override
    public void onPostExecute(Void result){

        this.didLoadFinished(builder.toString());
    }
}
