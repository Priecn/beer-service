package learn.cloud.beerservice.web.mapper;

import learn.cloud.beerservice.domain.Beer;
import learn.cloud.beerservice.web.model.BeerDto;
import org.mapstruct.Mapper;

@Mapper(uses = {DateMapper.class})
public interface BeerMapper {

    BeerDto beerToBeerDto(Beer beer);

    Beer beerDtoToBeer(BeerDto dto);
}
