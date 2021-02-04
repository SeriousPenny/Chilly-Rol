package com.seriouspenny.chillyrol.Fragments;


import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.seriouspenny.chillyrol.Adapters.DrawingImagesListAdapter;
import com.seriouspenny.chillyrol.Constants.Constants;
import com.seriouspenny.chillyrol.MainViewModel;
import com.seriouspenny.chillyrol.R;
import com.seriouspenny.chillyrol.Utilities.Utils;
import com.skydoves.colorpickerview.ColorPickerDialog;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class DrawingToolsFragment extends Fragment {

    private MainViewModel viewModel;
    private SeekBar strokeWidthSeekBar;
    private ImageView ivPencil, ivEraser, ivUndo;
    private DrawingToolsFragInterface callbacks;
    private RecyclerView rvDragCharacters;
    private DrawingImagesListAdapter rvdcAdapter;

    public interface DrawingToolsFragInterface
    {
        void undoLastAction();
        void selectPencil();
        void selectEraser();
        void clearCanvas();
        void changeColorPencil(int color);
    }

    public DrawingToolsFragment() {
        //Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_drawing_tools, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        callbacks = (DrawingToolsFragInterface)requireActivity();
        strokeWidthSeekBar = requireActivity().findViewById(R.id.sbStrokeWidth);
        ivPencil = requireActivity().findViewById(R.id.btnPencil);
        ivEraser = requireActivity().findViewById(R.id.btnEraser);
        ivUndo = requireActivity().findViewById(R.id.btnUndo);
        rvDragCharacters = requireActivity().findViewById(R.id.rvDragCharacters);

        //Characters list
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        rvdcAdapter = new DrawingImagesListAdapter(viewModel.getCreatures().getValue());
        rvDragCharacters.setLayoutManager(linearLayoutManager);
        rvDragCharacters.setAdapter(rvdcAdapter);

        //Highlight the tool selected by default (pencil)
        ivPencil.getBackground().setColorFilter(ContextCompat.getColor(requireContext(), R.color.violet), PorterDuff.Mode.LIGHTEN);
        ivPencil.setColorFilter(viewModel.getPencilColor());

        //Change progress bar
        strokeWidthSeekBar.setProgress((int)(viewModel.getStrokeWidth().getValue() - Constants.MINIMUM_STROKE_WIDTH));

        strokeWidthSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                viewModel.getStrokeWidth().setValue(Constants.MINIMUM_STROKE_WIDTH + progress*2);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        ivUndo.setOnClickListener(v -> {
            callbacks.undoLastAction();
            Utils.animateClick(v);
        });

        ivPencil.setOnClickListener(v -> {
            Utils.animateClick(v);
            ivPencil.getBackground().setColorFilter(ContextCompat.getColor(requireContext(), R.color.violet), PorterDuff.Mode.LIGHTEN);
            ivEraser.getBackground().clearColorFilter();
            callbacks.selectPencil();
        });

        ivPencil.setOnLongClickListener(v -> {
            Utils.animateClick(v);

            new ColorPickerDialog.Builder(requireContext())
                    .setTitle("Elige color de lápiz")
                    .setPreferenceName("PencilColorDialog")
                    .setPositiveButton("Okay",
                            (ColorEnvelopeListener) (envelope, fromUser) -> {
                                callbacks.changeColorPencil(envelope.getColor());
                                ivPencil.setColorFilter(envelope.getColor());
                            })
                    .setNegativeButton("Nein!",
                            (dialogInterface, i) -> dialogInterface.dismiss())
                    .attachAlphaSlideBar(false)
                    .attachBrightnessSlideBar(true)
                    .show();
            return true;
        });

        ivEraser.setOnClickListener(v -> {
            Utils.animateClick(v);
            ivPencil.getBackground().clearColorFilter();
            ivEraser.getBackground().setColorFilter(ContextCompat.getColor(requireContext(), R.color.violet), PorterDuff.Mode.LIGHTEN);
            callbacks.selectEraser();
        });

        ivEraser.setOnLongClickListener(v -> {
            AlertDialog alertDialog = new AlertDialog.Builder(requireContext()).create();
            alertDialog.setTitle("¿Quieres borrar todo?");

            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog, which) -> {
                callbacks.clearCanvas();
            });

            alertDialog.setCancelable(true);

            alertDialog.show();

            Utils.animateClick(v);

            return true;
        });
    }

    public void refreshList()
    {
        rvdcAdapter.notifyDataSetChanged();
    }
}
