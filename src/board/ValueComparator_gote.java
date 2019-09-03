package board;

import java.util.Comparator;

import calc_value.Value;
public class ValueComparator_gote implements Comparator<Board>{

	@Override
	public int compare(Board b1, Board b2) {
		Value v1 = b1.get_value();
		Value v2 = b2.get_value();
		//勝ちが決まっているならどちらでも良い?,千日手の時もある
		if(v1.get_determe() && v2.get_determe()) {
			if(v1.get_value() < v2.get_value()) {
				return 1;
			}else if(v1.get_value() == v2.get_value()) {
				return 0;
			}
			return -1;
		}
		if(v1.get_determe()) {
			if(v1.get_value() < 0) {
				return 1;
			}else if(v1.get_value() > 0) {
				return -1;
			}else {
				//千日手
				if(v1.get_value() < v2.get_value()) {
					return 1;
				}else if(v1.get_value() == v2.get_value()) {
					return 0;
				}
				return -1;
			}
		}
		if(v2.get_determe()) {
			if(v2.get_value() < 0) {
				return -1;
			}else if(v2.get_value() > 0) {
				return 1;
			}else {
				//千日手
				if(v1.get_value() < v2.get_value()) {
					return 1;
				}else if(v1.get_value() == v2.get_value()) {
					return 0;
				}
				return -1;
			}
		}
		if(v1.get_value() < v2.get_value()) {
			return 1;
		}else if(v1.get_value() == v2.get_value()) {
			return 0;
		}
		return -1;
	}
	/**
	 * b1が小さければtrue
	 * 同じときは気にしていない。
	 */
	public static boolean compare_board_gote(Board b1,Board b2) {
		Value v1 = b1.get_value();
		Value v2 = b2.get_value();
		//勝ちが決まっているならどちらでも良い?
		if(v1.get_determe()) {
			if(v1.get_value() < 0) {
				return true;
			}else if(v1.get_value() > 0) {
				return false;
			}else {
				//千日手
				if(v1.get_value() < v2.get_value()) {
					return true;
				}
				return false;
			}
		}
		if(v2.get_determe()) {
			if(v2.get_value() < 0) {
				return false;
			}else if(v2.get_value() > 0) {
				return true;
			}else {
				//千日手
				if(v1.get_value() < v2.get_value()) {
					return true;
				}
				return false;
			}
		}
		if(v1.get_value() < v2.get_value()) {
			return true;
		}
		return false;
	}

}
