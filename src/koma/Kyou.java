package koma;

import board.Kyokumen;

public class Kyou extends Koma {

	public Kyou(int teban_n) {
		super(teban_n);
		set_point(3000000);
	}
	/**
	 * 動けるか。反則にならないか。歩香桂だけ
	 * 反則でなければfalse,反則ならtrue
	 * @return
	 */
	public boolean able_to_move() {
		if(get_teban()==1 && get_place_b()==1) {
			return true;
		}
		if(get_teban()==2 && get_place_b() == 9) {
			return true;
		}
		return false;
	}
	/**
	 * なれる駒はtrueを返すようにする。
	 * @return
	 */
	public boolean able_to_naru() {
		return true;
	}

	public int get_koma_number() {
		return 2;
	}
	public String get_koma_name() {
		return "　香";
	}
	/**
	 * 動ける場所を決める
	 * 反則は確認しない
	 */
	public void decide_move_place(Kyokumen kyokumen) {
		if(get_teban()==1) {
			move_place_more(kyokumen, 0, -1);
		}else {
			move_place_more(kyokumen, 0, 1);
		}
	}
}
