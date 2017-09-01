package br.edu.infnet.minhasfotos;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private GridView            mGridFotos;
    private StorageReference    mStorageRef;
    private FirebaseDatabase    mDataBase;
    private DatabaseReference   mDataBaseRef;
    private Button              mBtnEscolher;
    private List<Bitmap>        mLista = new ArrayList<>();
    private List<Bitmap>        mListaInterna = new ArrayList<>();
    private  List<String>       mListaNomeFotos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mStorageRef = FirebaseStorage.getInstance().getReference();
        mDataBase = FirebaseDatabase.getInstance();
        mDataBaseRef = mDataBase.getReference("fotos");

        mGridFotos = (GridView) findViewById(R.id.gvGaleria);
        mBtnEscolher = (Button) findViewById(R.id.btnNovaFoto);

        String[] nomesArray = {"index.jpeg", "paisagem2.jpeg", "paisagem3.jpeg", "paisagem4.jpg"};
        int memoria = 1024 * 1024;

        for (String foto : nomesArray){

            mStorageRef.child(foto).getBytes(memoria).addOnSuccessListener(

                    new OnSuccessListener<byte[]>() {

                        @Override
                        public void onSuccess(byte[] bytes) {
                            Bitmap imagem = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            mLista.add(imagem);
                            mGridFotos.setAdapter(new FotoAdapter(getApplicationContext(), mLista));
                        }
                    }
            );
        }

        mBtnEscolher.setOnClickListener(adicionarFoto);
    }

    private View.OnClickListener adicionarFoto = new View.OnClickListener() {

        @Override
        public void onClick(View view) {

            File diretorio = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

            for(File imagem : diretorio.listFiles()) {
                Bitmap bitmap = BitmapFactory.decodeFile(imagem.getPath());
                mListaInterna.add(bitmap);
            }

            mGridFotos.setAdapter(new FotoAdapter(getApplicationContext(), mListaInterna));
            mBtnEscolher.setVisibility(View.INVISIBLE);
            mGridFotos.setOnItemClickListener(upload);
        }
    };

    private AdapterView.OnItemClickListener upload = new AdapterView.OnItemClickListener(){

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

             Bitmap bitmap = mListaInterna.get(i);
             String nome = String.valueOf(Calendar.getInstance().getTimeInMillis());

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            mStorageRef.child(nome).putBytes(byteArray);

            mListaNomeFotos.add(nome);
            mDataBaseRef.setValue(mListaNomeFotos);

            mGridFotos.setOnClickListener(null);
            mBtnEscolher.setVisibility(View.VISIBLE);

            mLista.add((bitmap));
            mGridFotos.setAdapter(new FotoAdapter(getApplicationContext(), mLista));
        }
    };
}
