package entities;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import connection.MySqlConnection;

public class Purchases {
	private int customer_id;
	private String purchase_date;
	private double total_amount;
	
	public int getCustomer_id() {
		return customer_id;
	}
	public void setCustomer_id(int customer_id) {
		this.customer_id = customer_id;
	}
	public String getPurchase_date() {
		return purchase_date;
	}
	public void setPurchase_date(String purchase_date) {
		this.purchase_date = purchase_date;
	}
	public double getTotal_amount() {
		return total_amount;
	}
	public void setTotal_amount(double total_amount) {
		this.total_amount = total_amount;
	}
	
	Scanner sc = new Scanner(System.in);
	
	public void RegisterPurchase(Purchases purchase) {
		System.out.print("\nId do Cliente: ");
		purchase.setCustomer_id(sc.nextInt());
		sc.nextLine();
		System.out.print("Data da compra (aaaa-mm-dd): ");
		purchase.setPurchase_date(sc.nextLine());
		
		String sql = "insert into purchases(customer_id, purchase_date, total_amount) values (?,?,?)";
		
		PreparedStatement ps = null;
		
		try {
			ps = MySqlConnection.getConnection().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
			ps.setInt(1, purchase.getCustomer_id());
			ps.setString(2, purchase.getPurchase_date());
			ps.setDouble(3, 0);
			
			ps.executeUpdate();
			
			ResultSet rs = ps.getGeneratedKeys();
		    int purchaseId = 0;
		    if (rs.next()) {
		        purchaseId = rs.getInt(1);
		    }
		    rs.close();
			ps.close();
			
			PurchaseItems pi = new PurchaseItems();
			pi.setPurchase_id(purchaseId);
			double total = pi.purchasedItems(pi);
			purchase.setTotal_amount(total);
			
			updateTotalAmount(purchase, purchaseId);
			System.out.printf("Valor total da compra: %.2f%n", purchase.getTotal_amount());
			Points pts = new Points();
			Customers c = new Customers();
			c.currentPoints(purchase.getCustomer_id(), purchase.getTotal_amount());
			pts.toAccumulatePoints(pts, purchase.getCustomer_id(), purchaseId, purchase.getTotal_amount());
			System.out.println("\nCompra registrada com sucesso!");
			
		} catch (SQLException e) {
			System.out.println("\nErro: Compra não foi registrada!\n");
			e.printStackTrace();
		}
		
	}
	
	public void updateTotalAmount(Purchases purchase, int purchaseId) {
	    String sql = "UPDATE purchases SET total_amount = ? WHERE purchase_id = ?";
	    PreparedStatement ps = null;

	    try {
	        ps = MySqlConnection.getConnection().prepareStatement(sql);
	        ps.setDouble(1, purchase.getTotal_amount()); 
	        ps.setInt(2, purchaseId);

	        ps.executeUpdate();

	        ps.close();
	    } catch (SQLException e) {
	        System.out.println("\nErro: Não foi possível atualizar o valor total da compra!\n");
	        e.printStackTrace();
	    }
	}
	
	public void purchasesTable() {
	    String sql = "SELECT p.purchase_id, p.customer_id, c.first_name, c.last_name, p.purchase_date, p.total_amount " +
	                 "FROM purchases p " +
	                 "JOIN customers c ON p.customer_id = c.customer_id";
	    
	    PreparedStatement ps = null;
	    
	    try {
	        ps = MySqlConnection.getConnection().prepareStatement(sql);
	        ResultSet rs = ps.executeQuery();
	        
	        System.out.println("\n===== Tabela de Compras =====");
	        System.out.printf("%-15s %-15s %-35s %-20s %-15s%n", "ID da Compra", "ID do Cliente", "Nome do Cliente", "Data da Compra", "Total da Compra");
	        System.out.println("----------------------------------------------------------------------------------------------------");
	        
	        while (rs.next()) {
	            int purchaseId = rs.getInt("purchase_id");
	            int customerId = rs.getInt("customer_id");
	            String firstName = rs.getString("first_name");
	            String lastName = rs.getString("last_name");
	            String customerName = firstName + " " + lastName;
	            String purchaseDate = rs.getString("purchase_date");
	            double totalAmount = rs.getDouble("total_amount");
	            
	            System.out.printf("%-15d %-15d %-35s %-20s %-15.2f%n", purchaseId, customerId, customerName, purchaseDate.toString(), totalAmount);
	        }
	        
	        rs.close();
	        ps.close();
	    } catch (SQLException e) {
	        System.out.println("\nErro ao buscar a tabela de compras!");
	        e.printStackTrace();
	    }
	}

	
	public void purchasesBetweenDate(){
		String sql = "select c.customer_id, c.first_name, c.last_name, pu.purchase_id, pu.purchase_date, pu.total_amount "
				+ "from purchases pu inner join customers c on pu.customer_id = c.customer_id "
				+ "where pu.purchase_date between ? AND ? order by pu.purchase_date asc";
		
		PreparedStatement ps = null;
		
		try {
			ps = MySqlConnection.getConnection().prepareStatement(sql);
			System.out.print("Data inicial (aaaa-mm-dd): ");
			String iDate = sc.nextLine();
			System.out.print("Data final (aaaa-mm-dd): ");
			String fDate = sc.nextLine();
			ps.setString(1, iDate);
			ps.setString(2, fDate);
			
			ResultSet rs = ps.executeQuery(); 
	        
			System.out.println("\n===== Compras realizadas dentro de um intervalo de datas =====");
			System.out.printf("%-15s %-35s %-15s %-20s %-20s%n", "ID do Cliente", "Nome do Cliente", "ID da Compra", "Data da Compra", "Valor Total da Compra");
	        System.out.println("----------------------------------------------------------------------------------------------------------");
	        while (rs.next()) {
	        	int id = rs.getInt("customer_id");
	        	String first_name = rs.getString("first_name");
	            String last_name = rs.getString("last_name");
	            String name = first_name + " " + last_name;
	            int pId = rs.getInt("purchase_id");
	            String date = rs.getString("purchase_date");
	            double total = rs.getDouble("total_amount");
	            System.out.printf("%-15d %-35s %-15d %-20s %-20.2f%n", id, name, pId, date, total);
	        }
	        ps.close();
	        rs.close();
		} catch (SQLException e) {
			System.out.println("\nErro ao buscar a tabela!");
	        e.printStackTrace();
		}
	}

}
