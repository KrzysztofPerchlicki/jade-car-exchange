package pl.uz.zgora.cartrading;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class CarBuyRequest implements Serializable {

	private List<Brand> brands;
	private List<String> models;
	private List<EngineType> engineTypes;
	private List<BodyType> bodyTypes;
	private Double minEngineCapacity;
	private Double maxEngineCapacity;
	private Integer minProductionYear;
	private Integer maxProductionYear;
	private BigDecimal minCost;
	private BigDecimal maxCost;
	private BigDecimal minAdditionalCost;
	private BigDecimal maxAdditionalCost;


	public static final class Builder {

		private List<Brand> brands;
		private List<String> models;
		private List<EngineType> engineTypes;
		private List<BodyType> bodyTypes;
		private Double minEngineCapacity;
		private Double maxEngineCapacity;
		private Integer minProductionYear;
		private Integer maxProductionYear;
		private BigDecimal minCost;
		private BigDecimal maxCost;
		private BigDecimal minAdditionalCost;
		private BigDecimal maxAdditionalCost;

		private Builder() {
		}

		public static Builder aCarBuyRequest() {
			return new Builder();
		}

		public Builder withBrands(final List<Brand> brands) {
			this.brands = brands;
			return this;
		}

		public Builder withModels(final List<String> models) {
			this.models = models;
			return this;
		}

		public Builder withEngineTypes(final List<EngineType> engineTypes) {
			this.engineTypes = engineTypes;
			return this;
		}

		public Builder withBodyTypes(final List<BodyType> bodyTypes) {
			this.bodyTypes = bodyTypes;
			return this;
		}

		public Builder withMinEngineCapacity(final Double minEngineCapacity) {
			this.minEngineCapacity = minEngineCapacity;
			return this;
		}

		public Builder withMaxEngineCapacity(final Double maxEngineCapacity) {
			this.maxEngineCapacity = maxEngineCapacity;
			return this;
		}

		public Builder withMinProductionYear(final Integer minProductionYear) {
			this.minProductionYear = minProductionYear;
			return this;
		}

		public Builder withMaxProductionYear(final Integer maxProductionYear) {
			this.maxProductionYear = maxProductionYear;
			return this;
		}

		public Builder withMinCost(final BigDecimal minCost) {
			this.minCost = minCost;
			return this;
		}

		public Builder withMaxCost(final BigDecimal maxCost) {
			this.maxCost = maxCost;
			return this;
		}

		public Builder withMinAdditionalCost(final BigDecimal minAdditionalCost) {
			this.minAdditionalCost = minAdditionalCost;
			return this;
		}

		public Builder withMaxAdditionalCost(final BigDecimal maxAdditionalCost) {
			this.maxAdditionalCost = maxAdditionalCost;
			return this;
		}

		public CarBuyRequest build() {
			final CarBuyRequest carBuyRequest = new CarBuyRequest();
			carBuyRequest.bodyTypes = this.bodyTypes;
			carBuyRequest.minAdditionalCost = this.minAdditionalCost;
			carBuyRequest.minEngineCapacity = this.minEngineCapacity;
			carBuyRequest.maxProductionYear = this.maxProductionYear;
			carBuyRequest.minCost = this.minCost;
			carBuyRequest.models = this.models;
			carBuyRequest.maxAdditionalCost = this.maxAdditionalCost;
			carBuyRequest.maxEngineCapacity = this.maxEngineCapacity;
			carBuyRequest.maxCost = this.maxCost;
			carBuyRequest.minProductionYear = this.minProductionYear;
			carBuyRequest.engineTypes = this.engineTypes;
			carBuyRequest.brands = this.brands;
			return carBuyRequest;
		}
	}
}
