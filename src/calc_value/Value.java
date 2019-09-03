package calc_value;

import java.util.ArrayList;

import board.Calc_kyokumen_foul;
import board.Kyokumen;
import board.Move;

/**
 * 評価値のクラス
 * 現在の局面だけの評価をする
 * @author satoshi
 *
 */

public class Value {
	private int value;//評価値,//歩特で1,000,000くらい？,2147480000max
	private boolean determe;//勝負が決まっている時true,
	/*
	private static final int max_depth = 3;//最大なんてまで読むか
	int depth;
	int value_expect;
	*/
	public Value(){
		determe = false;
		value = 0;//
	}
	/**
	 * 評価値を計算する。
	 * 次の手は読まずに現在の局面だけで評価する
	 */
	public void calc_value_present(Kyokumen kyokumen,ArrayList<Kyokumen> kyokumen_list,Move before_move) {
		//反則がないか
		Foul_value foul_value = new Foul_value(kyokumen);
		foul_value.calc_value(before_move);
		determe = foul_value.get_value_boolean();
		value = foul_value.get_value_int();
		if(determe) {
			return;//反則あれば終了
		}
		if(kyokumen_list!=null) {
		//千日手チェック
			int sennitite = Calc_kyokumen_foul.check_sennitite(kyokumen_list, kyokumen);
			if(sennitite==1) {
				System.out.println("sennitite!");
				set_determe(true);
				set_value(0);
				return;
			}else if(sennitite==2) {
				//手番の負け
				set_determe(true);
				set_value(100);
				if(kyokumen.get_teban()==1) {
					set_value(-100);
				}
				return;
			}else if(sennitite==3) {
				//手番の勝ち
				set_determe(true);
				set_value(100);
				if(kyokumen.get_teban()==2) {
					set_value(-100);
				}
				return;
			}
		}
		//詰みチェック,自分の玉に王手がかかっていて3手で詰むか
		if(Tumi_check.check_tumi_for_gyoku(kyokumen,2)) {
			//詰みがある.手番が負けている。
			set_lose(kyokumen.get_teban());
			return;
		}
		//詰みチェック,相手の玉を詰ますことができるか
		if(Tumi_check.check_tumi_n_te(kyokumen, 1)!=null) {
			//詰みがある。勝っている
			set_win(kyokumen.get_teban());
			return;
		}
		//駒の損得
		Loss_koma_value loss_koma_value = new Loss_koma_value(kyokumen);
		add_value(loss_koma_value.get_value_int());
		//駒の働き、動ける場所
		Move_place_value move_place_value = new Move_place_value(kyokumen);
		add_value(move_place_value.get_value_int());
		//タダで取られる駒
		///System.out.println("calc_value_present Tada_value start:"+get_value_int());
		Tada_value tada_value = new Tada_value(kyokumen);
		add_value(tada_value.get_value_int());
		///System.out.println("calc_value_present Tada_value fin:"+get_value_int());
		//玉の安全度
		Safety_gyoku_value safety_gyoku_value = new Safety_gyoku_value(kyokumen);
		add_value(safety_gyoku_value.get_value_int());
		///System.out.println("calc_value_present Safety_gyoku_value fin:"+get_value_int());
		//取られそうな駒の評価値を入れる。
		//
		/*
		if(count_depth.get_check_likely_do() || count_depth.get_check_likely_depth()) {
			//取られそうなの探索をしていないか、取られそうな駒の探索をしていてまだ見る時
			//count_depth.add_depth_likely();
			Likely_to_be_token_value likely_to_be_token_value = new Likely_to_be_token_value(kyokumen);
			///likely_to_be_token_value.set_count_depth(count_depth);
			//likely_to_be_token_value.set_depth(likely_depth + 1);
			likely_to_be_token_value.calc_value_l(count_depth);
			//とった時の相手の局面の中で最高の評価値がセットされている。今の評価値よりも良ければ良さの9割追加？
			if(likely_to_be_token_value.get_value_boolean()) {
				add_value_int(get_good_diff(change_double_int(likely_to_be_token_value.get_value_int() * 0.9)));
			}
			///System.out.println("Calc_value calc_value_present likely_to_be_token value"+likely_to_be_token_value.get_value_int()+"likely depth:"+likely_depth);///
		}
		*/
	}
	private void add_value(int add) {
		value += add;
	}
	public int get_value() {
		return value;
	}
	public boolean get_determe() {
		return determe;
	}
	public void set_value(int value) {
		this.value = value;
	}
	public void set_determe(boolean determe) {
		this.determe = determe;
	}
	public void set_value_all(Value value) {
		this.value = value.get_value();
		this.determe = value.get_determe();
	}
	/**
	 * 勝ちが決まっている時のその設定をする
	 * @param teban
	 */
	public void set_win(int teban) {
		set_determe(true);
		if(teban==1) {
			set_value(100);
		}else if(teban==2) {
			set_value(-100);
		}
	}
	/**
	 * 負けが決まっている時にその設定をする。
	 * 手番が負け
	 * @param teban
	 */
	private void set_lose(int teban) {
		set_determe(true);
		if(teban==1) {
			set_value(-100);
		}else if(teban==2) {
			set_value(100);
		}
	}
	/**
	 * tebanが勝っていればtrue
	 * @param teban
	 * @return
	 */
	public boolean get_win(int teban) {
		if(!determe) {
			return false;
		}
		if(teban==1 && (value > 0)) {
			return true;
		}
		if(teban==2 && (value < 0)) {
			return true;
		}
		return false;
	}

}
