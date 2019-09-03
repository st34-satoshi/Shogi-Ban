package board;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import koma.Calc_koma;
import koma.Koma;
import main.Calc;

/**
 * 駒台を保存するクラス。
 * @author satoshi
 *
 */
public class Komadai {
	//0:なし 1:歩 2:香 3:桂 4:銀 5:金 6:玉 7:角 8:飛車
	private int komadai[] = new int[9];
	private int teban;
	//private ArrayList<Koma> koma_array;//Komaクラスのポインタを入れておく。一旦使わない
	public Komadai(int[] komadai,int teban) {
		this.komadai = komadai;
		this.teban = teban;
	}
	public Komadai(int teban) {
		for(int i=0;i<9;i++) {
			//最初は駒台に駒はない
			komadai[i] = 0;
		}
		this.teban = teban;
	}
	public Komadai clone() {
		int dai[] = new int[9];
		for(int i=0;i<9;i++) {
			dai[i] = komadai[i];
		}
		return new Komadai(dai,teban);
	}
	/**
	 * @param komadai 比較する駒台
	 * @return true:同じ,false:違う
	 */
	public boolean equal(Komadai komadai_n) {
		if(this.teban != komadai_n.get_teban()) {
			return false;
		}
		for(int i=0;i<9;i++) {
			if(komadai[i] != komadai_n.get_koma_number(i)) {
				return false;
			}
		}
		return true;
	}
	/**
	 * ファイルに保存する
	 * @param file_writer
	 */
	public void save_file(FileWriter file_writer) {
		try {
			file_writer.write(get_teban_s()+"\n");
			for(int i=0;i<9;i++) {
				file_writer.write(komadai[i] + ",");
			}
			file_writer.write("\n");
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}
	/**
	 * 正しく読み込めたらtrue
	 * @return
	 */
	public boolean load_file(BufferedReader br) {
		try {
			//手番があっているか確認
			String str = br.readLine();
			if(Calc.isNumber(str)) {
				int teban = Integer.parseInt(str);
				if(this.teban != teban) {
					return false;
				}
			}else {
				return false;
			}
			str = br.readLine();
			String[] komadai_s = str.split(",", 0);
			if(komadai_s.length != 9) {
				System.out.println("駒台の長さが違います。");
				return false;
			}
			for(int i=0;i<9;i++) {
				if(Calc.isNumber(komadai_s[i])) {
					komadai[i] = Integer.parseInt(komadai_s[i]);
				}else {
					return false;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	/**
	 * 駒台にある駒のポイントの合計を返す。
	 * 先手ならプラス、後手ならマイナスで返す。
	 * @return
	 */
	public int get_all_point_teban() {
		Koma koma = null;
		int all_point = 0;
		for(int i = 1;i<9;i++) {
			koma = get_komadai(i);
			if(koma != null) {
				all_point += koma.get_point_teban() * komadai[i];
			}
		}
		return (int)(all_point * 1.3);//持ち駒は持っていた方が特
		//return all_point;
	}
	/**
	 * 駒台にある駒のポイントの合計を返す。
	 * @return
	 */
	/*
	public int get_all_point() {
		Koma koma = null;
		int all_point = 0;
		for(int i = 1;i<9;i++) {
			koma = get_komadai(i);
			if(koma != null) {
				all_point += koma.get_point_teban() * komadai[i];
			}
		}
		return all_point;
	}
	*/
	/**
	 * 駒台にある全ての駒のリストを返す。一つの駒は一回しか入らない
	 * @return
	 */
	public ArrayList<Koma> get_komadai_all_koma(){
		ArrayList<Koma> koma_list = new ArrayList<Koma>();
		Koma koma = null;
		for(int i=1;i<9;i++) {
			koma = get_komadai(i);
			if(koma != null) {
				koma_list.add(koma);
			}
		}
		return koma_list;

	}
	public void output_komadi() {
		if(teban==1) {
			System.out.println("先手の駒台の出力");
		}else {
			System.out.println("後手の駒台の出力");
		}
		for(int i=1;i<9;i++) {
			if(komadai[i] > 0) {
				System.out.print(change_n_s(i)+":"+komadai[i]+"枚、");
			}
		}
		System.out.println("");
	}

	/**
	 * 駒をうつ。減らす
	 * @param koma
	 */
	public void decrease_komadai(Koma koma) {
		if(koma.get_teban() != teban){
			System.out.println("error: Komadai decrease_komadai teban");
			return;//駒台が違う駒台
		}
		int n = koma.get_koma_number();
		if(komadai[n]<1) {
			System.out.println("error: Komadai decrease_komadai not have");
			return;
		}
		komadai[n] -= 1;
	}
	/**
	 * 駒をうつ。減らす
	 * @param koma
	 */
	public void decrease_komadai_koma_number(int koma_number) {
		if(koma_number<1 || koma_number>8) {
			System.out.println("error: Komadai decrease_komadai_koma_number");
			return;
		}
		if(komadai[koma_number]<1) {
			System.out.println("error: Komadai decrease_komadai not have .koma number");
			return;
		}
		komadai[koma_number] -= 1;
	}
	/**
	 * 駒を駒台に入れる。
	 */
	public void set_komadai(Koma koma) {
		/*
		if(koma.get_teban() == teban){
			System.out.println("error: Komadai set_komadai teban");
			return;//駒台が違う駒台
		}
		*/
		///自由の時も使うから、手番が同じ駒の時もある
		int n = koma.get_koma_number();
		if(n>10) {
			n -= 10;
		}
		komadai[n] += 1;
		//koma_array.add(koma);
	}
	/**
	 * 入力place_bに持ち駒があればそれを返す。
	 * その駒が持ち駒になければnullを返す。
	 * 実際に動かすわけではない
	 * @param place
	 * @return
	 */
	public Koma get_komadai(int place) {
		if(komadai[place] < 1) {
			return null;
		}
		Koma koma = null;
		koma = Calc_koma.make_koma(place, teban);
		koma.set_motigoma(true);
		koma.set_place(teban*10, place);
		return koma;
	}
	public int get_koma_number(int place) {
		return komadai[place];
	}
	public void set_teban(int teban) {
		this.teban = teban;
	}
	public int get_teban() {
		return teban;
	}
	private String get_teban_s() {
		if(teban==1) {
			return "1";
		}else if(teban==2) {
			return "2";
		}
		return "error";
	}
	private String change_n_s(int n) {
		//n:1,2,3,4,5,6,7,8を歩、香、桂、銀、金、玉、角、飛車に変える 10の位があれば成駒にする
		String st;
		//全角で２文字
		switch (n) {
			case 0:
				st = "　　";
				break;
			case 1:
				st = "　歩";
				break;
			case 2:
				st = "　香";
				break;
			case 3:
				st = "　桂";
				break;
			case 4:
				st = "　銀";
				break;
			case 5:
				st = "　金";
				break;
			case 6:
				st = "　玉";
				break;
			case 7:
				st = "　角";
				break;
			case 8:
				st = "飛車";
				break;
			case 11:
				st = "　と";
				break;
			case 12:
				st = "成香";
				break;
			case 13:
				st = "成桂";
				break;
			case 14:
				st = "成銀";
				break;
			case 17:
				st = "　馬";
				break;
			case 18:
				st = "　龍";
				break;
			default:
				st = "error";
				break;
		}
		return st;
	}
	/*
	private int change_double_int(double dou) {
		return (int)dou;
	}
	*/
}
