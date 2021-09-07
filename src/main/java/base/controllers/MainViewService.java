package base.controllers;

import java.sql.SQLException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Validation;
import javax.validation.Validator;

import base.models.Veiculo;
import base.repositories.VeiculoRepository;

public abstract class MainViewService {

	private static final Validator VALIDADOR = Validation.buildDefaultValidatorFactory().getValidator();

	private static MODO_FORMULARIO form_mode = MODO_FORMULARIO.CRIANDO_REGISTRO;

	public static boolean salvar(String placa, String marca, String modelo, String data_fabricacao, String cor) {
		try {

			var veiculo = new Veiculo(placa, modelo, marca, cor, LocalDate.parse(data_fabricacao));
			var erros = new ArrayList<>(VALIDADOR.validate(veiculo));

			if (!erros.isEmpty()) {
				throw new RuntimeException(erros.get(0).getMessage());
			}

			var existe = VeiculoRepository.existe(placa);

			if (form_mode == MODO_FORMULARIO.CRIANDO_REGISTRO) {
				if (existe) {
					throw new RuntimeException("Placa já cadastrada");
				}
			} else if (form_mode == MODO_FORMULARIO.ALTERANDO_REGISTRO) {
				if (!existe) {
					throw new RuntimeException(
							"Não foi possível encontrar a placa informada, alterações não permitem alterar placas");
				}
			}

			if (form_mode == MODO_FORMULARIO.CRIANDO_REGISTRO) {
				VeiculoRepository.salvar(veiculo);
			} else if (form_mode == MODO_FORMULARIO.ALTERANDO_REGISTRO) {
				VeiculoRepository.alterar(veiculo);
			}

			return true;

		} catch (DateTimeException dte) {
			throw new RuntimeException("Formato de data inválida");
		} catch (SQLException sqle) {
			throw new RuntimeException("Erro: " + sqle, sqle);
		} catch (RuntimeException re) {
			throw new RuntimeException(re.getMessage(), re);
		} catch (Exception e) {
			throw new RuntimeException("Erro desconhecido ao salvar, revise os dados", e);
		}
	}

	public static Veiculo buscar(String placa) {

		if (placa == null) {
			throw new RuntimeException("Placa não pode ser nula");
		}
		if (placa.length() > 7 || placa.length() < 1) {
			throw new RuntimeException("Placa deve ter entre 1 e 7 caracteres");
		}

		try {
			var veiculo = VeiculoRepository.buscar(placa).orElse(null);

			if (veiculo == null) {
				throw new RuntimeException("Veículo não encontrado");
			}

			return veiculo;

		} catch (SQLException sqle) {
			throw new RuntimeException("Erro: " + sqle, sqle);
		} catch (RuntimeException re) {
			throw new RuntimeException(re.getMessage(), re);
		} catch (Exception e) {
			throw new RuntimeException("Erro desconhecido: " + e, e);
		}

	}

	public static boolean excluir(String placa) {

		if (placa == null) {
			throw new RuntimeException("Placa não pode ser nula");
		}
		if (placa.length() > 7 || placa.length() < 1) {
			throw new RuntimeException("Placa deve ter entre 1 e 7 caracteres");
		}

		try {
			var excluido = VeiculoRepository.excluir(placa);

			if (!excluido) {
				throw new RuntimeException("Veículo não encontrado");
			}
			return true;

		} catch (SQLException sqle) {
			throw new RuntimeException("Erro: " + sqle, sqle);
		} catch (RuntimeException re) {
			throw new RuntimeException(re.getMessage(), re);
		} catch (Exception e) {
			throw new RuntimeException("Erro desconhecido: " + e, e);
		}
	}

	public static List<Veiculo> buscarTodos() {

		try {
			var resultado = VeiculoRepository.buscarTodos();

			if (resultado.isEmpty()) {
				throw new RuntimeException("Nenhum veículo foi encontrado");
			}
			return resultado;

		} catch (SQLException sqle) {
			throw new RuntimeException("Erro: " + sqle, sqle);
		} catch (RuntimeException re) {
			throw new RuntimeException(re.getMessage(), re);
		} catch (Exception e) {
			throw new RuntimeException("Erro desconhecido: " + e, e);
		}
	}

	public static List<Veiculo> buscarTodos(String placa_filtro, String marca_filtro, String fabricacao_filtro) {

		if (placa_filtro == null && marca_filtro == null && fabricacao_filtro == null) {
			throw new RuntimeException("Busca por filtro requer ao menos 1 filtro");
		} else if (placa_filtro.isBlank() && marca_filtro.isBlank() && fabricacao_filtro.isBlank()) {
			throw new RuntimeException("Busca por filtro requer ao menos 1 filtro com algum valor");
		}

		try {
			var resultado = VeiculoRepository.buscarTodosFiltrado(placa_filtro, marca_filtro, fabricacao_filtro);

			if (resultado.isEmpty()) {
				throw new RuntimeException("Nenhum veículo foi encontrado com os filtros fornecidos");
			}
			return resultado;

		} catch (SQLException sqle) {
			throw new RuntimeException("Erro: " + sqle, sqle);
		} catch (RuntimeException re) {
			throw new RuntimeException(re.getMessage(), re);
		} catch (Exception e) {
			throw new RuntimeException("Erro desconhecido: " + e, e);
		}
	}

	public static void modo_criacao() {
		form_mode = MODO_FORMULARIO.CRIANDO_REGISTRO;
	}

	public static void modo_editacao() {
		form_mode = MODO_FORMULARIO.ALTERANDO_REGISTRO;
	}

	public static boolean criando_registro() {
		return form_mode == MODO_FORMULARIO.CRIANDO_REGISTRO;
	}

	public enum MODO_FORMULARIO {
		CRIANDO_REGISTRO, ALTERANDO_REGISTRO;
	}
}
