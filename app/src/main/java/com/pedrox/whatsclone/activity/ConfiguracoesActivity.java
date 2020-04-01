package com.pedrox.whatsclone.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.pedrox.whatsclone.R;
import com.pedrox.whatsclone.helper.MyPreferences;
import com.pedrox.whatsclone.helper.Permission;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class ConfiguracoesActivity extends AppCompatActivity {


    private EditText editName;
    private DatabaseReference reference= FirebaseDatabase.getInstance().getReference();
    private String[] permissoes={Manifest.permission.READ_EXTERNAL_STORAGE};
    private ImageView imagemPerfil;
    private final int GALERY=500;
    private StorageReference storageReference= FirebaseStorage.getInstance().getReference();
    private String id;
    private ProgressBar progressBar;
    private MyPreferences myPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes);

        Permission.getPermission(permissoes,this,0);

        editName=findViewById(R.id.txtname);
        imagemPerfil=findViewById(R.id.imgPèrfil);
        progressBar=findViewById(R.id.progressBarConfiguracoes);
        progressBar.setVisibility(View.GONE);

        myPreferences=new MyPreferences(getSharedPreferences(MyPreferences.PREFERENCIASLOCAIS,0));
        String nameUser=myPreferences.recuperarPreferencia("nameUser");
        id=myPreferences.recuperarPreferencia("idUser");
        if(nameUser!=null){
            editName.setEnabled(false);
            editName.setText(nameUser);
        }else{}
        StorageReference imagemRef=storageReference
                .child("imagens")
                .child("perfil")
                .child(id+".jpeg");

        if(myPreferences.recuperarPreferencia("fotoOnline")!=null){
            progressBar.setVisibility(View.VISIBLE);
        imagemRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getApplicationContext()).load(uri).into(imagemPerfil);
                progressBar.setVisibility(View.GONE);
            }
        });
        }

        //config toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);




        //click FloatActionButton
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name=editName.getText().toString();
                if(!name.equals("")&&name!=null) {

                }else {
                Snackbar.make(view, "Erro ao salvar", Snackbar.LENGTH_LONG);}
            }
        });

    }

    public void TrocarImagem(View v){
        Intent i=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if(i.resolveActivity(getPackageManager())!=null){
            startActivityForResult(i,GALERY);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==RESULT_OK){
            Bitmap imagem=null;
            switch (requestCode){
                case GALERY:
                    Uri localImagem=data.getData();
                    try {
                    imagem=(Bitmap) MediaStore.Images.Media.getBitmap(getContentResolver(),localImagem);
                    }catch (Exception e){e.printStackTrace();}
                    break;
            }
            if (imagem!=null){
                progressBar.setVisibility(View.VISIBLE);

                imagemPerfil.setImageBitmap(imagem);

                ByteArrayOutputStream baos= new ByteArrayOutputStream();
                imagem.compress(Bitmap.CompressFormat.JPEG,75, baos);

                byte[] dadosImagem=baos.toByteArray();

                final StorageReference imagemRef=storageReference
                        .child("imagens")
                        .child("perfil")
                        .child(id+".jpeg");

                UploadTask uploadTask=imagemRef.putBytes(dadosImagem);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ConfiguracoesActivity.this, "Erro ao fazer upload da imagem"
                                , Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(ConfiguracoesActivity.this, "Sucesso ao fazer upload da imagem"
                                , Toast.LENGTH_SHORT).show();

                        String urlFoto=taskSnapshot.getStorage().toString();
                        StorageReference a=taskSnapshot.getStorage();
                        myPreferences.salvarPreferencia("fotoOnline","true");

                        setImage();

                    }
                }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Sucesso ao fazer upload da imagem"
                                , Toast.LENGTH_SHORT).show();

                    }
                });
            }



        }
    }

    private void setFotoUser(String url){
        reference.child("contato").child(id).child("foto").setValue(url);
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

    private void setImage(){
        final StorageReference imagemRef=storageReference
                .child("imagens")
                .child("perfil")
                .child(id+".jpeg");


        imagemRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                reference.child("contato").child(id).child("foto").setValue(uri.toString());
                Glide.with(getApplicationContext())
                        .load(uri)
                        .into(imagemPerfil);
                myPreferences.salvarPreferencia("urlFoto",uri.toString());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

}
