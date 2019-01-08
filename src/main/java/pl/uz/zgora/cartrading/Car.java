package pl.uz.zgora.cartrading;

import dnl.utils.text.table.TextTable;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class Car implements Serializable {

	private Brand brand;
	private String model;
	private BodyType bodyType;
	private EngineType engineType;
	private Double engineCapacity;
	private Integer productionYear;
	private BigDecimal cost;
	private BigDecimal additionalCost;

	@Override
	public String toString() {
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
		final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		final TextTable tt = new TextTable(columnNames, data);
		tt.printTable(new PrintStream(outputStream), 0);
		try {
			return outputStream.toString("UTF-8");
		} catch (final UnsupportedEncodingException e) {
			e.printStackTrace();
			return "";
		}
	}

}
