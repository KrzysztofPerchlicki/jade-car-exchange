package pl.uz.zgora.cartrading;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import java.util.List;
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
		sd.setType("car-selling");
		sd.setName("JADE-car-trading");
		dfd.addServices(sd);
		try {
			DFService.register(this, dfd);
		} catch (final FIPAException fe) {
			fe.printStackTrace();
		}
//
//		addBehaviour(new OfferRequestsServer());
//
//		addBehaviour(new PurchaseOrdersServer());
	}

	@Override
	protected void takeDown() {
		try {
			DFService.deregister(this);
		} catch (final FIPAException fe) {
			fe.printStackTrace();
		}
		myGui.dispose();
		System.out.println("Seller-agent " + getAID().getName() + " terminating.");
	}

//	private class OfferRequestsServer extends CyclicBehaviour {
//
//		public void action() {
//			MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.CFP);
//			ACLMessage msg = myAgent.receive(mt);
//			if (msg != null) {
//				// CFP Message received. Process it
//				String title = msg.getContent();
//				ACLMessage reply = msg.createReply();
//
//				Integer price = (Integer) catalogue.get(title);
//				if (price != null) {
//					reply.setPerformative(ACLMessage.PROPOSE);
//					reply.setContent(String.valueOf(price.intValue()));
//				} else {
//					reply.setPerformative(ACLMessage.REFUSE);
//					reply.setContent("not-available");
//				}
//				myAgent.send(reply);
//			} else {
//				block();
//			}
//		}
//	}
//
//	private class PurchaseOrdersServer extends CyclicBehaviour {
//
//		public void action() {
//			MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL);
//			ACLMessage msg = myAgent.receive(mt);
//			if (msg != null) {
//				String title = msg.getContent();
//				ACLMessage reply = msg.createReply();
//
//				Integer price = (Integer) catalogue.remove(title);
//				if (price != null) {
//					reply.setPerformative(ACLMessage.INFORM);
//					System.out.println(title + " sold to agent " + msg.getSender().getName());
//				} else {
//					reply.setPerformative(ACLMessage.FAILURE);
//					reply.setContent("not-available");
//				}
//				myAgent.send(reply);
//			} else {
//				block();
//			}
//		}
//	}
}
