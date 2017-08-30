package br.edu.infnet.minhasfotos;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by doug on 30/08/17.
 */

public class FotoAdapter extends BaseAdapter {

    private Context context;
    private List<Bitmap> listaFotos;

    public FotoAdapter(Context context, List<Bitmap> listaFotos) {

        super();
        this.context = context;
        this.listaFotos = listaFotos;

    }

    @Override
    public int getCount() {

        return listaFotos.size();
    }

    @Override
    public Object getItem(int i) {
        return listaFotos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Bitmap foto = listaFotos.get(i);
        ImageView imgFoto = new ImageView(context);

        int largImagem = (viewGroup.getWidth() / 3) - 10;
        int altImagem  = largImagem * foto.getHeight() / foto.getWidth();


        imgFoto.setImageBitmap(foto);
        imgFoto.setPadding(10,10,10,10);
        imgFoto.setLayoutParams(new ViewGroup.LayoutParams(largImagem, altImagem));
        return imgFoto;
    }
}
