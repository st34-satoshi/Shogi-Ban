package board;

import java.util.Scanner;

import koma.Koma;
import main.Calc;

/**
 * 動いた値を反対からも読めるように保存
 */
public class Move{
	private int before_place_a;//76の7,駒台なら10,20
	private int before_place_b;//76の6
	private int after_place_a;
	private int after_place_b;
	private boolean naru;//trueでなった。
	private int get_koma;//取っていなければ0

	public Move() {
		before_place_a = 0;
		before_place_b = 0;
		after_place_a = 0;
		after_place_b = 0;
		naru = false;
		get_koma = 0;
	}
	public Move(Move move) {
		this.before_place_a = move.get_before_place_a();
		this.before_place_b = move.get_before_place_b();
		this.after_place_a = move.get_after_place_a();
		this.after_place_b = move.get_after_place_b();
		this.naru = move.get_naru();
		this.get_koma = move.get_get_koma();
	}
	public Move(int before_a,int before_b,int after_a,int after_b) {
		set_before_place_a( before_a);
		set_before_place_b(before_b);
		set_after_place_a(after_a);
		set_after_place_b(after_b);
		naru = false;
		get_koma = 0;
	}
	public Move clone() {
		return new Move(this);
	}
	/**
	 * moveが等しいか判定
	 * @param move
	 * @return 同じならtrue
	 */
	public boolean equals_move(Move move) {
		if(this.before_place_a==move.get_before_place_a() && this.before_place_b==move.get_before_place_b() &&
				this.after_place_a==move.get_after_place_a() && this.after_place_b==move.get_after_place_b() &&
				this.get_koma==move.get_get_koma()) {
			if(this.naru && move.get_naru()) {
				return true;
			}
			if(!this.naru && !move.get_naru()) {
				return true;
			}
		}
		return false;
	}
	/**
	 * 駒がなれるか判断。なれる場合はどっちにするか確認,人間用
	 * 成る時はtrue返す
	 * @param koma
	 * @return
	 */
	public boolean check_naru(Koma koma) {
		//持ち駒からは成れない
		if(koma.get_motigoma()) {
			return false;
		}
		if(koma.able_to_naru()) {
			if(able_naru(koma.get_teban())) {
				//なれる
				Scanner scan = new Scanner(System.in);
				String str;
				while(true) {
					System.out.println("成りますか？　成る:y 不成:n");
					str = scan.next();
					if(str.equals("y")) {
						///scan.close();
						return true;//koma.get_narigoma();
					}else if(str.equals("n")) {
						///scan.close();
						return false;
					}
					System.out.println("入力が違います。");
				}
			}
		}
		return false;
	}
	/**
	 * なれるかどうかの判断
	 * なれるならtrue
	 * 駒の確認も必要
	 * @return
	 */
	public boolean able_naru(int teban) {
		if(teban==1) {
			if(before_place_b<4 || after_place_b<4) {
				return true;
			}
		}else {
			if(before_place_b>6 || after_place_b>6) {
				return true;
			}
		}
		return false;
	}
	public boolean check_able_naru(Koma koma) {
		//持ち駒からは成れない
		if(koma.get_motigoma()) {
			return false;
		}
		if(koma.able_to_naru()) {
			if(koma.get_teban()==1) {
				if(before_place_b<4 || after_place_b<4) {
					return true;
				}
			}else {
				if(before_place_b>6 || after_place_b>6) {
					return true;
				}
			}
		}
		return false;
	}
	/**
	 * moveの出力。テストで使う。///
	 * 棋譜保存の時も使う
	 */
	public void output_move_test() {
		int n = 0;//不成
		if(naru) {
			n = 1;//成る
		}
		System.out.println(before_place_a+","+before_place_b+","+after_place_a+","+after_place_b+","
		+n+","+get_koma+","+get_koma);
		//System.out.println("");
	}
	/**
	 * 棋譜を保存するときのアウトプット
	 */
	public void output_move_kihu() {
		int n = 0;//不成
		if(naru) {
			n = 1;//なったとき
		}
		System.out.println(before_place_a+","+before_place_b+","+after_place_a+","+
				after_place_b+","+n+","+get_koma);
	}
	/**
	 * 棋譜を保存するときの改行を含む文字列を返す
	 * @return
	 */
	public String get_move_kihu() {
		int n = 0;//不成
		if(naru) {
			n = 1;//なったとき
		}
		String ans = before_place_a+","+before_place_b+","+after_place_a+","+
				after_place_b+","+n+","+get_koma + "\n";
		return ans;
	}
	/**
	 * moveの入力。
	 */
	public void input_move() {
		Scanner scan = new Scanner(System.in);
		String str;
		//beforeの入力
		while(true) {
			System.out.println("動かす駒の今の場所を入力してください。");
			str = scan.next();
			if(Calc.isNumber(str)) {
				int before = Integer.parseInt(str);
				before_place_a = before / 10;
				before_place_b = before % 10;
				break;
			}
			System.out.println("入力が数字ではありません。");
		}
		//afterの入力
		while(true) {
			System.out.println("駒の動かす先の場所を入力してください。");
			str = scan.next();
			if(Calc.isNumber(str)) {
				int after = Integer.parseInt(str);
				after_place_a = after / 10;
				after_place_b = after % 10;
				break;
			}
			System.out.println("入力が数字ではありません。");
		}
		///scan.close();
	}
	/**
	 * ファイルから読み込むときに使う。
	 * 一行ずつ
	 * 新しくmoveクラス作って返す
	 */
	public Move get_input_move(String str) {
		String[] input = str.split(",", 0);
		if(input.length != 6) {
			System.out.println("error: Move get_input_move1"+str);
			return null;
		}
		int[] set_number = new int[6];
		for(int i=0;i<6;i++) {
			if(Calc.isNumber(input[i])) {
				set_number[i] = Integer.parseInt(input[i]);
			}else {
				System.out.println("error: Move get_input_move2"+input[i]);
				return null;
			}
		}
		Move move = new Move();
		move.set_before_place_a(set_number[0]);
		move.set_before_place_b(set_number[1]);
		move.set_after_place_a(set_number[2]);
		move.set_after_place_b(set_number[3]);
		if(set_number[4]==1) {
			move.set_naru(true);
		}else if(set_number[4]==0) {
			move.set_naru(false);
		}else {
			System.out.println("error: Move get_input_move naru"+set_number[4]);
			return null;
		}
		move.set_get_koma(set_number[5]);
		return move;
	}
	/**
	 * 入力と同じ場所がafter_placeか
	 * @return
	 */
	public boolean match_after_place(int a,int b) {
		if(a==after_place_a && b==after_place_b) {
			return true;
		}
		return false;
	}
	public boolean match_after_place(int place) {
		int a = place /10;
		int b = place % 10;
		return match_after_place(a, b);
	}
	public int get_before_place_a() {
		return before_place_a;
	}
	public int get_before_place_b() {
		return before_place_b;
	}
	public int get_after_place_a() {
		return after_place_a;
	}
	public int get_after_place_b() {
		return after_place_b;
	}
	public void set_before_place_a(int before_a) {
		if(before_a==10 || before_a==20 || (0<before_a && before_a<10) ) {
			this.before_place_a = before_a;
		}
	}
	public void set_before_place_b(int before_b) {
		if(before_b==10 || before_b==20 || (0<before_b && before_b<10) ) {
			this.before_place_b = before_b;
		}
	}
	public void set_place(int before_a,int before_b,int after) {
		set_before_place_a( before_a);
		set_before_place_b(before_b);
		set_after_place(after);
	}
	public void set_after_place_a(int after_a) {
		if(0<after_a && after_a<10 ) {
			this.after_place_a = after_a;
		}
	}
	public void set_after_place_b(int after_b) {
		if(0<after_b && after_b<10 ) {
			this.after_place_b = after_b;
		}
	}
	public void set_after_place(int after_place) {
		int a = after_place / 10;
		int b = after_place % 10;
		set_after_place_a(a);
		set_after_place_b(b);
	}
	public boolean get_naru() {
		return naru;
	}
	public int get_get_koma() {
		return get_koma;
	}
	public void set_naru(boolean naru) {
		this.naru = naru;
	}
	public void set_get_koma(int koma) {
		this.get_koma = koma;
	}
	public int get_after_place_mix() {
		return after_place_a*10 + after_place_b;
	}
}