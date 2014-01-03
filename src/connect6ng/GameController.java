package connect6ng;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.logging.*;

import javax.swing.*;


/**
 * The main window with AI kernel.
 */
@SuppressWarnings("serial")
class GameController extends JFrame {
	GameModel game_model;
	GameView game_view;
	
	MusicPlayer playBackground;

	// Vector<MyPoint> data;
	Alg kernel;

	// 菜单栏
	JMenuBar menu_JMenuBar = new JMenuBar();
	JMenu menu_game = new JMenu("游戏");
	JMenu menu_ngame = new JMenu("开始新游戏");
	JMenuItem menu_first = new JMenuItem("先手(玩家黑棋先行)");
	JMenuItem menu_last = new JMenuItem("后手(玩家白棋后行)");
	JMenuItem menu_restart = new JMenuItem("重新开始游戏");
	JMenuItem menu_reset = new JMenuItem("重置游戏");
	JCheckBoxMenuItem menu_comp = new JCheckBoxMenuItem("人机对战");
	JMenuItem menu_prac = new JMenuItem("切换为实战模式");
	JMenuItem menu_int = new JMenuItem("暂停游戏");
	JMenuItem menu_exit = new JMenuItem("退出");
	JMenuItem menu_back = new JMenuItem("悔棋");
	JMenu menu_file = new JMenu("游戏战绩");
	JMenuItem menu_open = new JMenuItem("打开游戏战绩");
	JMenuItem menu_save = new JMenuItem("保存游戏战绩");

	// 帮助：
	JMenu menu_help = new JMenu("帮助");
	JMenuItem menu_AboutSixChess = new JMenuItem("关于六子棋");
	JMenuItem menu_SeekHelp = new JMenuItem("查看帮助");
	JMenuItem menu_CheckUpdate = new JMenuItem("检查更新");
	
	/** Class constructor. */
	GameController() {
		super("Connect 6 - 游戏尚未开始 - 练习模式");
		setSize(650, 680);
		setResizable(false);

		// 初始化数据
		game_model = new GameModel();

		kernel = new Alg(game_model.getChessmans());

		// 菜单栏及按钮监听
		setJMenuBar(menu_JMenuBar);
		menu_JMenuBar.add(menu_game);
		menu_game.add(menu_ngame);
		menu_ngame.add(menu_first);
		menu_first.addActionListener(new ack_menu_first());
		// menu_first.setAccelerator( KeyStroke.getKeyStroke((char)
		// KeyEvent.VK_F2) );
		menu_ngame.add(menu_last);
		menu_last.addActionListener(new ack_menu_last());
		// menu_last.setShortcut(new JMenuShortcut(KeyEvent.VK_F3));
		menu_game.add(menu_restart);
		menu_restart.addActionListener(new ack_menu_restart());
		menu_restart.setEnabled(false);
		// menu_restart.setShortcut(new JMenuShortcut(KeyEvent.VK_F5));
		menu_game.add(menu_reset);
		menu_reset.addActionListener(new ack_menu_reset());
		// menu_reset.setShortcut(new JMenuShortcut(KeyEvent.VK_F1));
		menu_game.add(menu_comp);
		menu_comp.addItemListener(new ack_menu_comp());
		menu_comp.setState(true);
		menu_game.add(menu_prac);
		menu_prac.addActionListener(new ack_menu_prac());
		menu_game.add(menu_int);
		menu_int.addActionListener(new ack_menu_int());
		menu_int.setEnabled(false);
		menu_int.setAccelerator(KeyStroke.getKeyStroke('I',
				java.awt.Event.CTRL_MASK, false));
		menu_game.add(menu_back);
		menu_back.addActionListener(new ack_menu_back());
		menu_back.setEnabled(false);
		menu_back.setAccelerator(KeyStroke.getKeyStroke('B',
				java.awt.Event.CTRL_MASK, false));
		menu_game.add(menu_exit);
		menu_exit.addActionListener(new ack_menu_exit());
		menu_exit.setAccelerator(KeyStroke.getKeyStroke('E',
				java.awt.Event.CTRL_MASK, false));
		menu_JMenuBar.add(menu_file);
		menu_file.add(menu_open);
		menu_open.addActionListener(new ack_menu_open());
		menu_open.setAccelerator(KeyStroke.getKeyStroke('O',
				java.awt.Event.CTRL_MASK, false));
		menu_file.add(menu_save);
		menu_save.addActionListener(new ack_menu_save());
		menu_save.setAccelerator(KeyStroke.getKeyStroke('S',
				java.awt.Event.CTRL_MASK, false));

		// 帮助：
		menu_JMenuBar.add(menu_help);		
		
		menu_help.add(menu_AboutSixChess);
		menu_AboutSixChess.addActionListener(new ack_menu_AboutSixChess());
		menu_AboutSixChess.setAccelerator(KeyStroke.getKeyStroke('O',
				java.awt.Event.CTRL_MASK, false));
		
		menu_help.add(menu_SeekHelp);
		menu_SeekHelp.addActionListener(new ack_menu_SeekHelp());
		menu_SeekHelp.setAccelerator(KeyStroke.getKeyStroke('O',
				java.awt.Event.CTRL_MASK, false));
		
		menu_help.add(menu_CheckUpdate);
		menu_CheckUpdate.addActionListener(new ack_menu_CheckUpdate());
		menu_CheckUpdate.setAccelerator(KeyStroke.getKeyStroke('O',
				java.awt.Event.CTRL_MASK, false));

		// 窗口监听
		addWindowListener(new Wclose());
		// 监听鼠标
		addMouseListener(new amouse());

		Container cont = getContentPane();

		game_model = new GameModel();
		game_view = new GameView(game_model);
		game_model.addObserver(game_view);
		game_view.setVisible(true);

		cont.add(game_view);

		Vector<MyPoint> data = game_model.getChessmans();
		kernel.setData(data);

		repaint();
		setTitle();
		setLocationRelativeTo(null);
		setVisible(true);

		// 背景音乐播放：
		String music_state = "play_loop";
		playBackground = new MusicPlayer(music_state , "background.wav");
	}

	/**
	 * 界面绘制。
	 * 
	 * @param g
	 */
	public void paint(Graphics g) {
		super.paint(g);
	}

	class ack_menu_first implements ActionListener {
		// 新游戏->先手 事件响应
		public void actionPerformed(ActionEvent e) {
			menu_first.setEnabled(false);
			menu_last.setEnabled(true);
			game_model.setColor(0);
			game_model.newGame();
		}
	}

	class ack_menu_last implements ActionListener {
		// 新游戏->后手 事件响应
		public void actionPerformed(ActionEvent e) {
			menu_first.setEnabled(true);
			menu_last.setEnabled(false);
			game_model.setColor(1);
			game_model.newGame();
			if (game_model.getComputer()) {
				game_model.AddChessman(new MyPoint(9, 9, 0));
			}
		}
	}

	class ack_menu_restart implements ActionListener {
		// 重新开始游戏 事件响应
		public void actionPerformed(ActionEvent e) {
		}
	}

	class ack_menu_reset implements ActionListener {
		// 重置游戏 事件响应
		public void actionPerformed(ActionEvent e) {
		}
	}

	class ack_menu_comp implements ItemListener {
		// 选择或取消人机对战功能 事件响应
		// 选择或取消人机对战功能 事件响应
		public void itemStateChanged(ItemEvent e) {
			int color = game_model.getColor();
			menu_first.setEnabled(color == 1);
			menu_last.setEnabled(color == 0);
			System.out.println("first : " + (color == 1) + "  last : "
					+ (color == 0));

			Boolean computer = menu_comp.getState();
			game_model.setComputer(computer);
			game_model.newGame();
		}
	}

	class ack_menu_prac implements ActionListener {
		// 模式选择 事件响应
		public void actionPerformed(ActionEvent e) {
		}
	}

	class ack_menu_back implements ActionListener {
		// 悔棋 事件响应
		public void actionPerformed(ActionEvent e) {
		}
	}

	class ack_menu_exit implements ActionListener {
		// 退出 事件响应
		public void actionPerformed(ActionEvent e) {
			// TODO
			// history
			System.exit(0);
		}
	}

	class ack_menu_int implements ActionListener {
		// 暂停游戏事件响应
		public void actionPerformed(ActionEvent e) {
		}
	}

	class ack_menu_save implements ActionListener {
		// 保存 事件响应
		public void actionPerformed(ActionEvent e) {
			FileDialog myFileDialog = new FileDialog(GameController.this,
					"save", FileDialog.SAVE);
			myFileDialog.setVisible(true);
			String dir = myFileDialog.getDirectory();
			String fname = myFileDialog.getFile();
			if ((dir != null) && (fname != null)) {
				String fullFileName = (new String(fname)).toLowerCase();
				if (!fullFileName.endsWith(".c6db")) {
					fullFileName = fullFileName + ".c6db";
				}
				fullFileName = dir + fullFileName;

				try {
					FileOutputStream file = new FileOutputStream(fullFileName);
					ObjectOutputStream output = new ObjectOutputStream(file);
					try {
						// output.writeObject(data);
					} finally {
						output.close();
					}
				} catch (IOException ex) {
					fLogger.log(Level.WARNING,
							"Failed to perform output when saving.", ex);
					popupMessageBox("文件打开失败", "请确保有足够权限。");
				}
			}
		}
	}

	class ack_menu_open implements ActionListener {
		// 打开 事件响应
		public void actionPerformed(ActionEvent e) {
		}
	}
	
	/**
	 * 关于六子棋
	 * @author lujx
	 *
	 */
	class ack_menu_AboutSixChess implements ActionListener{		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			
			AboutDialog d = new AboutDialog( GameController.this, true );
			d.setLocationRelativeTo(null);
			d.setVisible(true);
		}
	}
	
	/**
	* 查看帮助
	* @author lujx
	*
	*/
	class ack_menu_SeekHelp implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent arg0) {			
			SeekHelpDialog d = new SeekHelpDialog( GameController.this );
			d.dialog.setLocationRelativeTo(null);
			d.dialog.setVisible(true);
		}
	}
	
	/**
	* 软件更新
	* @author lujx
	*
	*/
	class ack_menu_CheckUpdate implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent arg0) {				
			CheckUpdateDialog d = new CheckUpdateDialog( GameController.this );
			d.dialog.setLocationRelativeTo(null);
			d.dialog.setVisible(true);
		}	
	}


	/**
	 * Sets the title of the frame window to reflect different status.
	 */
	void setTitle() {
	}

	/**
	 * Popup a dialog box to show result message.
	 * 
	 * @param msg
	 *            the message to be shown in the dialog box
	 */
	void popupMessageBox(String msg, String title) {
		final JDialog myDialog = new JDialog(this, title, true);
		myDialog.setSize(180, 100);
		myDialog.setLayout(new FlowLayout(FlowLayout.CENTER, 1000, 10));
		myDialog.add(new JLabel(msg));
		JButton myDialogBotton = new JButton("确定");
		myDialog.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				myDialog.dispose();
			}
		});
		myDialog.add(myDialogBotton);
		myDialog.setLocationRelativeTo(null);
		myDialog.setResizable(false);
		myDialogBotton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				myDialog.dispose();
			}
		});
		myDialog.setVisible(true);
	}

	class Wclose extends WindowAdapter {
		// 窗口关闭
		public void windowClosing(WindowEvent e) {
			System.exit(0);
		}
	}

	class amouse extends MouseAdapter {
		// 鼠标在棋盘上点击 事件响应
		public void mouseClicked(MouseEvent e) {

			// 分析无效操作，包括非法点击，点击不可靠等
			if (!game_model.playerTurn()) {
				// TODO
				// add error music here
				System.out.println("Not your turn");
				game_model.display();
				return;
			}

			int x = e.getX(), y = e.getY();
			x = (int) (x - getInsets().left - getContentPane().getLocation()
					.getX());
			y = (int) (y - getInsets().top - getContentPane().getLocation()
					.getY());

			System.out.println("clicked at point : " + x + "," + y);

			int x_start = 22;
			int y_start = 22;
			int chess_size = 30;

			x = x - x_start;
			y = y - y_start;
			if ((x % 30) > 25 || (y % 30) > 25) {
				// TODO
				return;
			}

			if ((x < 0) || (x > 18*chess_size) || (y < 0) || (y > 18*chess_size)) {
				// TODO
				return;
			}
			System.out.println("clicked at point : " + x + "," + y);

			x = x / chess_size;
			y = y / chess_size;
			System.out.println("clicked at chess : " + x + "," + y);

			// 玩家turn
			int color = game_model.getCurrentColor();
			game_model.getClickedAt(x, y);
			setEnabled(false);

			kernel.setData(game_model.getChessmans());
			if (kernel.hasSix()) {
				if (game_model.getComputer()) {
					popupMessageBox("恭喜你战胜了电脑！！！", "游戏胜利");
					game_model.newGame();
				} else {
					String[] winner = { "黑子", "白子" };
					popupMessageBox(winner[color] + "获胜！！！", "游戏结束");
					game_model.newGame();
				}
			}

			if (game_model.getComputer()) {

				if (!game_model.playerTurn()) {

					// 电脑turn
					kernel.placeTwoStones(game_model.getCurrentColor());

					game_model.setChessMan(kernel.getData());
					// TODO
					// draw hint

					if (kernel.hasSix()) {
						popupMessageBox("你失败了，再接再厉！！！", "游戏失败");
						game_model.newGame();
					}
					setEnabled(true);
				}
			}
			setEnabled(true);

		}
	}

	private static final Logger fLogger = Logger.getLogger(GameController.class
			.getPackage().getName());
}
