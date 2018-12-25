package pl.uz.zgora.cartrading;

import java.util.Arrays;

public enum Brand {
	FIAT("Fiat"), AUDI("Audi"), VOLKSWAGEN("Volkswagen");

	private final String name;

	Brand(final String name) {
		this.name = name;
	}

	public static Brand of(final String value) {
		return Arrays.stream(values())
			.filter(val -> val.name().equalsIgnoreCase(value)).findFirst()
			.orElseThrow(
				IllegalArgumentException::new);
	}

	public String getName() {
		return name;
	}
}
