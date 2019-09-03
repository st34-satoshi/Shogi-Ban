package board;

import java.util.ArrayList;

import koma.Koma;

/**
 * Boardクラスから次に行けるBoardクラスのリストを作って返す。
 * moveクラスを返すこともできる。
 * @author satoshi
 *
 */
public class Make_board_list {
	/**
	 * 次の手で王手のリストを作る
	 * @param kyokumen
	 * @return
	 */
	public static ArrayList<Move> get_next_move_list_only_oute(Kyokumen kyokumen){
		int teban = kyokumen.get_teban();
		//moveのlistを作る。
		ArrayList<Move> move_list_all = make_move_list(kyokumen,teban);
		if(move_list_all==null) {
			return null;
		}else if(move_list_all.isEmpty()) {
			return null;
		}
		//move_listから反則を除く.反則かを調べるためには局面を進める必要がある。
		//反則でなくて王手になっている手を探す。
		Kyokumen next_kyokumen = null;
		ArrayList<Move> move_list_oute = new ArrayList<Move>();
		for(Move move:move_list_all) {
			next_kyokumen = kyokumen.clone();
			next_kyokumen.move_next_kyokumen(move);
			if(!Calc_kyokumen_foul.check_foul_all(next_kyokumen, move)) {
				//反則がない,王手がかかっているか確認する
				if(Calc_kyokumen_foul.check_oute(next_kyokumen, next_kyokumen.get_teban())) {
					move_list_oute.add(move);
				}
			}
		}
		return move_list_oute;
	}
	/**
	 * kyokumenクラスから次に行けるMoveクラスのリストを返す
	 * 評価値を決めたりはしない。反則は除く。
	 * @param kyokumen
	 * @return
	 */
	public static ArrayList<Move> get_next_move_list_no_foul(Kyokumen kyokumen) {
		int teban = kyokumen.get_teban();
		//moveのlistを作る。
		ArrayList<Move> move_list = make_move_list(kyokumen,teban);
		if(move_list==null) {
			return null;
		}else if(move_list.isEmpty()) {
			return null;
		}
		//move_listから反則を除く.反則かを調べるためには局面を進める必要がある。
		Kyokumen next_kyokumen = null;
		ArrayList<Move> move_list_no_foul = new ArrayList<Move>();
		for(Move move:move_list) {
			next_kyokumen = kyokumen.clone();
			next_kyokumen.move_next_kyokumen(move);
			if(!Calc_kyokumen_foul.check_foul_all(next_kyokumen, null)) {
				//打ち歩詰めを調べる必要はない。?ある?////
				//反則がない
				move_list_no_foul.add(move);
			}
		}
		return move_list_no_foul;
	}
	/**
	 * 次の手のBoardのリストを返す。
	 * 最初は全ての駒の全ての動きを調べる。そのうちいらないのは消していく
	 * 次の手の候補がなければnull返す
	 * @return
	 */
	public static ArrayList<Board> get_next_board_list(Board board) {
		Kyokumen kyokumen = board.get_kyokumen();
		int teban = kyokumen.get_teban();
		//moveのlistを作る。
		ArrayList<Move> move_list = make_move_list(kyokumen,teban);
		if(move_list==null) {
			return null;
		}else if(move_list.isEmpty()) {
			return null;
		}
		//move_listに評価値を加えていく。勝敗が決まる手があれば除いたり、Calc_valueクラスにする
		ArrayList<Board> board_list  = new ArrayList<Board>();
		Board b = null;
		for(Move move:move_list) {
			b = board.get_move_next_board(move);
			board_list.add(b);
		}
		//move_listの中でいらないやつを消していく。候補手の手以下にする。
		//cv_list = select_move(cv_list);
		return board_list;
	}
	/**
	 * kyokumenからmove_listを作る。
	 * 全ての駒の全ての動き方のリスト。反則も含まれる
	 * 駒がなれる時は成ると不成の両方
	 * 持ち駒から打つ時は空いている全部の場所
	 * @param my_koma_list
	 * @return
	 */
	private static ArrayList<Move> make_move_list(Kyokumen kyokumen,int teban){
		ArrayList<Koma> my_koma_list = kyokumen.get_teban_koma_list_all(teban);
		ArrayList<Move> move_list = new ArrayList<Move>();
		Move move = null;
		for(Koma koma:my_koma_list) {
			if(koma.get_motigoma()) {
				Koma ban_koma = null;
				//駒が持ち駒の時,盤上の全てのnullの場所
				for(int i=1;i<10;i++) {
					for(int j=1;j<10;j++) {
						ban_koma = kyokumen.get_koma_from_place(i, j);
						if(ban_koma==null) {
							move = new Move(koma.get_place_a(),koma.get_place_b(),i,j);
							move_list.add(move);
						}
					}
				}
			}
			//盤上の駒の時
			for(int place:koma.get_move_place()) {
				move = new Move();
				move.set_place(koma.get_place_a(), koma.get_place_b(), place);
				//反則ないかは確認しない
				move_list.add(move);
				//なれる場合は成るの時も
				if(koma.able_to_naru()) {
					if(move.able_naru(koma.get_teban())) {
						move = move.clone();
						move.set_naru(true);
						move_list.add(move);
					}
				}
			}
		}
		return move_list;
	}

}
