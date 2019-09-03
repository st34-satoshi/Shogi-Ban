package calc_value;

import java.util.ArrayList;

import board.Kyokumen;
import board.Move;
import koma.Koma;
/**
 * 取られそうな駒の評価値を入れる。
 * 手番によって違いがある。？
 * 自分が相手の駒を取れれば駒の点の8割？
 * 相手が自分の駒を取れれば駒の点の2割？
 *
 * 相手が自分の駒を取れるかは考えない。自分が相手の駒を取れる時の探索をする。
 * 取れる場合はとった先の探索をする。相手の局面を評価する。
 * 相手の局面の評価値の中で最高のものを残す
 * 相手の局面でも相手が取れる自分の駒を含めた評価を行う。
 * value_booleanは何も取れる駒がない時false,取れる駒がある時はtrue
 * @author satoshi
 *
 */

public class Likely_to_be_token_value extends Parent_value {
	//private Count_depth count_depth;

	public Likely_to_be_token_value(Kyokumen kyokumen) {
		super(kyokumen);
		// TODO 自動生成されたコンストラクター・スタブ
	}
	/**
	 * 評価値を決める。
	 * value_boolean,value_intを決める
	 */
	public void calc_value_l() {
		set_value_int(0);
		set_value_boolean(true);
		//自分の駒のリストを作る,盤上だけ
		ArrayList<Koma> my_koma_list = get_kyokumen().get_teban_koma_list_ban(get_teban());
		//自分の駒の動けるところに相手の駒があるか確認する。もし駒があればその先の評価値を決める。
		Koma koma_enemy = null;
		///Board board_enemy = null;
		Move move = null;
		Kyokumen kyokumen_enemy = null;
		///Calc_value calc_value = null;
		ArrayList<Integer> next_value_list = new ArrayList<Integer>();//次の局面の評価値のリスト
		///System.out.println("Likely_to_be_token able to get koma");
		///count_depth.output_depth();
		//count_deothのdepth_likelyを設定
		int depth_likely = 1;//count_depth.get_depth_likely();
		///count_depth.add_depth_likely();
		///count_depth.set_check_likely(false);
		for(Koma koma:my_koma_list) {
			for(int place:koma.get_move_place()) {
				koma_enemy = get_kyokumen().get_koma_from_place(place);//自分の駒が動ける場所に相手の駒があるか
				if(koma_enemy != null) {
					//確認
					//koma.output_test();
					//System.out.println("Likely_to_be_token there are no koma token");///
					//koma.output_move_place();
					//敵の駒を取ることができる。moveクラスを作ってその局面の評価値を得る,next_value_listに加える。
					move = new Move(koma.get_place_a(),koma.get_place_b(),place/10,place%10);
					kyokumen_enemy = get_kyokumen().clone();
					kyokumen_enemy.move_next_kyokumen(move);
					///System.out.println("Likely_to_be_token count_depth");
					///count_depth.output_depth();
					///calc_value = new Calc_value(kyokumen_enemy,count_depth);
					///calc_value.set_before_move(move);
					///calc_value.set_likely_depth(depth);
					///calc_value.calc_value_present();
					//次の局面の評価値をnext_value_listに加える。
					int value = 0;
					next_value_list.add(value);
				}
			}
		}
		//count_depth.set_check_likely(true);
		//count_depth.set_depth_likely(depth_likely);
		///System.out.println("Likely_to_be_token able to get koma fin");
		if(next_value_list.isEmpty()) {
			//System.out.println("Likely_to_be_token there are no koma token");///
			set_value_boolean(false);//何もない時
			return;
		}
		//next_value_listの中で最高の評価値が今の局面の評価値より良ければ良さの9割？加える。
		if(get_teban()==1) {
			int max = next_value_list.get(0);
			for(int v:next_value_list) {
				if(max<v) {
					max = v;
				}
			}
			set_value_int(max);
		}else {
			int min = next_value_list.get(0);
			for(int v:next_value_list) {
				if(min>v) {
					min = v;
				}
			}
			set_value_int(min);
		}
	}
	/*
	public void set_count_depth(Count_depth cd) {
		this.count_depth = cd;
	}
	*/

}
