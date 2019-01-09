package pl.uz.zgora.cartrading;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

class CarSellerGui extends JFrame {

	private CarSellerAgent myAgent;

	CarSellerGui(final CarSellerAgent agent) {
		super(agent.getLocalName());

		myAgent = agent;

		redrawPanel();
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(final WindowEvent e) {
				myAgent.doDelete();
			}
		});

		setResizable(true);
	}

	public void showGui() {
		super.setVisible(true);
	}

	public void redrawPanel() {
		final AtomicInteger counter = new AtomicInteger(1);
		final List<Car> catalogue = myAgent.getCatalogue();

		final JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(catalogue.size() + 1, 9));
		panel.add(new JLabel("Lp"));
		panel.add(new JLabel("Brand"));
		panel.add(new JLabel("Model"));
		panel.add(new JLabel("Body type"));
		panel.add(new JLabel("Engine type"));
		panel.add(new JLabel("Engine capacity"));
		panel.add(new JLabel("Production year"));
		panel.add(new JLabel("Cost"));
		panel.add(new JLabel("Additional cost"));

		catalogue.forEach(car -> {
			final Optional<Reservation> reservation = myAgent.getReservationManager().get(car);
			final Color background = reservation.isPresent() ? Color.ORANGE : panel.getBackground();
			final String tooltipText = reservation
				.map(reservation1 -> "Reserved by " + reservation1.getBuyerName())
				.orElse(null);

			panel.add(
				createLabel(String.valueOf(counter.getAndIncrement()), background, tooltipText));

			panel.add(
				createLabel(car.getBrand().getName(), background, tooltipText));

			panel.add(createLabel(car.getModel(), background, tooltipText));

			panel.add(createLabel(car.getBodyType().name(), background, tooltipText));

			panel.add(
				createLabel(car.getEngineType().name(), background, tooltipText));

			panel.add(
				createLabel(String.valueOf(car.getEngineCapacity()), background, tooltipText));

			panel.add(
				createLabel(String.valueOf(car.getProductionYear()), background, tooltipText));

			panel.add(
				createLabel(car.getCost().toPlainString(), background, tooltipText));

			panel.add(
				createLabel(car.getAdditionalCost().toPlainString(), background, tooltipText));
		});

		final JScrollPane scrollPane = new JScrollPane(panel);
		getContentPane().removeAll();
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		pack();
		setSize(new Dimension(getWidth(), 2 * getHeight() / 3));
		setLocation(0, getHeight() * (((int) myAgent.getArguments()[0]) - 1));
		repaint();
	}

	private JLabel createLabel(final String text, final Color background,
		final String tooltipText) {
		final JLabel label = new JLabel(text);
		label.setBackground(background);
		label.setOpaque(true);
		label.setToolTipText(tooltipText);
		return label;
	}


}
