package board;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;

import koma.Calc_koma;
import koma.Gin;
import koma.Gyoku;
import koma.Hisha;
import koma.Hu;
import koma.Kaku;
import koma.Kei;
import koma.Kin;
import koma.Koma;
import koma.Kyou;

public class Ban {
	private Koma banarray[][] = new Koma[10][10];
	public Ban() {
		set_first_ban();
	}
	public Ban(Koma[][] banarray) {
		this.banarray = banarray;
	}
	public Ban clone() {
		Koma array[][] = new Koma[10][10];
		for(int i=0;i<10;i++) {
			for(int j=0;j<10;j++) {
				array[i][j] = banarray[i][j];
			}
		}
		return new Ban(array);
	}
	/**
	 * @return true:同じ, false:違う
	 */
	public boolean equal(Ban ban) {
		for(int i=0;i<10;i++) {
			for(int j=0;j<10;j++) {
				if(banarray[i][j]==null) {
					if(ban.get_banarray(i, j)!=null) {
						return false;
					}
				}else {
					if(!banarray[i][j].equal(ban.get_banarray(i, j))) {
						return false;
					}
				}
			}
		}
		return true;
	}
	public void save_file(FileWriter file_writer) {
		try {
			for(int i=0;i<10;i++) {
				for(int j=0;j<10;j++) {
					if(banarray[i][j]==null) {
						file_writer.write("0\n");
					}else {
						banarray[i][j].save_file(file_writer);
					}
				}
			}
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
			String str = null;
			for(int i=0;i<10;i++) {
				for(int j=0;j<10;j++) {
					str = br.readLine();
					if(str.equals("0")) {
						banarray[i][j] = null;
					}else {
						Koma koma = Calc_koma.make_load_Koma(str);
						if(koma == null) {
							return false;
						}
						banarray[i][j] = koma;
					}
				}
			}
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			return false;
		}
		return true;
	}
	/**
	 * 盤面を出力する
	 */
	public void output_ban() {
		System.out.println("盤面を出力します。");
		Koma koma = null;
		for(int i=1;i<10;i++) {
			for(int j=9;j>0;j--) {
				koma = get_banarray(j, i);
				if(koma==null) {
					System.out.print("　　　");
				}else {
					koma.output_koma();
				}
			}
			System.out.println("");
		}
	}
	/**
	 * 初期局面を作る。
	 */
	public void set_first_ban() {
		//盤面,全てnullにする
		for(int i=1;i<10;i++) {
			for(int j=1;j<10;j++) {
				set_banarray(i, j, null);
			}
		}
		//駒配置
		for(int i=1;i<10;i++) {
			Koma hu1 = new Hu(1);
			Koma hu2 = new Hu(2);
			//歩
			set_banarray(i, 3, hu2);
			set_banarray(i, 7, hu1);
		}
		//香
		Koma kyou1 = new Kyou(1);
		Koma kyou2 = new Kyou(1);
		Koma kyou3 = new Kyou(2);
		Koma kyou4 = new Kyou(2);
		set_banarray(1, 1, kyou3);
		set_banarray(9, 1, kyou4);
		set_banarray(1, 9, kyou2);
		set_banarray(9, 9, kyou1);
		//桂
		Koma kei1 = new Kei(1);
		Koma kei2 = new Kei(1);
		Koma kei3 = new Kei(2);
		Koma kei4 = new Kei(2);
		set_banarray(2, 1, kei4);
		set_banarray(8, 1, kei3);
		set_banarray(8, 9, kei1);
		set_banarray(2, 9, kei2);
		//銀
		Koma gin1 = new Gin(1);
		Koma gin2 = new Gin(1);
		Koma gin3 = new Gin(2);
		Koma gin4 = new Gin(2);
		set_banarray(3, 1, gin4);
		set_banarray(7, 1, gin3);
		set_banarray(3, 9, gin2);
		set_banarray(7, 9, gin1);
		//金
		Koma kin1 = new Kin(1);
		Koma kin2 = new Kin(1);
		Koma kin3 = new Kin(2);
		Koma kin4 = new Kin(2);
		set_banarray(4, 1, kin3);
		set_banarray(6, 1, kin4);
		set_banarray(4, 9, kin2);
		set_banarray(6, 9, kin1);
		//玉
		Koma gyoku1 = new Gyoku(1);
		Koma gyoku2 = new Gyoku(2);
		set_banarray(5, 1, gyoku2);
		set_banarray(5, 9, gyoku1);
		//角
		Koma kaku1 = new Kaku(1);
		Koma kaku2 = new Kaku(2);
		set_banarray(2, 2, kaku2);
		set_banarray(8, 8, kaku1);
		//飛車
		Koma hisha1 = new Hisha(1);
		Koma hisha2 = new Hisha(2);
		set_banarray(8, 2, hisha2);
		set_banarray(2, 8, hisha1);
	}
	/**
	 * 盤に駒をセットする。
	 * 駒のplaceもセットする。
	 * @param a
	 * @param b
	 * @param koma
	 */
	public void set_banarray(int a,int b,Koma koma){
		if (0<a && a<10 && 0<b && b<10) {
			banarray[a][b] = koma;
			if(koma!=null) {
				koma.set_place(a,b);
			}
		}
	}
	public Koma get_banarray(int a,int b) {
		if (0<a && a<10 && 0<b && b<10) {
			return banarray[a][b];
		}
		return null;
	}

}
