package swing;

import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * 棋譜を再生する時に使うパネル
 * @author satoshi
 *
 */
public class Kihu_panel extends JPanel{
	private JButton button_next;
	private JButton button_back;
	private JButton button_first;
	private JButton button_end;
	public void set_kihu_panel(Action_kihu_listener action_kihu_listener) {
		setBackground(Color.darkGray);
		setBounds(10,300,145,40);//横、縦、幅、高さ
		setLayout(null);
		button_first = new JButton("<<");
		button_first.addActionListener(action_kihu_listener);
		button_first.setActionCommand("最初");//
		button_first.setBounds(5, 5, 30, 30);
		add(button_first);
		button_back = new JButton("<");
		button_back.addActionListener(action_kihu_listener);
		button_back.setActionCommand("前へ");//戻る
		button_back.setBounds(40, 5, 30, 30);
		add(button_back);
		button_next = new JButton(">");
		button_next.addActionListener(action_kihu_listener);
		button_next.setActionCommand("次へ");//
		button_next.setBounds(75, 5, 30, 30);
		add(button_next);
		button_end = new JButton(">>");
		button_end.addActionListener(action_kihu_listener);
		button_end.setActionCommand("最後");//
		button_end.setBounds(110, 5, 30, 30);
		add(button_end);
		this.setVisible(false);
	}
	public void set_visible() {
		this.setVisible(true);
	}
	public void set_not_visible() {
		this.setVisible(false);
	}

}
