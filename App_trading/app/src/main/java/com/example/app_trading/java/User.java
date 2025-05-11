package com.example.app_trading.java;

public class User {

    private Integer id = 0;
    private String nombre;
    private String apellido;
    private String apellido2;
    private String gmail;
    private String password;
    private String imageUrl;
    private String fechaNaz;
    private String fechaUpdate;
    private double dinero;
    private Integer idAccion;

    public User() {
    }

    public User(int id, String nombre, String apellido, String apellido2, String gmail, String password, String imageUrl, String fechaNaz, String fechaUpdate, double dinero, Integer idAccion) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.apellido2 = apellido2;
        this.gmail = gmail;
        this.password = password;
        this.imageUrl = imageUrl;
        this.fechaNaz = fechaNaz;
        this.fechaUpdate = fechaUpdate;
        this.dinero = dinero;
        this.idAccion = idAccion;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getApellido2() {
        return apellido2;
    }

    public void setApellido2(String apellido2) {
        this.apellido2 = apellido2;
    }

    public String getGmail() {
        return gmail;
    }

    public void setGmail(String gmail) {
        this.gmail = gmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getFechaNaz() {
        return fechaNaz;
    }

    public void setFechaNaz(String fechaNaz) {
        this.fechaNaz = fechaNaz;
    }

    public String getFechaUpdate() {
        return fechaUpdate;
    }

    public void setFechaUpdate(String fechaUpdate) {
        this.fechaUpdate = fechaUpdate;
    }

    public double getDinero() {
        return dinero;
    }

    public void setDinero(double dinero) {
        this.dinero = dinero;
    }

    public Integer getIdAccion() {
        return idAccion;
    }

    public void setIdAccion(Integer idAccion) {
        this.idAccion = idAccion;
    }
}
