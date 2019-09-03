package calc_value;

import java.util.ArrayList;

import board.Kyokumen;
import board.Move;
import koma.Koma;
import main.Calc;
/**
 * タダで取れる駒の評価
 * タダで取れる敵の駒の点数を追加する
 * 敵の駒の点数を自分の点数にする。
 * @author satoshi
 *
 */
public class Tada_value extends Parent_value {

	public Tada_value(Kyokumen kyokumen) {
		super(kyokumen);
		// TODO 自動生成されたコンストラクター・スタブ
	}
	/**
	 * 評価値を決める。
	 * value_boolean,value_intを決める
	 */
	public void calc_value() {
		//タダで取れる駒のリストを作る。
		//自分の駒のリストを作る,盤上だけ
		ArrayList<Koma> my_koma_list = get_kyokumen().get_teban_koma_list_ban(get_teban());
		//自分の駒の動けるところに相手の駒があるか確認する。もし駒があればその場所に相手の駒が動けるか確認する
		Koma koma_enemy = null;
		Move move = null;
		int value = 0;//評価値の合計を作る。
		for(Koma koma:my_koma_list) {
			for(int place:koma.get_move_place()) {
				koma_enemy = get_kyokumen().get_koma_from_place(place);//自分の駒が動ける場所に相手の駒があるか
				if(koma_enemy != null) {
					//自分の駒の動けるところに相手の駒がある。敵の駒を取ることができる。moveクラスを作って次の局面にいく。
					move = new Move(koma.get_place_a(),koma.get_place_b(),place/10,place%10);
					if(check_enemy_koma_move(move)) {
						//相手の駒が効いていない
						value += koma_enemy.get_point_teban();//取れる駒の評価値を加える
					}
				}
			}
		}
		value = value * -1;
		set_value_int(value);
	}
	/**
	 * 相手の駒がmoveの移動先に効いているか確認する。
	 * 効いていなければtrueを返す
	 * @param move
	 * @return
	 */
	private boolean check_enemy_koma_move(Move move) {
		Kyokumen kyokumen_enemy = get_kyokumen().clone();
		kyokumen_enemy.move_next_kyokumen(move);
		//相手の駒のリストを作る。
		ArrayList<Koma> koma_list_enemy = kyokumen_enemy.get_teban_koma_list_ban(Calc.change_teban(get_teban()));
		//相手の駒の動けるところにその場所があるか
		for(Koma koma:koma_list_enemy) {
			for(int place:koma.get_move_place()) {
				if(place == move.get_after_place_mix()) {
					return false;
				}
			}
		}
		return true;
	}

}
