package com.seriouspenny.chillyrol.Adapters;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.seriouspenny.chillyrol.Entities.Ally;
import com.seriouspenny.chillyrol.Entities.Creature;
import com.seriouspenny.chillyrol.Entities.Enemy;
import com.seriouspenny.chillyrol.Entities.Status;
import com.seriouspenny.chillyrol.Listeners.RepeatListener;
import com.seriouspenny.chillyrol.MainViewModel;
import com.seriouspenny.chillyrol.R;
import com.seriouspenny.chillyrol.Utilities.Utils;
import com.woxthebox.draglistview.DragItemAdapter;

import java.util.ArrayList;
import java.util.Arrays;

public class CreaturesListAdapter extends DragItemAdapter<Creature, CreaturesListAdapter.ViewHolder> {

    private int mLayoutId;
    private int mGrabHandleId;
    private boolean mDragOnLongPress;
    private Context context;
    private MainViewModel viewModel;
    private CreaturesListAdapterInterface callbacks;

    public CreaturesListAdapter(ArrayList<Creature> list, int layoutId, int grabHandleId, boolean dragOnLongPress, Context context, MainViewModel mainViewModel) {
        mLayoutId = layoutId;
        mGrabHandleId = grabHandleId;
        mDragOnLongPress = dragOnLongPress;
        this.context = context;
        this.viewModel = mainViewModel;
        callbacks = (CreaturesListAdapterInterface)context;
        setItemList(list);
    }

    public interface CreaturesListAdapterInterface
    {
        void abrirEditar(int position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(mLayoutId, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);

        if(mItemList.get(position) instanceof Ally)
        {
            Ally ally = (Ally)mItemList.get(position);

            holder.health.setText(Integer.toString(ally.getVidaActual()));
            holder.maxHealth.setText(Integer.toString(ally.getVidaMaxima()));

            //If it's an ally, the actual health and max health return to their original positions and become visible again
            holder.slash.setVisibility(View.VISIBLE);
            holder.maxHealth.setVisibility(View.VISIBLE);
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(holder.cLayout);
            constraintSet.connect(R.id.txtHealth,ConstraintSet.END,R.id.txtSlash,ConstraintSet.END,0);
            constraintSet.applyTo(holder.cLayout);
        }
        else
        {
            Enemy enemy = (Enemy)mItemList.get(position);

            //Health changes to become damage received
            holder.health.setText(Integer.toString(enemy.getDanoRecibido()));

            //If it's an enemy, the health is centered below the enemy's image
            holder.slash.setVisibility(View.GONE);
            holder.maxHealth.setVisibility(View.GONE);
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(holder.cLayout);
            constraintSet.connect(R.id.txtHealth,ConstraintSet.END,R.id.guideline20,ConstraintSet.END,0);
            constraintSet.applyTo(holder.cLayout);
        }

        holder.iniciativa.setText(Integer.toString(mItemList.get(position).getIniciativa()));
        holder.name.setText(mItemList.get(position).getNombre());
        holder.pifias.setText(Integer.toString(mItemList.get(position).getPifias()));
        holder.image.setImageResource(Utils.getCreatureImageId(mItemList.get(position)));

        //Load the spinner
        String[] estados = Arrays.toString(Status.values()).replaceAll("^.|.$", "").split(", ");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.spinnertext, estados);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.spinner.setAdapter(adapter);
        holder.spinner.setSelection(mItemList.get(position).getEstado().ordinal());


        holder.addHealth.setOnTouchListener(new RepeatListener(400, 100, 60, v -> {
            if(mItemList.get(position) instanceof Ally)
            {
                Ally ally = (Ally)mItemList.get(position);
                if(ally.getVidaActual() >= ally.getVidaMaxima())
                    Utils.animateError(v);
                else
                {
                    Utils.animateClick(v);
                    ally.cambiarVida(1);

                    holder.health.setText(Integer.toString(ally.getVidaActual()));
                }
            }
            else
            {
                Enemy enemy = (Enemy)mItemList.get(position);

                if(enemy.getDanoRecibido() >= 9999)
                {
                    Utils.animateError(v);
                }
                else
                {
                    Utils.animateClick(v);
                    enemy.cambiarDanoRecibido(1);

                    holder.health.setText(Integer.toString(enemy.getDanoRecibido()));
                }
            }
        }));

        holder.subtractHealth.setOnTouchListener(new RepeatListener(400, 100,60, v -> {

            //If it's an ally
            if(mItemList.get(position) instanceof Ally)
            {
                Ally ally = (Ally)mItemList.get(position);
                if(ally.getVidaActual() > 0) //If his health is above 0, you can lower it
                {
                    Utils.animateClick(v);
                    ally.cambiarVida(-1);
                    holder.health.setText(Integer.toString(ally.getVidaActual()));
                }
                else //Otherwise, don't allow
                    Utils.animateError(v);
            }
            else
            {
                Enemy enemy = (Enemy)mItemList.get(position);
                if(enemy.getDanoRecibido() > 0)
                {
                    Utils.animateClick(v);
                    enemy.cambiarDanoRecibido(-1);
                    holder.health.setText(Integer.toString(enemy.getDanoRecibido()));
                }
                else
                    Utils.animateError(v);
            }

        }));

        holder.addPifia.setOnTouchListener(new RepeatListener(400, 100,60, v -> {
            if(mItemList.get(position).getPifias() >= 99)
            {
                Utils.animateError(v);
                Toast.makeText(context, "¿Cómo has pifiado tanto, "+mItemList.get(position).getNombre()+"? WTF", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Utils.animateClick(v);
                mItemList.get(position).cambiarPifia(1);

                holder.pifias.setText(Integer.toString(mItemList.get(position).getPifias()));
            }
        }));

        holder.subtractPifia.setOnTouchListener(new RepeatListener(400, 100,60, v -> {

            if(mItemList.get(position).getPifias() <= 0)
                Utils.animateError(v);
            else
            {
                Utils.animateClick(v);
                mItemList.get(position).cambiarPifia(-1);

                holder.pifias.setText(Integer.toString(mItemList.get(position).getPifias()));
            }
        }));

        holder.close.setOnClickListener(v -> {
            if(mItemList.size() > 0)
            {
                Utils.animateClick(v);
                mItemList.remove(position);
                notifyDataSetChanged();
            }

        });

        holder.whole.setOnClickListener(v -> callbacks.abrirEditar(position));

        holder.spinner.setOnTouchListener((v, event) -> {
            holder.userInteraction = true;
            return false;
        });

        holder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int positionSpinner, long id) {
                if(holder.userInteraction)
                {
                    viewModel.getCreatures().getValue().get(position).setEstado(Status.values()[positionSpinner]);
                    holder.userInteraction = false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public long getUniqueItemId(int position) {
        return mItemList.get(position).getId();
    }

    class ViewHolder extends DragItemAdapter.ViewHolder {
        ConstraintLayout cLayout;
        TextView health, maxHealth, iniciativa, name, pifias, slash;
        CardView whole;
        ImageView image, close, addHealth, subtractHealth, addPifia, subtractPifia;
        Spinner spinner;
        boolean userInteraction;

        public ViewHolder(final View itemView) {
            super(itemView, mGrabHandleId, mDragOnLongPress);
            whole = itemView.findViewById(R.id.cardViewCharacter);
            cLayout = itemView.findViewById(R.id.char_layout_constraint_layout);
            health = itemView.findViewById(R.id.txtHealth);
            slash = itemView.findViewById(R.id.txtSlash);
            maxHealth = itemView.findViewById(R.id.txtMaxHealth);
            iniciativa = itemView.findViewById(R.id.txtIniciativa);
            name = itemView.findViewById(R.id.txtName);
            addHealth = itemView.findViewById(R.id.addHealth);
            subtractHealth = itemView.findViewById(R.id.substractHealth);
            addPifia = itemView.findViewById(R.id.addPifia);
            subtractPifia = itemView.findViewById(R.id.substractPifia);
            image = itemView.findViewById(R.id.imgCharacter);
            close = itemView.findViewById(R.id.closeCharacter);
            pifias = itemView.findViewById(R.id.txtPifias);
            spinner = itemView.findViewById(R.id.statusSpinner);
            userInteraction = false;
        }
    }
}
