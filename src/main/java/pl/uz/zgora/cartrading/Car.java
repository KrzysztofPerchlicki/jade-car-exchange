package pl.uz.zgora.cartrading;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Car {

	private Brand brand;
	private String model;
	private BodyType bodyType;
	private EngineType engineType;
	private Double engineCapacity;
	private Integer productionYear;
	private BigDecimal cost;
	private BigDecimal additionalCost;

}
