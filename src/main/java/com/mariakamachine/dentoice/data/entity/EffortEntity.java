package com.mariakamachine.dentoice.data.entity;

import com.mariakamachine.dentoice.rest.dto.Effort;
import com.mariakamachine.dentoice.util.validation.Numeric;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity(name = "effort")
@Table(name = "efforts")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@Data
public class EffortEntity implements Serializable {

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

    public EffortEntity(Effort effort) {
        this.position = effort.getPosition();
        this.name = effort.getName();
        this.pricePerUnit = effort.getPricePerUnit();
    }

    public EffortEntity updateEntity(Effort effort) {
        this.position = effort.getPosition();
        this.name = effort.getName();
        this.pricePerUnit = effort.getPricePerUnit();
        return this;
    }

}
