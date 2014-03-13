package com.example.mapkitpoc28;

import com.mapquest.android.maps.GeoPoint;
import com.mapquest.android.maps.MapView;
import com.mapquest.android.maps.Overlay;
import com.mapquest.android.maps.Projection;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;

public class MyOverLay extends Overlay {
    private GeoPoint gp1;
    private GeoPoint gp2;
    private int mRadius = 6;
    private int mode = 0;

    public MyOverLay(GeoPoint gp1, GeoPoint gp2, int mode) // Constructor
    {
        this.gp1 = gp1;
        this.gp2 = gp2;
        this.mode = mode;
    }

//    public MyOverLay1(GeoPoint startGP, GeoPoint startGP2, int mode2) {
//		// TODO Auto-generated constructor stub
//	}

	public int getMode() {
        return mode;
    }

    @Override
    public boolean draw(Canvas canvas, MapView mapView, boolean shadow,
            long when) {

        Projection projection = mapView.getProjection();
        if (shadow == false) {
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            Point point = new Point();
            projection.toPixels(gp1, point);
            // start
            if (mode == 1) {
                paint.setColor(Color.RED);
                RectF oval = new RectF(point.x - mRadius, point.y - mRadius,
                        point.x + mRadius, point.y + mRadius);

                canvas.drawOval(oval, paint);// start point
            }
            // mode=2&#65306;path
            else if (mode == 2) {
                paint.setColor(Color.BLUE);
                Point point2 = new Point();
                projection.toPixels(gp2, point2);
                paint.setStrokeWidth(5);
                paint.setAlpha(120);
                canvas.drawLine(point.x, point.y, point2.x, point2.y, paint);// mode=2&#65306;path
            }
            /* mode=3&#65306;end */
            else if (mode == 3) {
                /* the last path */

                paint.setColor(Color.RED);
                Point point2 = new Point();
                projection.toPixels(gp2, point2);
                paint.setStrokeWidth(5);
                paint.setAlpha(120);
                canvas.drawLine(point.x, point.y, point2.x, point2.y, paint);
                RectF oval = new RectF(point2.x - mRadius, point2.y - mRadius,
                        point2.x + mRadius, point2.y + mRadius);

                paint.setAlpha(255);
                canvas.drawOval(oval, paint);/* end point */
            }
        }
        return super.draw(canvas, mapView, shadow, when);
    }

}