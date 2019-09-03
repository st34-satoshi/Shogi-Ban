package calc_value;

import board.Kyokumen;
import koma.Koma;

/**
 * 駒の損得で評価値を決めるクラス。
 * 駒クラスで自分の得点を決めさせる。駒クラスで駒の働きによって自分の得点を決めるように変える？
 * 先手の駒ならプラス、後手の駒はマイナス。
 * @author satoshi
 *
 */
public class Loss_koma_value extends Parent_value {

	public Loss_koma_value(Kyokumen kyokumen) {
		super(kyokumen);
	}
	/**
	 * 評価値を決める。
	 * value_boolean,value_intを決める
	 */
	public void calc_value() {
		//set_value_boolean(false);//駒の損得では勝敗は決まらない。
		int value = 0;
		//盤上の駒
		for(Koma koma:get_kyokumen().get_koma_array()) {
			value += koma.get_point_teban();
		}
		//駒台の駒
		value += get_kyokumen().get_sente_komadai().get_all_point_teban() + get_kyokumen().get_gote_komadai().get_all_point_teban();
		set_value_int(value);
	}

}
