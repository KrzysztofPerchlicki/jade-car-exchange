package pl.uz.zgora.cartrading;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;

public class CarBuyerAgent extends Agent {


	private CarBuyerGui myGui;
	private CarBuyRequest carBuyRequest;
	//lista znanych sprzedawcow
	private AID[] sellerAgents;

	@Override
	protected void setup() {
		System.out.println(
			getAID().getLocalName() + ": Czekam na dyspozycje kupna...");
		myGui = new CarBuyerGui(this);
		myGui.showGui();
		//interwal czasowy dla kupujacego pomiedzy wysylaniem kolejnych cfp
		//przekazywany jako argument linii polecen
		int interval = 20000;
		final Object[] args = getArguments();
		if (args != null && args.length > 0) {
			interval = Integer.parseInt(args[0].toString());
		}
		addBehaviour(new TickerBehaviour(this, interval) {
			@Override
			protected void onTick() {
				//szukaj tylko jesli zlecony zostal tytul pozycji
				if (carBuyRequest != null) {
					System.out.println(getAID().getLocalName() + ": Szukam ofert od sprzedawcow");
					//aktualizuj liste znanych sprzedawcow
					final DFAgentDescription template = new DFAgentDescription();
					final ServiceDescription sd = new ServiceDescription();
					sd.setType("book-selling");
					template.addServices(sd);
					try {
						final DFAgentDescription[] result = DFService.search(myAgent, template);
						System.out.println(getAID().getLocalName() + ": Znaleziono sprzedajacych:");
						sellerAgents = new AID[result.length];
						for (int i = 0; i < result.length; ++i) {
							sellerAgents[i] = result[i].getName();
							System.out.println(sellerAgents[i].getLocalName());
						}
					} catch (final FIPAException fe) {
						fe.printStackTrace();
					}
					System.out.println(getAID().getLocalName() + ": Zaczynamy...");

//					myAgent.addBehaviour(new CarBuyerAgent.RequestPerformer());
				}
			}
		});
	}

	//metoda wywolywana przez gui, gdy skladana jest dyspozycja kupna auta
	public void updateClientRequest(final CarBuyRequest request) {
		addBehaviour(new OneShotBehaviour() {
			@Override
			public void action() {
				carBuyRequest = request;
				System.out.println(
					getAID().getLocalName() + ": Kryteria wyszukiwan:");
				carBuyRequest.print();
			}
		});
	}

	@Override
	protected void takeDown() {
		myGui.dispose();
		System.out.println(getAID().getLocalName() + ": Zakończyłem żywot");
	}

//	private class RequestPerformer extends Behaviour {
//
//		private AID bestSeller;
//		private int bestPrice;
//		private int repliesCnt = 0;
//		private MessageTemplate mt;
//		private int step = 0;
//
//		@Override
//		public void action() {
//			switch (step) {
//				case 0:
//					//call for proposal (cfp) do znalezionych sprzedajacych
//					final ACLMessage cfp = new ACLMessage(ACLMessage.CFP);
//					for (int i = 0; i < sellerAgents.length; ++i) {
//						cfp.addReceiver(sellerAgents[i]);
//					}
//					cfp.setContent(targetBookTitle);
//					cfp.setConversationId("book-trade");
//					cfp.setReplyWith("cfp" + System.currentTimeMillis()); //unikalna wartosc
//					myAgent.send(cfp);
//					mt = MessageTemplate.and(MessageTemplate.MatchConversationId("book-trade"),
//						MessageTemplate.MatchInReplyTo(cfp.getReplyWith()));
//					step = 1;
//					break;
//				case 1:
//					//odbior ofert od sprzedajacych
//					final ACLMessage reply = myAgent.receive(mt);
//					if (reply != null) {
//						if (reply.getPerformative() == ACLMessage.PROPOSE) {
//							//otrzymano oferte
//							final int price = Integer.parseInt(reply.getContent());
//							if (bestSeller == null || price < bestPrice) {
//								//jak na razie to najlepsza oferta
//								bestPrice = price;
//								bestSeller = reply.getSender();
//							}
//						}
//						repliesCnt++;
//						if (repliesCnt >= sellerAgents.length) {
//							//otrzymano wszystkie oferty -> nastepny krok
//							step = 2;
//						}
//					} else {
//						block();
//					}
//					break;
//				case 2:
//					//zakup najlepszej oferty
//					final ACLMessage order = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
//					order.addReceiver(bestSeller);
//					order.setContent(targetBookTitle);
//					order.setConversationId("book-trade");
//					order.setReplyWith("order" + System.currentTimeMillis());
//					myAgent.send(order);
//					mt = MessageTemplate.and(MessageTemplate.MatchConversationId("book-trade"),
//						MessageTemplate.MatchInReplyTo(order.getReplyWith()));
//					step = 3;
//					break;
//				case 3:
//					//potwierdzenie zakupu przez agenta sprzedajacego
//					reply = myAgent.receive(mt);
//					if (reply != null) {
//						if (reply.getPerformative() == ACLMessage.INFORM) {
//							//zakup zakonczony powodzeniem
//							System.out.println(
//								targetBookTitle + " kupiona za " + bestPrice + " od " + reply
//									.getSender().getLocalName());
//							System.out.println("Czekam na nowa dyspozycje kupna.");
//							targetBookTitle = "";
//							//myAgent.doDelete();
//						} else {
//							System.out.println("Zakup nieudany. " + targetBookTitle
//								+ " zostala sprzedana w miedzyczasie.");
//						}
//						step = 4;    //konczy cala interakcje, ktorej celem jest kupno
//					} else {
//						block();
//					}
//					break;
//			}
//		}
//
//		@Override
//		public boolean done() {
//			if (step == 2 && bestSeller == null) {
//				System.out.println(targetBookTitle + " nie ma w sprzedazy");
//			}
//			//koniec jesli ksiazki nie ma w sprzedazy lub nie udalo sie kupic
//			return ((step == 2 && bestSeller == null) || step == 4);
//		}
//	}
}