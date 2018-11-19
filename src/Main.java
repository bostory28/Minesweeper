import javax.swing.JOptionPane;


public class Main {
	public static void main(String arg[]) {
		int mine_su = 0;
		mine_su = setNumberOfMine();
		Layout layout = new Layout(mine_su);		//화면 실행
	}
	
	//지뢰를 몇개 할 것인지
	public static int setNumberOfMine() {
		String mine_su_temp = null;
		int mine_su = 0;			//지뢰개수
		while (true) {
			try {
				mine_su_temp = JOptionPane.showInputDialog("지뢰찾기 게임입니다.\n지뢰를 몇개로 하시겠습니까?\n(5 ~ 15 사이의 수를 입력하시오.)");
				if (mine_su_temp != null) {
					mine_su = Integer.valueOf(mine_su_temp);
					if (mine_su > 15 || mine_su < 5) {
						throw new Exception();
					}
				}
				else {
					System.exit(0);
				}
				break;
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "숫자를 입력하시오.\n(5 ~ 15 사이의 수를 입력하시오.)"); 
			}
		}
		return mine_su;
	}
}
