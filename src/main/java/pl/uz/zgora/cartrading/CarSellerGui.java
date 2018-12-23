package pl.uz.zgora.cartrading;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

class CarSellerGui extends JFrame {

	private CarSellerAgent myAgent;

	CarSellerGui(final CarSellerAgent a) {
		super(a.getLocalName());

		myAgent = a;

		final JPanel p = new JPanel();
		p.setLayout(new GridLayout(9, 8));
		p.add(new JLabel("Brand"));
		p.add(new JLabel("Model"));
		p.add(new JLabel("Body type"));
		p.add(new JLabel("Engine type"));
		p.add(new JLabel("Engine capacity"));
		p.add(new JLabel("Production year"));
		p.add(new JLabel("Cost"));
		p.add(new JLabel("Additional cost"));

		myAgent.getCatalogue().forEach(car -> {
			p.add(new JLabel(car.getBrand()));
			p.add(new JLabel(car.getModel()));
			p.add(new JLabel(car.getBodyType()));
			p.add(new JLabel(car.getEngineType()));
			p.add(new JLabel(car.getEngineCapacity()));
			p.add(new JLabel(car.getProductionYear()));
			p.add(new JLabel(car.getCost()));
			p.add(new JLabel(car.getAdditionalCost()));
		});

		getContentPane().add(p, BorderLayout.CENTER);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(final WindowEvent e) {
				myAgent.doDelete();
			}
		});

		setResizable(false);
	}

	public void showGui() {
		pack();
		final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		final int centerX = (int) screenSize.getWidth() / 2;
		final int centerY = (int) screenSize.getHeight() / 2;
		setLocation(centerX - getWidth() / 2, centerY - getHeight() / 2);
		super.setVisible(true);
	}
}
