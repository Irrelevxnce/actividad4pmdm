package com.irrelevxnce.actividad4;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.irrelevxnce.actividad4.DB.AppContext;
import com.irrelevxnce.actividad4.DB.DBHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    EditText nif;
    EditText nombre;
    EditText f_nac;
    CheckBox estudiante;
    TextView sexo;
    RadioGroup btns;
    RadioButton hombre;
    RadioButton mujer;
    ImageButton tarjeta;
    ImageButton guardar;
    AppContext con;
    Boolean valid;
    Boolean infoCorrect;
    String gen;
    int isChecked;
    int d;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nif = findViewById(R.id.Nif);
        nombre = findViewById(R.id.Nombre);
        f_nac = findViewById(R.id.editTextDate);
        estudiante = findViewById(R.id.checkBox);
        sexo = findViewById(R.id.textView);
        btns = findViewById(R.id.radiogroup);
        hombre = findViewById(R.id.radioButton);
        mujer = findViewById(R.id.radioButton2);
        tarjeta = findViewById(R.id.imageButton);
        guardar = findViewById(R.id.imageButton2);
        f_nac.setOnClickListener(this::establecerFecha);
        f_nac.setOnTouchListener((view, event) -> {
            int inType = f_nac.getInputType();
            f_nac.setInputType(InputType.TYPE_NULL);
            f_nac.onTouchEvent(event);
            f_nac.setInputType(inType);
            return true;
        });
        estudiante.setOnClickListener(this :: onCheckEstudiante);
        guardar.setOnClickListener(this::guardar);
        con = (AppContext) getApplicationContext();
        hombre.setOnClickListener(this :: onClickHombre);
        mujer.setOnClickListener(this :: onClickMujer);
        tarjeta.setOnClickListener(this :: onClickTarjeta);
        con.nif = nif.getText().toString();
    }

    private void onClickTarjeta(View view) {
        Intent i = new Intent(this, Tarjeta.class);
        startActivity(i);
    }

    private void onClickMujer(View view) {
        gen = "Mujer";
    }

    private void onClickHombre(View view) {
        gen = "Hombre";
    }

    private void onCheckEstudiante(View view) {
        if (isChecked == 1) {
            isChecked = 0;
        } else {
            isChecked = 1;
        }
    }

    private void guardar(View view) {
        infoCorrect = true;
        checkInfo();
        if (infoCorrect) {
            DBHelper dbHelper = new DBHelper(MainActivity.this, con.create);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            if (db != null) {
                Toast.makeText(this, "DB CREADA", Toast.LENGTH_SHORT).show();
            }
            valid = false;
            int sum = 0;
            boolean isOdd = true;
            String nifString = String.valueOf(nif.getText());
            String nifCreate = nifString.substring(1);
            nifString = nifString.substring(1, 8);
            for (int i = nifString.length() -1 ; i >= 0; i--) {
                int calc = nifString.charAt(i) - '0';
                if (isOdd) {
                    if(calc != 0) {
                        calc *= 2;
                        if (calc >= 10) {
                            sum += 1;
                            sum += calc % 10;
                        } else {
                            sum += calc;
                        }
                    }
                } else {
                    sum += calc;
                }
                isOdd = !isOdd;
            }
            int control = sum % 10;
            if (control != 0) {
                d = 10 - control;
            } else {
                d = 0;
            }

            if (d == (nif.getText().charAt(8) - '0')) {
                valid = true;
            }
            if (valid) {
                dbHelper.addCliente(nifCreate, String.valueOf(nombre.getText()), String.valueOf(f_nac.getText()), isChecked, gen);
                Toast.makeText(this, "INSERTADO", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "NIF INVALIDO", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "INFORMACION INCORRECTA", Toast.LENGTH_SHORT).show();
        }
    }

    private void establecerFecha(View view) {
        Fecha FechaFragment = new Fecha();
        FechaFragment.show(getSupportFragmentManager(), "FECHA");
    }

    @Override
    public void onDateSet(DatePicker datePicker, int a, int mes, int dia) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Calendar calendario = Calendar.getInstance();
        calendario.set(Calendar.YEAR, a);
        calendario.set(Calendar.MONTH, mes);
        calendario.set(Calendar.DAY_OF_MONTH, dia);
        String fecha = sdf.format(calendario.getTime());
        f_nac.setText(fecha);
    }

    public void checkInfo() {
        if (TextUtils.isEmpty(nif.getText())) {
            nif.setError("Campo Obligatorio");
            infoCorrect = false;
        }
        if (nif.getText().length() != 9) {
            nif.setError("FORMATO INCORRECTO");
            infoCorrect = false;
        }
        if (TextUtils.isEmpty(nombre.getText())) {
            nombre.setError("Campo Obligatorio");
            infoCorrect = false;
        }
        if (TextUtils.isEmpty(f_nac.getText())) {
            f_nac.setError("Campo Obligatorio");
            infoCorrect = false;
        }
        if (!hombre.isChecked() && !mujer.isChecked()) {
            mujer.setError("Campo Obligatorio");
            infoCorrect = false;
        }
    }
}