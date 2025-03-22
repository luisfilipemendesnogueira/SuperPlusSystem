package entities;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import connection.MySqlConnection;

public class Customers {
	private String first_name;
	private String last_name;
	private String cpf;
	private String phone_number;
	private double current_points;
	
	public String getFirst_name() {
		return first_name;
	}
	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}
	public String getLast_name() {
		return last_name;
	}
	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}
	public String getCpf() {
		return cpf;
	}
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	public String getPhone_number() {
		return phone_number;
	}
	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}
	
	public double getCurrent_points(int customerId) {
		String sql = "SELECT current_points FROM customers WHERE customer_id = ?";
		PreparedStatement ps = null;
		
		try {
			ps = MySqlConnection.getConnection().prepareStatement(sql);
			ps.setInt(1, customerId);
			
			ResultSet rs = ps.executeQuery(); 
            
            if (rs.next()) {
            	current_points = rs.getDouble("current_points");
            }
            ps.close();
            rs.close();
		} catch (SQLException e) {
			System.out.println("Erro ao buscar os pontos atuais do cliente!");
			e.printStackTrace();
		}
		return current_points;
	}

	public void setCurrent_points(double current_points) {
		this.current_points = current_points;
	}
	
	Scanner sc = new Scanner(System.in);

	public void currentPoints(int customerId, double totalAmount) {
		double totalPts = 0.0;
		totalPts = totalAmount / 10.0;
		
		String sql = "UPDATE customers SET current_points = current_points + ? WHERE customer_id = ?";
		PreparedStatement ps = null;
		
		try {
			ps = MySqlConnection.getConnection().prepareStatement(sql);
			ps.setDouble(1, totalPts);
			ps.setInt(2, customerId);
			
			int rowsAffected = ps.executeUpdate();
	        if (rowsAffected > 0) {
				System.out.println("\nPontuação atual atualizada!");
	        } else {
	            System.out.println("Erro: Pontuação atual não foi atualizada.");
	        }
			ps.close();
		} catch (SQLException e) {
			System.out.println("Erro: Pontuação atual não foi atualizada!");
			e.printStackTrace();
		}
	}
	
	public void updateCurrentPoints(double requiredPoints, int customerId) {
		String sql = "UPDATE customers SET current_points = current_points - ? WHERE customer_id = ?";
		PreparedStatement ps = null;
		
		try {
			ps = MySqlConnection.getConnection().prepareStatement(sql);
			ps.setDouble(1, requiredPoints);
			ps.setInt(2, customerId);
			
			int rowsAffected = ps.executeUpdate();
	        if (rowsAffected > 0) {
				System.out.println("\nPontos totais atualizados!");
	        } else {
	            System.out.println("Erro: Pontos totais inválidos.");
	        }
			ps.close();
		} catch (SQLException e) {
			System.out.println("Erro: Pontos totais não foram atualizados!");
			e.printStackTrace();
		}
	}
	
	public void RegisterCustomer(Customers customer) {
		System.out.print("\nNome: ");
		customer.setFirst_name(sc.nextLine());
		System.out.print("Sobrenome: ");
		customer.setLast_name(sc.nextLine());
		System.out.print("CPF: ");
		customer.setCpf(sc.nextLine());
		System.out.print("Telefone: ");
		customer.setPhone_number(sc.nextLine());
		
		String sql = "insert into customers(first_name, last_name, cpf, phone_number, current_points) values (?,?,?,?,?)";
		
		PreparedStatement ps = null;
		
		try {
			ps = MySqlConnection.getConnection().prepareStatement(sql);
			ps.setString(1, customer.getFirst_name());
			ps.setString(2, customer.getLast_name());
			ps.setString(3, customer.getCpf());
			ps.setString(4, customer.getPhone_number());
			ps.setDouble(5, 0.0);
			System.out.println("Cliente adicionado com sucesso!");
			
			ps.execute();
			ps.close();
			
		} catch (SQLException e) {
			System.out.println("Erro: Cliente não foi adicionado!");
			e.printStackTrace();
		}
	}
	
	public void customersTable() {
		String sql = "select * from customers";
		
		PreparedStatement ps = null;
		
		try {
			ps = MySqlConnection.getConnection().prepareStatement(sql);
			
			ResultSet rs = ps.executeQuery(); 
            
			System.out.println("\n===== Tabela de Clientes Cadastrados =====");
			System.out.printf("%-15s %-35s %-20s %-20s %-15s%n", "ID do Cliente", "Nome do Cliente", "CPF", "Número de Telefone", "Pontos Disponíveis");
            System.out.println("---------------------------------------------------------------------------------------------------------------------");
            while (rs.next()) {
            	int id = rs.getInt("customer_id");
                String first_name = rs.getString("first_name");
                String last_name = rs.getString("last_name");
                String name = first_name + " " + last_name;
                String cpf = rs.getString("cpf");
                String phone = rs.getString("phone_number");
                double currentPts = rs.getDouble("current_points");
                System.out.printf("%-15d %-35s %-20s %-20s %-20.2f%n", id, name, cpf, phone, currentPts);
            }
            ps.close();
            rs.close();
		} catch (SQLException e) {
			System.out.println("\nErro ao buscar a tabela!");
            e.printStackTrace();
		}
	}
	
	public void customersSpending() {
		String sql = "select c.customer_id, c.first_name, c.last_name, sum(pts.accumulated_points * 10) as total_spent "
				+ "from customers c inner join points pts on c.customer_id = pts.customer_id "
				+ "group by c.customer_id, c.first_name, c.last_name order by total_spent desc";
		
		PreparedStatement ps = null;
		
		try {
			ps = MySqlConnection.getConnection().prepareStatement(sql);
			
			ResultSet rs = ps.executeQuery(); 
            
			System.out.println("\n===== Clientes com os maiores gastos =====");
			System.out.printf("%-15s %-35s %-20s%n", "ID do Cliente", "Nome do Cliente", "Total Gasto");
            System.out.println("---------------------------------------------------------------");
            while (rs.next()) {
            	int id = rs.getInt("customer_id");
                String first_name = rs.getString("first_name");
                String last_name = rs.getString("last_name");
                String name = first_name + " " + last_name;
                double total = rs.getDouble("total_spent");
                System.out.printf("%-15d %-35s %-20.2f%n", id, name, total);
            }
            ps.close();
            rs.close();
		} catch (SQLException e) {
			System.out.println("\nErro ao buscar a tabela!");
            e.printStackTrace();
		}
	}
	
	public void customersMostPoints() {
		String sql = "select c.customer_id, c.first_name, c.last_name, c.current_points from customers c "
				+ "inner join points pts on c.customer_id = pts.customer_id "
				+ "group by c.customer_id, c.first_name, c.last_name order by c.current_points desc";
		
		PreparedStatement ps = null;
		
		try {
			ps = MySqlConnection.getConnection().prepareStatement(sql);
			
			ResultSet rs = ps.executeQuery(); 
            
			System.out.println("\n===== Clientes com mais pontos disponíveis =====");
			System.out.printf("%-15s %-35s %-20s%n", "ID do Cliente", "Nome do Cliente", "Pontos disponíveis");
            System.out.println("---------------------------------------------------------------");
            while (rs.next()) {
            	int id = rs.getInt("customer_id");
                String first_name = rs.getString("first_name");
                String last_name = rs.getString("last_name");
                String name = first_name + " " + last_name;
                double total = rs.getDouble("current_points");
                System.out.printf("%-15d %-35s %-20.2f%n", id, name, total);
            }
            ps.close();
            rs.close();
		} catch (SQLException e) {
			System.out.println("\nErro ao buscar a tabela!");
            e.printStackTrace();
		}
	}
}
