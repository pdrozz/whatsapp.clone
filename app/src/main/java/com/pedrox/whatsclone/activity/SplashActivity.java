package com.pedrox.whatsclone.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pedrox.whatsclone.R;
import com.pedrox.whatsclone.helper.MyPreferences;
import com.pedrox.whatsclone.model.ContatosModel;
import com.pedrox.whatsclone.utils.Datetime;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class SplashActivity extends AppCompatActivity {


    private Button btnEntrar;
    private EditText txtname;
    private DatabaseReference reference= FirebaseDatabase.getInstance().getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //configurações inicias
        btnEntrar=findViewById(R.id.btnEntrar);
        txtname=findViewById(R.id.nameUser);

        //recuperando nome de usuário
        //para verificar se ele já escolheu um nome
        final MyPreferences p=new MyPreferences(getSharedPreferences(MyPreferences.PREFERENCIASLOCAIS,0));
        final String nameUser=p.recuperarPreferencia("nameUser");
        if (nameUser!=null){
            startMain();
            }//se ele já escolheu inicia a mainActivity


        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    String name=txtname.getText().toString();
                    //verificando se o nome é valido
                    if(name!=null && !name.equals("")){
                        btnEntrar.setEnabled(false);
                    p.salvarPreferencia("nameUser",name);
                    //configurando Contato no banco do Firebase
                        String id=UUID.randomUUID().toString();
                        id=id.replace("-","");
                        ContatosModel contato=new ContatosModel();
                        contato.setId(id);
                    contato.setNome(name);
                    contato.setFoto("zero");//foto padrao
                    //tentando salvar os dados virtualmente
                    try{
                    reference.child("contato").child(id).setValue(contato);
                    reference.child("user").child(id).child("nome").setValue(name);
                    reference.child("contato").child(id).child("sign_up").setValue(Datetime.getDateToday());
                    reference.child("contato").child(id).child("stats").setValue("online");
                    p.salvarPreferencia("idUser",id);
                    startMain();
                    }catch (Exception e){
                        Toast.makeText(getApplicationContext(),"Erro ao cadastrar online:"
                                +e.toString(),Toast.LENGTH_SHORT).show();
                        btnEntrar.setEnabled(true);
                    }



                    }else {
                        Snackbar.make(v,"Você deve escolher um nome",Snackbar.LENGTH_LONG).show();
                    }
                }
            });



    }

    private void startMain(){
        startActivity(new Intent(this,MainActivity.class));
        finish();

    }
}
