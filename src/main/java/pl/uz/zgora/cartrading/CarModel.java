package pl.uz.zgora.cartrading;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CarModel {

	private Brand brand;
	private String model;
	private BigDecimal engineCapacity;
	private int minProductionYear;
	private int maxProductionYear;
	private BigDecimal minCost;
	private BigDecimal maxCost;
	private BigDecimal minAdditionalCost;
	private BigDecimal maxAdditionalCost;
}
