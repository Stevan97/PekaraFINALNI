package com.example.pekarafinalni.activities;

import android.app.AlertDialog;
import android.app.Dialog;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pekarafinalni.R;
import com.example.pekarafinalni.adapters.DrawerAdapter;
import com.example.pekarafinalni.db.DatabaseHelper;
import com.example.pekarafinalni.db.model.Pekara;
import com.example.pekarafinalni.dialogs.AboutDialog;
import com.example.pekarafinalni.model.NavigationItems;
import com.example.pekarafinalni.tools.InputOcena;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String NOTIFY_MESSAGE = "notify";
    private static final String TOAST_MESSAGE = "toast";

    private int position = 1;
    private Pekara pekara = null;

    private DatabaseHelper databaseHelper = null;

    private ListView listViewMain = null;
    private List<Pekara> pekaraList = null;
    private ArrayAdapter<Pekara> pekaraArrayAdapter = null;

    private SharedPreferences sharedPreferences = null;
    boolean showMessage = false;
    boolean showNotify = false;

    private Spannable message1 = null;
    private Spannable message2 = null;

    private DrawerLayout drawerLayout;
    private ListView drawerListView;
    private ActionBarDrawerToggle drawerToggle;
    private CharSequence drawerTitle;
    private RelativeLayout drawerPane;
    private ArrayList<NavigationItems> drawerItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigationDrawer();

        prikaziListuPekara();

    }

    private void prikaziListuPekara() {
        listViewMain = findViewById(R.id.list_view_MAIN);
        try {
            pekaraList = getDatabaseHelper().getPekara().queryForAll();
            pekaraArrayAdapter = new ArrayAdapter<>(this, R.layout.list_array_adapter, R.id.list_array_text_view, pekaraList);
            listViewMain.setAdapter(pekaraArrayAdapter);
            listViewMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    pekara = (Pekara) listViewMain.getItemAtPosition(position);

                    Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                    intent.putExtra("id", pekara.getId());
                    startActivity(intent);
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void dodajPekaru() {
        final Dialog dialog = new Dialog(this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.dodaj_pekaru);
        dialog.show();

        final EditText editNaziv = dialog.findViewById(R.id.add_pekaru_naziv);
        final EditText editOpis = dialog.findViewById(R.id.add_pekaru_opis);
        final EditText editAdresa = dialog.findViewById(R.id.add_pekaru_adresa);
        final EditText editBrojTelefona = dialog.findViewById(R.id.add_pekaru_brojTelefona);
        final EditText editDatumOsnivanja = dialog.findViewById(R.id.add_pekaru_datum_osnivanja);
        final EditText editOcena = dialog.findViewById(R.id.add_pekaru_ocena);
        final EditText editRadnoVremeOD = dialog.findViewById(R.id.add_pekaru_radnoVremeOD);
        final EditText editRadnoVremeDO = dialog.findViewById(R.id.add_pekaru_radnoVremeDO);

        /** Provera da li je unos od 0 do 10 jer imamo 10 zvezdica za rating bar ! */
        InputFilter limitFilter = new InputOcena(0, 10);
        editOcena.setFilters(new InputFilter[]{limitFilter});

        Button confirm = dialog.findViewById(R.id.add_pekaru_button_confirm);
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

                pekara = new Pekara();
                pekara.setNaziv(naziv);
                pekara.setOpis(opis);
                pekara.setAdresa(adresa);
                pekara.setDatumOsnivanja(datumOsnivanja);
                pekara.setOcena(ocena);
                pekara.setBrojTelefona(brojTelefona);
                pekara.setRadnoVremeOD(radnoVremeOD);
                pekara.setRadnoVremeDO(radnoVremeDO);

                try {
                    getDatabaseHelper().getPekara().create(pekara);
                    dialog.dismiss();
                    refresh();

                    message1 = new SpannableString("Uspesno kreirana Pekara:  ");
                    message2 = new SpannableString(pekara.getNaziv());

                    message1.setSpan(new StyleSpan(Typeface.BOLD), 0, message1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    message2.setSpan(new ForegroundColorSpan(Color.RED), 0, message2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                    if (showMessage) {
                        Toast toast = Toast.makeText(MainActivity.this, "", Toast.LENGTH_LONG);
                        View toastView = toast.getView();

                        TextView toastText = toastView.findViewById(android.R.id.message);
                        toastText.setText(message1);
                        toastText.append(message2);
                        toast.show();
                    }
                    if (showNotify) {
                        showStatusBar2(message2);
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        });


        Button cancel = dialog.findViewById(R.id.add_pekaru_button_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void refresh() {
        listViewMain = findViewById(R.id.list_view_MAIN);
        if (listViewMain != null) {
            pekaraArrayAdapter = (ArrayAdapter<Pekara>) listViewMain.getAdapter();
            if (pekaraArrayAdapter != null) {
                try {
                    pekaraList = getDatabaseHelper().getPekara().queryForAll();
                    pekaraArrayAdapter.clear();
                    pekaraArrayAdapter.addAll(pekaraList);
                    pekaraArrayAdapter.notifyDataSetChanged();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void showStatusBarMessage(Spannable string) {

        message1 = new SpannableString("Uspesno kreirana Pekara: ");
        message1.setSpan(new StyleSpan(Typeface.BOLD), 0, message1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this);
        builder.setSmallIcon(R.drawable.ic_notify);
        builder.setContentTitle(message1);
        builder.setContentText(string);

        notificationManager.notify(1, builder.build());
    }

    private void consultPreferences() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        showMessage = sharedPreferences.getBoolean(TOAST_MESSAGE, true);
        showNotify = sharedPreferences.getBoolean(NOTIFY_MESSAGE, false);
    }

    /**
     * Navigaciona Fioka
     */
    private void navigationDrawer() {
        Toolbar toolbar = findViewById(R.id.toolbar_MAIN);
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
        drawerListView = findViewById(R.id.nav_list_MAIN);
        drawerListView.setAdapter(drawerAdapter);
        drawerListView.setOnItemClickListener(new DrawerItemClickListener());

        drawerTitle = getTitle();
        drawerLayout = findViewById(R.id.drawer_layout_MAIN);
        drawerPane = findViewById(R.id.drawer_pane_MAIN);

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
                refresh();
            } else if (position == 1) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            } else if (position == 2) {
                AlertDialog aboutDialog = new AboutDialog(MainActivity.this).prepareDialog();
                aboutDialog.show();
            }

            drawerLayout.closeDrawer(drawerPane);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_add_pekaru:
                dodajPekaru();
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

    @Override
    protected void onRestart() {
        refresh();
        super.onRestart();
    }

    private void firstTimeDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.first_time_dialog);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        Button ok = dialog.findViewById(R.id.first_time_button_OK);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    @Override
    protected void onResume() {
        refresh();
        consultPreferences();

        if (sharedPreferences.getBoolean("firstrun", true)) {
            firstTimeDialog();
            sharedPreferences.edit().putBoolean("firstrun", false).apply();
        }

        super.onResume();
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
    private void showStatusBar2(Spannable string) {


        NotificationChannel notificationChannel = new NotificationChannel("NOTIFY_ID", "ReserveNotify", NotificationManager.IMPORTANCE_HIGH);
        notificationChannel.setLightColor(Color.GREEN);

        message1 = new SpannableString("Uspesno kreirana Pekara: ");
        message1.setSpan(new StyleSpan(Typeface.BOLD), 0, message1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nm.createNotificationChannel(notificationChannel);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this);
        builder.setSmallIcon(R.drawable.ic_notify);
        builder.setContentTitle(message1);
        builder.setContentText(string);
        builder.setChannelId("NOTIFY_ID");

        nm.notify(1, builder.build());
    }


}
