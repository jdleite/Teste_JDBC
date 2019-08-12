package com.rock;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class ClienteDAO {
	static String url = "jdbc:oracle:thin:@localhost:1521:XE";
	static String usuario = "CURSO_JAVA";
	static String senha = "schema";
	static Connection conexao;

	public static void main(String[] args) {

		try {
			conectar();
			Scanner entrada = new Scanner(System.in);
			int opcao = 0;
			long cpf = 0;
			String nome, email;

			while (opcao != 6) {
				System.out.println("Sistema de Gerenciamento de Cliente");
				System.out.println("======================================");
				System.out.println("Digite [1] para Consultar Todos os Clientes");
				System.out.println("Digite [2] para Consultar um Cliente Específico");
				System.out.println("Digite [3] para Cadastrar um Novo Cliente");
				System.out.println("Digite [4] para Alterar um Cliente");
				System.out.println("Digite [5] para Excluir um Cliente");
				System.out.println("Digite [6] para Sair do Sistema");
				System.out.println("==========================================");
				opcao = entrada.nextInt();

				switch (opcao) {
				case 1: {
					System.out.println("[1] Consultar Todos");
					List<Cliente> clienteList = ClienteDAO.consultarTodos();
					clienteList.forEach(System.out::println);
					System.out.println("Total de clientes " + clienteList.size());
					break;
				}
				case 2: {
					System.out.println("[2] Consultar um Cliente Específico");
					System.out.println("Informe o CPF");
					cpf = entrada.nextLong();
					Cliente cliente = ClienteDAO.consultar(cpf);
					System.out.println(cliente);
					break;

				}
				case 3: {
					System.out.println("[3] Cadastrar um Novo Cliente");
					System.out.println("Informe o CPF");
					cpf = entrada.nextLong();
					entrada.nextLine();
					System.out.println("Favor informar o Nome >>>");
					nome = entrada.nextLine();
					System.out.println("Favor informar o Email >>>");
					email = entrada.nextLine();
					Cliente cliente = new Cliente(cpf, nome, email);
					ClienteDAO.inserir(cliente);
					break;

				}
				case 4: {
					System.out.println("[4] Alterar um Cliente");
					System.out.println("Informe o CPF");
					cpf = entrada.nextLong();
					entrada.nextLine();
					System.out.println("Favor informar o Nome >>>");
					nome = entrada.nextLine();
					System.out.println("Favor informar o Email >>>");
					email = entrada.nextLine();
					Cliente cliente = new Cliente(cpf, nome, email);
					ClienteDAO.alterar(cliente);
					break;

				}
				case 5: {
					System.out.println("[4] Excluir um Cliente");
					System.out.println("Informe o CPF");
					cpf = entrada.nextLong();
					ClienteDAO.excluir(cpf);
					break;

				}
				case 6: {
					System.out.println("Encerrando o sistema");
					break;
				}
				}

			}
			entrada.close();
			desconectar();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public static void conectar() throws SQLException {

		conexao = DriverManager.getConnection(url, usuario, senha);
		conexao.setAutoCommit(false);
	}

	public static void desconectar() throws SQLException {

		conexao.close();
	}

	public static void inserir(Cliente cliente) throws SQLException {

		String sql = "insert into Cliente values(" + cliente.getCpf() + ",'" + cliente.getNome() + "','"
				+ cliente.getEmail() + "')";
		Statement statement = conexao.createStatement();
		statement.execute(sql);
		conexao.commit();

	}

	public static void inserirPS(long cpf, String nome, String email) throws SQLException {

		String sql = "insert into Cliente values(?,?,?)";
		PreparedStatement prepared = conexao.prepareStatement(sql);
		prepared.setLong(1, cpf);
		prepared.setString(2, nome);
		prepared.setString(3, email);
		prepared.executeUpdate();
		conexao.commit();

	}

	public static void inserirSP(long cpf, String nome, String email) throws SQLException {

		String sql = "{call sp_inserircliente(?,?,?)}";
		CallableStatement cls = conexao.prepareCall(sql);
		cls.setLong(1, cpf);
		cls.setString(2, nome);
		cls.setString(3, email);
		cls.execute();
		conexao.commit();

	}

	public static Cliente consultar(long cpf) throws SQLException {

		String sql = "select * from Cliente where cpf=" + cpf + "";
		Statement statement = conexao.createStatement();
		ResultSet rs = statement.executeQuery(sql);
		Cliente cliente = null;

		while (rs.next()) {
			cliente = new Cliente(rs.getLong(1), rs.getString(2), rs.getString(3));

		}
		return cliente;

	}

	public static List<Cliente> consultarTodos() throws SQLException {

		String sql = "select * from Cliente";
		Statement statement = conexao.createStatement();
		ResultSet rs = statement.executeQuery(sql);
		List<Cliente> clienteList = new LinkedList<Cliente>();

		while (rs.next()) {
			Cliente cliente = new Cliente(rs.getLong(1), rs.getString(2), rs.getString(3));
			clienteList.add(cliente);
		}

		return clienteList;

	}

	public static void alterar(Cliente cliente) throws SQLException {

		String sql = "update Cliente set nome ='" + cliente.getNome() + "',email ='" + cliente.getEmail()
				+ "' where cpf=" + cliente.getCpf();
		Statement statement = conexao.createStatement();
		statement.executeUpdate(sql);
		conexao.commit();

	}

	public static void excluir(long cpf) throws SQLException {
		String sql = "delete from Cliente where  cpf=" + cpf;
		Statement statement = conexao.createStatement();
		statement.executeUpdate(sql);
		conexao.commit();

	}

}
