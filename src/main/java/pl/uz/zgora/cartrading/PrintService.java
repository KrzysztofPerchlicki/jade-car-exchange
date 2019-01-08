package pl.uz.zgora.cartrading;

public class PrintService {

	public synchronized static void print(final String message) {
		System.out.println(message);
	}

}
