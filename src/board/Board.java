package board;

import java.util.ArrayList;
import java.util.Collections;

import calc_value.Value;
import main.Kihu;
import main.Player;

/**
 * ここがメインのクラス。
 * Boardクラスは次のBoardクラスを子に持ちどこに行くのがいいか判断する。
 * playerが人間しかいなければboardのこクラスは作らない。
 * playerによって次の手を考えるか、入力を待つか判断する。
 * @author satoshi
 *
 */
public class Board {
	//手番は局面から取る
	private Kyokumen kyokumen;
	private Player player;
	private Move before_move;//直前のmove,棋譜を保存するときに使う。
	private ArrayList<Board> board_list;//候補のリスト,全ての手のリスト
	private Value value;//評価値,2147480000max
	private static int max_depth = 2;//何手まで読めるようにするか,1手読むとは、自分が指して相手が指した手の評価をする?。
	private ArrayList<Kyokumen> kyokumen_list;//千日手チェックに必要,このboardの持つkyokumenは入っていない
	private Kihu kihu;//棋譜を保存する。

	public Board(Kyokumen kyokumen,Player player,ArrayList<Kyokumen> kyokumen_list,Kihu kihu) {
		this.kyokumen = kyokumen;
		this.player = player;
		this.kyokumen_list = kyokumen_list;
		board_list = new ArrayList<Board>();
		value = new Value();
		before_move = null;
		this.kihu = kihu;
	}
	/**
	 * main not swing よう
	 * @param kyokumen
	 * @param player
	 * @param kyokumen_list
	 */
	public Board(Kyokumen kyokumen,Player player,ArrayList<Kyokumen> kyokumen_list) {
		this.kyokumen = kyokumen;
		this.player = player;
		this.kyokumen_list = kyokumen_list;
		board_list = new ArrayList<Board>();
		value = new Value();
		before_move = null;
		this.kihu = new Kihu(kyokumen);
	}
	/**
	 * 局面クラスだけで作る。
	 * その局面が詰みかどうか局面だけから判断したい時に使う
	 * @param kyokumen
	 */
	public Board(Kyokumen kyokumen) {
		this.kyokumen = kyokumen;
		this.player = new Player();
		this.before_move = null;
		this.board_list = null;
		this.value = new Value();
		this.kyokumen_list = new ArrayList<Kyokumen>();
		this.kihu = new Kihu(kyokumen);
	}
	/**
	 * 棋譜を読み込む
	 * @param load_file
	 */
	public void read_kihu(String load_file) {
		set_first();
		kihu.read_kihu_from_first_kyokumen(load_file);
		kyokumen = kihu.get_first_kyokumen().clone();
	}
	/**
	 * 局面を読み込む
	 * 局面以外は初期状態にする。
	 */
	public void read_kyokumen(String load_file) {
		set_first();
		kyokumen.load_kyokumen(load_file);
	}
	/**
	 * 初期画面にする。他のフィールドも初期状態にする。
	 */
	public void set_first() {
		this.kyokumen = new Kyokumen();
		this.player = new Player();
		this.before_move = null;
		this.board_list = new ArrayList<Board>();
		this.value = new Value();
		this.kyokumen_list = new ArrayList<Kyokumen>();
		this.kihu = new Kihu(kyokumen);
	}
	/**
	 * 局面以外はリセット
	 */
	public void reset_except_kyokumen() {
		kyokumen.decide_all_koma_move_place();
		this.player = new Player();
		this.before_move = null;
		this.board_list = new ArrayList<Board>();
		this.value = new Value();
		this.kyokumen_list = new ArrayList<Kyokumen>();
		this.kihu = new Kihu(kyokumen);
	}
	/**
	 * 引数のboardに変身する
	 * @param board
	 */
	private void set_board(Board board) {
		this.kyokumen = board.get_kyokumen();
		//this.player
		this.before_move = board.get_before_move();
		this.board_list = board.get_board_list();
		this.value = board.get_value();
		this.kyokumen_list = board.get_kyokumen_list();
		this.kihu = board.get_kihu();
	}
	public void save_kyokumen(String save_file) {
		kyokumen.save_kyokumen(save_file);
	}
	public int check_sennitite(ArrayList<Kyokumen> kyokumen_list) {
		return Calc_kyokumen_foul.check_sennitite(kyokumen_list, kyokumen);
	}
	public int check_sennitite() {
		return Calc_kyokumen_foul.check_sennitite(kyokumen_list, kyokumen);
	}
	/**
	 * 詰んでいるか確認する
	 * @return手番の王様が詰んでいる時true
	 */
	public boolean check_tumi() {
		Board bb = new Board(kyokumen.clone());
		Board next_board = bb.decide_next_board_no_parent(max_depth);
		if(next_board==null) {
			//次に行く場所がない。詰み
			return true;
		}
		return false;
	}
	/**
	 * 反則ないか判断。反則ならtrue
	 * @return
	 */
	public boolean check_foul() {
		return Calc_kyokumen_foul.check_foul_all(kyokumen,before_move);
	}
	/**
	 * 全ての駒のmove_placeを決める
	 */
	public void decide_all_koma_move_place() {
		kyokumen.decide_all_koma_move_place();
	}

	/**
	 * 局面を出力する。
	 */
	public void output_kyokumen() {
		System.out.println("value: "+value.get_value());
		kyokumen.output_kyokumen();
	}

	/**
	 * Moveをもらって戻る。
	 * @param move
	 */
	public void move_back_kyokumen(Move move) {
		kyokumen.move_back_kyokumen(move);
	}
	/**
	 * moveから次の局面になるboardを返す
	 * すでにboard_listに作られていればそれを返す
	 * @param move
	 * @return
	 */
	public Board get_move_next_board(Move move) {
		kyokumen.clone().move_next_kyokumen(move);//ここで最終的にmoveが決まる
		if(board_list!=null && !board_list.isEmpty()) {
			for(Board b:board_list) {
				if(move.equals_move(b.get_before_move())) {
					return b;
				}
			}
		}
		ArrayList<Kyokumen> kyokumen_list_n = new ArrayList<Kyokumen>();
		for(Kyokumen k:kyokumen_list) {
			kyokumen_list_n.add(k);
		}
		//kyokumen_list_n.add(kyokumen.clone());
		Board board = new Board(kyokumen.clone(),player,kyokumen_list_n,kihu.clone_kihu());
		board.move_next_kyokumen(move);
		return board;
	}
	/**
	 * moveをもらって局面を進める。
	 * board_listにあればそのboardになる
	 * @param move
	 */
	public void move_next_kyokumen(Move move) {
		kyokumen.move_next_kyokumen(move);//ここで最終的にmoveが決まる
		if(board_list!=null && !board_list.isEmpty()) {
			for(Board b:board_list) {
				if(move.equals_move(b.get_before_move())) {
					set_board(b);
					return;
				}
			}
		}
		kyokumen_list.add(kyokumen.clone());
		kihu.add_move(move.clone());
		before_move = move;
	}
	/**
	 * 次の局面へいく。コンピュータ,人間共に使う。
	 * @return
	 * 次のboardを返す
	 */
	public Board go_next_board() {
		Board board = null;
		Move move = null;
		if(player.get_player(get_teban())) {
			//コンピュータ,候補手の数だけ探索する。
			board = decide_next_board_no_parent(1);
		}else {
			//人間,
			//正しいmoveを受け取る。反則は関係ない。
			Calc_move_human calc_move_human = new Calc_move_human(kyokumen);
			move = calc_move_human.get_move();
			kyokumen_list.add(kyokumen.clone());
			board = new Board(kyokumen, player,kyokumen_list);
			board.move_next_kyokumen(move);
			if(board != null) {
				board.set_before_move(move);//いらない///
			}
		}
		return board;
	}
	/**
	 * 次の局面がなければ0返す
	 * @return
	 */
	public int go_next_board_from_swing() {
		System.out.println("calc next te!");///
		Board board = decide_next_board_no_parent(1);
		if(board==null) {
			return 0;
		}
		set_board(board);
		return 1;
	}
	/**
	 * board_listを先手なら評価値の高い順に並び替える(後手なら低い順)
	 */
	private void sort_board_list() {
		if(get_teban()==1) {
			Collections.sort(board_list, new ValueComparator_gote());
		}else {
			Collections.sort(board_list,new ValueComparator_sente());
		}
	}
	/**
	 * board_list作る。選ぶ。
	 * board_listから次の局面を選んでその局面を返す。
	 * a-b法
	 * depthは1から。1なら1手読む
	 * valueの値も更新する。
	 * @return 次に行く局面を返す
	 */
	public Board decide_next_board_no_parent(int depth) {
		//board_listは一回しか作らない
		if(board_list==null || board_list.isEmpty()) {
			make_board_list();
		}
		select_board_list();
		if(board_list==null || board_list.isEmpty()) {
			//負けている
			value.set_determe(true);
			if(get_teban()==1) {
				value.set_value(-100);
			}else {
				value.set_value(100);
			}
			return null;
		}
		sort_board_list();
		if(depth==1) {
			System.out.println("sorted board list size: "+board_list.size());
		}
		if(depth>=max_depth) {
			Board board = board_list.get(0);//1手先で最善のやつ
			//ここでvalueの値も一つ下の値と同じに更新する。
			value.set_value_all(board.get_value());
			return board;
		}
		//a-b法,board_listの中で一番いいのを選びたい。
		Board next_board = null;//次の局面でその先の選択肢があるか？
		Board board_expect = null;//次になりそうなboard
		int n = 1;///
		for(Board board:board_list) {
			if(depth==1) {
				System.out.println("count "+n++);///
			}
			if(board_expect == null) {
				//初めての時
				if(!board.get_value().get_determe()) {
					//boardの評価値が決まっていない時はその下を探索する。評価値が決定されている時は下を探索する必要はない
					next_board = board.decide_next_board_no_parent(depth+1);
					if(next_board==null) {
						//勝っている.boardを選べば次相手は負ける。boardは負けている
						value.set_win(get_teban());
						return board;
					}
				}
				///勝敗決まっているときはその先を計算する必要ないかもしれないので,省略した方が良い??
				board_expect = board;
			}else {
				//board_expectがある時
				if(!board.get_value().get_determe()) {
					//boardの評価値が決まっていない時はその下を探索する。評価値が決定されている時は下を探索する必要はない
					next_board = board.decide_next_board_have_parent(depth+1, board_expect);
					if(next_board==null) {
						//勝っている.boardを選べば次相手は負ける。boardは負けている
						value.set_win(get_teban());
						return board;
					}
				}
				if(compare_board(board, board_expect)) {
					//boardの方がいい時
					board_expect = board;
				}
			}
		}
		value.set_value_all(board_expect.get_value());
		return board_expect;
	}
	/**
	 * board_list作る。選ぶ。
	 * board_listから次の局面を選んでその局面を返す。
	 * a-b法
	 * depthは1から。1なら1手読む
	 * valueの値も更新する。
	 * 自分の仮の評価値と親の評価値を比較して、ここで自分の評価値の方がよければ終了!
	 * @return そんなに意味はない?
	 */
	private Board decide_next_board_have_parent(int depth,Board board_parent) {
		if(board_list==null || board_list.isEmpty()) {
			make_board_list();
		}
		select_board_list();
		if(board_list==null || board_list.isEmpty()) {
			//負けている
			value.set_determe(true);
			if(get_teban()==1) {
				value.set_value(-100);
			}else {
				value.set_value(100);
			}
			return null;
		}
		sort_board_list();
		if(depth>=max_depth) {
			Board board = board_list.get(0);//1手先で最善のやつ
			//ここでvalueの値も一つ下の値と同じに更新する。
			value.set_value_all(board.get_value());
			return board;
		}
		//a-b法,board_listの中で一番いいのを選びたい。親の評価値より自分の評価値がよければ終了!
		Board next_board = null;
		Board board_expect = null;//次になりそうなboard
		for(Board board:board_list) {
			if(board_expect == null) {
				//初めての時
				if(!board.get_value().get_determe()) {
					//boardの評価値が決まっていない時はその下を探索する。評価値が決定されている時は下を探索する必要はない
					next_board = board.decide_next_board_no_parent(depth+1);
					if(next_board==null) {
						//勝っている.boardを選べば次相手は負ける。boardは負けている
						value.set_win(get_teban());
						return board;
					}
				}
				///勝敗決まっているときはその先を計算する必要ないかもしれないので,省略した方が良い??
				//親の評価値より自分の評価値がよければ終了!
				if(compare_board(board, board_parent)) {
					value.set_value_all(board.get_value());
					return board;
				}
				board_expect = board;
			}else {
				//board_expectがある時
				if(!board.get_value().get_determe()) {
					//boardの評価値が決まっていない時はその下を探索する。評価値が決定されている時は下を探索する必要はない
					next_board = board.decide_next_board_have_parent(depth+1, board_expect);
					if(next_board==null) {
						//勝っている.boardを選べば次相手は負ける。boardは負けている
						value.set_win(get_teban());
						return board;
					}
				}
				if(compare_board(board, board_expect)) {
					//boardの方がいい時
					//親の評価値より自分の評価値がよければ終了!
					if(compare_board(board, board_parent)) {
						value.set_value_all(board.get_value());
						return board;
					}
					board_expect = board;
				}
			}
		}
		value.set_value_all(board_expect.get_value());
		return board_expect;
	}
	/**
	 *
	 * @param b1
	 * @param b2
	 * @return b1がよければtrue,(先手なら大きい、後手なら小さい)
	 */
	private boolean compare_board(Board b1,Board b2) {
		if(get_teban()==1) {
			return ValueComparator_sente.cmpare_board_sente(b1, b2);
		}else if(get_teban()==2) {
			return ValueComparator_gote.compare_board_gote(b1, b2);
		}
		System.out.println("error:teban in Board class compare_board method");
		return true;
	}
	/**
	 * 勝っているのがあればそれ一つにする
	 * 負けているのは除外する
	 * 並び替える前に呼ばれる。
	 */
	private void select_board_list() {
		ArrayList<Board> remove_list = new ArrayList<Board>();
		Value value = null;
		for(Board board:board_list) {
			value = board.get_value();
			if(value.get_determe() && value.get_value()!=0) {
				//勝敗が決まっている。
				//引き分けは除かない
				if(value.get_win(get_teban())) {
					//勝ち
					ArrayList<Board> win_list = new ArrayList<Board>();
					win_list.add(board);
					board_list = win_list;
					return;
				}else {
					//負け
					remove_list.add(board);
				}
			}
		}
		for(Board board:remove_list) {
			board_list.remove(board);
		}
	}
	/**
	 * 全ての手のリストを作る
	 */
	private void make_board_list() {
		board_list = Make_board_list.get_next_board_list(this);
		//それぞれのboardクラスでその局面での評価値決める
		for(Board board:board_list) {
			board.set_value_this();
		}
	}
	public void set_value_this() {
		value.calc_value_present(kyokumen,kyokumen_list,before_move);
	}
	public void output_value_test() {
		value.calc_value_present(kyokumen,kyokumen_list,before_move);
		System.out.println("評価値は:"+value.get_value());
	}
	public String get_teban_s() {
		if(get_teban()==1) {
			return "先手";
		}else if(get_teban()==2){
			return "後手";
		}
		return "error";
	}
	public String get_teban_opposite_s() {
		if(get_teban()==2) {
			return "先手";
		}else if(get_teban()==1){
			return "後手";
		}
		return "error";
	}
	public Move get_before_move() {
		return before_move;
	}
	public Value get_value() {
		return value;
	}
	public void set_before_move(Move move) {
		this.before_move = move;
	}
	public Kyokumen get_kyokumen() {
		return kyokumen;
	}
	public ArrayList<Board> get_board_list(){
		return board_list;
	}
	public int get_teban() {
		return kyokumen.get_teban();
	}
	public void set_player_sente(boolean sente) {
		this.player.set_player_sente(sente);
	}
	public void set_player_gote(boolean gote) {
		this.player.set_player_gote(gote);
	}
	public String get_player_both() {
		return this.player.get_player_both();
	}
	public boolean get_teban_player() {
		if(get_teban()==1) {
			return this.player.get_player_sente();
		}
		return this.player.get_player_gote();
	}
	public ArrayList<Kyokumen> get_kyokumen_list(){
		return kyokumen_list;
	}
	public Kihu get_kihu() {
		return kihu;
	}
	public void save_kihu(String file_name) {
		kihu.save_kihu_from_first_kyokumen(file_name);
	}
	public void delete_last_kyokumen_list() {
		kyokumen_list.remove(kyokumen_list.size()-1);
	}
	/**
	 * cpが含まれていればtrue
	 * @return
	 */
	public boolean contain_cp() {
		return player.contain_cp();
	}
	public void set_kyokumen(Kyokumen kyokumen) {
		this.kyokumen = kyokumen;
	}
	public void set_kihu(Kihu kihu) {
		this.kihu = kihu;
	}
}
