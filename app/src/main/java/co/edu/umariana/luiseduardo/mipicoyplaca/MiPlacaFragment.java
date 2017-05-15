package co.edu.umariana.luiseduardo.mipicoyplaca;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link MiPlacaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MiPlacaFragment extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Spinner spinner;
    private ImageView iv_icono;
    private TextView tv_placa, tv_hoy, tv_proximo_dia, tv_picoyplaca;
    String picoYplacaHoy,text;
    Date d = new Date();
    private int NOTIF_ALERTA_ID;
    Calendar localCalendar = Calendar.getInstance();
    int diaAnio = localCalendar.get(Calendar.DAY_OF_YEAR);
    int diaSemana = localCalendar.get(Calendar.DAY_OF_WEEK);
    private static final String[] PYP = {"4 - 5", "6 - 7", "8 - 9", "0 - 1", "2 - 3"};
    private static final Integer[] FESTIVOS = {1, 9, 79, 103, 104, 121, 149, 170, 177, 184, 201, 219, 233, 289, 310, 317, 342, 359};

    public MiPlacaFragment()
    {
        // Required empty public constructor

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MiPlacaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MiPlacaFragment newInstance(String param1, String param2) {
        MiPlacaFragment fragment = new MiPlacaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_mi_placa, container, false);
        tv_placa=(TextView) getActivity().findViewById(R.id.tv_placa);
        tv_hoy=(TextView)getActivity().findViewById(R.id.tv_hoy);
        tv_proximo_dia= (TextView)getActivity().findViewById(R.id.tv_proximo_dia);
        tv_picoyplaca=(TextView)getActivity().findViewById(R.id.tv_picoyplaca);
        iv_icono=(ImageView)getActivity().findViewById(R.id.iv_icono);

        // Inflate the layout for this fragment
        spinner = (Spinner) rootView.findViewById(R.id.sp_placas);
        String[] opciones = {"0 - 1", "2 - 3", "4 - 5", "6 - 7", "8 - 9"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity().getBaseContext(),R.layout.spinner_item, opciones);
        spinner.setAdapter(adapter);

        SharedPreferences prefe = getActivity().getSharedPreferences("datos", Context.MODE_PRIVATE);
        int i = prefe.getInt("placa_pos", 0);


        spinner.setSelection(i);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View selectedItemView, int pos, long id) {
                SharedPreferences preferencias = getActivity().getSharedPreferences("datos", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferencias.edit();
                editor.putString("placa", parent.getItemAtPosition(pos).toString());
                editor.putInt("placa_pos", pos);
                editor.commit();

                tv_placa.setText(parent.getItemAtPosition(pos).toString());
                festivosYDominicales();
                proximoDiaPicoyPlacaDiaActual();
                proximoDiaPicoYplaca();

            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        return rootView;

    }

    public void proximoDiaPicoYplaca()
    {
        boolean enc1 = false;
        int pos1 = 2;
        for (int i = 0; i < PYP.length && !enc1; i++) {
            if (PYP[i].equals(tv_placa.getText())) {
                enc1 = true;
                pos1 = pos1 + i;
            }
        }

        while (pos1 <= diaAnio) {
            localCalendar.set(Calendar.DAY_OF_YEAR, pos1);
            text = String.valueOf(localCalendar.get(Calendar.DAY_OF_WEEK));


            pos1 = (text.equals("2")?pos1+11:pos1+6);

            localCalendar.set(Calendar.DAY_OF_YEAR, pos1);
        }
        if (Arrays.asList(FESTIVOS).contains(pos1)) {
            localCalendar.set(Calendar.DAY_OF_YEAR, pos1);
            text = String.valueOf(localCalendar.get(Calendar.DAY_OF_WEEK));
            pos1=localCalendar.get(Calendar.DAY_OF_WEEK)==Calendar.MONDAY?pos1 + 11:pos1 + 6;
            localCalendar.set(Calendar.DAY_OF_YEAR, pos1);
        }

        d = localCalendar.getTime();
        SimpleDateFormat fech = new SimpleDateFormat("EEEE, d' de 'MMMM");
        String fech1 = fech.format(d);
        tv_proximo_dia.setText("En " + (pos1 - diaAnio) + " dias, " + fech1);
    }


    public  void festivosYDominicales()
    {
        if (Arrays.asList(FESTIVOS).contains(diaAnio) || diaSemana == 1 || diaSemana == 7) {// si es festivo o sabado o domingo
            picoYplacaHoy = "NO APLICA";
        } else {
            int j = diaAnio;
            while (j > 7) {
                j = localCalendar.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY ? j - 11 : j - 6;//si es viernes resta 11 y si es otro dia resta 6
                localCalendar.set(Calendar.DAY_OF_YEAR, j);// cambia la fecha a la fecha restada
            }
            picoYplacaHoy = PYP[localCalendar.get(Calendar.DAY_OF_YEAR) - 2];//se resta dos porque lunes comenzo el dia 2
        }
    }

    //próximo pico y placa ... solo cuando el mismo dia corresponde con el pico y placa
    public  void proximoDiaPicoyPlacaDiaActual()
    {
        localCalendar.set(Calendar.DAY_OF_YEAR, diaAnio);//vuelve al dia actual

        String pico = text;
        Date d = new Date();
        if (Arrays.asList(FESTIVOS).contains(diaAnio) || diaSemana == 1 || diaSemana == 7) {// si es festivo o sabado o domingo
            picoYplacaHoy = "NO";
        } else {
            int j = diaAnio;
            while (j > 7) {
                j = localCalendar.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY ? j - 11 : j - 6;//si es viernes resta 11 y si es otro dia resta 6
                localCalendar.set(Calendar.DAY_OF_YEAR, j);// cambia la fecha a la fecha restada
            }
            picoYplacaHoy = PYP[localCalendar.get(Calendar.DAY_OF_YEAR) - 2];//se resta dos porque lunes comenzo el dia 2
        }
        if (picoYplacaHoy.equals(tv_placa.getText().toString())) {
            iv_icono.setImageResource(R.drawable.prohibido);
            tv_picoyplaca.setTextColor(Color.RED);
            tv_picoyplaca.setText("Tiene Pico y Placa ");
            alerta();
        } else {
            iv_icono.setImageResource(R.drawable.visto);
            tv_picoyplaca.setTextColor(Color.GREEN);
            tv_picoyplaca.setText("No Tiene Pico y Placa ");
        }
        tv_hoy.setText(" " + picoYplacaHoy + " ");
    }

    public void alerta()
    {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getContext().getApplicationContext());
        mBuilder.setSmallIcon(android.R.drawable.stat_sys_warning);
        NotificationCompat.Builder builder = mBuilder.setLargeIcon(((BitmapDrawable) getResources()
                .getDrawable(R.drawable.car)).getBitmap());
        mBuilder.setContentTitle("Mensaje de mi pico y placa");
        mBuilder.setContentText("Hoy tiene pico y placa.");
        mBuilder.setContentInfo("4");
        mBuilder.setTicker("Alerta!");

        Intent notIntent = new Intent(getContext(), MainActivity.class);
        PendingIntent contIntent = PendingIntent.getActivity(getContext(), 0, notIntent, 0);
        mBuilder.setContentIntent(contIntent);

        NotificationManager mNotificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(NOTIF_ALERTA_ID, mBuilder.build());
    }

}
