package pl.uz.zgora.cartrading;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

class CarBuyerGui extends JFrame {

	private CarBuyerAgent myAgent;
	private CarBuyRequest.Builder builder = CarBuyRequest.Builder.aCarBuyRequest();
	private List<CarBuyRequest> carBuyRequests;

	private JButton confirmB;
	private JButton resetB;
	private JLabel offersL;
	private JLabel moneyL = new JLabel();

	CarBuyerGui(final CarBuyerAgent agent) {
		super(agent.getLocalName());
		myAgent = agent;

		this.carBuyRequests = myAgent.getCarBuyRequests();
		createGUI();
	}

	public void removeRequest() {
		carBuyRequests = new ArrayList<>();
		confirmB.setEnabled(true);
		offersL.setText(String.valueOf(Integer.parseInt(offersL.getText()) - 1));
		confirmB.setText("Add");
	}

	public void updateMoneyL() {
		moneyL.setText(myAgent.getMoney().toPlainString());
	}

	public void showGui() {
		pack();
		final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		final int rightX = (int) screenSize.getWidth();
		setLocation(rightX, 0 + getHeight() * (myAgent.getAgentNumber() - 1));
		super.setVisible(true);
	}

	private void createGUI() {
		final AtomicInteger counter = new AtomicInteger(1);

		final JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(10, 4));

		updateMoneyL();

		panel.add(new JLabel("Current money: "));
		panel.add(moneyL);
		panel.add(new JLabel());
		panel.add(new JLabel());

		final PlaceholderTextField brandsPTF = new PlaceholderTextField();
		brandsPTF.setPlaceholder("values");
		brandsPTF.setToolTipText(Arrays.stream(Brand.values()).map(Enum::name).collect(
			Collectors.joining(",")));

		panel.add(new JLabel(String.valueOf(counter.getAndIncrement())));
		panel.add(new JLabel("Brands"));
		panel.add(brandsPTF);
		panel.add(new JLabel());

		final PlaceholderTextField modelsPTF = new PlaceholderTextField();
		modelsPTF.setPlaceholder("values");

		panel.add(new JLabel(String.valueOf(counter.getAndIncrement())));
		panel.add(new JLabel("Models"));
		panel.add(modelsPTF);
		panel.add(new JLabel());

		final PlaceholderTextField engineTypesPTF = new PlaceholderTextField();
		engineTypesPTF.setPlaceholder("values");
		engineTypesPTF.setToolTipText(Arrays.stream(EngineType.values()).map(Enum::name).collect(
			Collectors.joining(",")));

		panel.add(new JLabel(String.valueOf(counter.getAndIncrement())));
		panel.add(new JLabel("Engine types"));
		panel.add(engineTypesPTF);
		panel.add(new JLabel());

		final PlaceholderTextField bodyTypesPTF = new PlaceholderTextField();
		bodyTypesPTF.setPlaceholder("values");
		bodyTypesPTF.setToolTipText(Arrays.stream(BodyType.values()).map(Enum::name).collect(
			Collectors.joining(",")));

		panel.add(new JLabel(String.valueOf(counter.getAndIncrement())));
		panel.add(new JLabel("Body types"));
		panel.add(bodyTypesPTF);
		panel.add(new JLabel());

		final PlaceholderTextField minEngineCapacityPTF = new PlaceholderTextField();
		minEngineCapacityPTF.setPlaceholder("min");

		final PlaceholderTextField maxEngineCapacityPTF = new PlaceholderTextField();
		maxEngineCapacityPTF.setPlaceholder("max");

		panel.add(new JLabel(String.valueOf(counter.getAndIncrement())));
		panel.add(new JLabel("EngineCapacity"));
		panel.add(minEngineCapacityPTF);
		panel.add(maxEngineCapacityPTF);

		final PlaceholderTextField minProductionYearPTF = new PlaceholderTextField();
		minProductionYearPTF.setPlaceholder("min");

		final PlaceholderTextField maxProductionYearPTF = new PlaceholderTextField();
		maxProductionYearPTF.setPlaceholder("max");

		panel.add(new JLabel(String.valueOf(counter.getAndIncrement())));
		panel.add(new JLabel("ProductionYear"));
		panel.add(minProductionYearPTF);
		panel.add(maxProductionYearPTF);

		final PlaceholderTextField minCostPTF = new PlaceholderTextField();
		minCostPTF.setPlaceholder("min");

		final PlaceholderTextField maxCostPTF = new PlaceholderTextField();
		maxCostPTF.setPlaceholder("max");

		panel.add(new JLabel(String.valueOf(counter.getAndIncrement())));
		panel.add(new JLabel("Cost"));
		panel.add(minCostPTF);
		panel.add(maxCostPTF);

		final PlaceholderTextField minAdditionalCostPTF = new PlaceholderTextField();
		minAdditionalCostPTF.setPlaceholder("min");

		final PlaceholderTextField maxAdditionalCostPTF = new PlaceholderTextField();
		maxAdditionalCostPTF.setPlaceholder("max");

		panel.add(new JLabel(String.valueOf(counter.getAndIncrement())));
		panel.add(new JLabel("AdditionalCost"));
		panel.add(minAdditionalCostPTF);
		panel.add(maxAdditionalCostPTF);

		final JPanel confirmButtonPanel = new JPanel();
		offersL = new JLabel(String.valueOf(carBuyRequests.size()));
		confirmB = new JButton(carBuyRequests.size() < 3 ? "Add" : "Start");
		confirmB.setEnabled(carBuyRequests.size() < 3);

		confirmButtonPanel.add(confirmB);
		confirmButtonPanel.add(offersL);
		confirmB.setBackground(Color.GREEN);
		confirmB.addActionListener(event -> {
			if (carBuyRequests.size() < 3) {
				try {
					final List<Brand> brands = brandsPTF.getText().isEmpty() ? null
						: Arrays.stream(brandsPTF.getText().split(","))
							.filter(s -> !s.isEmpty())
							.map(Brand::of).collect(Collectors.toList());

					final List<String> models = modelsPTF.getText().isEmpty() ? null
						: Arrays.stream(modelsPTF.getText().split(","))
							.filter(s -> !s.isEmpty()).collect(Collectors.toList());

					final List<EngineType> engineTypes =
						engineTypesPTF.getText().isEmpty() ? null : Arrays
							.stream(engineTypesPTF.getText().split(","))
							.filter(s -> !s.isEmpty())
							.map(EngineType::of).collect(Collectors.toList());

					final List<BodyType> bodyTypes = bodyTypesPTF.getText().isEmpty() ? null
						: Arrays.stream(bodyTypesPTF.getText().split(","))
							.filter(s -> !s.isEmpty())
							.map(BodyType::of).collect(Collectors.toList());

					final Double minEngineCapacity = minEngineCapacityPTF.getText().isEmpty() ? null
						: Double.parseDouble(minEngineCapacityPTF.getText());

					final Double maxEngineCapacity = maxEngineCapacityPTF.getText().isEmpty() ? null
						: Double.parseDouble(maxEngineCapacityPTF.getText());

					final Integer minProductionYear =
						minProductionYearPTF.getText().isEmpty() ? null
							: Integer.parseInt(minProductionYearPTF.getText());

					final Integer maxProductionYear =
						maxProductionYearPTF.getText().isEmpty() ? null
							: Integer.parseInt(maxProductionYearPTF.getText());

					final BigDecimal minCost =
						minCostPTF.getText().isEmpty() ? null
							: new BigDecimal(minCostPTF.getText());

					final BigDecimal maxCost =
						maxCostPTF.getText().isEmpty() ? null
							: new BigDecimal(maxCostPTF.getText());

					final BigDecimal minAdditionalCost =
						minAdditionalCostPTF.getText().isEmpty() ? null
							: new BigDecimal(minAdditionalCostPTF.getText());

					final BigDecimal maxAdditionalCost =
						maxAdditionalCostPTF.getText().isEmpty() ? null
							: new BigDecimal(maxAdditionalCostPTF.getText());

					final CarBuyRequest request = builder.withBrands(brands).withModels(models)
						.withEngineTypes(engineTypes).withBodyTypes(bodyTypes)
						.withMinEngineCapacity(minEngineCapacity)
						.withMaxEngineCapacity(maxEngineCapacity)
						.withMinProductionYear(minProductionYear)
						.withMaxProductionYear(maxProductionYear).withMinCost(minCost)
						.withMaxCost(maxCost)
						.withMinAdditionalCost(minAdditionalCost)
						.withMaxAdditionalCost(maxAdditionalCost)
						.build();

					carBuyRequests.add(request);
					offersL.setText(String.valueOf(carBuyRequests.size()));

					if (carBuyRequests.size() == 3) {
						confirmB.setText("Start");
					}

				} catch (final Exception ex) {
					JOptionPane.showMessageDialog(CarBuyerGui.this,
						"Nieprawidlowe wartosci. " + ex.getMessage(), "Blad",
						JOptionPane.ERROR_MESSAGE);
				}
			} else {
				myAgent.updateClientRequests(carBuyRequests);
				confirmB.setEnabled(false);
			}
		});

		resetB = new JButton("Reset");

		final JPanel resetButtonPanel = new JPanel();
		resetB.setBackground(Color.RED);
		resetB.addActionListener(e -> {
			carBuyRequests = new ArrayList<>();
			confirmB.setEnabled(true);
			offersL.setText("0");
			confirmB.setText("Add");
		});
		resetButtonPanel.add(resetB);
		resetB.setForeground(Color.WHITE);

		panel.add(resetButtonPanel);
		panel.add(new JLabel());
		panel.add(new JLabel());
		panel.add(confirmButtonPanel);

		final JScrollPane scrollPane = new JScrollPane(panel);
		getContentPane().add(scrollPane, BorderLayout.CENTER);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(final WindowEvent e) {
				myAgent.doDelete();
			}
		});

		setResizable(true);
	}
}
