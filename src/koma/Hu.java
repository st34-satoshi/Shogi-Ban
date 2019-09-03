package koma;

import board.Kyokumen;

public class Hu extends Koma {

	public Hu(int teban_n) {
		super(teban_n);
		set_point(1000000);
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
		return 1;
	}
	public String get_koma_name() {
		return "　歩";
	}
	/**
	 * 動ける場所を決める
	 * 反則は確認しない
	 */
	public void decide_move_place(Kyokumen kyokumen) {
		Koma koma = null;
		int a = get_place_a();
		int b = get_place_b();
		int difference = 1;
		if(get_teban()==1) {
			difference = -1;
		}
		b += difference;
		koma = kyokumen.get_banarray(a, b);
		if(koma != null) {
			if(koma.get_teban() == get_teban()) {
				return;
			}
		}
		add_move_place(a*10+b);

	}
}
