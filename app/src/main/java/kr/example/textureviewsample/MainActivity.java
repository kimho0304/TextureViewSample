package kr.example.textureviewsample;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback {
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private Handler handler;
    private Button runBtn;
    private Bitmap[] frames; // Array of bitmaps for animation frames
    private int currentFrameIndex = 0;
    private static final int FRAME_DELAY = 0;
    private static final String TAG = "SurfaceTest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handler = new Handler(getMainLooper());

        surfaceView = findViewById(R.id.surface);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);

        runBtn = findViewById(R.id.runBtn);
        runBtn.setOnClickListener(view -> {
            startAnimation();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void drawFrame() {
        Log.i(TAG, "drawFrame() running");
        Canvas canvas = null;
        try {
            canvas = surfaceHolder.lockCanvas();
            if (canvas != null) {
                // Clear the canvas
                canvas.drawColor(Color.BLACK);

                // Draw the current frame
                canvas.drawBitmap(frames[currentFrameIndex], 0, 0, null);

                // Increment frame index for the next frame
                currentFrameIndex = (currentFrameIndex + 1) % frames.length;
            }
        } finally {
            if (canvas != null) {
                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
    }

    private void startAnimation() {
        // Create a runnable to periodically draw frames
        Runnable animationRunnable = new Runnable() {
            @Override
            public void run() {
                if (currentFrameIndex < frames.length)
                    drawFrame();
                // Control the animation frame rate here using a delay
                handler.postDelayed(this, FRAME_DELAY);
            }
        };

        // Start the animation loop
        handler.post(animationRunnable);
    }


    // Implement SurfaceHolder.Callback methods
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // Initialize your bitmaps here (frames)
        frames = new Bitmap[]{
                BitmapFactory.decodeResource(getResources(), R.drawable.test1),
                BitmapFactory.decodeResource(getResources(), R.drawable.test2),
                BitmapFactory.decodeResource(getResources(), R.drawable.test3),
                BitmapFactory.decodeResource(getResources(), R.drawable.test4),
                BitmapFactory.decodeResource(getResources(), R.drawable.test5),
                BitmapFactory.decodeResource(getResources(), R.drawable.test6),
                BitmapFactory.decodeResource(getResources(), R.drawable.test7)
        };
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // Handle surface changes (e.g., size changes)
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // Release resources when the surface is destroyed
        for (Bitmap frame : frames) {
            frame.recycle();
        }
    }
}