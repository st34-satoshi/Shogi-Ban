package koma;

import board.Kyokumen;

public class Kei extends Koma {

	public Kei(int teban_n) {
		super(teban_n);
		set_point(3000000);
	}
	/**
	 * 動けるか。反則にならないか。歩香桂だけ
	 * 反則でなければfalse,反則ならtrue
	 * @return
	 */
	public boolean able_to_move() {
		if(get_teban()==1 && (get_place_b()==1 || get_place_b()==2)) {
			return true;
		}
		if(get_teban()==2 && (get_place_b() == 9 || get_place_b()==8)) {
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
		return 3;
	}
	public String get_koma_name() {
		return "　桂";
	}
	/**
	 * 動ける場所を決める
	 * 反則は確認しない
	 */
	public void decide_move_place(Kyokumen kyokumen) {
		int difference = 2;//先後によって進むか戻るか
		if(get_teban()==1) {
			difference = -2;//先手の時
		}
		int a1 = get_place_a() + 1;
		int a2 = get_place_a() - 1;
		int b = get_place_b() + difference;//動く先の場所
		Koma koma = null;
		if(Calc_koma.isBan(a1, b)) {
			koma = kyokumen.get_banarray(a1, b);
			if(koma == null) {
				add_move_place(a1*10+b);
			}else {
				if(koma.get_teban() != get_teban()) {
					add_move_place(a1*10+b);
					add_point_enemy(koma);
				}else {
					add_point_my_koma();
				}
			}
		}
		if(Calc_koma.isBan(a2, b)) {
			koma = kyokumen.get_banarray(a2, b);
			if(koma == null) {
				add_move_place(a2*10+b);
			}else {
				if(koma.get_teban() != get_teban()) {
					add_move_place(a2*10+b);
					add_point_enemy(koma);
				}else {
					add_point_my_koma();
				}
			}
		}

	}

}
