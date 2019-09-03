package swing;

import java.awt.Color;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

public class Menu_panel extends JPanel{
	private JButton button_new;//新規対局ボタン
	private JButton button_start;//現在の局面から対局開始
	private JButton button_touryou;
	private JButton button_read_kyokumen;
	private JButton button_read_kihu;
	private JButton button_calc;
	private JButton button_tumi;
	private JButton button_save_kyokumen;
	private JButton button_save_kihu;
	private JButton button_matta;
	private JButton button_free;
	/**
	 * 対局中の状態にする。
	 */
	public void set_gaming() {
		button_new.setVisible(false);
		button_start.setVisible(false);
		button_touryou.setVisible(true);
		button_read_kyokumen.setVisible(false);
		button_read_kihu.setVisible(false);
		button_calc.setVisible(true);//人間同士の時は隠したほうがいい?
		button_tumi.setVisible(true);
		button_save_kyokumen.setVisible(true);
		button_save_kihu.setVisible(true);
		button_matta.setVisible(true);
		button_free.setVisible(false);
	}
	public void set_not_gaming() {
		button_new.setVisible(true);
		button_start.setVisible(true);
		button_touryou.setVisible(false);
		button_read_kyokumen.setVisible(true);
		button_read_kihu.setVisible(true);
		button_calc.setVisible(true);
		button_tumi.setVisible(true);
		button_save_kyokumen.setVisible(true);
		button_save_kihu.setVisible(true);
		button_matta.setVisible(false);
		button_free.setVisible(true);
	}
	public void set_free_before() {
		button_new.setVisible(false);
		button_start.setVisible(false);
		button_touryou.setVisible(false);
		button_read_kyokumen.setVisible(false);
		button_read_kihu.setVisible(false);
		button_calc.setVisible(false);
		button_tumi.setVisible(false);
		button_save_kyokumen.setVisible(true);
		button_save_kihu.setVisible(false);
		button_matta.setVisible(false);
		button_free.setVisible(true);
		///button_free.setText("終了");
	}

	public void set_menu_panel(Action_listener_menu action_listener_menu) {
		setBackground(Color.darkGray);
		setBounds(50,10,830,30);//横、縦、幅、高さ
		setLayout(new BoxLayout(this,BoxLayout.X_AXIS));
		button_new = new JButton("新規");//新規対局ボタン
		button_new.addActionListener(action_listener_menu);
		button_new.setActionCommand("新規対局");//
		add(button_new);
		button_start = new JButton("現始");//現在の局面から対局ボタン
		button_start.addActionListener(action_listener_menu);
		button_start.setActionCommand("現在の局面から対局");
		add(button_start);
		button_touryou = new JButton("投了");//投了ボタン
		button_touryou.addActionListener(action_listener_menu);
		button_touryou.setActionCommand("投了");
		add(button_touryou);
		button_read_kyokumen = new JButton("局読");//局面読み込みボタン
		button_read_kyokumen.addActionListener(action_listener_menu);
		button_read_kyokumen.setActionCommand("局面読み込み");
		add(button_read_kyokumen);
		button_read_kihu = new JButton("棋読");//棋譜読み込みボタン
		button_read_kihu.addActionListener(action_listener_menu);
		button_read_kihu.setActionCommand("棋譜読み込み");
		add(button_read_kihu);
		button_calc = new JButton("評価");//評価ボタン
		button_calc.addActionListener(action_listener_menu);
		button_calc.setActionCommand("局面評価");
		add(button_calc);
		button_tumi = new JButton("詰み");//詰みチェックボタン
		button_tumi.addActionListener(action_listener_menu);
		button_tumi.setActionCommand("詰みチェック");
		add(button_tumi);
		button_save_kyokumen = new JButton("局保");//局面保存チェックボタン
		button_save_kyokumen.addActionListener(action_listener_menu);
		button_save_kyokumen.setActionCommand("局面保存");
		add(button_save_kyokumen);
		button_save_kihu = new JButton("棋保");//棋譜保存チェックボタン
		button_save_kihu.addActionListener(action_listener_menu);
		button_save_kihu.setActionCommand("棋譜保存");
		add(button_save_kihu);
		button_matta = new JButton("待た");//待ったチェックボタン
		button_matta.addActionListener(action_listener_menu);
		button_matta.setActionCommand("待った");
		add(button_matta);
		button_free = new JButton("自由");//自由に駒を動かせるチェックボタン
		button_free.addActionListener(action_listener_menu);
		button_free.setActionCommand("自由");
		add(button_free);

	}


}
