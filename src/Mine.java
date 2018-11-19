
public class Mine {
	int column;
	int row;
	//지뢰정보
	int min_su;
	Integer mine_pos[];
	Integer notmine_pos[];

	//생성자
	public Mine(int min_su, int column, int row) {
		this.min_su = min_su;
		this.column = column;
		this.row = row;
		
		setMine();
	}
	
	//지뢰넣기
	public void setMine() {
		mine_pos = new Integer[min_su];
		for (int i = 0; i < min_su; i++) {
			mine_pos[i] = (int) (Math.random() * 81 + 1);
			for (int j = 0; j < i; j++) {
				if (mine_pos[i] == mine_pos[j]) {
					i--;
					break;
				}
			}
		}
	}
}
