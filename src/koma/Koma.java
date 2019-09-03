package koma;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import board.Kyokumen;

/*
 * 駒のクラス。自分の状態を保存する
 */

public class Koma {

	private int teban;//どちらの駒か。1:先手　2:後手
	private int place_a;
	private int place_b;
	private int point;//何点の価値があるか,一般的に,動ける場所多いとポイントあがる、いらない？
	private int point_special;//特別なポイント、攻めに重要とか、守りに重要とか
	private ArrayList<Integer> move_place;//動ける場所,持ち駒の時は打てる場所,76とか.place分けられない？
	private boolean motigoma;//持ち駒でなければfalse

	public Koma(int teban_n) {
		teban = teban_n;
		move_place = new ArrayList<Integer>();
		motigoma = false;
		place_a = 0;
		place_b = 0;
		point = 0;
		point_special = 0;
	}
	public Koma clone() {
		Koma koma_clone = Calc_koma.make_koma(get_koma_number(), get_teban());
		koma_clone.set_place(get_place_a(), get_place_b());
		koma_clone.set_point(get_point());
		koma_clone.set_point_special(get_point_special());
		koma_clone.set_move_place(get_move_place_clone_deep());
		koma_clone.set_motigoma(get_motigoma());
		return koma_clone;
	}
	/**
	 * @param koma 比較する駒
	 * @return true:同じ,false:違う
	 */
	public boolean equal(Koma koma) {
		if(koma==null) {
			return false;
		}
		if(this.teban != koma.get_teban()) {
			return false;
		}
		if(this.place_a != koma.get_place_a()) {
			return false;
		}
		if(this.place_b != koma.get_place_b()) {
			return false;
		}
		if(this.motigoma != koma.get_motigoma()) {
			return false;
		}
		return true;
	}
	public void save_file(FileWriter file_writer) {
		try {
			file_writer.write(teban + "," + place_a + "," + place_b + "," + get_koma_number() + "\n");
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}
	/**
	 * 動けるか。反則にならないか。歩香桂だけ
	 * 反則でなければfalse,反則ならtrue
	 * @return
	 */
	public boolean able_to_move() {
		return false;
	}
	/**
	 * 不成の駒作る
	 * おかしい時は自分を返す
	 * @return
	 */
	public Koma get_narazukoma() {
		if(get_koma_number()>10) {
			Koma narazu_koma = Calc_koma.make_koma(get_koma_number()-10, get_teban());
			narazu_koma.set_place(get_place_a(), get_place_b());
			return narazu_koma;
		}
		System.out.println("error: Koma get_narazukoma not narigoma");
		return this;
	}
	/**
	 * 成り駒を返す。
	 * しっかり成り駒を作る。
	 * @return
	 */
	public Koma get_narigoma() {
		int n = get_koma_number();
		Koma koma = Calc_koma.make_koma(n+10, get_teban());
		koma.set_place(get_place_a(), get_place_b());
		return koma;
	}
	/**
	 * なれる駒はtrueを返すようにする。
	 * @return
	 */
	public boolean able_to_naru() {
		return false;
	}
	/**
	 * String 全角2文字で返す。スペースは左にする。
	 * @return
	 */
	public String get_koma_name() {
		return "　駒";
	}
	private String get_teban_s() {
		if(teban==1) {
			return "↑";
		}else {
			return "↓";
		}
	}
	/**
	 * 全角2,半角1,↑↓,半角スペース1で出力する。
	 */
	public void output_koma() {
		System.out.print(get_koma_name() + get_teban_s() + " ");
		///System.out.print(get_koma_name()+get_place() + get_teban_s() + " ");///
	}
	/**
	 * テスト用のアウトプット
	 */
	public void output_test() {
		System.out.println(get_koma_name() + get_teban_s() + " " +get_place());
	}
	public void move_place_clear() {
		move_place.clear();
	}
	/**
	 * 動ける場所を決める
	 * 反則は確認しない
	 * 継承する子クラスで
	 */
	public void decide_move_place(Kyokumen kyokumen) {
		//
	}
	/**
	 * 香車などずっと動ける奴の動き
	 * その方向のマスをmove_placeに加える。
	 */
	public void move_place_more(Kyokumen kyokumen,int dif_a,int dif_b) {
		int a = get_place_a() + dif_a;
		int b = get_place_b() + dif_b;
		Koma koma = null;
		while(Calc_koma.isBan(a, b)) {
			koma = kyokumen.get_banarray(a, b);
			if(koma != null) {
				if(koma.get_teban() == get_teban()) {
					//自分の駒がある。
					break;
				}else {
					//相手の駒がある。
					add_move_place(a*10+b);
					break;
				}
			}else {
				//前に駒がない
				add_move_place(a*10+b);
				b += dif_b;
				a += dif_a;
			}
		}

	}
	/**
	 * placeがmove_placeに含まれるか
	 * @param place
	 * @return
	 */
	public boolean contain_move_place(int place) {
		return move_place.contains(place);
	}
	//自分のいる場所、駒の名前、動ける場所を出力
	public void output_move_place() {
		System.out.print(get_koma_name() + get_place_a() + "" + get_place_b()
		+ "move_place:");
		for(int i:move_place) {
			System.out.print(i+",");
		}
		System.out.println("");
	}
	public void set_teban(int teban_n) {
		teban = teban_n;
	}
	public int get_teban() {
		return teban;
	}
	public void set_place(int a,int b) {
		this.place_a = a;
		this.place_b = b;
	}
	public void set_point(int point_n) {
		point = point_n;
	}
	public int get_point() {
		return point;
	}
	public void set_point_special(int point_special_n) {
		point_special = point_special_n;
	}
	public int get_point_special() {
		return point_special;
	}
	public void set_move_place(ArrayList<Integer> move_place) {
		this.move_place = move_place;
	}
	public  ArrayList<Integer> get_move_place() {
		return move_place;
	}
	public  ArrayList<Integer> get_move_place_clone_deep() {
		ArrayList<Integer> move_place_clone = new ArrayList<Integer>();
		for(Integer i:move_place) {
			move_place_clone.add(i);
		}
		return move_place_clone;
	}
	public void add_move_place(int place) {
		move_place.add(place);
	}
	public int get_place_b() {
		return place_b;
	}
	public int get_place_a() {
		return place_a;
	}
	public int get_koma_number() {
		return 0;
	}
	public void set_motigoma(boolean boo) {
		this.motigoma = boo;
	}
	public boolean get_motigoma() {
		return motigoma;
	}
	public int get_place() {
		return place_a*10+place_b;
	}
	public int get_point_teban() {
		if(teban==1) {
			return get_point();
		}else if(teban==2) {
			return -1 * get_point();
		}
		System.out.println("error: Koma get_point_teban teban");
		return 0;
	}
	public void change_teban() {
		if(teban==1) {
			teban = 2;
		}else if(teban==2) {
			teban = 1;
		}else {
			System.out.println("error: Koma change_teban teban");
		}
	}

}
