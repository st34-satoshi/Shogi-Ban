package calc_value;

import java.util.ArrayList;

import board.Kyokumen;
import koma.Koma;
import main.Calc;
/**
 * 王様の安全どを評価する。
 * 責められているかどうかを評価
 * 自分と相手でやる。
 * 大雑把に決める。
 * 自分と相手との比較
 * 自陣に敵の駒がどれくらいあるか、駒の強さによって危険度変わる
 * @author satoshi
 *
 */

public class Safety_gyoku_value extends Parent_value {

	public Safety_gyoku_value(Kyokumen kyokumen) {
		super(kyokumen);
		// TODO 自動生成されたコンストラクター・スタブ
	}
	/**
	 * 評価値を決める。
	 * value_boolean,value_intを決める
	 */
	public void calc_value() {
		set_value_boolean(false);
		int value = 0;//最初は危険な駒のポイントの合計
		value += make_value(get_teban());
		value += make_value(Calc.change_teban(get_teban()));
		set_value_int((int)(value * 0.3));
	}
	/**
	 * tebanの玉の危険度を返す
	 * @param koma_list
	 * @param teban
	 * @return
	 */
	private int make_value(int teban) {
		int value = 0;//最初は危険な駒のポイントの合計
		//自陣に敵の駒が何枚あるか,駒のポイントによって危なさ変わる。
		//敵の駒のリストを作る
		ArrayList<Koma> koma_enemy_list = get_kyokumen().get_teban_koma_list_ban(Calc.change_teban(teban));
		//自分の玉の場所
		int place_gyoku = get_kyokumen().get_place_gyoku(teban);
		int place_gyoku_a = place_gyoku / 10;
		int place_gyoku_b = place_gyoku % 10;
		//自分の玉から3ます以内に敵の駒あれば危険。自陣に駒があれば危険。飛車、角、香、が玉に効いていれば危険。？？///
		int place_enemy_koma_a = 0;
		int place_enemy_koma_b = 0;
		int diff_a = 0;
		int diff_b = 0;
		for(Koma koma:koma_enemy_list) {
			place_enemy_koma_a = koma.get_place_a();
			place_enemy_koma_b = koma.get_place_b();
			//玉から3ます以内にあるか
			diff_a = place_gyoku_a - place_enemy_koma_a;
			diff_b = place_gyoku_b - place_enemy_koma_b;
			if(check_diff(diff_a) && check_diff(diff_b)) {
				value += koma.get_point_teban();
			}else {
				//自陣に駒があるか
				if(teban==1) {
					if(koma.get_place_b() > 6) {
						value += (int)(koma.get_point_teban() * 0.6);
					}
				}else if(teban==2) {
					if(koma.get_place_b() < 4) {
						value += (int)(koma.get_point_teban() * 0.6);
					}
				}
			}
		}
		return value;
	}
	/**
	 * diffが3ますいないか判断.3ます以内ならtrue
	 * @param diff
	 * @return
	 */
	private boolean check_diff(int diff) {
		if((diff > -4) && (diff < 4)) {
			return true;
		}
		return false;
	}

}
