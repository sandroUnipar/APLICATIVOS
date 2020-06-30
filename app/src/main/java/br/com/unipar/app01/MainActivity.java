package br.com.unipar.app01;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = (Button) findViewById(R.id.botao_tela);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cliente ps = getData();
                if(ps.getNascimento() != null && ps.getCPF != 0 && ps.getRendaMensal() != 0) {
                    if(check(ps)){
                        ps = calc(ps);
                        toResultActivity(ps);
                    }else{
                        Toast toast = Toast.makeText(getApplicationContext(), "Não aprovado", Toast.LENGTH_LONG);
                        toast.setMargin(0,0);
                        toast.show();
                    }

                }
            }
        }
    }



    private Cliente getData (){
        EditText cpf = (EditText) findViewById(R.id.CPF);
        EditText nasc = (EditText) findViewById(R.id.Data);
        EditText renda = (EditText) findViewById(R.id.renda01);
        Cliente ps = new Cliente (0, null, 0, null, 0);
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");


        try {
            ps.setCPF(Long.parseLong(cpf.getText().toString()));
            ps.setNascimento(df.parse(nasc.getText().toString()));
            ps.setRendaMensal((float) Double.parseDouble(renda.getText().toString()));
            System.out.println(ps.getNascimento ());

        }catch (NumberFormatException e){
            Toast toast = Toast.makeText(getApplicationContext(), "Preencha os campos corretamente", Toast.LENGTH_LONG);
            toast.setMargin(0,0);
            toast.show();

        }catch (ParseException pe){
            Toast toast = Toast.makeText(getApplicationContext(), "A data inserida está incorreta", Toast.LENGTH_LONG);
            toast.setMargin(0,0);
            toast.show();
        }catch (NullPointerException npe){
            Toast toast = Toast.makeText(getApplicationContext(), "Preencha todos os campos corretamente", Toast.LENGTH_LONG);
            toast.setMargin(0,0);
            toast.show();
        }

        return ps;
    }



    private void toResultActivity(Cliente ps){
        System.out.println("teste");
        Intent intent = new Intent(MainActivity.this, Resultado.class);
        Bundle b = new Bundle();
        b.putDouble("saldo", ps.getSaldo());
        b.putString("data", ps.getNascimento ().toString());

        intent.putExtras(b);
        startActivity(intent);
        finish();
    }
    private Cliente calc(Cliente ps){
        Calendar cal = Calendar.getInstance();
        cal.setTime(ps.getNascimento ());
        cal.add(Calendar.DATE, 20);
        cal.set(Calendar.YEAR, 2020);

        String date = cal.get(Calendar.YEAR)+"-"+(cal.get(Calendar.MONTH)+1)+"-"+cal.get(Calendar.DAY_OF_MONTH);
        double money = (ps.getRendaMensal ()/100)*70;

        if(money > 475){
            money = 475.00;
        }

        ps.setNascimento (date);
        ps.setSaldo((float) money);
        return ps;
    }
    private boolean check(Cliente ps){
        boolean valid = true;
        Calendar cal = Calendar.getInstance();
        cal.setTime(ps.getNascimento ());
        int years = Calendar.getInstance().get(Calendar.YEAR) - cal.get(Calendar.YEAR);
        if(years < 18) {
            valid = false;
        }

        if(ps.getRendaMensal () > 5000){
            valid = false;
        }
        return valid;
    }
}