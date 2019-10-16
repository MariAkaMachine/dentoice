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
public class Effort {

    @Numeric
    String position;
    @NotBlank
    @Length(max = 50)
    String name;
    @Min(0)
    Double quantity;
    @Min(0)
    Double pricePerUnit;
    @NotNull
    private Boolean isPrivate = false;

}
