package com.innoplus.javaapitables.data.options;

public class StockScreener {

	private String ticker;
	private String columnF;
	private String columnG;
	private String columnJ;
	private String columnD;
	
	private String columnN;
	private String columnO;
	
	public StockScreener() {
		super();
		// TODO Auto-generated constructor stub
	}

	public StockScreener(String ticker, String columnF, String columnG,
			String columnJ, String columnD, String columnN, String columnO) {
		super();
		this.ticker = ticker;
		this.columnF = columnF;
		this.columnG = columnG;
		this.columnJ = columnJ;
		this.columnD = columnD;
		this.columnN = columnN;
		this.columnO = columnO;
	}

	public String getTicker() {
		return ticker;
	}

	public void setTicker(String ticker) {
		this.ticker = ticker;
	}

	public String getColumnF() {
		return columnF;
	}

	public void setColumnF(String columnF) {
		this.columnF = columnF;
	}

	public String getColumnG() {
		return columnG;
	}

	public void setColumnG(String columnG) {
		this.columnG = columnG;
	}

	public String getColumnJ() {
		return columnJ;
	}

	public void setColumnJ(String columnJ) {
		this.columnJ = columnJ;
	}

	public String getColumnD() {
		return columnD;
	}

	public void setColumnD(String columnD) {
		this.columnD = columnD;
	}

	public String getColumnN() {
		return columnN;
	}

	public void setColumnN(String columnN) {
		this.columnN = columnN;
	}

	public String getColumnO() {
		return columnO;
	}

	public void setColumnO(String columnO) {
		this.columnO = columnO;
	}

}
