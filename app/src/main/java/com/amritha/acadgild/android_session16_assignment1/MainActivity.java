package com.amritha.acadgild.android_session16_assignment1;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {

    //initializing EditText,TextView, Buttons

    EditText inputText;

    TextView contentData, finalResult;

    Button addData, deleteData;

    //creating a file test

    String filename = "test.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //finding view for EditText, TextView, Buttons

        inputText = findViewById(R.id.enterText);

        contentData = findViewById(R.id.contentData);

        finalResult = findViewById(R.id.tv_result);

        addData = findViewById(R.id.addButton);

        deleteData = findViewById(R.id.deleteButton);

        //implementing onClickListener for Buttons

        addData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Getting input texts
                String data = inputText.getText().toString();

                //checking Permissions to write External storage File

                if (isExternalStorageWritable() && checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    //creating text file
                    File textFile = new File(Environment.getExternalStorageDirectory(), filename);

                    try {
                        //creating output stream
                        FileOutputStream fos = new FileOutputStream(textFile);
                        fos.write(data.getBytes());

                        AsyncTaskRunner runner = new AsyncTaskRunner();
                        String sleepTime = "5";
                        runner.execute(sleepTime);

                        contentData.setText(data);
                        fos.close();

                        Toast.makeText(MainActivity.this, "File Created Succesfully", Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Cannot write to External Storage", Toast.LENGTH_LONG).show();
                }

            }

        });

        deleteData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File textFile = new File(Environment.getExternalStorageDirectory(), filename);
                if (textFile.exists()) {
                    deleteFile(filename);//deleting the file

                    AsyncTaskRunner runner = new AsyncTaskRunner();
                    String sleepTime = "5";
                    runner.execute(sleepTime);

                    Toast.makeText(MainActivity.this, "File deleted Successfully", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainActivity.this, "File not deleted", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    //creating AsynkTask class

    private class AsyncTaskRunner extends AsyncTask<String, String, String> {

        private String resp;
        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {
            publishProgress("Sleeping..."); // Calls onProgressUpdate()
            try {
                int time = Integer.parseInt(params[0]) * 1000;

                Thread.sleep(time);
                resp = "Slept for " + params[0] + " seconds";
            } catch (InterruptedException e) {
                e.printStackTrace();
                resp = e.getMessage();
            } catch (Exception e) {
                e.printStackTrace();
                resp = e.getMessage();
            }
            return resp;
        }


        @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation
            progressDialog.dismiss();
            finalResult.setText(result);
        }


        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(MainActivity.this,
                    "ProgressDialog",
                    "Wait for 5 " + " seconds");
        }


        @Override
        protected void onProgressUpdate(String... text) {
            finalResult.setText(text[0]);

        }
    }

    //checking permission for External storage

    public boolean isExternalStorageWritable() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            Log.i("State", "Yes, it is Writable");
            return true;
        } else {
            return false;
        }
    }

    public boolean checkPermission(String permission) {
        int check = ContextCompat.checkSelfPermission(this, permission);
        return (check == PackageManager.PERMISSION_GRANTED);
    }

    public boolean isExternalStorageReadable() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || Environment.MEDIA_MOUNTED_READ_ONLY.equals(Environment.getExternalStorageState())) {
            Log.i("State", "Yes, it is Readable");
            return true;
        } else {
            return false;
        }
    }

}
