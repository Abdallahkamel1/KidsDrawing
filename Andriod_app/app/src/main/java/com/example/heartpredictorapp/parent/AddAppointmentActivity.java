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
import com.example.heartpredictorapp.models.Child;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Locale;

public class AddAppointmentActivity extends AppCompatActivity {

    TextView txtDoctor, txtAppDate, txtAppTime;
    ImageView imgBack, imgLogout, imgEditProfile;
    AppCompatButton btnAdd;
    ProgressBar progress;
    String strDoctorName, strDoctorID, strParentID, strAppDate, strAppTime;
    Boolean app_found =false;
    private Calendar calendar;
    int selected_day = 0, selected_month=0, selected_year = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_appointment);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        strDoctorID = getIntent().getStringExtra("id");
        strDoctorName = getIntent().getStringExtra("name");

        txtDoctor = findViewById(R.id.txtDoctor);
        txtAppDate = findViewById(R.id.txtAppDate);
        txtAppTime = findViewById(R.id.txtAppTime);
        btnAdd = findViewById(R.id.btnAdd);
        imgBack = findViewById(R.id.imgBack);
        progress = findViewById(R.id.progress);
        imgLogout = findViewById(R.id.imgLogout);
        imgEditProfile = findViewById(R.id.imgEditProfile);

        txtDoctor.setText("Appointment: Dr." + strDoctorName);

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
                Intent intent = new Intent(getApplicationContext(), ParentAllDoctorsActivity.class);
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
                datePicker = new DatePickerDialog(AddAppointmentActivity.this, new DatePickerDialog.OnDateSetListener() {
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

                TimePickerDialog timePicker = new TimePickerDialog(AddAppointmentActivity.this,
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
                    strParentID = LoginActivity.parent.getParentID();
                    AddAppointment();
                }
            }
        });

    }

    public void AddAppointment() {
        progress.setVisibility(View.VISIBLE);
        DatabaseReference databaseAppointment = FirebaseDatabase.getInstance().getReference("appointment");
        databaseAppointment.addListenerForSingleValueEvent(new ValueEventListener() {
                 @Override
                 public void onDataChange(@NonNull DataSnapshot snapshot) {
                     for (DataSnapshot snap : snapshot.getChildren()) {
                         Appointment appsRecord = snap.getValue(Appointment.class);
                         if (appsRecord.getParentID().equals(strParentID) && appsRecord.getDoctorID().equals(strDoctorID) && appsRecord.getAppStatus().equals("wait")) {
                             app_found = true;
                         }
                     }
                     if (app_found == true) {
                         Toast.makeText(getApplicationContext(), getString(R.string.wait_appointment), Toast.LENGTH_LONG).show();
                         app_found = false;
                         progress.setVisibility(View.INVISIBLE);
                     } else {
                         String id = databaseAppointment.push().getKey();
                         String state = "wait";
                         Appointment user = new Appointment(id, strParentID, strDoctorID,strDoctorName, strAppDate, strAppTime, state);
                         databaseAppointment.child(id).setValue(user);
                         progress.setVisibility(View.INVISIBLE);
                         //displaying a success toast
                         Toast.makeText(getApplicationContext(), "New Appointment added successfully", Toast.LENGTH_LONG).show();
                         Intent intent = new Intent(getApplicationContext(), ViewDoctorsActivity.class);
                         startActivityForResult(intent, 100);
                     }
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