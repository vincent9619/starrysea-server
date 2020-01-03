package top.starrysea.rina.jdbc.enums;

public enum WhereType {

	EQUALS("="), NOT_EQUALS("!="), GREATER(">"), GREATER_EQUAL(">="), LESS("<"), LESS_EQUAL("<=");

	private String chara;

	WhereType(String chara) {
		this.chara = chara;
	}

	public String getChara() {
		return chara;
	}
}
