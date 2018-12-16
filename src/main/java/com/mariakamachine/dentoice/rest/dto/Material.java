package com.mariakamachine.dentoice.rest.dto;

import com.mariakamachine.dentoice.util.validation.Numeric;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@Data
public class Material {

    @Numeric
    String position;
    @NotBlank
    String name;
    String notes;
    @Min(0)
    Double quantity;
    @Min(0)
    Double pricePerUnit;
    boolean isMetal;

}
