package com.sujanmaharjan008.taskmanagerapi;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.sujanmaharjan008.taskmanagerapi.api.UserAPI;
import com.sujanmaharjan008.taskmanagerapi.serverresponse.ImageResponse;
import com.sujanmaharjan008.taskmanagerapi.strictmode.StrictModeClass;
import com.sujanmaharjan008.taskmanagerapi.url.Url;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;

public class RegistrationActivity extends AppCompatActivity {

    private ImageView imageView;
    private EditText edtfirstName, edtlastName, edtPasswordR, edtCPasswordR, edtUsernameR;
    private Button btnRegister;
    String imagePath;
    private String imageName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        imageView = findViewById(R.id.imageView);
        edtfirstName = findViewById(R.id.edtfirstName);
        edtlastName = findViewById(R.id.edtlastName);
        edtUsernameR = findViewById(R.id.edtUsernameR);
        edtPasswordR = findViewById(R.id.edtPasswordR);
        edtCPasswordR = findViewById(R.id.edtCPasswordR);
        btnRegister = findViewById(R.id.btnRegister);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setImage();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (data == null) {
                Toast.makeText(this, "Please select an image.", Toast.LENGTH_SHORT).show();
            }
        }
        Uri uri = data.getData();
        imageView.setImageURI(uri);
        imagePath = getRealPathFromUri(uri);
        Toast.makeText(this, "Image Path is " + uri, Toast.LENGTH_SHORT).show();
    }

    public void setImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 0);

    }

    private String getRealPathFromUri(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(getApplicationContext(), uri, projection, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int colIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(colIndex);
        cursor.close();
        return result;
    }

    private void setImageOnly(){
        File file = new File(imagePath);
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"),file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("imageFile", file.getName(), requestBody);

        UserAPI userAPI = Url.getInstance().create(UserAPI.class);
        Call<ImageResponse> responseBodyCall = userAPI.uploadImage(body);

        //Synchronous method
        StrictModeClass.StrictMode();
        try{
            Response<ImageResponse> imageResponseResponse = responseBodyCall.execute();
            imageName = imageResponseResponse.body().getFilename();
            Toast.makeText(this, "Image name is " + imageName, Toast.LENGTH_SHORT).show();
        }
        catch (IOException e) {
            Toast.makeText(this, "Error" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
