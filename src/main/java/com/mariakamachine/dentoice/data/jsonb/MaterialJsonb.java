package com.mariakamachine.dentoice.data.jsonb;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mariakamachine.dentoice.data.entity.MaterialEntity;
import com.mariakamachine.dentoice.rest.dto.Material;
import com.mariakamachine.dentoice.util.validation.Numeric;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static org.apache.commons.lang3.StringUtils.isBlank;

@NoArgsConstructor
@Data
@JsonInclude(NON_EMPTY)
public class MaterialJsonb implements Serializable {

    @NotBlank
    @Numeric
    private String position;
    @NotBlank
    private String name;
    private String notes;
    @NotNull
    @Min(0)
    private Double quantity;
    @NotNull
    @Min(0)
    private Double pricePerUnit;
    private Boolean isMetal;

    public MaterialJsonb(Material material) {
        this.position = material.getPosition();
        this.name = material.getName();
        this.notes = material.getNotes();
        this.quantity = material.getQuantity();
        this.pricePerUnit = material.getPricePerUnit();
        this.isMetal = material.getIsMetal();
    }

    public MaterialJsonb(Material material, MaterialEntity entity) {
        this.position = material.getPosition();
        this.name = isBlank(material.getName()) ? entity.getName() : material.getName();
        this.notes = isBlank(material.getNotes()) ? null : material.getNotes();
        this.quantity = material.getQuantity();
        this.pricePerUnit = material.getPricePerUnit() == null ? entity.getPricePerUnit() : material.getPricePerUnit();
        this.isMetal = material.getIsMetal();
    }

}
