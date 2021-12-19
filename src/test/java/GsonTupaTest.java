import by.epam.finaltask.model.*;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

public class GsonTupaTest {

    @Test
    public void test(){
        Map<LotWithImages, AuctionResult> map = new LinkedHashMap<>();
        map.put(new LotWithImages(Lot.builder().setAuctionStatus(AuctionStatus.ENDED).build(),
                new Images(1, new Images.Image(""))), new AuctionResult(BigDecimal.ONE, "vova", BigDecimal.TEN));
        map.put(new LotWithImages(Lot.builder().setAuctionStatus(AuctionStatus.ENDED).build(),
                new Images(1, new Images.Image(""))), null);
        long count = 2;
        Gson gson = new Gson().newBuilder().serializeNulls().create();
        Object[] array = new Object[2];
        array[0] = map;
        array[1] = count;
        String json = gson.toJson(array);
        System.out.println(json);
    }
}
