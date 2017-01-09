package com.example.yusuke.opencvsample;

import android.app.Activity;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;

public class MainActivity extends Activity {
    private SurfaceView surfaceView;
    private Camera camera;
    private static final String TAG = "tag";
    OverlayView overlayView;
    int i = 0;
    int CONST = 500;

    int camera_width, camera_height, surfaceview_width, surfaceview_height, input_height = 0;
    int threshold1 = 80;
    int threshold2 = 100;
    int threshold = 100;
    int minLineLength = 400;
    int maxLineGap = 20;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        SurfaceHolder holder = surfaceView.getHolder();
        holder.addCallback(callback);
        overlayView = new OverlayView(this);
        addContentView(overlayView, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));

        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, loaderCallback);
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView tv1 = (TextView) findViewById(R.id.editText);
                TextView tv2 = (TextView) findViewById(R.id.editText2);
                TextView tv3 = (TextView) findViewById(R.id.editText6);
                TextView tv4 = (TextView) findViewById(R.id.editText3);
                TextView tv5 = (TextView) findViewById(R.id.editText4);
                threshold1 = Integer.parseInt(tv1.getText().toString());
                threshold2 = Integer.parseInt(tv2.getText().toString());
                threshold = Integer.parseInt(tv3.getText().toString());
                minLineLength = Integer.parseInt(tv4.getText().toString());
                maxLineGap = Integer.parseInt(tv5.getText().toString());
            }
        });

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (camera != null) {
            camera.autoFocus(null);
        }
        return true;
    }

    private SurfaceHolder.Callback callback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            Log.d(TAG, "surfaceCreated");
            SurfaceView surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
            surfaceview_height = surfaceView.getHeight();
            surfaceview_width = surfaceView.getWidth();
            LinearLayout layout = (LinearLayout) findViewById(R.id.input);
            input_height = layout.getHeight();
            Log.d(TAG, String.format("height:%d,width:%d", surfaceview_height, surfaceview_width));

            camera = Camera.open();
            camera.setPreviewCallback(previewCallback);

            Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
            Camera.getCameraInfo(0, cameraInfo);
            // ディスプレイの向き取得
            int rotation = getWindowManager().getDefaultDisplay().getRotation();
            int degrees = 0;
            switch (rotation) {
                case Surface.ROTATION_0:
                    degrees = 0;
                    break;
                case Surface.ROTATION_90:
                    degrees = 90;
                    break;
                case Surface.ROTATION_180:
                    degrees = 180;
                    break;
                case Surface.ROTATION_270:
                    degrees = 270;
                    break;
            }
            // プレビューの向き計算
            int result;
            if (cameraInfo.facing == cameraInfo.CAMERA_FACING_FRONT) {
                result = (cameraInfo.orientation + degrees) % 360;
                result = (360 - result) % 360; // compensate the mirror
            } else {// back-facing
                result = (cameraInfo.orientation - degrees + 360) % 360;
            }
            // ディスプレイの向き設定
            camera.setDisplayOrientation(result);

            try {
                camera.setPreviewDisplay(holder);
            } catch (Exception ex) {
                Log.d(TAG, ex.getMessage());
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            Log.d(TAG, "surfaceChanged");
            Camera.Size size = camera.getParameters().getPreviewSize();
            MainActivity.this.camera_height = size.height;
            MainActivity.this.camera_width = size.width;
            camera.startPreview();
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            Log.d(TAG, "surfaceDestroyed");
            camera.setPreviewCallback(null);
            camera.stopPreview();
            camera.release();
            camera = null;
        }

    };

    private Camera.PreviewCallback previewCallback;

    {
        previewCallback = new Camera.PreviewCallback() {

            @Override
            public void onPreviewFrame(byte[] bytes, Camera camera) {
                count++;
                if (count == 5) {
count=0;
                    Log.d(TAG, "onPreviewFrame");
                    Mat mYuv = new Mat(camera_height + camera_height / 2, camera_width, CvType.CV_8UC1);
                    mYuv.put(0, 0, bytes);

                    Mat image = new Mat();
                    Imgproc.cvtColor(mYuv, image, Imgproc.COLOR_YUV2GRAY_NV21);
                    Imgproc.Canny(image, image, threshold1, threshold2);

                    Mat image_rotation = new Mat(image.cols(), image.rows(), image.type());
                    Core.flip(image.t(), image_rotation, 1);

                /*
                Imgproc.HoughLinesP(image_rotation, image_rotation, 3, Math.PI / 90, threshold, minLineLength, maxLineGap);
                float[] line = new float[image_rotation.rows() * 4];
                for (int i = 0; i < image_rotationa.rows(); i++) {
                    double[] data = image_rotation.get(i, 0);
                    line[i * 4] = (float) data[0] * surfaceview_width / camera_height;
                    line[i * 4 + 1] = (float) data[1] * surfaceview_height / camera_width + input_height;
                    line[i * 4 + 2] = (float) data[2] * surfaceview_width / camera_height;
                    line[i * 4 + 3] = (float) data[3] * surfaceview_height / camera_width + input_height;
                }

                */
                    Imgproc.HoughLines(image_rotation, image_rotation, 1, Math.PI / 180, threshold);
                    float[] line = new float[image_rotation.rows() * 4];
                    for (int i = 0; i < image_rotation.rows(); i++) {
                        double[] data = image_rotation.get(i, 0);
                        double rho = data[0];
                        double theta = data[1];
                        double cosTheta = Math.cos(theta);
                        double sinTheta = Math.sin(theta);
                        line[i * 4] = (float) (rho / cosTheta) * surfaceview_width / camera_height;
                        line[i * 4 + 1] = 0 + input_height;
                        line[i * 4 + 2] = 0;
                        line[i * 4 + 3] = (float) (rho / sinTheta) * surfaceview_height / camera_width + input_height;
                    }

                    float lines[] = {0, 0, 1080 * (float) surfaceview_width / camera_height, 1920 * (float) surfaceview_height / camera_width};   //TODO:ここ画面の向き変えたらバグる
                    overlayView.setline(line);
                }
            }
        };
    }

    private BaseLoaderCallback loaderCallback = new BaseLoaderCallback() {
        @Override
        public void onManagerConnected(int status) {
            super.onManagerConnected(status);
            switch (status) {
                case BaseLoaderCallback.SUCCESS:
                    break;
                case BaseLoaderCallback.INCOMPATIBLE_MANAGER_VERSION:
                    break;
                case BaseLoaderCallback.INIT_FAILED:
                    break;
                case BaseLoaderCallback.INSTALL_CANCELED:
                    break;
                case BaseLoaderCallback.MARKET_ERROR:
                    break;
                default:
            }
        }
    };

    public Mat Mat_rotation90(Mat source, boolean isClockwise) {
        int cols = source.cols();
        int rows = source.rows();
        Mat rotation = new Mat(cols, rows, source.type());
        Point[] src_point = new Point[3];
        Point[] dst_point = new Point[3];
        if (isClockwise) {
            src_point[0] = new Point(0, 0);
            src_point[1] = new Point(cols, 0);
            src_point[2] = new Point(cols, rows);
            dst_point[0] = new Point(rows, 0);
            dst_point[1] = new Point(rows, cols);
            dst_point[2] = new Point(0, cols);
        } else {
            src_point[0] = new Point(0, 0);
            src_point[1] = new Point(cols, 0);
            src_point[2] = new Point(cols, rows);
            dst_point[0] = new Point(0, cols);
            dst_point[1] = new Point(0, 0);
            dst_point[2] = new Point(rows, 0);
        }
        MatOfPoint2f src_point2f = new MatOfPoint2f();
        MatOfPoint2f dst_point2f = new MatOfPoint2f();
        src_point2f.fromArray(src_point);
        dst_point2f.fromArray(dst_point);
        Mat matrix = Imgproc.getAffineTransform(src_point2f, dst_point2f);
        Imgproc.warpAffine(source, rotation, matrix, rotation.size());
        return rotation;
    }
}