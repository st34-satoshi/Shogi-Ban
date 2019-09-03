package calc_value;

import board.Kyokumen;
import koma.Koma;
/**
 * 自分の駒が効いているか
 * 相手の駒に自分の駒が効いているか
 * komaのスペシャルポイント使う
 * @author satoshi
 *
 */
public class Kiki_with_koma extends Parent_value{

	public Kiki_with_koma(Kyokumen kyokumen) {
		super(kyokumen);
	}

	public void calc_value() {
		int value = 0;
		//全ての盤上の駒でpoint_my_koma,point_enemyを調べる
		for(Koma koma:get_kyokumen().get_koma_array()) {
			value += koma.get_point_my_koma_and_enemy();
		}
		set_value_int(value);
	}

}
