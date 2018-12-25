//package pl.uz.zgora.cartrading;
//
//import java.awt.BorderLayout;
//import java.awt.Dimension;
//import java.awt.GridLayout;
//import java.awt.Toolkit;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.awt.event.WindowAdapter;
//import java.awt.event.WindowEvent;
//import javax.swing.JButton;
//import javax.swing.JFrame;
//import javax.swing.JLabel;
//import javax.swing.JOptionPane;
//import javax.swing.JPanel;
//import javax.swing.JTextField;
//
//class BookBuyerGui extends JFrame {
//
//	private BookBuyerAgent myAgent;
//
//	private JTextField titleField;
//
//	BookBuyerGui(final BookBuyerAgent a) {
//		super(a.getLocalName());
//
//		myAgent = a;
//
//		JPanel p = new JPanel();
//		p.setLayout(new GridLayout(2, 2));
//		p.add(new JLabel("Tytul:"));
//		titleField = new JTextField(15);
//		p.add(titleField);
//		getContentPane().add(p, BorderLayout.CENTER);
//
//		final JButton addButton = new JButton("Szukaj");
//		addButton.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(final ActionEvent ev) {
//				try {
//					final String title = titleField.getText().trim();
//					myAgent.lookForTitle(title);
//					titleField.setText("");
//				} catch (final Exception e) {
//					JOptionPane.showMessageDialog(BookBuyerGui.this,
//						"Nieprawidlowe wartosci. " + e.getMessage(), "Blad",
//						JOptionPane.ERROR_MESSAGE);
//				}
//			}
//		});
//		p = new JPanel();
//		p.add(addButton);
//		getContentPane().add(p, BorderLayout.SOUTH);
//
//		addWindowListener(new WindowAdapter() {
//			@Override
//			public void windowClosing(final WindowEvent e) {
//				myAgent.doDelete();
//			}
//		});
//
//		setResizable(false);
//	}
//
//	public void display() {
//		pack();
//		final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//		final int centerX = (int) screenSize.getWidth() / 2;
//		final int centerY = (int) screenSize.getHeight() / 2;
//		setLocation(centerX - getWidth() / 2, centerY - getHeight() / 2);
//		setVisible(true);
//	}
//}