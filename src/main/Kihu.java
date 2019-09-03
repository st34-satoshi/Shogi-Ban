package main;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import board.Kyokumen;
import board.Move;

/**
 * 棋譜を保存するクラス
 * @author satoshi
 *
 */
public class Kihu {

	private ArrayList<Move> kihu_list;
	private Kyokumen first_kyokumen;
	private Kyokumen last_kyokumen;//棋譜を読み込んだ時に作る。保存はしない.対局が終わった時にも作る。
	public Kihu() {
		kihu_list = new ArrayList<Move>();
	}
	public Kihu(Kyokumen kyokumen) {
		kihu_list = new ArrayList<Move>();
		first_kyokumen = kyokumen.clone();
		last_kyokumen = null;
	}
	public Kihu(ArrayList<Move> kihu_list,Kyokumen first_kyokumen) {
		this.kihu_list = kihu_list;
		this.first_kyokumen = first_kyokumen;
		last_kyokumen = null;
	}
	public Kihu clone_kihu() {
		ArrayList<Move> kihu_list_clone = new ArrayList<Move>();
		for(Move move:kihu_list) {
			kihu_list_clone.add(move);
		}
		return new Kihu(kihu_list_clone,first_kyokumen.clone());
	}
	public void make_last_kyokumen() {
		Kyokumen last_k = first_kyokumen.clone();
		for(Move move:kihu_list) {
			last_k.move_next_kyokumen(move);
		}
		last_kyokumen = last_k;
	}
	/**
	 * 最初の局面から保存されている棋譜を読み込む
	 */
	public void read_kihu_from_first_kyokumen(String input_file) {
		System.out.println("棋譜を読み込みます。最初の局面が保存されている。");
		kihu_list.clear();
		Move move = new Move();
		Move input_move = null;
		File file = new File(input_file);
		try {
			FileReader filereader = new FileReader(file);
			BufferedReader br = new BufferedReader(filereader);
			first_kyokumen.read_kyokumen_from_first_kyokumen(br);
			String str = br.readLine();
			while(str != null) {
				input_move = move.get_input_move(str);
				if(input_move == null) {
					break;
				}else {
					kihu_list.add(input_move);
				}
				str = br.readLine();
			}
			br.close();
			make_last_kyokumen();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}
	/**
	 * kihuファイルを読み込む
	 */
	public void input_kihu(String input_file){
		System.out.println("棋譜を読み込みます。");
		kihu_list.clear();
		Move move = new Move();
		Move input_move = null;
		File file = new File(input_file);
		try {
			FileReader filereader = new FileReader(file);
			BufferedReader br = new BufferedReader(filereader);
			String str = br.readLine();
			while(str != null) {
				input_move = move.get_input_move(str);
				if(input_move == null) {
					break;
				}else {
					kihu_list.add(input_move);
				}
				str = br.readLine();
			}
			br.close();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}
	/**
	 * 棋譜ファイルを最初の局面から保存する。
	 * 最初の局面を保存して、改行して、棋譜保存する。
	 * @param save_file
	 */
	public void save_kihu_from_first_kyokumen(String save_file) {
		System.out.println("棋譜を"+save_file+"に保存します。最初の局面も保存します。");
		File file = new File(save_file);
		try {
			FileWriter file_writer = new FileWriter(file);
			///保存する
			first_kyokumen.save_kyokumen_from_file_writer(file_writer);
			for(Move move:kihu_list) {
				file_writer.write(move.get_move_kihu());
			}
			file_writer.close();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			System.out.println("保存に失敗しました。");
		}
	}
	/**
	 * 棋譜をファイルに保存する。
	 * @param save_file
	 */
	public void save_kihu_file(String save_file) {
		System.out.println("棋譜を"+save_file+"に保存します。");
		File file = new File(save_file);
		try {
			FileWriter file_writer = new FileWriter(file);
			for(Move move:kihu_list) {
				file_writer.write(move.get_move_kihu());
			}
			file_writer.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("保存に失敗しました。");
		}
		System.out.println("保存されました。");
	}

	public void output_kihu() {
		System.out.println("棋譜を出力します。first_kyokumen");
		first_kyokumen.output_kyokumen();
		System.out.println("棋譜を出力します。kihu");
		for(Move move:kihu_list) {
			move.output_move_kihu();
		}
	}
	public void add_move(Move move) {
		kihu_list.add(move);
	}
	public ArrayList<Move> get_kihu_list(){
		return kihu_list;
	}
	public Move get_last_move() {
		if(kihu_list==null || kihu_list.isEmpty()) {
			return null;
		}
		return kihu_list.get(kihu_list.size()-1);
	}
	public void remove_last_move() {
		kihu_list.remove(kihu_list.size()-1);
	}
	public Kyokumen get_first_kyokumen() {
		return first_kyokumen;
	}
	public Kyokumen get_last_kyokumen() {
		return last_kyokumen;
	}
	//棋譜を保存する。before*100+after *100 + 駒の番号十の位は成駒なら1,なる時は2,成らずは3,普通は0,取った駒？
}


