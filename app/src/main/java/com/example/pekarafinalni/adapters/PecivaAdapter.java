package com.example.pekarafinalni.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.pekarafinalni.R;
import com.example.pekarafinalni.db.model.Peciva;

import java.util.List;

public class PecivaAdapter extends BaseAdapter {

    private Context context;
    private List<Peciva> pecivaList;

    public PecivaAdapter(Context context, List<Peciva> pecivaList) {
        this.context = context;
        this.pecivaList = pecivaList;
    }

    private Spannable message1 = null;
    private Spannable message2 = null;

    @Override
    public int getCount() {
        return pecivaList.size();
    }

    @Override
    public Object getItem(int position) {
        return pecivaList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint({"InflateParams", "ViewHolder", "ResourceAsColor"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater.inflate(R.layout.peciva_adapter, null);

        TextView naziv = convertView.findViewById(R.id.peciva_adapter_naziv);
        message1 = new SpannableString("Naziv Peciva: ");
        message2 = new SpannableString(pecivaList.get(position).getNaziv());
        spannableStyle();
        naziv.setText(message1);
        naziv.append(message2);

        TextView opis = convertView.findViewById(R.id.peciva_adapter_opis);
        message1 = new SpannableString("Opis Peciva: ");
        message2 = new SpannableString(pecivaList.get(position).getOpis());
        spannableStyle();
        opis.setText(message1);
        opis.append(message2);

        TextView cena = convertView.findViewById(R.id.peciva_adapter_cena);
        message1 = new SpannableString("Cena Peciva: ");
        message2 = new SpannableString(String.valueOf(pecivaList.get(position).getCena()));
        spannableStyle();
        cena.setText(message1);
        cena.append(message2);


        return convertView;
    }

    public void refreshList(List<Peciva> pecivas) {
        this.pecivaList.clear();
        this.pecivaList.addAll(pecivas);
        notifyDataSetChanged();
    }

    private void spannableStyle() {
        message1.setSpan(new StyleSpan(Typeface.BOLD), 0, message1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        message2.setSpan(new ForegroundColorSpan(Color.RED), 0, message2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

}