package pl.uz.zgora.cartrading;

import dnl.utils.text.table.TextTable;
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

	public void print() {
		final String[] columnNames = new String[]{"Brand", "Model", "Engine type", "Body type",
			"Engine capacity", "Production year", "Cost", "Additional cost"};
		final Object[][] data = new String[][]{
			{brand.name(),
				model,
				engineType.name(),
				bodyType.name(),
				engineCapacity.toString(),
				productionYear.toString(),
				cost.toPlainString(),
				additionalCost.toPlainString()
			}
		};
		final TextTable tt = new TextTable(columnNames, data);
		tt.printTable();
	}

}
