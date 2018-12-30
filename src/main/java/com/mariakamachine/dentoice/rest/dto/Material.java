package com.mariakamachine.dentoice.rest.dto;

import com.mariakamachine.dentoice.util.validation.Numeric;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Data
public class Material {

    @Numeric
    private String position;
    @NotBlank
    private String name;
    private String notes;
    @Min(0)
    private Double quantity;
    @Min(0)
    private Double pricePerUnit;
    @NotNull
    private Boolean isMetal;

}
