package com.example.calculadoragorjeta;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;

public class MainActivity extends AppCompatActivity {

    private static String TOAST_MESSAGE_FOR_EMPTY_VALUE = "Digite um valor primeiro!";
    private static String UNIDADE_MONETARIA = "R$";

    private EditText editValor;
    private TextView textPorcentagem;
    private TextView textGorjeta;
    private TextView textTotal;
    private SeekBar seekBarGorjeta;

    private double porcentagem = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editValor = findViewById(R.id.editValor);
        textGorjeta = findViewById(R.id.textGorjeta);
        textPorcentagem = findViewById(R.id.textPorcentagem);
        textTotal = findViewById(R.id.textTotal);
        seekBarGorjeta = findViewById(R.id.seekBarGorjeta);

        //Adicionar Listener SeekBar
        setSeekBarGorjetaListener();

    }


    private void setSeekBarGorjetaListener() {

        seekBarGorjeta.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                porcentagem = progress;
                textPorcentagem.setText(Math.round(porcentagem)+ "%");
                calcular();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });
    }


    private void calcular(){
        String valorRecuperado = editValor.getText().toString();

        if (valorRecuperado.isEmpty()) showToastIfValueIsEmpty();
        else calcularGorjeta(valorRecuperado);
    }


    private void showToastIfValueIsEmpty() {
        Toast.makeText(getApplicationContext(), TOAST_MESSAGE_FOR_EMPTY_VALUE, Toast.LENGTH_SHORT).show();
    }


    private void calcularGorjeta(String valorRecuperado) {

        // Converte o valorRecuperado para BigDecimal
        BigDecimal valorDigitado = new BigDecimal(valorRecuperado);

        // Calcula e arredonda (setScale) para duas casas decimais a gorjeta e o valor total
        BigDecimal gorjeta = getGorjeta(valorDigitado).setScale(2, BigDecimal.ROUND_UP);
        BigDecimal total = getTotal(gorjeta, valorDigitado).setScale(2, BigDecimal.ROUND_UP);

        // Insere o valor da gorjeta e do total no TextView
        setResultText(gorjeta, total);
    }


    private BigDecimal getGorjeta(BigDecimal valorDigitado) {
        return BigDecimal.valueOf(valorDigitado.doubleValue() * (porcentagem / 100));
    }


    private BigDecimal getTotal(BigDecimal gorjeta, BigDecimal valorDigitado) {
        return BigDecimal.ZERO.add(gorjeta).add(valorDigitado);
    }


    private void setResultText(BigDecimal gorjeta, BigDecimal total) {
        textGorjeta.setText(UNIDADE_MONETARIA + gorjeta);
        textTotal.setText(UNIDADE_MONETARIA + total);
    }

}