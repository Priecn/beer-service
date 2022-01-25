package learn.cloud.common.events;

import learn.cloud.common.model.BeerDto;
import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BeerEvent implements Serializable {

    static final long serialVersionUID = -4093690036575379336L;
    private BeerDto beerDto;
}
