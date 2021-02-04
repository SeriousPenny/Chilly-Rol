package com.seriouspenny.chillyrol.Fragments;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.seriouspenny.chillyrol.Entities.Creature;
import com.seriouspenny.chillyrol.MainViewModel;
import com.seriouspenny.chillyrol.Utilities.MyCanvas;
import com.seriouspenny.chillyrol.R;
import com.seriouspenny.chillyrol.Utilities.Utils;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class DrawingFragment extends Fragment {
    private MainViewModel viewModel;
    private MyCanvas myCanvas;
    private RelativeLayout relativeLayout;
    private DrawingFragInterface callbacks;

    public interface DrawingFragInterface
    {
        void refreshDraggingList();
    }

    public DrawingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_drawing, container, false);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        callbacks = (DrawingFragInterface)requireActivity();

        myCanvas = new MyCanvas(requireContext(), viewModel.getStrokeWidth().getValue(), viewModel.getDrawnPaths(), viewModel.getPencilColor());

        relativeLayout = requireActivity().findViewById(R.id.drawingLayout);
        relativeLayout.addView(myCanvas);

        //What makes dragging views to the canvas possible
        myCanvas.setOnDragListener(new View.OnDragListener() {
            View draggedView;

            @Override
            public boolean onDrag(View v, final DragEvent event) {
                switch (event.getAction()) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        draggedView = (View) event.getLocalState();
                        break;
                    case DragEvent.ACTION_DROP:
                        final float eventX = event.getX();
                        final float eventY = event.getY();

                        //Take the creature and alter its state
                        final Creature creature = (Creature)draggedView.getTag();
                        creature.setColocadaEnCanvas(true);

                        final View view = LayoutInflater.from(requireContext()).inflate(R.layout.drawing_drag_character, relativeLayout, false);
                        view.setTag(draggedView.getTag());
                        ((ImageView)view.findViewById(R.id.imgCharacterDrag)).setImageResource(Utils.getCreatureImageId(creature));
                        ((TextView)view.findViewById(R.id.txtNameDrag)).setText(creature.getNombre());

                        view.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
                            creature.setCanvasX(eventX-((float)view.getWidth()/2));
                            creature.setCanvasY(eventY-((float)view.getHeight()/2));
                            view.setX(creature.getCanvasX());
                            view.setY(creature.getCanvasY());
                        });

                        view.setOnClickListener(v1 -> {
                            ((Creature) v1.getTag()).setColocadaEnCanvas(false);
                            relativeLayout.removeView(view);
                            callbacks.refreshDraggingList();
                        });

                        relativeLayout.addView(view);
                        break;
                    case DragEvent.ACTION_DRAG_ENTERED:
                    case DragEvent.ACTION_DRAG_EXITED:
                    case DragEvent.ACTION_DRAG_ENDED:
                    default:
                        break;
                }
                return true;
            }
        });

        final Observer<Float> strokeWidthObserver = width -> myCanvas.changeStrokeWidth(width);

        //Add the characters views in case there are any
        for(Creature creature : viewModel.getCreatures().getValue())
        {
            if(creature.isColocadaEnCanvas())
            {
                View view = LayoutInflater.from(requireContext()).inflate(R.layout.drawing_drag_character, relativeLayout, false);
                view.setTag(creature);
                ((ImageView)view.findViewById(R.id.imgCharacterDrag)).setImageResource(Utils.getCreatureImageId(creature));
                ((TextView)view.findViewById(R.id.txtNameDrag)).setText(creature.getNombre());
                view.setX(creature.getCanvasX());
                view.setY(creature.getCanvasY());

                view.setOnClickListener(v -> {
                    ((Creature)v.getTag()).setColocadaEnCanvas(false);
                    relativeLayout.removeView(v);
                    callbacks.refreshDraggingList();
                });

                relativeLayout.addView(view);
            }
        }

        viewModel.getStrokeWidth().observe(getViewLifecycleOwner(), strokeWidthObserver);
    }

    @Override
    public void onPause() {
        super.onPause();

        viewModel.setDrawnPaths(myCanvas.getPaths());
    }

    public void undoLastAction()
    {
        myCanvas.undoLast();
    }

    public void selectPencil()
    {
        myCanvas.changeToPencil();
    }

    public void selectEraser()
    {
        myCanvas.changeToEraser();
    }

    public void clearCanvas()
    {
        int nCriaturasEnCanvas = 0;

        //Aside from deleting paintings, remove all views
        for(Creature c : viewModel.getCreatures().getValue())
        {
            if(c.isColocadaEnCanvas())
            {
                nCriaturasEnCanvas++;
                c.setColocadaEnCanvas(false);
            }
        }

        //Iterate only if there are creatures in the canvas
        if(nCriaturasEnCanvas != 0)
        {
            ViewGroup viewGroup = relativeLayout;
            ArrayList<View> viewsABorrar = new ArrayList<>();
            for(int i = 0; i < viewGroup.getChildCount(); i++)
            {
                View child = viewGroup.getChildAt(i);
                if(!child.equals(myCanvas))
                    viewsABorrar.add(child);
            }

            for(View view : viewsABorrar)
                relativeLayout.removeView(view);
        }

        callbacks.refreshDraggingList();
        myCanvas.clearCanvas();
    }

    public void changePencilColor(int color)
    {
        myCanvas.changePencilColor(color);
    }
}
