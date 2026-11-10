package com.bryandev.transfer_service.model.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransferRequestDto {
    @NotBlank(message = "La cuenta de origen no puede estar vacia")
    @Size(min = 10, max = 20, message = "La cuenta de origen debe tener entre 10 y 20 caracteres")
    private String sourceAccountNumber;

    @NotBlank(message = "La cuenta de destino no puede estar vacia")
    @Size(min = 10, max = 20, message = "La cuenta de destino debe tener entre 10 y  20 caracteres")
    private String targetAccountNumber;

    @NotNull(message = "El monto de la transferencia es obligatorio")
    @DecimalMin(value = "0.01",message = "El monto de la transferencia debe ser mayor a cero")
    private BigDecimal amount;
}
