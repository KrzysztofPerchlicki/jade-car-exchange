package pl.uz.zgora.cartrading;

import java.util.Arrays;

public enum EngineType {

	GASOLINE, GAS, DIESEL;

	public static EngineType of(final String value) {
		return Arrays.stream(values())
			.filter(val -> val.name().equalsIgnoreCase(value)).findFirst()
			.orElseThrow(
				IllegalArgumentException::new);
	}
}
