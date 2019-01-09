package pl.uz.zgora.cartrading;

import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Reservation {

	private String buyerName;
	private Car car;
	private ZonedDateTime endTime;


}
