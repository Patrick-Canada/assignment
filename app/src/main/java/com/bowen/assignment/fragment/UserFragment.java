package com.bowen.assignment.fragment;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.bowen.assignment.MainActivity;
import com.bowen.assignment.R;
import com.bowen.assignment.UserIconsActivity;
import com.bowen.assignment.common.*;
import com.bowen.assignment.common.Error;
import com.bowen.assignment.model.BaseModel;
import com.bowen.assignment.model.ModelDelegate;
import com.bowen.assignment.model.UserModel;

import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Created by patrick on 2015-03-10.
 */
public class UserFragment extends Fragment implements ModelDelegate{

    private ImageButton choiceUserButton;

    private Button goButton;

    private final static String TAG="UserFragment";

    private static final int SELECT_PICTURE = 1;

    private Button cancelButton;

    private boolean showCancelButton;

    private Bitmap previousBitmap;

    private Bitmap previousSmallBitmap;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.user_fragment, container, false);
        cancelButton=(Button)view.findViewById(R.id.cancel_button);
        Bundle arguments= getArguments();
        if (arguments!=null&&arguments.containsKey("showCancelButton")){

            showCancelButton=arguments.getBoolean("showCancelButton");
            previousBitmap=FileUtil.readFileFromInternalStorage(MConstant.USER_ICON_FILE_NAME);
            previousSmallBitmap=FileUtil.readFileFromInternalStorage(MConstant.USER_ICON_SMALL_FILE_NAME);
            cancelButton.setVisibility(View.VISIBLE);
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (previousBitmap!=null){
                        FileUtil.saveImageToInternalStorage(previousBitmap,
                                MConstant.USER_ICON_FILE_NAME);
                    }
                    if (previousSmallBitmap!=null){

                        FileUtil.saveImageToInternalStorage(previousSmallBitmap,
                                MConstant.USER_ICON_SMALL_FILE_NAME);
                    }
                    ((MainActivity)getActivity()).refreshIcons();

                    getFragmentManager().popBackStack();

                }
            });
        }

        goButton=(Button)view.findViewById(R.id.go_button);
        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        final Context context=getActivity();
        choiceUserButton=(ImageButton)view.findViewById(R.id.user_image_button);
        choiceUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, UserIconsActivity.class);
                startActivityForResult(intent,SELECT_PICTURE);
            }
        });


        Bitmap icon=FileUtil.readFileFromInternalStorage(MConstant.USER_ICON_FILE_NAME);
        if (icon!=null){
            choiceUserButton.setImageBitmap(icon);
        }

        if (!showCancelButton){
            cancelButton.setVisibility(View.GONE);
        }

        return view;
    }


    public void setActionLogo() {
        Bitmap bitmap = FileUtil.readFileFromInternalStorage(MConstant.USER_ICON_SMALL_FILE_NAME);
        if (bitmap!=null){
            ((MainActivity)getActivity()).setUserIcon(bitmap);
            ((ActionBarActivity) getActivity()).
                    getSupportActionBar().setDisplayShowTitleEnabled(false);
            ((MainActivity)getActivity()).refreshIcons();
        }
    }

    public Bitmap getUserIconFromAsset(String url,int width,int height){
        AssetManager assetManager = this.getActivity().getAssets();
        InputStream in= null;
        try {
            in = assetManager.open(url);
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
        BitmapFactory.Options options=ImageUtil.
                optionsBitmapFromInputStream(in,width,height);
        Bitmap bitmap = BitmapFactory.decodeStream(in,null,options);
        return bitmap;
    }


    public String getIconName(String url){
        String fileName = null;
        try {
            Pattern regex = Pattern.compile("([^\\\\/:*?\"<>|\r\n]+$)");
            Matcher regexMatcher = regex.matcher(url);
            if (regexMatcher.find()) {
                fileName = regexMatcher.group(1);
            }
        } catch (PatternSyntaxException ex) {
            // Syntax error in the regular expression
        }
        return fileName;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            String iconUrl= data.getStringExtra("icon_url");
            Bitmap userIcon=FileUtil.getBitmapFromAsset(this.getActivity(),iconUrl);
            choiceUserButton.setImageBitmap(userIcon);
            goButton.setEnabled(true);
            MGlobal.getInstance().setShowUserIconConfig(false);
            FileUtil.saveImageToInternalStorage(userIcon,MConstant.USER_ICON_FILE_NAME);
            Bitmap smallIcon=getUserIconFromAsset(iconUrl, 30, 30);
            FileUtil.saveImageToInternalStorage(smallIcon,MConstant.USER_ICON_SMALL_FILE_NAME);
            setActionLogo();
            if (MGlobal.getInstance().getUserId().length()==0){
                doAddUser(getIconName(iconUrl));
            }else{
                doUpdateUser(MGlobal.getInstance().getUserId(),getIconName(iconUrl));
            }
        }
    }


    /**
     * send to server side
     * @param userId
     * @param iconType
     */
    public void doUpdateUser(String userId, String iconType){

        /**
        UserModel userModel=UserModel.updateUser(
                MGlobal.getInstance().getAddress(),
                MGlobal.getInstance().getPort(),
                MGlobal.getInstance().getUserId(),
                iconType);
        userModel.setDelegate(this);
        userModel.startLoad();**/

    }


    /**
     * send to server side
     * @param iconType
     */
    public void doAddUser(String iconType){
        /**
        UserModel userModel=UserModel.initUser(MGlobal.getInstance().getAddress(),
                MGlobal.getInstance().getPort(),
                iconType);
        userModel.setDelegate(this);
        userModel.startLoad();**/

    }


    @Override
    public void didFinishLoad(BaseModel model) {

        if (model instanceof UserModel){
            if (model.getAction().equals("initUser")){
                String result=(String) model.getResults().get(0);
                MGlobal.getInstance().saveUserId(result);
            }
        }
    }


    @Override
    public void didLoadError(BaseModel model) {

        Error error= model.getError();
        String message=  error.getMessage();
        MGlobal.getInstance().alert(message,this.getActivity());
    }
}
