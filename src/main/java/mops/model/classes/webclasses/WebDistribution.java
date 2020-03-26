package mops.model.classes.webclasses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder(toBuilder = true)
public class WebDistribution {
    private String module;
    private String id;
    private String hours7;
    private String hours9;
    private String hours17;
    private List<WebDistributorApplicant> webDistributorApplicants;
}
