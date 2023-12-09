package com.example.braining.ui.gallery;

import static com.example.braining.R.drawable.*;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.braining.databinding.FragmentGalleryBinding;

import java.util.Random;

public class GalleryFragment extends Fragment {

    private FragmentGalleryBinding binding;

    TextView scoreDisplay, gameDesc;

    ViewFlipper flipper;

    Boolean[] stored;
    ToggleButton[] toggleButtons;

    final String TAG = "GAMES: ";
    Button start, incDifficulty;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        GalleryViewModel galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);
        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        gameDesc = binding.gameexplanation;
        flipper = binding.flipper;

        Log.d(TAG, "Entered successfully.");


        scoreDisplay = binding.scoredisplay;

        start = binding.startbutton;

        // Start of Game Code
        toggleButtons = new ToggleButton[]{
                binding.btn1, binding.btn2, binding.btn3, binding.btn4,
                binding.btn5, binding.btn6, binding.btn7, binding.btn8,
                binding.btn9, binding.btn10, binding.btn11, binding.btn12,
                binding.btn13, binding.btn14, binding.btn15, binding.btn16};
        for (int y = 0; y < toggleButtons.length; y++) {
            final int constY = y;
            toggleButtons[constY].setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (toggleButtons[constY].isChecked()) {
                    toggleButtons[constY].setBackgroundResource(cell_button);
                } else {
                    toggleButtons[constY].setBackgroundResource(cell_button_off);
                }
            });
        }
        Thread gameLoop = new Thread() {
            @Override
            public void run() {
                randomizeButtons();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                playerTurn();
            }
        };
        start.setOnClickListener(v -> {
            gameDesc.setBackgroundColor(Color.BLUE);
            gameLoop.start();
        });

        return root;
    }

    private void randomizeButtons() {
        stored = new Boolean[16];
        Random random = new Random();
        int idx = 0;
        for (ToggleButton tb : toggleButtons) {
            tb.setChecked(random.nextBoolean());
            if (tb.isChecked()) {
                Log.d(TAG, "Active Button: " + tb.toString());
                tb.setBackgroundResource(cell_button);
            } else {
                Log.d(TAG, "Inactive Button: " + tb.toString());
                tb.setBackgroundResource(cell_button_off);

            }

            stored[idx] = tb.isChecked();
            Log.d(TAG, "stored sub " + idx + " is " + stored[idx]);
            idx++;
        }
    }

    private void playerTurn() {
        for (ToggleButton tb : toggleButtons) {
            tb.setChecked(false);
            tb.setBackgroundResource(cell_button_off);
        }

        int potential = 0;
        for (int i = 0; i < stored.length; i++) {
            if (stored[i]) {
                potential++;
            }
        }
        final int resetPotential = potential;
        for (int i = 0; i < toggleButtons.length; i++) {
            if (!stored[i]) {
                int finalI = i;
                toggleButtons[i].setOnClickListener(v -> {
                    toggleButtons[finalI].setBackgroundResource(cell_button_wrong_choice);
                    playerLoses();
                });
            }
        }
    }

    private void playerLoses() {
    }

    private void scorePlayer() {
        Integer score = 0;
        Integer potential = 0;
        for (int i = 0; i < toggleButtons.length; i++) {
            if (toggleButtons[i].isChecked() && stored[i].booleanValue()) {
                score++;
            }
            if (toggleButtons[i].isChecked() && !stored[i].booleanValue()) {
                score--;
            }
            if (stored[i]) {
                potential++;
            }
        }
        scoreDisplay.setText("Score: " + score + "/" + potential);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}