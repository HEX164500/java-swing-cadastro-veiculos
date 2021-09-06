package base.repositories;

import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import base.data.Database;
import base.models.Veiculo;

public interface VeiculoRepository {
	
	static final String INSERT = "INSERT INTO veiculos (marca, modelo, placa, cor, fabricacao) VALUES (?, ?, ?, ?, ?);";
	static final String DELETE = "DELETE FROM veiculos WHERE placa = ?;";
	static final String UPDATE = "UPDATE veiculos SET marca = ?, modelo = ?, cor = ?, fabricacao = ? WHERE placa = ?;";
	static final String SELECT = "SELECT marca, modelo, cor, fabricacao FROM veiculos WHERE placa = ?;";
	static final String SELECT_ALL = "SELECT placa, marca, modelo, cor, fabricacao FROM veiculos;";
	static final String EXISTS = "SELECT CASE WHEN COUNT(placa) <> 0 THEN true ELSE false END as r FROM veiculos WHERE placa = ?;";
	static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	static Optional<Veiculo> salvar(Veiculo v) throws SQLException {

		try (var stmt = Database.getConnection().prepareStatement(INSERT)) {

			stmt.setString(1, v.getMarca());
			stmt.setString(2, v.getModelo());
			stmt.setString(3, v.getPlaca());
			stmt.setString(4, v.getCor());

			var millis = v.getFabricacao().atStartOfDay(ZoneId.of("GMT-3")).toInstant().toEpochMilli();
			stmt.setDate(5, new java.sql.Date(millis));

			stmt.executeUpdate();

			return Optional.of(v);
		}
	}

	static Optional<Veiculo> alterar(Veiculo v) throws SQLException {

		try (var stmt = Database.getConnection().prepareStatement(UPDATE)) {

			stmt.setString(1, v.getMarca());
			stmt.setString(2, v.getModelo());
			stmt.setString(4, v.getCor());
			stmt.setString(5, v.getFabricacao().format(DATE_FORMATTER));

			stmt.executeUpdate();

			return Optional.of(v);
		} catch (Exception e) {
			return Optional.empty();
		}
	}

	static boolean excluir(String placa) throws SQLException {

		try (var stmt = Database.getConnection().prepareStatement(DELETE)) {

			stmt.setString(1, placa);

			return stmt.executeUpdate() != 0;
		}
	}

	static Optional<Veiculo> buscar(String placa) throws SQLException {

		try (var stmt = Database.getConnection().prepareStatement(SELECT)) {

			stmt.setString(1, placa);

			try (var result = stmt.executeQuery()) {
				if(!result.next()) {
					return Optional.empty();
				}

				var marca = result.getString("marca");
				var modelo = result.getString("modelo");
				var cor = result.getString("cor");

				var time_millis = result.getDate("fabricacao").getTime();
				var fabricacao = LocalDate.ofInstant(Instant.ofEpochMilli(time_millis), ZoneId.of("GMT-3"));

				var v = new Veiculo(placa, modelo, marca, cor, fabricacao);

				return Optional.of(v);
			}
		}
	}

	static List<Veiculo> buscarTodos() throws SQLException {

		try (var stmt = Database.getConnection().prepareStatement(SELECT_ALL)) {

			try (var result = stmt.executeQuery()) {

				result.next();

				List<Veiculo> veiculos = new ArrayList<>();

				while (result.next()) {

					var placa = result.getString("placa");
					var marca = result.getString("marca");
					var modelo = result.getString("modelo");
					var cor = result.getString("cor");

					var time_millis = result.getDate("fabricacao").getTime();
					var fabricacao = LocalDate.ofInstant(Instant.ofEpochMilli(time_millis), ZoneId.of("GMT-3"));

					var v = new Veiculo(placa, modelo, marca, cor, fabricacao);

					veiculos.add(v);
				}

				return veiculos;
			}
		}
	}

	static boolean existe(String placa) throws SQLException {

		try (var stmt = Database.getConnection().prepareStatement(EXISTS)) {
			stmt.setString(1, placa);

			var result = stmt.executeQuery();

			result.next();

			return result.getBoolean("r");
		}
	}

	void make();
}

/**
 * create table if not exists veiculos ( placa varchar(7) PRIMARY KEY, marca
 * varchar(32) NOT NULL, modelo varchar(32) NOT NULL, cor varchar(32) NOT NULL,
 * fabricacao DATE NOT NULL )
 * 
 */
