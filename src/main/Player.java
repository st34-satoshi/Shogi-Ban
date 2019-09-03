package main;

import java.util.Scanner;

/**
 * 先手後手が人かコンピュータか保存する
 * @author satoshi
 *
 */
public class Player {
	private boolean player_sente;//trueならコンピュータ
	private boolean player_gote;
	public Player() {
		player_sente = false;
		player_gote = false;
	}

	/**
	 * 先手と後手のプレイヤー決める。
	 * コンピュータか人か。
	 * いつでも変えられるようにする。
	 * @return
	 */
	public void decide_player(){
		//先手後手のプレイヤーが人かコンピューターか入力させる
		Scanner scan = new Scanner(System.in);
		String str;
		//先手決める
		while (true) {
			System.out.println("先手番を入力してください。人:human,コンピューター:cp");
			str = scan.next();
			if(str.equals("human")) {
				player_sente = false;
				break;
			}else if(str.equals("cp")) {
				player_sente = true;
				break;
			}else {
				System.out.println("入力が正しくありません!");
			}
		}
		//後手決める
		while (true) {
			System.out.println("後手番を入力してください。人:human,コンピューター:cp");
			str = scan.next();
			if(str.equals("human")) {
				player_gote = false;
				break;
			}else if(str.equals("cp")) {
				player_gote = true;
				break;
			}else {
				System.out.println("入力が正しくありません!");
			}
		}
		//scan.close();
		output_player();
	}
	public void output_player() {
		if(player_sente) {
			System.out.println("先手番はコンピュータです。");
		}else {
			System.out.println("先手番は人間です。");
		}
		if(player_gote) {
			System.out.println("後手番はコンピュータです。");
		}else {
			System.out.println("後手番は人間です。");
		}
	}
	public String get_player_both() {
		return get_player_sente_string() + get_player_gote_string();
	}
	private String get_player_sente_string() {
		if(player_sente) {
			return "先手番はコンピュータです。";
		}else {
			return "先手番は人間です。";
		}
	}
	private String get_player_gote_string() {
		if(player_gote) {
			return "後手番はコンピュータです。";
		}else {
			return "後手番は人間です。";
		}
	}
	public boolean get_player_sente() {
		return player_sente;
	}
	public boolean get_player_gote() {
		return player_gote;
	}
	public boolean get_player(int teban) {
		if(teban==1) {
			return player_sente;
		}else if(teban==2) {
			return player_gote;
		}else {
			System.out.println("error:Player get_player");
			return false;
		}
	}
	public void set_player_sente(boolean player_sente) {
		this.player_sente = player_sente;
	}
	public void set_player_gote(boolean player_gote) {
		this.player_gote = player_gote;
	}
	/**
	 * cpが含まれていればtrue
	 * @return
	 */
	public boolean contain_cp() {
		return player_sente || player_gote;
	}

}
