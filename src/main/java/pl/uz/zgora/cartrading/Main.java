package pl.uz.zgora.cartrading;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.StaleProxyException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {

	public static void main(final String[] args) throws StaleProxyException {
		// Get a hold on JADE runtime
		final Runtime runtime = Runtime.instance();

		// Exit the JVM when there are no more containers around
		runtime.setCloseVM(true);
		System.out.print("runtime created\n");

		// Create a default profile
		final Profile profile = new ProfileImpl(null, 1200, null);
		System.out.print("profile created\n");

		System.out.println("Launching a whole in-process platform..." + profile);
		final AgentContainer mainContainer = runtime.createMainContainer(profile);

		System.out.println("Launching the rma agent on the main container ...");
		mainContainer.createNewAgent("rma", "jade.tools.rma.rma", new Object[0]).start();
		createSellerAgents(mainContainer, 1);
		final List<CarBuyRequest> buyer1Requests = new ArrayList<>();
		buyer1Requests.add(
			CarBuyRequest.Builder.aCarBuyRequest().withBrands(Collections.singletonList(Brand.FIAT))
				.build());
		buyer1Requests.add(
			CarBuyRequest.Builder.aCarBuyRequest()
				.withBrands(Collections.singletonList(Brand.VOLKSWAGEN))
				.build());
		buyer1Requests.add(
			CarBuyRequest.Builder.aCarBuyRequest().withBrands(Collections.singletonList(Brand.AUDI))
				.build());
		createBuyerAgent(mainContainer, buyer1Requests, 1);
//		final List<CarBuyRequest> buyer2Requests = new ArrayList<>();
//		buyer2Requests.add(
//			CarBuyRequest.Builder.aCarBuyRequest()
//				.withEngineTypes(Collections.singletonList(EngineType.GASOLINE))
//				.build());
//		buyer2Requests.add(
//			CarBuyRequest.Builder.aCarBuyRequest()
//				.withEngineTypes(Collections.singletonList(EngineType.GAS))
//				.build());
//		buyer2Requests.add(
//			CarBuyRequest.Builder.aCarBuyRequest()
//				.withEngineTypes(Collections.singletonList(EngineType.DIESEL))
//				.build());
//		createBuyerAgent(mainContainer, buyer2Requests, 2);
//		final List<CarBuyRequest> buyer3Requests = new ArrayList<>();
//		buyer3Requests.add(
//			CarBuyRequest.Builder.aCarBuyRequest()
//				.withBodyTypes(Collections.singletonList(BodyType.HATCHBACK))
//				.build());
//		buyer3Requests.add(
//			CarBuyRequest.Builder.aCarBuyRequest()
//				.withBodyTypes(Collections.singletonList(BodyType.KOMBI))
//				.build());
//		buyer3Requests.add(
//			CarBuyRequest.Builder.aCarBuyRequest()
//				.withBodyTypes(Collections.singletonList(BodyType.SEDAN))
//				.build());
//		createBuyerAgent(mainContainer, buyer3Requests, 3);
	}

	private static void createSellerAgents(final AgentContainer container, final int count)
		throws StaleProxyException {
		for (int i = 1; i <= count; i++) {
			final Object[] arguments = {i};
			container
				.createNewAgent("S" + i, "pl.uz.zgora.cartrading.CarSellerAgent", arguments)
				.start();
		}
	}

	private static void createBuyerAgent(final AgentContainer container,
		final List<CarBuyRequest> carBuyRequests, final int agentNumber)
		throws StaleProxyException {
		final Object[] arguments = {agentNumber, carBuyRequests};
		container
			.createNewAgent("B" + agentNumber, "pl.uz.zgora.cartrading.CarBuyerAgent", arguments)
			.start();
	}
}
