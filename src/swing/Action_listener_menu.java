package swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import board.Board;
import board.Free_kyokumen;
import board.Move;
import calc_value.Tumi_check;
import calc_value.Value;
import main.Kihu;

public class Action_listener_menu extends JFrame implements ActionListener{
	private Board board;
	private Frame_s frame;
	private State_game state;//状態を持つ。1:対局中,2:対局していない
	private static String save_kyokumen_name = "kyokumen_s.txt";//局面を保存する時のファイル名
	private static String read_kyokumen_name = "kyokumen_r.txt";//局面を読み込む時のファイル名
	private static String save_kihu_name = "kihu_s.txt";
	private static String read_kihu_name = "kihu_r.txt";
	public Action_listener_menu(Board board,Frame_s frame,State_game state) {
		this.board = board;
		this.frame = frame;
		this.state = state;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("新規対局") && state.check_not_gaming()) {
			int option = JOptionPane.showConfirmDialog(this, "データは失われます。新規対局を始めますか？", "最終確認", JOptionPane.YES_NO_OPTION,JOptionPane.PLAIN_MESSAGE);
		    if (option == JOptionPane.YES_OPTION){
		    		set_first_kyokumen();
		    }else if (option == JOptionPane.NO_OPTION){
		    		System.out.println("新規対局は中止しました");
		    }
		}else if(e.getActionCommand().equals("投了") && state.check_gaming_player()) {
			JOptionPane.showMessageDialog(this, board.get_teban_s()+"の負けです。", "投了",JOptionPane.PLAIN_MESSAGE);
			board.get_kihu().make_last_kyokumen();
			state.set_reading_kihu(board.get_kihu().get_kihu_list().size());
			frame.set_reading_kihu();
		}else if(e.getActionCommand().equals("局面評価")) {
			//局面を評価して値を表示.警告で表示?
			//とりあえず現在の局面だけの評価値,n手先の評価値まで求められるとよい。
			Value value = new Value();
			value.calc_value_present(board.get_kyokumen(), board.get_kyokumen_list(), board.get_before_move());
			String determine = "す。";
			if(!value.get_determe()) {
				determine = "せん。";
			}
			JOptionPane.showMessageDialog(this, "探索なしの局面の評価値!\n勝敗は決まっていま"+determine+"\n評価値は:"+value.get_value(),"この局面の評価値",JOptionPane.PLAIN_MESSAGE);
		}else if(e.getActionCommand().equals("待った")) {
			//待ったが押されたら一手戻る。cpと対局する時は2手戻る。cpが待ったを押すことはない.before_moveがなければ戻らない。
			if(board.contain_cp()) {
				matta();
			}
			matta();
		}else if(e.getActionCommand().equals("局面保存")) {
			//boardにある局面を保存する。
			board.save_kyokumen(save_kyokumen_name);
			JOptionPane.showMessageDialog(this, save_kyokumen_name+" に局面が保存されました。", "局面保存",JOptionPane.PLAIN_MESSAGE);
		}else if(e.getActionCommand().equals("局面読み込み")) {
			//局面読み込みは対局していない時しかできない。現在の情報が失われる確認をする。
			if(state.check_not_gaming()){
				int option = JOptionPane.showConfirmDialog(this, "データは失われます。局面を読み込みますか？", "最終確認", JOptionPane.YES_NO_OPTION,JOptionPane.PLAIN_MESSAGE);
			    if (option == JOptionPane.YES_OPTION){
			    		board.read_kyokumen(read_kyokumen_name);
			    		frame.set_kyokumen(board.get_kyokumen());
			    }else if (option == JOptionPane.NO_OPTION){
			    		System.out.println("局面読み込みは中止しました");
			    }
			    state.set_not_gaming();
			    frame.set_kihu_panel_not_visible();
			}
		}else if(e.getActionCommand().equals("棋譜保存")) {
			//棋譜再生中ならstateクラスにある棋譜を保存する。
			if(state.check_reading_kihu()) {
				state.get_kihu().save_kihu_from_first_kyokumen(save_kihu_name);
			}else {
				board.get_kihu().save_kihu_from_first_kyokumen(save_kihu_name);
			}
			JOptionPane.showMessageDialog(this, save_kihu_name+" に棋譜が保存されました。", "棋譜保存",JOptionPane.PLAIN_MESSAGE);
		}else if(e.getActionCommand().equals("棋譜読み込み")) {
			//棋譜読み込みは対局していない時しかできない。現在の情報が失われる確認をする。
			if(state.check_not_gaming()){
				int option = JOptionPane.showConfirmDialog(this, "データは失われます。棋譜を読み込みますか？", "最終確認", JOptionPane.YES_NO_OPTION,JOptionPane.PLAIN_MESSAGE);
			    if (option == JOptionPane.YES_OPTION){
			    		board.read_kihu(read_kihu_name);
			    		frame.set_kyokumen(board.get_kyokumen());
			    		state.set_reading_kihu(0);
			    		frame.set_kihu_panel_visible();
			    }else if (option == JOptionPane.NO_OPTION){
			    		System.out.println("棋譜読み込みは中止しました");
			    }
			}
		}else if(e.getActionCommand().equals("現在の局面から対局")) {
			//対局していない時なら現在の局面から対局開始。棋譜読み込み中なら棋譜の続きから対局することもできる。
			if(state.check_nothin()) {
				set_this_kyokumen();
			}else if(state.check_reading_kihu()) {
				int option = JOptionPane.showConfirmDialog(this, "データは失われます。対局を始めますか？", "最終確認", JOptionPane.YES_NO_OPTION,JOptionPane.PLAIN_MESSAGE);
			    if (option == JOptionPane.YES_OPTION){
			    		int option_2 = JOptionPane.showConfirmDialog(this, "棋譜の続きから対局をしますか？", "最終確認", JOptionPane.YES_NO_OPTION,JOptionPane.PLAIN_MESSAGE);
				    if (option_2 == JOptionPane.YES_OPTION){
				    		set_this_kihu();
				    }else if (option_2 == JOptionPane.NO_OPTION){
				    		set_this_kyokumen();
				    }
			    }else if (option == JOptionPane.NO_OPTION){
			    		System.out.println("対局再開を中止しました");
			    }
			}
		}else if(e.getActionCommand().equals("自由")) {
			//自由に駒を動かせるようにする
			if(state.check_not_gaming()) {
				//自由開始
				int option = JOptionPane.showConfirmDialog(this, "データは失われます。自由局面をはじめますか？", "最終確認", JOptionPane.YES_NO_OPTION,JOptionPane.PLAIN_MESSAGE);
			    if (option == JOptionPane.YES_OPTION){
			    		System.out.println("自由局面スタート!");
			    		Free_kyokumen free_kyokumen = new Free_kyokumen(board.get_kyokumen());
			    		frame.get_action_listener().set_free_kyokumen(free_kyokumen);
			    		state.set_free_before();
			    		frame.set_free_before();
			    }else if (option == JOptionPane.NO_OPTION){
			    		System.out.println("自由局面を中止しました");
			    }
			}else if(state.check_free_before() || state.check_free_after()) {
				frame.output_message("自由対局を終わります。", "");
				state.set_not_gaming();
				board.set_first();
				int teban = ask("この局面での手番は先手にしますか？","手番確認");
				if(teban!=2) {
					//エラーは先手番にする
					teban = 1;
				}
				board.set_kyokumen(frame.get_action_listener().get_free_kyokumen().get_kyokumen(teban));///
				frame.end_game();
			}
		}else if(e.getActionCommand().equals("詰みチェック")) {
			System.out.println("詰みチェックを始めます。");
			Move move = Tumi_check.check_tumi_n_te(board.get_kyokumen().clone(), 7);
			if(move!=null) {
				frame.output_message("詰みがあります.\n"+move.get_move_kihu(), "詰み");
				System.out.println("詰みがあります。");
				move.output_move_kihu();
			}else {
				frame.output_message("詰みはありません。", "詰みなし");
				System.out.println("詰みがありません。");
			}
		}
	}
	/**
	 * 質問をしてyesなら1,noなら2
	 * 変なのは0
	 * @return
	 */
	private int ask(String question,String title) {
		int option = JOptionPane.showConfirmDialog(this, question, title, JOptionPane.YES_NO_OPTION,JOptionPane.PLAIN_MESSAGE);
	    if (option == JOptionPane.YES_OPTION){
	    		return 1;
	    }else if (option == JOptionPane.NO_OPTION){
	    		return 2;
	    }
	    return 0;
	}
	private void matta() {
		Move back_move = board.get_kihu().get_last_move();
		if(back_move != null) {
			board.get_kihu().remove_last_move();
			board.delete_last_kyokumen_list();
			board.move_back_kyokumen(back_move);
			frame.change_panel(back_move.get_after_place_a(), back_move.get_after_place_b(), board.get_kyokumen());
			frame.change_panel(back_move.get_before_place_a(), back_move.get_before_place_b(), board.get_kyokumen());
			if(back_move.get_get_koma() != 0) {
				//駒を取っていた時
				frame.change_panel((board.get_teban())*10, back_move.get_get_koma()%10, board.get_kyokumen());
			}
			frame.change_label_teban(board.get_teban_s());
		}
	}
	private void set_this_kyokumen() {
		board.reset_except_kyokumen();
		frame.set_kyokumen(board.get_kyokumen());
		//先後決める
		if(!set_player()) {
			System.out.println("対局は中止しました");
			return;
		}
		frame.start_game();
		JOptionPane.showMessageDialog(this, board.get_player_both()+"\nよろしくお願いします。", "対局開始",JOptionPane.PLAIN_MESSAGE);
		state.set_gaming(board.get_teban_player());
		board.get_kyokumen().set_koma_array();
		board.decide_all_koma_move_place();
	}
	private void set_this_kihu() {
		//棋譜を引き継いで対局再開
		board.set_kyokumen(state.get_kihu().get_last_kyokumen());
		set_this_kyokumen();
		Kihu kihu = state.get_kihu();
		board.set_kihu(kihu);
		board.set_before_move(kihu.get_kihu_list().get(kihu.get_kihu_list().size()-1));
	}
	/**
	 * 新規対局になった時に,board,frameを初期画面にする。
	 * 先手後手を人かどうかも決める。
	 */
	private void set_first_kyokumen() {
		board.set_first();
		frame.set_kyokumen(board.get_kyokumen());
		//先後決める
		if(!set_player()) {
			System.out.println("新規対局は中止しました");
			return;
		}
		//先後決まったので対局開始
		frame.start_game();
		JOptionPane.showMessageDialog(this, board.get_player_both()+"\nよろしくお願いします。", "対局開始",JOptionPane.PLAIN_MESSAGE);
		state.set_gaming(board.get_teban_player());
	}
	/**
	 * 先後を決める。
	 * @return 決まらなければfalse
	 */
	private boolean set_player() {
		String selectvalues[] = {"人間,人間", "人間,コンピューター", "コンピューター,人間", "コンピューター,コンピューター"};
		String sengo = "";
		Object value = JOptionPane.showInputDialog(this, "先手,後手を決めてください!","先後選択", JOptionPane.INFORMATION_MESSAGE,null, selectvalues, selectvalues[0]);
		if (value == null){
				return false;
		}else{
			sengo = (String)value;
			if(sengo.equals("人間,人間")) {
				board.set_player_sente(false);
				board.set_player_gote(false);
			}else if(sengo.equals("人間,コンピューター")) {
				board.set_player_sente(false);
				board.set_player_gote(true);
			}else if(sengo.equals("コンピューター,人間")) {
				board.set_player_sente(true);
				board.set_player_gote(false);
			}else if(sengo.equals("コンピューター,コンピューター")) {
				board.set_player_sente(true);
				board.set_player_gote(true);
			}else {
				System.out.println("error:set_first_kyokumen in Action_listener_menu class no choise = "+sengo);
				return false;
			}
		}
		return true;
	}

}
