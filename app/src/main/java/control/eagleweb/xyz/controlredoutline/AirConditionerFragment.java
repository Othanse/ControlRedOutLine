package control.eagleweb.xyz.controlredoutline;

import android.content.Context;
import android.hardware.ConsumerIrManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import static android.content.Context.CONSUMER_IR_SERVICE;

/**
 * Created by ${高宇} on 17/6/5.
 */
//需要api大于19与下面if判断用途类似
@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class AirConditionerFragment extends Fragment implements View.OnClickListener {
    //获取红外控制类
    private ConsumerIrManager IR;
    //判断是否有红外功能
    boolean IRBack;

    private View view;
    private TextView tempShow, airWindDir, windDirAuto, windSpeed, modeShow;
    private ImageView modeCold, modeWatted, modeAuto, modeSupply, modeHeating;
    //开关、度数、模式、自动手动、风向、风量
    private AirBean airBean = new AirBean(0, 25, 0, 0, 0, 0);


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_air, container, false);
        inItEvent();
        inItUI();
        return view;
    }


    //初始化事务
    private void inItEvent() {
        //获取ConsumerIrManager实例
        IR = (ConsumerIrManager) getActivity().getSystemService(CONSUMER_IR_SERVICE);

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
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
    }

    //初始化UI
    private void inItUI() {

        //按钮设置

        DisplayMetrics dm = getResources().getDisplayMetrics();
        int screenWidth = dm.widthPixels / 4;
        int screenHeight = dm.heightPixels / 10;
        Log.e("gaoyu", "宽高" + screenWidth + screenHeight);

        Button air_power = (Button) view.findViewById(R.id.btn_air_power);
        air_power.setOnClickListener(this);
        air_power.setWidth(screenWidth);
        air_power.setHeight(screenHeight);

        Button air_mode = (Button) view.findViewById(R.id.btn_air_mode);
        air_mode.setOnClickListener(this);
        air_mode.setWidth(screenWidth);
        air_mode.setHeight(screenHeight);


        Button air_tempadd = (Button) view.findViewById(R.id.btn_air_up);
        air_tempadd.setOnClickListener(this);
        air_tempadd.setWidth(screenWidth);
        air_tempadd.setHeight(screenHeight);

        Button air_tempsub = (Button) view.findViewById(R.id.btn_air_down);
        air_tempsub.setOnClickListener(this);
        air_tempsub.setWidth(screenWidth);
        air_tempsub.setHeight(screenHeight);

        Button air_wind_auto_dir = (Button) view.findViewById(R.id.btn_air_auto);
        air_wind_auto_dir.setOnClickListener(this);
        air_wind_auto_dir.setWidth(screenWidth);
        air_wind_auto_dir.setHeight(screenHeight);

        Button air_wind_count = (Button) view.findViewById(R.id.btn_air_count);
        air_wind_count.setOnClickListener(this);
        air_wind_count.setWidth(screenWidth);
        air_wind_count.setHeight(screenHeight);

        Button air_wind_dir = (Button) view.findViewById(R.id.btn_air_direction);
        air_wind_dir.setOnClickListener(this);
        air_wind_dir.setWidth(screenWidth);
        air_wind_dir.setHeight(screenHeight);

        //显示设置
        Context context = getContext();
        tempShow = (TextView) view.findViewById(R.id.temp_show);

        modeShow = (TextView) view.findViewById(R.id.text_mode);
        windSpeed = ((TextView) view.findViewById(R.id.text_wind_speed));
        airWindDir = ((TextView) view.findViewById(R.id.wind_dir));
        windDirAuto = ((TextView) view.findViewById(R.id.wind_dir_auto));

        modeCold = (ImageView) view.findViewById(R.id.image_cold);
        modeWatted = (ImageView) view.findViewById(R.id.image_watted);
        modeAuto = (ImageView) view.findViewById(R.id.image_auto);
        modeSupply = (ImageView) view.findViewById(R.id.image_supply);
        modeHeating = (ImageView) view.findViewById(R.id.image_heating);

    }

    /**
     * 点击处理
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        //五中模式
        int data;
        //关机状态
        if (IRBack == false) {
            showToast("无红外设备！");
            return;
        }
        if (airBean.getmPower() == 0x00 && v.getId() != R.id.btn_air_power) {
            return;
        }

        switch (v.getId()) {

            case R.id.btn_air_mode:
                data = airBean.getmMode();
                data++;
                if (data > 4) {
                    data = 0;
                }
                airBean.setmMode(data);
                SendMsg(airBean);
                break;
            case R.id.btn_air_power:

                if (airBean.getmPower() == 0) {
                    airBean.setmPower(1);
                } else {
                    airBean.setmPower(0);
                }
                //发送消息
                SendMsg(airBean);
                break;
            case R.id.btn_air_up:
                data = airBean.getmTmp();
                data++;
                if (data > 30) {
                    data = 16;
                }
                airBean.setmTmp(data);
                SendMsg(airBean);
                break;
            case R.id.btn_air_down:
                data = airBean.getmTmp();
                data--;
                if (data < 16) {
                    data = 30;
                }
                airBean.setmTmp(data);
                SendMsg(airBean);
                break;
            case R.id.btn_air_auto:
                if (airBean.getmenergy() == 0) {
                    airBean.setmenergy(1);
                } else {
                    airBean.setmenergy(0);
                }
                SendMsg(airBean);
                break;
            case R.id.btn_air_count:
                data = airBean.getmWindCount();
                data++;
                if (data > 3) {
                    data = 0;
                }
                airBean.setmWindCount(data);
                SendMsg(airBean);
                break;
            case R.id.btn_air_direction:
                data = airBean.getmWindDir();
                data++;

                if (data > 3) {
                    data = 0;
                }
                airBean.setmWindDir(data);
                SendMsg(airBean);
                break;
            default:
                break;
        }
        //不论点击了什么 都要更新UI
        updataAirUI(airBean);
    }


    @Override
    public void onStart() {
        super.onStart();
        updataAirUI(airBean);
    }

    /**
     * 更新UI
     *
     * @param airBean_ui
     */
    public void updataAirUI(AirBean airBean_ui) {

        if (airBean_ui.getmPower() == 0x01) {

            if (airBean_ui.getmMode() == 0x00) {

                modeShow.setText(getString(R.string.air_mode_val)
                        + getString(R.string.air_mode_value_1));
                modeCold.setVisibility(View.INVISIBLE);
                modeWatted.setVisibility(View.INVISIBLE);
                modeAuto.setVisibility(View.VISIBLE);
                modeSupply.setVisibility(View.INVISIBLE);
                modeHeating.setVisibility(View.INVISIBLE);
                tempShow.setText(String.valueOf(airBean_ui.getmTmp())
                        + getResources().getString(R.string.degree));
            }
            if (airBean_ui.getmMode() == 0x01) {
                modeCold.setVisibility(View.VISIBLE);
                modeWatted.setVisibility(View.INVISIBLE);
                modeAuto.setVisibility(View.INVISIBLE);
                modeSupply.setVisibility(View.INVISIBLE);
                modeHeating.setVisibility(View.INVISIBLE);
                modeShow.setText(getString(R.string.air_mode_val)
                        + getString(R.string.air_mode_value_2));
                tempShow.setText(String.valueOf(airBean_ui.getmTmp())
                        + getResources().getString(R.string.degree));
            }
            if (airBean_ui.getmMode() == 0x02) {
                modeCold.setVisibility(View.INVISIBLE);
                modeWatted.setVisibility(View.VISIBLE);
                modeAuto.setVisibility(View.INVISIBLE);
                modeSupply.setVisibility(View.INVISIBLE);
                modeHeating.setVisibility(View.INVISIBLE);
                modeShow.setText(getString(R.string.air_mode_val)
                        + getString(R.string.air_mode_value_3));
                tempShow.setText("");
            }
            if (airBean_ui.getmMode() == 0x03) {
                modeCold.setVisibility(View.INVISIBLE);
                modeWatted.setVisibility(View.INVISIBLE);
                modeAuto.setVisibility(View.INVISIBLE);
                modeSupply.setVisibility(View.VISIBLE);
                modeHeating.setVisibility(View.INVISIBLE);
                modeShow.setText(getString(R.string.air_mode_val)
                        + getString(R.string.air_mode_value_4));
                tempShow.setText("");
            }
            if (airBean_ui.getmMode() == 0x04) {
                modeCold.setVisibility(View.INVISIBLE);
                modeWatted.setVisibility(View.INVISIBLE);
                modeAuto.setVisibility(View.INVISIBLE);
                modeSupply.setVisibility(View.INVISIBLE);
                modeHeating.setVisibility(View.VISIBLE);
                modeShow.setText(getString(R.string.air_mode_val)
                        + getString(R.string.air_mode_value_5));
                tempShow.setText(String.valueOf(airBean_ui.getmTmp())
                        + getResources().getString(R.string.degree));
            }
            if (airBean_ui.getmWindCount() == 0x00) {
                windSpeed.setText(getString(R.string.air_wind_val)
                        + getString(R.string.air_wind_count_value_1));
            } else if (airBean_ui.getmWindCount() == 0x01) {
                windSpeed.setText(getString(R.string.air_wind_val)
                        + getString(R.string.air_wind_count_value_2));
            } else if (airBean_ui.getmWindCount() == 0x02) {
                windSpeed.setText(getString(R.string.air_wind_val)
                        + getString(R.string.air_wind_count_value_3));
            } else if (airBean_ui.getmWindCount() == 0x03) {
                windSpeed.setText(getString(R.string.air_wind_val)
                        + getString(R.string.air_wind_count_value_4));
            }

            if (airBean_ui.getmWindDir() == 0x00) {
                airWindDir.setText(getString(R.string.air_wind_dir)
                        + getString(R.string.air_wind_dir_value_1));

            } else if (airBean_ui.getmWindDir() == 0x01) {
                airWindDir.setText(getString(R.string.air_wind_dir)
                        + getString(R.string.air_wind_dir_value_2));

            } else if (airBean_ui.getmWindDir() == 0x02) {
                airWindDir.setText(getString(R.string.air_wind_dir)
                        + getString(R.string.air_wind_dir_value_3));

            }

            if (airBean_ui.getmenergy() == 0x00) {
                windDirAuto.setText(getString(R.string.air_wind_auto_dir));

            } else if (airBean_ui.getmenergy() == 0x01) {
                windDirAuto.setText(getString(R.string.air_wind_auto_energy));
            }

        } else {
            modeCold.setVisibility(View.INVISIBLE);
            modeWatted.setVisibility(View.INVISIBLE);
            modeAuto.setVisibility(View.INVISIBLE);
            modeSupply.setVisibility(View.INVISIBLE);
            modeHeating.setVisibility(View.INVISIBLE);
            tempShow.setText("");
            windSpeed.setText("");
            airWindDir.setText("");
            windDirAuto.setText("");
            modeShow.setText(getString(R.string.air_mode_val)
                    + getString(R.string.air_power_off));

        }
    }

    /**
     * 逻辑处理
     * 发送消息
     *
     * @param airBean_Event
     */
    public void SendMsg(AirBean airBean_Event) {
        Log.e("gaoyu", "要发送的信息" + airBean_Event.toString());

        int mPower = airBean_Event.getmPower(); //开关
        int mTmp = airBean_Event.getmTmp();  //温度
        int mMode = airBean_Event.getmMode();  //模式
        int menergy = airBean_Event.getmenergy();  //节能省电/换气
        int mWindDir = airBean_Event.getmWindDir();   //风向
        int mWindCount = airBean_Event.getmWindCount(); //风量

        int tmWindDir = 0;//二进制方向
        //左右扫风风向判断
        if (mWindDir == 2) {
            tmWindDir = 1;
        } else if (mWindDir == 1) {
            tmWindDir = 0;
        } else {
            tmWindDir = 0;
        }
        //根据所选模式确定检验码
        //校验码 = [(模式 – 1) + (温度 – 16) + 5 +左右扫风+换气+节能]取二进制后四位，再逆序
        //以下为了思路清晰 就不写在一起了
        int check = (mMode - 1) + (mTmp - 16) + 5 + tmWindDir + menergy + menergy;//十进制数字
        String two_chack = Integer.toBinaryString(check);//转换成二进制
        //如果大于四位进行裁剪
        //补零
        switch (two_chack.length()) {
            case 3:
                two_chack = "0" + two_chack;
                break;
            case 2:
                two_chack = "00" + two_chack;
                break;
            case 1:
                two_chack = "000" + two_chack;
                break;
        }
        two_chack = two_chack.substring(two_chack.length() - 4, two_chack.length());//取后四位
        String Cut = new StringBuffer(two_chack).reverse().toString();//倒序
        Log.e("gaoyu", "裁剪之前" + two_chack + "裁剪倒序之后" + Cut);

        //分解字符（承载最后四个逆序字符）
        char[] item = new char[5];
        for (int i = 0; i < Cut.length(); i++) {
            item[i] = Cut.charAt(i);
        }
        //操作大数组
        int base[] = CodeCommand.base;

        //第一步 替换校验码  （分七步）
        //取出数组里的四个数
        int one = Integer.valueOf(String.valueOf(item[0])).intValue();
        int two = Integer.valueOf(String.valueOf(item[1])).intValue();
        int three = Integer.valueOf(String.valueOf(item[2])).intValue();
        int four = Integer.valueOf(String.valueOf(item[3])).intValue();
        //64-67位为校验码 131、132 \ 133、134 \ 135、136 \ 137、138
        //第一个数
        if (one == 1) {
            Log.e("gaoyu", "第一个数是1");
            //将大数组里的130、131位置1
            base[130] = CodeCommand.check_d;
            base[131] = CodeCommand.check_u;

        } else {
            Log.e("gaoyu", "第一个数是0");
            //将大数组里的64位不用变
        }
        //第二个数
        if (two == 1) {
            Log.e("gaoyu", "第二个数是1");
            //将大数组里的132、133位置1
            base[132] = CodeCommand.check_d;
            base[133] = CodeCommand.check_u;

        } else {
            Log.e("gaoyu", "第二个数是0");
            //将大数组里的132、133位不用变
        }
        //第三个数
        if (three == 1) {
            Log.e("gaoyu", "第三个数是1");
            //将大数组里的134、135位置1
            base[134] = CodeCommand.check_d;
            base[135] = CodeCommand.check_u;

        } else {
            Log.e("gaoyu", "第三个数是0");
            //将大数组里的134、135位不用变
        }
        //第四个数
        if (four == 1) {
            Log.e("gaoyu", "第四个数是1");
            //将大数组里的136、137位置1
            base[136] = CodeCommand.check_d;
            base[137] = CodeCommand.check_u;
        } else {
            Log.e("gaoyu", "第四个数是0");
            //将大数组里的136、137位不用变
        }

        //第二步 开关  8/9
        if (mPower == 1) {
            Log.e("gaoyu", "开");
            base[8] = CodeCommand.onedown;
            base[9] = CodeCommand.oneup;
        } else {
            base[8] = CodeCommand.zerodown;
            base[9] = CodeCommand.zeroup;
            Log.e("gaoyu", "关");
        }

        //第三步 温度 16-30度   数组中18、25
        switch (mTmp) {
            case 16:
                //默认十六
                break;
            case 17:
                base[18] = CodeCommand.onedown;
                base[19] = CodeCommand.oneup;
                base[20] = CodeCommand.zerodown;
                base[21] = CodeCommand.zeroup;
                base[22] = CodeCommand.zerodown;
                base[23] = CodeCommand.zeroup;
                base[24] = CodeCommand.zerodown;
                base[25] = CodeCommand.zeroup;
                break;
            case 18:
                base[18] = CodeCommand.zerodown;
                base[19] = CodeCommand.zeroup;
                base[20] = CodeCommand.onedown;
                base[21] = CodeCommand.oneup;
                base[22] = CodeCommand.zerodown;
                base[23] = CodeCommand.zeroup;
                base[24] = CodeCommand.zerodown;
                base[25] = CodeCommand.zeroup;
                break;
            case 19:
                base[18] = CodeCommand.onedown;
                base[19] = CodeCommand.oneup;
                base[20] = CodeCommand.onedown;
                base[21] = CodeCommand.oneup;
                base[22] = CodeCommand.zerodown;
                base[23] = CodeCommand.zeroup;
                base[24] = CodeCommand.zerodown;
                base[25] = CodeCommand.zeroup;
                break;
            case 20:
                base[18] = CodeCommand.zerodown;
                base[19] = CodeCommand.zeroup;
                base[20] = CodeCommand.zerodown;
                base[21] = CodeCommand.zeroup;
                base[22] = CodeCommand.onedown;
                base[23] = CodeCommand.oneup;
                base[24] = CodeCommand.zerodown;
                base[25] = CodeCommand.zeroup;
                break;
            case 21:
                base[18] = CodeCommand.onedown;
                base[19] = CodeCommand.oneup;
                base[20] = CodeCommand.zerodown;
                base[21] = CodeCommand.zeroup;
                base[22] = CodeCommand.onedown;
                base[23] = CodeCommand.oneup;
                base[24] = CodeCommand.zerodown;
                base[25] = CodeCommand.zeroup;
                break;
            case 22:
                base[18] = CodeCommand.zerodown;
                base[19] = CodeCommand.zeroup;
                base[20] = CodeCommand.onedown;
                base[21] = CodeCommand.oneup;
                base[22] = CodeCommand.onedown;
                base[23] = CodeCommand.oneup;
                base[24] = CodeCommand.zerodown;
                base[25] = CodeCommand.zeroup;
                break;
            case 23:
                base[18] = CodeCommand.onedown;
                base[19] = CodeCommand.oneup;
                base[20] = CodeCommand.onedown;
                base[21] = CodeCommand.oneup;
                base[22] = CodeCommand.onedown;
                base[23] = CodeCommand.oneup;
                base[24] = CodeCommand.zerodown;
                base[25] = CodeCommand.zeroup;
                break;
            case 24:
                base[18] = CodeCommand.zerodown;
                base[19] = CodeCommand.zeroup;
                base[20] = CodeCommand.zerodown;
                base[21] = CodeCommand.zeroup;
                base[22] = CodeCommand.zerodown;
                base[23] = CodeCommand.zeroup;
                base[24] = CodeCommand.onedown;
                base[25] = CodeCommand.oneup;
                break;
            case 25:
                base[18] = CodeCommand.onedown;
                base[19] = CodeCommand.oneup;
                base[20] = CodeCommand.zerodown;
                base[21] = CodeCommand.zeroup;
                base[22] = CodeCommand.zerodown;
                base[23] = CodeCommand.zeroup;
                base[24] = CodeCommand.onedown;
                base[25] = CodeCommand.oneup;
                break;
            case 26:
                base[18] = CodeCommand.zerodown;
                base[19] = CodeCommand.zeroup;
                base[20] = CodeCommand.onedown;
                base[21] = CodeCommand.oneup;
                base[22] = CodeCommand.zerodown;
                base[23] = CodeCommand.zeroup;
                base[24] = CodeCommand.onedown;
                base[25] = CodeCommand.oneup;
                break;
            case 27:
                base[18] = CodeCommand.onedown;
                base[19] = CodeCommand.oneup;
                base[20] = CodeCommand.onedown;
                base[21] = CodeCommand.oneup;
                base[22] = CodeCommand.zerodown;
                base[23] = CodeCommand.zeroup;
                base[24] = CodeCommand.onedown;
                base[25] = CodeCommand.oneup;
                break;
            case 28:
                base[18] = CodeCommand.zerodown;
                base[19] = CodeCommand.zeroup;
                base[20] = CodeCommand.zerodown;
                base[21] = CodeCommand.zeroup;
                base[22] = CodeCommand.onedown;
                base[23] = CodeCommand.oneup;
                base[24] = CodeCommand.onedown;
                base[25] = CodeCommand.oneup;
                break;
            case 29:
                base[18] = CodeCommand.onedown;
                base[19] = CodeCommand.oneup;
                base[20] = CodeCommand.zerodown;
                base[21] = CodeCommand.zeroup;
                base[22] = CodeCommand.onedown;
                base[23] = CodeCommand.oneup;
                base[24] = CodeCommand.onedown;
                base[25] = CodeCommand.oneup;
                break;
            case 30:
                base[18] = CodeCommand.zerodown;
                base[19] = CodeCommand.zeroup;
                base[20] = CodeCommand.onedown;
                base[21] = CodeCommand.oneup;
                base[22] = CodeCommand.onedown;
                base[23] = CodeCommand.oneup;
                base[24] = CodeCommand.onedown;
                base[25] = CodeCommand.oneup;
                break;
            default:
                break;
        }

        //第四步  模式  2-7
        switch (mMode) {
            case 0:
                base[2] = CodeCommand.zerodown;
                base[3] = CodeCommand.zeroup;
                base[4] = CodeCommand.zerodown;
                base[5] = CodeCommand.zeroup;
                base[6] = CodeCommand.zerodown;
                base[7] = CodeCommand.zeroup;
                break;
            case 1:
                base[2] = CodeCommand.onedown;
                base[3] = CodeCommand.oneup;
                base[4] = CodeCommand.zerodown;
                base[5] = CodeCommand.zeroup;
                base[6] = CodeCommand.zerodown;
                base[7] = CodeCommand.zeroup;
                break;
            case 2:
                base[2] = CodeCommand.zerodown;
                base[3] = CodeCommand.zeroup;
                base[4] = CodeCommand.onedown;
                base[5] = CodeCommand.oneup;
                base[6] = CodeCommand.zerodown;
                base[7] = CodeCommand.zeroup;
                break;
            case 3:
                base[2] = CodeCommand.onedown;
                base[3] = CodeCommand.oneup;
                base[4] = CodeCommand.onedown;
                base[5] = CodeCommand.oneup;
                base[6] = CodeCommand.zerodown;
                base[7] = CodeCommand.zeroup;
                break;
            case 4:
                base[2] = CodeCommand.zerodown;
                base[3] = CodeCommand.zeroup;
                base[4] = CodeCommand.zerodown;
                base[5] = CodeCommand.zeroup;
                base[6] = CodeCommand.onedown;
                base[7] = CodeCommand.oneup;
                break;
        }
        //第五步 节电、换气 48-51
        if (menergy == 1) {
            Log.e("gaoyu", "开启节电换气");
            base[48] = CodeCommand.onedown;
            base[49] = CodeCommand.oneup;
            base[50] = CodeCommand.onedown;
            base[51] = CodeCommand.oneup;
        } else {
            base[48] = CodeCommand.zerodown;
            base[49] = CodeCommand.zeroup;
            base[50] = CodeCommand.zerodown;
            base[51] = CodeCommand.zeroup;
        }
        //第六步  风向  1、上下 36 数组 74.75   2、左右 40  80.81
        switch (mWindDir) {
            case 0:
                //默认
                break;
            case 1:
                base[74] = CodeCommand.onedown;
                base[75] = CodeCommand.oneup;
                break;
            case 2:
                base[80] = CodeCommand.onedown;
                base[81] = CodeCommand.oneup;
                break;
        }

        //第七步  风量  10-13
        switch (mWindCount) {
            case 0:
                //默认
                break;
            case 1:
                base[10] = CodeCommand.onedown;
                base[11] = CodeCommand.oneup;
                base[12] = CodeCommand.zerodown;
                base[13] = CodeCommand.zeroup;
                break;
            case 2:
                base[10] = CodeCommand.zerodown;
                base[11] = CodeCommand.zeroup;
                base[12] = CodeCommand.onedown;
                base[13] = CodeCommand.oneup;
                break;
            case 3:
                base[10] = CodeCommand.onedown;
                base[11] = CodeCommand.oneup;
                base[12] = CodeCommand.onedown;
                base[13] = CodeCommand.oneup;
                break;
        }

        //最后一步 调取红外进行发送
        String content = null;
        for (int i = 0; i < base.length; i++) {
            content += String.valueOf(base[i]) + ",";
        }
        Log.e("gaoyu", "数组信息是" + content);
        int[] aaa = {9000, 4500,

                550, 550, 550, 550, 550, 550, 550, 550,550, 550, 550, 550, 550, 1660, 550, 550,

                550, 550, 550, 550, 550, 550, 550, 550,550, 550, 550, 550, 550, 550, 550, 550,

                550, 550, 550, 550, 550, 550, 550, 550,550, 550, 550, 550, 550, 550, 550, 550,

                550, 550, 550, 550, 550, 550, 550, 550,550, 1660, 550, 550, 550, 1660, 550, 550,

                550, 550, 550, 1660, 550, 550,

                550, 20000};
        //发送完数据将大数组还原
        sendIrMsg(38000, CodeCommand.base);
//        sendIrMsg(38000, base);
        base = CodeCommand.base;

    }


    /**
     * 发射红外信号
     * 可以查看这个标签的log   ConsumerIr
     *
     * @param carrierFrequency 红外传输的频率，一般的遥控板都是38KHz
     * @param pattern          指以微秒为单位的红外开和关的交替时间
     */
    private void sendIrMsg(int carrierFrequency, int[] pattern) {
        IR.transmit(carrierFrequency, pattern);

        showToast("发送成功");
        String content = null;
        for (int i = 0; i < pattern.length; i++) {
            content += String.valueOf(pattern[i]) + ",";
        }
        Log.e("gaoyu", "数组信息是" + content);
        Log.e("gaoyu", "一共有" + pattern.length);
    }

}