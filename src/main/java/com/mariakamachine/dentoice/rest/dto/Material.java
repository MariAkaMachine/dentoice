package com.mariakamachine.dentoice.rest.dto;

import com.mariakamachine.dentoice.util.validation.Numeric;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Data
public class Material {

    @Numeric
    private String position;
    @NotBlank
    @Length(max = 50)
    private String name;
    private String notes;
    @Min(0)
    private Double quantity;
    @Min(0)
    private Double pricePerUnit;
    @NotNull
    private Boolean isMetal;
    @NotNull
    private Boolean isPrivate = false;

}
