package com.mariakamachine.dentoice.data.entity;

import com.mariakamachine.dentoice.data.jsonb.EffortJsonb;
import com.mariakamachine.dentoice.data.jsonb.MaterialJsonb;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CostWrapperEntity implements Serializable {

    @NotNull
    private List<EffortJsonb> efforts;
    @NotNull
    private List<MaterialJsonb> materials;

}
