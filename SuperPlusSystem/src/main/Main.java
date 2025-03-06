package main;

import java.util.Scanner;

import entities.Customers;
import entities.Points;
import entities.Products;
import entities.Purchases;
import entities.RedeemableProducts;

public class Main {
	public static void main(String[] args) {
		int option = 0;
		Scanner sc = new Scanner(System.in);
		
		while(option != 7) {
			System.out.println("\nDigite o número da operação que você deseja realizar!");
			System.out.println("1- Cadastrar Cliente");
			System.out.println("2- Registrar Compra");
			System.out.println("3- Verificar Saldo de Pontos");
			System.out.println("4- Resgatar Produtos");
			System.out.println("5- Consultas");
			System.out.println("6- Relatórios");
			System.out.println("7- Sair");
            System.out.print("Opção: ");
			option = sc.nextInt();
			sc.nextLine();
			
			Customers c = new Customers();
			Products pr = new Products();
			Purchases p = new Purchases();
			Points pts = new Points();
			RedeemableProducts rp = new RedeemableProducts();
			
			switch(option) {
				case 1:
					c.RegisterCustomer(c);
					break;
				case 2:
					p.RegisterPurchase(p);
					break;
				case 3:
					pts.verifyPoints();
					break;
				case 4:
					rp.exchangePoints();
					break;
				case 5:
					int choice1 = 0;
					while(choice1 != 5) {
						System.out.println("\nDigite o número da consulta que você deseja realizar!");
						System.out.println("1- Consultar Tabela de Clientes Cadastrados");
						System.out.println("2- Consultar Tabela de Produtos Para Venda");
						System.out.println("3- Consultar Tabela de Produtos Resgatáveis");
						System.out.println("4- Consultar Tabela de Compras");
						System.out.println("5- Sair");
			            System.out.print("Opção: ");
						choice1 = sc.nextInt();
						sc.nextLine();
						
						switch(choice1) {
							case 1:
								c.customersTable();
								break;
							case 2:
								pr.productsTable();
								break;
							case 3:
								rp.rpTable();
								break;
							case 4:
								p.purchasesTable();
								break;
							case 5:
								break;
							default:
								System.out.println("Erro: Tente novamente.\n");
								break;
						}
					}
					break;
				case 6:
					int choice2 = 0;
					while(choice2 != 7) {
						System.out.println("\nDigite o número do relatório que você deseja realizar!");
						System.out.println("1- Clientes com os maiores gastos");
						System.out.println("2- Produtos já resgatados");
						System.out.println("3- Clientes com mais pontos disponíveis");
						System.out.println("4- Produtos mais comprados");
						System.out.println("5- Compras realizadas dentro de um intervalo de datas");
						System.out.println("6- Estoque atual de produtos");
						System.out.println("7- Sair");
			            System.out.print("Opção: ");
						choice2 = sc.nextInt();
						sc.nextLine();
						
						switch(choice2) {
							case 1:
								c.customersSpending();
								break;
							case 2:
								rp.redeemedProducts();
								break;
							case 3:
								c.customersMostPoints();
								break;
							case 4:
								pr.mostBoughtProducts();
								break;
							case 5:
								p.purchasesBetweenDate();
								break;
							case 6:
								pr.productsStock();
								break;
							case 7:
								break;
							default:
								System.out.println("Erro: Tente novamente.\n");
								break;
						}
					}
					break;
				case 7:
					break;
				default:
					System.out.println("Erro: Tente novamente.\n");
					break;
			}
		}
		sc.close();
	}
}
