package control.eagleweb.xyz.controlredoutline;

class AirBean {
    private int mPower;
    private int mMode;
    private int mTmp;
    private int mEnergy;
    private int mWindCount;
    private int mWindDir;

    /**
     * 开关、度数、模式、自动手动、风向、风量
     * @param power 开关
     * @param i1 度数
     * @param model 模式
     * @param i3 自动手动
     * @param i4 风向
     * @param i5 风量
     */
    public AirBean(int power, int i1, int model, int i3, int i4, int i5) {

        this.mPower = power;
        this.mMode = model;
    }

    public int getmPower() {
        return mPower;
    }

    public int getmMode() {
        return mMode;
    }

    public void setmPower(int mPower) {
        this.mPower = mPower;
    }

    public void setmMode(int mMode) {
        this.mMode = mMode;
    }

    public int getmTmp() {
        return mTmp;
    }

    public void setmTmp(int mTmp) {
        this.mTmp = mTmp;
    }

    public int getmenergy() {
        return mEnergy;
    }

    public void setmenergy(int mEnergy) {
        this.mEnergy = mEnergy;
    }

    public int getmWindCount() {
        return mWindCount;
    }

    public void setmWindCount(int mWindCount) {
        this.mWindCount = mWindCount;
    }

    public int getmWindDir() {
        return mWindDir;
    }

    public void setmWindDir(int mWindDir) {
        this.mWindDir = mWindDir;
    }
}
