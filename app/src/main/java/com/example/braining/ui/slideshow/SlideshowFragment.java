package com.example.braining.ui.slideshow;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;
import com.example.braining.R;
import com.example.braining.databinding.FragmentSlideshowBinding;

public class SlideshowFragment extends Fragment {

    private FragmentSlideshowBinding binding;

    View[] memoryGameSlideshow;
    ViewFlipper viewPager;

    @SuppressLint("ResourceType")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SlideshowViewModel slideshowViewModel =
                new ViewModelProvider(this).get(SlideshowViewModel.class);
        binding = FragmentSlideshowBinding.inflate(inflater, container, false);

        final String TAG = "SLIDE: ";
        Log.d(TAG, "Entered Successfully");
        View root = binding.getRoot();
        memoryGameSlideshow = new View[]{

        };
        Log.d(TAG, "Initialized View Array");
        viewPager = new ViewFlipper(getContext());
        for (View view : memoryGameSlideshow) {
            viewPager.addView(view);
        }
        Log.d(TAG, "Added Views to Viewpager");
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}