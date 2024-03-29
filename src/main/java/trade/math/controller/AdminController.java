package trade.math.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import trade.math.domain.tradeList.TradeList;
import trade.math.domain.tradeList.TradeListState;
import trade.math.model.dto.TradeBoardGameDTO;
import trade.math.service.TradeBoardGameService;
import trade.math.domain.tradeList.TradeListService;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private TradeBoardGameService tradeBoardGameService;

    @Autowired
    private TradeListService tradeListService;

    @RequestMapping(value = "/games", method = RequestMethod.POST)
    public ResponseEntity uploadGameList(@RequestBody List<TradeBoardGameDTO> tradeBoardGameDTOs) {
        tradeBoardGameService.save(tradeBoardGameDTOs);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/newList.command", method = RequestMethod.POST)
    public ResponseEntity createNewList() {
        TradeList list = tradeListService.createNewList();
        if (list == null)
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/closeList.command", method = RequestMethod.POST)
    public ResponseEntity closeList() {
        tradeListService.setState(TradeListState.CLOSED);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/openList.command", method = RequestMethod.POST)
    public ResponseEntity openList() {
        tradeListService.setState(TradeListState.OPEN);
        return new ResponseEntity(HttpStatus.OK);
    }

}
