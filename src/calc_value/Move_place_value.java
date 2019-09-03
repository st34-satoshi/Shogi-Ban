package calc_value;

import board.Kyokumen;
import koma.Koma;

/**
 * 動ける場所の多さで評価値を変える。
 * 一つの場所で10000くらい？
 * @author satoshi
 *
 */
public class Move_place_value extends Parent_value {

	public Move_place_value(Kyokumen kyokumen) {
		super(kyokumen);
	}

	/**
	 * 評価値を決める。
	 * value_boolean,value_intを決める
	 */
	public void calc_value() {
		//value_booleanは変えない
		int value = 0;
		int move_place_n = 0;
		for(Koma koma:get_kyokumen().get_koma_array()) {
			move_place_n = koma.get_move_place().size();
			if(koma.get_teban()==2) {
				move_place_n = -1 * move_place_n;
			}
			value += move_place_n;
		}
		set_value_int(value*30000);
	}

}
