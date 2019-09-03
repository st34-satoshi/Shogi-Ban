package koma;

import board.Kyokumen;

public class Gin extends Koma {

	public Gin(int teban_n) {
		super(teban_n);
		set_point(5000000);
	}
	/**
	 * なれる駒はtrueを返すようにする。
	 * @return
	 */
	public boolean able_to_naru() {
		return true;
	}

	public int get_koma_number() {
		return 4;
	}
	public String get_koma_name() {
		return "　銀";
	}
	/**
	 * 動ける場所を決める
	 * 反則は確認しない
	 */
	public void decide_move_place(Kyokumen kyokumen) {
		int difference = 1;//先後によって進むか戻るか
		if(get_teban()==1) {
			difference = -1;//先手の時
		}
		int a = get_place_a() * 10;
		int b = get_place_b();
		int b1 = b + difference;//動く先の場所
		Calc_koma calc = new Calc_koma();
		Koma koma = null;
		int[] array = {a+b1,a+10+b+1,a+10+b-1,a-10+b+1,a-10+b-1};
		for(int i:array) {
			if(calc.isBan(i)) {
				koma = kyokumen.get_banarray(i);
				if(koma == null) {
					add_move_place(i);
				}else {
					if(koma.get_teban() != get_teban()) {
						add_move_place(i);
						add_point_enemy(koma);
					}else {
						add_point_my_koma();
					}
				}
			}
		}
	}

}
