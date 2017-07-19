package com.minardwu.see.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import com.minardwu.see.R;
import com.minardwu.see.base.Config;
import com.minardwu.see.util.CameraUtil;
;

import java.io.IOException;
import java.util.List;

public class CameraActivity extends Activity implements SurfaceHolder.Callback, OnClickListener {

	SurfaceView surfaceView = null;
	SurfaceHolder mSurfaceHolder;
	ImageButton btn_shutter;
	ImageButton btn_save;
	ImageButton btn_delete;

	private float previewRate = -1f;
	private boolean isPreviewing = false;

	private Camera camera;
	private Camera.Parameters parameters;

	private byte[] byteArray;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera);

		DisplayMetrics dm = getResources().getDisplayMetrics();
		float w_screen = dm.widthPixels;
		float h_screen = dm.heightPixels;
		float W = w_screen;
		float H = h_screen;
		previewRate = H/W;

		surfaceView = (SurfaceView) findViewById(R.id.camera_surfaceview);
		btn_shutter = (ImageButton)findViewById(R.id.btn_shutter);
		btn_save = (ImageButton)findViewById(R.id.btn_save);
		btn_delete = (ImageButton)findViewById(R.id.btn_delete);

		mSurfaceHolder = surfaceView.getHolder();
		mSurfaceHolder.setFormat(PixelFormat.TRANSPARENT);
		mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		mSurfaceHolder.addCallback(this);

		btn_shutter.setEnabled(true);
		btn_shutter.setOnClickListener(this);
		btn_delete.setOnClickListener(this);
		btn_save.setOnClickListener(this);
	}

	@Override
	public void surfaceCreated(SurfaceHolder surfaceHolder) {
		camera = Camera.open();
		if(isPreviewing){
			camera.stopPreview();
			return;
		}
		if(camera != null){
			parameters = camera.getParameters();
			parameters.setPictureFormat(PixelFormat.JPEG);
			Camera.Size pictureSize = CameraUtil.getInstance().getPropPictureSize(parameters.getSupportedPictureSizes(),previewRate, 800);
			parameters.setPictureSize(pictureSize.width, pictureSize.height);
			Camera.Size previewSize = CameraUtil.getInstance().getPropPreviewSize(parameters.getSupportedPreviewSizes(), previewRate, 800);
			parameters.setPreviewSize(previewSize.width, previewSize.height);
			List<String> focusModes = parameters.getSupportedFocusModes();
			if(focusModes.contains("continuous-video")){
				parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
			}
			camera.setDisplayOrientation(90);
			camera.setParameters(parameters);

			try {
				camera.setPreviewDisplay(mSurfaceHolder);
				camera.startPreview();
			} catch (IOException e) {
				e.printStackTrace();
			}
			isPreviewing = true;
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
		if (camera != null) {
			camera.stopPreview();
			camera.release();
			isPreviewing = false;
		}
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()){
			case R.id.btn_shutter:
				if(isPreviewing && (camera != null)){
					btn_shutter.setEnabled(false);
					camera.takePicture(null, null, new Camera.PictureCallback() {
						@Override
						public void onPictureTaken(byte[] bytes, Camera camera) {
							if(bytes != null){
								byteArray = bytes;
								CameraActivity.this.camera.stopPreview();
								isPreviewing = false;
								btn_shutter.setVisibility(View.GONE);
								btn_save.setVisibility(View.VISIBLE);
								btn_delete.setVisibility(View.VISIBLE);
							}
						}
					});
				}
				break;
			case R.id.btn_save:
				Intent intent = new Intent(CameraActivity.this, PostPhotoActivity.class);
				intent.putExtra("from", "camera");
				intent.putExtra("bytes", byteArray);
				startActivity(intent);
				finish();
				break;
			case R.id.btn_delete:
				btn_shutter.setVisibility(View.VISIBLE);
				btn_save.setVisibility(View.GONE);
				btn_delete.setVisibility(View.GONE);
				CameraActivity.this.camera.startPreview();
				isPreviewing = true;
				break;
		}
	}
}
