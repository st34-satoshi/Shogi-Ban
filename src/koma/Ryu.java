package koma;

import board.Kyokumen;

public class Ryu extends Koma {

	public Ryu(int teban_n) {
		super(teban_n);
		set_point(15000000);
	}
	public int get_koma_number() {
		return 18;
	}
	public String get_koma_name() {
		return "　龍";
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
		int a = get_place_a() * 10;
		int b = get_place_b();
		Calc_koma calc = new Calc_koma();
		Koma koma = null;
		int[] array = {a+10+b+1,a+10+b-1,a-10+b+1,a-10+b-1};
		for(int i:array) {
			if(calc.isBan(i)) {
				koma = kyokumen.get_banarray(i);
				if(koma == null) {
					add_move_place(i);
				}else {
					if(koma.get_teban() != get_teban()) {
						add_move_place(i);
					}
				}
			}
		}
	}


}
