package com.example.pekarafinalni.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pekarafinalni.R;
import com.example.pekarafinalni.adapters.DrawerAdapter;
import com.example.pekarafinalni.adapters.PecivaAdapter;
import com.example.pekarafinalni.db.DatabaseHelper;
import com.example.pekarafinalni.db.model.Peciva;
import com.example.pekarafinalni.db.model.Pekara;
import com.example.pekarafinalni.dialogs.AboutDialog;
import com.example.pekarafinalni.model.NavigationItems;
import com.example.pekarafinalni.tools.InputOcena;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.ForeignCollection;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    private static final String NOTIFY_MESSAGE = "notify";
    private static final String TOAST_MESSAGE = "toast";

    private int position = 1;
    private DatabaseHelper databaseHelper = null;

    private Pekara pekara = null;
    private Peciva peciva = null;

    private ForeignCollection<Peciva> pecivaForeignCollection = null;
    private List<Peciva> pecivaList = null;
    private ListView listViewDetail = null;
    private PecivaAdapter pecivaAdapter = null;
    private Intent intentPosition = null;
    private int idPosition = 0;

    private Spannable message1 = null;
    private Spannable message2 = null;
    private Spannable message3 = null;
    private Toast toast = null;
    private View toastView = null;
    private TextView textToast = null;

    private Button cancel = null;
    private Button confirm = null;

    private boolean showMessage = false;
    private boolean showNotify = false;

    private DrawerLayout drawerLayout;
    private ListView drawerListView;
    private ActionBarDrawerToggle drawerToggle;
    private CharSequence drawerTitle;
    private RelativeLayout drawerPane;
    private ArrayList<NavigationItems> drawerItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        navigationDrawer();

        prikaziDetaljePekare();

    }

    private void prikaziDetaljePekare() {
        intentPosition = getIntent();
        idPosition = intentPosition.getExtras().getInt("id");

        try {
            pekara = getDatabaseHelper().getPekara().queryForId(idPosition);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        TextView naziv = findViewById(R.id.detail_naziv_pekare);
        message1 = new SpannableString("Naziv Pekare: ");
        message2 = new SpannableString(pekara.getNaziv());
        spannableStyle();
        naziv.setText(message1);
        naziv.append(message2);

        TextView opis = findViewById(R.id.detail_opis_pekare);
        message1 = new SpannableString("Opis Pekare: ");
        message2 = new SpannableString(pekara.getOpis());
        spannableStyle();
        opis.setText(message1);
        opis.append(message2);

        TextView adresa = findViewById(R.id.detail_adresa_pekare);
        message1 = new SpannableString("Adresa Pekare: ");
        message2 = new SpannableString(pekara.getAdresa());
        spannableStyle();
        adresa.setText(message1);
        adresa.append(message2);

        TextView datum = findViewById(R.id.detail_datum_osnivanja);
        message1 = new SpannableString("Datum Osnivanja: ");
        message2 = new SpannableString(pekara.getDatumOsnivanja());
        spannableStyle();
        datum.setText(message1);
        datum.append(message2);

        TextView broj = findViewById(R.id.detail_broj_telefona);
        message1 = new SpannableString("Broj Telefona Pekare: ");
        spannableStyle();
        broj.setText(message1);
        broj.append(String.valueOf(pekara.getBrojTelefona()));

        TextView radnoVremeOD = findViewById(R.id.detail_radno_vreme_OD);
        message1 = new SpannableString("Radno vreme Pekare OD: ");
        message2 = new SpannableString(String.valueOf(pekara.getRadnoVremeOD()));
        spannableStyle();
        radnoVremeOD.setText(message1);
        radnoVremeOD.append(message2);

        TextView radnoVremeDO = findViewById(R.id.detail_radno_vreme_DO);
        message1 = new SpannableString(" DO: ");
        message2 = new SpannableString(String.valueOf(pekara.getRadnoVremeDO()));
        spannableStyle();
        radnoVremeDO.setText(message1);
        radnoVremeDO.append(message2);

        RatingBar ratingBar = findViewById(R.id.detail_rating_bar);
        ratingBar.setRating(pekara.getOcena());

        listViewDetail = findViewById(R.id.list_view_DETAIL);
        try {
            pecivaForeignCollection = getDatabaseHelper().getPekara().queryForId(idPosition).getPeciva();
            pecivaList = new ArrayList<>(pecivaForeignCollection);
            pecivaAdapter = new PecivaAdapter(this, pecivaList);
            listViewDetail.setAdapter(pecivaAdapter);
            listViewDetail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    peciva = (Peciva) listViewDetail.getItemAtPosition(position);
                    message3 = new SpannableString("Uspesno ste porucili Pecivo sa Nazivom: " + peciva.getNaziv());
                    message3.setSpan(new ForegroundColorSpan(getColor(R.color.colorRED)), 0, message3.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                    showstatusBar2(message3);

                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void dodajPecivo() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dodaj_pecivo);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        final EditText editNaziv = dialog.findViewById(R.id.dodaj_pecivo_naziv);
        final EditText editOpis = dialog.findViewById(R.id.dodaj_pecivo_opis);
        final EditText editCena = dialog.findViewById(R.id.dodaj_pecivo_cena);

        Button confirm = dialog.findViewById(R.id.dodaj_pecivo_button_confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editNaziv.getText().toString().isEmpty()) {
                    editNaziv.setError("Polje naziv ne sme biti prazno!");
                    return;
                }
                if (editOpis.getText().toString().isEmpty()) {
                    editOpis.setError("Polje opis ne sme biti prazno!");
                    return;
                }
                if (editCena.getText().toString().isEmpty()) {
                    editCena.setError("Polje Cena ne sme biti prazno!");
                    return;
                }

                String naziv = editNaziv.getText().toString();
                String opis = editOpis.getText().toString();
                double cena = Double.parseDouble(editCena.getText().toString());

                intentPosition = getIntent();
                idPosition = intentPosition.getExtras().getInt("id");

                try {
                    pekara = getDatabaseHelper().getPekara().queryForId(idPosition);
                    peciva = new Peciva();
                    peciva.setNaziv(naziv);
                    peciva.setOpis(opis);
                    peciva.setCena(cena);
                    peciva.setPekara(pekara);

                    getDatabaseHelper().getPeciva().create(peciva);

                    dialog.dismiss();
                    osveziPeciva();

                    message1 = new SpannableString("Uspesno kreirano Pecivo sa nazivom: ");
                    message2 = new SpannableString(peciva.getNaziv());
                    spannableStyle();

                    if (showMessage) {
                        toast = Toast.makeText(DetailActivity.this, "", Toast.LENGTH_LONG);
                        toastView = toast.getView();

                        textToast = toastView.findViewById(android.R.id.message);
                        textToast.setText(message1);
                        textToast.append(message2);
                        toast.show();
                    }
                    if (showNotify) {
                        message3 = new SpannableString("Uspesno kreirano Pecivo sa nazivom: " + peciva.getNaziv());
                        message3.setSpan(new ForegroundColorSpan(getColor(R.color.colorRED)), 0, message3.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        showstatusBar2(message3);
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }


            }
        });

        Button cancel = dialog.findViewById(R.id.dodaj_pecivo_button_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    private void izbrisiPekaru() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.izbrisi_pekaru);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        intentPosition = getIntent();
        idPosition = intentPosition.getExtras().getInt("id");

        try {
            pekara = getDatabaseHelper().getPekara().queryForId(idPosition);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        TextView text = dialog.findViewById(R.id.izbrisi_pekaru_text);
        message1 = new SpannableString("Da li ste sigurni da zelite da izbrisete Pekaru pod nazivom: ");
        message2 = new SpannableString(pekara.getNaziv());
        spannableStyle();
        text.setText(message1);
        text.append(message2);

        Button confirm = dialog.findViewById(R.id.izbrisi_pekaru_button_confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    pecivaForeignCollection = getDatabaseHelper().getPekara().queryForId(idPosition).getPeciva();
                    pecivaList = new ArrayList<>(pecivaForeignCollection);

                    getDatabaseHelper().getPeciva().delete(pecivaList);
                    getDatabaseHelper().getPekara().delete(pekara);
                    onBackPressed();

                    message1 = new SpannableString("Uspesno izbrisana Pekara sa nazivom:  ");
                    message2 = new SpannableString(pekara.getNaziv());
                    spannableStyle();

                    if (showMessage) {
                        toast = Toast.makeText(DetailActivity.this, "", Toast.LENGTH_LONG);
                        toastView = toast.getView();

                        textToast = toastView.findViewById(android.R.id.message);
                        textToast.setText(message1);
                        textToast.append(message2);
                        toast.show();
                    }
                    if (showNotify) {
                        message3 = new SpannableString("Uspesno obrisana Pekara: " + pekara.getNaziv());
                        message3.setSpan(new ForegroundColorSpan(getColor(R.color.colorRED)), 0, message3.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        showstatusBar2(message3);
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        });

        Button cancel = dialog.findViewById(R.id.izbrisi_pekaru_button_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void izmenaPekaru() {
        final Dialog dialog = new Dialog(this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.izmena_pekare);
        dialog.show();

        final EditText editNaziv = dialog.findViewById(R.id.izmeni_pekaru_naziv);
        final EditText editOpis = dialog.findViewById(R.id.izmeni_pekaru_opis);
        final EditText editAdresa = dialog.findViewById(R.id.izmeni_pekaru_adresa);
        final EditText editBrojTelefona = dialog.findViewById(R.id.izmeni_pekaru_brojTelefona);
        final EditText editDatumOsnivanja = dialog.findViewById(R.id.izmeni_pekaru_datum_osnivanja);
        final EditText editOcena = dialog.findViewById(R.id.izmeni_pekaru_ocena);
        final EditText editRadnoVremeOD = dialog.findViewById(R.id.izmeni_pekaru_radnoVremeOD);
        final EditText editRadnoVremeDO = dialog.findViewById(R.id.izmeni_pekaru_radnoVremeDO);

        /** Provera da li je unos od 0 do 10 jer imamo 10 zvezdica za rating bar ! */
        InputFilter limitFilter = new InputOcena(0, 10);
        editOcena.setFilters(new InputFilter[]{limitFilter});

        Button confirm = dialog.findViewById(R.id.izmeni_pekaru_button_confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editNaziv.getText().toString().isEmpty()) {
                    editNaziv.setError("Polje naziv ne sme biti prazno!");
                    return;
                }
                if (editOpis.getText().toString().isEmpty()) {
                    editOpis.setError("Polje opis ne sme biti prazno!");
                    return;
                }
                if (editAdresa.getText().toString().isEmpty()) {
                    editAdresa.setError("Unesite pravu adresu da bi mogli da otvorite u MAPS!");
                    return;
                }
                if (editDatumOsnivanja.getText().toString().isEmpty() || editDatumOsnivanja.getText().toString().length() > 10
                        || editDatumOsnivanja.getText().toString().length() < 10
                        || !isValidDate(editDatumOsnivanja.getText().toString())) {
                    editDatumOsnivanja.setError("Date Format: dd-MM-yyyy with - !");
                    return;
                }
                if (editBrojTelefona.getText().toString().isEmpty() || editBrojTelefona.getText().toString().length() < 5
                        || editBrojTelefona.getText().toString().length() > 10) {
                    editBrojTelefona.setError("Broj telefona mora biti duzi od 5 i manji od 10 !");
                    return;
                }
                if (editOcena.getText().toString().isEmpty()) {
                    editOcena.setError("Ocena od 0 do 10 !");
                    return;
                }
                if (editRadnoVremeOD.getText().toString().isEmpty()) {
                    editRadnoVremeOD.setError("Polje radno vreme OD ne sme biti prazno!");
                    return;
                }
                if (editRadnoVremeDO.getText().toString().isEmpty()) {
                    editRadnoVremeDO.setError("Polje radno vreme DO ne sme biti prazno!");
                    return;
                }

                String naziv = editNaziv.getText().toString();
                String opis = editOpis.getText().toString();
                String adresa = editAdresa.getText().toString();
                String datumOsnivanja = editDatumOsnivanja.getText().toString();
                float ocena = Float.parseFloat(editOcena.getText().toString());
                int brojTelefona = Integer.parseInt(editBrojTelefona.getText().toString());
                int radnoVremeOD = Integer.parseInt(editRadnoVremeOD.getText().toString());
                int radnoVremeDO = Integer.parseInt(editRadnoVremeDO.getText().toString());

                intentPosition = getIntent();
                idPosition = intentPosition.getExtras().getInt("id");


                try {
                    pekara = getDatabaseHelper().getPekara().queryForId(idPosition);

                    pekara.setNaziv(naziv);
                    pekara.setOpis(opis);
                    pekara.setAdresa(adresa);
                    pekara.setDatumOsnivanja(datumOsnivanja);
                    pekara.setOcena(ocena);
                    pekara.setBrojTelefona(brojTelefona);
                    pekara.setRadnoVremeOD(radnoVremeOD);
                    pekara.setRadnoVremeDO(radnoVremeDO);


                    getDatabaseHelper().getPekara().update(pekara);
                    dialog.dismiss();
                    startActivity(getIntent());
                    finish();
                    overridePendingTransition(0,0);

                    message1 = new SpannableString("Uspesna Izmena | Novo ime Pekare: ");
                    message2 = new SpannableString(pekara.getNaziv());

                    message1.setSpan(new StyleSpan(Typeface.BOLD), 0, message1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    message2.setSpan(new ForegroundColorSpan(Color.RED), 0, message2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                    if (showMessage) {
                        Toast toast = Toast.makeText(DetailActivity.this, "", Toast.LENGTH_LONG);
                        View toastView = toast.getView();

                        TextView toastText = toastView.findViewById(android.R.id.message);
                        toastText.setText(message1);
                        toastText.append(message2);
                        toast.show();
                    }
                    if (showNotify) {
                        showstatusBar2(message2);
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        Button cancel = dialog.findViewById(R.id.izmeni_pekaru_button_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    private void osveziPeciva() {
        listViewDetail = findViewById(R.id.list_view_DETAIL);
        if (listViewDetail != null) {
            pecivaAdapter = (PecivaAdapter) listViewDetail.getAdapter();
            if (pecivaAdapter != null) {
                intentPosition = getIntent();
                idPosition = intentPosition.getExtras().getInt("id");
                try {
                    pecivaForeignCollection = getDatabaseHelper().getPekara().queryForId(idPosition).getPeciva();
                    pecivaList = new ArrayList<>(pecivaForeignCollection);
                    pecivaAdapter.refreshList(pecivaList);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private void spannableStyle() {
        message1.setSpan(new StyleSpan(Typeface.BOLD), 0, message1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        message2.setSpan(new ForegroundColorSpan(getColor(R.color.colorRED)), 0, message2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    private void consultPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(DetailActivity.this);
        showMessage = sharedPreferences.getBoolean(TOAST_MESSAGE, true);
        showNotify = sharedPreferences.getBoolean(NOTIFY_MESSAGE, false);
    }

    /**
     * KORISTI OVU METODU NA TESTU !
     */
    private void showStatusBarMessage(Spannable string) {

        message1 = new SpannableString("Uspesno!");
        message1.setSpan(new StyleSpan(Typeface.BOLD), 0, message1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(DetailActivity.this);
        builder.setSmallIcon(R.drawable.ic_notify);
        builder.setContentTitle(message1);
        builder.setContentText(string);

        notificationManager.notify(1, builder.build());
    }

    @Override
    protected void onResume() {
        osveziPeciva();
        consultPreferences();
        super.onResume();
    }

    /**
     * Navigaciona Fioka
     */
    private void navigationDrawer() {
        Toolbar toolbar = findViewById(R.id.toolbar_DETAIL);
        setSupportActionBar(toolbar);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_nav_list);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.show();
        }

        drawerItems.add(new NavigationItems("Pekare", "Prikazuje listu Pekara", R.drawable.ic_show_all));
        drawerItems.add(new NavigationItems("Podesavanja", "Otvara Podesavanja Aplikacije", R.drawable.ic_settings));
        drawerItems.add(new NavigationItems("Informacije", "Informacije o Aplikaciji", R.drawable.info_about_app));

        DrawerAdapter drawerAdapter = new DrawerAdapter(this, drawerItems);
        drawerListView = findViewById(R.id.nav_list_DETAIL);
        drawerListView.setAdapter(drawerAdapter);
        drawerListView.setOnItemClickListener(new DetailActivity.DrawerItemClickListener());

        drawerTitle = getTitle();
        drawerLayout = findViewById(R.id.drawer_layout_DETAIL);
        drawerPane = findViewById(R.id.drawer_pane_DETAIL);

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer) {

            @Override
            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(drawerTitle);
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                getSupportActionBar().setTitle(drawerTitle);
                super.onDrawerClosed(drawerView);
            }
        };

    }

    /**
     * OnItemClick iz NavigacioneFioke.
     */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (position == 0) {
                onBackPressed();
                overridePendingTransition(0, 0);
            } else if (position == 1) {
                Intent intent = new Intent(DetailActivity.this, SettingsActivity.class);
                startActivity(intent);
            } else if (position == 2) {
                AlertDialog aboutDialog = new AboutDialog(DetailActivity.this).prepareDialog();
                aboutDialog.show();
            }

            drawerLayout.closeDrawer(drawerPane);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_detail_delete:
                izbrisiPekaru();
                break;
            case R.id.menu_detail_update:
                izmenaPekaru();
                break;
            case R.id.menu_detail_dodaj_pecivo:
                dodajPecivo();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Proveravamo datum
     */
    public static boolean isValidDate(String inDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(inDate.trim());
        } catch (ParseException pe) {
            return false;
        }
        return true;
    }


    public DatabaseHelper getDatabaseHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
        }
        return databaseHelper;
    }

    @Override
    protected void onDestroy() {

        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }

        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("position", position);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void showstatusBar2(Spannable string) {


        NotificationChannel notificationChannel = new NotificationChannel("NOTIFY_ID", "ReserveNotify", NotificationManager.IMPORTANCE_HIGH);
        notificationChannel.setLightColor(Color.GREEN);

        message1 = new SpannableString("Uspesno! ");
        message1.setSpan(new StyleSpan(Typeface.BOLD), 0, message1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nm.createNotificationChannel(notificationChannel);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(DetailActivity.this);
        builder.setSmallIcon(R.drawable.ic_notify);
        builder.setContentTitle(message1);
        builder.setContentText(string);
        builder.setChannelId("NOTIFY_ID");

        nm.notify(1, builder.build());
    }

}
