package src;

public class Company {
	static String name;
	static String ticker_Number;
	static String peRatio;
	static String marketCap;
	static String latestPrice;
	
	Company () {
	}
	
	public Company(String n, String t, String pe, String m, String lp) {
		name = n;
		ticker_Number = t;
		peRatio = pe;
		marketCap = m;
		latestPrice = lp;
	}
	
	public static String getName() {
		return name;
	}
	
	public static String getTicker() {
		return ticker_Number;
	}
	
	public static String getpeRatio() {
		return peRatio;
	}
	
	public static String marketCap() {
		return marketCap;
	}
	
	public static String getLatestPrice() {
		return latestPrice;
	}

}
