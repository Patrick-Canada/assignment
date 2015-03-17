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
import com.bowen.assignment.R;
import com.bowen.assignment.common.FileUtil;
import com.bowen.assignment.common.ImageUtil;
import com.bowen.assignment.common.MConstant;
import com.bowen.assignment.common.MGlobal;

import java.io.FileNotFoundException;
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

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.user_fragment,container,false);
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
        return view;
    }


    public String getUserIconName(){
        return MConstant.USER_ICON_FILE_NAME;
    }



    public void setActionLogo() {
        Bitmap bitmap = FileUtil.readFileFromInternalStorage(getUserIconName());
        ((ActionBarActivity) getActivity()).
                getSupportActionBar().setIcon(new BitmapDrawable(this.getResources(), bitmap));
        ((ActionBarActivity) getActivity()).
                getSupportActionBar().setDisplayShowTitleEnabled(false);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            Uri selectedImageUri = data.getData();
            try {
                InputStream inputStream=this.getActivity().getContentResolver().
                        openInputStream(selectedImageUri);
                BitmapFactory.Options options=ImageUtil.optionsBitmapFromInputStream(inputStream,95,95);
                Bitmap bitmap= ImageUtil.getBitmapFromUriByOptions(this.getActivity(), selectedImageUri, options);
                inputStream.close();
                choiceUserButton.setImageBitmap(bitmap);
                goButton.setEnabled(true);
                MGlobal.getInstance().setShowUserIconConfig(false);
                FileUtil.saveImageToInternalStorage(bitmap, getUserIconName());
                setActionLogo();
            } catch (FileNotFoundException e) {
                Log.e(TAG, "exception", e);
                showMessage("can't get image");
            } catch (IOException e){
                Log.e(TAG,"exception",e);
                showMessage("can't get image");
            }
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
