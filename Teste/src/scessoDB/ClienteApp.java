package scessoDB;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class ClienteApp {
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
					consultarTodos();
					break;
				}
				case 2: {
					System.out.println("[2] Consultar um Cliente Específico");
					System.out.println("Informe o CPF");
					cpf = entrada.nextLong();
					consultar(cpf);
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
					// inserir(cpf, nome, email);
					// inserirPS(cpf, nome, email);
					inserirSP(cpf, nome, email);
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
					alterar(cpf, nome, email);
					break;

				}
				case 5: {
					System.out.println("[4] Excluir um Cliente");
					System.out.println("Informe o CPF");
					cpf = entrada.nextLong();
					excluir(cpf);
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

	public static void inserir(long cpf, String nome, String email) throws SQLException {

		String sql = "insert into Cliente values(" + cpf + ",'" + nome + "','" + email + "')";
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

	public static void consultar(long cpf) throws SQLException {

		String sql = "select * from Cliente where cpf=" + cpf + "";
		Statement statement = conexao.createStatement();
		ResultSet rs = statement.executeQuery(sql);

		while (rs.next()) {
			System.out.println("cpf :" + rs.getInt(1) + " nome : " + rs.getString(2) + " email : " + rs.getString(3));

		}

	}

	public static void consultarTodos() throws SQLException {

		String sql = "select * from Cliente";
		Statement statement = conexao.createStatement();
		ResultSet rs = statement.executeQuery(sql);
		int contador = 0;

		while (rs.next()) {
			System.out.println("cpf :" + rs.getInt(1) + " nome : " + rs.getString(2) + " email : " + rs.getString(3));
			System.out.println("=================================================");
			contador++;
		}

		System.out.println("Numero de clientes listados :" + contador);

	}

	public static void alterar(long cpf, String nome, String email) throws SQLException {

		String sql = "update Cliente set nome ='" + nome + "',email ='" + email + "' where cpf=" + cpf;
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
