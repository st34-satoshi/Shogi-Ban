package swing;

import javax.swing.ImageIcon;

/**
 * 駒の数字から駒のImageを作る。
 * @author satoshi
 *
 */
public class Make_koma_image {

	public static ImageIcon make_koma_image(int koma_number,int teban) {
		ImageIcon image_icon = null;
		String file_path = "../koma_image/";
		switch(koma_number) {
		case 1:
			if(teban==1) {
				image_icon = new ImageIcon(file_path+"hu.png");
			}else {
				image_icon = new ImageIcon(file_path+"hu_t.png");
			}
			break;
		case 2:
			if(teban==1) {
				image_icon = new ImageIcon(file_path+"kyou.png");
			}else {
				image_icon = new ImageIcon(file_path+"kyou_t.png");
			}
			break;
		case 3:
			if(teban==1) {
				image_icon = new ImageIcon(file_path+"kei.png");
			}else {
				image_icon = new ImageIcon(file_path+"kei_t.png");
			}
			break;
		case 4:
			if(teban==1) {
				image_icon = new ImageIcon(file_path+"gin.png");
			}else {
				image_icon = new ImageIcon(file_path+"gin_t.png");
			}
			break;
		case 5:
			if(teban==1) {
				image_icon = new ImageIcon(file_path+"kin.png");
			}else {
				image_icon = new ImageIcon(file_path+"kin_t.png");
			}
			break;
		case 6:
			if(teban==1) {
				image_icon = new ImageIcon(file_path+"gyoku.png");
			}else {
				image_icon = new ImageIcon(file_path+"gyoku_t.png");
			}
			break;
		case 7:
			if(teban==1) {
				image_icon = new ImageIcon(file_path+"kaku.png");
			}else {
				image_icon = new ImageIcon(file_path+"kaku_t.png");
			}
			break;
		case 8:
			if(teban==1) {
				image_icon = new ImageIcon(file_path+"hisha.png");
			}else {
				image_icon = new ImageIcon(file_path+"hisha_t.png");
			}
			break;
		case 11:
			if(teban==1) {
				image_icon = new ImageIcon(file_path+"tokin.png");
			}else {
				image_icon = new ImageIcon(file_path+"tokin_t.png");
			}
			break;
		case 12:
			if(teban==1) {
				image_icon = new ImageIcon(file_path+"narikyou.png");
			}else {
				image_icon = new ImageIcon(file_path+"narikyou_t.png");
			}
			break;
		case 13:
			if(teban==1) {
				image_icon = new ImageIcon(file_path+"narikei.png");
			}else {
				image_icon = new ImageIcon(file_path+"narikei_t.png");
			}
			break;
		case 14:
			if(teban==1) {
				image_icon = new ImageIcon(file_path+"narigin.png");
			}else {
				image_icon = new ImageIcon(file_path+"narigin_t.png");
			}
			break;
		case 17:
			if(teban==1) {
				image_icon = new ImageIcon(file_path+"uma.png");
			}else {
				image_icon = new ImageIcon(file_path+"uma_t.png");
			}
			break;
		case 18:
			if(teban==1) {
				image_icon = new ImageIcon(file_path+"ryuu.png");
			}else {
				image_icon = new ImageIcon(file_path+"ryuu_t.png");
			}
			break;
		}
		return image_icon;
	}
}
