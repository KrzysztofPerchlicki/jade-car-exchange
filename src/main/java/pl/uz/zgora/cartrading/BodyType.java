package pl.uz.zgora.cartrading;

import java.util.Arrays;

public enum BodyType {

	HATCHBACK, KOMBI, SEDAN, SUV, COUPE;

	public static BodyType of(final String value) {
		return Arrays.stream(values())
			.filter(val -> val.name().equalsIgnoreCase(value)).findFirst()
			.orElseThrow(
				IllegalArgumentException::new);
	}
}
