package com.home.khalil.smartshop;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudant.client.api.ClientBuilder;
import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

import kotlin.jvm.internal.MagicApiIntrinsics;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    URL u;
    private RecyclerView productsRecycler;
    private  RecyclerViewProductAdapter mAdapter;

    private ArrayList<Product> productList;
    public static Button pay;
    public static TextView empty;
    final int PIC_CROP = 2;
    String id;
    Product p;
    ProgressBar progressBar;
    Database database;
    ProgressDialog progressDialog;

    CloudantClient client;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private String  ROOT_URL="http://max-ocr-max-deployments.mycluster-ishanitekton4-162e406f043e20da9b0ef0731954a894-0000.us-south.containers.appdomain.cloud/";
    FloatingActionButton btn;
    Retrofit retro;
    APIservice api;
    public static TextView amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //image = (ImageView) findViewById(R.id.img);
        getSupportActionBar().hide();
        btn = (FloatingActionButton) findViewById(R.id.camera);
        amount = (TextView) findViewById(R.id.total_amount);
        empty = (TextView) findViewById(R.id.empty);

        pay = (Button) findViewById(R.id.pay);


        progressDialog = new ProgressDialog(MainActivity.this,R.style.MyAlertDialogStyle);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);

        //ProgressBar progressbar=(ProgressBar) progressDialog.findViewById(android.R.id.progress);
        //progressbar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#348EE5"), android.graphics.PorterDuff.Mode.SRC_IN);

        
        retro  = new Retrofit.Builder().baseUrl(ROOT_URL).addConverterFactory(GsonConverterFactory.create()).build();
        api = retro.create(APIservice.class);


        productList = new ArrayList<Product>();

        mAdapter = new RecyclerViewProductAdapter(productList);
        productsRecycler = (RecyclerView) findViewById(R.id.recycler_view);
        productsRecycler.setLayoutManager(new LinearLayoutManager(this));
        productsRecycler.setAdapter(mAdapter);







        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int permission = ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

                if (permission != PackageManager.PERMISSION_GRANTED) {
                    // We don't have permission so prompt the user
                    ActivityCompat.requestPermissions(
                            MainActivity.this,
                            PERMISSIONS_STORAGE,
                            REQUEST_EXTERNAL_STORAGE
                    );

                }else{
                    dispatchTakePictureIntent();
                }

            }
        });

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage("Processing Payment...");
                progressDialog.show();
                new updateQuantity().execute();

            }
        });

    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            Uri tempUri = getImageUri(getApplicationContext(), imageBitmap);
            String path =getRealPathFromURI(tempUri);
            Log.d("qwerty",imageBitmap+ " "+path );

            uploadImage(path);

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, timeStamp, null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        String path = "";
        if (getContentResolver() != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;
    }



    private void uploadImage(String path){
        empty.setVisibility(View.INVISIBLE);
        progressDialog.setMessage("Getting Product...");
        progressDialog.show();
        final File file = new File(path);
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
        Log.d("ICE", file.getName()+"");

        Call call = api.uploadImage(body);

        call.enqueue(new Callback<TextResult>() {
            @Override
            public void onResponse(Call<TextResult> call, Response<TextResult> response) {
                id= response.body().getText().get(0).get(0);
                if(file.exists()) {
                    file.delete();
                }

               // Toast.makeText(MainActivity.this, "Successfully Added: "+response.body().getText(), Toast.LENGTH_SHORT).show();
                Log.d("response",response.body().getText().toString()+" --- "+ id);

                new MyTask().execute();
            }
            @Override
            public void onFailure(Call call, Throwable t) {
                Log.d("response","Error"+ t.toString());
            }
        });


    }

    private class MyTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                u = new URL("https://5fa53148-f184-4963-a268-3e1811fd1b7d-bluemix.cloudantnosqldb.appdomain.cloud");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            client = ClientBuilder.url(u)
                    .iamApiKey("WWx01ngH-a02PYgPd6fLc0awz1ed9y_-3wulnqgBrhKl")
                    .build();

            database = client.database("smartshop-db", false);

            p =database.find(Product.class,id);
            p.setQuantity(1);
            Log.d("response","hey: "+ p.getTitle());
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
           progressDialog.hide();
            productsRecycler.setVisibility(View.VISIBLE);
            pay.setVisibility(View.VISIBLE);
            productList.add(p);
            mAdapter.notifyItemInserted(productList.size());

            int current_amount = Integer.parseInt(String.valueOf(amount.getText()));
            current_amount+=p.getPrice();
            amount.setText(current_amount+"");




        }
    }

    private class updateQuantity extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

         /* try {
                u = new URL("https://5fa53148-f184-4963-a268-3e1811fd1b7d-bluemix.cloudantnosqldb.appdomain.cloud");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            client = ClientBuilder.url(u)
                    .iamApiKey("WWx01ngH-a02PYgPd6fLc0awz1ed9y_-3wulnqgBrhKl")
                    .build();

            Database database = client.database("smartshop-db", false);*/



            for (int i=0; i<productList.size();i++){
                Product p = productList.get(i);
                int totalQuantity= p.getTotalQuantity();
                int currentQuantity = p.getQuantity();
                p.setTotalQuantity(totalQuantity+currentQuantity);
                database.update(p);

            }


            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.hide();
            Toast.makeText(MainActivity.this,"SUCCESS", Toast.LENGTH_LONG).show();
            productList.clear();
            mAdapter.notifyDataSetChanged();
            empty.setVisibility(View.VISIBLE);
            amount.setText("0");
            pay.setVisibility(View.INVISIBLE);

        }
    }
}
