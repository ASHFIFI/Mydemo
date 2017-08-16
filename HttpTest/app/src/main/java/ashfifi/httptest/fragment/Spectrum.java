package ashfifi.httptest.fragment;

import android.Manifest;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.audiofx.Equalizer;
import android.media.audiofx.Visualizer;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import ashfifi.httptest.R;

/**
 * Created by aa on 2017/8/16.
 */

public class Spectrum extends Fragment {
    Activity activity;
    private static final String TAG = "Spectrum";

    private static final float VISUALIZER_HEIGHT_DIP = 160f;

    private MediaPlayer mMediaPlayer;
    private Visualizer mVisualizer;
    private Equalizer mEqualizer;

    private LinearLayout mLinearLayout;
    private VisualizerView mVisualizerView;
    private TextView mStatusTextView;
    private TextView mInfoView;
    View view;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(view==null){
            super.onCreateView(inflater,container,savedInstanceState);
            activity = this.getActivity();
            view = activity.getLayoutInflater().inflate(R.layout.spectrum,container,false);
            mStatusTextView = new TextView(activity);
            //mLinearLayout = new LinearLayout(activity);
            mLinearLayout = (LinearLayout)view.findViewById(R.id.spec);
            mLinearLayout.setOrientation(LinearLayout.VERTICAL);
            mLinearLayout.addView(mStatusTextView);

            // Create the MediaPlayer
            mMediaPlayer = MediaPlayer.create(activity, R.raw.qhc);
            Log.d(TAG, "MediaPlayer audio session ID: " + mMediaPlayer.getAudioSessionId());

            if(ContextCompat.checkSelfPermission(activity, Manifest.permission.RECORD_AUDIO)== PackageManager.PERMISSION_GRANTED){

                setupVisualizerFxAndUI();
                setupEqualizerFxAndUI();


                // Make sure the visualizer is enabled only when you actually want to
                // receive data, and
                // when it makes sense to receive data.
                mVisualizer.setEnabled(true);

                // When the stream ends, we don't need to collect any more data. We
                // don't do this in
                // setupVisualizerFxAndUI because we likely want to have more,
                // non-Visualizer related code
                // in this callback.
                mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
                {
                    public void onCompletion(MediaPlayer mediaPlayer)
                    {
                        mVisualizer.setEnabled(false);
                        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                        activity.setVolumeControlStream(AudioManager.STREAM_SYSTEM);
                        mStatusTextView.setText("音乐播放完毕");
                    }
                });

                activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
                mMediaPlayer.start();
                mStatusTextView.setText("播放音乐中....");

            }else{
                ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.RECORD_AUDIO},9);
            }
        }else {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
        }


        return view;
    }
    private void setupEqualizerFxAndUI()
    {
        // Create the Equalizer object (an AudioEffect subclass) and attach it
        // to our media player,
        // with a default priority (0).
        mEqualizer = new Equalizer(0, mMediaPlayer.getAudioSessionId());
        mEqualizer.setEnabled(true);

        TextView eqTextView = new TextView(activity);
        eqTextView.setText("均衡器:");
        mLinearLayout.addView(eqTextView);

        short bands = mEqualizer.getNumberOfBands();

        final short minEQLevel = mEqualizer.getBandLevelRange()[0];
        final short maxEQLevel = mEqualizer.getBandLevelRange()[1];

        for (short i = 0; i < bands; i++)
        {
            final short band = i;

            TextView freqTextView = new TextView(activity);
            freqTextView.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.FILL_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            freqTextView.setGravity(Gravity.CENTER_HORIZONTAL);
            freqTextView.setText((mEqualizer.getCenterFreq(band) / 1000) + " Hz");
            mLinearLayout.addView(freqTextView);

            LinearLayout row = new LinearLayout(activity);
            row.setOrientation(LinearLayout.HORIZONTAL);

            TextView minDbTextView = new TextView(activity);
            minDbTextView.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            minDbTextView.setText((minEQLevel / 100) + " dB");

            TextView maxDbTextView = new TextView(activity);
            maxDbTextView.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            maxDbTextView.setText((maxEQLevel / 100) + " dB");

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.FILL_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.weight = 1;
            SeekBar bar = new SeekBar(activity);
            bar.setLayoutParams(layoutParams);
            bar.setMax(maxEQLevel - minEQLevel);
            bar.setProgress(mEqualizer.getBandLevel(band));

            bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
            {
                public void onProgressChanged(SeekBar seekBar, int progress,
                                              boolean fromUser)
                {
                    mEqualizer.setBandLevel(band, (short) (progress + minEQLevel));
                }

                public void onStartTrackingTouch(SeekBar seekBar)
                {
                }

                public void onStopTrackingTouch(SeekBar seekBar)
                {
                }
            });

            row.addView(minDbTextView);
            row.addView(bar);
            row.addView(maxDbTextView);

            mLinearLayout.addView(row);
        }
    }

    private void setupVisualizerFxAndUI()
    {
        mVisualizerView = new VisualizerView(activity);
        mVisualizerView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT,
                (int) (VISUALIZER_HEIGHT_DIP * getResources()
                        .getDisplayMetrics().density)));
        mLinearLayout.addView(mVisualizerView);

        mInfoView = new TextView(activity);
        String infoStr = "";

        int[] csr = Visualizer.getCaptureSizeRange();
        if(csr != null)
        {
            String csrStr = "CaptureSizeRange: ";
            for(int i = 0; i < csr.length; i ++)
            {
                csrStr += csr[i];
                csrStr +=" ";
            }
            infoStr += csrStr;
        }

        final int maxCR = Visualizer.getMaxCaptureRate();

        infoStr = infoStr + "\nMaxCaptureRate: " + maxCR;

        mInfoView.setText(infoStr);
        mLinearLayout.addView(mInfoView);

        mVisualizer = new Visualizer(mMediaPlayer.getAudioSessionId());
        mVisualizer.setCaptureSize(256);
        mVisualizer.setDataCaptureListener(
                new Visualizer.OnDataCaptureListener()
                {
                    public void onWaveFormDataCapture(Visualizer visualizer,byte[] bytes, int samplingRate)
                    {
                        mVisualizerView.updateVisualizer(bytes);
                    }

                    public void onFftDataCapture(Visualizer visualizer,byte[] fft, int samplingRate)
                    {
                        mVisualizerView.updateVisualizer(fft);
                    }
                }, maxCR / 2, false, true);
    }

    @Override
    public void onPause()
    {
        super.onPause();

        if (activity.isFinishing() && mMediaPlayer != null)
        {
            mVisualizer.release();
            mEqualizer.release();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }


    /**
     * A simple class that draws waveform data received from a
     * {@link Visualizer.OnDataCaptureListener#onWaveFormDataCapture }
     */
    class VisualizerView extends View
    {
        private byte[] mBytes;
        private float[] mPoints;
        private Rect mRect = new Rect();

        private Paint mForePaint = new Paint();
        private int mSpectrumNum = 48;
        private boolean mFirst = true;

        public VisualizerView(Context context)
        {
            super(context);
            init();
        }

        private void init()
        {
            mBytes = null;

            mForePaint.setStrokeWidth(8f);
            mForePaint.setAntiAlias(true);
            mForePaint.setColor(Color.rgb(0, 128, 255));
        }

        public void updateVisualizer(byte[] fft)
        {
            if(mFirst )
            {
                mInfoView.setText(mInfoView.getText().toString() + "\nCaptureSize: " + fft.length);
                mFirst = false;
            }


            byte[] model = new byte[fft.length / 2 + 1];

            model[0] = (byte) Math.abs(fft[0]);
            for (int i = 2, j = 1; j < mSpectrumNum;)
            {
                model[j] = (byte) Math.hypot(fft[i], fft[i + 1]);
                i += 2;
                j++;
            }
            mBytes = model;
            invalidate();
        }

        @Override
        protected void onDraw(Canvas canvas)
        {
            super.onDraw(canvas);

            if (mBytes == null)
            {
                return;
            }

            if (mPoints == null || mPoints.length < mBytes.length * 4)
            {
                mPoints = new float[mBytes.length * 4];
            }

            mRect.set(0, 0, getWidth(), getHeight());

            //绘制波形
            // for (int i = 0; i < mBytes.length - 1; i++) {
            // mPoints[i * 4] = mRect.width() * i / (mBytes.length - 1);
            // mPoints[i * 4 + 1] = mRect.height() / 2
            // + ((byte) (mBytes[i] + 128)) * (mRect.height() / 2) / 128;
            // mPoints[i * 4 + 2] = mRect.width() * (i + 1) / (mBytes.length - 1);
            // mPoints[i * 4 + 3] = mRect.height() / 2
            // + ((byte) (mBytes[i + 1] + 128)) * (mRect.height() / 2) / 128;
            // }

            //绘制频谱
            final int baseX = mRect.width()/mSpectrumNum;
            final int height = mRect.height();

            for (int i = 0; i < mSpectrumNum ; i++)
            {
                if (mBytes[i] < 0)
                {
                    mBytes[i] = 127;
                }

                final int xi = baseX*i + baseX/2;

                mPoints[i * 4] = xi;
                mPoints[i * 4 + 1] = height;

                mPoints[i * 4 + 2] = xi;
                mPoints[i * 4 + 3] = height - mBytes[i];
            }

            canvas.drawLines(mPoints, mForePaint);
        }
    }

    @Override
    public void onRequestPermissionsResult(int RequestCode,String[] Permissions,int[] Grantresult){
        if(RequestCode == 9){
            if(Grantresult[0]==PackageManager.PERMISSION_GRANTED){
                setupVisualizerFxAndUI();
                setupEqualizerFxAndUI();

                // Make sure the visualizer is enabled only when you actually want to
                // receive data, and
                // when it makes sense to receive data.
                mVisualizer.setEnabled(true);

                // When the stream ends, we don't need to collect any more data. We
                // don't do this in
                // setupVisualizerFxAndUI because we likely want to have more,
                // non-Visualizer related code
                // in this callback.
                mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
                {
                    public void onCompletion(MediaPlayer mediaPlayer)
                    {
                        mVisualizer.setEnabled(false);
                        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                        activity.setVolumeControlStream(AudioManager.STREAM_SYSTEM);
                        mStatusTextView.setText("音乐播放完毕");
                    }
                });

                activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
                mMediaPlayer.start();
                mStatusTextView.setText("播放音乐中....");
            }else{
                Toast.makeText(getActivity(),"没有权限",Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(RequestCode,Permissions,Grantresult);
    }
}