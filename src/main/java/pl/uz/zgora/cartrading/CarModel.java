package pl.uz.zgora.cartrading;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CarModel {

	private Brand brand;
	private String model;
	private double engineCapacity;
	private int minProductionYear;
	private int maxProductionYear;
	private BigDecimal minCost;
	private BigDecimal maxCost;
	private BigDecimal minAdditionalCost;
	private BigDecimal maxAdditionalCost;
}
