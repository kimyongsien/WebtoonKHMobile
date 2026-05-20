package kh.edu.rupp.webtoonkh;

import android.app.DatePickerDialog;
import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Locale;

public class FeedbackActivity extends BaseNavigationActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        setupFloatingBottomNavigation(2);
        setupBirthdayPicker();
        setupSendButton();
    }

    private void setupBirthdayPicker() {
        EditText inputBirthday = findViewById(R.id.inputBirthday);

        inputBirthday.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();

            DatePickerDialog dialog = new DatePickerDialog(
                    this,
                    (view, year, month, dayOfMonth) -> inputBirthday.setText(
                            String.format(
                                    Locale.US,
                                    "%02d/%02d/%04d",
                                    dayOfMonth,
                                    month + 1,
                                    year
                            )
                    ),
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );

            dialog.show();
        });
    }

    private void setupSendButton() {
        EditText inputName = findViewById(R.id.inputName);
        EditText inputBirthday = findViewById(R.id.inputBirthday);
        EditText inputMessage = findViewById(R.id.inputMessage);
        TextView btnSendFeedback = findViewById(R.id.btnSendFeedback);

        btnSendFeedback.setOnClickListener(v -> {
            boolean hasName = inputName.getText().toString().trim().length() > 0;
            boolean hasBirthday = inputBirthday.getText().toString().trim().length() > 0;
            boolean hasMessage = inputMessage.getText().toString().trim().length() > 0;

            if (!hasName || !hasBirthday || !hasMessage) {
                Toast.makeText(this, "Please fill out the required fields", Toast.LENGTH_SHORT).show();
                return;
            }

            new AlertDialog.Builder(this)
                    .setMessage("Thank you for feedback")
                    .setPositiveButton("OK", null)
                    .show();
        });
    }
}
