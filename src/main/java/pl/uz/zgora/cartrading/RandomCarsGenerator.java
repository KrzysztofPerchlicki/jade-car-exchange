package pl.uz.zgora.cartrading;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomCarsGenerator {

	public static final List<Car> generateCars() {
		List<Car> cars = new ArrayList<>();
		Random random = new Random(100);
		BodyType[] bodyTypes = BodyType.values();
		EngineType[] engineTypes = EngineType.values();
		CarModelDictionary.CARS.forEach(carModel -> {
			BodyType bodyType = bodyTypes[random.nextInt(bodyTypes.length)];
			EngineType engineType = engineTypes[random.nextInt(engineTypes.length)];
			String productionYear = String.valueOf(carModel.getMinProductionYear() + random
				.nextInt(carModel.getMaxProductionYear() - carModel.getMinProductionYear()));
			String cost = generateRandomBigDecimalFromRange(carModel.getMinCost(),
				carModel.getMaxCost()).toPlainString();
			String additionalCost = generateRandomBigDecimalFromRange(carModel.getMinCost(),
				carModel.getMaxCost()).toPlainString();

			cars.add(new Car(carModel.getBrand().name(), carModel.getModel(), bodyType.name(),
				engineType.name(), String.valueOf(carModel.getEngineCapacity()), productionYear,
				cost, additionalCost));
		});
		return cars;
	}

	public static BigDecimal generateRandomBigDecimalFromRange(BigDecimal min, BigDecimal max) {
		BigDecimal randomBigDecimal = min
			.add(BigDecimal.valueOf(Math.random()).multiply(max.subtract(min)));
		return randomBigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP);
	}

}
