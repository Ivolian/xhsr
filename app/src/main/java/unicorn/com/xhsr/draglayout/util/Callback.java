package unicorn.com.xhsr.draglayout.util;

public interface Callback {
	void onBefore();

	boolean onRun();

	void onAfter(boolean b);
}
