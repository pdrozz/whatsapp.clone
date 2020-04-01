package com.pedrox.whatsclone.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.pedrox.whatsclone.R;
import com.pedrox.whatsclone.adapter.AdapterContatos;
import com.pedrox.whatsclone.adapter.AdapterViewPagerTab;
import com.pedrox.whatsclone.helper.MyPreferences;
import com.pedrox.whatsclone.model.ContatosModel;
import com.pedrox.whatsclone.model.MensagensModel;
import com.pedrox.whatsclone.sqlite.MensagemContatoDAO;
import com.pedrox.whatsclone.utils.Datetime;

import java.util.List;


public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TabLayout.Tab ItemTabCamera,ItemTabConversas,ItemTabStatus,ItemTabChamadas;
    private Toolbar toolBar;
    private DatabaseReference reference=FirebaseDatabase.getInstance().getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //config inicial
        tabLayout=findViewById(R.id.tabs);
        viewPager=findViewById(R.id.viewPager);
        toolBar=findViewById(R.id.toolbarMain);
        setSupportActionBar(toolBar);//config ToolBar
        getSupportActionBar().setElevation(0);

        //config abas
        ItemTabCamera=(tabLayout.newTab().setIcon(R.drawable.ic_camera));
        ItemTabConversas=(tabLayout.newTab().setText("Conversas"));
        ItemTabStatus=(tabLayout.newTab().setText("Status"));
        ItemTabChamadas=(tabLayout.newTab().setText("Chamadas"));

        //configurando viewPager
        viewPager.setAdapter(new AdapterViewPagerTab(getSupportFragmentManager()));
        //adicionando Abas
        tabLayout.addTab(ItemTabCamera);
        tabLayout.addTab(ItemTabConversas);
        tabLayout.addTab(ItemTabStatus);
        tabLayout.addTab(ItemTabChamadas);


        //online
        MyPreferences myPreferences=new MyPreferences(getSharedPreferences(MyPreferences.PREFERENCIASLOCAIS,0));
        String idUser=myPreferences.recuperarPreferencia("idUser");

        reference.child("contato").child(idUser).child("stats").setValue("online");

        //variaveis temporarias
        final TabLayout.Tab finalItem0 = ItemTabCamera;
        final TabLayout.Tab finalItem1 = ItemTabConversas;
        final TabLayout.Tab finalItem2 = ItemTabStatus;
        final TabLayout.Tab finalItem3 = ItemTabChamadas;

        //sincronizando ViewPager com TabLayout
        //Isso foi necessário pois o SetupWithViewPager não aceita abas com ícones
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                System.out.println("POSITION: "+position);
                switch (position){
                    case 0:
                        tabLayout.selectTab(finalItem0);
                        break;
                    case 1:
                        tabLayout.selectTab(finalItem1);
                        break;
                    case 2:
                        tabLayout.selectTab(finalItem2);
                        break;
                    case 3:
                        tabLayout.selectTab(finalItem3);
                        break;

                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab==finalItem0) {
                    viewPager.setCurrentItem(0);
                }else if(tab==finalItem1){
                    viewPager.setCurrentItem(1);
                }
                else if(tab==finalItem2){
                    viewPager.setCurrentItem(2);
                }
                else if(tab==finalItem3){
                    viewPager.setCurrentItem(3);

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });




        //set page inicial
        viewPager.setCurrentItem(1);




    }

    public void floatActionClick(View v){
        startActivity(new Intent(this, ContatosActivity.class));

    }

    //config menus
    //pesquisa e more
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal,menu);
        return super.onCreateOptionsMenu(menu);
    }

    //actions do menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.configuracoes:
                Intent i=new Intent(getApplicationContext(), ConfiguracoesActivity.class);
                startActivity(i);
                break;
            case R.id.pesquisa:

                break;

        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    protected void onStart() {
        super.onStart();
        MyPreferences myPreferences=new MyPreferences(getSharedPreferences(MyPreferences.PREFERENCIASLOCAIS,0));
        String idUser=myPreferences.recuperarPreferencia("idUser");
        //set user online
        DatabaseReference r=FirebaseDatabase.getInstance().getReference();
        r.child("contato").child(idUser).child("stats").setValue("online");
        r.child("contato").child(idUser).child("last_login").setValue(Datetime.getDateToday());




    }

    @Override
    protected void onStop() {
        super.onStop();

        //set user offline
        MyPreferences myPreferences=new MyPreferences(getSharedPreferences(MyPreferences.PREFERENCIASLOCAIS,0));
        String idUser=myPreferences.recuperarPreferencia("idUser");
        reference.child("contato").child(idUser).child("stats").setValue("offline");



    }


}
