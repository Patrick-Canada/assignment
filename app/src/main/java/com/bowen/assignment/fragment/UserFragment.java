package com.bowen.assignment.fragment;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
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
import com.bowen.assignment.common.FileUtil;
import com.bowen.assignment.common.ImageUtil;
import com.bowen.assignment.common.MConstant;
import com.bowen.assignment.common.MGlobal;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by patrick on 2015-03-10.
 */
public class UserFragment extends Fragment {

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
        View view= inflater.inflate(R.layout.user_fragment,container,false);

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
                    ((MainActivity)getActivity()).refreshIcon();

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

        choiceUserButton=(ImageButton)view.findViewById(R.id.user_image_button);
        choiceUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,
                        "Select Picture"), SELECT_PICTURE);
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
            ((ActionBarActivity) getActivity()).
                    getSupportActionBar().setIcon(new BitmapDrawable(this.getResources(), bitmap));
            ((ActionBarActivity) getActivity()).
                    getSupportActionBar().setDisplayShowTitleEnabled(false);
            ((MainActivity)getActivity()).refreshIcon();
        }
    }


    public Bitmap getUserIconBitmap(Uri iconUri,int width,int height){
        InputStream inputStream=null;
        try {
            inputStream=this.getActivity().getContentResolver().
                    openInputStream(iconUri);
            BitmapFactory.Options options=ImageUtil.
                    optionsBitmapFromInputStream(inputStream,width,height);
            Bitmap bitmap= ImageUtil.
                    getBitmapFromUriByOptions(this.getActivity(), iconUri, options);
            return bitmap;
        }catch (IOException e){
            Log.e(TAG, "exception", e);
            showMessage("can't get image");
        }finally {
            try {
                inputStream.close();
            }catch (IOException e){
                Log.e(TAG, "exception", e);
                showMessage("can't get image");
            }
        }
        return null;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            Uri selectedImageUri = data.getData();
            Bitmap userIcon=getUserIconBitmap(selectedImageUri,95,95);
            choiceUserButton.setImageBitmap(userIcon);
            goButton.setEnabled(true);
            MGlobal.getInstance().setShowUserIconConfig(false);
            FileUtil.saveImageToInternalStorage(userIcon,MConstant.USER_ICON_FILE_NAME);
            Bitmap smallIcon=getUserIconBitmap(selectedImageUri,30,30);
            FileUtil.saveImageToInternalStorage(smallIcon,MConstant.USER_ICON_SMALL_FILE_NAME);
            setActionLogo();
        }
    }


    public void showMessage(String message){
        new AlertDialog.Builder(this.getActivity())
                .setTitle("Message")
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       dialog.cancel();
                    }
                }).create().show();
    }

}
