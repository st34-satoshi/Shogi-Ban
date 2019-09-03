package swing;

import java.awt.Window;

import javax.swing.ImageIcon;

/**
 * 駒の数字から駒のImageを作る。
 * @author satoshi
 *
 */
public class Make_koma_image {

	public static ImageIcon make_koma_image(int koma_number,int teban, Window him) {
		ImageIcon image_icon = null;
//		ClassLoader cl = Make_koma_image.class.getClass().getClassLoader(); // to load image
		ClassLoader classloader = him.getClass().getClassLoader();
//		URL resUrl = classloader.getResource("hu.png"));
		switch(koma_number) {
		case 1:
			if(teban==1) {
				image_icon = new ImageIcon(classloader.getResource("koma_image/hu.png"));
//				image_icon = new ImageIcon("hu.png"));
			}else {
				image_icon = new ImageIcon(classloader.getResource("koma_image/hu_t.png"));
			}
			break;
		case 2:
			if(teban==1) {
				image_icon = new ImageIcon(classloader.getResource("koma_image/kyou.png"));
			}else {
				image_icon = new ImageIcon(classloader.getResource("koma_image/kyou_t.png"));
			}
			break;
		case 3:
			if(teban==1) {
				image_icon = new ImageIcon(classloader.getResource("koma_image/kei.png"));
			}else {
				image_icon = new ImageIcon(classloader.getResource("koma_image/kei_t.png"));
			}
			break;
		case 4:
			if(teban==1) {
				image_icon = new ImageIcon(classloader.getResource("koma_image/gin.png"));
			}else {
				image_icon = new ImageIcon(classloader.getResource("koma_image/gin_t.png"));
			}
			break;
		case 5:
			if(teban==1) {
				image_icon = new ImageIcon(classloader.getResource("koma_image/kin.png"));
			}else {
				image_icon = new ImageIcon(classloader.getResource("koma_image/kin_t.png"));
			}
			break;
		case 6:
			if(teban==1) {
				image_icon = new ImageIcon(classloader.getResource("koma_image/gyoku.png"));
			}else {
				image_icon = new ImageIcon(classloader.getResource("koma_image/gyoku_t.png"));
			}
			break;
		case 7:
			if(teban==1) {
				image_icon = new ImageIcon(classloader.getResource("koma_image/kaku.png"));
			}else {
				image_icon = new ImageIcon(classloader.getResource("koma_image/kaku_t.png"));
			}
			break;
		case 8:
			if(teban==1) {
				image_icon = new ImageIcon(classloader.getResource("koma_image/hisha.png"));
			}else {
				image_icon = new ImageIcon(classloader.getResource("koma_image/hisha_t.png"));
			}
			break;
		case 11:
			if(teban==1) {
				image_icon = new ImageIcon(classloader.getResource("koma_image/tokin.png"));
			}else {
				image_icon = new ImageIcon(classloader.getResource("koma_image/tokin_t.png"));
			}
			break;
		case 12:
			if(teban==1) {
				image_icon = new ImageIcon(classloader.getResource("koma_image/narikyou.png"));
			}else {
				image_icon = new ImageIcon(classloader.getResource("koma_image/narikyou_t.png"));
			}
			break;
		case 13:
			if(teban==1) {
				image_icon = new ImageIcon(classloader.getResource("koma_image/narikei.png"));
			}else {
				image_icon = new ImageIcon(classloader.getResource("koma_image/narikei_t.png"));
			}
			break;
		case 14:
			if(teban==1) {
				image_icon = new ImageIcon(classloader.getResource("koma_image/narigin.png"));
			}else {
				image_icon = new ImageIcon(classloader.getResource("koma_image/narigin_t.png"));
			}
			break;
		case 17:
			if(teban==1) {
				image_icon = new ImageIcon(classloader.getResource("koma_image/uma.png"));
			}else {
				image_icon = new ImageIcon(classloader.getResource("koma_image/uma_t.png"));
			}
			break;
		case 18:
			if(teban==1) {
				image_icon = new ImageIcon(classloader.getResource("koma_image/ryuu.png"));
			}else {
				image_icon = new ImageIcon(classloader.getResource("koma_image/ryuu_t.png"));
			}
			break;
		}
		return image_icon;
	}
}
