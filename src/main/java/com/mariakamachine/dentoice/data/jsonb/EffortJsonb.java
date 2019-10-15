package com.mariakamachine.dentoice.data.jsonb;

import com.mariakamachine.dentoice.data.entity.EffortEntity;
import com.mariakamachine.dentoice.rest.dto.Effort;
import com.mariakamachine.dentoice.util.validation.Numeric;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

import static org.apache.commons.lang3.StringUtils.isBlank;

@NoArgsConstructor
@Data
public class EffortJsonb implements Serializable {

    @NotBlank
    @Numeric
    private String position;
    @NotBlank
    private String name;
    @NotNull
    @Min(0)
    private Double quantity;
    @NotNull
    @Min(0)
    private Double pricePerUnit;
    private Boolean isPrivate;

    public EffortJsonb(Effort effort) {
        this.position = effort.getPosition();
        this.name = effort.getName();
        this.quantity = effort.getQuantity();
        this.pricePerUnit = effort.getPricePerUnit();
        this.isPrivate = effort.getIsPrivate();
    }

    public EffortJsonb(Effort effort, EffortEntity entity) {
        this.position = effort.getPosition();
        this.name = isBlank(effort.getName()) ? entity.getName() : effort.getName();
        this.quantity = effort.getQuantity();
        this.pricePerUnit = effort.getPricePerUnit() == null ? entity.getPricePerUnit() : effort.getPricePerUnit();
        this.isPrivate = effort.getIsPrivate();
    }

}
