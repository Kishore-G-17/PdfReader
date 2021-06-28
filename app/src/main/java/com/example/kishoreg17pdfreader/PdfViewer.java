package com.example.kishoreg17pdfreader;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;

public class PdfViewer extends AppCompatActivity {

    private int position;
    private PDFView pdfView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_viewer);

        Intent i=getIntent();
        Bundle bundle=i.getExtras();
        position=bundle.getInt("position",-1);

        pdfView=(PDFView)findViewById(R.id.pdfView);

        DisplayPdf();
    }

    private void DisplayPdf() {

        pdfView.fromFile(MainActivity.myPdfFiles.get(position))
                .enableSwipe(true)
                .enableAnnotationRendering(true)
                .scrollHandle(new DefaultScrollHandle(this))
                .load();
    }
}
