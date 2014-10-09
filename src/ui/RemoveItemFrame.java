package ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import db.DbException;
import db.ShopDatabaseHandler;
import db.TXTReaderAndWriter;
import domain.Shop;

public class RemoveItemFrame extends JFrame 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int FRAME_WIDTH = 300;
	private static final int FRAME_HEIGHT = 100;
	
	private Shop shop;
	private ShopDatabaseHandler handler;
	private String file = "Winkel.txt";
	private JLabel result;
	
	public RemoveItemFrame(String title, Shop shop)
	{
		super(title);
		this.setSize(FRAME_WIDTH, FRAME_HEIGHT);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setResizable(false);
		
		//Set window in the center of the screen
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
				
		this.shop = shop;
		try {
			handler = new TXTReaderAndWriter(file, this.shop);
		} catch (DbException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), 
					 "Error", JOptionPane.ERROR_MESSAGE); 
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), 
					 "Error", JOptionPane.ERROR_MESSAGE); 
		}
		
		JPanel flowPanel = new JPanel();
		JPanel inputPanel = new JPanel();
		JPanel submitPanel = new JPanel();
		JPanel resultPanel = new JPanel();
		
		flowPanel.setLayout(new BorderLayout());
		inputPanel.setLayout(new GridLayout(1,2));
		submitPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		resultPanel.setLayout(new BorderLayout());
		
		flowPanel.add(resultPanel, BorderLayout.SOUTH);
		flowPanel.add(inputPanel, BorderLayout.NORTH);
		flowPanel.add(submitPanel, BorderLayout.CENTER);
		
		
		JLabel id = new JLabel("Enter the id: ");
		inputPanel.add(id);
		
		JTextField itemID = new JTextField();
		inputPanel.add(itemID);
		
		JButton submitButton = new JButton("Submit");
		submitPanel.add(submitButton);
		
		result = new JLabel("The item has been succesfully removed.");
		result.setVisible(false);
		resultPanel.add(result);
		
		this.add(flowPanel);
		
		ActionListener listener = new removeItemListener(itemID);
		submitButton.addActionListener(listener);
	}
	
	class removeItemListener implements ActionListener
	{
		private JTextField itemID;
		
		public removeItemListener(JTextField itemID)
		{
			this.itemID = itemID;
		}
		
		public void actionPerformed(ActionEvent event)
		{
			removeItem(itemID.getText());
		}
	}
	
	private void removeItem(String id) 
	{
		try{
			this.shop.removeItem(id);
			result.setVisible(true);
			handler.setShop(this.shop);
			handler.writeItem();
		}catch(IllegalArgumentException e){
			JOptionPane.showMessageDialog(null, e.getMessage(), 
					 "Error", JOptionPane.ERROR_MESSAGE); 
		}catch(DbException e){
			JOptionPane.showMessageDialog(null, e.getMessage(), 
					 "Error", JOptionPane.ERROR_MESSAGE); 
		}
	}

	public Shop getShop()
	{
		return this.shop;
	}
	
}
