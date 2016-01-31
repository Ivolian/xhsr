package unicorn.com.xhsr.other;


public class ClickHelp {

    private static long lastClickTime;

    public synchronized static boolean isFastClick() {
        long time = System.currentTimeMillis();
        if ( time - lastClickTime < 500) {
//            ToastUtils.show("fast click");
            return true;
        }
        lastClickTime = time;
        return false;
    }

}
