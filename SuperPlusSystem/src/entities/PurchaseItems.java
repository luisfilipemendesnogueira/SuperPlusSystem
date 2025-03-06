package entities;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

import connection.MySqlConnection;

public class PurchaseItems {
	private int purchase_id;
	private int product_id;
	private int quantity;
	private double unit_price;
	
	public int getPurchase_id() {
		return purchase_id;
	}
	public void setPurchase_id(int purchase_id) {
		this.purchase_id = purchase_id;
	}
	public int getProduct_id() {
		return product_id;
	}
	public void setProduct_id(int product_id) {
		this.product_id = product_id;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public double getUnit_price() {
		return unit_price;
	}
	public void setUnit_price(double unit_price) {
		this.unit_price = unit_price;
	}
	
	Scanner sc = new Scanner(System.in);
	
	public double purchasedItems(PurchaseItems pi) {
		double total = 0.0;
		while (true) {
			boolean validQuantity = false;
			while(!validQuantity) {
				System.out.print("\nDigite as informações do produto comprado:");
				System.out.print("\nId do Produto: ");
				pi.setProduct_id(sc.nextInt());
				System.out.print("Quantidade comprada do produto " + pi.getProduct_id() + ": ");
				pi.setQuantity(sc.nextInt());
				Products p = new Products();
				pi.setUnit_price(p.getPrice(pi.getProduct_id()));
				if(p.verifyAvailability(pi.getProduct_id(), pi.getQuantity())) {
					p.updateStock(pi.getProduct_id(), pi.getQuantity());
					validQuantity = true;
				} 
			}
			
			String sql = "insert into purchase_items(purchase_id, product_id, quantity, unit_price) values (?,?,?,?)";
			
			PreparedStatement ps = null;
			
			try {
				ps = MySqlConnection.getConnection().prepareStatement(sql);
				ps.setInt(1, pi.getPurchase_id());
				ps.setInt(2, pi.getProduct_id());
				ps.setInt(3, pi.getQuantity());
				ps.setDouble(4, pi.getUnit_price());
				System.out.println("\nCompra de produto registrada com sucesso!\n");
				
				ps.execute();
				ps.close();
				
			} catch (SQLException e) {
				System.out.println("\nErro: Compra de produto não foi registrada!\n");
				e.printStackTrace();
			}
			
			total += pi.getUnit_price() * pi.getQuantity();
			
			System.out.print("Deseja adicionar mais um produto? (s/n): ");
	        sc.nextLine(); 
	        String choice = "";
	        while (!choice.equalsIgnoreCase("s") && !choice.equalsIgnoreCase("n")) {
	            choice = sc.nextLine();
	            if (!choice.equalsIgnoreCase("s") && !choice.equalsIgnoreCase("n")) {
	                System.out.println("Por favor, digite 's' para sim ou 'n' para não.");
	                System.out.print("\nDeseja adicionar mais um produto? (s/n): ");
	            }
	        }
	        if (choice.equalsIgnoreCase("n")) {
	            break;
	        }
		}
		return total;
	}
}
