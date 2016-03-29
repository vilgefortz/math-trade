package trade.math.service;

import trade.math.model.TradeBoardGame;
import trade.math.model.TradeBoardGameProperties;
import trade.math.model.TradeItem;

/**
 * Created by daniel on 11.03.16.
 */
public interface TradeBoardGamePropertiesService {

    TradeBoardGameProperties findByTradeItem(TradeItem tradeItem);

    TradeBoardGameProperties save(TradeBoardGameProperties tradeBoardGameProperties);

    boolean deleteByTradeItem(TradeItem tradeItem);

    void deleteAll();
}