package com.example.rationable;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.barcodescanner.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;

import java.util.List;

public class BarcodeScannerActivity extends AppCompatActivity {


    // i used this tutorial to help me a lot with the barcode scanner
    //https://youtu.be/jngzWPWWiKE?si=2oZkfmtsJ-2Y5P-u

    //UI Views
    private MaterialButton cameraBtn;
    private MaterialButton galleryBtn;
    private ImageView imageIv;
    private MaterialButton scanBtn;
    private TextView resultTv;

    //to handle the result of Camera/Gallery permission onRequestPermissionResults

    private static final int CAMERA_REQUEST_CODE=100;
    private static final int STORAGE_REQUEST_CODE=101;

    //arrays of permisisons required to pick image from Camera/Gallery

    private String[] cameraPermissions;
    private String[] storagePermissions;

    //Uri of the image we will take from Camera/Gallery

    private Uri imageUri=null;

    private BarcodeScannerOptions barcodeScannerOptions;
    private BarcodeScanner barcodeScanner;

    private static final String TAG = "MAIN_TAG";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_barcode);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

        //back button just so i can find it easy
        MaterialButton homeBarBtn =  findViewById(R.id.homeBarBtn);
        homeBarBtn.setOnClickListener(v -> {
            Intent intent = new Intent(BarcodeScannerActivity.this, StartActivity.class);
            startActivity(intent);
        });



        //init UI Views

        cameraBtn=findViewById(R.id.cameraBtn);
        galleryBtn=findViewById(R.id.galleryBtn);
        imageIv=findViewById(R.id.imageIv);
        scanBtn=findViewById(R.id.scanBtn);
        resultTv=findViewById(R.id.resultTv);

        //init the arrays of permissions required to pick image from Camera/Gallery
        cameraPermissions=new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}; //Image From Camera: CAMERA and WRITE_EXTERNAL_STORAGE permissions
        storagePermissions=new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}; //Image from Gallery: WRITE_EXTERNAL_STORAGE permission only

        /*init/setup BarcodeScannerOptions, put comma separated types in .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS) or
         * add Barcode.FORMAT_ALL_FORMATS if you want to scan all formats
         * The following formats are supported:
         * Code 128 (FORMAT_CODE_128), Code 39 (FORMAT_CODE_39), Code 93 (FORMAT_CODE_93),
         * Codabar (FORMAT_CODABAR). EAN-13 (FORMAT_EAN_13), EAN-8 (FORMAT_EAN_8), ITF (FORMAT_ITF),
         * UPC-A (FORMAT_UPC_A),  UPC-E (FORMAT_UPC_E), QR Code (FORMAT_QR_CODE), PDF417 (FORMAT_PDF417),
         * Aztec (Format_Aztec), Data Matrix (FORMAT_DATA_MATRIX)*/

        barcodeScannerOptions = new BarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS).build();

        //init BarcodeScanner with BarcodeScannerOptions
        barcodeScanner = BarcodeScanning.getClient(barcodeScannerOptions);

        //handle cameraBtn click, check permissions related to Camera (i.e write storage and camera) and take image from Camera

        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkCameraPermission()){
                    //permission required for camera already granted, launch camera intent
                    pickImageCamera();
                }else{
                    //permission required for camera was not already granted, request permissions
                    requestCameraPermission();
                }

            }
        });

        //handle galleryBtn click, check permission related to Gallery (i.e write storage) and take image from Camera

        galleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkStoragePermission()){
                    //permission required for gallery already granted, launch gallery intent
                    pickImageGallery();
                }else{
                    //permission required for gallery was not already granted, request permissions
                    requestStoragePermission();
                }

            }
        });

        //handle scanBtn click, scan the Barcode from image picked from Camera/Gallery

        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(imageUri == null){

                    Toast.makeText(BarcodeScannerActivity.this, "Pick image first...", Toast.LENGTH_SHORT).show();

                }else{

                    detectResultFromImage();

                }

            }
        });


    }

    private void detectResultFromImage() {

        try {
            //prepare image form image uri
            InputImage inputImage = InputImage.fromFilePath(this, imageUri);
            //start scanning the barcode data from image
            Task<List<Barcode>> barcodeResult = barcodeScanner.process(inputImage)
                    .addOnSuccessListener(new OnSuccessListener<List<Barcode>>() {
                @Override
                public void onSuccess(List<Barcode> barcodes) {
                    //Task completed successfully, we can get detailed info now
                    extractBarcodeInfo(barcodes);
                }
            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //Task failed with an exception, we can't get any detail
                            String errorMessage = (e.getMessage()!=null) ? e.getMessage(): "unknown error";
                           // Toast.makeText(this, "Failed scanning", Toast.LENGTH_SHORT).show();
                        }
                    });

        } catch (Exception e){
            //failed with n exception either dure to preparing InputImage or issue in BarcodeScanner init
            Toast.makeText(this, "Failed due to "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    @SuppressLint("SetTextI18n")
    private void extractBarcodeInfo(List<Barcode> barcodes) {
        //Get information form barcodes
        for (Barcode barcode: barcodes){
            Rect bounds;
            bounds = barcode.getBoundingBox();
            Point[] corners = barcode.getCornerPoints();

            //raw info scanned from the barcode
            String rawValue = barcode.getRawValue();
            Log.d(TAG, "extractBarcodeInfo: rawValue: "+rawValue);

            /* the following types are supported:
            Barcode.TYPE_UNKNOWN, Barcode.TYPE_CONTACT_INFO, Barcode.TYPE_EMAIL, Barcode.TYPE_ISBN,
            Barcode.TYPE_PHONE, Barcode.TYPE_PRODUCT, Barcode.TYPE_SMS, Barcode.TYPE_TEXT,
            Barcode.TYPE_URL, Barcode.TYPE_WIFI, Barcode.TYPE_GEO, Barcode.TYPE_CALENDER_EVENT,
            Barcode.TYPE_DRIVER_LICENSE*/
            int valueType = barcode.getValueType();

            resultTv.setText("raw value: "+rawValue);

            /*doing a bit of research ive determined that UPC-A and EAN-13 are the barcodes most often
            used for food products im assuming that they would be considered under Barcode.TYPE_PRODUCT
             or Barcode.TYPE_UNKNOWN so I'm just going to manage those atm

             on second thought im don't think i need to parse the raw data sense im going to be using
             https://www.barcodelookup.com/ for that
             */
            /*switch (valueType){
                case Barcode.TYPE_PRODUCT:{

                    Barcode

                }
                break;

            }*/

            


        }

    }

    private void pickImageGallery(){
        //intent to pick image from gallery, will show all resources from where we can pick the image
        Intent intent = new Intent(Intent.ACTION_PICK);
        //set type of file we want to pick i.e image
        intent.setType("image/*");
        galleryActivityResultLauncher.launch(intent);

    }

    private final ActivityResultLauncher<Intent> galleryActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    //here we will recive the image, if picked from gallery
                    if(result.getResultCode()== BarcodeScannerActivity.RESULT_OK){
                        //image picked, get the uri of the image picked
                        Intent data= result.getData();
                        imageUri=data.getData();
                        Log.d(TAG, "onActivityResult: "+imageUri);
                        //set to imageview
                        imageIv.setImageURI(imageUri);

                    }else{
                        //cancelled
                        Toast.makeText(BarcodeScannerActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();

                    }

                }
            }
    );

    private void pickImageCamera(){
        //get ready the image data to store in MediaStore
        ContentValues contentValues= new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, "Sample Title");
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Sample Image Description");
        //Image Uri
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        //Intent to launch camera
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        cameraActivityResultLauncher.launch(intent);

    }

    private  final ActivityResultLauncher<Intent> cameraActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    //here we will receive the image, if taken from camera
                    if(result.getResultCode() == BarcodeScannerActivity.RESULT_OK){
                        //image is taken from camera
                        Intent data = result.getData();
                        //we already have the image in the imageUri using function pickImageCamera()
                        Log.d(TAG, "onActivityResult: imageUri: "+imageUri);
                        //set to imageview
                        imageIv.setImageURI(imageUri);

                    }else{
                        //cancelled
                        Toast.makeText(BarcodeScannerActivity.this, "Cancelled...", Toast.LENGTH_SHORT).show();

                    }

                }
            }

    );

    private boolean checkStoragePermission(){

        /*check if storage permission is allowed or not
        return true if allowed, false if not allowed */

        boolean result= ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
        return result;
    }

    private  void requestStoragePermission(){
        //request storage permission (for gallery image pick)
        ActivityCompat.requestPermissions(this, storagePermissions, STORAGE_REQUEST_CODE);
    }

    private boolean checkCameraPermission(){
        //check if camera permission is allowed, true if yes false if no
        boolean resultCamera= ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED;
        //check if storage permission is allowed, true if yes false if no
        boolean resultStorage = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
        //return both results as true/false
        return resultCamera && resultStorage;
    }

    private void requestCameraPermission(){

        ActivityCompat.requestPermissions(this, cameraPermissions, CAMERA_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case CAMERA_REQUEST_CODE:{
                //Check if some action from permission dialog performed or not Allow/Deny
                if(grantResults.length>0){
                    //Check if Camera, Storage permissions granted, contains boolean results either true or false
                    boolean cameraAccepted= grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted= grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    //Check if both permissions are granted or not
                    if(cameraAccepted && storageAccepted){
                        //both permissions (Camera & Gallery) are granted, we can launch camera intent
                        pickImageCamera();

                    }else{
                        //one or both permissions are denied, can't launch camera intent
                        Toast.makeText(this, "Camera & Storage permissions are required...", Toast.LENGTH_SHORT).show();
                    }
                }


            }
            break;
            case STORAGE_REQUEST_CODE:{
                //Check if some action from permission dialog performed or not Allow/Deny
                if(grantResults.length>0){
                    //Check if Storage permissions Granted, contains boolean results either true or false
                    boolean storageAccepted= grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    //Check if storage permission is granted or not
                    if(storageAccepted){
                        //storage permission granted, we can launch gallery intent
                        pickImageGallery();

                    }else{
                        //storage permission denied, can't launch gallery intent
                        Toast.makeText(this, "Storage Permission is required...", Toast.LENGTH_SHORT).show();

                    }

                }

            }
            break;
        }
    }
}