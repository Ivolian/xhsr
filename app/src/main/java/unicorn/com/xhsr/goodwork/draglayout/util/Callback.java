package unicorn.com.xhsr.goodwork.draglayout.util;

public interface Callback {
	void onBefore();

	boolean onRun();

	void onAfter(boolean b);
}
