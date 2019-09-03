package calc_value;

import java.util.ArrayList;

import board.Calc_kyokumen_foul;
import board.Kyokumen;
import board.Make_board_list;
import board.Move;

/**
 * 詰みがあるかどうかを局面から判断するクラス。
 * kyokumenクラスにあった方が良いが長くなってしまうのでこのクラスを作る。
 * @author satoshi
 *
 */

public class Tumi_check {
	/**
	 *手番の玉がその時詰んでいるかを確認する。0手読み
	 * @param kyokumen
	 * @return 詰みがある時true
	 */
	public static boolean check_tumi_teban(Kyokumen kyokumen) {
		if(Calc_kyokumen_foul.check_oute(kyokumen, kyokumen.get_teban())) {
			//王手が手番の玉にかかっている。回避できるか調べる。
			ArrayList<Move> move_list = Make_board_list.get_next_move_list_no_foul(kyokumen);
			if(move_list==null || move_list.isEmpty()) {
				return true;
			}
		}
		return false;
	}
	/**
	 * 局面でn手以下の詰みがあるか調べる
	 * なるべく最短の詰みを見つけられるように横に調べる。本当は横で調べたいがやり方がわからないので縦に調べる。
	 * 手に優先順位をつけられるとよい.
	 * nは奇数を想定している。
	 * @param kyokumen 王手のかかっていない局面。詰将棋と同じ
	 * @return 詰みがあれば最初の一手のmoveを返す。
	 */
	public static Move check_tumi_n_te(Kyokumen kyokumen,int n) {
		if(n<1) {
			return null;
		}
		//次の手で王手の手のリストを作る。
		ArrayList<Move> move_list_oute = Make_board_list.get_next_move_list_only_oute(kyokumen);
		//どれか一つでも詰んでいるのがあればそのmoveを返す
		Kyokumen kyokumen_next = null;
		for(Move move:move_list_oute){
			kyokumen_next = kyokumen.clone();
			kyokumen_next.move_next_kyokumen(move);
			//次の局面で玉側が詰されているかを調べる
			if(check_tumi_for_gyoku(kyokumen_next, n-1)) {
				//詰んでいる
				return move;
			}
		}
		return null;
	}
	/**
	 * 王手されている局面でn手以下で詰まされるか調べる。
	 * @param kyokumen nは偶数を想定
	 * @return 詰みがあればtrue
	 */
	public static boolean check_tumi_for_gyoku(Kyokumen kyokumen,int n) {
		if(n<0) {
			return false;
		}
		//王手でなければfalse
		if(!Calc_kyokumen_foul.check_oute(kyokumen, kyokumen.get_teban())) {
			return false;
		}
		ArrayList<Move> move_list_escape = Make_board_list.get_next_move_list_no_foul(kyokumen);
		if(move_list_escape==null || move_list_escape.isEmpty()) {
			return true;
		}
		//全ての逃げ方で詰むかを確認する.一つでも詰まないのがあればfalse
		Kyokumen kyokumen_next = null;
		for(Move move:move_list_escape) {
			kyokumen_next = kyokumen.clone();
			kyokumen_next.move_next_kyokumen(move);
			if(check_tumi_n_te(kyokumen_next, n-1)==null) {
				//詰まない
				return false;
			}
		}
		//詰まない逃げ方はない
		return true;
	}

}
