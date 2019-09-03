package koma;

import board.Kyokumen;

public class Hisha extends Koma {

	public Hisha(int teban_n) {
		super(teban_n);
		set_point(12000000);
	}

	/**
	 * なれる駒はtrueを返すようにする。
	 * @return
	 */
	public boolean able_to_naru() {
		return true;
	}
	public int get_koma_number() {
		return 8;
	}
	public String get_koma_name() {
		return "飛車";
	}

	/**
	 * 動ける場所を決める
	 * 反則は確認しない
	 */
	public void decide_move_place(Kyokumen kyokumen) {
		move_place_more(kyokumen, 1, 0);
		move_place_more(kyokumen, -1, 0);
		move_place_more(kyokumen, 0, 1);
		move_place_more(kyokumen, 0, -1);
	}

}
