package de.lmu.msp.trimmdich.run;

import de.lmu.msp.trimmdich.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class CompassView extends ImageView {
	public CompassView(Context context) {
		super(context);
	}

	float rotation = 0;

	public void setRotation(float rotation) {
		this.rotation = rotation % 360;
	}

	public CompassView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public CompassView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		int drawable = R.drawable.navigation_big;

		// Decode the drawable into a bitmap
		Bitmap bitmapOrg = BitmapFactory.decodeResource(getResources(),
				drawable);

		// Get the width/height of the drawable
		// DisplayMetrics dm = new DisplayMetrics();
		// getWindowManager().getDefaultDisplay().getMetrics(dm);
		//

		int width = bitmapOrg.getWidth(), height = bitmapOrg.getHeight();

		// Initialize a new Matrix
		Matrix matrix = new Matrix();

		// Actually rotate the image
		matrix.postRotate(rotation, width, height);

		// recreate the new Bitmap via a couple conditions
		Bitmap rotatedBitmap = Bitmap.createBitmap(bitmapOrg, 0, 0, width,
				height, matrix, true);
		// BitmapDrawable bmd = new BitmapDrawable( rotatedBitmap );

		// imageView.setImageBitmap( rotatedBitmap );
		setImageDrawable(new BitmapDrawable(getResources(), rotatedBitmap));
		setScaleType(ScaleType.CENTER);

		super.onDraw(canvas);
	}

}
