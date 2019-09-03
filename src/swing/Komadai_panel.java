package swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Window;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import board.Komadai;

public class Komadai_panel extends JPanel{
	private GridBagLayout grid_bag_layout;
	private JPanel[] panel_array;//0:歩
	private Window him;

	public Komadai_panel(Window her) {
		him = her;
	}

	public void change_color(int b) {
		JPanel panel = panel_array[b-1];
		if(panel != null) {
			panel.setOpaque(true);
		}
	}
	/*
	public void change_color_back(int b) {
		JPanel panel = panel_array[b-1];
		if(panel != null) {
			panel.setOpaque(false);
		}
	}
	*/
	/**
	 * 駒台の一箇所の画像を変える
	 * @param b
	 * @param komadai
	 * @param action_listener
	 */
	public void change_panel(int b,Komadai komadai,Action_listener action_listener) {
		GridBagLayout gbl = grid_bag_layout;
		GridBagConstraints gbc = new GridBagConstraints();
		int teban = komadai.get_teban();
		int koma_many = komadai.get_koma_number(b);
		JPanel old_panel = panel_array[b-1];
		if(old_panel != null) {
			old_panel.setVisible(false);
		}
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setPreferredSize(new Dimension(45,50));
		panel.setBorder(new LineBorder(Color.black,1));
		panel.setLayout(new BorderLayout());
		int x = (b-1)%4;
		int y = (b-1)/4;
		if(teban==1) {
			gbc.gridx = x;
			gbc.gridy = y;
		}else {
			gbc.gridx = 3-x;
			gbc.gridy = 1-y;
		}
		gbl.setConstraints(panel, gbc);
		if(koma_many>0) {
			JLabel label = new JLabel(""+koma_many);
			///駒の画像を表示させる。
			ImageIcon icon = Make_koma_image.make_koma_image(b, teban, him);
			Image image = icon.getImage().getScaledInstance(36, 40, Image.SCALE_DEFAULT);
			icon = new ImageIcon(image);
			JButton button = new JButton(icon);
			button.setContentAreaFilled(false);//背景透明化
			button.setBorderPainted(false);
			//actionlistener加える
			button.addActionListener(action_listener);
			button.setActionCommand(""+teban+"0"+b);//駒台の駒の番号。盤の番号と同じ。先手の歩なら11
			panel.add(button,BorderLayout.CENTER);
			panel.add(label,BorderLayout.NORTH);
			add(panel);
			panel_array[b-1] = panel;
		}else {
			//駒がない時
			JButton button = new JButton("");
			//actionlistener加える
			button.addActionListener(action_listener);
			button.setActionCommand(""+teban+"0"+b);//駒台の駒の番号。盤の番号と同じ。先手の歩なら11
			panel.add(button,BorderLayout.CENTER);
			add(panel);
			panel_array[b-1] = panel;
		}
	}
	public void set_komadai_panel(int p_w,int p_h) {
		setBackground(new Color(153,102,102));
		setBounds(p_w,p_h,200,120);//横、縦、幅、高さ
		GridBagLayout gbl = new GridBagLayout();
		grid_bag_layout = gbl;
		setLayout(gbl);
		make_komadai(gbl);
		panel_array = new JPanel[8];
		for(int i=0;i<8;i++) {
			panel_array[i] = null;
		}
	}
	private void make_komadai(GridBagLayout gbl) {
		GridBagConstraints gbc = new GridBagConstraints();
		for(int i=1;i<5;i++) {
			for(int j=1;j<3;j++) {
				JPanel panel = new JPanel();
				panel.setOpaque(false);
				panel.setPreferredSize(new Dimension(45,50));
				panel.setBorder(new LineBorder(Color.black,1));
				gbc.gridx = i-1;
				gbc.gridy = j-1;
				gbl.setConstraints(panel, gbc);
				add(panel);
			}
		}
	}
	public void make_komadai_panel(Komadai komadai,Action_listener action_listener) {
		int side = this.getX();
		int length = this.getY();
		removeAll();
		set_komadai_panel(side, length);
		GridBagLayout gbl = grid_bag_layout;
		GridBagConstraints gbc = new GridBagConstraints();
		int teban = komadai.get_teban();
		int koma_number = 1;
		if(teban==2) {
			koma_number = 8;
		}
		for(int j=1;j<3;j++) {
			for(int i=1;i<5;i++) {
				int koma_many = komadai.get_koma_number(koma_number);
				if(koma_many>0) {
					JPanel panel = new JPanel();
					panel.setOpaque(false);
					panel.setPreferredSize(new Dimension(45,50));
					panel.setBorder(new LineBorder(Color.black,1));
					panel.setLayout(new BorderLayout());
					gbc.gridx = i-1;
					gbc.gridy = j-1;
					gbl.setConstraints(panel, gbc);
					JLabel label = new JLabel(""+koma_many);
					///駒の画像を表示させる。
					ImageIcon icon = Make_koma_image.make_koma_image(koma_number, teban, him);
					System.out.println(""+koma_number+","+i+","+j);
					Image image = icon.getImage().getScaledInstance(36, 40, Image.SCALE_DEFAULT);
					icon = new ImageIcon(image);
					JButton button = new JButton(icon);
					button.setContentAreaFilled(false);//背景透明化
					button.setBorderPainted(false);
					//actionlistener加える
					button.addActionListener(action_listener);
					button.setActionCommand(""+teban+"0"+koma_number);//駒台の駒の番号。盤の番号と同じ。先手の歩なら11
					panel.add(button,BorderLayout.CENTER);
					panel.add(label,BorderLayout.NORTH);
					add(panel);
					panel_array[koma_number-1] = panel;
				}else {
					//駒台に駒がなくてもボタンは作る
					JPanel panel = new JPanel();
					panel.setOpaque(false);
					panel.setPreferredSize(new Dimension(45,50));
					panel.setBorder(new LineBorder(Color.black,1));
					panel.setLayout(new BorderLayout());
					gbc.gridx = i-1;
					gbc.gridy = j-1;
					gbl.setConstraints(panel, gbc);
					///駒の画像を表示させる。駒はないのでボタンだけ表示
					JButton button = new JButton("");
					//actionlistener加える
					button.addActionListener(action_listener);
					button.setActionCommand(""+teban+"0"+koma_number);//駒台の駒の番号。盤の番号と同じ。先手の歩なら11
					panel.add(button,BorderLayout.CENTER);
					add(panel);
					panel_array[koma_number-1] = panel;
				}
				if(teban==1) {
					koma_number++;
				}else {
					koma_number--;
				}
			}
		}
	}
}
