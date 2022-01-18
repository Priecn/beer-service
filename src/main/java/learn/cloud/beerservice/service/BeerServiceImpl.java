package learn.cloud.beerservice.service;

import learn.cloud.beerservice.domain.Beer;
import learn.cloud.beerservice.exceptions.NotFoundException;
import learn.cloud.beerservice.repository.BeerRepository;
import learn.cloud.beerservice.web.mapper.BeerMapper;
import learn.cloud.beerservice.web.model.BeerDto;
import learn.cloud.beerservice.web.model.BeerPagedList;
import learn.cloud.beerservice.web.model.BeerStyleEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class BeerServiceImpl implements BeerService {

    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;

    @Override
    public UUID saveNewBeer(BeerDto beerDto) {
        return beerRepository.save(beerMapper.beerDtoToBeer(beerDto)).getId();
    }

    @Override
    public void updateBeer(UUID beerId, BeerDto beerDto) {
        Beer beer = beerRepository.findById(beerId).orElseThrow(NotFoundException::new);

        beer.setName(beerDto.getName());
        beer.setStyle(beerDto.getStyle());
        beer.setPrice(beerDto.getPrice());
        beer.setUpc(beerDto.getUpc());
    }

    @Cacheable(cacheNames = "beerCache", condition = "#showInventoryOnHand == false")
    @Override
    public BeerDto getById(UUID beerId, Boolean showInventoryOnHand) {
        log.info("Data for {} not present in cache, making method call", beerId);
        if (showInventoryOnHand){
            return beerMapper.beerToBeerDtoWithInventory(
                    beerRepository.findById(beerId)
                            .orElseThrow(NotFoundException::new));
        }
        return beerMapper.beerToBeerDto(
                beerRepository.findById(beerId)
                        .orElseThrow(NotFoundException::new));
    }

    @Cacheable(cacheNames = "beerUpcCache", condition = "#showInventoryOnHand == false")
    @Override
    public BeerDto getByUPC(String upc, Boolean showInventoryOnHand) {
        log.info("Data for {} not present in cache, making method call", upc);
        if (showInventoryOnHand){
            return beerMapper.beerToBeerDtoWithInventory(
                    beerRepository.findByUpc(upc)
                            .orElseThrow(NotFoundException::new));
        }
        return beerMapper.beerToBeerDto(
                beerRepository.findByUpc(upc)
                        .orElseThrow(NotFoundException::new));
    }

    @Cacheable(cacheNames = "beerListCache", condition = "#showInventoryOnHand == false")
    @Override
    public BeerPagedList listBeers(String beerName, BeerStyleEnum beerStyle, PageRequest pageRequest, Boolean showInventoryOnHand) {
        log.info("Beer list not present in cache, making method call");
        Page<Beer> beerPage;
        if (!StringUtils.isEmpty(beerName) && !StringUtils.isEmpty(beerStyle))
            beerPage = beerRepository.findAllByNameAndStyle(beerName, beerStyle, pageRequest);
        else if (!StringUtils.isEmpty(beerName))
            beerPage = beerRepository.findAllByName(beerName, pageRequest);
        else if (!StringUtils.isEmpty(beerStyle))
            beerPage = beerRepository.findAllByStyle(beerStyle, pageRequest);
        else
            beerPage = beerRepository.findAll(pageRequest);

        List<BeerDto> data = null;

        if (showInventoryOnHand) {
            data = beerPage.getContent()
                    .stream()
                    .map(beerMapper::beerToBeerDtoWithInventory)
                    .collect(Collectors.toList());
        } else {
            data = beerPage.getContent()
                    .stream()
                    .map(beerMapper::beerToBeerDto)
                    .collect(Collectors.toList());
        }

        return new BeerPagedList(data,
                PageRequest.of(beerPage.getPageable().getPageNumber(),
                        beerPage.getPageable().getPageSize()),
                beerPage.getTotalPages()
        );
    }
}
