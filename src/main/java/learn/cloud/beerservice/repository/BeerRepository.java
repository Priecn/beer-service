package learn.cloud.beerservice.repository;

import learn.cloud.beerservice.domain.Beer;
import learn.cloud.beerservice.web.model.BeerStyleEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BeerRepository extends PagingAndSortingRepository<Beer, UUID> {
    Page<Beer> findAllByNameAndStyle(String name, BeerStyleEnum style, PageRequest pageRequest);
    Page<Beer> findAllByName(String name, PageRequest pageRequest);
    Page<Beer> findAllByStyle(BeerStyleEnum style, PageRequest pageRequest);
    Optional<Beer> findByUpc(String upc);
}
