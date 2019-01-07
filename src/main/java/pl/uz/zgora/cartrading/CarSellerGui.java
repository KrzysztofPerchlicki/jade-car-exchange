package pl.uz.zgora.cartrading;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
		final AtomicInteger counter = new AtomicInteger(1);

		final JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(9, 9));
		panel.add(new JLabel("Lp"));
		panel.add(new JLabel("Brand"));
		panel.add(new JLabel("Model"));
		panel.add(new JLabel("Body type"));
		panel.add(new JLabel("Engine type"));
		panel.add(new JLabel("Engine capacity"));
		panel.add(new JLabel("Production year"));
		panel.add(new JLabel("Cost"));
		panel.add(new JLabel("Additional cost"));

		myAgent.getCatalogue().forEach(car -> {
			panel.add(new JLabel(String.valueOf(counter.getAndIncrement())));
			panel.add(new JLabel(car.getBrand().getName()));
			panel.add(new JLabel(car.getModel()));
			panel.add(new JLabel(car.getBodyType().name()));
			panel.add(new JLabel(car.getEngineType().name()));
			panel.add(new JLabel(String.valueOf(car.getEngineCapacity())));
			panel.add(new JLabel(String.valueOf(car.getProductionYear())));
			panel.add(new JLabel(car.getCost().toPlainString()));
			panel.add(new JLabel(car.getAdditionalCost().toPlainString()));
		});

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

	public void showGui() {
		pack();
		final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(new Dimension(getWidth(), 2 * getHeight() / 3));
		setLocation(0, 0 + getHeight() * (((int) myAgent.getArguments()[0]) - 1));

		super.setVisible(true);
	}
}
