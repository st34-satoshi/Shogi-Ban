package swing;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.border.LineBorder;

import board.Kyokumen;

public class Frame_s extends JFrame{
	private Menu_panel panel_menu;
	private Ban_panel panel_ban;
	private Komadai_panel panel_sente;
	private Komadai_panel panel_gote;
	private JLabel label_teban;
	private Action_listener action_listener;
	private Action_listener_menu action_listener_menu;
	private Action_kihu_listener action_kihu_listener;
	private Kihu_panel panel_kihu;
	public void set_action_listener(Action_listener action_listener,Action_listener_menu action_listener_menu,Action_kihu_listener action_kihu_listener) {
		this.action_listener = action_listener;
		this.action_listener_menu = action_listener_menu;
		this.action_kihu_listener = action_kihu_listener;

	}
	/**
	 * 次の局面に行く時にこのメソッドを呼んでおく
	 * 次の局面になってから呼ぶ。盤上の駒は変わっている
	 */
	public void set_next_te(String teban) {
		change_label_teban(teban);
	}
	public void change_label_teban(String string) {
		label_teban.setText(string);
	}
	/**
	 * cp同士の対局の時に
	 * 次の手に行くかどうかを確認する。
	 * @return true:次に行く
	 */
	public boolean check_next_or_exit() {
		int option = JOptionPane.showConfirmDialog(this, "次へ進みますか？", "確認", JOptionPane.YES_NO_OPTION,JOptionPane.PLAIN_MESSAGE);
	    if (option == JOptionPane.YES_OPTION){
	    		return true;
	    }else if (option == JOptionPane.NO_OPTION){
	    		return false;
	    }
	    return false;
	}
	public void output_message(String mes,String title) {
		JOptionPane.showMessageDialog(this, mes,title,JOptionPane.PLAIN_MESSAGE);
	}
	public void output_touryou(String teban) {
		JOptionPane.showMessageDialog(this, teban+"の負けです。", "投了",JOptionPane.PLAIN_MESSAGE);
	}
	/**
	 * panelの指定したところだけ変える
	 * placeは76なら76
	 * @param place_a
	 * @param place_b
	 */
	public void change_panel(int place_a,int place_b,Kyokumen kyokumen) {
		if(0<place_a && place_a<10) {
			//盤上の駒
			panel_ban.change_panel(place_a, place_b, kyokumen, action_listener);
		}else if(place_a==10) {
			//先手の駒台
			panel_sente.change_panel(place_b, kyokumen.get_sente_komadai(), action_listener);
		}else if(place_a==20) {
			panel_gote.change_panel(place_b, kyokumen.get_gote_komadai(), action_listener);
		}
		this.setVisible(true);
	}
	public void change_button_color(int place) {
		//placeは76なら76
		int a = place / 10;
		int b = place % 10;
		if(0<a && a<10 && 0<b && b<10) {
			//盤上
			panel_ban.change_color(a, b);
		}
		if(a==10) {
			panel_sente.change_color(b);
		}else if(a==20) {
			panel_gote.change_color(b);
		}
	}
	/*
	public void change_button_color_back(int place) {
		//placeは76なら76
		int a = place / 10;
		int b = place % 10;
		if(0<a && a<10 && 0<b && b<10) {
			//盤上
			panel_ban.change_color_back(a, b);
		}
		if(a==10) {
			panel_sente.change_color_back(b);
		}else if(a==20) {
			panel_gote.change_color_back(b);
		}
		this.setVisible(true);
	}
	*/
	/**
	 * 対局開始の処理。menu_panelの表示非表示を変える
	 */
	public void start_game() {
		panel_menu.set_gaming();
		panel_kihu.set_not_visible();
	}
	public void end_game() {
		panel_menu.set_not_gaming();
	}
	public void set_free_before() {
		panel_menu.set_free_before();
	}
	public void set_reading_kihu() {
		panel_menu.set_not_gaming();
		panel_kihu.set_visible();
	}
	public void swing2() {
		//this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("TS-shogi");
		this.setLayout(new SpringLayout());
		//this.setLayout(null);
		JPanel panel = new JPanel();
		panel.setBackground(Color.lightGray);
		panel.setLayout(null);
		this.setContentPane(panel);
		this.setBounds(0,0,905,550);
		make_panel(panel);
		this.label_teban = new JLabel("手番:");
		label_teban.setBounds(10, 200, 150, 50);//x,y,width,heigh
		label_teban.setBorder(new LineBorder(Color.black,2));
		panel.add(label_teban);
		this.setVisible(true);
	}
	public void make_panel(JPanel panel) {
		panel_ban = new Ban_panel(this);
		panel_ban.set_ban_panel();
		panel.add(panel_ban);
		panel_sente = new Komadai_panel(this);
		panel_sente.set_komadai_panel(645, 390);
		panel.add(panel_sente);
		panel_gote = new Komadai_panel(this);
		panel_gote.set_komadai_panel(10, 50);
		panel.add(panel_gote);
		panel_menu = new Menu_panel();
		panel_menu.set_menu_panel(action_listener_menu);
		panel.add(panel_menu);
		panel_kihu = new Kihu_panel();
		panel_kihu.set_kihu_panel(action_kihu_listener);
		panel.add(panel_kihu);

		//初期局面表示する。
		Kyokumen kyokumen = new Kyokumen();
		panel_ban.make_masu_from_ban(kyokumen.get_ban(),action_listener);
		panel_sente.make_komadai_panel(kyokumen.get_sente_komadai(),action_listener);
		panel_gote.make_komadai_panel(kyokumen.get_gote_komadai(),action_listener);
		panel_menu.set_not_gaming();
	}
	/**
	 * 入力の局面を表示する
	 * @param kyokumen
	 */
	public void set_kyokumen(Kyokumen kyokumen) {
		panel_ban.make_masu_from_ban(kyokumen.get_ban(),action_listener);
		panel_sente.make_komadai_panel(kyokumen.get_sente_komadai(),action_listener);
		panel_gote.make_komadai_panel(kyokumen.get_gote_komadai(),action_listener);
		change_label_teban("手番: "+kyokumen.get_teban_string());
		this.setVisible(true);
	}
	public void set_kihu_panel_visible() {
		panel_kihu.set_visible();
	}
	public void set_kihu_panel_not_visible() {
		panel_kihu.set_not_visible();
	}
	public Action_listener get_action_listener() {
		return action_listener;
	}
}
