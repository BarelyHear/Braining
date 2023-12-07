package com.example.braining.ui.gallery;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.braining.databinding.FragmentGalleryBinding;

import java.util.Arrays;
import java.util.Random;

public class GalleryFragment extends Fragment {

    private FragmentGalleryBinding binding;


    TextView scoreDisplay;
    Boolean[] stored;
    ToggleButton[] buttons;

    Button start;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        GalleryViewModel galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);
        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        scoreDisplay = binding.scoredisplay;

        start = binding.startbutton;


        // Start of Game Code
        buttons = new ToggleButton[]{
                binding.btn1, binding.btn2, binding.btn3, binding.btn4,
                binding.btn5, binding.btn6, binding.btn7, binding.btn8,
                binding.btn9, binding.btn10, binding.btn11, binding.btn12,
                binding.btn13, binding.btn14, binding.btn15, binding.btn16};
        for (int y = 0; y < buttons.length; y++) {
            final int why = y;
            buttons[why].setOnClickListener(v -> {
                buttons[why].setChecked(buttons[why].isChecked());
                if (buttons[why].isChecked()) {
                    buttons[why].setBackgroundColor(Color.BLUE);
                } else {
                    buttons[why].setBackgroundColor(Color.BLACK);
                }
            });
        }

        start.setOnClickListener(v -> {
            // Note that randomizeButtons is coupled to the game loop and so is not a drop in abstraction
            randomizeButtons();
        });




        return root;
    }

    private void randomizeButtons() {
        stored = new Boolean[16];
        Random random = new Random();
        int idx = 0;
        for (ToggleButton tb : buttons) {
            tb.setChecked(random.nextBoolean());
            if (tb.isChecked()) {
                Log.d("Game: ", "Active Button: " + tb.toString());

                tb.setChecked(true);
                tb.setBackgroundColor(Color.BLUE);
            } else {
                Log.d("Game: ", "Inactive Button: " + tb.toString());
                tb.setChecked(false);
                tb.setBackgroundColor(Color.BLACK);
            }

            stored[idx] = tb.isChecked();
            Log.d("Game: ", "stored sub " + idx + " is " + stored[idx]);
            idx++;
        }
        start.setText("Press Me For Your Turn");
        start.setOnClickListener(v -> {
            playerTurn();
        });
    }

    private void playerTurn() {
        for (ToggleButton tb : buttons) {
            tb.setChecked(false);
            tb.setBackgroundColor(Color.BLACK);
        }
        start.setText("Check Score");
        start.setOnClickListener(v -> {
            scorePlayer();
        });
    }

    private void scorePlayer() {
        Integer score = 0;
        Integer potential = 0;
        for (int i = 0; i < buttons.length; i++) {
            if (buttons[i].isChecked() && stored[i].booleanValue()) {
                score++;
            }
            if (buttons[i].isChecked() && !stored[i].booleanValue()) {
                score--;
            }
            if (stored[i]) {
                potential++;
            }
        }
        scoreDisplay.setText("Score: " + score + "/" + potential);
        start.setText("Play Again");
        start.setOnClickListener(v -> {
            randomizeButtons();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}