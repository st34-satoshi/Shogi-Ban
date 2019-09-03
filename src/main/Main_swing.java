package main;

import java.util.ArrayList;

import board.Board;
import board.Kyokumen;
import swing.Action_kihu_listener;
import swing.Action_listener;
import swing.Action_listener_menu;
import swing.Frame_s;
import swing.State_game;

public class Main_swing{

	public static void main(String[] args) {
		Frame_s frame = new Frame_s();
		Board board = new Board(new Kyokumen(),new Player(),new ArrayList<Kyokumen>());
		State_game state = new State_game(board,frame);
		Action_listener action_listener = new Action_listener(board,frame,state);
		Action_listener_menu action_listener_menu = new Action_listener_menu(board,frame,state);
		Action_kihu_listener action_kihu_listener = new Action_kihu_listener(board, frame, state);
		frame.set_action_listener(action_listener, action_listener_menu,action_kihu_listener);
		frame.swing2();
		state.set_not_gaming();
	}


}
