package com.seriouspenny.chillyrol.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.seriouspenny.chillyrol.Entities.Ally;
import com.seriouspenny.chillyrol.Entities.Creature;
import com.seriouspenny.chillyrol.Entities.Enemy;
import com.seriouspenny.chillyrol.Entities.Status;
import com.seriouspenny.chillyrol.MainViewModel;
import com.seriouspenny.chillyrol.R;

import java.util.Arrays;

public class EditCharacterDialogFragment extends DialogFragment {
    private View view;
    private MainViewModel viewModel;
    private int position;

    public EditCharacterDialogFragment(int position)
    {
        this.position = position;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        final View view = inflater.inflate(R.layout.create_character_popup, null);
        this.view = view;

        builder.setView(view)
                // Add action buttons
                .setPositiveButton("Editar", (dialog, id) -> {

                })
                .setNegativeButton("Cancelar", (dialog, id) -> {
                    dialog.cancel();
                })
                .setCancelable(false);

        //Load viewmodel
        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        //Change header
        ((TextView)view.findViewById(R.id.createTitle)).setText("EDITAR");

        //Create spinner
        Spinner spinner = view.findViewById(R.id.createEstado);
        String[] estados = Arrays.toString(Status.values()).replaceAll("^.|.$", "").split(", ");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, estados);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        //Give values we know
        Creature c = viewModel.getCreatures().getValue().get(position);
        ((EditText)view.findViewById(R.id.createNombre)).setText(c.getNombre());
        ((EditText)view.findViewById(R.id.createIniciativa)).setText(Integer.toString(c.getIniciativa()));
        ((Spinner)view.findViewById(R.id.createEstado)).setSelection(c.getEstado().ordinal());

        //Hide the copies selector, because we're editing
        view.findViewById(R.id.createCopiasLinearLayout).setVisibility(View.GONE);

        if(c instanceof Ally)
        {
            Ally ally = (Ally)c;
            ((EditText)view.findViewById(R.id.createVidaMaxima)).setText(Integer.toString(ally.getVidaMaxima()));
            ((EditText)view.findViewById(R.id.createVidaActual)).setText(Integer.toString(ally.getVidaActual()));
            ((EditText)view.findViewById(R.id.createDanoRecibido)).setText("0");
            ((CheckBox)view.findViewById(R.id.createEsJugador)).setChecked(true);

            //Views visibilities
            view.findViewById(R.id.create_character_popup_dano_recibido).setVisibility(View.GONE);
            view.findViewById(R.id.create_character_popup_vida_maxima).setVisibility(View.VISIBLE);
            view.findViewById(R.id.create_character_popup_vida_actual).setVisibility(View.VISIBLE);
        }
        else
        {
            Enemy enemy = (Enemy)c;
            ((EditText)view.findViewById(R.id.createVidaMaxima)).setText("");
            ((EditText)view.findViewById(R.id.createVidaActual)).setText("");
            ((EditText)view.findViewById(R.id.createDanoRecibido)).setText(Integer.toString(enemy.getDanoRecibido()));
            ((CheckBox)view.findViewById(R.id.createEsJugador)).setChecked(false);

            //Views visibilities
            view.findViewById(R.id.create_character_popup_dano_recibido).setVisibility(View.VISIBLE);
            view.findViewById(R.id.create_character_popup_vida_maxima).setVisibility(View.GONE);
            view.findViewById(R.id.create_character_popup_vida_actual).setVisibility(View.GONE);
        }

        ((CheckBox)view.findViewById(R.id.createEsJugador)).setOnCheckedChangeListener((buttonView, isChecked) -> {
            //If it's a player, hide the copies button. If not, show it.
            if(isChecked)
            {
                view.findViewById(R.id.create_character_popup_dano_recibido).setVisibility(View.GONE);
                view.findViewById(R.id.create_character_popup_vida_actual).setVisibility(View.VISIBLE);
                view.findViewById(R.id.create_character_popup_vida_maxima).setVisibility(View.VISIBLE);
                ((EditText)view.findViewById(R.id.createCopias)).setText("1");
            }
            else
            {
                view.findViewById(R.id.create_character_popup_dano_recibido).setVisibility(View.VISIBLE);
                view.findViewById(R.id.create_character_popup_vida_actual).setVisibility(View.GONE);
                view.findViewById(R.id.create_character_popup_vida_maxima).setVisibility(View.GONE);
            }
        });

        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        AlertDialog d = (AlertDialog)getDialog();
        if(d != null)
        {
            d.getButton(Dialog.BUTTON_POSITIVE).setOnClickListener(v -> {
                EditText eNombre, eIniciativa, eVidaActual, eVidaMaxima, eDanoRecibido;
                CheckBox cEsJugador;
                Spinner sEstado;
                String nombre;
                int iniciativa, vidaActual, vidaMaxima;
                boolean okay = true, esJugador;
                Status estado;

                eNombre = view.findViewById(R.id.createNombre);
                eIniciativa = view.findViewById(R.id.createIniciativa);
                eVidaMaxima = view.findViewById(R.id.createVidaMaxima);
                eVidaActual = view.findViewById(R.id.createVidaActual);
                cEsJugador = view.findViewById(R.id.createEsJugador);
                eDanoRecibido = view.findViewById(R.id.createDanoRecibido);
                sEstado = view.findViewById(R.id.createEstado);

                nombre = eNombre.getText().toString();
                esJugador = cEsJugador.isChecked();
                iniciativa = eIniciativa.getText().toString().equals("") ? 0 : Integer.valueOf(eIniciativa.getText().toString());
                vidaMaxima = eVidaMaxima.getText().toString().equals("") ? 0 : Integer.valueOf(eVidaMaxima.getText().toString());
                vidaActual = esJugador ? (eVidaActual.getText().toString().equals("") ? 0 : Integer.valueOf(eVidaActual.getText().toString())) : (eDanoRecibido.getText().toString().equals("") ? 0 : Integer.valueOf(eDanoRecibido.getText().toString()));
                estado = Status.valueOf((String)sEstado.getSelectedItem());

                //Check data
                if(nombre.isEmpty())
                {
                    eNombre.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
                    Toast.makeText(getContext(), "El nombre no puede estar vacío", Toast.LENGTH_SHORT).show();
                    okay = false;
                }
                else
                    eNombre.getBackground().clearColorFilter();

                if(iniciativa < 0)
                {
                    eIniciativa.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
                    Toast.makeText(getContext(), "La iniciativa no puede ser menor a 0", Toast.LENGTH_SHORT).show();
                    okay = false;
                }
                else
                    eIniciativa.getBackground().clearColorFilter();

                if(esJugador)
                {
                    if(vidaMaxima <= 0)
                    {
                        eVidaMaxima.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
                        Toast.makeText(getContext(), "La vida máxima no puede ser menor o igual a 0", Toast.LENGTH_SHORT).show();
                        okay = false;
                    }
                    else
                        eVidaMaxima.getBackground().clearColorFilter();

                    if(vidaActual < 0)
                    {
                        eVidaActual.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
                        Toast.makeText(getContext(), "La vida actual no puede ser menor o igual a 0", Toast.LENGTH_SHORT).show();
                        okay = false;
                    }
                    else if(vidaActual > vidaMaxima)
                    {
                        eVidaActual.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
                        Toast.makeText(getContext(), "La vida actual no puede ser mayor a la vida máxima", Toast.LENGTH_SHORT).show();
                        okay = false;
                    }
                    else
                        eVidaActual.getBackground().clearColorFilter();
                }
                else
                {
                    //Remember that actual health is damage received for monsters
                    if(vidaActual < 0)
                    {
                        eDanoRecibido.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
                        Toast.makeText(getContext(), "El daño recibido no puede ser menor a 0", Toast.LENGTH_SHORT).show();
                        okay = false;
                    }
                    else
                        eDanoRecibido.getBackground().clearColorFilter();
                }

                if(okay)
                {
                    Creature creature = viewModel.getCreatures().getValue().get(position);

                    //Now it depends on whether it's changed from Ally to Enemy, from Enemy to Ally, or if data changed from Enemy or Ally
                    if(creature instanceof Ally)
                    {
                        Ally ally = (Ally)creature;

                        //If it's still an Ally (didn't change to Enemy)
                        if(esJugador)
                        {
                            ally.setNombre(nombre);
                            ally.setVidaActual(vidaActual);
                            ally.setVidaMaxima(vidaMaxima);
                            ally.setIniciativa(iniciativa);
                            ally.setEstado(estado);
                        }
                        else //If it changed to be an Enemy
                            viewModel.getCreatures().getValue().set(position, new Enemy(nombre, vidaActual, iniciativa, estado, ally.getPifias()));
                    }
                    else
                    {
                        Enemy enemy = (Enemy)creature;

                        //If it's still an Enemy (didn't change to Ally)
                        if(!esJugador)
                        {
                            enemy.setNombre(nombre);
                            enemy.setDanoRecibido(vidaActual);
                            enemy.setIniciativa(iniciativa);
                            enemy.setEstado(estado);
                        }
                        else //If it's changed to be an Ally
                            viewModel.getCreatures().getValue().set(position, new Ally(nombre, vidaActual, vidaMaxima, iniciativa, estado, enemy.getPifias()));
                    }

                    //Refresh list after finishing updating data
                    viewModel.getPerformListRefresh().setValue(true);
                    dismiss();
                }
            });
        }
    }
}
