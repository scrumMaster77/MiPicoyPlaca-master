package co.edu.umariana.luiseduardo.mipicoyplaca;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.DrawableRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link HoyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HoyFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private static final String[] PYP = {"4 - 5", "6 - 7", "8 - 9", "0 - 1", "2 - 3"};
    private static final Integer[] FESTIVOS = {1, 9, 79, 103, 104, 121, 149, 170, 177, 184, 201, 219, 233, 289, 310, 317, 342, 359};
    Calendar localCalendar = Calendar.getInstance();
    Date d = new Date();
    int diaAnio = localCalendar.get(Calendar.DAY_OF_YEAR);
    int diaSemana = localCalendar.get(Calendar.DAY_OF_WEEK);
    ImageView iv_icono;
    TextView tv_hoy, tv_placa, tv_proximo_dia, tv_picoyplaca;
    String text;
    String picoYplacaHoy;



    public HoyFragment()
    {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HoyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HoyFragment newInstance(String param1, String param2) {
        HoyFragment fragment = new HoyFragment();
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
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_hoy, container, false);
        tv_placa=(TextView) rootView.findViewById(R.id.tv_placa);
        tv_hoy=(TextView)rootView.findViewById(R.id.tv_hoy);
        tv_proximo_dia= (TextView)rootView.findViewById(R.id.tv_proximo_dia);
        tv_picoyplaca=(TextView)rootView.findViewById(R.id.tv_picoyplaca);
        iv_icono=(ImageView)rootView.findViewById(R.id.iv_icono);
        // Inflate the layout for this fragment

        SharedPreferences prefe = getActivity().getSharedPreferences("datos", Context.MODE_PRIVATE);
        tv_placa.setText(prefe.getString("placa", "0-1"));

        proximoDiaPicoyPlacaDiaActual();
        proximoDiaPicoYplaca();
        festivosYDominicales();

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

        } else {
            iv_icono.setImageResource(R.drawable.visto);
            tv_picoyplaca.setTextColor(Color.GREEN);
            tv_picoyplaca.setText("No Tiene Pico y Placa ");
        }
        tv_hoy.setText(" " + picoYplacaHoy + " ");
    }
}
