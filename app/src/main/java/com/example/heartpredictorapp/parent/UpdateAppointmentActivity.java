package com.example.heartpredictorapp.parent;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.heartpredictorapp.LoginActivity;
import com.example.heartpredictorapp.R;
import com.example.heartpredictorapp.models.Appointment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Locale;

public class UpdateAppointmentActivity extends AppCompatActivity {

    TextView txtAppDate, txtAppTime;
    ImageView imgBack, imgLogout, imgEditProfile;
    AppCompatButton btnAdd;
    ProgressBar progress;
    String strDoctorName, strDoctorID, strParentID, strAppDate, strAppTime, appID, appState;
    Boolean app_found =false;
    private Calendar calendar;
    int selected_day = 0, selected_month=0, selected_year = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_appointment);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        appID = getIntent().getStringExtra("id");
        strParentID = getIntent().getStringExtra("parent");
        strDoctorID = getIntent().getStringExtra("doctorID");
        strDoctorName = getIntent().getStringExtra("doctorName");
        strAppDate = getIntent().getStringExtra("appDate");
        strAppTime = getIntent().getStringExtra("appTime");
        appState = getIntent().getStringExtra("appState");

        txtAppDate = findViewById(R.id.txtAppDate);
        txtAppTime = findViewById(R.id.txtAppTime);
        btnAdd = findViewById(R.id.btnAdd);
        imgBack = findViewById(R.id.imgBack);
        progress = findViewById(R.id.progress);
        imgLogout = findViewById(R.id.imgLogout);
        imgEditProfile = findViewById(R.id.imgEditProfile);

        txtAppDate.setText(strAppDate);
        txtAppTime.setText(strAppTime);

        imgEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EditProfileParentActivity.class);
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
                Intent intent = new Intent(getApplicationContext(), ManageAppointmentActivity.class);
                startActivityForResult(intent, 100);
            }
        });
        txtAppDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);
                DatePickerDialog datePicker;
                datePicker = new DatePickerDialog(UpdateAppointmentActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        selected_day = i2;
                        selected_month = i1 + 1;
                        selected_year = i;
                        txtAppDate.setText(selected_year + "-" + selected_month + "-" + selected_day);
                    }
                }, year, month, day);
                datePicker.setTitle("Appointment Date");
                datePicker.show();
            }
        });
        txtAppTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);

                TimePickerDialog timePicker = new TimePickerDialog(UpdateAppointmentActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                                txtAppTime.setText(String.format(Locale.getDefault(), "%02d:%02d", selectedHour, selectedMinute));
                            }
                        }, hour, minute, true); // `true` for 24-hour format, use `false` for 12-hour format

                timePicker.setTitle("Appointment Time");
                timePicker.show();
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtAppDate.getText().toString().trim().isEmpty()) {
                    txtAppDate.setError(getString(R.string.app_date_required));
                    txtAppDate.requestFocus();
                    return;
                }
                if (txtAppTime.getText().toString().trim().isEmpty()) {
                    txtAppTime.setError(getString(R.string.app_time_required));
                    txtAppTime.requestFocus();
                    return;
                } else {
                    strAppDate = txtAppDate.getText().toString().trim();
                    strAppTime = txtAppTime.getText().toString().trim();
                    UpdateAppointment();
                }
            }
        });

    }

    public void UpdateAppointment() {
        progress.setVisibility(View.VISIBLE);
        DatabaseReference databaseAppointment = FirebaseDatabase.getInstance().getReference("appointment");
        databaseAppointment.addListenerForSingleValueEvent(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot snapshot) {
                       DatabaseReference dR = FirebaseDatabase.getInstance().getReference("appointment").child(appID);
                       Appointment user = new Appointment(appID, strParentID, strDoctorID,strDoctorName, strAppDate, strAppTime, appState);
                       databaseAppointment.child(appID).setValue(user);
                       progress.setVisibility(View.INVISIBLE);
                       //displaying a success toast
                       Toast.makeText(getApplicationContext(), "Appointment updated successfully", Toast.LENGTH_LONG).show();
                       Intent intent = new Intent(getApplicationContext(), ManageAppointmentActivity.class);
                       startActivityForResult(intent, 100);
                   }
                   @Override
                   public void onCancelled(@NonNull DatabaseError error) {

                   }
               }
        );
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
}