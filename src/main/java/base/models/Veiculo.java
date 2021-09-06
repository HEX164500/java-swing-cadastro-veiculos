package base.models;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Veiculo {

	@NotBlank(message = "Placa não pode estar em branco")
	@Size(min = 1, max = 7, message = "Placa deve ter entre {min} e {max} caracteres")
	private String placa;

	@NotBlank(message = "Modelo não pode estar em branco")
	@Size(min = 1, max = 32, message = "Modelo deve ter entre {min} e {max} caracteres")
	private String modelo;

	@NotBlank(message = "Marca não pode estar em branco")
	@Size(min = 1, max = 32, message = "Marca deve ter entre {min} e {max} caracteres")
	private String marca;

	@NotBlank(message = "Cor não pode estar em branco")
	@Size(min = 1, max = 32, message = "Cor deve ter entre {min} e {max} caracteres")
	private String cor;

	@NotNull(message = "Ano de fabricação deve ser informado")
	@PastOrPresent(message = "Ano de fabricação não pode ser no futuro")
	private LocalDate fabricacao;
}
