package main;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import board.Board;
import board.Kyokumen;
import board.Move;

/**
 * @author satoshi
 *
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException{
		System.out.println("対局を始めます。");
		Kyokumen kyokumen = new Kyokumen();
		/*
		if(!kyokumen.load_kyokumen("kyokumen.txt")) {
			return;
		}
		*/
		//先手後手のプレイヤー決める
		Player player = new Player();
		player.decide_player();
		//file_write();
		ArrayList<Kyokumen> kyokumen_list = new ArrayList<Kyokumen>();
		Board board = new Board(kyokumen,player,kyokumen_list);
		//board.output_kyokumen();
		Kihu kihu = new Kihu();
		kihu.input_kihu("inputhu.txt");//棋譜をインプットするとき
		//kihu.output_kihu();
		kyokumen_list.add(board.get_kyokumen().clone());
		for(Move move:kihu.get_kihu_list()) {
			board.move_next_kyokumen(move);
			kyokumen_list.add(board.get_kyokumen().clone());
		}
		kyokumen_list.remove(kyokumen_list.size()-1);
		//対局中。コンピュータと人間を変えたりするときは対局を中断か投了かしないとできない。
		int i = 1;
		while(i<1000) {
			System.out.println(i+"手目の入力をしてください。"+board.get_teban_s());
			//全ての駒のmove_placeを決める。
			board.decide_all_koma_move_place();
			board.output_kyokumen();
			//反則チェック
			if(board.check_foul()) {
				System.out.println(board.get_teban_opposite_s()+"の反則負けです。");
				if(check_yes("戻りますか?")) {
					//まったする
					Move back_move = kihu.get_last_move();
					kihu.remove_last_move();
					board.move_back_kyokumen(back_move);
					continue;
				}else {
					break;
				}
			}
			//千日手チェック,同じ局面4回目なら千日手で終了
			int sennitite = board.check_sennitite(kyokumen_list);
			if(sennitite != 0) {
				if(sennitite==1) {
					System.out.println("千日手により引き分けです。");
				}else if(sennitite==2) {
					System.out.println("連続王手による千日手で"+board.get_teban_s()+"の反則負けです。");
				}else if(sennitite==3) {
					System.out.println("連続王手による千日手で"+board.get_teban_opposite_s()+"の反則負けです。");
				}
				if(check_yes("戻りますか?")) {
					//まったする
					Move back_move = kihu.get_last_move();
					kihu.remove_last_move();
					board.move_back_kyokumen(back_move);
					continue;
				}else {
					break;
				}
			}
			//kyokumen_listに局面を追加
			kyokumen_list.add(board.get_kyokumen().clone());
			//対局終了、中断、投了、まった(二手戻る),次の手の入力と同じところでできるとより良い,コンピュータ同士の対局では邪魔
			int check_int = check_interruption();//対局を続けるなら1、待ったなら2、投了3,局面評価4
			//int check_int = 1;
			if(check_int==2) {
				if(i<2) {
					System.out.println("error: Main back i");
					break;
				}
				//待った、二手戻る。
				Move back_move = kihu.get_last_move();
				kihu.remove_last_move();
				board.move_back_kyokumen(back_move);
				back_move = kihu.get_last_move();
				kihu.remove_last_move();
				board.move_back_kyokumen(back_move);
				i -= 2;
			}else if(check_int==1){
				//次の手
				board = board.go_next_board();
				if(board == null) {
					System.out.println("負けました。");
					break;
				}
				//棋譜を保存する
				kihu.add_move(board.get_before_move());
				System.out.println("指した手は以下です。");
				board.get_before_move().output_move_kihu();///
				//kihu.output_kihu();
				i++;
			}else if(check_int==3) {
				//投了
				System.out.println("負けました。");
				break;
			}else if(check_int==4) {
				///test
				///board.test();
				///System.out.println("局面の評価値を出力します。");
				///board.output_value_test();
				/*
				//局面を評価
				System.out.println("評価値を求めます。");
				Count_depth cd = new Count_depth();
				Calc_value cv = new Calc_value(kyokumen,cd);
				//int depth_main = cd.get_depth_main();
				cv.calc_value_depth();
				//cv.calc_value_present();
				//cd.set_depth_main(depth_main);
				System.out.println("評価値:"+cv.get_value_real());
				*/
			}else if(check_int==5) {
				//局面保存
				String save_file = "kyokumen.txt";
				///System.out.println("局面を"+save_file+"に保存します。");
				board.save_kyokumen(save_file);
			}
		}
		//棋譜出力確認
		check_output_kihu(kihu);
		System.out.println("プログラムを終了します。");
	}
	/**
	 * 対局中断なら,対局を続けるなら1、待ったなら2、投了3,局面評価4
	 * @return
	 */
	private static int check_interruption() {
		Scanner scan = new Scanner(System.in);
		String str;
		while(true) {
			System.out.println("対局を続けますか？ n:次の手, int:中断, back:待った, loss:負けました。calc:局面評価, save:局面保存");
			str = scan.next();
			if(str.equals("n")) {
				return 1;
			}else if(str.equals("back")) {
				//待った、行って戻る
				return 2;
			}else if(str.equals("int")) {
				///
			}else if(str.equals("loss")) {
				return 3;
			}else if(str.equals("calc")) {
				return 4;
			}else if(str.equals("save")) {
				return 5;
			}
			System.out.println("入力が違います。");
		}
		///return false;
	}
	/**
	 * 棋譜を出力するか確認する
	 * @param kihu
	 */
	public static void check_output_kihu(Kihu kihu) {
		String save_file = "output.txt";
		String output_str = "棋譜を出力(保存)しますか？"+save_file;
		if(check_yes(output_str)) {
			//棋譜保存
			//kihu.output_kihu();
			kihu.save_kihu_file(save_file);
		}
	}
	/**
	 * yes,noを確認してyesならtrue
	 * noならfalseで返す
	 * 確認の文章をStringで受け取る
	 * @return
	 */
	public static boolean check_yes(String output_str) {
		Scanner scan = new Scanner(System.in);
		String str;
		while(true) {
			System.out.println(output_str);
			System.out.println("y:yes,n:no を入力してください。");
			str = scan.next();
			if(str.equals("y")) {
				return true;
			}else if(str.equals("n")) {
				return false;
			}
			System.out.println("入力が正しくありません。");
		}
	}
	/**
	 * テスト用
	 * @throws IOException
	 */
	public static void file_write() throws IOException {
		File file = new File("testwrite.txt");
		FileWriter filewriter = new FileWriter(file);
		filewriter.write("hahaha");
		filewriter.close();
	}
}
