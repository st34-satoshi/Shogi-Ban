package swing;

import board.Board;
import board.Move;
import main.Kihu;

/**
 * 状態を保存するクラス。
 * 対局中とか、ある駒を選択しているとか
 * @author satoshi
 *
 */

public class State_game {
	//1:何もしていない,2:対局中手番はcp,3:対局中手番は人間で何も選択していない,4:対局中手番は人間で何かの駒を選択している。5:棋譜読み込んでいる。6:フリー自由に動かせる.選択していない.7:選択されている
	private int state;
	private int before_place;
	private Kihu kihu;//棋譜を読み込んだ時に棋譜が保存される。
	private int count_tesuu;//棋譜を読み込んだ時になんて目のところにいるかを保存する。最初の局面にいる時は0.
 	private Board board;
	private Frame_s frame;
	public State_game(Board board,Frame_s frame) {
		this.state = 1;
		this.board = board;
		this.frame = frame;
		kihu = null;
		count_tesuu = 0;
	}
	public void set_not_gaming() {
		before_place = 0;
		state = 1;
	}
	public void set_before_place(int before_place) {
		//before_placeを設定して手番が人間で駒を選択した状態にする。
		this.before_place = before_place;
		this.state = 4;
	}
	/**
	 * 対局中で人間が手番の時true
	 * @return
	 */
	public boolean check_gaming_player() {
		if(state==3 || state == 4) {
			return true;
		}
		return false;
	}
	public boolean check_chose_after_place() {
		if(this.state == 4) {
			return true;
		}
		return false;
	}
	/**
	 * 対局中かどうかを判断する。
	 * 対局をしていないならtrue
	 * @return
	 */
	public boolean check_not_gaming() {
		if(state==1 || state==5) {
			return true;
		}
		return false;
	}
	public boolean check_nothin() {
		if(state==1) {
			return true;
		}
		return false;
	}
	public boolean check_chose_before_koma() {
		if(state==3) {
			return true;
		}
		return false;
	}
	public boolean check_reading_kihu() {
		if(state==5) {
			return true;
		}
		return false;
	}
	public boolean check_free_before() {
		if(state==6) {
			return true;
		}
		return false;
	}
	public boolean check_free_after() {
		if(state==7) {
			return true;
		}
		return false;
	}
	public int get_before_place() {
		/*
		if(state != 4) {
			return -1;
		}
		*/
		return before_place;
	}
	public int get_state() {
		return state;
	}
	public void set_gaming(boolean player) {
		//対局開始の状態にする。開始でない時も呼ばれる
		if(player) {
			this.state = 2;//cp
			//次の一手を計算する。次の局面に進める。
			String teban = board.get_teban_s();
			if(board.go_next_board_from_swing()==0) {
				//投了
				frame.output_touryou(teban);
				board.get_kihu().make_last_kyokumen();
				set_reading_kihu(board.get_kihu().get_kihu_list().size());
				frame.set_reading_kihu();
				return;
			}
			Move move = board.get_before_move();
			frame.change_panel(move.get_after_place_a(), move.get_after_place_b(), board.get_kyokumen());
			frame.change_panel(move.get_before_place_a(), move.get_before_place_b(), board.get_kyokumen());
			move.output_move_kihu();
			if(move.get_get_koma() != 0) {
				//駒を取っていた時
				frame.change_panel((3-board.get_teban())*10, move.get_get_koma()%10, board.get_kyokumen());
			}
			//cp同士でも反則があれば終了できる
			//駒を動かしたら反則がないか確認あればそこで対局終了
			if(board.check_foul()) {
				System.out.println(board.get_teban_opposite_s()+"の反則負けです。！！");
				frame.output_message(board.get_teban_opposite_s()+"の反則負けです。", "反則");
				set_not_gaming();
				frame.end_game();
			}
			int sennitite = board.check_sennitite();
			if(sennitite==1) {
				//千日手
				frame.output_message("千日手です。", "千日手");
				set_not_gaming();
				frame.end_game();
			}else if(sennitite==2) {
				//連続王手の千日手。手番が王手している。
				frame.output_message(board.get_teban_s()+"の反則負けです。", "反則:連続王手");
				set_not_gaming();
				frame.end_game();
			}else if(sennitite==3) {
				//連続王手の千日手。手番が王手されている。
				frame.output_message(board.get_teban_opposite_s()+"の反則負けです。", "反則:連続王手");
				set_not_gaming();
				frame.end_game();
			}
			//次もcpの時は待つために次へボタン表示
			if(board.get_teban_player()) {
				//次へ行くか確認してやめることもできるようにする。
				if(frame.check_next_or_exit()) {
					//次へ行く
					set_gaming(board.get_teban_player());
				}else {
					//そこで対局を終了する。
					frame.output_message("対局を終了します。", "対局終了");
					board.get_kihu().make_last_kyokumen();
					set_reading_kihu(board.get_kihu().get_kihu_list().size());
					frame.set_reading_kihu();
				}
			}else {
				//次が人の手番
				set_gaming(board.get_teban_player());
			}

		}else {
			this.state = 3;//人間
			//入力を待つ
		}
		frame.set_next_te("手番: "+board.get_teban_s());
	}
	public void set_free_before() {
		state = 6;
		before_place = 0;
	}
	public void set_free_after() {
		state = 7;
	}
	public void set_reading_kihu(int tesuu) {
		state = 5;
		before_place = 0;
		this.kihu = board.get_kihu();
		count_tesuu = tesuu;
	}
	public void set_tesuu_add() {
		count_tesuu++;
		if(count_tesuu>kihu.get_kihu_list().size()) {
			count_tesuu = kihu.get_kihu_list().size();
			System.out.println("error:count tesuu in state game class add ");
		}
	}
	public void set_tesuu_decrease() {
		count_tesuu--;
		if(count_tesuu<0) {
			count_tesuu = 0;
			System.out.println("error:count tesuu in state game class decrease");
		}
	}
	public void set_tesuu_first() {
		count_tesuu = 0;
	}
	public void set_tesuu_end() {
		count_tesuu = kihu.get_kihu_list().size();
	}
	public Kihu get_kihu() {
		return kihu;
	}
	public int get_count_tesuu() {
		return count_tesuu;
	}
	public Move get_move_next() {
		if(count_tesuu >= kihu.get_kihu_list().size()) {
			return null;
		}
		return kihu.get_kihu_list().get(count_tesuu);
	}
	public Move get_move_back() {
		if(count_tesuu <= 0) {
			return null;
		}
		return kihu.get_kihu_list().get(count_tesuu-1);
	}
	public Board get_board() {
		return board;
	}

}
