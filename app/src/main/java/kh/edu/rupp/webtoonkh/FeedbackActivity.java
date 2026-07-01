package kh.edu.rupp.webtoonkh;

import android.app.DatePickerDialog;
import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.Calendar;
import java.util.Locale;

import kh.edu.rupp.webtoonkh.api.ApiService;
import kh.edu.rupp.webtoonkh.api.RetrofitClient;
import kh.edu.rupp.webtoonkh.model.Feedback;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        EditText inputStoryRequest = findViewById(R.id.inputStoryRequest);
        EditText inputMessage = findViewById(R.id.inputMessage);
        TextView btnSendFeedback = findViewById(R.id.btnSendFeedback);

        btnSendFeedback.setOnClickListener(v -> {
            String nameText = inputName.getText().toString().trim();
            String birthdayText = inputBirthday.getText().toString().trim();
            String storyRequestText = inputStoryRequest != null ? inputStoryRequest.getText().toString().trim() : "";
            String messageText = inputMessage.getText().toString().trim();

            if (nameText.isEmpty() || birthdayText.isEmpty() || messageText.isEmpty()) {
                Toast.makeText(this, "Please fill out the required fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Construct full message since the database table feedback only has name and message columns
            StringBuilder fullMessageBuilder = new StringBuilder();
            fullMessageBuilder.append(messageText);

            boolean hasExtras = !birthdayText.isEmpty() || !storyRequestText.isEmpty();
            if (hasExtras) {
                fullMessageBuilder.append("\n\n--- Metadata ---");
                if (!birthdayText.isEmpty()) {
                    fullMessageBuilder.append("\nBirthday: ").append(birthdayText);
                }
                if (!storyRequestText.isEmpty()) {
                    fullMessageBuilder.append("\nRequested Story: ").append(storyRequestText);
                }
            }
            String finalMessageText = fullMessageBuilder.toString();

            btnSendFeedback.setEnabled(false);

            ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
            Feedback feedback = new Feedback(nameText, finalMessageText);

            apiService.postFeedback(feedback).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                    btnSendFeedback.setEnabled(true);
                    if (response.isSuccessful()) {
                        // Clear the input fields on success
                        inputName.setText("");
                        inputBirthday.setText("");
                        if (inputStoryRequest != null) {
                            inputStoryRequest.setText("");
                        }
                        inputMessage.setText("");

                        new AlertDialog.Builder(FeedbackActivity.this)
                                .setMessage("Thank you for your feedback!")
                                .setPositiveButton("OK", null)
                                .show();
                    } else {
                        Toast.makeText(FeedbackActivity.this, "Failed to submit feedback: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                    btnSendFeedback.setEnabled(true);
                    Toast.makeText(FeedbackActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}

