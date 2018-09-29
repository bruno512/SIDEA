package com.soruco.bruno.sidea;

import android.graphics.drawable.Drawable;

// Tutorial para personlizar la lista de contactos utiles:
// https://androidstudiofaqs.com/tutoriales/adapter-personalizado-en-android

//Category representa cada uno de los elementos de nuestra lista
public class Category {

    private String title;
    private String categoryId;
    private String description;
    private Drawable imagen;
    private String nombre;
    private String apellido;

    public Category() {
        super();
    }

    public Category(String categoryId, String title, String description, Drawable imagen,String nombre, String apellido) {
        super();
        this.title = title;
        this.description = description;
        this.imagen = imagen;
        this.categoryId = categoryId;
        this.nombre = nombre;
        this.apellido = apellido;
    }

    public String getNombre() {
        return nombre;
    }
    public String getApellido() {
        return apellido;
    }


    public String getTitle() {
        return title;
    }

    public void setTittle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Drawable getImage() {
        return imagen;
    }

    public void setImagen(Drawable imagen) {
        this.imagen = imagen;
    }

    public String getCategoryId(){return categoryId;}

    public void setCategoryId(String categoryId){this.categoryId = categoryId;}

}

