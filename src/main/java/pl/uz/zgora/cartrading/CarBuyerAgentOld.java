//package pl.uz.zgora.cartrading;
//
//import jade.core.AID;
//import jade.core.Agent;
//import jade.core.behaviours.Behaviour;
//import jade.core.behaviours.TickerBehaviour;
//import jade.domain.DFService;
//import jade.domain.FIPAAgentManagement.DFAgentDescription;
//import jade.domain.FIPAAgentManagement.ServiceDescription;
//import jade.domain.FIPAException;
//import jade.lang.acl.ACLMessage;
//import jade.lang.acl.MessageTemplate;
//
//public class CarBuyerAgentOld extends Agent {
//
//	private String targetBookTitle;
//	private AID[] sellerAgents;
//
//	@Override
//	protected void setup() {
//		System.out.println("Hallo! Buyer-agent " + getAID().getName() + " is ready.");
//		final Object[] args = getArguments();
//		if (args != null && args.length > 0) {
//			targetBookTitle = (String) args[0];
//			System.out.println("Target book is " + targetBookTitle);
//
//			addBehaviour(new TickerBehaviour(this, 60000) {
//				@Override
//				protected void onTick() {
//					System.out.println("Trying to buy " + targetBookTitle);
//					final DFAgentDescription template = new DFAgentDescription();
//					final ServiceDescription sd = new ServiceDescription();
//					sd.setType("car-selling");
//					template.addServices(sd);
//					try {
//						final DFAgentDescription[] result = DFService.search(myAgent, template);
//						System.out.println("Found the following seller agents:");
//						sellerAgents = new AID[result.length];
//						for (int i = 0; i < result.length; ++i) {
//							sellerAgents[i] = result[i].getName();
//							System.out.println(sellerAgents[i].getName());
//						}
//					} catch (final FIPAException fe) {
//						fe.printStackTrace();
//					}
//
//					// Perform the request
//					myAgent.addBehaviour(new RequestPerformer());
//				}
//			});
//		} else {
//			// Make the agent terminate
//			System.out.println("No target book title specified");
//			doDelete();
//		}
//	}
//
//	// Put agent clean-up operations here
//	@Override
//	protected void takeDown() {
//		// Printout a dismissal message
//		System.out.println("Buyer-agent " + getAID().getName() + " terminating.");
//	}
//
//	/**
//	 * Inner class RequestPerformer. This is the behaviour used by Book-buyer agents to request
//	 * seller agents the target book.
//	 */
//	private class RequestPerformer extends Behaviour {
//
//		private AID bestSeller; // The agent who provides the best offer
//		private int bestPrice;  // The best offered price
//		private int repliesCnt = 0; // The counter of replies from seller agents
//		private MessageTemplate mt; // The template to receive replies
//		private int step = 0;
//
//		@Override
//		public void action() {
//			switch (step) {
//				case 0:
//					// Send the cfp to all sellers
//					final ACLMessage cfp = new ACLMessage(ACLMessage.CFP);
//					for (int i = 0; i < sellerAgents.length; ++i) {
//						cfp.addReceiver(sellerAgents[i]);
//					}
//					cfp.setContent(targetBookTitle);
//					cfp.setConversationId("book-trade");
//					cfp.setReplyWith("cfp" + System.currentTimeMillis()); // Unique value
//					myAgent.send(cfp);
//					// Prepare the template to get proposals
//					mt = MessageTemplate.and(MessageTemplate.MatchConversationId("book-trade"),
//						MessageTemplate.MatchInReplyTo(cfp.getReplyWith()));
//					step = 1;
//					break;
//				case 1:
//					// Receive all proposals/refusals from seller agents
//					final ACLMessage reply = myAgent.receive(mt);
//					if (reply != null) {
//						// Reply received
//						if (reply.getPerformative() == ACLMessage.PROPOSE) {
//							// This is an offer
//							final int price = Integer.parseInt(reply.getContent());
//							if (bestSeller == null || price < bestPrice) {
//								// This is the best offer at present
//								bestPrice = price;
//								bestSeller = reply.getSender();
//							}
//						}
//						repliesCnt++;
//						if (repliesCnt >= sellerAgents.length) {
//							// We received all replies
//							step = 2;
//						}
//					} else {
//						block();
//					}
//					break;
//				case 2:
//					// Send the purchase order to the seller that provided the best offer
//					final ACLMessage order = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
//					order.addReceiver(bestSeller);
//					order.setContent(targetBookTitle);
//					order.setConversationId("book-trade");
//					order.setReplyWith("order" + System.currentTimeMillis());
//					myAgent.send(order);
//					// Prepare the template to get the purchase order reply
//					mt = MessageTemplate.and(MessageTemplate.MatchConversationId("book-trade"),
//						MessageTemplate.MatchInReplyTo(order.getReplyWith()));
//					step = 3;
//					break;
//				case 3:
//					// Receive the purchase order reply
//					reply = myAgent.receive(mt);
//					if (reply != null) {
//						// Purchase order reply received
//						if (reply.getPerformative() == ACLMessage.INFORM) {
//							// Purchase successful. We can terminate
//							System.out.println(
//								targetBookTitle + " successfully purchased from agent " + reply
//									.getSender().getName());
//							System.out.println("Price = " + bestPrice);
//							myAgent.doDelete();
//						} else {
//							System.out.println("Attempt failed: requested book already sold.");
//						}
//
//						step = 4;
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
//				System.out
//					.println("Attempt failed: " + targetBookTitle + " not available for sale");
//			}
//			return ((step == 2 && bestSeller == null) || step == 4);
//		}
//	}  // End of inner class RequestPerformer
//}
