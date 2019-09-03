package main;

public class Calc {
	/*
	 * 文字列が数字かどうかを判断
	 */
	public static boolean isNumber(String str) {
		try {
			Integer.parseInt(str);
			return true;
		}catch(NumberFormatException e) {
			return false;
		}
	}
	public static int change_teban(int teban) {
		//手番を変える1なら2,2なら1にする
		if (teban==1) {
			return 2;
		}else if (teban==2) {
			return 1;
		}
		System.out.println("error:Calc change_teban");
		return 1;
	}

}
