package swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import board.Board;
import board.Free_kyokumen;
import board.Move;
import koma.Koma;
import main.Calc;

public class Action_listener extends JFrame implements ActionListener{
	private Board board;
	private Frame_s frame;
	private State_game state;//状態を持つ。1:対局中,2:対局していない
	private Free_kyokumen free_kyokumen;
	public Action_listener(Board board,Frame_s frame,State_game state) {
		this.board = board;
		this.frame = frame;
		this.state = state;
		free_kyokumen = null;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		///System.out.println(e.getActionCommand()+": player"+board.get_player_both());
		if(state.check_chose_before_koma() && check_before_koma(e.getActionCommand())) {
			//人間が手番でまだ駒を選択していない。選択された駒は手番の駒である。
			int place = Integer.parseInt(e.getActionCommand());
			state.set_before_place(place);//before_placeを設定して駒を選択した状態にする。
			//選択した駒の背景変える
			frame.change_button_color(place);
		}else if(state.check_chose_after_place()) {
			//人間が手番でbefore_placeを選択している。after_placeが選択された
			if(check_after_place(e.getActionCommand())) {
				//動かせる場所なので動かす。
				int after_place = Integer.parseInt(e.getActionCommand());
				Move move = make_move(state.get_before_place(), after_place);
				board.move_next_kyokumen(move);
				frame.change_panel(move.get_after_place_a(), move.get_after_place_b(), board.get_kyokumen());
				frame.change_panel(move.get_before_place_a(), move.get_before_place_b(), board.get_kyokumen());
				if(move.get_get_koma() != 0) {
					//駒を取っていた時
					frame.change_panel((3-board.get_teban())*10, move.get_get_koma()%10, board.get_kyokumen());
				}
				//駒を動かしたら反則がないか確認あればそこで対局終了
				if(board.check_foul()) {
					System.out.println(board.get_teban_opposite_s()+"の反則負けです。！！");
					JOptionPane.showMessageDialog(this, board.get_teban_opposite_s()+"の反則負けです。", "反則",JOptionPane.PLAIN_MESSAGE);
					state.set_not_gaming();
					frame.end_game();
				}
				int sennitite = board.check_sennitite();
				if(sennitite==1) {
					//千日手
					JOptionPane.showMessageDialog(this, "千日手です。", "千日手",JOptionPane.PLAIN_MESSAGE);
					state.set_not_gaming();
					frame.end_game();
				}else if(sennitite==2) {
					//連続王手の千日手。手番が王手している。
					JOptionPane.showMessageDialog(this, board.get_teban_s()+"の反則負けです。", "反則:連続王手",JOptionPane.PLAIN_MESSAGE);
					state.set_not_gaming();
					frame.end_game();
				}else if(sennitite==3) {
					//連続王手の千日手。手番が王手されている。
					JOptionPane.showMessageDialog(this, board.get_teban_opposite_s()+"の反則負けです。", "反則:連続王手",JOptionPane.PLAIN_MESSAGE);
					state.set_not_gaming();
					frame.end_game();
				}
				state.set_gaming(board.get_teban_player());
			}else {
				//動かせない場所を選択したら最初の選択を解除
				frame.change_panel(state.get_before_place()/10, state.get_before_place()%10, board.get_kyokumen());
				state.set_gaming(board.get_teban_player());
			}
		}else if(state.check_free_before() && check_free_before(e.getActionCommand())) {
			int place = Integer.parseInt(e.getActionCommand());
			state.set_before_place(place);//before_placeを設定して駒を選択した状態にする。
			//選択した駒の背景変える
			frame.change_button_color(place);
			state.set_free_after();
		}else if(state.check_free_after() && check_free_after(e.getActionCommand())) {
			///System.out.println("自由局面after!"+state.get_before_place());
			//駒を動かす。
			int place_after = Integer.parseInt(e.getActionCommand());
			int place_before = state.get_before_place();
			///System.out.println("自由局面after place "+place_before+" af: "+place_after);
			Koma koma = free_kyokumen.move(place_before, place_after);
			frame.change_panel(place_before/10, place_before%10, free_kyokumen);
			if(koma==null) {
				frame.change_panel(place_after/10, place_after%10, free_kyokumen);
			}else {
				//駒を駒台に移す時
				frame.change_panel(place_after/10, koma.get_koma_number()%10, free_kyokumen);
			}
			state.set_free_before();
		}
	}
	private boolean check_free_after(String place_s) {
		int after_place = 0;
		if(Calc.isNumber(place_s)) {
			after_place = Integer.parseInt(place_s);
		}else {
			System.out.println("error: button after place ,free, "+place_s);
			return false;
		}
		//同じ場所をさしていたら動かせる。盤上なら成反転,駒台なら何もしない
		if(after_place == state.get_before_place()) {
			return true;
		}
		if(after_place/100 > 0) {
			//駒台を選択している。駒台には必ず動ける?自分の駒台から自分の駒台は無駄だが操作する。
			return true;
		}
		//盤上は駒がなければ動ける
		Koma koma = free_kyokumen.get_koma_from_place(after_place);
		if(koma == null) {
			return true;
		}
		//盤上に駒があれば正しくない。同じ場所をさしていることはない。
		return false;
	}
	/**
	 * place が正しいかどうかを判断する。
	 * @param place
	 * @return 正しければtrue
	 */
	private boolean check_free_before(String place_s) {
		int place = 0;
		if(Calc.isNumber(place_s)) {
			place = Integer.parseInt(place_s);
		}else {
			System.out.println("error: button before place free, "+place_s);
			return false;
		}
		if(free_kyokumen==null) {
			System.out.println("error: no free kyokumen , ");
		}
		Koma koma = free_kyokumen.get_koma_from_place(place);
		if(koma == null) {
			System.out.println("error: button before place no koma, free,"+place_s);
			return false;
		}
		//駒があればok
		return true;
	}
	/**
	 * Moveクラスを作る
	 * @return
	 */
	private Move make_move(int before_place,int after_place){
		Move move = new Move(before_place/10,before_place%10,after_place/10,after_place%10);
		//なれるかどうかを確認する
		Koma koma = board.get_kyokumen().get_koma_from_place(state.get_before_place());
		if(move.check_able_naru(koma)) {
			int option = JOptionPane.showConfirmDialog(this, "成りますか？", "成る確認", JOptionPane.YES_NO_OPTION,JOptionPane.PLAIN_MESSAGE);
		    if (option == JOptionPane.YES_OPTION){
		    		move.set_naru(true);
		    }else if (option == JOptionPane.NO_OPTION){
		    		move.set_naru(false);
		    }
		}
		return move;
	}
	/**
	 * 駒を選択した後に動かせる場所かどうかをチェック
	 * 動かせる場所ならtrue
	 * @return
	 */
	private boolean check_after_place(String place_s) {
		int after_place = 0;
		if(Calc.isNumber(place_s)) {
			after_place = Integer.parseInt(place_s);
		}else {
			System.out.println("error: button after place , "+place_s);
			return false;
		}
		if(after_place/100 > 0) {
			//駒台を選択している。
			return false;
		}
		//before_placeの駒をつくる
		Koma koma = board.get_kyokumen().get_koma_from_place(state.get_before_place());
		if(koma == null) {
			return false;
		}
		if(koma.get_motigoma()) {
			//持ち駒ならそこに駒がなければよい
			Koma after_koma = board.get_kyokumen().get_koma_from_place(after_place);
			if(after_koma==null) {
				return true;
			}
			return false;
		}else {
			if(koma.contain_move_place(after_place)) {
				return true;
			}
		}
		return false;
	}
	/**
	 * 人間が手番で駒が選択されていない時に選択できる駒かどうかを判断する。
	 * 選択できればtrue,選択できない(相手の駒)ならfalse
	 * @return
	 */
	private boolean check_before_koma(String place_s) {
		int place = 0;
		if(Calc.isNumber(place_s)) {
			place = Integer.parseInt(place_s);
		}else {
			System.out.println("error: button before place , "+place_s);
			return false;
		}
		Koma koma = board.get_kyokumen().get_koma_from_place(place);
		if(koma == null) {
			System.out.println("error: button before place no koma, "+place_s);
			return false;
		}else {
			if(koma.get_teban()==board.get_teban()) {
				return true;
			}
		}
		return false;
	}
	public void set_free_kyokumen(Free_kyokumen free_kyokumen) {
		this.free_kyokumen = free_kyokumen;
	}
	public Free_kyokumen get_free_kyokumen() {
		return free_kyokumen;
	}

}
