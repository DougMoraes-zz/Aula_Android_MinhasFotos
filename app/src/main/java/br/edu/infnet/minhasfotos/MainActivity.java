package br.edu.infnet.minhasfotos;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private GridView gvGaleria;
    private StorageReference mStorageRef;
    private List<Bitmap> fotos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gvGaleria = (GridView) findViewById(R.id.gvGaleria);

        String[] nomesArray = {"index.jpg", "paisagem2.jpg", "paisagem3.jpg", "paisagem4.jpg"};

        mStorageRef = FirebaseStorage.getInstance().getReference();
        int memoria = 1024 * 1024;



        for (String foto : nomesArray){
            mStorageRef.child(foto).getBytes(memoria).addOnSuccessListener(
                    new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            Bitmap imagem = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            fotos.add(imagem);
                            gvGaleria.setAdapter(new FotoAdapter(getApplicationContext(), fotos));
                        }
                    }
            );
        }
    }
}
