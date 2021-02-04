package com.seriouspenny.chillyrol.Fragments;


import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.seriouspenny.chillyrol.Adapters.CreaturesListAdapter;
import com.seriouspenny.chillyrol.MainViewModel;
import com.seriouspenny.chillyrol.R;
import com.woxthebox.draglistview.DragListView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListFragment extends Fragment {

    private MainViewModel viewModel;

    public ListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        ImageView cv = requireActivity().findViewById(R.id.btnAddCreature);
        DragListView listView = requireActivity().findViewById(R.id.dragList);

        cv.setOnLongClickListener(v -> {
            AlertDialog alertDialog = new AlertDialog.Builder(requireContext()).create();
            alertDialog.setTitle("¿Borrar todas las criaturas?");

            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    viewModel.getCreatures().getValue().clear();
                    viewModel.getPerformListRefresh().setValue(true);
                }
            });

            alertDialog.setCancelable(true);

            alertDialog.show();

            return true;
        });

        listView.setLayoutManager(new LinearLayoutManager(requireContext()));
        CreaturesListAdapter listAdapter = new CreaturesListAdapter(viewModel.getCreatures().getValue(), R.layout.character_layout, R.id.cardViewCharacter, true, requireContext(), viewModel);
        listView.setAdapter(listAdapter, false);
        listView.setCanDragHorizontally(false);
        listView.setCustomDragItem(null);

        //Observer para refrescar la lista
        final Observer<Boolean> refreshList = bool -> ((DragListView)requireActivity().findViewById(R.id.dragList)).getAdapter().notifyDataSetChanged();

        viewModel.getPerformListRefresh().observe(getViewLifecycleOwner(), refreshList);
    }

    public void abrirEditar(int position)
    {
        DialogFragment popup = new EditCharacterDialogFragment(position);
        popup.show(((AppCompatActivity)requireContext()).getSupportFragmentManager(), "popupEdit");
    }
}
