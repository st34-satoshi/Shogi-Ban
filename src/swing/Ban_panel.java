package swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import board.Ban;
import board.Kyokumen;
import koma.Koma;

public class Ban_panel extends JPanel{
	private GridBagLayout grid_bag_layout;
	private JPanel[][] panel_array;

	public void change_panel(int a,int b,Kyokumen kyokumen,Action_listener action_listener) {
		GridBagLayout gbl = grid_bag_layout;
		GridBagConstraints gbc = new GridBagConstraints();
		Koma koma = kyokumen.get_koma_from_place(a, b);
		if(koma == null) {
			JPanel panel_o = panel_array[a-1][b-1];
			JLabel l = new JLabel("hoge");
			panel_o.add(l,BorderLayout.NORTH);
			panel_o.setVisible(false);
			//panel.removeAll();
			JPanel panel = new JPanel();
			//panel.add(l,BorderLayout.NORTH);
			panel.setOpaque(false);
			panel.setPreferredSize(new Dimension(45,50));
			panel.setBorder(new LineBorder(Color.black,1));
			gbc.gridx = 8-(a-1);//左右反転
			gbc.gridy = b-1;
			gbl.setConstraints(panel, gbc);
			JButton button = new JButton();
			button.setPreferredSize(new Dimension(45,50));
			button.setBackground(Color.black);///
			//actionlistener加える
			button.addActionListener(action_listener);
			button.setActionCommand(""+a+""+b);//盤の番号。76とか
			panel.add(button,BorderLayout.CENTER);
			panel.setBackground(Color.lightGray);
			///panel.setOpaque(true);
			add(panel);
			panel_array[a-1][b-1] = panel;
		}else {
			JPanel panel_o = panel_array[a-1][b-1];
			panel_o.setVisible(false);
			//panel.removeAll();
			JPanel panel = new JPanel();
			panel.setOpaque(false);
			panel.setPreferredSize(new Dimension(45,50));
			panel.setBorder(new LineBorder(Color.black,1));
			gbc.gridx = 8-(a-1);//左右反転
			gbc.gridy = b-1;
			gbl.setConstraints(panel, gbc);
			///駒の画像を表示させる。
			ImageIcon icon = Make_koma_image.make_koma_image(koma.get_koma_number(), koma.get_teban());
			Image image = icon.getImage().getScaledInstance(36, 40, Image.SCALE_DEFAULT);
			icon = new ImageIcon(image);
			JButton button = new JButton(icon);
			button.setContentAreaFilled(false);//背景透明化
			button.setBorderPainted(false);
			//actionlistener加える
			button.addActionListener(action_listener);
			button.setActionCommand(""+a+""+b);//盤の番号。76とか
			panel.add(button,BorderLayout.CENTER);
			panel.setBackground(Color.lightGray);
			///panel.setOpaque(true);
			add(panel);
			panel_array[a-1][b-1] = panel;
		}
	}
	public void change_color(int a,int b) {
		if(0<a && a<10 && 0<b && b<10) {
			JPanel panel = panel_array[a-1][b-1];
			if(panel != null) {
				panel.setOpaque(true);
			}
		}
	}
	/*
	public void change_color_back(int a,int b) {
		System.out.println("/////////");
		if(0<a && a<10 && 0<b && b<10) {
			JPanel panel = panel_array[a-1][b-1];
			if(panel != null) {
				panel.setOpaque(false);
			}
		}
	}
	*/
	public void set_ban_panel() {
		setBounds(220,50,415,460);//横、縦、幅、高さ
		setBackground(new Color(153,102,0));
		GridBagLayout gbl = new GridBagLayout();
		grid_bag_layout = gbl;
		setLayout(gbl);
		make_masu(gbl);
		panel_array = new JPanel[9][9];//00-88,11なら00,76なら65
		for(int i=0;i<9;i++) {
			for(int j=0;j<9;j++) {
				panel_array[i][j] = null;
			}
		}
	}
	private void make_masu(GridBagLayout gbl) {
		GridBagConstraints gbc = new GridBagConstraints();
		for(int i=1;i<10;i++) {
			for(int j=1;j<10;j++) {
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
	/**
	 * Banを受け取ってその局面通りに表示させる。
	 * 新しいパネルを一つずつ作って行く
	 * @param ban
	 */
	public void make_masu_from_ban(Ban ban,Action_listener action_listener) {
		removeAll();//前の情報は消す。
		set_ban_panel();
		GridBagLayout gbl = grid_bag_layout;
		GridBagConstraints gbc = new GridBagConstraints();
		for(int i=1;i<10;i++) {
			for(int j=1;j<10;j++) {
				Koma koma = ban.get_banarray(i, j);
				if(koma != null) {
					JPanel panel = new JPanel();
					panel.setOpaque(false);
					panel.setPreferredSize(new Dimension(45,50));
					panel.setBorder(new LineBorder(Color.black,1));
					gbc.gridx = 8-(i-1);//左右反転
					gbc.gridy = j-1;
					gbl.setConstraints(panel, gbc);
					///駒の画像を表示させる。
					ImageIcon icon = Make_koma_image.make_koma_image(koma.get_koma_number(), koma.get_teban());
					Image image = icon.getImage().getScaledInstance(36, 40, Image.SCALE_DEFAULT);
					icon = new ImageIcon(image);
					JButton button = new JButton(icon);
					button.setContentAreaFilled(false);//背景透明化
					button.setBorderPainted(false);
					//actionlistener加える
					button.addActionListener(action_listener);
					button.setActionCommand(""+i+""+j);//盤の番号。76とか
					panel.add(button,BorderLayout.CENTER);
					panel.setBackground(Color.lightGray);
					///panel.setOpaque(true);
					add(panel);
					panel_array[i-1][j-1] = panel;
				}else {
					//駒のないところにもボタンはつくる
					JPanel panel = new JPanel();
					panel.setOpaque(false);
					panel.setPreferredSize(new Dimension(45,50));
					panel.setBorder(new LineBorder(Color.black,1));
					gbc.gridx = 8-(i-1);//左右反転
					gbc.gridy = j-1;
					gbl.setConstraints(panel, gbc);
					JButton button = new JButton();
					//button.setContentAreaFilled(false);//背景透明化
					//button.setBorderPainted(false);///
					button.setPreferredSize(new Dimension(45,50));
					button.setBackground(Color.black);///
					//actionlistener加える
					button.addActionListener(action_listener);
					button.setActionCommand(""+i+""+j);//盤の番号。76とか
					panel.add(button,BorderLayout.CENTER);
					panel.setBackground(Color.lightGray);
					///panel.setOpaque(true);
					add(panel);
					panel_array[i-1][j-1] = panel;
				}
			}
		}
	}

}
