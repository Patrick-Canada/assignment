package com.bowen.assignment;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.provider.MediaStore;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.bowen.assignment.common.FileUtil;
import com.bowen.assignment.common.MConstant;
import com.bowen.assignment.common.MGlobal;
import com.bowen.assignment.fragment.SendFragment;
import com.bowen.assignment.fragment.UserFragment;

import java.util.Date;


public class MainActivity extends ActionBarActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private View mainView;

    private boolean isShowingSend;

    private static final String IsShowingSendKey="IsShowingSendKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME |
                ActionBar.DISPLAY_SHOW_TITLE);
        if (savedInstanceState!=null){
            isShowingSend=savedInstanceState.getBoolean(IsShowingSendKey);
        }
        setContentView(R.layout.activity_main);
        mainView=findViewById(R.id.main_view);
        configEnvironment();
        if (MGlobal.getInstance().isShowUserIconConfig()){
            showUserFragment();
        }else{
            Bitmap userBitMap=FileUtil.
                    readFileFromInternalStorage(MConstant.USER_ICON_FILE_NAME);
            getSupportActionBar().setIcon(new BitmapDrawable(this.getResources(),userBitMap));
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }


    private void configEnvironment(){

        MGlobal.init(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    public void showUserFragment(){
        if (getSupportFragmentManager().
                findFragmentByTag(getString(R.string.user_fragment_tag))==null){
            UserFragment fragment=new UserFragment();
            FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(mainView.getId(),
                    fragment, getString(R.string.user_fragment_tag));
            fragmentTransaction.addToBackStack(getString(R.string.user_fragment_tag));
            fragmentTransaction.commit();
        }
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            FileUtil.saveFile(imageBitmap, new Date().getTime()+"");
        }
    }


    public void goImagesActivity(){
        Intent intent=new Intent(this,GalleryActivity.class);
        startActivity(intent);
    }


    public void showSendFragment(){
        isShowingSend=true;
        SendFragment fragment=new SendFragment();
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(mainView.getId(),
                fragment, getString(R.string.send_fragment_tag));
        fragmentTransaction.addToBackStack(getString(R.string.send_fragment_tag));
        fragmentTransaction.commit();
    }


    public void goCameraActivity(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_Location) {
            goImagesActivity();
            return true;
        }else if (id==R.id.action_camera){
            goCameraActivity();
            return true;
        }else if (id==R.id.action_setting){
            showSendFragment();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle saveInstanceState){
        saveInstanceState.putBoolean(IsShowingSendKey,isShowingSend);
        super.onSaveInstanceState(saveInstanceState);
    }
}
