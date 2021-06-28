package com.example.kishoreg17pdfreader;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.DownloadManager;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{

    private RecyclerView recyclerView;
    private int REQUEST_PERMISSION=1;
    private pdfAdapter pdfadapter;
    public static ArrayList<File> myPdfFiles=new ArrayList<>(),specificFiles=new ArrayList<>();
    private File directory=new File("/mnt/");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView=(RecyclerView)findViewById(R.id.recycleviewer);

        GridLayoutManager gridLayoutManager=new GridLayoutManager(MainActivity.this,2);
        recyclerView.setLayoutManager(gridLayoutManager);


    }

    @Override
    protected void onStart() {
        super.onStart();
        myPdfFiles.clear();
        specificFiles.clear();
//        directory=new File("/mnt/");
        permissionAsking();
    }

    private void permissionAsking() {

        if((ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)){
            AccessFiles(directory);
        }
        else{
            if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)){
                new AlertDialog.Builder(this)
                        .setTitle("Permission Needed:")
                        .setMessage("This permission is needed to access your SD card")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                              ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_PERMISSION);
                              AccessFiles(directory);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create().show();
            }
            else{
                ActivityCompat.requestPermissions(MainActivity.this,new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_PERMISSION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == REQUEST_PERMISSION){

            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this,"Permission Granted",Toast.LENGTH_SHORT).show();
                AccessFiles(directory);
            }
            else{
                Toast.makeText(this,"Permission Denied",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void AccessFiles(File directory) {

        File[] files=directory.listFiles();

        if(files!=null && files.length>0){

//            myPdfFiles.clear();

            for(File file : files) {

                if (file.isDirectory()) {

                    AccessFiles(file);

                } else if (file.getName().endsWith(".pdf")) {

                    myPdfFiles.add(file);
                }
            }
            pdfadapter=new pdfAdapter(MainActivity.this,myPdfFiles);
            recyclerView.setAdapter(pdfadapter);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.search_bar,menu);
        MenuItem menuItem=menu.findItem(R.id.search_icon);
        SearchView searchView=(SearchView)menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                specificFiles.clear();
                SearchFiles(newText,directory);
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);

    }

    private void SearchFiles(String s,File directory){

        if(!s.equals("")){

            File[] files=directory.listFiles();

            if(files != null && files.length>0){

                for(File file : files) {

                    if (file.isDirectory()) {

                        SearchFiles(s, file);

                    }
                    else {

                        if (file.getName().endsWith(".pdf")) {

                            String filename = file.getName().replace(".pdf", "").toString().toLowerCase();

                            if(filename.startsWith(s)){

                                specificFiles.add(file);
                            }

                        }

                    }
                }
                pdfadapter=new pdfAdapter(getApplicationContext(),specificFiles);
                recyclerView.setAdapter(pdfadapter);
            }
        }
        else{
            myPdfFiles.clear();
            AccessFiles(directory);
        }

    }

}
