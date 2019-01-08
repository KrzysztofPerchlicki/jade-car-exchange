package pl.uz.zgora.cartrading;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.Getter;

public class CarSellerAgent extends Agent {

	@Getter
	private List<Car> catalogue;
	private CarSellerGui myGui;

	@Override
	protected void setup() {
		catalogue = RandomCarsGenerator.generateCars();

		myGui = new CarSellerGui(this);
		myGui.showGui();

		final DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		final ServiceDescription sd = new ServiceDescription();
		sd.setType("car-seller");
		sd.setName("JADE-car-trading");
		dfd.addServices(sd);
		try {
			DFService.register(this, dfd);
		} catch (final FIPAException fe) {
			fe.printStackTrace();
		}

		addBehaviour(new OfferRequestsServer());

		addBehaviour(new PurchaseOrdersServer());
	}

	@Override
	protected void takeDown() {
		try {
			DFService.deregister(this);
		} catch (final FIPAException fe) {
			fe.printStackTrace();
		}
		myGui.dispose();
		PrintService.print(
			getAID().getLocalName() + ": Seller-agent " + getAID().getName() + " terminating.");
	}

	private class OfferRequestsServer extends CyclicBehaviour {

		@Override
		public void action() {
			final MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.CFP);
			final ACLMessage msg = myAgent.receive(mt);
			if (msg != null) {
				// CFP Message received. Process it
				try {
					final CarBuyRequest request = (CarBuyRequest) msg.getContentObject();

					final Optional<Car> carOffer = getBestOffer(request);
					final ACLMessage reply = msg.createReply();
					if (carOffer.isPresent()) {
						final Car car = carOffer.get();
						PrintService
							.print(getAID().getLocalName() + ": Znaleziono\n" + car.toString()
								+ "\nDla zapytania\n" + request.toString());
						reply.setPerformative(ACLMessage.PROPOSE);
						reply.setContentObject(car);
					} else {
						reply.setPerformative(ACLMessage.REFUSE);
						reply.setContent("not-available");
					}
					myAgent.send(reply);
				} catch (final UnreadableException | IOException e) {
					e.printStackTrace();
					block();
				}
			} else {
				block();
			}
		}

		private Optional<Car> getBestOffer(final CarBuyRequest request) {
			return catalogue.stream().filter(car -> {
				final List<Brand> brands =
					request.getBrands() == null ? new ArrayList<>() : request.getBrands();
				final List<String> models =
					request.getModels() == null ? new ArrayList<>() : request.getModels();
				final List<EngineType> engineTypes =
					request.getEngineTypes() == null ? new ArrayList<>()
						: request.getEngineTypes();
				final List<BodyType> bodyTypes =
					request.getBodyTypes() == null ? new ArrayList<>()
						: request.getBodyTypes();
				final Double minEngineCapacity = request.getMinEngineCapacity() == null ? 0
					: request.getMinEngineCapacity();
				final Double maxEngineCapacity =
					request.getMaxEngineCapacity() == null ? Double.MAX_VALUE
						: request.getMaxEngineCapacity();
				final Integer minProductionYear =
					request.getMinProductionYear() == null ? 1800
						: request.getMinProductionYear();
				final Integer maxProductionYear =
					request.getMaxProductionYear() == null ? 2020
						: request.getMaxProductionYear();
				final BigDecimal minCost =
					request.getMinCost() == null ? BigDecimal.ZERO : request.getMinCost();
				final BigDecimal maxCost =
					request.getMaxCost() == null ? BigDecimal.valueOf(100000)
						: request.getMaxCost();
				final BigDecimal minAdditionalCost =
					request.getMinAdditionalCost() == null ? BigDecimal.ZERO
						: request.getMinAdditionalCost();
				final BigDecimal maxAdditionalCost =
					request.getMaxAdditionalCost() == null ? BigDecimal.valueOf(100000)
						: request.getMaxAdditionalCost();

				if (!brands.isEmpty() && !brands.contains(car.getBrand())) {
					return false;
				}

				if (!models.isEmpty() && models.stream()
					.noneMatch(s -> s.equalsIgnoreCase(car.getModel()))) {
					return false;
				}

				if (!engineTypes.isEmpty() && !engineTypes.contains(car.getEngineType())) {
					return false;
				}

				if (!bodyTypes.isEmpty() && !bodyTypes.contains(car.getBodyType())) {
					return false;
				}

				if (car.getEngineCapacity() < minEngineCapacity
					|| car.getEngineCapacity() > maxEngineCapacity) {
					return false;
				}

				if (car.getProductionYear() < minProductionYear
					|| car.getProductionYear() > maxProductionYear) {
					return false;
				}

				if (car.getCost().compareTo(minCost) < 0
					|| car.getCost().compareTo(maxCost) > 0) {
					return false;
				}

				if (car.getAdditionalCost().compareTo(minAdditionalCost) < 0
					|| car.getAdditionalCost().compareTo(maxAdditionalCost) > 0) {
					return false;
				}

				return true;

			}).min((car1, car2) -> {
				final BigDecimal cost1 = car1.getCost().add(car1.getAdditionalCost());
				final BigDecimal cost2 = car2.getCost().add(car2.getAdditionalCost());
				return cost1.compareTo(cost2);
			});
		}
	}

	private class PurchaseOrdersServer extends CyclicBehaviour {

		@Override
		public void action() {
			final MessageTemplate mt = MessageTemplate
				.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL);
			final ACLMessage msg = myAgent.receive(mt);
			if (msg != null) {
				try {
					final Car car = (Car) msg.getContentObject();
					final ACLMessage reply = msg.createReply();

					if (catalogue.remove(car)) {
						myGui.redrawPanel();
						reply.setPerformative(ACLMessage.INFORM);
						PrintService.print(
							getAID().getLocalName() + ": Auto ponizej sprzedane dla kupującego "
								+ msg.getSender().getLocalName() + "\n" + car.toString());
					} else {
						reply.setPerformative(ACLMessage.FAILURE);
						reply.setContent("not-available");
					}
					myAgent.send(reply);
				} catch (final UnreadableException e) {
					e.printStackTrace();
				}

			} else {
				block();
			}
		}
	}
}
