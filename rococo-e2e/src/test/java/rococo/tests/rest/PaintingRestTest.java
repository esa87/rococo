package rococo.tests.rest;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import rococo.api.GatewayApiClient;
import rococo.jupiter.annotation.ApiLogin;
import rococo.jupiter.annotation.Painting;
import rococo.jupiter.annotation.Token;
import rococo.jupiter.annotation.User;
import rococo.jupiter.annotation.meta.RestTest;
import rococo.model.PaintingJson;
import rococo.utils.RandomDataUtils;

import java.util.List;

@RestTest
@DisplayName("Тесты REST API для картин")
@Epic("Управление картинами")
public class PaintingRestTest {

    private final GatewayApiClient gatewayApiClient = new GatewayApiClient();

    @Description("Проверка дублирования после изменения названия")
    @DisplayName("Изменение названия картины не должно создавать дубликат")
    @Painting
    @User
    @ApiLogin(setupBrowser = false)
    @Test
    void checkDuplicateAfterChangeTitle(PaintingJson painting, @Token String token) {
        String title = RandomDataUtils.randomName();
        PaintingJson newPainting = new PaintingJson(
                painting.id(),
                title,
                painting.description(),
                painting.artist(),
                painting.museum(),
                painting.content()
        );
        gatewayApiClient.updatePainting(newPainting, token);

        List<PaintingJson> paintingJsonList = gatewayApiClient.getAllPaintings(title, 0, 10, null, token);

        Assertions.assertTrue(paintingJsonList.size()==1);
    }

    @Description("Проверка дублирования после изменения описания")
    @DisplayName("Изменение описания картины не должно создавать дубликат")
    @Painting
    @User
    @ApiLogin(setupBrowser = false)
    @Test
    void checkDuplicateAfterChangeDescription(PaintingJson painting, @Token String token) {

        String description = RandomDataUtils.randomSentence(5);
        PaintingJson newPainting = new PaintingJson(
                painting.id(),
                painting.title(),
                description,
                painting.artist(),
                painting.museum(),
                painting.content()
        );
        gatewayApiClient.updatePainting(newPainting, token);

        List<PaintingJson> paintingJsonList = gatewayApiClient.getAllPaintings(painting.title(), 0, 10, null, token);

        Assertions.assertTrue(paintingJsonList.size()==1);
    }
}
