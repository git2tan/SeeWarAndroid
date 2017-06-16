package com.example.seawar;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class RegistrationActivity extends AppCompatActivity implements IRefreshable{
    IModel model;
    IController controller;
    TextView errorLabel;
    EditText addLoginField;
    EditText addPasswordField1;
    EditText addPasswordField2;
    Button registrationButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        model = MyModel.getInstance();
        controller = MyController.getInstance();
        controller.setCurrentActivity(this);

        errorLabel = (TextView)findViewById(R.id.errorLabel);
        addLoginField = (EditText)findViewById(R.id.addLoginField);
        addPasswordField1 = (EditText)findViewById(R.id.addPasswordField1);
        addPasswordField2 = (EditText)findViewById(R.id.addPasswordField2);
        registrationButton = (Button)findViewById(R.id.registrationButtonNew);

        registrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addLoginField.getText().length() < 3){
                    errorLabel.setText("Логин не может быть короче 3 символов");

                }
                else if (addPasswordField1.getText().length() == 0){
                    errorLabel.setText("Пароль не может быть пустым");
                }
                else if (!(addPasswordField1.getText().toString().equals(addPasswordField2.getText().toString()))){
                    errorLabel.setText("Пароли не совпадают");
                }
                else{
                    errorLabel.setText("Отправлен запрос серверу");
                    controller.buttonRegistrationNewAccount(addLoginField.getText().toString(), addPasswordField1.getText().toString());
                }
            }
        });
    }

    public android.os.Handler handler = new android.os.Handler(new android.os.Handler.Callback() {
        @Override
        public boolean handleMessage(android.os.Message msg) {

            if (msg.what == IModel.ModelState.startFrame.ordinal()) {
                Activity tmp = new LoginActivity();
                Intent intent = new Intent(RegistrationActivity.this, tmp.getClass());
                startActivity(intent);
            }
            else if(msg.what == IModel.ModelState.mainMenuFrame.ordinal()){
                Activity tmp = new MainMenuActivity();
                Intent intent = new Intent(RegistrationActivity.this, tmp.getClass());
                startActivity(intent);
            }
            else if(msg.what == IModel.ModelState.onlyRefresh.ordinal()){
                refresh();
            }
            return false;
        }
    });

    @Override
    public void refresh() {
        IModel.RegistrationState state = model.getRegistrationState();

        switch (state){
            case none:
                errorLabel.setText("");
                break;
            case forbidden:
                errorLabel.setText("Логин занят");
                break;
            case success:
                AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationActivity.this);
                builder.setTitle("Зарегистрирован").setMessage("Логин успешно зарегистриован!").setCancelable(false).setNegativeButton("ОК", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
                break;
        }
    }

    @Override
    public Handler getHandler() {
        return handler;
    }

    @Override
    public IModel.ModelState getRelevantState() {
        return IModel.ModelState.registrationFrame;
    }
}
