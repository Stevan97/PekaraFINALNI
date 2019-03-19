package com.example.pekarafinalni.db.model;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = Pekara.TABL_PEKARA)
public class Pekara {

    public static final String TABL_PEKARA = "pekara";
    private static final String FIELD_ID = "id";
    private static final String FIELD_NAZIV = "naziv";
    private static final String FIELD_OPIS = "opis";
    private static final String FIELD_ADRESA = "adresa";
    private static final String FIELD_DATUM_OSNIVANJA = "datumOsnivanja";
    private static final String FIELD_BROJ_TELEFONA = "brojTelefona";
    private static final String FIELD_OCENA = "ocenaPekare";
    private static final String FIELD_RADNO_VREME_OD = "radnoVremeOD";
    private static final String FIELD_RADNO_VREME_DO = "radnoVremeDO";
    private static final String FIELD_PECIVA = "peciva";

    @DatabaseField(columnName = FIELD_ID,generatedId = true)
    private int id;

    @DatabaseField(columnName = FIELD_NAZIV)
    private String naziv;

    @DatabaseField(columnName = FIELD_OPIS)
    private String opis;

    @DatabaseField(columnName = FIELD_ADRESA)
    private String adresa;

    @DatabaseField(columnName = FIELD_DATUM_OSNIVANJA)
    private String datumOsnivanja;

    @DatabaseField(columnName = FIELD_BROJ_TELEFONA)
    private int brojTelefona;

    @DatabaseField(columnName = FIELD_OCENA)
    private float ocena;

    @DatabaseField(columnName = FIELD_RADNO_VREME_OD)
    private int radnoVremeOD;

    @DatabaseField(columnName = FIELD_RADNO_VREME_DO)
    private int radnoVremeDO;

    @ForeignCollectionField(columnName = FIELD_PECIVA, eager = true)
    private ForeignCollection<Peciva> peciva;

    public Pekara() {

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

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public String getDatumOsnivanja() {
        return datumOsnivanja;
    }

    public void setDatumOsnivanja(String datumOsnivanja) {
        this.datumOsnivanja = datumOsnivanja;
    }

    public int getBrojTelefona() {
        return brojTelefona;
    }

    public void setBrojTelefona(int brojTelefona) {
        this.brojTelefona = brojTelefona;
    }

    public float getOcena() {
        return ocena;
    }

    public void setOcena(float ocena) {
        this.ocena = ocena;
    }

    public int getRadnoVremeOD() {
        return radnoVremeOD;
    }

    public void setRadnoVremeOD(int radnoVremeOD) {
        this.radnoVremeOD = radnoVremeOD;
    }

    public int getRadnoVremeDO() {
        return radnoVremeDO;
    }

    public void setRadnoVremeDO(int radnoVremeDO) {
        this.radnoVremeDO = radnoVremeDO;
    }

    public ForeignCollection<Peciva> getPeciva() {
        return peciva;
    }

    public void setPeciva(ForeignCollection<Peciva> peciva) {
        this.peciva = peciva;
    }

    public String toString() {
        return naziv;
    }

}

