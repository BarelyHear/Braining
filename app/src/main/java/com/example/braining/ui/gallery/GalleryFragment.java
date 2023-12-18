package com.example.braining.ui.gallery;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.ViewFlipper;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.braining.MainActivity;
import com.example.braining.R;
import com.example.braining.databinding.FragmentGalleryBinding;
import java.util.Random;

public class GalleryFragment extends Fragment {

    private FragmentGalleryBinding binding;



    Boolean[] stored;
    ToggleButton[] toggleButtons;

    final String TAG = "GAMES: ";
    Button start;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        GalleryViewModel galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);
        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        stored = new Boolean[16];

        Log.d(TAG, "Entered successfully.");
        toggleButtons = new ToggleButton[]{
                binding.btn1, binding.btn2, binding.btn3, binding.btn4,
                binding.btn5, binding.btn6, binding.btn7, binding.btn8,
                binding.btn9, binding.btn10, binding.btn11, binding.btn12,
                binding.btn13, binding.btn14, binding.btn15, binding.btn16};
        start = binding.startbutton;

        // Start of Game Code
        Thread gameLoop = new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                randomizeButtons();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                playerTurn();
                resetButtons();
            }
        };
        start.setOnClickListener(v -> {
            start.setClickable(false);
            gameLoop.start();
        });

        return root;
    }

    private void resetButtons() {
        stored = new Boolean[16];
        toggleButtons = new ToggleButton[]{
                binding.btn1, binding.btn2, binding.btn3, binding.btn4,
                binding.btn5, binding.btn6, binding.btn7, binding.btn8,
                binding.btn9, binding.btn10, binding.btn11, binding.btn12,
                binding.btn13, binding.btn14, binding.btn15, binding.btn16};
    }

    private void randomizeButtons() {
        stored = new Boolean[16];

        for (int y = 0; y < toggleButtons.length; y++) {
            final int constY = y;
            toggleButtons[constY].setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (toggleButtons[constY].isChecked()) {
                    toggleButtons[constY].setBackgroundResource(R.drawable.cell_button);
                } else {
                    toggleButtons[constY].setBackgroundResource(R.drawable.cell_button_off);
                }
            });
        }
        int idx = 0;
        Random random = new Random();
        for (ToggleButton tb : toggleButtons) {
            tb.setChecked(random.nextBoolean());
            stored[idx] = tb.isChecked();
            if (tb.isChecked()) {
                Log.d(TAG, "Active Button: " + tb.toString());
                tb.setBackgroundResource(R.drawable.cell_button);
            } else {
                Log.d(TAG, "Inactive Button: " + tb.toString());
                tb.setBackgroundResource(R.drawable.cell_button_off);
            }
            // Stored is the problem.
            Log.d(TAG, "stored sub " + idx + " is " + stored[idx]);
            idx++;
        }
        for (int i = 0; i < stored.length; i++) {
            if (!stored[i]) {
                int finalI = i;
                toggleButtons[i].setOnClickListener(v -> {
                    toggleButtons[finalI].setBackgroundResource(R.drawable.cell_button_wrong_choice);
                    playerLoses();
                });
            }
            if (stored[i] && toggleButtons[i].isChecked()) {
                playerWins();
            }
        }
    }

    private void playerWins() {
        Context context = getContext();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("You Won!");
        DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                stored = null;
                dialog.dismiss();
            }
        };
        builder.setNeutralButton("Go For Broke", onClickListener);
        builder.create().show();
    }

    private void playerTurn() {
        for (ToggleButton tb : toggleButtons) {
            tb.setChecked(false);
            tb.setBackgroundResource(R.drawable.cell_button_off);
        }
    }


    private void playerLoses() {
        start.setClickable(true);
        Context context = getContext();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("You Lost");

        DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                stored = null;
                dialog.dismiss();
            }
        };
        builder.setNeutralButton("Try Again", onClickListener);
        builder.create().show();
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
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}