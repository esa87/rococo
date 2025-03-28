package rococo.pageobject;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import rococo.model.PaintingJson;
import rococo.pageobject.component.Header;

import java.io.File;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class EditPaintingPage extends BasePage<EditPaintingPage> {
    private final SelenideElement photoInput = $("form input[type='file']");
    private final SelenideElement nameInput = $("input[name='title']");
    private final ElementsCollection artistsSelect = $$("select[name='authorId'] option");
    private final SelenideElement descriptionTextarea = $("textarea[name='description']");
    private final ElementsCollection museumSelect = $$("select[name='museumId'] option");
    private final SelenideElement cancelButton = $("div.text-right button[type='button']");
    private final SelenideElement saveButton = $("button[type='submit']");

    // Шаги заполнения данных
    @Step("Заполнить карточку картины: название '{paintingJson.title}', " +
            "фото: {paintingJson.content}, художник: {paintingJson.artist}, " +
            "описание: {paintingJson.description}, музей: {paintingJson.museum}")
    public EditPaintingPage addPainting(PaintingJson paintingJson) {
        nameInput.sendKeys(paintingJson.title());
        if (!paintingJson.content().equals("")) photoInput.sendKeys(new File(paintingJson.content()).getAbsolutePath());
        if (paintingJson.artist() != null) artistsSelect.get(0).click();
        descriptionTextarea.sendKeys(paintingJson.description());
        if (paintingJson.museum() != null) museumSelect.get(0).click();
        return this;
    }

    // Шаги редактирования полей
    @Step("Изменить название картины на '{titlePainting}'")
    public EditPaintingPage editTitlePainting(String titlePainting) {
        nameInput.clear();
        nameInput.sendKeys(titlePainting);
        return this;
    }

    @Step("Заменить фото картины на изображение из '{path}'")
    public EditPaintingPage editPhotoPainting(String path) {
        photoInput.sendKeys(new File(path).getAbsolutePath());
        return this;
    }

    @Step("Обновить описание картины: '{descriptionPainting}'")
    public EditPaintingPage editDescriptionPainting(String descriptionPainting) {
        descriptionTextarea.clear();
        descriptionTextarea.sendKeys(descriptionPainting);
        return this;
    }

    @Step("Изменить художника на второго в списке")
    public EditPaintingPage editArtistPainting() {
        artistsSelect.get(1).click();
        return this;
    }

    @Step("Изменить музей на четвертый в списке")
    public EditPaintingPage editMuseumPainting() {
        museumSelect.get(3).click();
        return this;
    }

    // Шаги сохранения
    @Step("Сохранить валидные данные картины с переходом в карточку")
    public PaintingCardPage saveData() {
        saveButton.click();
        return new PaintingCardPage();
    }

    @Step("Сохранить валидные данные картины с переходом в карточку")
    public ArtistCardPage saveDataForArtist() {
        saveButton.click();
        return new ArtistCardPage();
    }


    @Step("Попытаться сохранить некорректные данные (остаемся на форме)")
    public EditPaintingPage saveWrongData() {
        saveButton.click();
        return this;
    }

    @Step("Сохранить новую картину с переходом в галерею")
    public PaintingPage saveNewData() {
        saveButton.click();
        return new PaintingPage();
    }

    @Step("Отменить изменения без сохранения")
    public PaintingCardPage cancelSaveData() {
        cancelButton.click();
        return new PaintingCardPage();
    }

    // Валидационные шаги
    @Step("Проверить видимость кнопки сохранения")
    public EditPaintingPage checkButtonSaveIsVisible() {
        saveButton.should(visible);
        return this;
    }
}