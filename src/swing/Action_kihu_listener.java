package swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import board.Board;
import board.Move;

public class Action_kihu_listener implements ActionListener{
	private Board board;
	private Frame_s frame;
	private State_game state;

	public Action_kihu_listener(Board board,Frame_s frame,State_game state) {
		this.board = board;
		this.frame = frame;
		this.state = state;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("最初") && state.check_reading_kihu()) {
			board.set_kyokumen(state.get_kihu().get_first_kyokumen().clone());
			board.reset_except_kyokumen();
			frame.set_kyokumen(board.get_kyokumen());
			frame.change_label_teban(board.get_teban_s());
			state.set_tesuu_first();
		}else if(e.getActionCommand().equals("次へ") && state.check_reading_kihu()) {
			Move move = state.get_move_next();
			if(move != null) {
				board.move_next_kyokumen(move);
				frame.change_panel(move.get_after_place_a(), move.get_after_place_b(), board.get_kyokumen());
				frame.change_panel(move.get_before_place_a(), move.get_before_place_b(), board.get_kyokumen());
				if(move.get_get_koma() != 0) {
					//駒を取っていた時
					frame.change_panel((3-board.get_teban())*10, move.get_get_koma()%10, board.get_kyokumen());
				}
				frame.change_label_teban(board.get_teban_s());
				state.set_tesuu_add();
			}
		}else if(e.getActionCommand().equals("前へ") && state.check_reading_kihu()) {
			//局面を戻す
			Move move = state.get_move_back();
			if(move != null) {
				board.move_back_kyokumen(move);
				frame.change_panel(move.get_after_place_a(), move.get_after_place_b(), board.get_kyokumen());
				frame.change_panel(move.get_before_place_a(), move.get_before_place_b(), board.get_kyokumen());
				if(move.get_get_koma() != 0) {
					//駒を取っていた時
					frame.change_panel((board.get_teban())*10, move.get_get_koma()%10, board.get_kyokumen());
				}
				frame.change_label_teban(board.get_teban_s());
				state.set_tesuu_decrease();
			}
		}else if(e.getActionCommand().equals("最後") && state.check_reading_kihu()) {
			board.set_kyokumen(state.get_kihu().get_last_kyokumen().clone());
			board.reset_except_kyokumen();
			frame.set_kyokumen(board.get_kyokumen());
			frame.change_label_teban(board.get_teban_s());
			state.set_tesuu_end();
		}

	}

}
