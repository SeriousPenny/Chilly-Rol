package com.seriouspenny.chillyrol;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Pair;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.seriouspenny.chillyrol.Constants.Constants;
import com.seriouspenny.chillyrol.Entities.Creature;

import java.util.ArrayList;

public class MainViewModel extends ViewModel {
    private MutableLiveData<ArrayList<Creature>> creatures;
    private MutableLiveData<Boolean> performListRefresh; //Used only to observe and refresh the list
    private ArrayList<Pair<Path, Paint>> drawnPaths;
    private int pencilColor;

    //Attributes for drawing
    private MutableLiveData<Float> strokeWidth;

    public MainViewModel()
    {
        creatures = new MutableLiveData<>();
        performListRefresh = new MutableLiveData<>();
        drawnPaths = new ArrayList<>();
        pencilColor = Color.BLACK;

        strokeWidth = new MutableLiveData<>(); strokeWidth.setValue(Constants.MINIMUM_STROKE_WIDTH);

        creatures.setValue(new ArrayList<Creature>());
    }

    public MutableLiveData<ArrayList<Creature>> getCreatures() {
        return creatures;
    }

    public MutableLiveData<Boolean> getPerformListRefresh() {
        return performListRefresh;
    }

    public MutableLiveData<Float> getStrokeWidth() {
        return strokeWidth;
    }

    public ArrayList<Pair<Path, Paint>> getDrawnPaths() {
        return drawnPaths;
    }

    public void setDrawnPaths(ArrayList<Pair<Path, Paint>> drawnPaths) {
        this.drawnPaths = drawnPaths;
    }

    public int getPencilColor() {
        return pencilColor;
    }

    public void setPencilColor(int pencilColor) {
        this.pencilColor = pencilColor;
    }
}
