package calc_value;

import board.Calc_kyokumen_foul;
import board.Kyokumen;
import board.Move;

/**
 * 反則を選ばないようにするためのクラス
 * 反則ならvalue_booleanをtrueにする。
 * 王手放置も反則
 * @author satoshi
 *
 */
public class Foul_value extends Parent_value {

	public Foul_value(Kyokumen kyokumen) {
		super(kyokumen);
	}
	/**
	 * 評価値を決める。
	 * value_boolean,value_intを決める
	 */
	public void calc_value(Move before_move) {
		if(Calc_kyokumen_foul.check_foul_all(get_kyokumen(),before_move)) {
			//反則ある
			set_value_boolean(true);
			if(get_teban()==1) {
				set_value_int(100);
			}else if(get_teban()==2) {
				set_value_int(-100);
			}else {
				set_value_int(0);
				System.out.println("error: Foul_value class teban"+get_teban());
			}
		}else {
			set_value_boolean(false);
			set_value_int(0);//このクラスでは評価値に差はない。
		}
	}

}
