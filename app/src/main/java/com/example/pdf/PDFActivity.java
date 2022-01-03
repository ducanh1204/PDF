package com.example.pdf;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;

public class PDFActivity extends AppCompatActivity {

    private PDFView pdfView;
    private String path;
    private File file;
    private byte[] imageByteArray;
    private Button btnPrint;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfactivity);
        pdfView = findViewById(R.id.pdfView);

        Intent intent = getIntent();
        path = intent.getStringExtra("path");
        file = new File(path);
        pdfView.fromFile(file).load();


    }

}