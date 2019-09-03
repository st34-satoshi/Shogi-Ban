package board;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import koma.Calc_koma;
import koma.Koma;

/**
*
* @author satoshi
* 局面を保存する。盤面と駒台
*  駒 0:なし 1:歩 2:香 3:桂 4:銀 5:金 6:玉 7:角 8:飛車
* 10の位 0:普通 1:成駒 100の位 1:先手の駒 2:後手の駒
*/
public class Kyokumen {
	//盤の状態,11-99盤の呼び方と同じ
	private Komadai sente_komadai;
	private Komadai gote_komadai;
	private Ban ban;
	private int teban;//1:先手2:後手
	private ArrayList<Koma> koma_array;//全てのコマのリスト。駒を探しやすいように.盤上にある駒だけ

	public Kyokumen(Ban ban,Komadai sente_komadai,Komadai gote_komadai,int teban) {
		this.teban = teban;
		this.ban = ban;
		this.sente_komadai = sente_komadai;
		this.gote_komadai = gote_komadai;
		koma_array = new ArrayList<Koma>();
		set_koma_array();
		clone_all_koma();
		decide_all_koma_move_place();///
	}
	public Kyokumen() {
		//入力がなければ初期局面
		ban = new Ban();
		sente_komadai = new Komadai(1);
		gote_komadai = new Komadai(2);
		teban = 1;
		koma_array = new ArrayList<Koma>();
		set_koma_array();
		decide_all_koma_move_place();///
	}
	public Kyokumen clone() {
		return new Kyokumen(ban.clone(),sente_komadai.clone(),gote_komadai.clone(),teban);
	}
	/**
	 * @param kyokumen,比較する局面クラス
	 * @return true:同じ局面,false:違う局面
	 */
	public boolean equal(Kyokumen kyokumen) {
		if(this.teban != kyokumen.get_teban()) {
			return false;
		}
		if(!this.sente_komadai.equal(kyokumen.get_sente_komadai())) {
			return false;
		}
		if(!this.gote_komadai.equal(kyokumen.get_gote_komadai())) {
			return false;
		}
		if(!this.ban.equal(kyokumen.get_ban())) {
			return false;
		}
		return true;
	}
	/**
	 * 全ての駒のクローン作って置き換える
	 * 盤上とkoma_array
	 */
	private void clone_all_koma() {
		Koma new_koma = null;
		ArrayList<Koma> koma_array_new = new ArrayList<Koma>();
		for(Koma koma:koma_array) {
			new_koma = koma.clone();
			koma_array_new.add(new_koma);
			set_koma_from_place(new_koma, new_koma.get_place_a(), new_koma.get_place_b());
		}
		koma_array = koma_array_new;
	}
	/**
	 * 自分の駒のリストを返す。
	 * 盤上だけ
	 * @return
	 */
	public ArrayList<Koma> get_teban_koma_list_ban(int teban){
		ArrayList<Koma> my_koma_list = new ArrayList<Koma>();
		for(Koma koma:koma_array) {
			if(koma.get_teban() == teban) {
				//自分の駒。盤上の駒
				my_koma_list.add(koma);
			}
		}
		return my_koma_list;
	}
	/**
	 * 自分の駒のリストを返す。
	 * 持ち駒もリストに入れる。持ち駒の同じ駒は一枚だけ
	 * @return
	 */
	public ArrayList<Koma> get_teban_koma_list_all(int teban){
		ArrayList<Koma> my_koma_list = get_teban_koma_list_ban(teban);//盤上の駒
		//駒台.先手か後手
		for(Koma koma:get_komadai(teban).get_komadai_all_koma()) {
			my_koma_list.add(koma);
		}
		return my_koma_list;
	}
	/**
	 * 手番の王様の場所を返す
	 * @return
	 */
	public int get_place_gyoku(int teban) {
		for(Koma koma:koma_array) {
			if(koma.get_koma_number()==6) {
				if(koma.get_teban()==teban) {
					return koma.get_place();
				}
			}
		}
		return 0;
	}
	/**
	 * 打った駒を受け取って駒台のその駒を減らす。
	 * @param koma
	 */
	public void decrease_motigoma(Koma koma) {
		if(koma.get_teban()==1) {
			sente_komadai.decrease_komadai(koma);
		}else if(koma.get_teban()==2) {
			gote_komadai.decrease_komadai(koma);
		}else {
			System.out.println("error: Kyokumen decrease_motigoma teban");
		}
	}
	/**
	 * moveもらって前の局面へ
	 * 手番変える
	 * @param move
	 */
	public void move_back_kyokumen(Move move) {
		//動いた駒を元の場所に戻す
		Koma move_koma = get_koma_from_place(move.get_after_place_a(), move.get_after_place_b());//動いた駒
		remove_koma_array(move_koma);
		if(move.get_naru()) {
			//成っている時
			move_koma = move_koma.get_narazukoma();
		}
		if(move.get_before_place_a()<10) {
			//盤上の移動の時
			ban.set_banarray(move.get_before_place_a(), move.get_before_place_b(), move_koma);
			add_koma_array(move_koma);
		}else {
			//駒を打った時
			move_koma.change_teban();
			set_koma_from_place(move_koma,move.get_before_place_a() , move.get_before_place_b());
		}
		//動いた後の場所に元あった(なかった)駒を配置
		if(move.get_get_koma()==0) {
			//とっていない
			change_teban();
			ban.set_banarray(move.get_after_place_a(), move.get_after_place_b(), null);
		}else {
			//駒を取っていた。
			Koma koma = Calc_koma.make_koma(move.get_get_koma(), teban);
			ban.set_banarray(move.get_after_place_a(), move.get_after_place_b(), koma);
			int koma_number = move.get_get_koma();
			if(koma_number>9) {
				koma_number -= 10;
			}
			//駒台減らす
			if(teban==1) {
				//後手番の駒台
				gote_komadai.decrease_komadai_koma_number(koma_number);
			}else {
				sente_komadai.decrease_komadai_koma_number(koma_number);
			}
			change_teban();
			add_koma_array(koma);
		}
		decide_all_koma_move_place();
	}
	/**
	 * moveをもらって次の局面へ
	 * before,after
	 * なるかどうかはここで確認する。
	 * 手番変える
	 */
	public void move_next_kyokumen(Move move) {
		int a = move.get_before_place_a();
		int b = move.get_before_place_b();
		Koma koma = get_koma_from_place(a, b);
		if(koma.get_motigoma()) {
			//持ち駒を打つとき。
			koma.set_motigoma(false);
			decrease_motigoma(koma);//駒台減らす。
			a = move.get_after_place_a();
			b = move.get_after_place_b();
			ban.set_banarray(a, b, koma);
			add_koma_array(koma);
		}else {
			//盤上の駒を動かす
			ban.set_banarray(a, b, null);//動かす前の場所
			a = move.get_after_place_a();
			b = move.get_after_place_b();
			Koma get_koma = get_banarray(a,b);
			if(get_koma != null) {
				//駒を取っていたら駒台へ
				if(teban==1) {
					sente_komadai.set_komadai(get_koma);
				}else {
					gote_komadai.set_komadai(get_koma);
				}
				//取った駒はkoma_arrayから消す
				koma_array.remove(get_koma);
				//moveのget_komaをset
				move.set_get_koma(get_koma.get_koma_number());
			}
			//なれるかの確認,
			if(move.get_naru()) {
				//成る時
				remove_koma_array(koma);
				koma = koma.get_narigoma();
				add_koma_array(koma);
			}
			ban.set_banarray(a, b, koma);
		}
		change_teban();
		decide_all_koma_move_place();
	}
	private void change_teban() {
		if(teban==1) {
			teban = 2;
		}else if(teban==2) {
			teban = 1;
		}
	}
	public void decide_all_koma_move_place() {
		for(Koma koma:koma_array) {
			koma.move_place_clear();
			koma.decide_move_place_all(this);
		}
	}
	public void add_koma_array(Koma koma) {
		if(koma.get_place()>99) {
			System.out.println("add_koma_array miss");
		}
		koma_array.add(koma);
	}
	public void remove_koma_array(Koma koma) {
		koma_array.remove(koma);
	}
	/**
	 * koma_arrayを作る。
	 * ban,見て作る。
	 * 駒台の駒は入れない
	 */
	public void set_koma_array() {
		koma_array = new ArrayList<Koma>();
		Koma koma = null;
		//盤
		for(int i=1;i<10;i++) {
			for(int j=0;j<10;j++) {
				koma = ban.get_banarray(i, j);
				if(koma != null) {
					koma_array.add(koma);
					if(koma.get_place()>99) {
						System.out.println("set_koma_array miss");///
					}
				}
			}
		}
	}
	public void output_kyokumen() {
		//出力する。いい方法あるといい
		System.out.println("局面を出力します。teban"+teban);
		//後手の駒台の出力
		gote_komadai.output_komadi();
		ban.output_ban();//盤面の出力
		//先手の駒台の出力
		sente_komadai.output_komadi();
		//output_koma_array();//koma_arrayの出力
	}
	/**
	 * file_writerを受け取って局面をそのファイルに保存する。
	 */
	public void save_kyokumen_from_file_writer(FileWriter file_writer) {
		try {
			//手番
			file_writer.write(teban+"\n");
			//先手駒台
			sente_komadai.save_file(file_writer);
			//後手駒台
			gote_komadai.save_file(file_writer);
			//盤面
			ban.save_file(file_writer);
			file_writer.write("\n");
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}
	/**
	 * 局面をファイルに保存する。
	 */
	public void save_kyokumen(String save_file) {
		System.out.println("局面を"+save_file+"に保存します。");
		File file = new File(save_file);
		try {
			FileWriter file_writer = new FileWriter(file);
			//手番
			file_writer.write(teban+"\n");
			//先手駒台
			sente_komadai.save_file(file_writer);
			//後手駒台
			gote_komadai.save_file(file_writer);
			//盤面
			ban.save_file(file_writer);
			file_writer.close();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			System.out.println("保存に失敗しました。");
		}
		System.out.println("保存されました。");
	}
	/**
	 * 局面を読み込む
	 * @return成功したらtrue
	 */
	public boolean read_kyokumen_from_first_kyokumen(BufferedReader br) {
		System.out.println("局面を読み込みます。");
		try {
			//最初は手番を読み込む
			String str = br.readLine();
			if(str.equals("1")) {
				this.teban = 1;
			}else if(str.equals("2")) {
				this.teban = 2;
			}else {
				System.out.println("手番の読み込みに失敗しました。");
				return false;
			}
			if(!sente_komadai.load_file(br)) {
				System.out.println("先手の駒台の読み込みに失敗しました。");
				return false;
			}
			if(!gote_komadai.load_file(br)) {
				System.out.println("後手の駒台の読み込みに失敗しました。");
				return false;
			}
			if(!ban.load_file(br)) {
				System.out.println("盤の読み込みに失敗しました。");
				return false;
			}
			br.readLine();//改行を読む
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		return true;
	}
	public boolean load_kyokumen(String load_file) {
		System.out.println("局面を読み込みます。");
		File file = new File(load_file);
		try {
			FileReader filereader = new FileReader(file);
			BufferedReader br = new BufferedReader(filereader);
			//最初は手番を読み込む
			String str = br.readLine();
			if(str.equals("1")) {
				this.teban = 1;
			}else if(str.equals("2")) {
				this.teban = 2;
			}else {
				System.out.println("手番の読み込みに失敗しました。");
				br.close();
				return false;
			}
			if(!sente_komadai.load_file(br)) {
				System.out.println("先手の駒台の読み込みに失敗しました。");
				return false;
			}
			if(!gote_komadai.load_file(br)) {
				System.out.println("後手の駒台の読み込みに失敗しました。");
				return false;
			}
			if(!ban.load_file(br)) {
				System.out.println("盤の読み込みに失敗しました。");
				return false;
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}
	public void output_koma_array() {
		System.out.println("koma_arrayを出力します。");
		for(Koma koma:koma_array) {
			koma.output_move_place();
		}
	}
	/**
	 * 入力の場所の駒を返す。何もないときはnull
	 * 入力はa,bとか
	 * @return
	 */
	public Koma get_koma_from_place(int place_a,int place_b) {
		if(place_a==10) {
			Koma koma = sente_komadai.get_komadai(place_b);
			return koma;
		}else if(place_a==20) {
			Koma koma = gote_komadai.get_komadai(place_b);
			return koma;
		}else {
			return ban.get_banarray(place_a, place_b);
		}
	}
	public Koma get_koma_from_place(int place) {
		return get_koma_from_place(place/10, place%10);
	}
	/**
	 * 場所に駒をセットする。
	 * 盤だけではなく駒台もできるようにする。
	 * @param koma
	 * @param place_a
	 * @param place_b
	 */
	public void set_koma_from_place(Koma koma,int place_a,int place_b) {
		if(place_a<10) {
			//盤の時
			ban.set_banarray(place_a, place_b, koma);
		}else if(place_a==10){
			//先手の駒台
			sente_komadai.set_komadai(koma);
		}else if(place_a==20) {
			//後手の駒台
			gote_komadai.set_komadai(koma);
		}
	}
	public Koma get_sente_komadai_koma(int place_b) {
		return sente_komadai.get_komadai(place_b);
	}
	public Koma get_gote_komadai_koma(int place_b) {
		return gote_komadai.get_komadai(place_b);
	}
	public Komadai get_sente_komadai() {
		return sente_komadai;
	}
	public Komadai get_gote_komadai() {
		return gote_komadai;
	}
	public Komadai get_komadai(int teban) {
		if(teban==1) {
			return get_sente_komadai();
		}else if(teban==2) {
			return get_gote_komadai();
		}
		System.out.println("error: Kyokumen class get_komadai teban");
		return null;
	}
	public Koma get_banarray(int a,int b) {
		if (0<a && a<10 && 0<b && b<10) {
			return ban.get_banarray(a, b);
		}
		return null;
	}
	public Koma get_banarray(int place) {
		int a = place / 10;
		int b = place % 10;
		return get_banarray(a, b);
	}
	public int get_teban() {
		return teban;
	}
	public ArrayList<Koma> get_koma_array(){
		return koma_array;
	}
	public Ban get_ban() {
		return ban;
	}
	public String get_teban_string() {
		if(teban==1) {
			return "先手";
		}else if(teban==2) {
			return "後手";
		}
		return "error:get_teban_string int kyokumen class";
	}
	public void set_ban_komadai(Ban ban,Komadai sente,Komadai gote) {
		this.ban = ban;
		this.sente_komadai = sente;
		this.gote_komadai = gote;
	}
}
