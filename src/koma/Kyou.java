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
		int difference = 1;//先後によって進むか戻るか
		if(get_teban()==1) {
			difference = -1;//先手の時
		}
		int a = get_place_a();
		int b = get_place_b() + difference;//動く先の場所
		Koma front_koma = null;
		while(b>0 && b<10) {
			//動く先に自分の駒がないか確認
			front_koma = kyokumen.get_banarray(a, b);
			if(front_koma != null) {
				if(front_koma.get_teban() == get_teban()) {
					//自分の駒がある。
					break;
				}else {
					//相手の駒がある。
					add_move_place(a*10+b);
					break;
				}
			}else {
				//前に駒がない
				add_move_place(a*10+b);
				b += difference;
			}
		}
	}
}
