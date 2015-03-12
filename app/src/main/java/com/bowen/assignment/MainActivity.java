package com.bowen.assignment;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.bowen.assignment.fragment.SendFragment;


public class MainActivity extends ActionBarActivity {

    private View mainView;

    private boolean isShowingSend;

    private static final String IsShowingSendKey="IsShowingSendKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState!=null){
            isShowingSend=savedInstanceState.getBoolean(IsShowingSendKey);
        }
        setContentView(R.layout.activity_main);
        mainView=findViewById(R.id.main_view);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_images) {
            goImagesActivity();
            return true;
        }else if (id==R.id.action_camera){
            goCameraActivity();
            return true;
        }else if (id==R.id.action_send){
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
