package com.mariakamachine.dentoice.rest.dto;

import com.mariakamachine.dentoice.util.validation.Numeric;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;

@NoArgsConstructor
@Data
public class Effort {

    @Numeric
    String position;
    String name;
    @Min(0)
    Double quantity;
    @Min(0)
    Double pricePerUnit;

}
