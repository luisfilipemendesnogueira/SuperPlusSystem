package entities;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

import connection.MySqlConnection;

public class ExchangeProducts {
	private int redeemable_product_id;
	private int customer_id;
	private String exchange_date;
	
	
	public int getRedeemable_product_id() {
		return redeemable_product_id;
	}

	public void setRedeemable_product_id(int redeemable_product_id) {
		this.redeemable_product_id = redeemable_product_id;
	}

	public int getCustomer_id() {
		return customer_id;
	}

	public void setCustomer_id(int customer_id) {
		this.customer_id = customer_id;
	}

	public String getExchange_date() {
		return exchange_date;
	}

	public void setExchange_date(String exchange_date) {
		this.exchange_date = exchange_date;
	}
	
	Scanner sc = new Scanner(System.in);

	public void exchangeProducts(ExchangeProducts ep, int costumerId, int rpId) {
		ep.setRedeemable_product_id(rpId);
		ep.setCustomer_id(costumerId);
		System.out.print("Data do resgate (aaaa-mm-dd): ");
		ep.setExchange_date(sc.nextLine());
		String sql = "insert into exchange_products(redeemable_product_id, customer_id, exchange_date) values (?,?,?)";
		
		PreparedStatement ps = null;
		
		try {
			ps = MySqlConnection.getConnection().prepareStatement(sql);
			ps.setInt(1, ep.getRedeemable_product_id());
			ps.setInt(2, ep.getCustomer_id());
			ps.setString(3, ep.getExchange_date());
			System.out.println("\nTroca realizada com sucesso!");
			
			ps.execute();
			ps.close();
			
		} catch (SQLException e) {
			System.out.println("\nErro: Troca não realizada!");
			e.printStackTrace();
		}
	}
}
