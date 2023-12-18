package com.example.braining.ui.slideshow;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;
import com.example.braining.R;
import com.example.braining.databinding.FragmentSlideshowBinding;
import com.google.android.gms.common.util.Strings;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class SlideshowFragment extends Fragment {

    private FragmentSlideshowBinding binding;

    String[] wordsPrompt;
    String[] total;
    TextView wordDisplay;


    Button cycle;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SlideshowViewModel slideshowViewModel =
                new ViewModelProvider(this).get(SlideshowViewModel.class);
        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        cycle = binding.wordcycler;
        wordDisplay = binding.worddisplay;
        wordsPrompt = new String[]{
                "Bitter",
                "Acidic",
                "Sour",
                "Sweet",
                "Flavor",
                "Savory",
                "Salty",
                "Hardy",
                "Umami",
                "Seasoning"
        };

        Random random = new Random();
        String[] duplicates = new String[3];
        for (int i = 0; i < duplicates.length; i++) {
            duplicates[i] = wordsPrompt[random.nextInt(duplicates.length)];
        }
        total = new String[duplicates.length + wordsPrompt.length];
        for (int i = 0; i < total.length; i++) {
            if (i < wordsPrompt.length) {
                total[i] = wordsPrompt[i];
            } else {
                total[i] = duplicates[i - wordsPrompt.length];
            }
        }
        AtomicInteger idx = new AtomicInteger();
        cycle.setOnClickListener(v -> {
            if (!(idx.get() == total.length)) {
                wordDisplay.setText(total[idx.get()]);
                idx.addAndGet(1);
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                String prompt = total[random.nextInt(total.length)];
                builder.setTitle("Was the word " + prompt + " in the list more than once?");
                boolean answer = checkWord(prompt, duplicates);
                DialogInterface.OnClickListener positive = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (answer) {
                            dialog.dismiss();
                            cycle.setBackgroundColor(Color.GREEN);
                        } else {
                            cycle.setBackgroundColor(Color.RED);
                        }
                    }
                };
                DialogInterface.OnClickListener negative = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!answer) {
                            dialog.dismiss();
                            cycle.setBackgroundColor(Color.GREEN);
                        } else {
                            cycle.setBackgroundColor(Color.RED);
                        }
                    }
                };

                DialogInterface.OnClickListener neutral = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        cycle.setBackgroundColor(Color.BLUE);
                    }
                };
                builder.setPositiveButton("Yes!", positive);
                builder.setNegativeButton("No!", negative);
                builder.setNeutralButton("I Don't Know!", neutral);
                builder.create();
                builder.show();
            }
        });











        return root;
    }

    private boolean checkWord(String input, String[] checked) {
        List<String> temp = Arrays.asList(checked);
        return temp.contains(input);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}