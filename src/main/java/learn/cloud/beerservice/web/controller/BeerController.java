package learn.cloud.beerservice.web.controller;

import learn.cloud.beerservice.service.BeerService;
import learn.cloud.common.model.BeerDto;
import learn.cloud.beerservice.web.model.BeerPagedList;
import learn.cloud.common.model.BeerStyleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/beer")
public class BeerController {

    private static final Integer DEFAULT_PAGE_NUMBER = 0;
    private static final Integer DEFAULT_PAGE_SIZE = 25;

    final BeerService beerService;

    @GetMapping("/{beerId}")
    public ResponseEntity<BeerDto> getBeerById(@PathVariable UUID beerId,
                                               @RequestParam(value = "showInventoryOnHand", required = false, defaultValue = "false") Boolean showInventoryOnHand) {
        return new ResponseEntity<>(beerService.getById(beerId, showInventoryOnHand), HttpStatus.OK);
    }

    @GetMapping("/upc/{upc}")
    public ResponseEntity<BeerDto> getBeerByUPC(@PathVariable String upc,
                                               @RequestParam(value = "showInventoryOnHand", required = false, defaultValue = "false") Boolean showInventoryOnHand) {
        return new ResponseEntity<>(beerService.getByUPC(upc, showInventoryOnHand), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<BeerPagedList> listBeers(@RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                                   @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                   @RequestParam(value = "beerName", required = false) String beerName,
                                                   @RequestParam(value = "beerStyle", required = false) BeerStyleEnum beerStyle,
                                                   @RequestParam(value = "showInventoryOnHand", required = false, defaultValue = "false") Boolean showInventoryOnHand) {

        if(pageNumber == null || pageNumber < 0) {
            pageNumber = DEFAULT_PAGE_NUMBER;
        }

        if(pageSize == null || pageSize < 0) {
            pageSize = DEFAULT_PAGE_SIZE;
        }

        BeerPagedList beerPagedList = beerService.listBeers(beerName, beerStyle, PageRequest.of(pageNumber, pageSize), showInventoryOnHand);

        return ResponseEntity.ok(beerPagedList);
    }
    @PostMapping
    public ResponseEntity<Void> createNewBeer(@RequestBody @Validated BeerDto beer) {
        UUID savedBeerId = beerService.saveNewBeer(beer);
        HttpHeaders headers = new HttpHeaders();
        headers.add("location", "/api/v1/beer/" + savedBeerId);
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @PutMapping("/{beerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> updateBeer(@PathVariable UUID beerId, @RequestBody @Validated BeerDto beer) {
        beerService.updateBeer(beerId, beer);
        HttpHeaders headers = new HttpHeaders();
        headers.add("location", "/api/v1/beer/" + beerId);
        return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT);
    }
}
