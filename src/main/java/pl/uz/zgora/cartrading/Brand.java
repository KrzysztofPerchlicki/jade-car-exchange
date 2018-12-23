package pl.uz.zgora.cartrading;

public enum Brand {
	FIAT("Fiat"), AUDI("Audi"), VOLKSWAGEN("Volkswagen");

	private final String name;

	Brand(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
