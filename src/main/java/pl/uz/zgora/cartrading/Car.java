package pl.uz.zgora.cartrading;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Car {

	private String brand;
	private String model;
	private String bodyType;
	private String engineType;
	private String engineCapacity;
	private String productionYear;
	private String cost;
	private String additionalCost;

}
