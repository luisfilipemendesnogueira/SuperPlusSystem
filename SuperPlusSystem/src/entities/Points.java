package entities;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import connection.MySqlConnection;

public class Points {
	private int customer_id;
	private int purchase_id;
	private double accumulated_points;
	
	public int getCustomer_id() {
		return customer_id;
	}
	public void setCustomer_id(int customer_id) {
		this.customer_id = customer_id;
	}
	public int getPurchase_id() {
		return purchase_id;
	}
	public void setPurchase_id(int purchase_id) {
		this.purchase_id = purchase_id;
	}
	
	public double getAccumulated_points() {
		return accumulated_points;
	}
	
	public double getAccumulated_points(int customerId) {
		String sql = "SELECT sum(accumulated_points) as total_points FROM points WHERE customer_id = ?";
		PreparedStatement ps = null;
		
		try {
			ps = MySqlConnection.getConnection().prepareStatement(sql);
			ps.setInt(1, customerId);
			
			ResultSet rs = ps.executeQuery(); 
            
            if (rs.next()) {
            	accumulated_points = rs.getDouble("total_points");
            }
            ps.close();
            rs.close();
		} catch (SQLException e) {
			System.out.println("Erro ao buscar os pontos acumulados!");
			e.printStackTrace();
		}
		return accumulated_points;
	}
	
	public void setAccumulated_points(double accumulated_points) {
		this.accumulated_points = accumulated_points;
	}
	
	Scanner sc = new Scanner(System.in);
	
	public void toAccumulatePoints(Points pts, int customer_id, int purchase_id, double totalAmount) {
		pts.setCustomer_id(customer_id);
		pts.setPurchase_id(purchase_id);
		double totalPts = 0.0;
		totalPts = totalAmount / 10.0;
		pts.setAccumulated_points(totalPts);
		
		String sql = "insert into points(customer_id, purchase_id, accumulated_points) values (?,?,?)";
		
		PreparedStatement ps = null;
		
		try {
			ps = MySqlConnection.getConnection().prepareStatement(sql);
			ps.setInt(1, pts.getCustomer_id());
			ps.setInt(2, pts.getPurchase_id());
			ps.setDouble(3, pts.getAccumulated_points());
			System.out.println("\nPontos adicionados com sucesso!");
			
			ps.execute();
			ps.close();
			
		} catch (SQLException e) {
			System.out.println("\nErro: Pontos não foram adicionados!");
			e.printStackTrace();
		}
	}
	
	public void verifyPoints() {
		int costumerId = 0;
		System.out.print("\nDigite o ID do cliente que você deseja consultar o saldo de pontos: ");
		costumerId = sc.nextInt();
		sc.nextLine();
		
		String sql = "select c.customer_id, c.first_name, c.last_name, sum(pts.accumulated_points) as accumulated_points, c.current_points from customers c inner join points pts on c.customer_id = pts.customer_id where c.customer_id = ? group by c.customer_id, c.first_name, c.last_name, c.current_points";
		
		PreparedStatement ps = null;
		
		try {
			ps = MySqlConnection.getConnection().prepareStatement(sql);
			ps.setInt(1, costumerId);
			
			ResultSet rs = ps.executeQuery(); 
            
            if (rs.next()) {
            	System.out.println("\n===== Saldo de Pontos do Cliente =====");
                System.out.println("ID: " + rs.getInt("customer_id"));
                System.out.println("Nome: " + rs.getString("first_name") + " " + rs.getString("last_name"));
                System.out.println("Pontos totais acumulados: " + rs.getDouble("accumulated_points"));
                System.out.println("Pontos disponíveis: " + rs.getDouble("current_points"));
            } else {
            	System.out.println("\nCliente não encontrado ou sem pontos acumulados.");
            }
            ps.close();
            rs.close();
		} catch (SQLException e) {
			System.out.println("\nErro ao buscar os pontos do cliente!");
            e.printStackTrace();
		}
	}
}
