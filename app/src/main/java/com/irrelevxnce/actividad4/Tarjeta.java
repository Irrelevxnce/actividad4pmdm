package com.irrelevxnce.actividad4;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.irrelevxnce.actividad4.DB.AppContext;
import com.irrelevxnce.actividad4.DB.DBHelper;

public class Tarjeta extends AppCompatActivity {

    Button b1, b2;
    EditText num, mes, a, cvc;
    Boolean infoCorrect, isOdd;
    Boolean valid;
    AppContext context;
    String nif;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tarjeta);
        b1 = findViewById(R.id.aceptar);
        b2 = findViewById(R.id.cancelar);
        num = findViewById(R.id.numero);
        mes = findViewById(R.id.mes);
        a = findViewById(R.id.año);
        cvc = findViewById(R.id.cvc);
        b1.setOnClickListener(this :: onCLickb1);
        b2.setOnClickListener(this :: onClickb2);
        context  = (AppContext) getApplicationContext();
        nif = context.nif;
    }

    private void onCLickb1(View view) {
        infoCorrect = true;
        checkInfo();
        if(infoCorrect) {
            DBHelper dbHelper = new DBHelper(Tarjeta.this, context.create);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            String number = num.getText().toString();
            number = number.replace(" ", "");
            number = number.replace("-", "");
            int sum = 0;
            valid = false;
            isOdd = true;
            for (int i = number.length() -1 ; i >= 0; i--) {
                int calc = number.charAt(i) - '0';
                if (!isOdd) {
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
            if (sum % 10 == 0) {
                valid = true;
            }
            if (valid) {
                dbHelper.addTarjeta(nif, number, Integer.parseInt(mes.getText().toString()), Integer.parseInt(a.getText().toString()), Integer.parseInt(cvc.getText().toString()));
                Toast.makeText(this, "Inserted", Toast.LENGTH_SHORT);
            } else {
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void onClickb2(View view) {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    public void checkInfo() {
        if (TextUtils.isEmpty(num.getText())) {
            num.setError("Campo Obligatorio");
            infoCorrect = false;
        }
        if (num.getText().length() > 20) {
            num.setError("FORMATO INCORRECTO");
            infoCorrect = false;
        }
        if (TextUtils.isEmpty(mes.getText()) || mes.getText().length() != 2) {
            mes.setError("ERROR");
            infoCorrect = false;
        }
        if (TextUtils.isEmpty(a.getText()) || a.getText().length() != 2) {
            a.setError("ERROR");
            infoCorrect = false;
        }
        if (TextUtils.isEmpty(cvc.getText()) || cvc.getText().length() != 3) {
            cvc.setError("ERROR");
            infoCorrect = false;
        }
    }
}
