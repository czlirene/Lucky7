package test; 
public class Cow {
	public static String yell() {
		return "Moo";
	}
	
	public int gotMilk() {
		return 0;
	} 
	
	public static void main (String[] args) {
		Cow betty = new Cow(); 
		String sound = Cow.yell(); 
		int milk = betty.gotMilk(); 
		String[] a = new String[1];
	}
}

