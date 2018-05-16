package br.com.senaijandira.room;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    GridView list_view;

    ProducaoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        list_view = (GridView) findViewById(R.id.list_view);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            startActivity(new Intent(view.getContext(), CadastroActivity.class));

            }
        });

        list_view = findViewById(R.id.list_view);

        adapter = new ProducaoAdapter(this);

        list_view.setAdapter(adapter);

        list_view.setOnItemClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        new AsyncTask<Void, Void, String>(){

            @Override
            protected String doInBackground(Void... voids) {

                String json = "";
                String url = "http://10.0.2.2/INF3T20181/TURMA%20A/Felipe%20Nascimento/AppFlix/selecionar.php";
                json = HttpConnection.get(url);

                return json;
            }

            @Override
            protected void onPostExecute(String json) {
                super.onPostExecute(json);

                ArrayList<Producao> lstProducao = new ArrayList<>();
                if(json != null){

                    try {
                        JSONArray arrayProducoes = new JSONArray(json);

                        for(int i=0; i < arrayProducoes.length();i++){

                            //pegando o objeto json dentro do array
                            JSONObject producaoJson =
                                    arrayProducoes.getJSONObject(i);

                            //criando o contato a partir do json
                            Producao producao = new Producao();
                            producao.setId(producaoJson.getInt("id"));
                            producao.setTitulo(producaoJson.getString("titulo"));
                            producao.setSinopse(producaoJson.getString("sinopse"));
                            producao.setLink(producaoJson.getString("link"));
                            producao.setAvaliacao(producaoJson.getDouble("avaliacao"));

                            //adicionando a lista
                            lstProducao.add(producao);
                        }


                    }catch (Exception ex){
                        ex.printStackTrace();
                    }

                    adapter.clear();

                    //Atualizando a UI
                    adapter.addAll(lstProducao);
                }
            }
        }.execute();

    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        Producao item = adapter.getItem(i);

        Intent intent = new Intent(getApplicationContext(), VisualizarActivity.class);

        intent.putExtra("id", "");

        startActivity(intent);
    }
}
