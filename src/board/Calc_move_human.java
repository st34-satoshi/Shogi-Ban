package board;

import koma.Koma;

/**
 * 人間の入力ができるようにする。
 * @author satoshi
 *
 */
public class Calc_move_human {
	private Kyokumen kyokumen;
	public Calc_move_human(Kyokumen kyokumen) {
		this.kyokumen = kyokumen;
	}
	/**
	 * 正しい入力のmoveを返す
	 * @return
	 */
	public Move get_move() {
		//moveを作る。
		Move move = null;
		while(true) {
			move = make_move();
			//moveが正しいか判定。正しくなければやり直し。正しければそれをreturn
			if(check_go_next_kyokumen(move)) {
				//System.out.println("next kyokumen");
				break;
			}
			System.out.println("入力が正しくないです。もう一度やり直してください。");//76
		}
		//成るかどうかの確認
		Koma koma = kyokumen.get_koma_from_place(move.get_before_place_a(), move.get_before_place_b());
		if(move.check_naru(koma)) {
			move.set_naru(true);
		}
		return move;
	}
	/**
	 * 次の局面にいけるか確認する
	 * 入力はMoveクラス。見るのはbefore_placeとafter_placeだけ。
	 * いけたらtrue,いけなければfalse
	 * @return
	 */
	private boolean check_go_next_kyokumen(Move move) {
		//beforeが正しいかチェック
		int before_a = move.get_before_place_a();
		int before_b = move.get_before_place_b();
		if(!check_go_before_place(before_a, before_b)) {
			return false;
		}
		//after_placeが正しいか確認する。Komaクラスのmove_placeを見れば良い
		Koma koma = kyokumen.get_koma_from_place(before_a, before_b);
		int a = move.get_after_place_a();
		int b = move.get_after_place_b();
		//持ち駒の時は盤がnullかだけ見れば良い。
		if(koma.get_motigoma()) {
			//a,bが0はだめ
			if(a==0 || b==0) {
				return false;
			}
			Koma after_koma = kyokumen.get_banarray(a,b);
			if(after_koma==null) {
				return true;
			}
			return false;
		}
		if(koma.contain_move_place(a*10+b)) {
			return true;
		}
		System.out.println("afterの入力が違います。check_go_next_kyokumen");
		return false;

	}
	/**
	 * check_go_next_kyokumen メソッドで使う。before_a,before_bの場所に駒があるか確認する。
	 * @return
	 */
	private boolean check_go_before_place(int before_a,int before_b) {
		if(before_a > 9) {
			//駒台のとき
			if((before_a/10) == get_teban()) {
				//手番の駒台を指している。駒台に駒があるか確認する。
				if(kyokumen.get_koma_from_place(before_a, before_b) != null) {
					//駒がある
					return true;
				}else {
					//駒がない
					System.out.println("error: beforepalce is not true in Calc_move class check_go_before_place method");
					return false;
				}
			}else {
				//手番でない駒台を指しているので入力がおかしい。
				System.out.println("beforeの入力が違います。Calc_move check_go_before_place");
				return false;
			}
		}else {
			//盤面の駒を動かすとき
			Koma koma = kyokumen.get_banarray(before_a, before_b);
			if(koma == null) {
				System.out.println("beforeの入力が違います。ban koma==nullCalc_move check_go_before_place"+before_a+before_b);
				return false;
			}
			if(koma.get_teban()==get_teban()) {
				//手番の駒がある
				return true;
			}
			System.out.println("beforeの入力が違います。teban Calc_move check_go_before_place"+before_a+before_b);
			return false;
		}
	}
	/**
	 * moveクラスを作る。
	 * コンピュータか人間かによって作り方違う。
	 * 駒を動かせるmoveを返す。人間の入力の時は二歩などの反則はあっていい。
	 * @return
	 */
	private Move make_move() {
		if(get_teban()==1) {
			System.out.println("先手番です。");
		}else if(get_teban()==2){
			System.out.println("後手番です。");
		}
		Move move = new Move();
		move.input_move();
		//成るかどうか確認していない。入力は間違っていてもいい
		return move;
	}
	private int get_teban() {
		return kyokumen.get_teban();
	}
}
