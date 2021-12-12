package com.roksanagulewska.seniorsapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.roksanagulewska.seniorsapp.R;

public class UsersInfoActivity extends AppCompatActivity {

    EditText nameEditTxt;
    EditText localisationEditTxt;
    EditText ageEditTxt;
    SwitchCompat sexSwitch;
    Button confirmInfoBtn;
    String sex = "female"; //domyślna płeć to kobieta, ponieważ taką wartość reprezentuje switch w swoim położeniu startowym
    int age; // ustawiam wartość początkową wieku na 0, aby upewnić się że użytkownik wprowadził jakąś wartość
    boolean ageFormat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_info);

        nameEditTxt = findViewById(R.id.nameEditText);
        localisationEditTxt = findViewById(R.id.localisationEditText);
        ageEditTxt = findViewById(R.id.ageEditText);
        sexSwitch = findViewById(R.id.sexSwitch);
        confirmInfoBtn = findViewById(R.id.confirmInfoButton); //przycisk potwierdzający wpisane informacje

        Intent registerIntent = getIntent(); //intencja która wywołała tą aktywność
        Bundle registerBundle = registerIntent.getExtras(); // przypisanie bundla który przyszedł z intencją
        String email = registerBundle.getString("email"); //email przysłany z aktywności RegistrationActivity
        String password = registerBundle.getString("password"); //hasło przysłane z aktywności RegistrationActivity

        //sprawdzanie stanu switcha
        sexSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) { //jeżeli został przesunięty
                    sex = "male";
                } else { //jeżeli jest w pozycji startowej
                    sex = "female";
                }
            }
        });

        confirmInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = nameEditTxt.getText().toString().trim(); //przypisywanie zmiennej name, tekstu wpisanego w pole nameEditText, trim() pozbywa się białych znaków z początku i końca łańcucha
                String localisation = localisationEditTxt.getText().toString().trim(); //przypisywanie zmiennej localisation, tekstu wpisanego w pole nameEditText, trim() pozbywa się białych znaków z początku i końca łańcucha
                ageFormat = true;

                try {
                    age = Integer.parseInt(ageEditTxt.getText().toString().trim()); //przypisanie zmiennej age wartości wpisanej w pole ageEditText
                } catch (NumberFormatException exception) { //jeżeli wpisany text nie będzie liczbą, zostanie wyrzucony wyjątek
                    if (age != 0) {
                        ageFormat = false;
                    }
                }

                if (ageFormat) {
                    if (name.isEmpty() || localisation.isEmpty() || ageEditTxt.getText().toString().isEmpty()) { //sprawdzenie czy wszystkie pola zostały uzupełnione
                        Toast.makeText(getApplicationContext(), "Please add all required information.", Toast.LENGTH_SHORT).show(); //jeśli nie, wyświetlany jest komunikat
                    } else {
                        if (containsLettersOnly(name) && containsLettersOnly(localisation)) { //sprawdzenie czy do pól localisation zostały wprowadzone łańcuchy znaków zawierające tylko litery
                            if (age >= 130 || age < 18) { //sprawdzenie czy podany wiek jest ralistyczny i zgodny z przeznaczeniem aplikacji
                                Toast.makeText(getApplicationContext(), "Insert correct age, it must be greater or equal to 18.", Toast.LENGTH_LONG).show();
                            } else { //jeżeli wszystkie powyższe warunki mają odpowiednie wartości

                                name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase(); //formatowanie imienia tak aby zaczynało się od wielkiej litery
                                localisation = localisation.substring(0, 1).toUpperCase() + localisation.substring(1).toLowerCase(); //formatowanie miasta tak aby zaczynało się od wielkiej litery

                                Intent infoIntent = new Intent(getApplicationContext(), PreferencesActivity.class);
                                Bundle infoBundle = new Bundle();
                                infoBundle.putString("email", email);
                                infoBundle.putString("password", password);
                                infoBundle.putString("name", name);
                                infoBundle.putString("localisation", localisation);
                                infoBundle.putInt("age", age);
                                infoBundle.putString("sex", sex);
                                infoIntent.putExtras(infoBundle); //przyłączanie bundla z danymi wprowadzonymi przez użytkownika do intencji
                                startActivity(infoIntent);
                                finish();

                            }
                        } else { //jeśli localisation i name składają się z innych znaków niż litery
                            Toast.makeText(getApplicationContext(), "Name and localisation must contain only letters.", Toast.LENGTH_LONG).show();
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Incorrect age format.", Toast.LENGTH_LONG).show(); //komunikat informujący o wyrzuceniu wyjątku
                }

            }
        });
    }

    //metoda sprawdzająca czy łańcuch podany w argumencie zawiera tylko litery
    public boolean containsLettersOnly(String text) {
        char[] textCharacters = text.toCharArray();
        boolean isIt = true;

        for (char character : textCharacters) {
            if (!Character.isLetter(character)) {
                Log.d("VALID", character + " is NOT letter");
                isIt = false;
                return isIt;
            }
            Log.d("VALID", character + " is letter");
        }
        return isIt;
    }

}