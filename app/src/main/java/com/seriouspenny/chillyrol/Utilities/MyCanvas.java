package com.seriouspenny.chillyrol.Utilities;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

public class MyCanvas extends View {
    float width;
    private ArrayList<Pair<Path, Paint>> paths;
    private Path pathInUse;
    private Paint paintInUse;
    boolean isPencil;
    private int colorPencil;

    public MyCanvas(Context context, float width, ArrayList<Pair<Path, Paint>> paths, int colorPencil) {
        super(context);
        this.width = width;
        this.paths = paths;
        this.isPencil = true;
        this.colorPencil = colorPencil;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for(Pair<Path, Paint> path : paths)
        {
            canvas.drawPath(path.first, path.second);
        }
        if(pathInUse != null && paintInUse != null)
        {
            canvas.drawPath(pathInUse, paintInUse);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float xPos = event.getX();
        float yPos = event.getY();

        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                pathInUse = new Path();

                paintInUse = new Paint();
                paintInUse.setAntiAlias(true);
                paintInUse.setColor(isPencil ? colorPencil : Color.WHITE);
                paintInUse.setStrokeJoin(Paint.Join.ROUND);
                paintInUse.setStyle(Paint.Style.STROKE);
                paintInUse.setStrokeWidth(isPencil ? width : width*2);

                pathInUse.moveTo(xPos, yPos);
                return true;

            case MotionEvent.ACTION_MOVE:
                pathInUse.lineTo(xPos, yPos);
                break;

            case MotionEvent.ACTION_UP:
                paths.add(new Pair<>(pathInUse, paintInUse));
                pathInUse = null;
                paintInUse = null;
                break;

            default:
                return false;
        }
        invalidate();
        return true;
    }

    public void changeToPencil()
    {
        isPencil = true;
    }

    public void changeToEraser()
    {
        isPencil = false;
    }

    public void changeStrokeWidth(float width)
    {
        this.width = width;
    }

    public void undoLast()
    {
        if(!paths.isEmpty())
        {
            paths.remove(paths.size()-1);
            invalidate();
        }
    }

    public void clearCanvas()
    {
        paths.clear();
        invalidate();
    }

    public void changePencilColor(int color)
    {
        this.colorPencil = color;
    }

    public ArrayList<Pair<Path, Paint>> getPaths() {
        return paths;
    }

}
