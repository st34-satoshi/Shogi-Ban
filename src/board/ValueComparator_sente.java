package board;

import java.util.Comparator;

import calc_value.Value;

public class ValueComparator_sente implements Comparator<Board> {

	/**
	 * b1がよければ1,b2がよければ-1返す。
	 */
	@Override
	public int compare(Board b1, Board b2) {
		Value v1 = b1.get_value();
		Value v2 = b2.get_value();
		//勝ちが決まっているならどちらでも良い?
		if(v1.get_determe() && v2.get_determe()) {
			if(v1.get_value() < v2.get_value()) {
				return -1;
			}if(v1.get_value() == v2.get_value()) {
				return 0;
			}
			return 1;
		}
		if(v1.get_determe()) {
			if(v1.get_value() > 0) {
				return 1;
			}else if(v1.get_value() < 0){
				return -1;
			}else {
				//千日手
				if(v1.get_value() < v2.get_value()) {
					return -1;
				}else if(v1.get_value() == v2.get_value()) {
					return 0;
				}
				return 1;
			}
		}
		if(v2.get_determe()) {
			if(v2.get_value() > 0) {
				return -1;
			}else if(v2.get_value() < 0) {
				return 1;
			}else {
				//千日手
				if(v1.get_value() < v2.get_value()) {
					return -1;
				}else if(v1.get_value() == v2.get_value()) {
					return 0;
				}
				return 1;
			}
		}
		if(v1.get_value() < v2.get_value()) {
			return -1;
		}else if(v1.get_value() == v2.get_value()) {
			return 0;
		}
		return 1;
	}
	/**
	 * b1が大きければtrue
	 * 同じときは気にしていない
	 */
	public static boolean cmpare_board_sente(Board b1,Board b2) {
		Value v1 = b1.get_value();
		Value v2 = b2.get_value();
		//勝ちが決まっているならどちらでも良い?
		if(v1.get_determe()) {
			if(v1.get_value() > 0) {
				return true;
			}else if(v1.get_value() < 0){
				return false;
			}else {
				//千日手
				if(v1.get_value() < v2.get_value()) {
					return false;
				}
				return true;
			}
		}
		if(v2.get_determe()) {
			if(v2.get_value() > 0) {
				return false;
			}else if(v2.get_value() < 0) {
				return true;
			}else {
				//千日手
				if(v1.get_value() < v2.get_value()) {
					return false;
				}
				return true;
			}
		}
		if(v1.get_value() < v2.get_value()) {
			return false;
		}
		return true;

	}

}
