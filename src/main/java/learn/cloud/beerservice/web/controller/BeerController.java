package learn.cloud.beerservice.web.controller;

import learn.cloud.beerservice.service.BeerService;
import learn.cloud.beerservice.web.model.BeerDto;
import lombok.RequiredArgsConstructor;
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

    final BeerService beerService;

    @GetMapping("/{beerId}")
    public ResponseEntity<BeerDto> getBeerById(@PathVariable UUID beerId) {
        return new ResponseEntity<>(beerService.getById(beerId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity createNewBeer(@RequestBody @Validated BeerDto beer) {
        UUID savedBeerId = beerService.saveNewBeer(beer);
        HttpHeaders headers = new HttpHeaders();
        headers.add("location", "/api/v1/beer/" + savedBeerId);
        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    @PutMapping("/{beerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity updateBeer(@PathVariable UUID beerId, @RequestBody @Validated BeerDto beer) {
        beerService.updateBeer(beerId, beer);
        HttpHeaders headers = new HttpHeaders();
        headers.add("location", "/api/v1/beer/" + beerId);
        return new ResponseEntity(headers, HttpStatus.NO_CONTENT);
    }
}
