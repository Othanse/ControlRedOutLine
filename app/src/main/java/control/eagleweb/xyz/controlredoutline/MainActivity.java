package control.eagleweb.xyz.controlredoutline;

import android.hardware.ConsumerIrManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    //获取红外控制类
    private ConsumerIrManager IR;
    //判断是否有红外功能
    boolean IRBack;
    private TextView mContentTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContentTextView = (TextView) findViewById(R.id.tv_content);


        inItEvent();

//        Timer timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
        receiveMsg();
//            }
//        }, 1000, 500);
    }

    //初始化事务
    private void inItEvent() {
        //获取ConsumerIrManager实例
        IR = (ConsumerIrManager) getSystemService(CONSUMER_IR_SERVICE);

        //如果sdk版本大于4.4才进行是否有红外的功能（手机的android版本）
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            IRBack = IR.hasIrEmitter();
            if (!IRBack) {
                showToast("对不起，该设备上没有红外功能!");
            } else {
                showToast("红外设备就绪");//可进行下一步操作
            }
        }
    }

    private void showToast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    /**
     * 发射红外信号
     *
     * @param carrierFrequency 红外传输的频率，一般的遥控板都是38KHz
     * @param pattern          指以微秒为单位的红外开和关的交替时间
     */
    private void sendMsg(int carrierFrequency, int[] pattern) {
        IR.transmit(carrierFrequency, pattern);
        String text = "发送成功：\n频率:" + carrierFrequency + "\n数据：";
        for (int i : pattern) {
            text += i + ",";
        }
//        mContentTextView.setText(text);

    }

    private void receiveMsg() {
        final StringBuilder b = new StringBuilder();

        if (!IR.hasIrEmitter()) {
            showToast("未找到红外接收器4!");
            return;
        }

        // 获得可用的载波频率范围
        ConsumerIrManager.CarrierFrequencyRange[] freqs = IR
                .getCarrierFrequencies();
        b.append("IR Carrier Frequencies:\n");// 红外载波频率
        // 边里获取频率段
        for (ConsumerIrManager.CarrierFrequencyRange range : freqs) {
            b.append(String.format("    %d - %d\n",
                    range.getMinFrequency(), range.getMaxFrequency()));
            b.append(range);
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mContentTextView.setText(b.toString());// 显示结果
            }
        });

    }

    public void toggle(View view) {
        sendMsg(38000, CodeCommand.auto);
    }
}
