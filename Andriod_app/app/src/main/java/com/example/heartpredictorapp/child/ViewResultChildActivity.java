package com.example.heartpredictorapp.child;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.heartpredictorapp.LoginActivity;
import com.example.heartpredictorapp.R;
import com.example.heartpredictorapp.doctor.ViewDrawingDoctorActivity;
import com.example.heartpredictorapp.parent.EditProfileParentActivity;
import com.example.heartpredictorapp.parent.ViewDrawingParentActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ViewResultChildActivity extends AppCompatActivity {
    Interpreter tflite;
    String strFile;
    String fileUrl;
    ProgressBar progress;
    ImageView imgBack, imgLogout, imgEditProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_result_child);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        strFile = getIntent().getStringExtra("file");
        TextView txtDiagnose = findViewById(R.id.txtDiagnose);
        TextView txtRecommend = findViewById(R.id.txtRecommend);
        progress = findViewById(R.id.progress);
        imgBack = findViewById(R.id.imgBack);
        imgLogout = findViewById(R.id.imgLogout);
        imgEditProfile = findViewById(R.id.imgEditProfile);

        imgEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EditProfileChildActivity.class);
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
                Intent intent = new Intent(getApplicationContext(), ChildActivity.class);
                startActivity(intent);
            }
        });

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference ref = storage.getReferenceFromUrl(strFile);
        try {
            tflite = new Interpreter(loadModelFile("my_model.tflite"));  // Make sure model.tflite is in assets/
        } catch (IOException e) {
            e.printStackTrace();
        }

        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

            @Override
            public void onSuccess(Uri uri) {
                progress.setVisibility(View.VISIBLE);
                ExecutorService executor = Executors.newSingleThreadExecutor();
                Handler handler = new Handler(Looper.getMainLooper());
                fileUrl = uri.toString();
                Log.d("Firebase", "File URL: " + fileUrl);
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        // Background work (network call)
                        Bitmap bitmap = getBitmapFromURL(fileUrl); // fileUrl from getDownloadUrl()

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                // UI thread (update UI or run model)
                                if (bitmap != null) {
                                    float[][][][] inputVal = preprocessBitmap(bitmap, 224);
                                    float[][] outputVal = new float[1][4];
                                    tflite.run(inputVal, outputVal);
                                    int maxIndex = 0;
                                    for (int i = 1; i < 4; i++) {
                                        if (outputVal[0][i] > outputVal[0][maxIndex]) {
                                            maxIndex = i;
                                        }
                                    }
                                    String state = ""; String recommend = "";
                                    if(maxIndex == 0) {
                                        state = "Anger and  Aggression";
                                        recommend = "To support a child with anger and aggression, identify triggers, teach emotional expression, and model calming techniques. Maintain a consistent routine, reinforce positive behavior, and avoid rewarding aggression. If needed, seek help from a mental health professional.";
                                    }
                                    else if(maxIndex == 1){
                                        state = "Fear and Anxiety";
                                        recommend = "To help a child with fear and anxiety, acknowledge and validate their feelings, encouraging them to express worries openly. Teach simple coping techniques like deep breathing and use stories to normalize emotions. Gradually expose them to fears in supportive steps, praise their efforts, and maintain consistent routines to build security. Model calm behavior, promote healthy sleep and activity, and seek professional help if anxiety affects daily functioning.";
                                    }
                                    else if(maxIndex == 2){
                                        state = "Happy ad Positive";
                                        recommend = "To nurture a child's happiness, encourage self-expression, foster optimism, and model positivity. Support healthy friendships, create fun and creative opportunities, and teach emotional balance. Promote kindness and helping others to strengthen their sense of purpose and happiness.";
                                    }
                                    else if(maxIndex == 3){
                                        state = "Sad";
                                        recommend = "When a child is sad, approach their emotions with empathy by acknowledging their feelings and offering comfort. Create a safe environment, encourage open communication, and engage in activities they enjoy. Teach coping skills, support social connections, and model healthy emotional expression. If sadness persists, consider seeking professional help.";
                                    }
                                    progress.setVisibility(View.INVISIBLE);
                                    txtDiagnose.setText("Predicted Child: " + state);
                                    txtRecommend.setText(recommend);
                                }
                            }
                        });
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e("Firebase", "Failed to get download URL", exception);
            }
        });
    }

    private MappedByteBuffer loadModelFile(String modelPath) throws IOException {
        AssetFileDescriptor fileDescriptor = getAssets().openFd(modelPath);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    public Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public float[][][][] preprocessBitmap(Bitmap bitmap, int inputSize) {
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, inputSize, inputSize, true);
        float[][][][] input = new float[1][inputSize][inputSize][3]; // 3 for RGB

        for (int y = 0; y < inputSize; y++) {
            for (int x = 0; x < inputSize; x++) {
                int pixel = resizedBitmap.getPixel(x, y);
                input[0][y][x][0] = ((pixel >> 16) & 0xFF) / 255.0f; // Red
                input[0][y][x][1] = ((pixel >> 8) & 0xFF) / 255.0f;  // Green
                input[0][y][x][2] = (pixel & 0xFF) / 255.0f;         // Blue
            }
        }
        return input;
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