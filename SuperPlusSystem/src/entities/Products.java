package entities;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import connection.MySqlConnection;

public class Products {
	private double price;
	
	public double getPrice(int productId) {
		String sql = "SELECT price FROM products WHERE product_id = ?";
		PreparedStatement ps = null;
		
		try {
			ps = MySqlConnection.getConnection().prepareStatement(sql);
			ps.setInt(1, productId);
			
			ResultSet rs = ps.executeQuery(); 
            
            if (rs.next()) {
                price = rs.getDouble("price");
            }
            ps.close();
            rs.close();
		} catch (SQLException e) {
			System.out.println("Erro ao buscar o preço do produto!");
			e.printStackTrace();
		}
		return price;
	}
	
	public boolean verifyAvailability(int productId, int quantitySold) {
		int stockUnits = 0;
		String sql = "SELECT units_in_stock FROM products WHERE product_id = ?";
		PreparedStatement ps = null;
		
		try {
			ps = MySqlConnection.getConnection().prepareStatement(sql);
			ps.setInt(1, productId);
			
			ResultSet rs = ps.executeQuery(); 
            
            if (rs.next()) {
                stockUnits = rs.getInt("units_in_stock");
            }
            ps.close();
            rs.close();
		} catch (SQLException e) {
			System.out.println("Erro ao buscar o estoque do produto!");
			e.printStackTrace();
		}
		
		if(stockUnits <= 0 || stockUnits < quantitySold || quantitySold <= 0) {
			System.out.println("Estoque vazio ou quantidade pedida inválida");
			return false;
		}
		return true;
	}
	
	public void updateStock(int productId, int quantitySold) {
		String sql = "UPDATE products SET units_in_stock = units_in_stock - ? WHERE product_id = ?";
		PreparedStatement ps = null;
		
		try {
			ps = MySqlConnection.getConnection().prepareStatement(sql);
			ps.setInt(1, quantitySold);
			ps.setInt(2, productId);
			
			int rowsAffected = ps.executeUpdate();
	        if (rowsAffected > 0) {
				System.out.println("\nEstoque atualizado!");
	        } else {
	            System.out.println("Erro: produto não encontrado ou quantidade inválida.");
	        }
			ps.close();
		} catch (SQLException e) {
			System.out.println("Erro: Estoque não foi atualizado!");
			e.printStackTrace();
		}
	}
	
	public void productsTable() {
	    String sql = "SELECT * FROM products";
	    PreparedStatement ps = null;
	    try {
	        ps = MySqlConnection.getConnection().prepareStatement(sql);
	        ResultSet rs = ps.executeQuery();
	        System.out.println("\n===== Tabela de Produtos =====");
	        System.out.printf("%-15s %-35s %-15s %-20s%n", "ID do Produto", "Nome do Produto", "Preço", "Unidades em Estoque");
	        System.out.println("--------------------------------------------------------------------------------");
	        while (rs.next()) {
	            int productId = rs.getInt("product_id");
	            String name = rs.getString("name");
	            double price = rs.getDouble("price");
	            int unitsInStock = rs.getInt("units_in_stock");
	            System.out.printf("%-15d %-35s %-15.2f %-20d%n", productId, name, price, unitsInStock);
	        }
	        rs.close();
	        ps.close();
	    } catch (SQLException e) {
	        System.out.println("\nErro ao buscar a tabela de produtos!");
	        e.printStackTrace();
	    }
	}
	
	public void mostBoughtProducts() {
		String sql = "select p.product_id, p.name, sum(pi.quantity) as total_sold from products p "
				+ "inner join purchase_items pi on p.product_id = pi.product_id group by p.product_id, p.name "
				+ "order by total_sold desc";
		
		PreparedStatement ps = null;
		
		try {
			ps = MySqlConnection.getConnection().prepareStatement(sql);
			
			ResultSet rs = ps.executeQuery(); 
            
			System.out.println("\n===== Produtos mais comprados =====");
			System.out.printf("%-15s %-35s %-20s%n", "ID do Produto", "Nome do Produto", "Quantidades vendidas");
            System.out.println("---------------------------------------------------------------");
            while (rs.next()) {
            	int id = rs.getInt("product_id");
                String name = rs.getString("name");
                int total = rs.getInt("total_sold");
                System.out.printf("%-15d %-35s %-20d%n", id, name, total);
            }
            ps.close();
            rs.close();
		} catch (SQLException e) {
			System.out.println("\nErro ao buscar a tabela!");
            e.printStackTrace();
		}
	}
	
	public void productsStock() {
		String sql = "select p.product_id, p.name, p.units_in_stock from products p order by p.units_in_stock asc";
		
		PreparedStatement ps = null;
		
		try {
			ps = MySqlConnection.getConnection().prepareStatement(sql);
			
			ResultSet rs = ps.executeQuery(); 
            
			System.out.println("\n===== Estoque atual de produtos =====");
			System.out.printf("%-15s %-35s %-20s%n", "ID do Produto", "Nome do Produto", "Quantidades no Estoque");
            System.out.println("---------------------------------------------------------------");
            while (rs.next()) {
            	int id = rs.getInt("product_id");
                String name = rs.getString("name");
                int stock = rs.getInt("units_in_stock");
                System.out.printf("%-15d %-35s %-20d%n", id, name, stock);
            }
            ps.close();
            rs.close();
		} catch (SQLException e) {
			System.out.println("\nErro ao buscar a tabela!");
            e.printStackTrace();
		}
	}
}
