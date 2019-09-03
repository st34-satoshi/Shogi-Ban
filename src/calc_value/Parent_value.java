package calc_value;

import board.Kyokumen;

/**
 * kyokumenクラスのそれぞれの評価値を決める親クラス
 * このクラスを継承して評価値を決めるクラスを作るs
 * @author satoshi
 *
 */
public class Parent_value {
	private Kyokumen kyokumen;
	private boolean value_boolean;//勝敗が決まっているときはtrue
	private int value_int;
	public Parent_value(Kyokumen kyokumen) {
		this.kyokumen = kyokumen;
		calc_value();
	}

	/**
	 * 評価値を決める。
	 * value_boolean,value_intを決める
	 */
	public void calc_value() {
		value_boolean = false;
		value_int = 0;
	}
	/**
	 * 反則や詰み、王手などダメなてやこれしかないなどの手がある時trueにする?
	 * 反則や王手無視などダメな時だけ。
	 * 勝っているときにどうするかは？？勝ちにできたら強い
	 * @return
	 */
	public boolean get_value_boolean() {
		return value_boolean;
	}
	/**
	 * int 型で評価値を返す。
	 * @return
	 */
	public int get_value_int() {
		return value_int;
	}
	public Kyokumen get_kyokumen() {
		return kyokumen;
	}
	public int get_teban() {
		return kyokumen.get_teban();
	}
	public void set_value_int(int value_int) {
		this.value_int = value_int;
	}
	public void set_value_boolean(boolean value_boolean) {
		this.value_boolean = value_boolean;
	}

}
