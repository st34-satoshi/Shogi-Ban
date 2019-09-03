package board;

import java.util.ArrayList;

import calc_value.Tumi_check;
import koma.Koma;
import main.Calc;

/**
 * kyokumen に反則がないかを調べるクラス。千日手も調べる
 * 本来はkyokumenクラスに書きたいが、長く成るのでこのクラスを作った
 * @author satoshi
 *
 */
public class Calc_kyokumen_foul {
	/**
	 * 千日手がかどうかを調べる
	 * kyokumen_listにkyokumenは入っている?
	 * @return 0:千日手ではない,1:千日手,2;連続王手の千日手手番が王手している,3:連続王手の千日手手番が王手されている
	 */
	public static int check_sennitite(ArrayList<Kyokumen> kyokumen_list,Kyokumen kyokumen) {
		//kyokumen_listに同じ局面があるか調べる。
		if(kyokumen_list==null || kyokumen_list.isEmpty()) {
			return 0;
		}
		int same_kyokumen_count = 0;
		for(Kyokumen k:kyokumen_list) {
			if(k.equal(kyokumen)) {
				same_kyokumen_count++;
			}
		}
		if(same_kyokumen_count<4) {
			return 0;
		}
		for(Kyokumen k:kyokumen_list) {
			k.output_kyokumen();
		}
		//連続王手が無いか調べる,手番の玉に王手がかかっているか
		if(check_oute(kyokumen, kyokumen.get_teban())) {
			//手番の玉に王手がかかっている。2手ずつ戻って王手が連続しているか調べる。
			int number = kyokumen_list.size()-2;
			Kyokumen kyokumen_s = null;
			while(true) {
				kyokumen_s = kyokumen_list.get(number);
				if(kyokumen_s.equal(kyokumen)){
					//連続王手のまま一つ前の同じ局面まで来ている
					return 3;
				}
				if(!check_oute(kyokumen_s, kyokumen.get_teban())) {
					//王手がかかっていない局面があれば連続王手では無い
					break;
				}
				number = number - 2;
				if(number < 0) {
					System.out.println("error: check_sennitite sennitite but no same kyokumen");
					return 0;
				}
			}
		}
		//連続王手、手番が王手している。
		int number = kyokumen_list.size()-1;//一つ前の局面
		Kyokumen kyokumen_first = kyokumen_list.get(number);
		if(check_oute(kyokumen_first, kyokumen_first.get_teban())) {
			Kyokumen kyokumen_s = null;
			number = number - 2;
			while(true) {
				kyokumen_s = kyokumen_list.get(number);
				if(kyokumen_s.equal(kyokumen_first)){
					//連続王手のまま一つ前の同じ局面まで来ている
					return 2;
				}
				if(!check_oute(kyokumen_s, kyokumen_s.get_teban())) {
					//一つ前の局面で相手玉に王手がかかっていない。
					break;
				}
				number = number - 2;
				if(number < 0) {
					System.out.println("error: check_sennitite sennitite but no same kyokumen");
					return 0;
				}
			}
		}
		return 1;
	}
	/**
	 * 今の局面に反則がないか確認する
	 * 反則がなければfalse,反則があれば(局面としておかしければ)true返す
	 */
	public static boolean check_foul_all(Kyokumen kyokumen,Move before_move) {
		if(check_koma_number(kyokumen)) {
			//System.out.println("foul koma number!!");
			///kyokumen.output_kyokumen();///
			return true;
		}
		if(check_dontmove(kyokumen)) {
			//System.out.println("foul!! there are koma that dont move!!");
			return true;
		}
		if(check_nihu(kyokumen)) {
			//System.out.println("there are nihu!!");
			return true;
		}
		//王手放置は反則
		if(check_oute_ignore(kyokumen)) {
			//System.out.println("oute ignore!!");
			return true;
		}
		//打ち歩詰め
		if(before_move!=null) {
			if(check_utihuzume(kyokumen, before_move)) {
				//System.out.println("utihuzume!!");
				return true;
			}
		}
		return false;

	}
	/**
	 * その局面が打ち歩詰めになっているか。打ち歩詰めなら反則。
	 * @param kyokumen
	 * @param before_move
	 * @return打ち歩詰めならtrue,普通はfalse
	 */
	public static boolean check_utihuzume(Kyokumen kyokumen,Move before_move) {
		//最後に歩を打っているか確認する
		if(before_move.get_before_place_b()==1) {
			if(before_move.get_before_place_a()==10 || before_move.get_before_place_a()==20) {
				//最後に歩を打っている。その局面で手番の玉が詰んでいるか調べる
				///詰みチェックをしなくてはいけない///
				if(Tumi_check.check_tumi_teban(kyokumen)) {
					return true;
				}
			}
		}
		return false;
	}
	/**
	 * 手番に王手がかかっているか確認し、王手がかかって入ればtrue
	 * 王手がなければfalse
	 * @return
	 */
	public static boolean check_oute(Kyokumen kyokumen,int teban) {
		int place_gyoku = kyokumen.get_place_gyoku(teban);
		int teban_r = Calc.change_teban(teban);
		ArrayList<Koma> my_koma_list = kyokumen.get_teban_koma_list_all(teban_r);
		for(Koma koma:my_koma_list) {
			for(int place:koma.get_move_place()) {
				if(place==place_gyoku) {
					return true;
				}
			}
		}
		return false;
	}
	/**
	 * 王手放置は反則負け
	 * 王手放置をして入ればtrue
	 * 相手の玉に王手がかかって入ればtrue
	 * @return
	 */
	private static boolean check_oute_ignore(Kyokumen kyokumen) {
		int teban_r = Calc.change_teban(kyokumen.get_teban());
		return check_oute(kyokumen,teban_r);
	}
	/**
	 * コマの数を数える。
	 * 数があっていればfalse,おかしければtrue
	 * コマ落ちも使うならあってはいけないメソッド。コマの枚数が変わることはありえないので正しく動くようになればいらない
	 */
	private static boolean check_koma_number(Kyokumen kyokumen) {
		int i,j;
		int[] count_koma = new int[9];
		for(i=0;i<9;i++) {
			count_koma[i] = 0;//初期化
		}
		for(i=0;i<9;i++) {
			count_koma[i] += kyokumen.get_sente_komadai().get_koma_number(i);
			count_koma[i] += kyokumen.get_gote_komadai().get_koma_number(i);//駒台の駒数える
		}
		Koma koma = null;
		int n = 0;
		for(i=1;i<10;i++) {
			for(j=1;j<10;j++) {
				koma = kyokumen.get_koma_from_place(i, j);
				if(koma!=null) {
					n = koma.get_koma_number() % 10;
					count_koma[n] += 1;
				}
			}
		}
		if(count_koma[1] != 18) {
			System.out.println("check_foul error count hu");
			return true;
		}else if(count_koma[2] != 4) {
			System.out.println("check_foul error count kyou");
			return true;
		}else if(count_koma[3] != 4) {
			System.out.println("check_foul error count kei");
			return true;
		}else if(count_koma[4] != 4) {
			System.out.println("check_foul error count gin");
			return true;
		}else if(count_koma[5] != 4) {
			System.out.println("check_foul error count kin");
			return true;
		}else if(count_koma[6] != 2) {
			System.out.println("check_foul error count gyoku");
			return true;
		}else if(count_koma[7] != 2) {
			System.out.println("check_foul error count kaku");
			return true;
		}else if(count_koma[8] != 2) {
			System.out.println("check_foul error count hisha");
			return true;
		}
		return false;
	}

	/**
	 * 動かせない駒(歩、香、桂)があるかチェック
	 * なければfalse,あればtrue
	 */
	private static boolean check_dontmove(Kyokumen kyokumen) {
		for(Koma koma:kyokumen.get_koma_array()) {
			if(koma.able_to_move()) {
				return true;//一つでも反則あればダメ。
			}
		}
		return false;
	}

	/**
	 * 二歩チェック
	 * 反則なければfalse,二歩あればtrue
	 */
	private static boolean check_nihu(Kyokumen kyokumen) {
		//二歩チェック
		int[] sente = {0,0,0,0,0,0,0,0,0,0};
		int[] gote = {0,0,0,0,0,0,0,0,0,0};
		for(Koma koma:kyokumen.get_koma_array()) {
			if(koma.get_koma_number()==1) {
				int n = koma.get_place_a();
				if(koma.get_teban()==1) {
					sente[n] += 1;
				}else {
					gote[n] += 1;
				}
			}
		}
		for(int i=0;i<10;i++) {
			if(sente[i]>1 || gote[i]>1) {
				return true;//二歩
			}
		}
		return false;
	}
}
