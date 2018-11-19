import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;



public class Layout extends JFrame implements MouseListener {
	//틀구성
	int column = 9;
	int row = 9;
	Container con = this.getContentPane();
	GridLayout grid = new GridLayout(column, row);
	JButton bt_mine[][] = new JButton[column][row];
	
	//지뢰정보
	Mine mine;
	int mine_su = 0;		//지뢰수
	int count = 0;			//지뢰를 찾은 개수
	
	public Layout(int mine_su) {
		this.mine_su = mine_su;
		mine = new Mine(mine_su, column, row);	//지뢰넣기
		
		super.setTitle("지뢰찾기");
		
		init();
		start();
		
		super.setSize(500, 500);
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frm = this.getSize();
		int x = d.width/2 - frm.width/2;
		int y = d.height/2 - frm.height/2;
		super.setLocation(x, y);
		super.setVisible(true);
	}

	//초기값설정
	public void init() {
		con.setLayout(grid);
		
		//button의 초기값
		int row_mine;
		int column_mine;
		for (int i = 0; i < column; i++) {
			for (int j = 0; j < row; j++) {
				bt_mine[i][j] = new JButton("0");
			}
		}
		//지뢰를 button에 넣는다.
		for (int j = 0; j < mine_su; j++) {
			if (mine.mine_pos[j] % row == 0) {
				row_mine = mine.mine_pos[j] / row - 1; 
			} 
			else {
				row_mine = mine.mine_pos[j] / row; 
			}
			column_mine = mine.mine_pos[j] % row - 1;
			if (column_mine == -1) {
				column_mine = 8;
			}
			System.out.print("지뢰위치 :  (");
			System.out.print(row_mine + 1);
			System.out.print("행 , ");
			System.out.print(column_mine+1);
			System.out.println(")");
			Font font = new Font("돋움", Font.BOLD, 8);			//지뢰의 font 설정
			bt_mine[row_mine][column_mine].setText("지뢰");
			bt_mine[row_mine][column_mine].setFont(font);
			
		}
		
		//지뢰주변의 값을 +1을 해준다.
		for (int r = 0; r < row; r++) {
			for (int c = 0; c < column; c++) {
				if (bt_mine[r][c].getText().equals("지뢰")) {
					//지뢰가 틀 밖으로 넘어가는 것을 방지하기 위해
					int start_r = (r - 1 < 0) ? r : r - 1;
					int end_r = (r + 1 < row) ? r + 1 : r;
					int start_c = (c - 1 < 0) ? c : c - 1;
					int end_c = (c + 1 < column) ? c + 1 : c;
					
					for (int _r = start_r; _r <= end_r; _r++) {
						for (int _c = start_c; _c <= end_c; _c++) {
							if (bt_mine[_r][_c].getText().equals("지뢰")) {
								continue;
							}
							bt_mine[_r][_c].setText((Integer.valueOf(bt_mine[_r][_c].getText())+1)+"");
						}
					}
				}
				bt_mine[r][c].setBackground(Color.WHITE);
				bt_mine[r][c].setForeground(Color.WHITE);
				con.add(bt_mine[r][c]);
			}
		}
	}
	
	//이벤트 헨들링
	public void start() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < column; j++) {
				bt_mine[i][j].addMouseListener(this);
			}
		}
	}

	
	@Override
	public void mouseClicked(MouseEvent e) {
		//오른쪽 마우스 클릭 시
		if (e.isMetaDown()) {
			Object obj = e.getSource();
			for (int i = 0; i < row; i++) {
				for (int j = 0; j < column; j++) {
					if (obj == bt_mine[i][j]) {
						if (bt_mine[i][j].getForeground() != Color.RED) {		//오른쪽 버튼 두번 선택했을 때 제한하기 위해
							if (bt_mine[i][j].getText().equals("지뢰")) {
								bt_mine[i][j].setText("지뢰");
								bt_mine[i][j].setForeground(Color.RED);
								count++;
								System.out.println("지뢰 찾은 개수 : " + count);
							}
							else {
								int result = 0;
								result = JOptionPane.showConfirmDialog(null,"지뢰가 이닙니다.\n게임을 계속하시겠습니까", "종료", JOptionPane.YES_NO_OPTION);
								if (result != 0) {
									dispose();
								}
								else {
									dispose();
									mine_su = Main.setNumberOfMine();
									new Layout(mine_su);
								}
							}
							if (count == mine_su) {
								for (int r = 0; r < row; r++) {
									for (int c = 0; c < column; c++) {
										setFontOfColor(r, c);
									}
								}
								int result = 0;
								result = JOptionPane.showConfirmDialog(null,"모든 지뢰를 찾았습니다.\n다시 시작하시겠습니까", "종료", JOptionPane.YES_NO_OPTION);
								if (result == 0) {
									dispose();
									mine_su = Main.setNumberOfMine();
									new Layout(mine_su);
								}
								else {
									dispose();
								}
							}
						}
					}
				}
			}
		}
		//왼쪽 마우스 클릭 시
		else {
			JButton b = (JButton) e.getSource();
			Object obj = e.getSource();
			//0을 선택했을 때 주변의 0까지 찾는다.
			if (b.getText().equals("0")) {
				for (int i = 0; i < row; i++) {
					for (int j = 0; j < column; j++) {
						if (obj == bt_mine[i][j]) {
							selfFunction(i, j);				//재귀함수호출
						}
					}
				}
			}
			//지뢰를 선택했을 때
			else if (b.getText().equals("지뢰")) {
				int result = 0;
				result = JOptionPane.showConfirmDialog(null,"게임오버입니다.\n게임을 계속하시겠습니까", "종료", JOptionPane.YES_NO_OPTION);
				if (result != 0) {
					dispose();
				}
				else {
					dispose();
					mine_su = Main.setNumberOfMine();
					new Layout(mine_su);
				}
			}
			//0이 아닌 숫자를 선택했을 때
			else if (!b.getText().equals("0")) {
				for (int i = 0; i < row; i++) {
					for (int j = 0; j < column; j++) {
						if (obj == bt_mine[i][j]) {
							setFontOfColor(i, j);
						}
					}
				}
			}
		}
	}
	
	//선택한 0주변에 있는 0의 위치를 찾기 위한 재귀함수
	public void selfFunction(int i, int j) {
		int start_r = (i - 1 < 0) ? i : i - 1;
		int end_r = (i + 1 < row) ? i + 1 : i;
		int start_c = (j - 1 < 0) ? j : j - 1;
		int end_c = (j + 1 < column) ? j + 1 : j;
		for (int r = start_r; r <= end_r; r++) {
			for (int c = start_c; c <= end_c; c++) {
				if (!bt_mine[r][c].getText().equals("0")) {
					setFontOfColor(r, c);
				}
				else {
					if (r == i && c == j || bt_mine[r][c].getBackground() == Color.gray) {
						bt_mine[r][c].setBackground(Color.gray);
						bt_mine[r][c].setForeground(Color.gray);
					}
					else {
						bt_mine[r][c].setBackground(Color.gray);
						selfFunction(r, c);
					}
				}
			}
		}
	}
	
	//숫자와 지뢰에 색깔을 지정한다.
	public void setFontOfColor(int r, int c) {
		switch (bt_mine[r][c].getText()) {
		case "0":
			bt_mine[r][c].setBackground(Color.gray);
			bt_mine[r][c].setForeground(Color.gray);
			break;
		case "지뢰":
			bt_mine[r][c].setForeground(Color.RED);
			break;
		case "1":
			bt_mine[r][c].setForeground(Color.DARK_GRAY);
			break;
		case "2":
			bt_mine[r][c].setForeground(Color.BLUE);
			break;
		case "3":
			bt_mine[r][c].setForeground(Color.GREEN);
			break;
		case "4":
			bt_mine[r][c].setForeground(Color.ORANGE);
			break;
		case "5":
			bt_mine[r][c].setForeground(Color.PINK);
			break;
		case "6":
			bt_mine[r][c].setForeground(Color.YELLOW);
			break;
		case "7":
			bt_mine[r][c].setForeground(Color.CYAN);
			break;
		case "8":
			bt_mine[r][c].setForeground(Color.magenta);
			break;
		}
	}
	
	@Override
	public void mousePressed(MouseEvent e) {}
	@Override
	public void mouseReleased(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
}
