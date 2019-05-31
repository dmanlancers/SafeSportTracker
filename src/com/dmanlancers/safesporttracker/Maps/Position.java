package com.dmanlancers.safesporttracker.Maps;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.location.Location;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

public class Position extends Overlay {

	Location location;

	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}

	@Override
	public boolean onTap(GeoPoint point, MapView mapView) {
		return false;
	}

	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		Projection projection = mapView.getProjection();

		if (shadow == false) {
			// Get the current location    
			Double latitude = location.getLatitude()*1E6;
			Double longitude = location.getLongitude()*1E6;

			GeoPoint geoPoint; 
			geoPoint = new GeoPoint(latitude.intValue(),longitude.intValue());
			Point point = new Point();
			projection.toPixels(geoPoint, point);

			//GeoPoint gP1 = new GeoPoint(19240000,-99120000);
			//GeoPoint gP2 = new GeoPoint(37423157, -122085008);
			//Point p1 = new Point();
			// Point p2 = new Point();

			new Path();

			Paint paint = new Paint();
			paint.setARGB(250, 255, 0, 0);
			paint.setAntiAlias(true);
			paint.setFakeBoldText(true);
			/*paint.setDither(true);
      paint.setColor(Color.RED);
      paint.setStyle(Paint.Style.FILL_AND_STROKE);
      paint.setStrokeJoin(Paint.Join.ROUND);
      paint.setStrokeCap(Paint.Cap.ROUND);
      paint.setStrokeWidth(2);*/

			//float dist = location.distanceTo(getLocation());
			// Convert the location to screen pixels     
			//if (dist > 0.1) {

			projection.toPixels(geoPoint, point);
			//projection.toPixels(gP2, p2);

			//path.moveTo(p2.x, p2.y);
			//path.lineTo(p1.x,p1.y);

			int markerRadius = 5;
			RectF oval = new RectF(point.x-markerRadius,
					point.y-markerRadius,
					point.x+markerRadius,
					point.y+markerRadius);


			canvas.drawOval(oval, paint);

			//canvas.drawPath(path, paint);



		}

		super.draw(canvas, mapView, shadow);
	}
}