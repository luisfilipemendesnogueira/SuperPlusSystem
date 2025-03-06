package entities;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import connection.MySqlConnection;

public class RedeemableProducts {
	private double requiredPoints;
	
	public double getRequiredPoints(int rpId) {
		String sql = "SELECT points_required FROM redeemable_products where redeemable_product_id = ?";
		PreparedStatement ps = null;
		
		try {
			ps = MySqlConnection.getConnection().prepareStatement(sql);
			ps.setInt(1, rpId);
			
			ResultSet rs = ps.executeQuery(); 
            
            if (rs.next()) {
            	requiredPoints = rs.getDouble("points_required");
            }
            ps.close();
            rs.close();
		} catch (SQLException e) {
			System.out.println("Erro ao buscar os pontos necessários para troca!");
			e.printStackTrace();
		}
		return requiredPoints;
	}
	
	Scanner sc = new Scanner(System.in);
	
	public void rpTable() {
		String sql = "select * from redeemable_products";
		
		PreparedStatement ps = null;
		
		try {
			ps = MySqlConnection.getConnection().prepareStatement(sql);
			
			ResultSet rs = ps.executeQuery(); 
            
			System.out.println("\n===== Tabela de Produtos Resgatáveis =====");
			System.out.printf("%-5s %-30s %-20s %-10s%n", "ID", "Produto", "Pontos necessários", "Estoque");
            System.out.println("---------------------------------------------------------------");
            while (rs.next()) {
            	int id = rs.getInt("redeemable_product_id");
                String name = rs.getString("name");
                double points = rs.getDouble("points_required");
                int stock = rs.getInt("units_in_stock");
                System.out.printf("%-5d %-30s %-20.2f %-10d%n", id, name, points, stock);
            }
            ps.close();
            rs.close();
		} catch (SQLException e) {
			System.out.println("\nErro ao buscar a tabela!");
            e.printStackTrace();
		}
	}
	
	public void exchangePoints() {
		this.rpTable();
        int customerId = 0;
        int rpId = 0;
		System.out.print("\nDigite o ID do cliente que deseja trocar os pontos por produtos: ");
		customerId = sc.nextInt();
		sc.nextLine();
		System.out.printf("\nDigite o ID do produto que será resgatado pelo cliente %d: ", customerId);
		rpId = sc.nextInt();
		sc.nextLine();
		
		Customers c = new Customers();
		ExchangeProducts ep = new ExchangeProducts();
		double currentPoints = c.getCurrent_points(customerId);
		if(currentPoints >= this.getRequiredPoints(rpId)) {
    		ep.exchangeProducts(ep, customerId, rpId);
    		this.updateStock(rpId);
    		c.updateCurrentPoints(this.getRequiredPoints(rpId), customerId);
		} else {
			System.out.println("\nPontos totais não são suficientes para troca.");
		}
	}
	
	public void updateStock(int rpId) {
		String sql = "UPDATE redeemable_products SET units_in_stock = units_in_stock - 1 WHERE redeemable_product_id = ?";
		PreparedStatement ps = null;
		
		try {
			ps = MySqlConnection.getConnection().prepareStatement(sql);
			ps.setInt(1, rpId);
			
			int rowsAffected = ps.executeUpdate();
	        if (rowsAffected > 0) {
				System.out.println("\nEstoque atualizado!");
	        } else {
	            System.out.println("Erro: produto não encontrado.");
	        }
			ps.close();
		} catch (SQLException e) {
			System.out.println("Erro: Estoque não foi atualizado!");
			e.printStackTrace();
		}
	}
	
	public void redeemedProducts(){
		String sql = "select c.customer_id, c.first_name, c.last_name, rp.name as exchanged_product_name, ep.redeemable_product_id as exchange_id "
				+ "from customers c inner join exchange_products ep on c.customer_id = ep.customer_id "
				+ "inner join redeemable_products rp on ep.redeemable_product_id = rp.redeemable_product_id";
		
		PreparedStatement ps = null;
		
		try {
			ps = MySqlConnection.getConnection().prepareStatement(sql);
			
			ResultSet rs = ps.executeQuery(); 
            
			System.out.println("\n===== Produtos já resgatados =====");
			System.out.printf("%-15s %-35s %-35s %-15s%n", "ID do Cliente", "Nome do Cliente", "Nome do Produto Resgatado", "Id do Produto");
            System.out.println("----------------------------------------------------------------------------------------------------------");
            while (rs.next()) {
            	int id = rs.getInt("customer_id");
            	String first_name = rs.getString("first_name");
                String last_name = rs.getString("last_name");
                String name = first_name + " " + last_name;
                String prName = rs.getString("exchanged_product_name");
                int prId = rs.getInt("exchange_id");
                System.out.printf("%-15d %-35s %-35s %-15d%n", id, name, prName, prId);
            }
            ps.close();
            rs.close();
		} catch (SQLException e) {
			System.out.println("\nErro ao buscar a tabela!");
            e.printStackTrace();
		}
	}
}
