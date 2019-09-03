package koma;

import main.Calc;

/**
 * 駒を作るクラス
 * 番号から駒を作る
 * @author satoshi
 *
 */
public class Calc_koma {
	/**
	 * ファイルから局面を読み込んだ時の読み込み
	 * @param load_str
	 */
	public static Koma make_load_Koma(String load_str) {
		String[] str = load_str.split(",", 0);
		if(str.length != 4) {
			System.out.println("駒の保存の長さが違います。");
			return null;
		}
		//手番
		int teban = change_teban_int_from_string(str[0]);
		if(teban == 0) {
			return null;
		}
		//place
		int a = -1;
		int b = -1;
		if(Calc.isNumber(str[1])) {
			a = Integer.parseInt(str[1]);
		}else {
			return null;
		}
		if(Calc.isNumber(str[2])) {
			b = Integer.parseInt(str[2]);
		}else {
			return null;
		}
		if(!isBan(a,b)) {
			return null;
		}
		int n = -1;
		if(Calc.isNumber(str[3])) {
			n = Integer.parseInt(str[3]);
		}else {
			return null;
		}
		Koma koma = make_koma(n, teban);
		if(koma == null) {
			return null;
		}
		koma.set_place(a, b);
		return koma;
	}
	private static int change_teban_int_from_string(String str) {
		if(str.equals("1")) {
			return 1;
		}else if(str.equals("2")) {
			return 2;
		}
		return 0;
	}
	/**
	 * 番号から駒を作る。
	 * @param n
	 * @return
	 */
	public static Koma make_koma(int n,int teban) {
		///
		Koma koma = null;
		if(n==1) {
			koma = new Hu(teban);
		}else if(n==2) {
			koma = new Kyou(teban);
		}else if(n==3) {
			koma = new Kei(teban);
		}else if(n==4) {
			koma = new Gin(teban);
		}else if(n==5) {
			koma = new Kin(teban);
		}else if(n==6) {
			koma = new Gyoku(teban);
		}else if(n==7) {
			koma = new Kaku(teban);
		}else if(n==8) {
			koma = new Hisha(teban);
		}else if(n==11) {
			koma = new To(teban);
		}else if(n==12) {
			koma = new Narikyou(teban);
		}else if(n==13) {
			koma = new Narikei(teban);
		}else if(n==14) {
			koma = new Narigin(teban);
		}else if(n==17) {
			koma = new Uma(teban);
		}else if(n==18) {
			koma = new Ryu(teban);
		}
		return koma;
	}

	/**
	 * a*10+bが11-99の中にあるならtrue
	 * @param a
	 * @param b
	 * @return
	 */
	public static boolean isBan(int a,int b) {
		if(a>0 && a<10 && b>0 && b<10) {
			return true;
		}else {
			return false;
		}
	}
	/**
	 * a*10+bが11-99の中にあるならtrue
	 * 入力は76とか
	 * @param a
	 * @param b
	 * @return
	 */
	public boolean isBan(int place) {
		int a = place /10;
		int b = place % 10;
		return isBan(a,b);
	}


}
