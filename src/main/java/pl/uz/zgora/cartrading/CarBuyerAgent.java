package pl.uz.zgora.cartrading;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CarBuyerAgent extends Agent {


	private CarBuyerGui myGui;
	private List<CarBuyRequest> carBuyRequests = new ArrayList<>();
	private int agentNumber;
	//lista znanych sprzedawcow
	private AID[] sellerAgents;
	private BigDecimal money = BigDecimal.valueOf(100000);

	@Override
	protected void setup() {
		System.out.println(
			getAID().getLocalName() + ": Czekam na dyspozycje kupna...\n");
		final Object[] args = getArguments();
		if (args.length > 0) {
			this.agentNumber = (int) args[0];
			this.carBuyRequests = (List<CarBuyRequest>) args[1];

		}
		myGui = new CarBuyerGui(this);
		myGui.showGui();
		//interwal czasowy dla kupujacego pomiedzy wysylaniem kolejnych cfp
		//przekazywany jako argument linii polecen
		final int interval = 5000;
		addBehaviour(new TickerBehaviour(this, interval) {
			@Override
			protected void onTick() {
				if (carBuyRequests != null && !carBuyRequests.isEmpty()) {
					PrintService.print(getAID().getLocalName() + ": Szukam ofert od sprzedawcow");
					//aktualizuj liste znanych sprzedawcow
					final DFAgentDescription template = new DFAgentDescription();
					final ServiceDescription sd = new ServiceDescription();
					sd.setType("car-seller");
					template.addServices(sd);
					try {
						final DFAgentDescription[] result = DFService.search(myAgent, template);
						PrintService.print(getAID().getLocalName() + ": Znaleziono sprzedajacych:");
						sellerAgents = new AID[result.length];
						for (int i = 0; i < result.length; ++i) {
							sellerAgents[i] = result[i].getName();
							PrintService.print(sellerAgents[i].getLocalName());
						}
					} catch (final FIPAException fe) {
						fe.printStackTrace();
					}
					PrintService.print(getAID().getLocalName() + ": Zaczynamy...\n\n");

					carBuyRequests.forEach(carBuyRequest -> {
						myAgent.addBehaviour(new CarBuyerAgent.RequestPerformer(carBuyRequest));
					});
					carBuyRequests = new ArrayList<>();
					myGui.removeAllRequests();
				}
			}
		});
	}

	//metoda wywolywana przez gui, gdy skladana jest dyspozycja kupna auta
	public void updateClientRequests(final List<CarBuyRequest> requests) {
		addBehaviour(new OneShotBehaviour() {
			@Override
			public void action() {
				carBuyRequests = requests;
				final StringBuilder stringBuilder = new StringBuilder();
				stringBuilder.append(getAID().getLocalName()).append(": Kryteria wyszukiwan:\n");
				carBuyRequests.forEach(carBuyRequest -> stringBuilder.append(carBuyRequests));
				PrintService.print(stringBuilder.toString());

			}
		});
	}

	@Override
	protected void takeDown() {
		myGui.dispose();
		PrintService.print(getAID().getLocalName() + ": Zakończyłem żywot");
	}

	private class RequestPerformer extends Behaviour {

		private AID bestSeller;
		private BigDecimal bestPrice;
		private int repliesCount = 0;
		private MessageTemplate mt;
		private BuyerSteps step = BuyerSteps.SEARCH;
		private CarBuyRequest carBuyRequest;

		public RequestPerformer(final CarBuyRequest carBuyRequest) {
			this.carBuyRequest = carBuyRequest;
		}

		@Override
		public void action() {
			switch (step) {
				case SEARCH:
					//call for proposal (cfp) do znalezionych sprzedajacych
					final ACLMessage cfp = new ACLMessage(ACLMessage.CFP);
					for (int i = 0; i < sellerAgents.length; ++i) {
						cfp.addReceiver(sellerAgents[i]);
					}
					try {
						cfp.setContentObject(carBuyRequest);
						cfp.setConversationId("car-trade");
						cfp.setReplyWith("cfp" + System.currentTimeMillis()); //unikalna wartosc
						myAgent.send(cfp);
						mt = MessageTemplate.and(MessageTemplate.MatchConversationId("car-trade"),
							MessageTemplate.MatchInReplyTo(cfp.getReplyWith()));
						step = BuyerSteps.RECEIVE_OFFERS;
					} catch (final IOException ex) {
						ex.printStackTrace();
						step = BuyerSteps.END_ERROR;
					}
					break;
				case RECEIVE_OFFERS:
					//odbior ofert od sprzedajacych
					final ACLMessage searchReply = myAgent.receive(mt);
					if (searchReply != null) {
						if (searchReply.getPerformative() == ACLMessage.PROPOSE) {
							//otrzymano oferte
							final BigDecimal price = BigDecimal
								.valueOf(Double.parseDouble(searchReply.getContent()));
							if (bestSeller == null || price.compareTo(bestPrice) < 0) {
								//jak na razie to najlepsza oferta
								bestPrice = price;
								bestSeller = searchReply.getSender();
							}
						}
						repliesCount++;
						if (repliesCount >= sellerAgents.length) {
							//otrzymano wszystkie oferty -> nastepny krok
							step = BuyerSteps.TRY_BUY;
						}
					} else {
						block();
					}
					break;
				case TRY_BUY:
					//zakup najlepszej oferty
					final ACLMessage order = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
					order.addReceiver(bestSeller);
					try {
						order.setContentObject(carBuyRequest);
						order.setConversationId("book-trade");
						order.setReplyWith("order" + System.currentTimeMillis());
						myAgent.send(order);
						mt = MessageTemplate.and(MessageTemplate.MatchConversationId("book-trade"),
							MessageTemplate.MatchInReplyTo(order.getReplyWith()));
						step = BuyerSteps.CONFIRM_BUY;
					} catch (final IOException ex) {
						ex.printStackTrace();
						step = BuyerSteps.END_ERROR;
					}
					break;
				case CONFIRM_BUY:
					//potwierdzenie zakupu przez agenta sprzedajacego
					final ACLMessage confirmBuyReply = myAgent.receive(mt);
					if (confirmBuyReply != null) {
						if (confirmBuyReply.getPerformative() == ACLMessage.INFORM) {
							//zakup zakonczony powodzeniem
							PrintService.print(getAID().getLocalName()
								+ ": Kupiono auto o ponizszych parametrach za "
								+ bestPrice + " od " + confirmBuyReply
								.getSender().getLocalName() + "\n" + carBuyRequest.toString());
						} else {
							PrintService.print(getAID().getLocalName()
								+ ": Zakup nieudany. Auto o poniższych parametrach kupiono w międzyczasie\n"
								+ carBuyRequest.toString());
						}
						step = BuyerSteps.END_SUCCESSFUL;    //konczy cala interakcje, ktorej celem jest kupno
					} else {
						block();
					}
					break;
				default:
					block();
			}
		}

		@Override
		public boolean done() {
			if (step == BuyerSteps.RECEIVE_OFFERS && bestSeller == null) {
				PrintService.print(
					getAID().getLocalName() + ": Nie ma w sprzedazy dla parametrow\n"
						+ carBuyRequest
						.toString());
			}
			return ((step == BuyerSteps.RECEIVE_OFFERS && bestSeller == null)
				|| step == BuyerSteps.END_SUCCESSFUL || step == BuyerSteps.END_ERROR);
		}
	}

	public List<CarBuyRequest> getCarBuyRequests() {
		return carBuyRequests;
	}

	public int getAgentNumber() {
		return agentNumber;
	}

	public BigDecimal getMoney() {
		return money;
	}
}