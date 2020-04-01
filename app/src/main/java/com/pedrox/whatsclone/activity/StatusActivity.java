package com.pedrox.whatsclone.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.pedrox.whatsclone.R;
import com.pedrox.whatsclone.helper.MyPreferences;
import com.pedrox.whatsclone.helper.Permission;
import com.pedrox.whatsclone.model.StatusModel;
import com.pedrox.whatsclone.utils.Datetime;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

public class StatusActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ProgressBar progressBar;
    private ProgressBar progressBarHorizontal;
    private String[] permissoes={Manifest.permission.READ_EXTERNAL_STORAGE};
    private ImageView imagemStatus;
    private MyPreferences preferences;
    private StorageReference storageReference= FirebaseStorage.getInstance().getReference();
    private DatabaseReference reference= FirebaseDatabase.getInstance().getReference();
    private String ID,NAMEUSER,FOTOUSER;
    private int value=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        toolbar=findViewById(R.id.toolbarStatus);
        progressBar=findViewById(R.id.progressBarStatus);
        progressBarHorizontal=findViewById(R.id.progressBarTempo);
        imagemStatus=findViewById(R.id.imageStatus);

        preferences=new MyPreferences(this);
        ID=preferences.recuperarPreferencia("idUser");
        NAMEUSER=preferences.recuperarPreferencia("nameUser");
        FOTOUSER=preferences.recuperarPreferencia("urlFoto");
        Permission.getPermission(permissoes,this,2);

        if(getIntent().getExtras().getBoolean("myStatus")){
        NewStatus();}
        else {
            StatusModel status=(StatusModel)getIntent().getExtras().getSerializable("obj");
            Picasso.get().load(status.getUrlStatus()).into(imagemStatus);
            Handler handler=new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);
                }
            },2000);
            toolbar.setTitle(status.getNome());
//            getActionBar().setTitle(status.getNome());
//            getSupportActionBar().setTitle(status.getNome());


        }



        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void TimeStatus(){
        Handler h=new Handler();
                h.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        value=value+1;
                        progressBarHorizontal.setProgress(value);
                        TimeStatus();
                        if (value==7){
                            finish();
                        }
                    }
                },500);

    }

    private void NewStatus(){
        Intent i=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if(i.resolveActivity(getPackageManager())!=null){
            startActivityForResult(i,2);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            Bitmap imagem=null;
            Uri localImagem=data.getData();
            try {
                imagem=(Bitmap) MediaStore.Images.Media.getBitmap(getContentResolver(),localImagem);
            }catch (Exception e){e.printStackTrace();}
            if (imagem!=null){
                progressBar.setVisibility(View.VISIBLE);

                imagemStatus.setImageBitmap(imagem);

                ByteArrayOutputStream baos= new ByteArrayOutputStream();
                imagem.compress(Bitmap.CompressFormat.JPEG,60, baos);

                byte[] dadosImagem=baos.toByteArray();

                final StorageReference imagemRef=storageReference
                        .child("imagens")
                        .child("status")
                        .child(ID+".jpeg");

                UploadTask uploadTask=imagemRef.putBytes(dadosImagem);
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        setStatus();
                        Toast.makeText(getApplicationContext(), "Sucesso ao postar Status"
                                , Toast.LENGTH_SHORT).show();
                    }
                });



        }
    }
        finish();
    }

    private void setStatus(){
        final StorageReference imagemRef=storageReference
                .child("imagens")
                .child("status")
                .child(ID+".jpeg");
        imagemRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                StatusModel status=new StatusModel(NAMEUSER, Datetime.getDateToday(),FOTOUSER,uri.toString(),"teste");
                reference.child("status").child(ID).setValue(status);
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for(int resultado:grantResults){
            if (resultado== PackageManager.PERMISSION_DENIED) DeniedPermission();
        }
    }

    private void DeniedPermission(){
        Toast.makeText(this, "Para usar o app é necessário aceitar as permissões", Toast.LENGTH_SHORT).show();
        finish();
    }
}
