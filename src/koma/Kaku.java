package koma;

import board.Kyokumen;

public class Kaku extends Koma {

	public Kaku(int teban_n) {
		super(teban_n);
		set_point(10000000);
	}
	/**
	 * なれる駒はtrueを返すようにする。
	 * @return
	 */
	public boolean able_to_naru() {
		return true;
	}

	public int get_koma_number() {
		return 7;
	}
	public String get_koma_name() {
		return "　角";
	}
	/**
	 * 動ける場所を決める
	 * 反則は確認しない
	 */
	public void decide_move_place(Kyokumen kyokumen) {
		move_place_more(kyokumen, 1, 1);
		move_place_more(kyokumen, 1, -1);
		move_place_more(kyokumen, -1, 1);
		move_place_more(kyokumen, -1, -1);
	}

}
