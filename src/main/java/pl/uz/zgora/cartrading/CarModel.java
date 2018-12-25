package pl.uz.zgora.cartrading;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CarModel {

	private Brand brand;
	private String model;
	private Double engineCapacity;
	private Integer minProductionYear;
	private Integer maxProductionYear;
	private BigDecimal minCost;
	private BigDecimal maxCost;
	private BigDecimal minAdditionalCost;
	private BigDecimal maxAdditionalCost;
}
