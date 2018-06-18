package control.eagleweb.xyz.controlredoutline;

/**
 * Created by ${高宇} on 17/6/3.
 * <p>
 * <p>
 * 下面是空调遥控器编码
 * 格力空调遥控器（YB0F2）红外码组成如下，按解码顺序排列
 * 起始码（S）+35位数据码+连接码（C）+32位数据码
 * 各种编码的电平宽度：
 * 数据码由“0”“1”组成：
 * 0的电平宽度为：600us低电平+600us高电平，
 * 1的电平宽度为：600us低电平+1600us高电平
 * 起始码S电平宽度为：9000us低电平+4500us高电平
 * 连接码C电平宽度为：600us低电平+20000us高电平
 * <p>
 * 校验码的形成机制如下：
 * 校验码 = [(模式 – 1) + (温度 – 16) + 5 +左右扫风+换气+节能]取二进制后四位，再逆序；
 * <p>
 * 例如：如果需要设置一下的状态，模式4，30℃，左右扫风，换气关闭，节能关闭，那么校验码为：
 * (4 – 1)+(30-16)+5+1+0+0= 23,二进制为10111，取低四位为0111，逆序后为1110
 * <p>
 * 为了方便编码，在编码时可以正序，解码端再逆序，解码的时候先解码低字节，再解码高字节的位。
 * 另外定时数据对最后的校验码的影响没有测试，因为很少会用到这个功能。
 */

public class CodeCommand {
    /**
     * 9000, 4500,

     550, 550, 550, 550, 550, 550, 550, 550,550, 550, 550, 550, 550, 1660, 550, 550,

     550, 550, 550, 550, 550, 550, 550, 550,550, 550, 550, 550, 550, 550, 550, 550,

     550, 550, 550, 550, 550, 550, 550, 550,550, 550, 550, 550, 550, 550, 550, 550,

     550, 550, 550, 550, 550, 550, 550, 550,550, 1660, 550, 550, 550, 1660, 550, 550,

     550, 550, 550, 1660, 550, 550,

     550, 20000
     *
     * 老式空调
     * 由此可知，格力空调红外码（短码）是这样构成的：起始码+32位数据码+010+结束码

     起始码：高电平9000us+低电平4500us

     数值0：高电平550us+低电平550us

     数值1：高电平550us+低电平1660us

     结束码：高电平5500us+低电平20000us
     *
     * */
    //编码规则
    //起始码S电平宽度为：9000us低电平+4500us高电平
    public static final int startdown = 9000;
    public static final int startup = 4500;

    //连接码C电平宽度为：600us低电平+20000us高电平
    public static final int connectdown = 600;
    public static final int connectup = 20000;

    //数据码由0，1组成：
    //0的电平宽度为：600us低电平+600us高电平，
    public static final int zerodown = 600;
    public static final int zeroup = 600;

    //1的电平宽度为：600us低电平+1600us高电平
    public static final int onedown = 600;
    public static final int oneup = 1600;

    //命令格式（数组内的数值拼接）


    //模式自动数组（扫风）开机
    public static final int[] auto = {
            startdown, startup,//起始码
            zerodown, zeroup, zerodown, zeroup, zerodown, zeroup,//1-3
            onedown, oneup, zerodown, zeroup, zerodown, zeroup,//4-6
            onedown, oneup, zerodown, zeroup, zerodown, zeroup,//7-9
            zerodown, zeroup, zerodown, zeroup, zerodown, zeroup,//10-12
            zerodown, zeroup, zerodown, zeroup, zerodown, zeroup,//13-15
            zerodown, zeroup, zerodown, zeroup, zerodown, zeroup,//16-18
            zerodown, zeroup, zerodown, zeroup, zerodown, zeroup,//19-21
            zerodown, zeroup, zerodown, zeroup, zerodown, zeroup,//22-24
            zerodown, zeroup, zerodown, zeroup, zerodown, zeroup,//25-27
            zerodown, zeroup, onedown, oneup, zerodown, zeroup,//28-30
            onedown, oneup, zerodown, zeroup, zerodown, zeroup,//31-33
            onedown, oneup, zerodown, zeroup,//34-35  前35位数据码结束
            connectdown, connectup,//连接码   后32位开始
            zerodown, zeroup, zerodown, zeroup, zerodown, zeroup,//36-38
            zerodown, zeroup, zerodown, zeroup, zerodown, zeroup,//39-41
            zerodown, zeroup, zerodown, zeroup, onedown, oneup,//42-44
            zerodown, zeroup, zerodown, zeroup, zerodown, zeroup,//45-47
            zerodown, zeroup, zerodown, zeroup, zerodown, zeroup,//48-50
            zerodown, zeroup, zerodown, zeroup, zerodown, zeroup,//51-53
            zerodown, zeroup, zerodown, zeroup, zerodown, zeroup,//54-56
            zerodown, zeroup, zerodown, zeroup, zerodown, zeroup,//57-59
            zerodown, zeroup, zerodown, zeroup, zerodown, zeroup,//60-62
            zerodown, zeroup, //63
            zerodown, zeroup, onedown, oneup, zerodown, zeroup, onedown, oneup,//64-67(四位检验码)后32位结束

    };

    /**
     * 大数组模块
     * 全部置通用 在替换
     */
    public static final int[] base = {
            startdown, startup,//起始码
            zerodown, zeroup, zerodown, zeroup, zerodown, zeroup,//1-3
            zerodown, zeroup, zerodown, zeroup, zerodown, zeroup,//4-6
            onedown, oneup, zerodown, zeroup, zerodown, zeroup,//7-9
            zerodown, zeroup, zerodown, zeroup, zerodown, zeroup,//10-12
            zerodown, zeroup, zerodown, zeroup, zerodown, zeroup,//13-15
            zerodown, zeroup, zerodown, zeroup, zerodown, zeroup,//16-18
            zerodown, zeroup, zerodown, zeroup, zerodown, zeroup,//19-21
            zerodown, zeroup, zerodown, zeroup, zerodown, zeroup,//22-24
            zerodown, zeroup, zerodown, zeroup, zerodown, zeroup,//25-27
            zerodown, zeroup, onedown, oneup, zerodown, zeroup,//28-30
            onedown, oneup, zerodown, zeroup, zerodown, zeroup,//31-33
            onedown, oneup, zerodown, zeroup,//34-35  前35位数据码结束
            connectdown, connectup,//连接码   后32位开始
            zerodown, zeroup, zerodown, zeroup, zerodown, zeroup,//36-38
            zerodown, zeroup, zerodown, zeroup, zerodown, zeroup,//39-41
            zerodown, zeroup, zerodown, zeroup, onedown, oneup,//42-44
            zerodown, zeroup, zerodown, zeroup, zerodown, zeroup,//45-47
            zerodown, zeroup, zerodown, zeroup, zerodown, zeroup,//48-50
            zerodown, zeroup, zerodown, zeroup, zerodown, zeroup,//51-53
            zerodown, zeroup, zerodown, zeroup, zerodown, zeroup,//54-56
            zerodown, zeroup, zerodown, zeroup, zerodown, zeroup,//57-59
            zerodown, zeroup, zerodown, zeroup, zerodown, zeroup,//60-62
            zerodown, zeroup, //63
            zerodown, zeroup, zerodown, zeroup, zerodown, zeroup, zerodown, zeroup,//64-67(四位检验码)后32位结束

    };

    //校验码--1
    public static int check_d = onedown;
    public static int check_u = oneup;
}
