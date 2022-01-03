package com.example.pdf;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private CheckBox cboMale, cboFemale;
    private EditText edtName, edtDateOfBirth, edtPlaceOfBirth, edtNationality, edtIdentityNumber, edtDateOfDistribution, edtPlaceOfDistribution, edtPermanentAddress;
    private Button btnExportPdf;
    private View view;
    private NestedScrollView nestedScrollView;
    public static final int PERMISSION_REQUEST_CODE = 110;
    private Display mDisplay;
    private int totalHeight;
    private int totalWidth;
    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edtName = findViewById(R.id.edt_name);
        edtDateOfBirth = findViewById(R.id.edt_date_of_birth);
        edtPlaceOfBirth = findViewById(R.id.edt_place_of_birth);
        edtNationality = findViewById(R.id.edt_nationality);
        edtIdentityNumber = findViewById(R.id.edt_identity_number);
        edtDateOfDistribution = findViewById(R.id.edt_date_of_distribution);
        edtPlaceOfDistribution = findViewById(R.id.edt_place_of_distribution);
        edtPermanentAddress = findViewById(R.id.edt_permanent_address);
        cboMale = findViewById(R.id.cbo_male);
        cboFemale = findViewById(R.id.cbo_female);
        btnExportPdf = findViewById(R.id.btn_export_pdf);
        nestedScrollView = findViewById(R.id.ticket);
        view = findViewById(R.id.ticket);

        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        mDisplay = wm.getDefaultDisplay();

        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
            } else {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            }
        }

        cboMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cboMale.setChecked(true);
                cboFemale.setChecked(false);
            }
        });
        cboFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cboMale.setChecked(false);
                cboFemale.setChecked(true);
            }
        });


        btnExportPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeScreenShot();
                Intent intent = new Intent(MainActivity.this, PDFActivity.class);
                intent.putExtra("path", path);
                startActivity(intent);

            }
        });
    }

    private void showInfo() {
        cboMale.setChecked(true);
        edtName.setText("Nguyễn Đức Anh");
        edtDateOfBirth.setText("12/04/2000");
        edtPlaceOfBirth.setText("Thanh Hồng Thanh Hà Hải Dương");
        edtNationality.setText("Việt Nam");
        edtIdentityNumber.setText("030200004949");
        edtDateOfDistribution.setText("27/06/2017");
        edtPlaceOfDistribution.setText("Cục CS Hải Dương");
        edtPermanentAddress.setText("Thanh Hồng Thanh Hà Hải Dương");
    }

    private void takeScreenShot() {
        Bitmap bitmap = getBitmapFromView(view);
        createPdf(bitmap);
    }

    public Bitmap getBitmapFromView(View view) {
        // Layout -> Bitmap -> canvas image
        totalWidth = nestedScrollView.getChildAt(0).getWidth();
        totalHeight = nestedScrollView.getChildAt(0).getHeight();
        Bitmap returnedBitmap = Bitmap.createBitmap(totalWidth, totalHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null) {
            bgDrawable.draw(canvas);
        } else {
            canvas.drawColor(Color.WHITE);
        }
        view.draw(canvas);
        return returnedBitmap;
    }

    private void createPdf(Bitmap bitmap) {
        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#ffffff"));
        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(bitmap.getWidth(), bitmap.getHeight(), 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();
        canvas.drawPaint(paint);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);
        canvas.drawBitmap(scaledBitmap, 0, 0, null);
        document.finishPage(page);
        path = Environment.getExternalStorageDirectory().toString() + "/Pictures";
        String file_name = "Screenshot";
        path +=  file_name+".pdf";
        File filePath = new File(path);
        try {
            document.writeTo(new FileOutputStream(filePath));
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
        document.close();
    }
}