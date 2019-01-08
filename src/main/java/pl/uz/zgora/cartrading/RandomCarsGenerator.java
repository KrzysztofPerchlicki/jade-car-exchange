package pl.uz.zgora.cartrading;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomCarsGenerator {

	public static final List<Car> generateCars() {
		final List<Car> cars = new ArrayList<>();
		final Random random = new Random();
		final BodyType[] bodyTypes = BodyType.values();
		final EngineType[] engineTypes = EngineType.values();
		CarModelDictionary.CARS.forEach(carModel -> {
			final BodyType bodyType = bodyTypes[random.nextInt(bodyTypes.length)];
			final EngineType engineType = engineTypes[random.nextInt(engineTypes.length)];
			final int productionYear = carModel.getMinProductionYear() + random
				.nextInt(carModel.getMaxProductionYear() - carModel.getMinProductionYear());
			final BigDecimal cost = generateRandomBigDecimalFromRange(carModel.getMinCost(),
				carModel.getMaxCost());
			final BigDecimal additionalCost = generateRandomBigDecimalFromRange(
				carModel.getMinCost(),
				carModel.getMaxCost());

			cars.add(new Car(carModel.getBrand(), carModel.getModel(), bodyType,
				engineType, carModel.getEngineCapacity(), productionYear,
				cost, additionalCost));
		});
		return cars;
	}

	public static BigDecimal generateRandomBigDecimalFromRange(
		final BigDecimal min, final BigDecimal max) {
		final BigDecimal randomBigDecimal = min
			.add(BigDecimal.valueOf(Math.random()).multiply(max.subtract(min)));
		return randomBigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP);
	}

}
