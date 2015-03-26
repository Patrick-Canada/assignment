package com.bowen.assignment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.bowen.assignment.common.FileUtil;
import com.bowen.assignment.common.MConstant;
import com.bowen.assignment.common.MGlobal;
import com.bowen.assignment.dao.MImageDao;
import com.bowen.assignment.entity.ImageEntity;
import com.bowen.assignment.fragment.SendFragment;
import com.bowen.assignment.fragment.UserFragment;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MainActivity extends ActionBarActivity implements LocationListener{

    private final static String TAG="MainActivity";

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private View mainView;

    private boolean isShowingSend;

    private static final String IsShowingSendKey="IsShowingSendKey";

    private SupportMapFragment mapFragment;

    private GoogleMap googleMap;

    private LatLng myPosition;

    private List<Marker> markerList=new ArrayList<>();

    private List<ImageEntity> entities=new ArrayList<>();

    private ImageButton userIcon;

    private MImageDao imageDao;

    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View actionBar=View.inflate(this,R.layout.action_bar,null);
        userIcon=(ImageButton)actionBar.findViewById(R.id.user_image_button);
        userIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle arguments=new Bundle();
                arguments.putBoolean("showCancelButton",true);
                showUserFragment(arguments);
            }
        });
        getSupportActionBar().setCustomView(actionBar);

        getSupportActionBar().setDisplayShowCustomEnabled(true);
        if (savedInstanceState!=null){
            isShowingSend=savedInstanceState.getBoolean(IsShowingSendKey);
        }
        setContentView(R.layout.activity_main);
        mainView=findViewById(R.id.main_view);

        mapFragment= (SupportMapFragment)getSupportFragmentManager().
                findFragmentByTag(getString(R.string.map_fragment_tag));
        googleMap=mapFragment.getMap();

        configEnvironment();
        if (MGlobal.getInstance().isShowUserIconConfig()){
            showUserFragment(null);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
        }else{
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            Bitmap userBitMap=FileUtil.
                    readFileFromInternalStorage(MConstant.USER_ICON_FILE_NAME);
            userIcon.setImageBitmap(userBitMap);
        }
    }



    public void setUserIcon(Bitmap bitmap){
        userIcon.setImageBitmap(bitmap);
    }

    public void showMyself(){
        Location location=getCurrentLocation();
        if(location!=null){
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            myPosition = new LatLng(latitude, longitude);
            zoomToPoint(myPosition);
        }
    }


    public void zoomToPoint(LatLng myPosition){
        CameraUpdate center=
                CameraUpdateFactory.newLatLng(myPosition);
        CameraUpdate zoom=CameraUpdateFactory.zoomTo(15);
        googleMap.moveCamera(center);
        googleMap.animateCamera(zoom);
    }

    private void configEnvironment(){

        MGlobal.init(this);

        imageDao = new MImageDao(this);
        try{
            imageDao.open();
        }catch (Exception SQLException){
            Log.e(TAG,"SQLException");
        }

        // Getting LocationManager object from System Service LOCATION_SERVICE
        locationManager = (LocationManager) this.
                getSystemService(LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,0,this);


        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){

            Intent intent=new Intent(Settings.ACTION_LOCALE_SETTINGS);
            startActivity(intent);
        }

        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        showMyself();

        refreshIcons();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    public void showUserFragment(Bundle arguments){
        if (getSupportFragmentManager().
                findFragmentByTag(getString(R.string.user_fragment_tag))==null){
            UserFragment fragment=new UserFragment();
            if (arguments!=null){
                fragment.setArguments(arguments);
            }
            FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(mainView.getId(),
                    fragment, getString(R.string.user_fragment_tag));
            fragmentTransaction.addToBackStack(getString(R.string.user_fragment_tag));
            fragmentTransaction.commit();
        }
    }

    private String  getCameraFileImageName(){
        return new Date().getTime()+"";
    }

    private Location getCurrentLocation(){
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, true);
        Location location = locationManager.getLastKnownLocation(provider);
        return location;
    }


    private void removeAllMarkers(){
        for (Marker marker:this.markerList){
            marker.remove();
        }
        this.markerList.clear();
    }

    private void refreshMarkers(){
        this.removeAllMarkers();
        Bitmap icon=FileUtil.readFileFromInternalStorage(MConstant.USER_ICON_SMALL_FILE_NAME);
        for (ImageEntity imageEntity:this.entities){
            double lat= imageEntity.getX();
            double log= imageEntity.getY();
            LatLng myPosition = new LatLng(lat, log);
            Marker marker= googleMap.addMarker(new MarkerOptions().position(myPosition).
                    icon(BitmapDescriptorFactory.fromBitmap(icon)));
            this.markerList.add(marker);
        }
    }


    private void refreshImageEntities(){
       this.entities=imageDao.getAllImages();
    }


    public void refreshIcons(){

        refreshImageEntities();
        refreshMarkers();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            String  fileName=this.getCameraFileImageName();
            ImageEntity imageEntity=new ImageEntity();
            Location location=getCurrentLocation();
            if (location!=null){
                imageEntity.setX(location.getLatitude());
                imageEntity.setY(location.getLongitude());
            }
            imageEntity.setUri(fileName);
            imageEntity.setName("");
            imageDao.createImageEntity(imageEntity);
            FileUtil.saveFile(imageBitmap, fileName);
            refreshIcons();
        }
        super.onActivityResult(requestCode,resultCode,data);

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


    @Override
    protected void onResume() {
        try{
            imageDao.open();
        }catch (Exception SQLException){
            Log.e(TAG, "SQLException");
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        imageDao.close();
        locationManager.removeUpdates(this);
        super.onPause();
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
