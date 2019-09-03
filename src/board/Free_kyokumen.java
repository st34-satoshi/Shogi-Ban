package board;

import koma.Calc_koma;
import koma.Koma;

/**
 * 局面を自由に編集できるようにするクラス。
 * @author satoshi
 *
 */
public class Free_kyokumen extends Kyokumen{
	public Free_kyokumen(Kyokumen kyokumen) {
		set_ban_komadai(kyokumen.get_ban().clone(), kyokumen.get_sente_komadai().clone(), kyokumen.get_gote_komadai().clone());
	}
	public Kyokumen get_kyokumen(int teban) {
		Kyokumen kyokumen = new Kyokumen(get_ban(),get_sente_komadai(),get_gote_komadai(),teban);
		kyokumen.set_koma_array();
		kyokumen.decide_all_koma_move_place();
		return kyokumen;
	}
	/**
	 * 駒を動かす
	 * @param before_place
	 * @param after_place
	 * 駒台に置く時は取った駒返す
	 */
	public Koma move(int before_place,int after_place) {
		//before_placeの駒はなくす。駒台なら減らす
		Koma koma = get_koma_from_place(before_place);
		if(koma == null) {
			System.out.println("error: free kyokumen move: "+before_place);
			return null;
		}
		if(before_place/10 < 10) {
			//動かす前は盤上の駒
			set_koma_from_place(null, before_place/10, before_place%10);
		}else {
			decrease_motigoma(koma);//前の場所を無くした。
		}
		//同じ場所なら成る反転させる。
		if(before_place==after_place) {
			if(koma.able_to_naru() || koma.get_koma_number()>10) {
				int koma_number = koma.get_koma_number();
				if(koma_number<10) {
					koma_number += 10;
				}else {
					koma_number -= 10;
				}
				Koma koma_n = Calc_koma.make_koma(koma_number, koma.get_teban());
				set_koma_from_place(koma_n, after_place/10, after_place%10);
			}else {
				//成れない駒の時はなにもしない
				set_koma_from_place(koma, after_place/10, after_place%10);
			}
			return null;
		}
		set_koma_from_place(koma, after_place/10, after_place%10);
		koma.set_motigoma(false);//打っているかもしれない
		if(after_place/10 < 10) {
			//盤上に移動
			return null;
		}
		return koma;
	}

}
