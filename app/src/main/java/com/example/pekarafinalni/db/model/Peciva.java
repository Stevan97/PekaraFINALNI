package com.example.pekarafinalni.db.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = Peciva.PECIVA)
public class Peciva {

    public static final String PECIVA = "peciva";
    private static final String FIELD_ID = "id";
    private static final String FIELD_NAZIV = "naziv";
    private static final String FIELD_OPIS = "opis";
    private static final String FIELD_CENA = "cena";
    private static final String FIELD_PEKARA = "pekara";

    @DatabaseField(columnName = FIELD_ID, generatedId = true)
    private int id;

    @DatabaseField(columnName = FIELD_NAZIV)
    private String naziv;

    @DatabaseField(columnName = FIELD_OPIS)
    private String opis;

    @DatabaseField(columnName = FIELD_CENA)
    private double cena;

    @DatabaseField(columnName = FIELD_PEKARA, foreign = true, foreignAutoRefresh = true)
    private Pekara pekara;

    public Peciva() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public double getCena() {
        return cena;
    }

    public void setCena(double cena) {
        this.cena = cena;
    }

    public Pekara getPekara() {
        return pekara;
    }

    public void setPekara(Pekara pekara) {
        this.pekara = pekara;
    }

    public String toString() {
        return naziv;
    }

}
