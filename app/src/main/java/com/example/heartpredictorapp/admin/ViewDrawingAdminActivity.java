package com.example.heartpredictorapp.admin;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.heartpredictorapp.LoginActivity;
import com.example.heartpredictorapp.R;
import com.example.heartpredictorapp.adapters.DrawingsAdminAdapter;
import com.example.heartpredictorapp.adapters.DrawingsDoctorAdapter;
import com.example.heartpredictorapp.doctor.AddDiagnosisActivity;
import com.example.heartpredictorapp.doctor.DoctorActivity;
import com.example.heartpredictorapp.doctor.EditProfileDoctorActivity;
import com.example.heartpredictorapp.doctor.ViewDrawingDoctorActivity;
import com.example.heartpredictorapp.models.Doctor;
import com.example.heartpredictorapp.models.Parent;
import com.example.heartpredictorapp.models.Report;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ortiz.touchview.TouchImageView;

import java.io.InputStream;
import java.util.ArrayList;

public class ViewDrawingAdminActivity extends AppCompatActivity {

    ImageView imgBack, imgLogout, imgEditProfile;
    String strFile, drawID, parentID, reportID, doctorID, reportDate, strParentName, strDoctorName;
    private TouchImageView ZoomageView;
    AppCompatButton btnAnalyze;
    TextView txtDoctor, txtParent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_drawing_admin);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        strFile = getIntent().getStringExtra("file");
        drawID = getIntent().getStringExtra("drawID");
        parentID = getIntent().getStringExtra("parentID");
        doctorID = getIntent().getStringExtra("doctorID");
        reportID = getIntent().getStringExtra("reportID");
        reportDate = getIntent().getStringExtra("reportDate");

        imgBack = findViewById(R.id.imgBack);
        imgLogout = findViewById(R.id.imgLogout);
        imgEditProfile = findViewById(R.id.imgEditProfile);
        ZoomageView = findViewById(R.id.singleImage);
        btnAnalyze = findViewById(R.id.btnAnalyze);
        txtDoctor = findViewById(R.id.txtDoctor);
        txtParent = findViewById(R.id.txtParent);

        ReadParent();
        if(doctorID.equals("0")){
            txtDoctor.setText("AI Analyzed");
        }else{
            ReadDoctor();
        }

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference ref = storage.getReferenceFromUrl(strFile);
        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                new ViewImage(ZoomageView).execute(uri.toString());
                //Glide.with(getApplicationContext()).load(uri.toString()).into(imgDraw);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

        imgEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EditProfileAdminActivity.class);
                startActivity(intent);
            }
        });
        imgLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showConfirmationDialog();
            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        btnAnalyze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ViewDiagnosisAdminActivity.class);
                intent.putExtra("drawID", drawID);
                startActivity(intent);
            }
        });
    }
    private void showConfirmationDialog() {
        // Build the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.confirm_logout));
        builder.setMessage(getString(R.string.logout_msg));
        // Positive button (Proceed)
        builder.setPositiveButton(getString(R.string.yes), (dialog, which) -> {
            // Navigate to the login activity
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        });
        // Negative button (Cancel)
        builder.setNegativeButton(getString(R.string.no), (dialog, which) -> {
            // Dismiss the dialog
            dialog.dismiss();
        });
        // Show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private class ViewImage extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;
        public ViewImage(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    public void ReadParent() {
        DatabaseReference databaseGroup = FirebaseDatabase.getInstance().getReference("parent");
        databaseGroup.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap : snapshot.getChildren()) {
                    Parent groupsRecord = snap.getValue(Parent.class);
                    if (groupsRecord.getParentID().equals(parentID)) {
                        strParentName = groupsRecord.getParentName();
                        break; // Exit loop after update
                    }
                }
                txtParent.setText("Parent: " + strParentName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FIREBASE", "Database error: " + error.getMessage());
            }
        });
    }
    public void ReadDoctor() {
        DatabaseReference databaseGroup = FirebaseDatabase.getInstance().getReference("doctor");
        databaseGroup.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap : snapshot.getChildren()) {
                    Doctor groupsRecord = snap.getValue(Doctor.class);
                    if (groupsRecord.getDoctorID().equals(doctorID)) {
                        strDoctorName = groupsRecord.getDoctorName();
                        break; // Exit loop after update
                    }
                }
                txtDoctor.setText("Doctor: " + strDoctorName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FIREBASE", "Database error: " + error.getMessage());
            }
        });
    }
}