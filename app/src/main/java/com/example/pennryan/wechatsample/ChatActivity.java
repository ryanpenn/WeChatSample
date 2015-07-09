package com.example.pennryan.wechatsample;

import android.app.Activity;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pennryan.wechatsample.chat.ChatMsgEntity;
import com.example.pennryan.wechatsample.chat.ChatMsgViewAdapter;
import com.example.pennryan.wechatsample.chat.SoundMeter;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class ChatActivity extends Activity {

    private Button mBtnSend;
    private TextView mBtnRcd;
    private EditText mEditTextContent;
    private RelativeLayout mBottom;
    private ListView mListView;
    private ChatMsgViewAdapter mAdapter;
    private List<ChatMsgEntity> mDataArrays = new ArrayList<ChatMsgEntity>();

    private boolean isShosrt = false;
    private LinearLayout voice_rcd_hint_loading, voice_rcd_hint_rcding,
            voice_rcd_hint_tooshort;
    private ImageView img1, sc_img1;
    private SoundMeter mSensor;
    private View rcChat_popup;
    private LinearLayout del_re;
    private ImageView chatting_mode_btn, volume;
    private boolean btn_vocie = false;
    private int flag = 1;
    private Handler mHandler = new Handler();
    private String voiceName;
    private long startVoiceT, endVoiceT;

    private String[] msgArray = new String[] {
            "有人就有恩怨","有恩怨就有江湖","人就是江湖","你怎么退出？ ","生命中充满了巧合","两条平行线也会有相交的一天。"};
    private String[] dataArray = new String[] { "2012-10-31 18:00",
            "2012-10-31 18:10", "2012-10-31 18:11", "2012-10-31 18:20",
            "2012-10-31 18:30", "2012-10-31 18:35"};
    private final static int COUNT = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle(R.string.title_activity_chat);

        // 启动activity时不自动弹出软键盘
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        initView();
        initData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            Intent intent = NavUtils.getParentActivityIntent(this);
            if (NavUtils.shouldUpRecreateTask(this, intent)) {
                TaskStackBuilder.create(this)
                        .addNextIntentWithParentStack(intent)
                        .startActivities();
            } else {
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                NavUtils.navigateUpTo(this, intent);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        mListView = (ListView) findViewById(R.id.listview);
        mBtnSend = (Button) findViewById(R.id.btn_send);
        mBtnRcd = (TextView) findViewById(R.id.btn_rcd);
        mBtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send();
            }
        });

        mBottom = (RelativeLayout) findViewById(R.id.btn_bottom);
        chatting_mode_btn = (ImageView) findViewById(R.id.ivPopUp);
        volume = (ImageView) findViewById(R.id.volume);
        rcChat_popup = findViewById(R.id.rcChat_popup);
        img1 = (ImageView) findViewById(R.id.img1);
        sc_img1 = (ImageView) findViewById(R.id.sc_img1);
        del_re = (LinearLayout) findViewById(R.id.del_re);
        voice_rcd_hint_rcding = (LinearLayout) findViewById(R.id.voice_rcd_hint_rcding);
        voice_rcd_hint_loading = (LinearLayout) findViewById(R.id.voice_rcd_hint_loading);
        voice_rcd_hint_tooshort = (LinearLayout) findViewById(R.id.voice_rcd_hint_tooshort);
        mSensor = new SoundMeter();
        mEditTextContent = (EditText) findViewById(R.id.et_sendmessage);

        //语音文字切换按钮
        chatting_mode_btn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                if (btn_vocie) {
                    mBtnRcd.setVisibility(View.GONE);
                    mBottom.setVisibility(View.VISIBLE);
                    btn_vocie = false;
                    chatting_mode_btn.setImageResource(R.drawable.chatting_setmode_msg_btn);

                } else {
                    mBtnRcd.setVisibility(View.VISIBLE);
                    mBottom.setVisibility(View.GONE);
                    chatting_mode_btn.setImageResource(R.drawable.chatting_setmode_voice_btn);
                    btn_vocie = true;
                }
            }
        });
        mBtnRcd.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                //按下语音录制按钮时返回false执行父类OnTouch
                return false;
            }
        });
    }


    private void initData() {
        for (int i = 0; i < COUNT; i++) {
            ChatMsgEntity entity = new ChatMsgEntity();
            entity.setDate(dataArray[i]);
            if (i % 2 == 0) {
                entity.setName("白富美");
                entity.setMsgType(true);
            } else {
                entity.setName("高富帅");
                entity.setMsgType(false);
            }

            entity.setText(msgArray[i]);
            mDataArrays.add(entity);
        }

        mAdapter = new ChatMsgViewAdapter(this, mDataArrays);
        mListView.setAdapter(mAdapter);
    }

    private void send() {
        String contString = mEditTextContent.getText().toString();
        if (contString.length() > 0) {
            ChatMsgEntity entity = new ChatMsgEntity();
            entity.setDate(getDate());
            entity.setName("高富帅");
            entity.setMsgType(false);
            entity.setText(contString);

            mDataArrays.add(entity);
            mAdapter.notifyDataSetChanged();
            mEditTextContent.setText("");
            mListView.setSelection(mListView.getCount() - 1);
        }
    }

    private String getDate() {
        Calendar c = Calendar.getInstance();

        String year = String.valueOf(c.get(Calendar.YEAR));
        String month = String.valueOf(c.get(Calendar.MONTH));
        String day = String.valueOf(c.get(Calendar.DAY_OF_MONTH) + 1);
        String hour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
        String mins = String.valueOf(c.get(Calendar.MINUTE));

        StringBuffer sbBuffer = new StringBuffer();
        sbBuffer.append(year + "-" + month + "-" + day + " " + hour + ":"
                + mins);

        return sbBuffer.toString();
    }

    ///////////////////////////////////////////////////////////////

    //按下语音录制按钮时
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (!Environment.getExternalStorageDirectory().exists()) {
            Toast.makeText(this, "No SDCard", Toast.LENGTH_LONG).show();
            return false;
        }

        if (btn_vocie) {
            System.out.println("1");
            int[] location = new int[2];
            mBtnRcd.getLocationInWindow(location); // 获取在当前窗口内的绝对坐标
            int btn_rc_Y = location[1];
            int btn_rc_X = location[0];
            int[] del_location = new int[2];
            del_re.getLocationInWindow(del_location);
            int del_Y = del_location[1];
            int del_x = del_location[0];
            if (event.getAction() == MotionEvent.ACTION_DOWN && flag == 1) {
                if (!Environment.getExternalStorageDirectory().exists()) {
                    Toast.makeText(this, "No SDCard", Toast.LENGTH_LONG).show();
                    return false;
                }
                System.out.println("2");
                if (event.getY() > btn_rc_Y && event.getX() > btn_rc_X) {//判断手势按下的位置是否是语音录制按钮的范围内
                    System.out.println("3");
                    mBtnRcd.setBackgroundResource(R.mipmap.voice_rcd_btn_pressed);
                    rcChat_popup.setVisibility(View.VISIBLE);
                    voice_rcd_hint_loading.setVisibility(View.VISIBLE);
                    voice_rcd_hint_rcding.setVisibility(View.GONE);
                    voice_rcd_hint_tooshort.setVisibility(View.GONE);
                    mHandler.postDelayed(new Runnable() {
                        public void run() {
                            if (!isShosrt) {
                                voice_rcd_hint_loading.setVisibility(View.GONE);
                                voice_rcd_hint_rcding
                                        .setVisibility(View.VISIBLE);
                            }
                        }
                    }, 300);
                    img1.setVisibility(View.VISIBLE);
                    del_re.setVisibility(View.GONE);
                    startVoiceT = SystemClock.currentThreadTimeMillis();
                    voiceName = startVoiceT + ".amr";
                    start(voiceName);
                    flag = 2;
                }
            } else if (event.getAction() == MotionEvent.ACTION_UP && flag == 2) {//松开手势时执行录制完成
                System.out.println("4");
                mBtnRcd.setBackgroundResource(R.mipmap.voice_rcd_btn_nor);
                if (event.getY() >= del_Y
                        && event.getY() <= del_Y + del_re.getHeight()
                        && event.getX() >= del_x
                        && event.getX() <= del_x + del_re.getWidth()) {
                    rcChat_popup.setVisibility(View.GONE);
                    img1.setVisibility(View.VISIBLE);
                    del_re.setVisibility(View.GONE);
                    stop();
                    flag = 1;
                    File file = new File(android.os.Environment.getExternalStorageDirectory()+"/"
                            + voiceName);
                    System.out.println(file.getAbsolutePath());
                    if (file.exists()) {
                        file.delete();
                    }
                } else {

                    voice_rcd_hint_rcding.setVisibility(View.GONE);
                    stop();
                    endVoiceT = SystemClock.currentThreadTimeMillis();
                    flag = 1;
                    int time = (int) ((endVoiceT - startVoiceT) / 1000);
                    if (time < 1) {
                        isShosrt = true;
                        voice_rcd_hint_loading.setVisibility(View.GONE);
                        voice_rcd_hint_rcding.setVisibility(View.GONE);
                        voice_rcd_hint_tooshort.setVisibility(View.VISIBLE);
                        mHandler.postDelayed(new Runnable() {
                            public void run() {
                                voice_rcd_hint_tooshort.setVisibility(View.GONE);
                                rcChat_popup.setVisibility(View.GONE);
                                isShosrt = false;
                            }
                        }, 500);
                        return false;
                    }
                    ChatMsgEntity entity = new ChatMsgEntity();
                    entity.setDate(getDate());
                    entity.setName("高富帅");
                    entity.setMsgType(false);
                    entity.setTime(time+"\"");
                    entity.setText(voiceName);
                    mDataArrays.add(entity);
                    mAdapter.notifyDataSetChanged();
                    mListView.setSelection(mListView.getCount() - 1);
                    rcChat_popup.setVisibility(View.GONE);

                }
            }
            if (event.getY() < btn_rc_Y) {//手势按下的位置不在语音录制按钮的范围内
                System.out.println("5");
                Animation mLitteAnimation = AnimationUtils.loadAnimation(this,
                        R.anim.cancel_rc);
                Animation mBigAnimation = AnimationUtils.loadAnimation(this,
                        R.anim.cancel_rc2);
                img1.setVisibility(View.GONE);
                del_re.setVisibility(View.VISIBLE);
                del_re.setBackgroundResource(R.mipmap.voice_rcd_cancel_bg);
                if (event.getY() >= del_Y
                        && event.getY() <= del_Y + del_re.getHeight()
                        && event.getX() >= del_x
                        && event.getX() <= del_x + del_re.getWidth()) {
                    del_re.setBackgroundResource(R.mipmap.voice_rcd_cancel_bg_focused);
                    sc_img1.startAnimation(mLitteAnimation);
                    sc_img1.startAnimation(mBigAnimation);
                }
            } else {

                img1.setVisibility(View.VISIBLE);
                del_re.setVisibility(View.GONE);
                del_re.setBackgroundResource(0);
            }
        }
        return super.onTouchEvent(event);
    }

    private static final int POLL_INTERVAL = 300;

    private Runnable mSleepTask = new Runnable() {
        public void run() {
            stop();
        }
    };
    private Runnable mPollTask = new Runnable() {
        public void run() {
            double amp = mSensor.getAmplitude();
            updateDisplay(amp);
            mHandler.postDelayed(mPollTask, POLL_INTERVAL);

        }
    };

    private void start(String name) {
        mSensor.start(name);
        mHandler.postDelayed(mPollTask, POLL_INTERVAL);
    }

    private void stop() {
        mHandler.removeCallbacks(mSleepTask);
        mHandler.removeCallbacks(mPollTask);
        mSensor.stop();
        volume.setImageResource(R.mipmap.amp1);
    }

    private void updateDisplay(double signalEMA) {

        switch ((int) signalEMA) {
            case 0:
            case 1:
                volume.setImageResource(R.mipmap.amp1);
                break;
            case 2:
            case 3:
                volume.setImageResource(R.mipmap.amp2);

                break;
            case 4:
            case 5:
                volume.setImageResource(R.mipmap.amp3);
                break;
            case 6:
            case 7:
                volume.setImageResource(R.mipmap.amp4);
                break;
            case 8:
            case 9:
                volume.setImageResource(R.mipmap.amp5);
                break;
            case 10:
            case 11:
                volume.setImageResource(R.mipmap.amp6);
                break;
            default:
                volume.setImageResource(R.mipmap.amp7);
                break;
        }
    }
}
