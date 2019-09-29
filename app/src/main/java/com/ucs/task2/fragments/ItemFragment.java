package com.ucs.task2.fragments;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.ucs.task2.database.DatabaseHelper;
import com.ucs.task2.model.CheckInModels;
import com.ucs.task2.myapplication.ItemActivity;
import com.ucs.task2.myapplication.MapActivity;
import com.ucs.task2.myapplication.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.support.constraint.Constraints.TAG;

public class ItemFragment extends Fragment implements
        View.OnClickListener , LocationListener {

    DatabaseHelper databaseHelper;
    View view;
Button dateButton,timeButton,mapButton,shareButton,saveButton,deleteButton;
Location capturedLocation;
String capturedLocationString;
    TextInputEditText ed_loc,ed_title,ed_place,ed_detail;
    public static String tvLongi;
    public static String tvLati;
    LocationManager locationManager;
    private static final int MY_CAMERA_REQUEST_CODE = 101;
    private ImageView imgCapture;
    Bitmap tempBP;
    String imageName;
    int id;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_item_fragment, container, false);
        databaseHelper = new DatabaseHelper(getActivity());
        id = getArguments().getInt("id");


        dateButton = view.findViewById(R.id.bt_date);
        timeButton = view.findViewById(R.id.bt_time);
        mapButton = view.findViewById(R.id.show_map);
        saveButton = view.findViewById(R.id.save_btn);
        deleteButton = view.findViewById(R.id.delete_btn);
        shareButton = view.findViewById(R.id.share_btn);
        ed_loc = view.findViewById(R.id.ed_loc);
        ed_title = view.findViewById(R.id.ed_title);
        ed_place = view.findViewById(R.id.ed_place);
        ed_detail = view.findViewById(R.id.ed_detail);
        imgCapture= view.findViewById(R.id.im_receipt);

        Button b = view.findViewById(R.id.btn_cam);


        if(id != -1){

            setFields(id);
        }



        CheckPermission();

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("d MMM yyyy, HH:mm");
        String formattedDate = df.format(c);
        String date = formattedDate.split(",")[0];
        String time = formattedDate.split(",")[1];

if(id == -1){
    deleteButton.setVisibility(View.GONE);
    saveButton.setVisibility(View.VISIBLE);
    dateButton.setText(date);
    timeButton.setText(time);
    dateButton.setOnClickListener(this);
    timeButton.setOnClickListener(this);
    saveButton.setOnClickListener(this);
}
else {

    deleteButton.setVisibility(View.VISIBLE);
    saveButton.setVisibility(View.GONE);
b.setEnabled(false);
}
        mapButton.setOnClickListener(this);

        shareButton.setOnClickListener(this);
        deleteButton.setOnClickListener(this);


        b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (getActivity().checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
                    }else{
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, 100);

                    }
                }

            }
        });


        return view;
    }

    @Override
    public void onClick(View view) {
        if (view == dateButton) {

            final Calendar c = Calendar.getInstance();
            int  mYear = c.get(Calendar.YEAR);
            int  mMonth = c.get(Calendar.MONTH);
            int mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            Calendar calendar = Calendar.getInstance();
                            calendar.set(year, monthOfYear, dayOfMonth);

                            SimpleDateFormat format = new SimpleDateFormat("d MMM yyyy");
                            String strDate = format.format(calendar.getTime());
                            dateButton.setText(strDate);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();

        }
        else if (view == timeButton) {

            final Calendar c = Calendar.getInstance();
            int  mHour = c.get(Calendar.HOUR_OF_DAY);
            int mMinute = c.get(Calendar.MINUTE);
            TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

String stringHour = String.format("%02d" , hourOfDay);
String stringminute = String.format("%02d" , minute);
                            timeButton.setText(stringHour + ":" + stringminute);
                        }
                    }, mHour, mMinute, true);
            timePickerDialog.show();

        }else if (view == mapButton) {


            Intent intent = new Intent(getActivity(), MapActivity.class);
            intent.putExtra("location",capturedLocationString);
            startActivity(intent);

        }

        else if (view == shareButton) {
            shareIt();

        }

        else if (view == saveButton) {

            saveDetails();

        }
        else if (view == deleteButton) {

           if( databaseHelper.deleteCheckInByID(id)){

               Toast.makeText(getActivity(), "record deleted", Toast.LENGTH_SHORT).show();
getActivity().finish();

           }else {
               Toast.makeText(getActivity(), "error cannot delete record", Toast.LENGTH_SHORT).show();
           }

        }

        }




    public  void setFields(int id){
CheckInModels updatedCheckin =  databaseHelper.getCheckInByID(id);

ed_title.setText(updatedCheckin.getTitle());
ed_place.setText(updatedCheckin.getPlace());
ed_detail.setText(updatedCheckin.getDescription());
timeButton.setText(updatedCheckin.getTime());
dateButton.setText(updatedCheckin.getDate());
ed_loc.setText(updatedCheckin.getLocation());
capturedLocationString = updatedCheckin.getLocation();
imageName = updatedCheckin.getImage_path();




    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, 100);

            } else {
                Toast.makeText(getContext(), "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                Bitmap bp = (Bitmap) data.getExtras().get("data");
                tempBP = bp;
                imgCapture.setImageBitmap(bp);
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getContext(), "Cancelled", Toast.LENGTH_LONG).show();
            }
        }

    }


    public void CheckPermission() {
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED&&
                ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        getLocation();
    }

    @Override
    public void onPause() {
        super.onPause();
        locationManager.removeUpdates(ItemFragment.this);
    }

    public void getLocation() {
        try {
            locationManager = (LocationManager)getContext().getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    public void onLocationChanged(Location location) {

        tvLongi = String.valueOf(location.getLongitude());
        tvLati = String.valueOf(location.getLatitude());
        if(capturedLocation == null && capturedLocationString  == null) {


            capturedLocationString =tvLati + "," + tvLongi;
            ed_loc.setText(capturedLocationString);
            //    Toast.makeText(getContext(), "Longitude:" + tvLongi + "Latitude:" + tvLati, Toast.LENGTH_SHORT).show();
        }
        capturedLocation = location;
        locationManager.removeUpdates(ItemFragment.this);
    }


    @Override
    public void onProviderDisabled(String provider) {
        if(capturedLocationString==null)
        Toast.makeText(getContext(), "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        if(capturedLocationString==null)
        Toast.makeText(getContext(), "Enabled new provider!" + provider,
                Toast.LENGTH_SHORT).show();
    }

    private void shareIt() {

        String shareMessage = "";
        if(ed_detail.getText().toString().length() < 1 && ed_place.getText().toString().length() < 1 &&
                ed_title.getText().toString().length() < 1){

            Toast.makeText(getContext(), "please fill all fields", Toast.LENGTH_SHORT).show();

        }else
        {


            shareMessage = "At "+timeButton.getText().toString()+
                    " on "+dateButton.getText().toString()+
                    ", I visited "+ed_place.getText().toString()+
                    "."+ ed_detail.getText().toString();
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));

        }


    }


    private void saveDetails(){


        if(ed_detail.getText().toString().length() < 1 && ed_place.getText().toString().length() < 1 &&
                ed_title.getText().toString().length() < 1){

            Toast.makeText(getContext(), "please fill all fields", Toast.LENGTH_SHORT).show();

        }else
        {

            String nameFile = String.valueOf(System.currentTimeMillis());
            CheckInModels checkInModels = new CheckInModels();


            checkInModels.setTitle(ed_title.getText().toString());
            checkInModels.setPlace(ed_place.getText().toString());
            checkInModels.setDescription(ed_detail.getText().toString());
            checkInModels.setDate(dateButton.getText().toString());
            checkInModels.setTime(timeButton.getText().toString());
            checkInModels.setTime(timeButton.getText().toString());
            checkInModels.setLocation(capturedLocationString);
        //   if(tempBP!=null)
            checkInModels.setImage_path(nameFile);

         long  id =  databaseHelper.insertCheckins(checkInModels);
            Bitmap bm=((BitmapDrawable)imgCapture.getDrawable()).getBitmap();
           // if(tempBP!=null)
            createDirectoryAndSaveFile(bm,nameFile);
if (id!= -1){

    getActivity().finish();
}


        }





    }


    private void createDirectoryAndSaveFile(Bitmap imageToSave, String fileName) {
        String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
        OutputStream outStream = null;

        File file = new File(fileName + ".png");
        if (file.exists()) {
            file.delete();
            file = new File(extStorageDirectory, fileName + ".png");
            Log.e("file exist", "" + file + ",Bitmap= " + fileName);
        }
        try {
            // make a new bitmap from your file
            Bitmap bitmap =imageToSave;

            outStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.flush();
            outStream.close();
        } catch (Exception e) {
            Toast.makeText(getActivity(), "error saving image", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        Log.e("file", "" + file);
     //   return file;
    }


}
