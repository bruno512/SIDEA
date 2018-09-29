package com.soruco.bruno.sidea;

import android.widget.BaseAdapter;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

// Tutorial para personlizar la lista de contactos utiles:
// https://androidstudiofaqs.com/tutoriales/adapter-personalizado-en-android

//AdapterCategory será la encargada de visualizar los elementos a nuestro gusto en la lista
public class AdapterItem extends BaseAdapter {

    protected Activity activity;
    protected ArrayList<Category> items;

    public AdapterItem (Activity activity, ArrayList<Category> items) {
        this.activity = activity;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    public void clear() {
        items.clear();
    }

    public void addAll(ArrayList<Category> category) {
        for (int i = 0; i < category.size(); i++) {
            items.add(category.get(i));
        }
    }

    @Override
    public Object getItem(int arg0) {
        return items.get(arg0);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (convertView == null) {
            LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(R.layout.item_category, null);
        }

        Category dir = items.get(position);

        if (dir.getTitle() == " "){
            TextView title = (TextView) v.findViewById(R.id.category);
            title.setText(dir.getNombre() +" "+dir.getApellido());
        }else {
            TextView title = (TextView) v.findViewById(R.id.category);
            title.setText(dir.getTitle());
        }

        TextView description = (TextView) v.findViewById(R.id.texto);
        description.setText(dir.getDescription());

        ImageView imagen = (ImageView) v.findViewById(R.id.imageView4);
        imagen.setImageDrawable(dir.getImage());

        return v;
    }
}

