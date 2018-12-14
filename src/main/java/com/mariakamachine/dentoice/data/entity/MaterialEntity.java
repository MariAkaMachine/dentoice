package com.mariakamachine.dentoice.data.entity;

import com.mariakamachine.dentoice.rest.dto.Material;
import com.mariakamachine.dentoice.util.validation.Numeric;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity(name = "material")
@Table(name = "materials")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@Data
public class MaterialEntity implements Serializable {

    @NotBlank
    @Numeric
    @Id
    @Column(nullable = false, unique = true)
    private String position;
    @NotBlank
    private String name;
    @NotNull
    @Column(name = "price_per_unit")
    private Double pricePerUnit;

    public MaterialEntity(Material material) {
        this.position = material.getPosition();
        this.name = material.getName();
        this.pricePerUnit = material.getPricePerUnit();
    }

    public MaterialEntity updateEntity(Material material) {
        this.position = material.getPosition();
        this.name = material.getName();
        this.pricePerUnit = material.getPricePerUnit();
        return this;
    }

}
